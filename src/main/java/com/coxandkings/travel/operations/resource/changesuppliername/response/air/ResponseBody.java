
package com.coxandkings.travel.operations.resource.changesuppliername.response.air;

import com.coxandkings.travel.ext.model.be.PricedItinerary;

import java.util.List;

public class ResponseBody {

    private List<PricedItinerary> pricedItinerary = null;

    public List<PricedItinerary> getPricedItinerary() {
        return pricedItinerary;
    }

    public void setPricedItinerary(List<PricedItinerary> pricedItinerary) {
        this.pricedItinerary = pricedItinerary;
    }

}
