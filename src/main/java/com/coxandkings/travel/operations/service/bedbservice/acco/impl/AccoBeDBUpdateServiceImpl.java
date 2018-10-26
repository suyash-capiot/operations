package com.coxandkings.travel.operations.service.bedbservice.acco.impl;

import com.coxandkings.travel.ext.model.be.CompanyTax;
import com.coxandkings.travel.ext.model.be.CompanyTaxes;
import com.coxandkings.travel.ext.model.be.Receivable;
import com.coxandkings.travel.ext.model.be.Receivables;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.service.bedbservice.acco.AccoBeDBUpdateService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("accoBeDBUpdateServiceImpl")
public class AccoBeDBUpdateServiceImpl implements AccoBeDBUpdateService {
	private static final Logger logger = LogManager.getLogger(AccoBeDBUpdateServiceImpl.class);
	
    @Value("${booking_engine.update.acco.order-price}")
    private String updateOrderLevelPriceUrl;
    private RestTemplate restTemplate;
    @Autowired
    private UserService userService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    

    @Override
    public String updateOrderPrice(OpsProduct opsProduct) throws OperationException {

        JSONObject pricingDetail = new JSONObject();
        pricingDetail.put("orderID", opsProduct.getOrderID());
        pricingDetail.put("orderClientCommercials", getOrderClientCommercials(opsProduct.getOrderDetails().getClientCommercials()));
        pricingDetail.put("orderSupplierPriceInfo", getOrderSupplierPriceInfo(opsProduct.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo()));
        pricingDetail.put("orderTotalPriceInfo", getOrderTotalPriceInfo(opsProduct.getOrderDetails().getHotelDetails().getOpsAccommodationTotalPriceInfo()));
        pricingDetail.put("orderSupplierCommercials", getOrderSupplierCommercials(opsProduct.getOrderDetails().getSupplierCommercials()));

        String userId=userService.getLoggedInUserId();
        if(userId==null) {
        	userId="";
        }
        pricingDetail.put("userID", userId);
        JSONArray rooms = new JSONArray();
        ResponseEntity<String> responseEntity = null;
        for (OpsRoom opsRoom : opsProduct.getOrderDetails().getHotelDetails().getRooms()) {
        	try {
            JSONObject room = new JSONObject();
            room.put("roomID", opsRoom.getRoomID());
            JSONObject supplierPriceInfo = new JSONObject();
            OpsRoomSuppPriceInfo roomSuppPriceInfo = opsRoom.getRoomSuppPriceInfo();
            supplierPriceInfo.put("supplierPrice", roomSuppPriceInfo.getRoomSupplierPrice());
            supplierPriceInfo.put("currencyCode", roomSuppPriceInfo.getCurrencyCode());
            supplierPriceInfo.put("taxes", getTaxes(roomSuppPriceInfo.getTaxes()));


            JSONObject totalPriceInfo = new JSONObject();
            totalPriceInfo.put("totalPrice", opsRoom.getRoomTotalPriceInfo().getRoomTotalPrice());
            totalPriceInfo.put("currencyCode", opsRoom.getRoomTotalPriceInfo().getCurrencyCode());
            totalPriceInfo.put("taxes", getTaxes(opsRoom.getRoomTotalPriceInfo().getOpsTaxes()));
            
            totalPriceInfo.put("discounts",objectMapper.writeValueAsString(opsRoom.getRoomTotalPriceInfo().getDiscounts()));
            totalPriceInfo.put("incentives", objectMapper.writeValueAsString(opsRoom.getRoomTotalPriceInfo().getIncentives()));
            
            totalPriceInfo.put("companyTaxes", objectMapper.writeValueAsString(getCompanyTaxes(opsRoom.getRoomTotalPriceInfo().getCompanyTaxes())));
            totalPriceInfo.put("receivables", objectMapper.writeValueAsString(getReceivables(opsRoom.getRoomTotalPriceInfo().getReceivables())));
            
            

            room.put("supplierPriceInfo", supplierPriceInfo);
            room.put("totalPriceInfo", totalPriceInfo);

            List<OpsClientEntityCommercial> opsClientEntityCommercials = opsRoom.getOpsClientEntityCommercial();
            JSONArray clientCommercials = new JSONArray();
            if (opsClientEntityCommercials != null && opsClientEntityCommercials.size() > 0) {
                for (OpsClientEntityCommercial clientEntityCommercial : opsClientEntityCommercials) {
                    JSONObject clientCommercial = new JSONObject();
                    clientCommercial.put("clientID", clientEntityCommercial.getClientID());
                    clientCommercial.put("parentClientID", clientEntityCommercial.getParentClientID());
                    clientCommercial.put("commercialEntityType", clientEntityCommercial.getCommercialEntityType());
                    clientCommercial.put("commercialEntityID", clientEntityCommercial.getCommercialEntityID());
                    
                    clientCommercial.put("entityName", clientEntityCommercial.getEntityName());
                    clientCommercial.put("clientMarket", clientEntityCommercial.getClientMarket());
                    clientCommercial.put("commercialEntityMarket", clientEntityCommercial.getCommercialEntityMarket());
                    
                    if (clientEntityCommercial.getOpsPaxRoomClientCommercial() != null) {
                        clientCommercial.put("clientCommercials", getOpsPaxRoomClientCommercials(clientEntityCommercial.getOpsPaxRoomClientCommercial()));
                    }
                    clientCommercials.put(clientCommercial);
                }
            }
            room.put("clientCommercials", clientCommercials);

            room.put("supplierCommercials", getRoomSupplierCommercials(opsRoom.getRoomSuppCommercials()));


            rooms.put(room);
        }
        	catch(Exception e) {
        		logger.error("Error creating Acco DB update Request",e.getMessage());
        		throw new OperationException("Error Creating Accomodation Database Update Request");
        	}
        	}
        pricingDetail.put("rooms", rooms);
        restTemplate = RestUtils.getTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(pricingDetail.toString(), headers);
        String result = null;
        try {
        	logger.info("Booking DB Update RQ " +pricingDetail.toString());
            responseEntity = restTemplate.exchange(updateOrderLevelPriceUrl, HttpMethod.PUT, httpEntity, String.class);
            result = responseEntity.getBody();
            return result;
        } catch (Exception e) {
            throw new OperationException("Booking DB update failed");
        }


    }

