package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OpsClientEntityCommercial implements Serializable {

    @JsonProperty("clientID")
    private String clientID;

    @JsonProperty("parentClientID")
    private String parentClientID;

    @JsonProperty("clientCommercials")
    private List<OpsPaxRoomClientCommercial> opsPaxRoomClientCommercial = new ArrayList<OpsPaxRoomClientCommercial>();

    @JsonProperty("commercialEntityType")
    private String commercialEntityType;

    @JsonProperty("commercialEntityID")
    private String commercialEntityID;

    @JsonProperty("entityName")
    private String entityName;
    
    @JsonProperty("clientMarket")
    private String clientMarket;
    
    @JsonProperty("commercialEntityMarket")
    private String commercialEntityMarket;

    public OpsClientEntityCommercial() {
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getParentClientID() {
        return parentClientID;
    }

    public void setParentClientID(String parentClientID) {
        this.parentClientID = parentClientID;
    }

    public List<OpsPaxRoomClientCommercial> getOpsPaxRoomClientCommercial() {
        return opsPaxRoomClientCommercial;
    }

    public void setOpsPaxRoomClientCommercial(List<OpsPaxRoomClientCommercial> opsPaxRoomClientCommercial) {
        this.opsPaxRoomClientCommercial = opsPaxRoomClientCommercial;
    }

    public String getCommercialEntityType() {
        return commercialEntityType;
    }

    public void setCommercialEntityType(String commercialEntityType) {
        this.commercialEntityType = commercialEntityType;
    }

    public String getCommercialEntityID() {
        return commercialEntityID;
    }

    public void setCommercialEntityID(String commercialEntityID) {
        this.commercialEntityID = commercialEntityID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsClientEntityCommercial that = (OpsClientEntityCommercial) o;
        return Objects.equals(clientID, that.clientID) &&
                Objects.equals(parentClientID, that.parentClientID) &&
                Objects.equals(opsPaxRoomClientCommercial, that.opsPaxRoomClientCommercial) &&
                Objects.equals(commercialEntityType, that.commercialEntityType) &&
                Objects.equals(commercialEntityID, that.commercialEntityID);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clientID, parentClientID, opsPaxRoomClientCommercial, commercialEntityType, commercialEntityID);
    }

    @JsonProperty("entityName")
	public String getEntityName() {
		return entityName;
	}

    @JsonProperty("entityName")
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

    @JsonProperty("clientMarket")
	public String getClientMarket() {
		return clientMarket;
	}

    @JsonProperty("clientMarket")
	public void setClientMarket(String clientMarket) {
		this.clientMarket = clientMarket;
	}

    @JsonProperty("commercialEntityMarket")
	public String getCommercialEntityMarket() {
		return commercialEntityMarket;
	}

    @JsonProperty("commercialEntityMarket")
	public void setCommercialEntityMarket(String commercialEntityMarket) {
		this.commercialEntityMarket = commercialEntityMarket;
	}
}
