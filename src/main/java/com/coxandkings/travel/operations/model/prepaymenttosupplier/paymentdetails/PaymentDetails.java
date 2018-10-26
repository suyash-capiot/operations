package com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails;

import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentDetailsType;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PaymentDetails")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PaymentDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "paymentType")
    private PaymentDetailsType paymentDetailsType;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_details_id")
    private List<PayToSupplier> payToSupplierSet;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private GuaranteeToSupplier guaranteeToSupplier;

    @OneToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    private PaymentAdvice paymentAdviceNumber;

    @Column(name = "document_reference_id")
    @ElementCollection(targetClass = String.class)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> documentReferenceIds = new ArrayList<>();

    public PaymentAdvice getPaymentAdviceNumber() {
        return paymentAdviceNumber;
    }

    public void setPaymentAdviceNumber(PaymentAdvice paymentAdviceNumber) {
        this.paymentAdviceNumber = paymentAdviceNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PaymentDetailsType getPaymentDetailsType() {
        return paymentDetailsType;
    }

    public void setPaymentDetailsType(PaymentDetailsType paymentDetailsType) {
        this.paymentDetailsType = paymentDetailsType;
    }

    public List<PayToSupplier> getPayToSupplierSet() {
        return payToSupplierSet;
    }

    public void setPayToSupplierSet(List<PayToSupplier> payToSupplierSet) {
        this.payToSupplierSet = payToSupplierSet;
    }

    public GuaranteeToSupplier getGuaranteeToSupplier() {
        return guaranteeToSupplier;
    }

    public void setGuaranteeToSupplier(GuaranteeToSupplier guaranteeToSupplier) {
        this.guaranteeToSupplier = guaranteeToSupplier;
    }

    public List<String> getDocumentReferenceIds() {
        return documentReferenceIds;
    }

    public void setDocumentReferenceIds(List<String> documentReferenceIds) {
        this.documentReferenceIds = documentReferenceIds;
    }
}
