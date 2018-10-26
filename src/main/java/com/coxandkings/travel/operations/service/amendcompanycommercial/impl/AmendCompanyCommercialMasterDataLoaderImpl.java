package com.coxandkings.travel.operations.service.amendcompanycommercial.impl;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.resource.amendentitycommercial.AmendCommercialMetaData;
import com.coxandkings.travel.operations.resource.amendentitycommercial.CommercialHead;
import com.coxandkings.travel.operations.resource.amendentitycommercial.CommercialResource;
import com.coxandkings.travel.operations.service.amendcompanycommercial.AmendCompanyCommercialMasterDataLoader;
import com.coxandkings.travel.operations.service.amendcompanycommercial.AmendCompanyCommercialService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.MarginCalculatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AmendCompanyCommercialMasterDataLoaderImpl implements AmendCompanyCommercialMasterDataLoader {

	@Autowired
	private OpsBookingService opsBookingService;
	@Autowired
	private AmendCompanyCommercialService amendCompanyCommercialService;
    @Autowired
    private MarginCalculatorUtil marginCalculator;

	@Override
    public AmendCommercialMetaData getScreenMetaData(String bookingId, String orderId, String uniqueId)
            throws OperationException {
        OpsBooking currentBooking = null;

        currentBooking = opsBookingService.getBooking(bookingId);

		if (currentBooking == null) {
            throw new OperationException(Constants.BOOKING_NOT_FOUND, bookingId);
		}
		List<OpsProduct> opsProducts = currentBooking.getProducts();
        Optional<OpsProduct> optOpsProduct = opsProducts.parallelStream().filter(product -> product.getOrderID().equals(orderId)).findAny();
        if (!optOpsProduct.isPresent()) {
            throw new OperationException(Constants.ORDER_NOT_FOUND, orderId, bookingId);
        }

        OpsProduct opsProduct = optOpsProduct.get();
        OpsOrderStatus orderStatus=opsProduct.getOrderDetails().getOpsOrderStatus();
        if (orderStatus.getProductStatus().equalsIgnoreCase(OpsOrderStatus.VCH.getProductStatus()) || orderStatus.getProductStatus().equalsIgnoreCase(OpsOrderStatus.TKD.getProductStatus())) {
            throw new OperationException(Constants.CANNOT_AMEND_COMMERCIALS, orderStatus.getProductStatus());
        }
        
        AmendCommercialMetaData screenMetaData = null;

        OpsProductSubCategory opsProductSubCategory = opsProduct.getOpsProductSubCategory();
				OpsOrderDetails opsOrder = opsProduct.getOrderDetails();
        screenMetaData = new AmendCommercialMetaData();
				screenMetaData.setBookId(bookingId);
				screenMetaData.setOrderId(orderId);
				screenMetaData.setClientId(currentBooking.getClientID());
				screenMetaData.setClientType(currentBooking.getClientType());
				screenMetaData.setCompanyId(currentBooking.getCompanyId());
				screenMetaData.setProductSubCategory(opsProduct.getOpsProductSubCategory());
				screenMetaData=getCommercialHeadsForOrder(screenMetaData,opsOrder,opsProductSubCategory,uniqueId);
				//TODO change after Holidays enum is added
        if (opsProductSubCategory.equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_EVENTS)) {
					screenMetaData.setApplyOnProducts(getApplyOnProductsFromOrder(opsOrder));
				}
				screenMetaData.setSellingPriceComponents(getSellingPriceComponentsFromOrder(opsOrder, opsProductSubCategory));
				screenMetaData.setCurrency(getSupplierCurrencyFromOrder(opsOrder, opsProductSubCategory));
				CommercialResource commercialResource=amendCompanyCommercialService.getCommercialResourceForOrder(opsOrder, opsProductSubCategory , uniqueId);
        commercialResource.setMarginDetails(marginCalculator.calculateMargin(currentBooking, opsProduct));
				screenMetaData.setCommercials(commercialResource);

				return screenMetaData;


    }

    private AmendCommercialMetaData getCommercialHeadsForOrder(AmendCommercialMetaData screenMetaData, OpsOrderDetails opsOrder, OpsProductSubCategory opsProductSubCategory, String uniqueId) throws OperationException {

		List<CommercialHead> companyCommercialHeads = null;
		List<CommercialHead> notEligibleCommercialHeads = null;
		OpsClientEntityCommercial company = null;
		switch (opsProductSubCategory) {
		case PRODUCT_SUB_CATEGORY_FLIGHT: {

			Optional<OpsClientEntityCommercial> optCompany;
			Optional<OpsPaxTypeFareFlightClient> paxTypeFare = opsOrder.getFlightDetails().getTotalPriceInfo()
					.getPaxTypeFares().stream().filter(paxTypeFares -> paxTypeFares.getPaxType().equals(uniqueId))
					.findAny();
			if (!paxTypeFare.isPresent()) {
                throw new OperationException(Constants.PAX_NOT_FOUND, uniqueId);
			}
				// TODO add company flag filter (entity -> entity.getCompanyFlag().equals(true))
				optCompany = paxTypeFare.get().getOpsClientEntityCommercial().stream()
						.filter(entity -> entity.getOpsPaxRoomClientCommercial().get(0).getCompanyFlag() == true)
						.findAny();

				if (!optCompany.isPresent()) {
                    throw new OperationException(Constants.PAX_COMMERCIAL_NOT_FOUND, uniqueId);
			}
			company = optCompany.get();
			break;

		}
		case PRODUCT_SUB_CATEGORY_HOTELS: {

			Optional<OpsRoom> opsRoom = opsOrder.getHotelDetails().getRooms().stream()
					.filter(room -> room.getRoomID().equals(uniqueId)).findAny();
			if (!opsRoom.isPresent()) {
                throw new OperationException(Constants.ROOM_NOT_FOUND, uniqueId);
			}

			// TODO add company flag filter (entity -> entity.getCompanyFlag().equals(true))
			Optional<OpsClientEntityCommercial> optCompany = opsRoom.get().getOpsClientEntityCommercial().stream()
					.filter(entity -> entity.getOpsPaxRoomClientCommercial().get(0).getCompanyFlag() == true)
					.findFirst();
			if (!optCompany.isPresent()) {
                throw new OperationException(Constants.ROOM_COMMERCIAL_NOT_FOUND, uniqueId);
			}

			company = optCompany.get();
			break;
		}

		default:
			break;

		}

		List<OpsPaxRoomClientCommercial> companyCommercials = company.getOpsPaxRoomClientCommercial();
		companyCommercialHeads = new ArrayList<>();
		notEligibleCommercialHeads = new ArrayList<>();
		for (OpsPaxRoomClientCommercial companyCommercial : companyCommercials) {
			String commercialName = companyCommercial.getCommercialName();
			if (EditableCommercialHeads.getCommercialHead(commercialName)!=null) {
				// creating Commercial ID
				// TODO check if BE can give Commercial ID
                //TODO add mdmRuleId
                companyCommercialHeads.add(new CommercialHead(company.getCommercialEntityID(), commercialName, companyCommercial.getMdmRuleID()));

			}
            notEligibleCommercialHeads.add(new CommercialHead(company.getCommercialEntityID(), commercialName, companyCommercial.getMdmRuleID()));

		}

		screenMetaData.setCommercialHeads(companyCommercialHeads);
		screenMetaData.setNotEligibleCommercialHeads(notEligibleCommercialHeads);

		return screenMetaData;
	}

    private List<String> getSupplierCurrencyFromOrder(OpsOrderDetails opsOrder, OpsProductSubCategory opsProductSubCategory) {
        List<String> currency = new ArrayList<String>();

        switch (opsProductSubCategory) {
            case PRODUCT_SUB_CATEGORY_FLIGHT:

                currency.add(opsOrder.getFlightDetails().getTotalPriceInfo().getCurrencyCode());
                
                break;

            case PRODUCT_SUB_CATEGORY_HOTELS:

                currency.add(opsOrder.getHotelDetails().getOpsAccommodationTotalPriceInfo().getCurrencyCode());
                
                break;

            default:
                break;

        }

        return currency;
    }

	private List<String> getApplyOnProductsFromOrder(OpsOrderDetails opsOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<String> getSellingPriceComponentsFromOrder(OpsOrderDetails opsOrder,OpsProductSubCategory opsProductSubCategory) {
		
		List<String> sellingPriceComponents = null;
		switch (opsProductSubCategory) {
		case PRODUCT_SUB_CATEGORY_FLIGHT:
			sellingPriceComponents = new ArrayList<>();
			sellingPriceComponents.add("totalPrice");
			if (opsOrder.getFlightDetails().getTotalPriceInfo().getBaseFare() != null) {
				sellingPriceComponents.add("baseFare");
			}
			sellingPriceComponents.addAll(opsOrder.getFlightDetails().getTotalPriceInfo().getFees().getFee()
					.parallelStream().map(OpsFee::getFeeCode).collect(Collectors.toList()));
			sellingPriceComponents.addAll(opsOrder.getFlightDetails().getTotalPriceInfo().getTaxes().getTax()
					.parallelStream().map(OpsTax::getTaxCode).collect(Collectors.toList()));

			break;

		case PRODUCT_SUB_CATEGORY_HOTELS:
			sellingPriceComponents = new ArrayList<>();
			sellingPriceComponents.add("totalPrice");
			sellingPriceComponents.addAll(opsOrder.getHotelDetails().getOpsAccommodationTotalPriceInfo().getOpsTaxes()
					.getTax().parallelStream().map(OpsTax::getTaxCode).collect(Collectors.toList()));

			break;

		default:
			break;
		}

		return sellingPriceComponents;

	}
    
    private enum EditableCommercialHeads {
    	ServiceCharge("ServiceCharge"), MarkUp("MarkUp"), ManagementFees("ManagementFees");


        private String head;

        EditableCommercialHeads(String newHead )   {
            head = newHead;
        }

        public static EditableCommercialHeads getCommercialHead(String head) {
            EditableCommercialHeads commercialHead = null;
            if(StringUtils.isEmpty(head)) {
                return null;
            }

            for(EditableCommercialHeads editableCommercialHead: EditableCommercialHeads.values()) {
                if(editableCommercialHead.getHead().equalsIgnoreCase(head)) {
                    commercialHead = editableCommercialHead;
                    break;
                }
            }

            return commercialHead;
        }

        public String getHead() {
            return head;
        }
    }
}
