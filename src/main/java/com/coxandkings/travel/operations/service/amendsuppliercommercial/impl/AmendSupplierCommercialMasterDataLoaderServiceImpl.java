package com.coxandkings.travel.operations.service.amendsuppliercommercial.impl;

import com.coxandkings.travel.operations.controller.coreBE.RetrieveBookingDetailsController;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.amendsuppliercommercial.AccoSupplierCommercial;
import com.coxandkings.travel.operations.model.amendsuppliercommercial.AirSupplierCommercial;
import com.coxandkings.travel.operations.model.amendsuppliercommercial.CommercialHead;
import com.coxandkings.travel.operations.model.amendsuppliercommercial.SupplierCommercialResource;
import com.coxandkings.travel.operations.model.commercials.ApplyCommercialOn;
import com.coxandkings.travel.operations.model.commercials.SellingPriceComponent;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.AmendSupplierCommercialMetaDataResource;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.SupplierCommercialPricingDetailResource;
import com.coxandkings.travel.operations.service.amendsuppliercommercial.AmendSupplierCommercialMasterDataLoaderService;
import com.coxandkings.travel.operations.service.amendsuppliercommercial.AmendSupplierCommercialService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Service
public class AmendSupplierCommercialMasterDataLoaderServiceImpl implements AmendSupplierCommercialMasterDataLoaderService {
    private static Logger logger = LogManager.getLogger(RetrieveBookingDetailsController.class);
    @Autowired
    private OpsBookingService opsBookingService;
    @Autowired
    private AmendSupplierCommercialService amendSupplierCommercialService;
    
    @Value("${mdm.supplier-commercial-head}")
    private String getcommercialHeadUrl;

    @Autowired
    private MDMRestUtils mdmRestUtils;
    
