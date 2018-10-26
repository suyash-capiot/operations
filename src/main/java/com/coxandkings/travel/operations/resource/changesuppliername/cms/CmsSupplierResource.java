package com.coxandkings.travel.operations.resource.changesuppliername.cms;

import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;


public class CmsSupplierResource {

    private String cm_id;
    private String name;
    private String supp_id;

    public CmsSupplierResource(){}

    public CmsSupplierResource(String cm_id, String cm_name, String supp_id){
        this.cm_id = cm_id;
        this.name = cm_name;
        this.supp_id = supp_id;
    }

    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("cm_id",cm_id);
        jsonObject.put("name", name);
        jsonObject.put("supp_id",supp_id);

        return jsonObject;
    }

    public String getCm_id() {
        return cm_id;
    }

    public void setCm_id(String cm_id) {
        this.cm_id = cm_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupp_id() {
        return supp_id;
    }

    public void setSupp_id(String supp_id) {
        this.supp_id = supp_id;
    }
}