    private JSONArray getRoomSupplierCommercials(List<OpsRoomSuppCommercial> roomSuppCommercials) {
        JSONArray roomSupplierCommercials = new JSONArray();
        if (roomSuppCommercials != null && roomSuppCommercials.size() > 0) {
            for (OpsRoomSuppCommercial opsRoomSuppCommercial : roomSuppCommercials) {
                JSONObject roomSupplierCommercial = new JSONObject();
                roomSupplierCommercial.put("commercialCalculationAmount", opsRoomSuppCommercial.getCommercialCalculationAmount());
                roomSupplierCommercial.put("commercialFareComponent", opsRoomSuppCommercial.getCommercialFareComponent());
                roomSupplierCommercial.put("commercialCalculationPercentage", opsRoomSuppCommercial.getCommercialCalculationPercentage());
                roomSupplierCommercial.put("commercialInitialAmount", opsRoomSuppCommercial.getCommercialInitialAmount());
                roomSupplierCommercial.put("commercialTotalAmount", opsRoomSuppCommercial.getCommercialTotalAmount());
                roomSupplierCommercial.put("commercialCurrency", opsRoomSuppCommercial.getCommercialCurrency());
                roomSupplierCommercial.put("commercialType", opsRoomSuppCommercial.getCommercialType());
                roomSupplierCommercial.put("commercialAmount", opsRoomSuppCommercial.getCommercialAmount());
                roomSupplierCommercial.put("commercialName", opsRoomSuppCommercial.getCommercialName());
                roomSupplierCommercial.put("mdmRuleID", opsRoomSuppCommercial.getMdmRuleID());
                roomSupplierCommercial.put("supplierID",opsRoomSuppCommercial.getSupplierID());
                roomSupplierCommercials.put(roomSupplierCommercial);
            }
        }
        return roomSupplierCommercials;
    }

