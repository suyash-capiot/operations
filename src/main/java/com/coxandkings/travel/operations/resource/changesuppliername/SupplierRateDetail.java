package com.coxandkings.travel.operations.resource.changesuppliername;

import org.json.JSONObject;

public class SupplierRateDetail {
    private SupplierHeaderRate supplierHeaderRate;
    //@JsonIgnore
    private JSONObject supplierBodyRate;

    public SupplierHeaderRate getSupplierHeaderRate() {
        return supplierHeaderRate;
    }

    public void setSupplierHeaderRate(SupplierHeaderRate supplierHeaderRate) {
        this.supplierHeaderRate = supplierHeaderRate;
    }

    public JSONObject getSupplierBodyRate() {
        return supplierBodyRate;
    }

    public void setSupplierBodyRate(JSONObject supplierBodyRate) {
        this.supplierBodyRate = supplierBodyRate;
    }
}
