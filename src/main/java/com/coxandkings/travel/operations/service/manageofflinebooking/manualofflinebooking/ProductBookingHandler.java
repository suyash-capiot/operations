package com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import org.json.JSONObject;

public interface ProductBookingHandler {

    public boolean isResponsibleFor(String product);

    public JSONObject getRequestJSON(JSONObject pUIJSObject, JSONObject mdmDataJSon)throws OperationException;

    public JSONObject getResponseJSON(JSONObject pUIJSObject, JSONObject mdmDataJSon)throws OperationException;

    public JSONObject calculateCommercials(JSONObject pUIJSObject, JSONObject mdmDataJSon)throws OperationException;

    public JSONObject saveBooking(JSONObject applyCommRespJSON, JSONObject createBookingJSON, String bookID) throws OperationException;

    public JSONObject generateBookingInvoice(JSONObject applyCommRespJSON, JSONObject createBookingJSON, String bookID) throws OperationException;

    public String  getSupplierName(JSONObject travelDetails) throws OperationException;

    public String  getLeadPaxName(JSONObject travelDetails) throws OperationException;

    public JSONObject fetchDataFromMDM(JSONObject criteria) throws OperationException;
}