    @Override
    public AmendSupplierCommercialMetaDataResource getScreenMetaData(String bookingId, String orderId, String roomId, String paxType) throws OperationException {
        if (StringUtils.isEmpty(bookingId)) {
            throw new OperationException(Constants.RECORD_NOT_FOUND, bookingId);
        }
       /* List<String> editableCommercialHeads = new ArrayList<String>();
        for (AmendSupplierCommercialMasterDataLoaderServiceImpl.EditableCommercialHeads commercialHead : AmendSupplierCommercialMasterDataLoaderServiceImpl.EditableCommercialHeads.values()) {
            editableCommercialHeads.add(commercialHead.name());
        }*/

        SupplierCommercialResource oldSupplierCommercialResource = new SupplierCommercialResource();
        OpsBooking opsBooking = null;
        opsBooking = opsBookingService.getBooking(bookingId);
       
        Optional<OpsProduct> optOpsProduct = opsBooking.getProducts().stream().filter(opsProduct -> opsProduct.getOrderID().equalsIgnoreCase(orderId))
                .findAny();
        if (!optOpsProduct.isPresent()) {
            throw new OperationException(Constants.RECORD_NOT_FOUND, orderId);
        }
        Set<CommercialHead> opsSupplierCHs = new LinkedHashSet<>();
        OpsProduct aProduct = optOpsProduct.get();
        OpsOrderStatus orderStatus=aProduct.getOrderDetails().getOpsOrderStatus();
        if (!orderStatus.getProductStatus().equalsIgnoreCase(OpsOrderStatus.OK.getProductStatus()) && !orderStatus.getProductStatus().equalsIgnoreCase(OpsOrderStatus.VCH.getProductStatus())) {
            throw new OperationException(Constants.CANNOT_AMEND_COMMERCIALS, orderStatus.getProductStatus());
        }
        
        /*Set<CommercialHead> notEligibleList = new LinkedHashSet<>();
        for(NonEligibleCommercialHeads commercialHead:NonEligibleCommercialHeads.values()) {
        	notEligibleList.add(new CommercialHead(commercialHead.getMdmValue(), null));
        }*/
        
        //ToDo Not decided how it will come from booking engine
        Set<ApplyCommercialOn> applyOnSet = null;
        
        oldSupplierCommercialResource.setOrderSupplierCommercials(aProduct.getOrderDetails().getSupplierCommercials());
        oldSupplierCommercialResource.setOrderClientCommercials(aProduct.getOrderDetails().getClientCommercials());
        Set<SellingPriceComponent> opsSellingPriceSet = new LinkedHashSet<>();
        SupplierCommercialPricingDetailResource supplierCommercialPricingDetailResource = null;


        AmendSupplierCommercialMetaDataResource screenMetaData = null;

        screenMetaData = new AmendSupplierCommercialMetaDataResource();
        String sourceSupplierId=aProduct.getSourceSupplierID();
        OpsProductCategory aProductCategory = OpsProductCategory.getProductCategory(aProduct.getProductCategory());
        OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(aProductCategory, aProduct.getProductSubCategory());
        screenMetaData.setProductCategory(aProduct.getProductCategory());
        screenMetaData.setProductSubcategory(aProduct.getProductSubCategory());
        supplierCommercialPricingDetailResource = amendSupplierCommercialService.calculateMargin(opsBooking,aProduct);
        switch (opsProductSubCategory) {
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                if (StringUtils.isEmpty(paxType)) {
                    logger.error("Missing paxType in request param");
                    throw new OperationException("Missing paxType in request param");
                }
                AirSupplierCommercial airSupplierCommercial = new AirSupplierCommercial();
                airSupplierCommercial.setMargin(supplierCommercialPricingDetailResource.getMargin());
                airSupplierCommercial.setNetPayableToSupplier(supplierCommercialPricingDetailResource.getNetPayableToSupplier());
                airSupplierCommercial.setTotalSellingPrice(supplierCommercialPricingDetailResource.getTotalSellingPrice());
                airSupplierCommercial.setCurrencyCode(supplierCommercialPricingDetailResource.getCurrencyCode());
                airSupplierCommercial.setOpsFlightSupplierPriceInfo(aProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo());
                airSupplierCommercial.setOpsFlightSupplierCommercials(aProduct.getOrderDetails().getSupplierCommercials());
                airSupplierCommercial.setOpsFlightTotalPriceInfo(aProduct.getOrderDetails().getFlightDetails().getTotalPriceInfo());
                oldSupplierCommercialResource.setAirSupplierCommercial(airSupplierCommercial);
                List<OpsPaxTypeFareFlightSupplier> paxTypeFares = aProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getPaxTypeFares();
                screenMetaData.setSupplierCurrency(aProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getCurrencyCode());
                if (paxTypeFares != null) {
                	
                    for (OpsPaxTypeFareFlightSupplier opsPaxTypeFareFlightSupplier : paxTypeFares) {
                        if (opsPaxTypeFareFlightSupplier.getPaxType().equalsIgnoreCase(paxType)) {
                            List<OpsFlightPaxSupplierCommercial> supplierCommercials = opsPaxTypeFareFlightSupplier.getSupplierCommercials();
                            for (OpsFlightPaxSupplierCommercial opsFlightPaxSupplierCommercial : supplierCommercials) {
                            	
                            	if(opsFlightPaxSupplierCommercial.getSupplierID()==null) {
                            		throw new OperationException("Supplier Id not found for Supplier Commercial- "+opsFlightPaxSupplierCommercial.getCommercialName());
                            	}
                            	if(opsFlightPaxSupplierCommercial.getSupplierID().equals(sourceSupplierId)) {
                            	if(EditableCommercialHeads.getCommercialHead(opsFlightPaxSupplierCommercial.getCommercialName())!=null
                            			&& EditableCommercialType.getCommercialType(EditableCommercialHeads.getCommercialHead(opsFlightPaxSupplierCommercial.getCommercialName()), opsFlightPaxSupplierCommercial.getCommercialType())!=null)
                            	{
                                    opsSupplierCHs.add(new CommercialHead(opsFlightPaxSupplierCommercial.getCommercialName(), opsFlightPaxSupplierCommercial.getMdmRuleID()));
                            	}
                            	
                            	/*if(NonEligibleCommercialHeads.getCommercialHead(opsFlightPaxSupplierCommercial.getCommercialName())!=null) {
                            	notEligibleList.add(new CommercialHead(opsFlightPaxSupplierCommercial.getCommercialName(), opsFlightPaxSupplierCommercial.getMdmRuleID()));
                            	}*/

                            }
                           }
                            
                            
                            opsSellingPriceSet.add(new SellingPriceComponent("Total"));
                            if(opsPaxTypeFareFlightSupplier.getBaseFare()!=null) {
                            opsSellingPriceSet.add(new SellingPriceComponent("Basic"));
                            }
                            
                            List<OpsTax> opsTaxes = opsPaxTypeFareFlightSupplier.getTaxes().getTax();
                            
                                for (OpsTax opsTax : opsTaxes) {
                                    opsSellingPriceSet.add(new SellingPriceComponent(opsTax.getTaxCode()));
                                }
                            
                            List<OpsFee> opsFrees = opsPaxTypeFareFlightSupplier.getFees().getFee();
                            
                                for (OpsFee opsFee : opsFrees) {
                                    opsSellingPriceSet.add(new SellingPriceComponent(opsFee.getFeeCode()));
                                }
                                
                                if (!(aProduct.getOrderID().equalsIgnoreCase(orderId))) {
                                    screenMetaData.getProductsToExclude().put(aProduct.getOrderID(), aProduct.getProductSubCategory());
                                }
                        }
                    }

                }
                
                
                break;
                
            case PRODUCT_SUB_CATEGORY_HOTELS:
                if (StringUtils.isEmpty(roomId)) {
                    logger.error("Room id is missing request param");
                    throw new OperationException("Room Id Missing in Request Param");
                }
                AccoSupplierCommercial accoSupplierCommercial = new AccoSupplierCommercial();
                accoSupplierCommercial.setMargin(supplierCommercialPricingDetailResource.getMargin());
                accoSupplierCommercial.setNetPayableToSupplier(supplierCommercialPricingDetailResource.getNetPayableToSupplier());
                accoSupplierCommercial.setTotalSellingPrice(supplierCommercialPricingDetailResource.getTotalSellingPrice());
                accoSupplierCommercial.setCurrencyCode(supplierCommercialPricingDetailResource.getCurrencyCode());
                OpsOrderDetails orderDetails = aProduct.getOrderDetails();
                accoSupplierCommercial.setOpsAccommodationTotalPriceInfo(orderDetails.getHotelDetails().getOpsAccommodationTotalPriceInfo());
                accoSupplierCommercial.setOpsOrderSupplierCommercials(orderDetails.getSupplierCommercials());
                accoSupplierCommercial.setOpsAccoOrderSupplierPriceInfo(orderDetails.getHotelDetails().getOpsAccoOrderSupplierPriceInfo());

                String currency = aProduct.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getCurrencyCode();
                oldSupplierCommercialResource.setAccoSupplierCommercial(accoSupplierCommercial);
                screenMetaData.setSupplierCurrency(currency);
                List<OpsRoom> rooms = aProduct.getOrderDetails().getHotelDetails().getRooms();
                if (rooms != null) {
                    for (OpsRoom room : rooms) {
                        if (room.getRoomID().equalsIgnoreCase(roomId)) {
                            for (OpsRoomSuppCommercial roomSuppCommercial : room.getRoomSuppCommercials()) {
                            	
                            	if(roomSuppCommercial.getSupplierID()==null|| roomSuppCommercial.getSupplierID().equals(sourceSupplierId)) {
 
                            	if(EditableCommercialHeads.getCommercialHead(roomSuppCommercial.getCommercialName())!=null
                            			&& EditableCommercialType.getCommercialType(EditableCommercialHeads.getCommercialHead(roomSuppCommercial.getCommercialName()), roomSuppCommercial.getCommercialType())!=null)
                            	{
                                    opsSupplierCHs.add(new CommercialHead(roomSuppCommercial.getCommercialName(), roomSuppCommercial.getMdmRuleID()));
                                }
                            	/*if(NonEligibleCommercialHeads.getCommercialHead(roomSuppCommercial.getCommercialName())!=null) {
                                notEligibleList.add(new CommercialHead(roomSuppCommercial.getCommercialName(), roomSuppCommercial.getMdmRuleID()));
                            	}*/
                            	}
                            }
                            accoSupplierCommercial.setRoom(room);
                            
                            opsSellingPriceSet.add(new SellingPriceComponent("Total"));
                         
                            OpsTaxes tax = room.getRoomSuppPriceInfo().getTaxes();
                            for (OpsTax opsTax : tax.getTax()) {
                                opsSellingPriceSet.add(new SellingPriceComponent(opsTax.getTaxCode()));
                            }
                            
                        }
                    }
                }
                
                
                
                
                
                break;
                
            case PRODUCT_SUB_CATEGORY_HOLIDAYS:
            	//ToDo Not decided how it will come from booking engine
                applyOnSet = new LinkedHashSet<>(Arrays.asList(new ApplyCommercialOn("Main Package"), new ApplyCommercialOn("Flight "), new ApplyCommercialOn("Visa")));// todo from where to get
            break;
            default:
                new OperationException("AbstractProductFactory Not Supported");
        }

        screenMetaData.setOrderId(orderId);
        screenMetaData.setRoomId(roomId);
        screenMetaData.setBookId(opsBooking.getBookID());
        screenMetaData.setClientId(opsBooking.getClientID());
        screenMetaData.setClientTypeId(opsBooking.getClientType());
        screenMetaData.setCompanyId(opsBooking.getCompanyId());
        screenMetaData.setSupplierCommercialHeads(opsSupplierCHs);
        
        screenMetaData.setBookingNotEligibleFor(getNonEligibleCommercialHeads(sourceSupplierId));
        
        screenMetaData.setSupplierPriceComponents(opsSellingPriceSet);
        screenMetaData.setApplyProducts(applyOnSet);
        screenMetaData.setOldSupplierCommercialResource(oldSupplierCommercialResource);
        return screenMetaData;
    }


