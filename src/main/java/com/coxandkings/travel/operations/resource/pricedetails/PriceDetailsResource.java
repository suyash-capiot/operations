package com.coxandkings.travel.operations.resource.pricedetails;

import com.coxandkings.travel.operations.model.pricedetails.PriceComponentInfo;

import java.util.ArrayList;
import java.util.List;

public class PriceDetailsResource {

    private List<PriceComponentInfo> priceComponentInfo = new ArrayList<>();

    private String total;

    public PriceDetailsResource()   {
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<PriceComponentInfo> getPriceComponentInfo() {
        return priceComponentInfo;
    }

    public void setPriceComponentInfo(List<PriceComponentInfo> priceComponentInfo) {
        this.priceComponentInfo = priceComponentInfo;
    }
}