    private JSONArray getOpsPaxRoomClientCommercials(List<OpsPaxRoomClientCommercial> opsPaxRoomClientCommercials) {
        JSONArray roomClientCommercialJson = new JSONArray();
        if (opsPaxRoomClientCommercials != null && opsPaxRoomClientCommercials.size() > 0) {
            for (OpsPaxRoomClientCommercial opsPaxRoomClientCommercial : opsPaxRoomClientCommercials) {
                JSONObject clientCommercial = new JSONObject();
                clientCommercial.put("commercialAmount", opsPaxRoomClientCommercial.getCommercialAmount());
                clientCommercial.put("commercialCurrency", opsPaxRoomClientCommercial.getCommercialCurrency());
                clientCommercial.put("commercialName", opsPaxRoomClientCommercial.getCommercialName());
                clientCommercial.put("commercialType", opsPaxRoomClientCommercial.getCommercialType());
                clientCommercial.put("companyFlag", opsPaxRoomClientCommercial.getCompanyFlag());
                
                clientCommercial.put("mdmRuleID", opsPaxRoomClientCommercial.getMdmRuleID());
                clientCommercial.put("commercialCalculationAmount", opsPaxRoomClientCommercial.getCommercialCalculationAmount());
                clientCommercial.put("commercialFareComponent", opsPaxRoomClientCommercial.getCommercialFareComponent());
                clientCommercial.put("commercialCalculationPercentage", opsPaxRoomClientCommercial.getCommercialCalculationPercentage());
                
                clientCommercial.put("retentionPercentage", opsPaxRoomClientCommercial.getRetentionPercentage());
                clientCommercial.put("retentionAmountPercentage", opsPaxRoomClientCommercial.getRetentionAmountPercentage());
                clientCommercial.put("remainingPercentageAmount", opsPaxRoomClientCommercial.getRemainingPercentageAmount());
                clientCommercial.put("remainingAmount", opsPaxRoomClientCommercial.getRemainingAmount());
                
                roomClientCommercialJson.put(clientCommercial);
            }
        }
        return roomClientCommercialJson;
    }


    private JSONArray getOrderSupplierCommercials(List<OpsOrderSupplierCommercial> supplierCommercials) {
        JSONArray orderSupplierCommercials = new JSONArray();
        if (supplierCommercials != null && supplierCommercials.size() > 0) {
            for (OpsOrderSupplierCommercial opsOrderSupplierCommercial : supplierCommercials) {
                JSONObject supplierCommercial = new JSONObject();
                supplierCommercial.put("commercialAmount", opsOrderSupplierCommercial.getCommercialAmount());
                supplierCommercial.put("commercialCurrency", opsOrderSupplierCommercial.getCommercialCurrency());
                supplierCommercial.put("commercialName", opsOrderSupplierCommercial.getCommercialName());
                supplierCommercial.put("commercialType", opsOrderSupplierCommercial.getCommercialType());
                supplierCommercial.put("suppCommId", opsOrderSupplierCommercial.getSuppCommId());
                supplierCommercial.put("isEligible", opsOrderSupplierCommercial.isEligible());
                supplierCommercial.put("supplierID",opsOrderSupplierCommercial.getSupplierID());
                orderSupplierCommercials.put(supplierCommercial);
            }
        }
        return orderSupplierCommercials;
    }


    private JSONObject getOrderTotalPriceInfo(OpsAccommodationTotalPriceInfo opsAccommodationTotalPriceInfo) {
        JSONObject orderTotalPriceInfo = new JSONObject();
        orderTotalPriceInfo.put("currencyCode", opsAccommodationTotalPriceInfo.getCurrencyCode());
        orderTotalPriceInfo.put("totalPrice", opsAccommodationTotalPriceInfo.getTotalPrice());
        orderTotalPriceInfo.put("taxes", getTaxes(opsAccommodationTotalPriceInfo.getOpsTaxes()));
        return orderTotalPriceInfo;
    }

    private JSONArray getOrderClientCommercials(List<OpsOrderClientCommercial> clientCommercials) {
        JSONArray orderClientCommercialsJson = new JSONArray();
        for (OpsOrderClientCommercial orderClientCommercial : clientCommercials) {
            JSONObject orderClientCommercialJson = new JSONObject();
            orderClientCommercialJson.put("clientCommId", orderClientCommercial.getClientCommId());
            orderClientCommercialJson.put("clientID", orderClientCommercial.getClientID());
            orderClientCommercialJson.put("parentClientID", orderClientCommercial.getParentClientID());
            orderClientCommercialJson.put("commercialEntityType", orderClientCommercial.getCommercialEntityType());
            orderClientCommercialJson.put("companyFlag", orderClientCommercial.getCompanyFlag());
            orderClientCommercialJson.put("commercialCurrency", orderClientCommercial.getCommercialCurrency());
            orderClientCommercialJson.put("commercialType", orderClientCommercial.getCommercialType());
            orderClientCommercialJson.put("commercialEntityID", orderClientCommercial.getCommercialEntityID());
            orderClientCommercialJson.put("commercialAmount", orderClientCommercial.getCommercialAmount());
            orderClientCommercialJson.put("commercialName", orderClientCommercial.getCommercialName());
            orderClientCommercialJson.put("isEligible", orderClientCommercial.isEligible());
            orderClientCommercialsJson.put(orderClientCommercialJson);
        }
        return orderClientCommercialsJson;
    }


