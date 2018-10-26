package com.coxandkings.travel.operations.resource.prepaymenttosupplier.response;

public class PaymentAdviceResponse {

    private String paymentAdviceNumber;
    private String paymentAdviceStatusId;
    private Boolean guaranteeToSupplier;
    private Boolean payToSupplier;


    private String paymentAdviceGenerationDueDate;

    private String dayOfPaymentAdviceGeneration;
    private String typeOfSettlement;
    private String selectedSupplierCurrency;
    private String amountPayableForSupplier;
    private String modeOfPayment;
    private String advancedType;
    private String paymentRemark;

    public String getPaymentAdviceNumber() {
        return paymentAdviceNumber;
    }

    public void setPaymentAdviceNumber(String paymentAdviceNumber) {
        this.paymentAdviceNumber = paymentAdviceNumber;
    }

    public String getPaymentAdviceStatusId() {
        return paymentAdviceStatusId;
    }

    public void setPaymentAdviceStatusId(String paymentAdviceStatusId) {
        this.paymentAdviceStatusId = paymentAdviceStatusId;
    }

    public Boolean getGuaranteeToSupplier() {
        return guaranteeToSupplier;
    }

    public void setGuaranteeToSupplier(Boolean guaranteeToSupplier) {
        this.guaranteeToSupplier = guaranteeToSupplier;
    }

    public Boolean getPayToSupplier() {
        return payToSupplier;
    }

    public void setPayToSupplier(Boolean payToSupplier) {
        this.payToSupplier = payToSupplier;
    }

    public String getPaymentAdviceGenerationDueDate() {
        return paymentAdviceGenerationDueDate;
    }

    public void setPaymentAdviceGenerationDueDate(String paymentAdviceGenerationDueDate) {
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

    public String getAmountPayableForSupplier() {
        return amountPayableForSupplier;
    }

    public void setAmountPayableForSupplier(String amountPayableForSupplier) {
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
}
