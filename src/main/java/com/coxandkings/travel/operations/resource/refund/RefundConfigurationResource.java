package com.coxandkings.travel.operations.resource.refund;

import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.resource.BaseResource;

public class RefundConfigurationResource extends BaseResource {
    private String companyMarket;

    private String clientType;

    private String clientCategory;

    private String clientSubCategory;

    private String clientName;

    private String clientGroup;

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
