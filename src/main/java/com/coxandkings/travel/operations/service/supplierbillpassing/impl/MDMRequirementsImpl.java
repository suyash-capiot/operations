package com.coxandkings.travel.operations.service.supplierbillpassing.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.supplierbillpassing.MDMRequirements;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Service
public class MDMRequirementsImpl implements MDMRequirements {

    @Value("${mdm.common.supplier.supplier-by-id}")
    private String getSupplierNameById;
    @Value("${mdm.product-category}")
    private String getProductCategoryById;
    @Value("${mdm.product-subCategory}")
    private String getProductSubCategoryById;
    @Value("${mdm.product}")
    private String getProductById;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;
    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Override
     public String getSupplierNameById(String id) throws OperationException {
        String response= mdmRestUtils.getForObject(getSupplierNameById+id,String.class);
        return jsonObjectProvider.getAttributeValue(response,"$.supplier.name", String.class);
    }

    @Override
     public String getProductCategoryById(String id) throws OperationException, JSONException, IOException {
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("_id",id);
        String filterById=jsonObject.toString();
        System.out.println("getProductCategoryById+filterById = " + getProductCategoryById+filterById);
        URI uri = UriComponentsBuilder.fromUriString(getProductCategoryById+filterById).build().encode().toUri();
        String response= mdmRestUtils.exchange(uri,HttpMethod.GET,String.class);
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.readTree(response).elements().next().path("data").path("value").textValue();
    }

    @Override
     public String getProductSubCategoryById(String id) throws JSONException, OperationException, IOException {
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("_id",id);
        String filterById=jsonObject.toString();
        System.out.println("getProductCategoryById+filterById = " + getProductSubCategoryById+filterById);
        URI uri = UriComponentsBuilder.fromUriString(getProductSubCategoryById+filterById).build().encode().toUri();
        String response= mdmRestUtils.exchange(uri,HttpMethod.GET,String.class);
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.readTree(response).path("data").elements().next().path("data").path("value").textValue();
    }

    @Override
    public String getProductById(String id) throws JSONException, OperationException, IOException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("_id",id);
        String filter=jsonObject.toString();
        System.out.println("getProductById+filter = " + getProductById+filter);
        URI uri=UriComponentsBuilder.fromUriString(getProductById+filter).build().encode().toUri();
        String response=mdmRestUtils.exchange(uri,HttpMethod.GET,String.class);
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.readTree(response).elements().next().path("data").path("value").textValue();

    }

    @Override
    public List getProductCategorySubType(String supplierId, String productCategory) throws OperationException {
        String uri=getSupplierNameById+supplierId;
        String response = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
        String jsonPathExpression="$.products.[?(@.productCategory=='"+productCategory+"')]";
        List list=  jsonObjectProvider.getChildrenCollection(response,jsonPathExpression,Map.class);
        return list;
    }
}
