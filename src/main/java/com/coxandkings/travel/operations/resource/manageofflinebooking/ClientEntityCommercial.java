package com.coxandkings.travel.operations.resource.manageofflinebooking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "clientID",
    "parentClientID",
    "clientCommercials",
    "commercialEntityType",
    "commercialEntityID",
    "entityName",
    "clientMarket",
    "commercialEntityMarket"
})
public class ClientEntityCommercial {

    @JsonProperty("clientID")
    private String clientID;
    @JsonProperty("parentClientID")
    private String parentClientID;
    @JsonProperty("clientCommercials")
    private List<ClientCommercial> clientCommercials = null;
    @JsonProperty("commercialEntityType")
    private String commercialEntityType;
    @JsonProperty("commercialEntityID")
    private String commercialEntityID;
    @JsonProperty("entityName")
    private Object entityName;
    @JsonProperty("clientMarket")
    private Object clientMarket;
    @JsonProperty("commercialEntityMarket")
    private Object commercialEntityMarket;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("clientID")
    public String getClientID() {
        return clientID;
    }

    @JsonProperty("clientID")
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @JsonProperty("parentClientID")
    public String getParentClientID() {
        return parentClientID;
    }

    @JsonProperty("parentClientID")
    public void setParentClientID(String parentClientID) {
        this.parentClientID = parentClientID;
    }

    @JsonProperty("clientCommercials")
    public List<ClientCommercial> getClientCommercials() {
        return clientCommercials;
    }

    @JsonProperty("clientCommercials")
    public void setClientCommercials(List<ClientCommercial> clientCommercials) {
        this.clientCommercials = clientCommercials;
    }

    @JsonProperty("commercialEntityType")
    public String getCommercialEntityType() {
        return commercialEntityType;
    }

    @JsonProperty("commercialEntityType")
    public void setCommercialEntityType(String commercialEntityType) {
        this.commercialEntityType = commercialEntityType;
    }

    @JsonProperty("commercialEntityID")
    public String getCommercialEntityID() {
        return commercialEntityID;
    }

    @JsonProperty("commercialEntityID")
    public void setCommercialEntityID(String commercialEntityID) {
        this.commercialEntityID = commercialEntityID;
    }

    @JsonProperty("entityName")
    public Object getEntityName() {
        return entityName;
    }

    @JsonProperty("entityName")
    public void setEntityName(Object entityName) {
        this.entityName = entityName;
    }

    @JsonProperty("clientMarket")
    public Object getClientMarket() {
        return clientMarket;
    }

    @JsonProperty("clientMarket")
    public void setClientMarket(Object clientMarket) {
        this.clientMarket = clientMarket;
    }

    @JsonProperty("commercialEntityMarket")
    public Object getCommercialEntityMarket() {
        return commercialEntityMarket;
    }

    @JsonProperty("commercialEntityMarket")
    public void setCommercialEntityMarket(Object commercialEntityMarket) {
        this.commercialEntityMarket = commercialEntityMarket;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
