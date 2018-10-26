package com.coxandkings.travel.operations.model.mailroomanddispatch;


import com.coxandkings.travel.operations.enums.mailroomanddispatch.ParcelType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="create_parcel")
public class Parcel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="id")
    private String id;
    private String parcelId;
    private Double weight;
    @Enumerated
    private ParcelType parcelType;
    @OneToOne (cascade=CascadeType.ALL)
    private BaseParcelRecipientDetails recipientDetailsList;
    @JsonIgnore
    @OneToMany(mappedBy = "parcel",cascade = CascadeType.ALL)
    private List<OutboundDispatch> outboundDispatches;

    /*@ManyToOne
    @JoinColumn(name = "statusId")
    private OutboundStatus outboundStatus;*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParcelId() {
        return parcelId;
    }

    public void setParcelId(String parcelId) {
        this.parcelId = parcelId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public ParcelType getParcelType() {
        return parcelType;
    }

    public void setParcelType(ParcelType parcelType) {
        this.parcelType = parcelType;
    }

    public BaseParcelRecipientDetails getRecipientDetailsList() {
        return recipientDetailsList;
    }

    public void setRecipientDetailsList(BaseParcelRecipientDetails recipientDetailsList) {
        this.recipientDetailsList = recipientDetailsList;
    }

    public List<OutboundDispatch> getOutboundDispatches() {
        return outboundDispatches;
    }

    public void setOutboundDispatches(List<OutboundDispatch> outboundDispatches) {
        this.outboundDispatches = outboundDispatches;
    }

    /* public OutboundStatus getOutboundStatus() {
        return outboundStatus;
    }

    public void setOutboundStatus(OutboundStatus outboundStatus) {
        this.outboundStatus = outboundStatus;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parcel parcel = (Parcel) o;
        return Objects.equals(parcelId, parcel.parcelId) &&
                Objects.equals(weight, parcel.weight) &&
                Objects.equals(parcelType, parcel.parcelType) &&
                Objects.equals(recipientDetailsList, parcel.recipientDetailsList) ;
               // Objects.equals(outboundStatus, parcel.outboundStatus);
    }

    @Override
    public int hashCode() {

        return Objects.hash(parcelId, weight, parcelType, recipientDetailsList);
    }

    @Override
    public String toString() {
        return "Parcel{" +
                "parcelId='" + parcelId + '\'' +
                ", weight=" + weight +
                ", parcelType='" + parcelType + '\'' +
                ", recipientDetailsList=" + recipientDetailsList +
               // ", outboundStatus=" + outboundStatus +
                '}';
    }
}