    private enum EditableCommercialHeads {
        STANDARD("Standard"), OVERRIDING("Overriding"),COMMISSION("Commission"), MSF("msfFee"), ISSUANCE("IssuanceFees"), SERVICE("ServiceCharge");


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
    	
    
    	
    	STANDARD_RECEIVABLE( AmendSupplierCommercialMasterDataLoaderServiceImpl.EditableCommercialHeads.STANDARD, "Receivable" ),
    	OVERRIDING_RECEIVABLE( AmendSupplierCommercialMasterDataLoaderServiceImpl.EditableCommercialHeads.OVERRIDING, "Receivable" ),
    	COMMISSION_RECEIVABLE( AmendSupplierCommercialMasterDataLoaderServiceImpl.EditableCommercialHeads.COMMISSION, "Receivable" ),
    	MSF_PAYABLE( AmendSupplierCommercialMasterDataLoaderServiceImpl.EditableCommercialHeads.MSF, "Payable" ),
    	ISSUANCE_PAYABLE( AmendSupplierCommercialMasterDataLoaderServiceImpl.EditableCommercialHeads.ISSUANCE, "Payable" ),
    	SERVICE_PAYABLE( AmendSupplierCommercialMasterDataLoaderServiceImpl.EditableCommercialHeads.SERVICE, "Payable" );
        
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
    
