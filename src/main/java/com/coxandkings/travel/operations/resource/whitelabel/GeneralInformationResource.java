package com.coxandkings.travel.operations.resource.whitelabel;

import com.coxandkings.travel.operations.resource.BaseResource;


public class GeneralInformationResource extends BaseResource {

    private Boolean enableUpSell;

    private Boolean enableCrossSell;

    private Boolean shoppingCartRequired;

    private Boolean enableComboProducts;

    private Boolean enableProductDisplaySorting;

    private String whiteLabelId;

    public Boolean getEnableUpSell() {
        return enableUpSell;
    }

    public void setEnableUpSell(Boolean enableUpSell) {
        this.enableUpSell = enableUpSell;
    }

    public Boolean getEnableCrossSell() {
        return enableCrossSell;
    }

    public void setEnableCrossSell(Boolean enableCrossSell) {
        this.enableCrossSell = enableCrossSell;
    }

    public Boolean getShoppingCartRequired() {
        return shoppingCartRequired;
    }

    public void setShoppingCartRequired(Boolean shoppingCartRequired) {
        this.shoppingCartRequired = shoppingCartRequired;
    }

    public Boolean getEnableComboProducts() {
        return enableComboProducts;
    }

    public void setEnableComboProducts(Boolean enableComboProducts) {
        this.enableComboProducts = enableComboProducts;
    }

    public Boolean getEnableProductDisplaySorting() {
        return enableProductDisplaySorting;
    }

    public void setEnableProductDisplaySorting(Boolean enableProductDisplaySorting) {
        this.enableProductDisplaySorting = enableProductDisplaySorting;
    }

    public String getWhiteLabelId() {
        return whiteLabelId;
    }

    public void setWhiteLabelId(String whiteLabelId) {
        this.whiteLabelId = whiteLabelId;
    }
}
