package com.coxandkings.travel.operations.resource.whitelabel;

import com.coxandkings.travel.operations.model.whitelabel.ConfigurationTypeEnumHandler;
import com.coxandkings.travel.operations.model.whitelabel.Language;

import java.util.Set;

public class LandingPageResponse{

    private String id;
    private String clientMarketId;
    private ConfigurationTypeEnumHandler configurationTypeEnumHandler;
    private Set<Language> language;
    private String pointOfSale;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientMarketId() {
        return clientMarketId;
    }

    public void setClientMarketId(String clientMarketId) {
        this.clientMarketId = clientMarketId;
    }

    public ConfigurationTypeEnumHandler getConfigurationTypeEnumHandler() {
        return configurationTypeEnumHandler;
    }

    public void setConfigurationTypeEnumHandler(ConfigurationTypeEnumHandler configurationTypeEnumHandler) {
        this.configurationTypeEnumHandler = configurationTypeEnumHandler;
    }

    public Set<Language> getLanguage() {
        return language;
    }

    public void setLanguage(Set<Language> language) {
        this.language = language;
    }

    public String getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(String pointOfSale) {
        this.pointOfSale = pointOfSale;
    }
}
