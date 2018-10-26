package com.coxandkings.travel.operations.service.amendclientcommercial.impl;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.resource.amendentitycommercial.AmendCommercialMetaData;
import com.coxandkings.travel.operations.resource.amendentitycommercial.CommercialHead;
import com.coxandkings.travel.operations.resource.amendentitycommercial.CommercialResource;
import com.coxandkings.travel.operations.service.amendclientcommercial.AmendClientCommercialsMasterDataLoaderService;
import com.coxandkings.travel.operations.service.amendcompanycommercial.AmendCompanyCommercialService;
import com.coxandkings.travel.operations.service.amendsuppliercommercial.impl.AmendSupplierCommercialMasterDataLoaderServiceImpl.NonEligibleCommercialHeads;
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
public class AmendClientCommercialsMasterDataLoaderServiceImpl implements AmendClientCommercialsMasterDataLoaderService {

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
        if (orderStatus.getProductStatus().equalsIgnoreCase(OpsOrderStatus.XL.getProductStatus()) || orderStatus.getProductStatus().equalsIgnoreCase(OpsOrderStatus.RQ.getProductStatus())) {
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
        screenMetaData = getCommercialHeadsForOrder(screenMetaData, opsOrder, opsProductSubCategory, uniqueId);
        //TODO change after Holidays enum is added
        if (opsProductSubCategory.equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_EVENTS)) {
            screenMetaData.setApplyOnProducts(getApplyOnProductsFromOrder(opsOrder));
        }

        screenMetaData.setSellingPriceComponents(getSellingPriceComponentsFromOrder(opsOrder, opsProductSubCategory));
        screenMetaData.setCurrency(getSupplierCurrencyFromOrder(opsOrder, opsProductSubCategory));
        CommercialResource commercialResource = amendCompanyCommercialService.getCommercialResourceForOrder(opsOrder, opsProductSubCategory, uniqueId);
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
        for(NonEligibleCommercialHeads commercialHead:NonEligibleCommercialHeads.values()) {
        	notEligibleCommercialHeads.add(new CommercialHead(null,commercialHead.getMdmValue(), null));
        }
        
        for (OpsPaxRoomClientCommercial companyCommercial : companyCommercials) {
            String commercialName = companyCommercial.getCommercialName();
            String commerialtype = companyCommercial.getCommercialType();
            String companyClient = companyCommercial.getMdmRuleID().split("\\|")[2].contains("Client")?"Client":"Company";
            if(EditableCommercialHeads.getCommercialHead(commercialName)!=null &&
               EditableCommercialType.getCommercialType(EditableCommercialHeads.getCommercialHead(commercialName), String.format("%s_%s",commerialtype,companyClient))!=null)
        	 {
                // creating Commercial ID
                // TODO check if BE can give Commercial ID
                //TODO add mdmRuleID
                companyCommercialHeads.add(new CommercialHead(company.getCommercialEntityID(), commercialName, companyCommercial.getMdmRuleID()));

            } /*else if (NonEligibleCommercialHeads.getCommercialHead(commercialName)!=null) {
                notEligibleCommercialHeads.add(new CommercialHead(company.getCommercialEntityID(), commercialName, companyCommercial.getMdmRuleID()));
            }*/
        }

        screenMetaData.setCommercialHeads(companyCommercialHeads);
        screenMetaData.setNotEligibleCommercialHeads(notEligibleCommercialHeads);

        return screenMetaData;
    }

    private List<String> getApplyOnProductsFromOrder(OpsOrderDetails opsOrder) {
        // TODO Auto-generated method stub
        return null;
    }

    private List<String> getSellingPriceComponentsFromOrder(OpsOrderDetails opsOrder, OpsProductSubCategory opsProductSubCategory) {

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

    
    private enum EditableCommercialHeads {
        STANDARD("Standard"), OVERRIDING("Overriding"), DISCOUNT("Discount"),ServiceCharge("ServiceCharge"),ManagementFees("ManagementFees");


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
    
    
    public enum EditableCommercialType {
    	
    
    	
    	STANDARD_PAYABLE_CLIENT( AmendClientCommercialsMasterDataLoaderServiceImpl.EditableCommercialHeads.STANDARD, "Payable_Client" ),
    	OVERRIDING_PAYABLE_CLIENT( AmendClientCommercialsMasterDataLoaderServiceImpl.EditableCommercialHeads.OVERRIDING, "Payable_Client" ),
    	DISCOUNT_PAYABLE_CLIENT( AmendClientCommercialsMasterDataLoaderServiceImpl.EditableCommercialHeads.DISCOUNT, "Payable_Client" ),
    	ServiceCharge_PAYABLE_COMPANY(AmendClientCommercialsMasterDataLoaderServiceImpl.EditableCommercialHeads.ServiceCharge,"Payable_Company"),
    	ManagementFees_PAYABLE_COMPANY(AmendClientCommercialsMasterDataLoaderServiceImpl.EditableCommercialHeads.ManagementFees,"Payable_Company");
    	
        
    	private EditableCommercialHeads commercialHead;

        private String commercialType;

        EditableCommercialType( EditableCommercialHeads commercialHead, String commercialType )     {
        	this.commercialHead = commercialHead;
        	this.commercialType = commercialType;
        }

        public static EditableCommercialType getCommercialType(EditableCommercialHeads commercialHead, String commercialType )  {
            if( commercialHead == null || (commercialType == null || commercialType.trim().length() == 0 ) )  {
                return null;
            }


                    for( EditableCommercialType aTmpCommercialType : EditableCommercialType.values() )    {
                    	
                    	if(aTmpCommercialType.getCommercialHead().equals(commercialHead) && aTmpCommercialType.getCommercialType().equalsIgnoreCase( commercialType ))   {
                        	return aTmpCommercialType;
                        }
                    }
         

            return null;
        }

        public EditableCommercialHeads getCommercialHead() {
            return commercialHead;
        }
        

        public String getCommercialType() {
            return commercialType;
        }
    
    }
    
    public static enum NonEligibleCommercialHeads {
    	PLB("Productivity Linked Bonus"), DestinationIncentive("Destination Incentives"), SegmentFee("Segments Fees"), SectorWiseIncentive("Sector Wise Incentives");

        private String mdmValue;

        NonEligibleCommercialHeads(String newHead )   {
            mdmValue = newHead;
        }

        public static NonEligibleCommercialHeads getCommercialHead(String head) {
        	NonEligibleCommercialHeads commercialHead = null;
            if(StringUtils.isEmpty(head)) {
                return null;
            }

            for(NonEligibleCommercialHeads nonEligibleCommercialHead: NonEligibleCommercialHeads.values()) {
                if(nonEligibleCommercialHead.getMdmValue().equalsIgnoreCase(head)) {
                    commercialHead = nonEligibleCommercialHead;
                    break;
                }
            }

            return commercialHead;
        }

		public String getMdmValue() {
			return mdmValue;
		}

		

        
    }
}
