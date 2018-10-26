package com.coxandkings.travel.operations.resource.whitelabel;

import com.coxandkings.travel.operations.resource.BaseResource;

import java.util.Set;


public class WhiteLabelResource extends BaseResource {

    private String clientMarketId;
    private String pointOfSales;
    private String domainUrl;
    private Set<String> languageIds;
    private Set<String> currencyIds;
    private String whiteLabelEnumHandlerId;

    public String getClientMarketId() {
        return clientMarketId;
    }

    public void setClientMarketId(String clientMarketId) {
        this.clientMarketId = clientMarketId;
    }


    public String getPointOfSales() {
        return pointOfSales;
    }

    public void setPointOfSales(String pointOfSales) {
        this.pointOfSales = pointOfSales;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public Set<String> getLanguageIds() {
        return languageIds;
    }

    public void setLanguageIds(Set<String> languageIds) {
        this.languageIds = languageIds;
    }

    public Set<String> getCurrencyIds() {
        return currencyIds;
    }

    public void setCurrencyIds(Set<String> currencyIds) {
        this.currencyIds = currencyIds;
    }

    public String getWhiteLabelEnumHandlerId() {
        return whiteLabelEnumHandlerId;
    }

    public void setWhiteLabelEnumHandlerId(String whiteLabelEnumHandlerId) {
        this.whiteLabelEnumHandlerId = whiteLabelEnumHandlerId;
    }
}