package com.coxandkings.travel.operations.model.referal;

import com.coxandkings.travel.operations.enums.referal.TypeOfClient;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "reference_customer_details")
public class ReferenceCustomerDetails {

    @Id
    @GenericGenerator(name = "sequence_referal_id", strategy = "com.coxandkings.travel.operations.resource.referal.ReferalIdGenerator")
    @GeneratedValue(generator = "sequence_referal_id")
    private String clientReferalID;
    private String passengerName;
    private String passengerInformation;
    @Enumerated(EnumType.STRING)
    private TypeOfClient typeOfClient;
    private String referedBy;
    private String refereeDetails;
    private String bookID;
    private String status;

    public ReferenceCustomerDetails() {
    }

    public String getClientReferalID() {
        return clientReferalID;
    }

    public void setClientReferalID(String clientReferalID) {
        this.clientReferalID = clientReferalID;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerInformation() {
        return passengerInformation;
    }

    public void setPassengerInformation(String passengerInformation) {
        this.passengerInformation = passengerInformation;
    }

    public TypeOfClient getTypeOfClient() {
        return typeOfClient;
    }

    public void setTypeOfClient(TypeOfClient typeOfClient) {
        this.typeOfClient = typeOfClient;
    }

    public String getReferedBy() {
        return referedBy;
    }

    public void setReferedBy(String referedBy) {
        this.referedBy = referedBy;
    }

    public String getRefereeDetails() {
        return refereeDetails;
    }

    public void setRefereeDetails(String refereeDetails) {
        this.refereeDetails = refereeDetails;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
