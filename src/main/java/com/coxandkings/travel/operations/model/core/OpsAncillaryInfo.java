package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsAncillaryInfo implements Serializable {

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("quantity")
    private String quantity;

    @JsonProperty("description")
    private String description;

    @JsonProperty("type")
    private String type;

    private final static long serialVersionUID = -3821676439604739123L;

    public OpsAncillaryInfo() {
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsAncillaryInfo that = (OpsAncillaryInfo) o;
        return Objects.equals(unit, that.unit) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(description, that.description) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {

        return Objects.hash(unit, amount, quantity, description, type);
    }
}
