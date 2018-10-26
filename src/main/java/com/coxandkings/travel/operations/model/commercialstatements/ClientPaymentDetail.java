package com.coxandkings.travel.operations.model.commercialstatements;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class ClientPaymentDetail {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @OneToOne(cascade = CascadeType.MERGE)//persist
    @JoinColumn(name = "paymentAdviceId")
    private ClientPaymentAdvice paymentAdviceDetails;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "client_payment_detail")
    private Set<ClientCommercialStatementPaymentDetail> clientCommercialStatementPaymentDetails;

    @Column(name = "document_reference_id")
    @ElementCollection(targetClass = String.class)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> documentReferenceIds = new ArrayList<>();

//    @Enumerated(value = EnumType.STRING)
//    @Column(name = "paymentType")
//    private PaymentDetailsType paymentDetailsType;

//    @Column(name="pament_advice_number")
//    private String paymentAdviceNumber;


    public List<String> getDocumentReferenceIds() {
        return documentReferenceIds;
    }

    public void setDocumentReferenceIds(List<String> documentReferenceIds) {
        this.documentReferenceIds = documentReferenceIds;
    }

    public ClientPaymentDetail(){

    }

    public ClientPaymentAdvice getPaymentAdviceDetails() {
        return paymentAdviceDetails;
    }

    public void setPaymentAdviceDetails(ClientPaymentAdvice paymentAdviceDetails) {
        this.paymentAdviceDetails = paymentAdviceDetails;
    }

//    public String getPaymentAdviceNumber() {
//        return paymentAdviceNumber;
//    }
//
//    public void setPaymentAdviceNumber(String paymentAdviceNumber) {
//        this.paymentAdviceNumber = paymentAdviceNumber;
//    }

    public Set<ClientCommercialStatementPaymentDetail> getClientCommercialStatementPaymentDetails() {
        return clientCommercialStatementPaymentDetails;
    }

    public void setClientCommercialStatementPaymentDetails(Set<ClientCommercialStatementPaymentDetail> clientCommercialStatementPaymentDetails) {
        this.clientCommercialStatementPaymentDetails = clientCommercialStatementPaymentDetails;
    }

//    public PaymentDetailsType getPaymentDetailsType() {
//        return paymentDetailsType;
//    }
//
//    public void setPaymentDetailsType(PaymentDetailsType paymentDetailsType) {
//        this.paymentDetailsT  ype = paymentDetailsType;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
