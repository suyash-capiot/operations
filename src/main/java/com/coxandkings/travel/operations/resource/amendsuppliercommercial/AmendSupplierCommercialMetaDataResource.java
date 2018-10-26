package com.coxandkings.travel.operations.resource.amendsuppliercommercial;

import com.coxandkings.travel.operations.model.amendsuppliercommercial.CommercialHead;
import com.coxandkings.travel.operations.model.amendsuppliercommercial.SupplierCommercialResource;
import com.coxandkings.travel.operations.model.commercials.ApplyCommercialOn;
import com.coxandkings.travel.operations.model.commercials.SellingPriceComponent;

import java.util.HashMap;
import java.util.Set;

public class AmendSupplierCommercialMetaDataResource {
    private Set<CommercialHead> supplierCommercialHeads;
    private Set<CommercialHead> bookingNotEligibleFor;
    private Set<SellingPriceComponent> supplierPriceComponents;
    private Set<ApplyCommercialOn> applyProducts;
    private HashMap<String, String> productsToExclude = new HashMap<>();
    private String productCategory;
    private String productSubcategory;
    private String bookId;
    private String orderId;
    private String companyId;
    private String clientTypeId;
    private String clientId;
    private String roomId;
    private String supplierCurrency;
    private SupplierCommercialResource oldSupplierCommercialResource;

    public Set<CommercialHead> getSupplierCommercialHeads() {
        return supplierCommercialHeads;
    }

    public void setSupplierCommercialHeads(Set<CommercialHead> supplierCommercialHeads) {
        this.supplierCommercialHeads = supplierCommercialHeads;
    }

    public Set<CommercialHead> getBookingNotEligibleFor() {
        return bookingNotEligibleFor;
    }

    public void setBookingNotEligibleFor(Set<CommercialHead> bookingNotEligibleFor) {
        this.bookingNotEligibleFor = bookingNotEligibleFor;
    }

    public Set<SellingPriceComponent> getSupplierPriceComponents() {
        return supplierPriceComponents;
    }

    public void setSupplierPriceComponents(Set<SellingPriceComponent> supplierPriceComponents) {
        this.supplierPriceComponents = supplierPriceComponents;
    }

    public Set<ApplyCommercialOn> getApplyProducts() {
        return applyProducts;
    }

    public void setApplyProducts(Set<ApplyCommercialOn> applyProducts) {
        this.applyProducts = applyProducts;
    }

    public HashMap<String, String> getProductsToExclude() {
        return productsToExclude;
    }

    public void setProductsToExclude(HashMap<String, String> productsToExclude) {
        this.productsToExclude = productsToExclude;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductSubcategory() {
        return productSubcategory;
    }

    public void setProductSubcategory(String productSubcategory) {
        this.productSubcategory = productSubcategory;
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

    public String getClientTypeId() {
        return clientTypeId;
    }

    public void setClientTypeId(String clientTypeId) {
        this.clientTypeId = clientTypeId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSupplierCurrency() {
        return supplierCurrency;
    }

    public void setSupplierCurrency(String supplierCurrency) {
        this.supplierCurrency = supplierCurrency;
    }

    public SupplierCommercialResource getOldSupplierCommercialResource() {
        return oldSupplierCommercialResource;
    }

    public void setOldSupplierCommercialResource(SupplierCommercialResource oldSupplierCommercialResource) {
        this.oldSupplierCommercialResource = oldSupplierCommercialResource;
    }

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
}
