package com.coxandkings.travel.operations.resource.amendentitycommercial;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;

import java.util.List;

public class AmendCommercialMetaData {

    private List<CommercialHead> commercialHeads;
    private List<String> currency;
    private List<String> sellingPriceComponents;
    private List<String> applyOnProducts;
    private List<CommercialHead> notEligibleCommercialHeads;
    private CommercialResource commercials;
    private String bookId;
    private String orderId;
    private OpsProductSubCategory productSubCategory;
    private String clientType;
    private String clientId;
    private String companyId;

    public AmendCommercialMetaData() {
        super();
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public OpsProductSubCategory getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(OpsProductSubCategory productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public List<String> getApplyOnProducts() {
        return applyOnProducts;
    }

    public void setApplyOnProducts(List<String> applyOnProducts) {
        this.applyOnProducts = applyOnProducts;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<String> getSellingPriceComponents() {
        return sellingPriceComponents;
    }

    public void setSellingPriceComponents(List<String> sellingPriceComponents) {
        this.sellingPriceComponents = sellingPriceComponents;
    }

    public List<CommercialHead> getCommercialHeads() {
        return commercialHeads;
    }

    public void setCommercialHeads(List<CommercialHead> commercialHeads) {
        this.commercialHeads = commercialHeads;
    }

    public List<CommercialHead> getNotEligibleCommercialHeads() {
        return notEligibleCommercialHeads;
    }

    public void setNotEligibleCommercialHeads(List<CommercialHead> notEligibleCommercialHeads) {
        this.notEligibleCommercialHeads = notEligibleCommercialHeads;
    }

    public List<String> getCurrency() {
        return currency;
    }

    public void setCurrency(List<String> currency) {
        this.currency = currency;
    }

    public CommercialResource getCommercials() {
        return commercials;
    }

    public void setCommercials(CommercialResource commercials) {
        this.commercials = commercials;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }


}
