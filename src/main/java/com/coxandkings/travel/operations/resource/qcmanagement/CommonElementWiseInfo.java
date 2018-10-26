package com.coxandkings.travel.operations.resource.qcmanagement;

import java.util.Objects;

public class CommonElementWiseInfo {
    private String supplier;
    private String clientType;
    private String productCategorySubType;

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonElementWiseInfo that = (CommonElementWiseInfo) o;
        return Objects.equals(supplier, that.supplier) &&
                Objects.equals(clientType, that.clientType) &&
                Objects.equals(productCategorySubType, that.productCategorySubType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(supplier, clientType, productCategorySubType);
    }

    @Override
    public String toString() {
        return "CommonElementWiseInfo{" +
                "supplier='" + supplier + '\'' +
                ", clientType='" + clientType + '\'' +
                ", productCategorySubType='" + productCategorySubType + '\'' +
                '}';
    }
}
