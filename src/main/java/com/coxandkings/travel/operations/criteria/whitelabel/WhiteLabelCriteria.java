package com.coxandkings.travel.operations.criteria.whitelabel;

import com.coxandkings.travel.operations.criteria.BaseCriteria;
import com.coxandkings.travel.operations.model.whitelabel.ConfigurationTypeEnumHandler;

public class WhiteLabelCriteria  extends BaseCriteria{

    private String clientMarketId;
    private String pointOfSales;
    private String domainUrl;
    private ConfigurationTypeEnumHandler configurationTypeEnumHandler;

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

    public ConfigurationTypeEnumHandler getConfigurationTypeEnumHandler() {
        return configurationTypeEnumHandler;
    }

    public void setConfigurationTypeEnumHandler(ConfigurationTypeEnumHandler configurationTypeEnumHandler) {
        this.configurationTypeEnumHandler = configurationTypeEnumHandler;
    }
}
