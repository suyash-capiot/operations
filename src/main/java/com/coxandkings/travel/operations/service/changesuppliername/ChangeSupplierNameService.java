package com.coxandkings.travel.operations.service.changesuppliername;

import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.changesuppliername.DiscountOnSupplierPrice;
import com.coxandkings.travel.operations.model.changesuppliername.SupplementOnSupplierPrice;
import com.coxandkings.travel.operations.resource.changesuppliername.ChangedSupplierPriceResource;
import com.coxandkings.travel.operations.resource.changesuppliername.SupplierResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ChangeSupplierNameService {
    List<SupplierResource> getSuppliers(String productCategory, String productSubCategory, String clientId, String clientType) throws OperationException;

    Object getRatesForAcco(String bookingRefId, String orderId, String supplierName) throws ParseException, IOException, OperationException, JSONException;

    Object getRatesForAir(String bookingRefId, String orderId, String supplierName) throws ParseException, IOException, OperationException, JSONException;

    Object applyRatesForAir(JSONObject supplierChangeRequest);

    Object applyRatesForAcco(JSONObject supplierChangeRequest) throws ParseException, OperationException;


    Object getRePriceForAcco(String bookId, String orderId);


    JSONObject getSupplierRates(OpsProductCategory productCategory, OpsProductSubCategory productSubCategory, String supplierId) throws JSONException, JsonProcessingException, OperationException;

    ChangedSupplierPriceResource getDiscountOrAddSupplementOnSupplierPrice(JSONObject discountOnSupplierPriceResource) throws OperationException;

    //JSONObject addSupplementOnSupplierPrice(JSONObject supplementOnSupplierPriceResource);

    JSONObject getScreenMetaData(JSONObject jsonResource) throws OperationException;

    Object getRePriceForAir(String bookId, String orderId);

    void saveDiscountOnSupplierPriceDetail(DiscountOnSupplierPrice discountOnSupplierPrice) throws OperationException;

    void saveSupplementOnSupplierPriceDetail(SupplementOnSupplierPrice supplementOnSupplierPrice) throws OperationException;
}

