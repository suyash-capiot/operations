
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "totalFare",
    "paxType",
    "baseFare",
    "fees",
    "taxes",
    "supplierCommercials"
})
public class PaxTypeFare implements Serializable
{

    @JsonProperty("totalFare")
    private TotalFare totalFare;
    @JsonProperty("paxType")
    private String paxType;
    @JsonProperty("baseFare")
    private BaseFare baseFare;
    @JsonProperty("fees")
    private Fees fees;
    @JsonProperty("taxes")
    private Taxes taxes;
    @JsonProperty("supplierCommercials")
    private List<SupplierCommercial> supplierCommercials = new ArrayList<SupplierCommercial>();
    @JsonProperty("clientEntityCommercials")
    private List<ClientEntityCommercial> clientEntityCommercials = new ArrayList<ClientEntityCommercial>();

    @JsonProperty("quantity")
    private String quantity;
    @JsonProperty("totalPrice")
    private String totalPrice;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("age")
    private String age;
    @JsonProperty("participantCategory")
    private String participantCategory;

//    @JsonProperty("offers")
//    private Offers offers;
    private final static long serialVersionUID = -8035857533158441490L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PaxTypeFare() {
    }

    /**
     * 
     * @param totalFare
     * @param paxType
     * @param baseFare
     * @param fees
     * @param taxes
     * @param supplierCommercials
     */
    public PaxTypeFare(TotalFare totalFare, String paxType, BaseFare baseFare, Fees fees, Taxes taxes, List<SupplierCommercial> supplierCommercials) {
        super();
        this.totalFare = totalFare;
        this.paxType = paxType;
        this.baseFare = baseFare;
        this.fees = fees;
        this.taxes = taxes;
        this.supplierCommercials = supplierCommercials;
    }

//    public Offers getOffers() {
//        return offers;
//    }
//
//    public void setOffers(Offers offers) {
//        this.offers = offers;
//    }

    @JsonProperty("totalFare")
    public TotalFare getTotalFare() {
        return totalFare;
    }

    @JsonProperty("totalFare")
    public void setTotalFare(TotalFare totalFare) {
        this.totalFare = totalFare;
    }

    @JsonProperty("paxType")
    public String getPaxType() {
        return paxType;
    }

    @JsonProperty("paxType")
    public void setPaxType(String paxType) {
        this.paxType = paxType;
    }

    @JsonProperty("baseFare")
    public BaseFare getBaseFare() {
        return baseFare;
    }

    @JsonProperty("baseFare")
    public void setBaseFare(BaseFare baseFare) {
        this.baseFare = baseFare;
    }

    @JsonProperty("fees")
    public Fees getFees() {
        return fees;
    }

    @JsonProperty("fees")
    public void setFees(Fees fees) {
        this.fees = fees;
    }

    @JsonProperty("taxes")
    public Taxes getTaxes() {
        return taxes;
    }

    @JsonProperty("taxes")
    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }

    @JsonProperty("supplierCommercials")
    public List<SupplierCommercial> getSupplierCommercials() {
        return supplierCommercials;
    }

    @JsonProperty("supplierCommercials")
    public void setSupplierCommercials(List<SupplierCommercial> supplierCommercials) {
        this.supplierCommercials = supplierCommercials;
    }

    public List<ClientEntityCommercial> getClientEntityCommercials() {
        return clientEntityCommercials;
    }

    public void setClientEntityCommercials(List<ClientEntityCommercial> clientEntityCommercials) {
        this.clientEntityCommercials = clientEntityCommercials;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getParticipantCategory() {
        return participantCategory;
    }

    public void setParticipantCategory(String participantCategory) {
        this.participantCategory = participantCategory;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PaxTypeFare.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("totalFare");
        sb.append('=');
        sb.append(((this.totalFare == null)?"<null>":this.totalFare));
        sb.append(',');
        sb.append("paxType");
        sb.append('=');
        sb.append(((this.paxType == null)?"<null>":this.paxType));
        sb.append(',');
        sb.append("baseFare");
        sb.append('=');
        sb.append(((this.baseFare == null)?"<null>":this.baseFare));
        sb.append(',');
        sb.append("fees");
        sb.append('=');
        sb.append(((this.fees == null)?"<null>":this.fees));
        sb.append(',');
        sb.append("taxes");
        sb.append('=');
        sb.append(((this.taxes == null)?"<null>":this.taxes));
        sb.append(',');
        sb.append("supplierCommercials");
        sb.append('=');
        sb.append(((this.supplierCommercials == null)?"<null>":this.supplierCommercials));
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
        result = ((result* 31)+((this.totalFare == null)? 0 :this.totalFare.hashCode()));
        result = ((result* 31)+((this.paxType == null)? 0 :this.paxType.hashCode()));
        result = ((result* 31)+((this.baseFare == null)? 0 :this.baseFare.hashCode()));
        result = ((result* 31)+((this.fees == null)? 0 :this.fees.hashCode()));
        result = ((result* 31)+((this.taxes == null)? 0 :this.taxes.hashCode()));
        result = ((result* 31)+((this.supplierCommercials == null)? 0 :this.supplierCommercials.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PaxTypeFare) == false) {
            return false;
        }
        PaxTypeFare rhs = ((PaxTypeFare) other);
        return (((((((this.totalFare == rhs.totalFare)||((this.totalFare!= null)&&this.totalFare.equals(rhs.totalFare)))&&((this.paxType == rhs.paxType)||((this.paxType!= null)&&this.paxType.equals(rhs.paxType))))&&((this.baseFare == rhs.baseFare)||((this.baseFare!= null)&&this.baseFare.equals(rhs.baseFare))))&&((this.fees == rhs.fees)||((this.fees!= null)&&this.fees.equals(rhs.fees))))&&((this.taxes == rhs.taxes)||((this.taxes!= null)&&this.taxes.equals(rhs.taxes))))&&((this.supplierCommercials == rhs.supplierCommercials)||((this.supplierCommercials!= null)&&this.supplierCommercials.equals(rhs.supplierCommercials))));
    }

}
