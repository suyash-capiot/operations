package com.coxandkings.travel.operations.model.whitelabel;


import com.coxandkings.travel.operations.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "white_label_general_information")
public class GeneralInformationStandardDetail extends BaseModel {

//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private String id;

    @Column(name= "enable_required")
    private Boolean enableUpSell;

    @Column(name= "enable_cross_sell")
    private Boolean enableCrossSell;

    @Column(name= "shopping_cart_required")
    private Boolean shoppingCartRequired;

    @Column(name = "enable_combo_products")
    private Boolean enableComboProducts;

    //TODO: Verify data structure for this
    @Column(name = "enable_product_display_sorting")
    private Boolean enableProductDisplaySorting;

    @OneToOne
    @JoinColumn(name = "white_label_id")
    @JsonIgnore
    private WhiteLabel whiteLabel;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

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

    public WhiteLabel getWhiteLabel() {
        return whiteLabel;
    }

    public void setWhiteLabel(WhiteLabel whiteLabel) {
        this.whiteLabel = whiteLabel;
    }

}
