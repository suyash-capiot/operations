package com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice;

import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentAdviceStatusValues;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class PaymentAdviceResource {

    private String paymentAdviceNumber;
    private PaymentAdviceStatusValues paymentAdviceStatusId;
    private boolean guaranteeToSupplier;
    private boolean payToSupplier;

    @JsonProperty("paymentAdviceGenerationDueDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime paymentAdviceGenerationDueDate;

    private String dayOfPaymentAdviceGeneration;
    private String typeOfSettlement;
    private String selectedSupplierCurrency;
    private BigDecimal amountPayableForSupplier;
    private String modeOfPayment;
    private String advancedType;
    private String paymentRemark;
    private String documentReferenceId;
    private Boolean isUiTrigger;


    public String getPaymentAdviceNumber() {
        return paymentAdviceNumber;
    }

    public void setPaymentAdviceNumber(String paymentAdviceNumber) {
        this.paymentAdviceNumber = paymentAdviceNumber;
    }

    public PaymentAdviceStatusValues getPaymentAdviceStatusId() {
        return paymentAdviceStatusId;
    }

    public void setPaymentAdviceStatusId(PaymentAdviceStatusValues paymentAdviceStatusId) {
        this.paymentAdviceStatusId = paymentAdviceStatusId;
    }

    public boolean isGuaranteeToSupplier() {
        return guaranteeToSupplier;
    }

    public void setGuaranteeToSupplier(boolean guaranteeToSupplier) {
        this.guaranteeToSupplier = guaranteeToSupplier;
    }

    public boolean isPayToSupplier() {
        return payToSupplier;
    }

    public void setPayToSupplier(boolean payToSupplier) {
        this.payToSupplier = payToSupplier;
    }

    public ZonedDateTime getPaymentAdviceGenerationDueDate() {
        return paymentAdviceGenerationDueDate;
    }

    public void setPaymentAdviceGenerationDueDate(ZonedDateTime paymentAdviceGenerationDueDate) {
        this.paymentAdviceGenerationDueDate = paymentAdviceGenerationDueDate;
    }

    public String getDayOfPaymentAdviceGeneration() {
        return dayOfPaymentAdviceGeneration;
    }

    public void setDayOfPaymentAdviceGeneration(String dayOfPaymentAdviceGeneration) {
        this.dayOfPaymentAdviceGeneration = dayOfPaymentAdviceGeneration;
    }

    public String getTypeOfSettlement() {
        return typeOfSettlement;
    }

    public void setTypeOfSettlement(String typeOfSettlement) {
        this.typeOfSettlement = typeOfSettlement;
    }

    public String getSelectedSupplierCurrency() {
        return selectedSupplierCurrency;
    }

    public void setSelectedSupplierCurrency(String selectedSupplierCurrency) {
        this.selectedSupplierCurrency = selectedSupplierCurrency;
    }

    public BigDecimal getAmountPayableForSupplier() {
        return amountPayableForSupplier;
    }

    public void setAmountPayableForSupplier(BigDecimal amountPayableForSupplier) {
        this.amountPayableForSupplier = amountPayableForSupplier;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getAdvancedType() {
        return advancedType;
    }

    public void setAdvancedType(String advancedType) {
        this.advancedType = advancedType;
    }

    public String getPaymentRemark() {
        return paymentRemark;
    }

    public void setPaymentRemark(String paymentRemark) {
        this.paymentRemark = paymentRemark;
    }

    public String getDocumentReferenceId() {
        return documentReferenceId;
    }

    public void setDocumentReferenceId(String documentReferenceId) {
        this.documentReferenceId = documentReferenceId;
    }

    public Boolean getUiTrigger() {
        return isUiTrigger;
    }

    public void setUiTrigger(Boolean uiTrigger) {
        isUiTrigger = uiTrigger;
    }


}
