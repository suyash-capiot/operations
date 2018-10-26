package com.coxandkings.travel.operations.service.supplierbillpassing;

import com.coxandkings.travel.operations.exceptions.OperationException;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public interface MDMRequirements {
    String getSupplierNameById(String id) throws OperationException;

    String getProductCategoryById(String id) throws OperationException, JSONException, IOException;

    String getProductSubCategoryById(String id) throws JSONException, OperationException, IOException;

    String getProductById(String id) throws  JSONException,OperationException,IOException;

    List getProductCategorySubType(String supplierId, String productCategory) throws OperationException, IOException;

}
