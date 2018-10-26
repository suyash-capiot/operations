package com.coxandkings.travel.operations.resource.supplierbillpassing;

public class ApolloSupplierResource {

    private String supplierName;
    private String CategoryId;
    private String OrganizationId;
    private String AdapterId;
    private String UserId;
    private String ClientSupplierId;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getOrganizationId() {
        return OrganizationId;
    }

    public void setOrganizationId(String organizationId) {
        OrganizationId = organizationId;
    }

    public String getAdapterId() {
        return AdapterId;
    }

    public void setAdapterId(String adapterId) {
        AdapterId = adapterId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getClientSupplierId() {
        return ClientSupplierId;
    }

    public void setClientSupplierId(String clientSupplierId) {
        ClientSupplierId = clientSupplierId;
    }
}
