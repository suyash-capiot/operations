package com.coxandkings.travel.operations.model.forex;

import com.coxandkings.travel.operations.enums.forex.IndentStatus;
import com.coxandkings.travel.operations.enums.forex.IndentType;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity
public class ForexIndent {

    @Id
    @GenericGenerator(name = "indent_generator", strategy = "com.coxandkings.travel.operations.model.forex.generator.IndentIdGenerator")
    @GeneratedValue(generator = "indent_generator")
    @Column(name = "indent_id", updatable = false, nullable = false)
    private String id;

    @Column(name = "created_by_user_id")
    @CreatedBy
    private String createdByUserId;

    @Column(name = "last_modified_by_user_id")
    @LastModifiedBy
    private String lastModifiedByUserId;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "created_at_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAtTime;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "last_modified_at_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime lastModifiedAtTime;

    @ManyToOne
    @JoinColumn(name = "forex_id")
//    @JsonBackReference
    private ForexBooking forexBooking;

    private String supplierName; //Indent raised to this Supplier
    private String supplierId;
    private String paymentType; //Supplier's preferred mode of payment

    @Enumerated(EnumType.STRING)
    private IndentType indentFor;

    @Enumerated(EnumType.STRING)
    private IndentStatus indentStatus;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "indent_id")
    private Set<ForexBuyingCurrency> forexBuyingCcyDetails;

    @Column
    private String approverRemark;

    @Column
    private Boolean isSentToSupplier;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "indent_id")
    private DisbursementDetails disbursementDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public ZonedDateTime getCreatedAtTime() {
        return createdAtTime;
    }

    public void setCreatedAtTime(ZonedDateTime createdAtTime) {
        this.createdAtTime = createdAtTime;
    }

    public ZonedDateTime getLastModifiedAtTime() {
        return lastModifiedAtTime;
    }

    public void setLastModifiedAtTime(ZonedDateTime lastModifiedAtTime) {
        this.lastModifiedAtTime = lastModifiedAtTime;
    }

    public ForexBooking getForexBooking() {
        return forexBooking;
    }

    public void setForexBooking(ForexBooking forexBooking) {
        this.forexBooking = forexBooking;
    }

    public Set<ForexBuyingCurrency> getForexBuyingCcyDetails() {
        return forexBuyingCcyDetails;
    }

    public void setForexBuyingCcyDetails(Set<ForexBuyingCurrency> forexBuyingCcyDetails) {
        this.forexBuyingCcyDetails = forexBuyingCcyDetails;
    }

    public IndentType getIndentFor() {
        return indentFor;
    }

    public void setIndentFor(IndentType indentFor) {
        this.indentFor = indentFor;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public IndentStatus getIndentStatus() {
        return indentStatus;
    }

    public void setIndentStatus(IndentStatus indentStatus) {
        this.indentStatus = indentStatus;
    }

    public String getApproverRemark() {
        return approverRemark;
    }

    public void setApproverRemark(String approverRemark) {
        this.approverRemark = approverRemark;
    }

    public DisbursementDetails getDisbursementDetails() {
        return disbursementDetails;
    }

    public void setDisbursementDetails(DisbursementDetails disbursementDetails) {
        this.disbursementDetails = disbursementDetails;
    }

    public Boolean getSentToSupplier() {
        return isSentToSupplier;
    }

    public void setSentToSupplier(Boolean sentToSupplier) {
        isSentToSupplier = sentToSupplier;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
}
