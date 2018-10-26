package com.coxandkings.travel.operations.model.refund;

import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "refund_configuration")
public class RefundConfiguration {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;
    @Column(name = "company_market")
    private String companyMarket;

    @Column(name = "client_type")
    private String clientType;

    @Column(name = "client_category")
    private String clientCategory;

    @Column(name = "client_sub_category")
    private String clientSubCategory;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_group")
    private String clientGroup;

    @Column(name = "refund_types")
    @Enumerated(EnumType.STRING)
    private RefundTypes refundTypes;

    public String getCompanyMarket() {
        return companyMarket;
    }

    public void setCompanyMarket(String companyMarket) {
        this.companyMarket = companyMarket;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientCategory() {
        return clientCategory;
    }

    public void setClientCategory(String clientCategory) {
        this.clientCategory = clientCategory;
    }

    public String getClientSubCategory() {
        return clientSubCategory;
    }

    public void setClientSubCategory(String clientSubCategory) {
        this.clientSubCategory = clientSubCategory;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientGroup() {
        return clientGroup;
    }

    public void setClientGroup(String clientGroup) {
        this.clientGroup = clientGroup;
    }

    public RefundTypes getRefundTypes() {
        return refundTypes;
    }

    public void setRefundTypes(RefundTypes refundTypes) {
        this.refundTypes = refundTypes;
    }
}
