package com.coxandkings.travel.operations.model.commercialstatements;

import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseCommercialDetails implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "statement_id")
    private String statementId;

    @Column(name = "commercial_type")
    private String commercialType;

    @Column(name = "commercial_head")
    private String commercialHead;

    @Column(name = "currency")
    private String currency;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "product_category_sub_type")
    private String productCategorySubType;

    @Column(name = "product_name_sub_type")
    private String productNameSubType;

    @Column(name = "product_flavour_name")
    private String productFlavourName;

    @Column(name = "product")
    private String productName;

    @Column(name = "final_invoice_number")
    private String finalInvoiceNumber;

    @Column(name = "provisional_invoice_number")
    private String performaInvoiceNumber;

    @Column(name = "statement_name")
    private String statementName;

    @Column(name = "company_market")
    private String companyMarket;

    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    @Column(name = "settlement_due_date")
    private ZonedDateTime settlementDueDate;

    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    @Column(name = "booking_date_from")
    private ZonedDateTime bookingPeriodFrom;

    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    @Column(name = "booking_date_to")
    private ZonedDateTime bookingPeriodTo;

    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    @Column(name = "travel_date_from")
    private ZonedDateTime travelDateFrom;

    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    @Column(name = "travel_date_to")
    private ZonedDateTime travelDateTo;

    @Column(name = "supplier_client_id")
    private String supplierOrClientId;

    @Column(name = "payment_advice_ids")
    @ElementCollection
    private Set<String> paymentAdviceIds;

    @Column(name = "credit_debit_note_number")
    private String creditOrDebitNoteNumber;

    @Column(name = "supplier_client_commercial_id")
    private String supplierOrClientCommercialId;

    @Column(name = "receipt_number" )
    private String receiptNumber;

    @Column(name = "settlement_status")
    private String settlementStatus;

    @Column(name = "supplier_client_name")
    private String supplierOrClientName;

    private BigDecimal balancePayable;

    private BigDecimal balanceReceivable;

    private BigDecimal totalReceived;

    private BigDecimal totalReceivable;

    private BigDecimal totalPaid;

    private BigDecimal totalPayable;

    private BigDecimal writeOffAmount;

    private BigDecimal totalServiceTax;

    private String companyId;

    @Column(name = "commercial_statement_for")
    private String commercialStatementFor;

    private String calculationType;

    private Boolean isPercentage;

    private Boolean isAmount;

    private BigDecimal percentageValue;

    private BigDecimal amountValue;

    @OneToMany(mappedBy = "commercialStatement",cascade = CascadeType.ALL)
    private Set<CommercialStatementDetails> commercialStatementDetails;

    public String getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
    }

    public Boolean getPercentage() {
        return isPercentage;
    }

    public void setPercentage(Boolean percentage) {
        isPercentage = percentage;
    }

    public Boolean getAmount() {
        return isAmount;
    }

    public void setAmount(Boolean amount) {
        isAmount = amount;
    }

    public BigDecimal getPercentageValue() {
        return percentageValue;
    }

    public void setPercentageValue(BigDecimal percentageValue) {
        this.percentageValue = percentageValue;
    }

    public BigDecimal getAmountValue() {
        return amountValue;
    }

    public void setAmountValue(BigDecimal amountValue) {
        this.amountValue = amountValue;
    }

    public String getCommercialType() {
        return commercialType;
    }

    public void setCommercialType(String commercialType) {
        this.commercialType = commercialType;
    }

    public String getCommercialHead() {
        return commercialHead;
    }

    public void setCommercialHead(String commercialHead) {
        this.commercialHead = commercialHead;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatementName() {
        return statementName;
    }

    public void setStatementName(String statementName) {
        this.statementName = statementName;
    }

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public ZonedDateTime getSettlementDueDate() {
        return settlementDueDate;
    }

    public void setSettlementDueDate(ZonedDateTime settlementDueDate) {
        this.settlementDueDate = settlementDueDate;
    }

    public ZonedDateTime getBookingPeriodFrom() {
        return bookingPeriodFrom;
    }

    public void setBookingPeriodFrom(ZonedDateTime bookingPeriodFrom) {
        this.bookingPeriodFrom = bookingPeriodFrom;
    }

    public ZonedDateTime getBookingPeriodTo() {
        return bookingPeriodTo;
    }

    public void setBookingPeriodTo(ZonedDateTime bookingPeriodTo) {
        this.bookingPeriodTo = bookingPeriodTo;
    }

    public ZonedDateTime getTravelDateFrom() {
        return travelDateFrom;
    }

    public void setTravelDateFrom(ZonedDateTime travelDateFrom) {
        this.travelDateFrom = travelDateFrom;
    }

    public ZonedDateTime getTravelDateTo() {
        return travelDateTo;
    }

    public void setTravelDateTo(ZonedDateTime travelDateTo) {
        this.travelDateTo = travelDateTo;
    }

    public String getSupplierOrClientId() {
        return supplierOrClientId;
    }

    public void setSupplierOrClientId(String supplierOrClientId) {
        this.supplierOrClientId = supplierOrClientId;
    }

    public Set<String> getPaymentAdviceIds() {
        return paymentAdviceIds;
    }

    public void setPaymentAdviceIds(Set<String> paymentAdviceIds) {
        this.paymentAdviceIds = paymentAdviceIds;
    }

    public String getCreditOrDebitNoteNumber() {
        return creditOrDebitNoteNumber;
    }

    public void setCreditOrDebitNoteNumber(String creditOrDebitNoteNumber) {
        this.creditOrDebitNoteNumber = creditOrDebitNoteNumber;
    }

    public String getSupplierOrClientCommercialId() {
        return supplierOrClientCommercialId;
    }

    public void setSupplierOrClientCommercialId(String supplierOrClientCommercialId) {
        this.supplierOrClientCommercialId = supplierOrClientCommercialId;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public BigDecimal getBalancePayable() {
        return balancePayable;
    }

    public void setBalancePayable(BigDecimal balancePayable) {
        this.balancePayable = balancePayable;
    }

    public BigDecimal getBalanceReceivable() {
        return balanceReceivable;
    }

    public void setBalanceReceivable(BigDecimal balanceReceivable) {
        this.balanceReceivable = balanceReceivable;
    }

    public BigDecimal getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(BigDecimal totalReceived) {
        this.totalReceived = totalReceived;
    }

    public BigDecimal getTotalReceivable() {
        return totalReceivable;
    }

    public void setTotalReceivable(BigDecimal totalReceivable) {
        this.totalReceivable = totalReceivable;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public BigDecimal getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(BigDecimal totalPayable) {
        this.totalPayable = totalPayable;
    }

    public Set<CommercialStatementDetails> getCommercialStatementDetails() {
        return commercialStatementDetails;
    }

    public void setCommercialStatementDetails(Set<CommercialStatementDetails> commercialStatementDetails) {
        this.commercialStatementDetails = commercialStatementDetails;
    }

    public String getCommercialStatementFor() {
        return commercialStatementFor;
    }

    public void setCommercialStatementFor(String commercialStatementFor) {
        this.commercialStatementFor = commercialStatementFor;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    public String getProductNameSubType() {
        return productNameSubType;
    }

    public void setProductNameSubType(String productNameSubType) {
        this.productNameSubType = productNameSubType;
    }

    public String getProductFlavourName() {
        return productFlavourName;
    }

    public void setProductFlavourName(String productFlavourName) {
        this.productFlavourName = productFlavourName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCompanyMarket() {
        return companyMarket;
    }

    public void setCompanyMarket(String companyMarket) {
        this.companyMarket = companyMarket;
    }

    public String getSupplierOrClientName() {
        return supplierOrClientName;
    }

    public void setSupplierOrClientName(String supplierOrClientName) {
        this.supplierOrClientName = supplierOrClientName;
    }

    public String getFinalInvoiceNumber() {
        return finalInvoiceNumber;
    }

    public void setFinalInvoiceNumber(String finalInvoiceNumber) {
        this.finalInvoiceNumber = finalInvoiceNumber;
    }

    public String getPerformaInvoiceNumber() {
        return performaInvoiceNumber;
    }

    public void setPerformaInvoiceNumber(String performaInvoiceNumber) {
        this.performaInvoiceNumber = performaInvoiceNumber;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public BigDecimal getWriteOffAmount() {
        return writeOffAmount;
    }

    public void setWriteOffAmount(BigDecimal writeOffAmount) {
        this.writeOffAmount = writeOffAmount;
    }

    public BigDecimal getTotalServiceTax() {
        return totalServiceTax;
    }

    public void setTotalServiceTax(BigDecimal totalServiceTax) {
        this.totalServiceTax = totalServiceTax;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
