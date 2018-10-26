
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "taxes",
    "supplierPrice",
    "currencyCode"
})
public class SupplierPriceInfo implements Serializable
{

    @JsonProperty("taxes")
    private Taxes taxes;
    @JsonProperty("supplierPrice")
    private String supplierPrice;
    @JsonProperty("currencyCode")
    private String currencyCode;
    private final static long serialVersionUID = 6401081391785470594L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public SupplierPriceInfo() {
    }

    /**
     * 
     * @param taxes
     * @param supplierPrice
     * @param currencyCode
     */
    public SupplierPriceInfo(Taxes taxes, String supplierPrice, String currencyCode) {
        super();
        this.taxes = taxes;
        this.supplierPrice = supplierPrice;
        this.currencyCode = currencyCode;
    }

    @JsonProperty("taxes")
    public Taxes getTaxes() {
        return taxes;
    }

    @JsonProperty("taxes")
    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }

    @JsonProperty("supplierPrice")
    public String getSupplierPrice() {
        return supplierPrice;
    }

    @JsonProperty("supplierPrice")
    public void setSupplierPrice(String supplierPrice) {
        this.supplierPrice = supplierPrice;
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SupplierPriceInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("taxes");
        sb.append('=');
        sb.append(((this.taxes == null)?"<null>":this.taxes));
        sb.append(',');
        sb.append("supplierPrice");
        sb.append('=');
        sb.append(((this.supplierPrice == null)?"<null>":this.supplierPrice));
        sb.append(',');
        sb.append("currencyCode");
        sb.append('=');
        sb.append(((this.currencyCode == null)?"<null>":this.currencyCode));
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
        result = ((result* 31)+((this.taxes == null)? 0 :this.taxes.hashCode()));
        result = ((result* 31)+((this.supplierPrice == null)? 0 :this.supplierPrice.hashCode()));
        result = ((result* 31)+((this.currencyCode == null)? 0 :this.currencyCode.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SupplierPriceInfo) == false) {
            return false;
        }
        SupplierPriceInfo rhs = ((SupplierPriceInfo) other);
        return ((((this.taxes == rhs.taxes)||((this.taxes!= null)&&this.taxes.equals(rhs.taxes)))&&((this.supplierPrice == rhs.supplierPrice)||((this.supplierPrice!= null)&&this.supplierPrice.equals(rhs.supplierPrice))))&&((this.currencyCode == rhs.currencyCode)||((this.currencyCode!= null)&&this.currencyCode.equals(rhs.currencyCode))));
    }

}
