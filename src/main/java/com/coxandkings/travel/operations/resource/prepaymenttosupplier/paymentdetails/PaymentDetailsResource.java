package com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentdetails;


import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentDetailsType;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.GuaranteeToSupplier;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PayToSupplier;

import java.util.ArrayList;
import java.util.List;

public class PaymentDetailsResource {
    private String id;

    private PaymentDetailsType paymentDetailsType;
    //  @JsonBackReference
    private List<PayToSupplier> payToSupplierSet;
    //  @JsonBackReference
    private GuaranteeToSupplier guaranteeToSupplier;
    // @JsonBackReference
    private PaymentAdvice paymentAdviceNumber;

    private List<String> documentReferenceIds = new ArrayList<>();

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

    public PaymentAdvice getPaymentAdviceNumber() {
        return paymentAdviceNumber;
    }

    public void setPaymentAdviceNumber(PaymentAdvice paymentAdviceNumber) {
        this.paymentAdviceNumber = paymentAdviceNumber;
    }

    public List<String> getDocumentReferenceIds() {
        return documentReferenceIds;
    }

    public void setDocumentReferenceIds(List<String> documentReferenceIds) {
        this.documentReferenceIds = documentReferenceIds;
    }
}
