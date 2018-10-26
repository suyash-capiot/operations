
package com.coxandkings.travel.ext.model.be;

import java.util.List;

public class AirItineraryPricingInfo {

    private ItinTotalFare itinTotalFare;
    private List<PaxTypeFare> paxTypeFares = null;

    public ItinTotalFare getItinTotalFare() {
        return itinTotalFare;
    }

    public void setItinTotalFare(ItinTotalFare itinTotalFare) {
        this.itinTotalFare = itinTotalFare;
    }

    public List<PaxTypeFare> getPaxTypeFares() {
        return paxTypeFares;
    }

    public void setPaxTypeFares(List<PaxTypeFare> paxTypeFares) {
        this.paxTypeFares = paxTypeFares;
    }

}