    private JSONObject getOrderSupplierPriceInfo(OpsAccoOrderSupplierPriceInfo opsAccoOrderSupplierPriceInfo) {
        JSONObject orderSupplierPriceInfo = new JSONObject();
        orderSupplierPriceInfo.put("supplierPrice", opsAccoOrderSupplierPriceInfo.getSupplierPrice());
        orderSupplierPriceInfo.put("currencyCode", opsAccoOrderSupplierPriceInfo.getCurrencyCode());

        orderSupplierPriceInfo.put("taxes", getTaxes(opsAccoOrderSupplierPriceInfo.getTaxes()));
        return orderSupplierPriceInfo;
    }


    private JSONObject getTaxes(OpsTaxes taxes) {
        JSONObject taxesJson = new JSONObject();
        taxesJson.put("currencyCode", taxes.getCurrencyCode());
        taxesJson.put("amount", taxes.getAmount());
        JSONArray taxArray = new JSONArray();

        List<OpsTax> opsTaxes = taxes.getTax();
        for (OpsTax opsTax : opsTaxes) {
            JSONObject taxJson = new JSONObject();
            taxJson.put("amount", opsTax.getAmount());
            taxJson.put("currencyCode", opsTax.getCurrencyCode());
            taxJson.put("taxCode", opsTax.getTaxCode());
            taxArray.put(taxJson);
        }
        taxesJson.put("tax", taxArray);
        return taxesJson;
    }
    
    public Receivables getReceivables(OpsReceivables opsReceivables) {
        Receivables receivables = new Receivables();
        if (opsReceivables != null) {
            receivables.setReceivable(getReceivable(opsReceivables.getReceivable()));
            receivables.setCurrencyCode(opsReceivables.getCurrencyCode());
            receivables.setAmount(opsReceivables.getAmount());
        }
        return receivables;

    }

    private List<Receivable> getReceivable(List<OpsReceivable> opsReceivableList) {
        List<Receivable> receivableList = null;
        if (opsReceivableList != null && opsReceivableList.size() > 0) {
            receivableList = new ArrayList<>();
            for (OpsReceivable opsReceivable : opsReceivableList) {
                Receivable receivable = new Receivable();
                receivable.setCode(opsReceivable.getCode());
                receivable.setCurrencyCode(opsReceivable.getCurrencyCode());
                receivable.setAmount(opsReceivable.getAmount());
                receivableList.add(receivable);

            }
        }
        return receivableList;
    }
    
    public CompanyTaxes getCompanyTaxes(OpsCompanyTaxes opsCompanyTaxes) {

    	CompanyTaxes companyTaxes = null;

        if (opsCompanyTaxes != null) {
        	companyTaxes = new CompanyTaxes();
        	companyTaxes.setCurrencyCode(opsCompanyTaxes.getCurrencyCode());
        	companyTaxes.setAmount(opsCompanyTaxes.getAmount());

            if (opsCompanyTaxes.getCompanyTax() != null && opsCompanyTaxes.getCompanyTax().size() > 0)
            	companyTaxes.setCompanyTax(opsCompanyTaxes.getCompanyTax().stream().map(tax1 -> getOpsCompanyTax(tax1)).collect(Collectors.toList()));
            else
            	companyTaxes.setCompanyTax(new ArrayList<CompanyTax>());
        }
        return companyTaxes;
    }

    public CompanyTax getOpsCompanyTax(OpsCompanyTax opsTax) {

    	CompanyTax tax = new CompanyTax();

        tax.setCurrencyCode(opsTax.getCurrencyCode());
        tax.setAmount(opsTax.getAmount());
        tax.setTaxCode(opsTax.getTaxCode());
        tax.setHsnCode(opsTax.getHsnCode());
        tax.setSacCode(opsTax.getSacCode());
        tax.setTaxComponent(opsTax.getTaxComponent());
        tax.setTaxPercent(opsTax.getTaxPercent());
        return tax;
    }
}