    public Set<CommercialHead> getNonEligibleCommercialHeads(String supplierId) throws OperationException{
    	Set<CommercialHead> notEligibleList = new LinkedHashSet<>();
    	
    	List<String> commercialHeadList= new ArrayList<String>();
    	for(NonEligibleCommercialHeads commercialHead:NonEligibleCommercialHeads.values()) {
    		commercialHeadList.add(commercialHead.getMdmValue());
        }
    	ResponseEntity<String> commercialHeadData=null;
        try {
        	URI builder = UriComponentsBuilder.fromUriString(getcommercialHeadUrl + "?filter={\"supplierCommercialData.commercialDefinition.supplierId\":\""+supplierId+"\"}" ).build().encode().toUri();
            commercialHeadData = mdmRestUtils.exchange(builder, HttpMethod.GET, null, String.class);
        }
        
        catch(Exception e) {
        	throw new OperationException("Could not get Supplier Commercial Heads for Supplier "+supplierId);
        }
    	JSONObject commercialHeadDataJSON = new JSONObject(commercialHeadData.getBody());
        JSONArray commercialHeads = commercialHeadDataJSON.getJSONArray("data");
    	for(int i=0;i<commercialHeads.length();i++) {
    		String headName=commercialHeads.getString(i);
    		if(commercialHeadList.contains(headName)) {
    			notEligibleList.add(new CommercialHead(headName, null));
    		}
    	}
        
        
    	return notEligibleList;
    	
    }
}

