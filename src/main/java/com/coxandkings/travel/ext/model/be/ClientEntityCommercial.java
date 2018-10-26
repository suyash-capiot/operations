
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "clientID",
    "clientCommercials",
    "parentClientID",
    "commercialEntityType",
    "commercialEntityID"
})
public class ClientEntityCommercial implements Serializable
{

    @JsonProperty("clientID")
    private String clientID;
    @JsonProperty("clientCommercials")
    private List<ClientCommercial> clientCommercials = new ArrayList<ClientCommercial>();
    @JsonProperty("parentClientID")
    private String parentClientID;
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
    
   
    
    private final static long serialVersionUID = 5070275388598335411L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ClientEntityCommercial() {
    }

    /**
     * 
     * @param clientID
     * @param clientCommercials
     * @param parentClientID
     * @param commercialEntityType
     * @param commercialEntityID
     */
    public ClientEntityCommercial(String clientID, List<ClientCommercial> clientCommercials, String parentClientID, String commercialEntityType, String commercialEntityID) {
        super();
        this.clientID = clientID;
        this.clientCommercials = clientCommercials;
        this.parentClientID = parentClientID;
        this.commercialEntityType = commercialEntityType;
        this.commercialEntityID = commercialEntityID;
    }

    @JsonProperty("clientID")
    public String getClientID() {
        return clientID;
    }

    @JsonProperty("clientID")
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @JsonProperty("clientCommercials")
    public List<ClientCommercial> getClientCommercials() {
        return clientCommercials;
    }

    @JsonProperty("clientCommercials")
    public void setClientCommercials(List<ClientCommercial> clientCommercials) {
        this.clientCommercials = clientCommercials;
    }

    @JsonProperty("parentClientID")
    public String getParentClientID() {
        return parentClientID;
    }

    @JsonProperty("parentClientID")
    public void setParentClientID(String parentClientID) {
        this.parentClientID = parentClientID;
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
    public String getEntityName() {
        return entityName;
    }

    @JsonProperty("entityName")
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ClientEntityCommercial.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("clientID");
        sb.append('=');
        sb.append(((this.clientID == null)?"<null>":this.clientID));
        sb.append(',');
        sb.append("clientCommercials");
        sb.append('=');
        sb.append(((this.clientCommercials == null)?"<null>":this.clientCommercials));
        sb.append(',');
        sb.append("parentClientID");
        sb.append('=');
        sb.append(((this.parentClientID == null)?"<null>":this.parentClientID));
        sb.append(',');
        sb.append("commercialEntityType");
        sb.append('=');
        sb.append(((this.commercialEntityType == null)?"<null>":this.commercialEntityType));
        sb.append(',');
        sb.append("commercialEntityID");
        sb.append('=');
        sb.append(((this.commercialEntityID == null)?"<null>":this.commercialEntityID));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.clientID == null)? 0 :this.clientID.hashCode()));
        result = ((result* 31)+((this.clientCommercials == null)? 0 :this.clientCommercials.hashCode()));
        result = ((result* 31)+((this.parentClientID == null)? 0 :this.parentClientID.hashCode()));
        result = ((result* 31)+((this.commercialEntityType == null)? 0 :this.commercialEntityType.hashCode()));
        result = ((result* 31)+((this.commercialEntityID == null)? 0 :this.commercialEntityID.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ClientEntityCommercial) == false) {
            return false;
        }
        ClientEntityCommercial rhs = ((ClientEntityCommercial) other);
        return ((((((this.clientID == rhs.clientID)||((this.clientID!= null)&&this.clientID.equals(rhs.clientID)))&&((this.clientCommercials == rhs.clientCommercials)||((this.clientCommercials!= null)&&this.clientCommercials.equals(rhs.clientCommercials))))&&((this.parentClientID == rhs.parentClientID)||((this.parentClientID!= null)&&this.parentClientID.equals(rhs.parentClientID))))&&((this.commercialEntityType == rhs.commercialEntityType)||((this.commercialEntityType!= null)&&this.commercialEntityType.equals(rhs.commercialEntityType))))&&((this.commercialEntityID == rhs.commercialEntityID)||((this.commercialEntityID!= null)&&this.commercialEntityID.equals(rhs.commercialEntityID))));
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
