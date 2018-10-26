package com.coxandkings.travel.operations.model.commercialstatements;

import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentAdviceStatusValues;
import com.coxandkings.travel.operations.model.BaseModel;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "payment_advice_client")
public class ClientPaymentAdvice  {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    //    @OneToOne(mappedBy = "paymentAdviceDetails", cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY, optional = false)
//    private ClientPaymentDetail clientPaymentDetail;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "paymentDueDate")
    @JsonSerialize(using = ZonedDateSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime paymentDueDate;

    @Column(name = "day_of_payment")
    private String dayOfPayment;

    @Column(name = "supplier_currency")
    private String clientCurrency;

    @Column(name = "net_payable_to_client")
    private BigDecimal netPayableToClient;

    @Column(name = "balanceAmtPayableToClient")
    private BigDecimal balanceAmtPayableToClient;

    @Column
    private BigDecimal amountPaidByFinance;


    @Column(name = "pre_payment_applicable")
    private Boolean prePaymentApplicable;

    @Column(name = "approver_remark")
    private String approverRemark;

    /*@Column(name = "finance_approval_status")
    private String financeApprovalStatus;*/

    @Column(name = "payment_advice_number")
    private String paymentAdviceNumber;

    @Column(name = "payment_advice_status")
    @Enumerated(value = EnumType.STRING)
    private PaymentAdviceStatusValues paymentAdviceStatus;

    @Column(name = "day_of_payment_advice_generation")
    private String dayOfPaymentAdviceGeneration;

  /*  @Column(name = "type_of_settlement")
    private String typeOfSettlement;*/

    /* @Column(name = "selected_client_currency")
     private String selectedClientCurrency;
 */
    @Column(name = "mode_of_payment")
    private String modeOfPayment;

  /*  @Column(name = "advanced_type_id")
    private String advancedType;*/

    @Column(name = "payment_remark")
    private String paymentRemark;

    @Column(name = "amountPayable_for_client")
    private BigDecimal amountPayableForClient;

    @OneToMany(cascade = CascadeType.ALL)//all
    @JoinColumn(name = "clientPaymentAdviceId")
    private Set<PaymentAdviceStatementInfo> paymentAdviceStatementInfoSet;


    public Set<PaymentAdviceStatementInfo> getPaymentAdviceStatementInfoSet() {
        return paymentAdviceStatementInfoSet;
    }

    public void setPaymentAdviceStatementInfoSet(Set<PaymentAdviceStatementInfo> paymentAdviceStatementInfoSet) {
        this.paymentAdviceStatementInfoSet = paymentAdviceStatementInfoSet;
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

   /* public String getFinanceApprovalStatus() {
        return financeApprovalStatus;
    }

    public void setFinanceApprovalStatus(String financeApprovalStatus) {
        this.financeApprovalStatus = financeApprovalStatus;
    }*/

    public String getPaymentAdviceNumber() {
        return paymentAdviceNumber;
    }

    public void setPaymentAdviceNumber(String paymentAdviceNumber) {
        this.paymentAdviceNumber = paymentAdviceNumber;
    }

//    public String getPaymentAdviceStatus() {
//        return paymentAdviceStatus;
//    }
//
//    public void setPaymentAdviceStatus(String paymentAdviceStatus) {
//        this.paymentAdviceStatus = paymentAdviceStatus;
//    }


    public PaymentAdviceStatusValues getPaymentAdviceStatus() {
        return paymentAdviceStatus;
    }

    public void setPaymentAdviceStatus(PaymentAdviceStatusValues paymentAdviceStatus) {
        this.paymentAdviceStatus = paymentAdviceStatus;
    }

    public String getDayOfPaymentAdviceGeneration() {
        return dayOfPaymentAdviceGeneration;
    }

    public void setDayOfPaymentAdviceGeneration(String dayOfPaymentAdviceGeneration) {
        this.dayOfPaymentAdviceGeneration = dayOfPaymentAdviceGeneration;
    }

   /* public String getTypeOfSettlement() {
        return typeOfSettlement;
    }

    public void setTypeOfSettlement(String typeOfSettlement) {
        this.typeOfSettlement = typeOfSettlement;
    }*/

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

   /* public String getAdvancedType() {
        return advancedType;
    }

    public void setAdvancedType(String advancedType) {
        this.advancedType = advancedType;
    }*/

    public String getPaymentRemark() {
        return paymentRemark;
    }

    public void setPaymentRemark(String paymentRemark) {
        this.paymentRemark = paymentRemark;
    }

    public BigDecimal getAmountPayableForClient() {
        return amountPayableForClient;
    }

    public void setAmountPayableForClient(BigDecimal amountPayableForClient) {
        this.amountPayableForClient = amountPayableForClient;
    }

   /* public Boolean getUiTrigger() {
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
    }*/

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientCurrency() {
        return clientCurrency;
    }

    public void setClientCurrency(String clientCurrency) {
        this.clientCurrency = clientCurrency;
    }

    public BigDecimal getNetPayableToClient() {
        return netPayableToClient;
    }

    public void setNetPayableToClient(BigDecimal netPayableToClient) {
        this.netPayableToClient = netPayableToClient;
    }

    public BigDecimal getBalanceAmtPayableToClient() {
        return balanceAmtPayableToClient;
    }

    public void setBalanceAmtPayableToClient(BigDecimal balanceAmtPayableToClient) {
        this.balanceAmtPayableToClient = balanceAmtPayableToClient;
    }

   /* public String getSelectedClientCurrency() {
        return selectedClientCurrency;
    }

    public void setSelectedClientCurrency(String selectedClientCurrency) {
        this.selectedClientCurrency = selectedClientCurrency;
    }*/

    public BigDecimal getAmountPaidByFinance() {
        return amountPaidByFinance;
    }

    public void setAmountPaidByFinance(BigDecimal amountPaidByFinance) {
        this.amountPaidByFinance = amountPaidByFinance;
    }
}
