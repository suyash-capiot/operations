package com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking.impl.activities;

import com.coxandkings.travel.operations.enums.manageofflinebooking.CommercialsOperation;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking.ProductBookingHandler;
import com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking.enums.OffersType;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ActivitiesBookingHandler implements ProductBookingHandler {

    private static Logger logger = Logger.getLogger(ActivitiesBookingHandler.class);

    public boolean isResponsibleFor(String product) {
        return "Activities".equalsIgnoreCase(product);
    }

    public JSONObject calculateCommercials(JSONObject pUIJSObject, JSONObject mdmDataJSon) throws OperationException {
        logger.debug("Inside calculateCommercials method:Start");
        JSONObject jsObjRequest = null, jsObjResponse = null, jsObjSupplierCommercialsResponse = null, jsObjClientCommercialsResponse = null;
        Map<Integer, String> mapSI2BRMSRoomMap = new HashMap<Integer, String>();
        try {
            jsObjRequest = getRequestJSON(pUIJSObject, mdmDataJSon);
            jsObjResponse = getResponseJSON(pUIJSObject, mdmDataJSon);
           /* jsObjSupplierCommercialsResponse = supplierCommercials.getSupplierCommercials(CommercialsOperation.Book, jsObjRequest, jsObjResponse, mapSI2BRMSRoomMap, mdmDataJSon);
            jsObjClientCommercialsResponse = clientCommercials.getClientCommercials(jsObjRequest, jsObjSupplierCommercialsResponse, mdmDataJSon);
            calculatePrices(jsObjRequest, jsObjResponse, jsObjClientCommercialsResponse, jsObjSupplierCommercialsResponse, mapSI2BRMSRoomMap, true, null, mdmDataJSon);
            taxEngine.getCompanyTaxes(jsObjRequest, jsObjResponse, mdmDataJSon, pUIJSObject);
            companyOffers.getCompanyOffers(CommercialsOperation.Book, jsObjRequest, jsObjResponse, OffersType.COMPANY_SEARCH_TIME, mdmDataJSon);*/
        } catch (OperationException oe) {
            logger.error("An exceptions occurred calculateCommercials : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in calculateCommercials method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in calculateCommercials method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside calculateCommercials method:End");
        return jsObjResponse;
    }

    @Override
    public JSONObject getRequestJSON(JSONObject pUIJSObject, JSONObject mdmDataJSon) throws OperationException {
        return null;
    }

    @Override
    public JSONObject getResponseJSON(JSONObject pUIJSObject, JSONObject mdmDataJSon) throws OperationException {
        return null;
    }

    //----------------------------------------------------------------KAFKA RQ starts here-------------------------------------------------------------------------------------
    @Override
    public JSONObject saveBooking(JSONObject applyCommRespJSON, JSONObject createBookingJSON, String bookID) throws OperationException {
        return null;
    }

    //----------------------------------------------------------------KAFKA RS starts here-------------------------------------------------------------------------------------

    @Override
    public JSONObject generateBookingInvoice(JSONObject applyCommRespJSON, JSONObject createBookingJSON, String bookID) throws OperationException {
        return null;
    }


    @Override
    public String getSupplierName(JSONObject travelDetails) throws OperationException {
        return null;
    }

    @Override
    public String getLeadPaxName(JSONObject travelDetails) throws OperationException {
        return null;
    }

    //----------------------------------------------------------------Fetch Data for Supplier starts here-------------------------------------------------------------------------------------
    @Override
    public JSONObject fetchDataFromMDM(JSONObject criteria) throws OperationException {
        return null;
    }

}
