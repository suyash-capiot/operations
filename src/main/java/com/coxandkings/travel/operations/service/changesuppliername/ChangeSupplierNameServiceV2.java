package com.coxandkings.travel.operations.service.changesuppliername;

import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.changesuppliername.DiscountOnSupplierPrice;
import com.coxandkings.travel.operations.model.changesuppliername.SupplementOnSupplierPrice;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsHotelDetails;
import com.coxandkings.travel.operations.resource.changesuppliername.ChangedSupplierPriceResource;
import com.coxandkings.travel.operations.resource.changesuppliername.SupplierResource;
import com.coxandkings.travel.operations.resource.changesuppliername.cms.CmsSupplierResource;
import com.coxandkings.travel.operations.resource.changesuppliername.request.accoV2.CSCreateBookingResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;

import javax.management.OperationsException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ChangeSupplierNameServiceV2 {

    JSONObject getDefinedRates(String bookID, String orderID, String suppID) throws OperationException;
    public JSONObject createBooking(CSCreateBookingResource csCreateBookingResource) throws OperationException;

    public String getCMSDefinedRates(String bookID, String orderID, String suppID) throws OperationException;

    public JSONObject processCMSBooking(JSONObject requestHeader, OpsHotelDetails opsHotelDetails, String newSuppId, JSONObject definedRate) throws OperationException;

    public JSONArray getCMS(List<CmsSupplierResource> responseList ,String param);
    public List<CmsSupplierResource> getSupplierElements(String key) throws Exception;
}

