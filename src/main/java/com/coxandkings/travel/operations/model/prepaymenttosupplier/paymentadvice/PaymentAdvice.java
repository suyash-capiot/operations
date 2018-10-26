package com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice;

import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentAdviceStatusValues;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.model.commercialstatements.PaymentAdviceStatementInfo;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PaymentDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JoinColumnOrFormula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

//@SameCurrency
@Entity
@Table(name = "PaymentAdvice")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PaymentAdvice
{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "paymentAdviceId")
    private Set<PaymentAdviceOrderInfo> paymentAdviceOrderInfoSet = new HashSet<PaymentAdviceOrderInfo>();

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "supplierPaymentAdviceId")
    private Set<PaymentAdviceStatementInfo> paymentAdviceStatementInfoSet;

    @Column(name = "supplierReferenceId")
    private String supplierRefId;           //TODO: prepopulate as per booking(Non-editable)

    @Column(name = "supplierName")
    private String supplierName;            //TODO: prepopulate as per booking(Non-editable)

    @Column(name = "paymentDueDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime paymentDueDate;  //TODO: as per payment schedule for booking(editable)

    @Column(name = "dayOfPayment")
    private String dayOfPayment;        //TODO: display as per paymentDueDate(Non-editable)

    @Column(name = "supplierCurrency")
    private String supplierCurrency;     //TODO: prepopulate as per booking(if multiple currency - dropdown or else single currency)

    @Column(name = "netPayableToSupplier")
    private BigDecimal netPayableToSupplier;    //TODO: prepopulate as per bookings(Non-editable)

    @Column(name = "balanceAmtPayableToSupplier")
    private BigDecimal balanceAmtPayableToSupplier;         //TODO: prepopulate as per bookings(Non-editable)

    @Column(name = "prePaymentApplicable")
    private Boolean prePaymentApplicable;

    @Column(name = "approverRemark")
    private String approverRemark;

    @Column(name = "financeApprovalStatus")
    private String financeApprovalStatus;

    @Column(name = "paymentAdviceNumber")
    private String paymentAdviceNumber;         //TODO: Auto generated

    @Enumerated(value = EnumType.STRING)
    @Column(name = "paymentAdviceStatus")
    private PaymentAdviceStatusValues paymentAdviceStatus;

    @Column(name = "guaranteeToSupplier")
    private boolean guaranteeToSupplier;        //TODO: (editable)

    @Column(name = "payToSupplier")             //TODO: (editable)
    private boolean payToSupplier;

    @Column(name = "paymentAdviceGenerationDueDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime paymentAdviceGenerationDueDate;       //TODO:prepopulated as per supplier settlement (Non-editable)

    @Column(name = "dayOfPaymentAdviceGeneration")
    private String dayOfPaymentAdviceGeneration;                 //TODO:prepopulated as per paymentAdviceGenerationDueDate (Non-editable)

    @Column(name = "typeOfSettlement")
    private String typeOfSettlement;                       //TODO: prepopulated as per supplier settlement (Non-editable)

    @Column(name = "selectedSupplierCurrency")
    private String selectedSupplierCurrency;

    @Column(name = "modeOfPayment")
    private String modeOfPayment;

    @Column(name = "advancedTypeId")
    private String advancedType;

    @Column(name = "paymentRemark")
    private String paymentRemark;

    @Column(name = "amountPayableForSupplier")
    private BigDecimal amountPayableForSupplier;        //TODO: by default display balance amt

    @Column(name = "isUiTrigger")
    private Boolean isUiTrigger;

    @Column(name = "documentReferenceId")
    private String documentReferenceId;


    @Column(name = "paymentAdviceGenerationDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime paymentAdviceGenerationDate;

    @Transient
    private String companyId = "GC22";  // todo needed for finance

    @Column(name = "approverToDoTaskId")
    private String approverToDoTaskId;

    @Column(name = "financeTodoTaskId")
    private String financeTodoTaskId;

    public Set<PaymentAdviceStatementInfo> getPaymentAdviceStatementInfoSet() {
        return paymentAdviceStatementInfoSet;
    }

    public void setPaymentAdviceStatementInfoSet(Set<PaymentAdviceStatementInfo> paymentAdviceStatementInfoSet) {
        this.paymentAdviceStatementInfoSet = paymentAdviceStatementInfoSet;
    }

    public String getApproverToDoTaskId() {
        return approverToDoTaskId;
    }

    public void setApproverToDoTaskId(String approverToDoTaskId) {
        this.approverToDoTaskId = approverToDoTaskId;
    }

    public String getFinanceTodoTaskId() {
        return financeTodoTaskId;
    }

    public void setFinanceTodoTaskId(String financeTodoTaskId) {
        this.financeTodoTaskId = financeTodoTaskId;
    }

    public ZonedDateTime getPaymentAdviceGenerationDate() {
        return paymentAdviceGenerationDate;
    }

    public void setPaymentAdviceGenerationDate(ZonedDateTime paymentAdviceGenerationDate) {
        this.paymentAdviceGenerationDate = paymentAdviceGenerationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<PaymentAdviceOrderInfo> getPaymentAdviceOrderInfoSet() {
        return paymentAdviceOrderInfoSet;
    }

    public void setPaymentAdviceOrderInfoSet(Set<PaymentAdviceOrderInfo> paymentAdviceOrderInfoSet) {
        this.paymentAdviceOrderInfoSet = paymentAdviceOrderInfoSet;
    }

    public String getSupplierRefId() {
        return supplierRefId;
    }

    public void setSupplierRefId(String supplierRefId) {
        this.supplierRefId = supplierRefId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public ZonedDateTime getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(ZonedDateTime paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public String getDayOfPayment() {
        return dayOfPayment;
    }

    public void setDayOfPayment(String dayOfPayment) {
        this.dayOfPayment = dayOfPayment;
    }

    public String getSupplierCurrency() {
        return supplierCurrency;
    }

    public void setSupplierCurrency(String supplierCurrency) {
        this.supplierCurrency = supplierCurrency;
    }

    public BigDecimal getNetPayableToSupplier() {
        return netPayableToSupplier;
    }

    public void setNetPayableToSupplier(BigDecimal netPayableToSupplier) {
        this.netPayableToSupplier = netPayableToSupplier;
    }

    public BigDecimal getBalanceAmtPayableToSupplier() {
        return balanceAmtPayableToSupplier;
    }

    public void setBalanceAmtPayableToSupplier(BigDecimal balanceAmtPayableToSupplier) {
        this.balanceAmtPayableToSupplier = balanceAmtPayableToSupplier;
    }

    public Boolean getPrePaymentApplicable() {
        return prePaymentApplicable;
    }

    public void setPrePaymentApplicable(Boolean prePaymentApplicable) {
        this.prePaymentApplicable = prePaymentApplicable;
    }

    public String getApproverRemark() {
        return approverRemark;
    }

    public void setApproverRemark(String approverRemark) {
        this.approverRemark = approverRemark;
    }

    public String getFinanceApprovalStatus() {
        return financeApprovalStatus;
    }

    public void setFinanceApprovalStatus(String financeApprovalStatus) {
        this.financeApprovalStatus = financeApprovalStatus;
    }

    public String getPaymentAdviceNumber() {
        return paymentAdviceNumber;
    }

    public void setPaymentAdviceNumber(String paymentAdviceNumber) {
        this.paymentAdviceNumber = paymentAdviceNumber;
    }

    public PaymentAdviceStatusValues getPaymentAdviceStatus() {
        return paymentAdviceStatus;
    }

    public void setPaymentAdviceStatus(PaymentAdviceStatusValues paymentAdviceStatus) {
        this.paymentAdviceStatus = paymentAdviceStatus;
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

    public BigDecimal getAmountPayableForSupplier() {
        return amountPayableForSupplier;
    }

    public void setAmountPayableForSupplier(BigDecimal amountPayableForSupplier) {
        this.amountPayableForSupplier = amountPayableForSupplier;
    }

    public Boolean getUiTrigger() {
        return isUiTrigger;
    }

    public void setUiTrigger(Boolean uiTrigger) {
        isUiTrigger = uiTrigger;
    }

    public String getDocumentReferenceId() {
        return documentReferenceId;
    }

    public void setDocumentReferenceId(String documentReferenceId) {
        this.documentReferenceId = documentReferenceId;
    }

   /* public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }*/

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
