package com.coxandkings.travel.operations.model.amendsuppliercommercial;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.*;

@Table(name = "supplier_price_component")
@Entity
public class SupplierPriceComponent extends BaseModel {


    @Column(name = "price_component")
    private String priceComponent;
    @ManyToOne
    @JoinColumn(name = "amend_supplier_commercial_id")
    private AmendSupplierCommercial amendSupplierCommercial;

    public SupplierPriceComponent() {
    }

    public SupplierPriceComponent(String priceComponent) {
        this.priceComponent = priceComponent;
    }

    public String getPriceComponent() {
        return priceComponent;
    }

    public void setPriceComponent(String priceComponent) {
        this.priceComponent = priceComponent;
    }

    public AmendSupplierCommercial getAmendSupplierCommercial() {
        return amendSupplierCommercial;
    }

    public void setAmendSupplierCommercial(AmendSupplierCommercial amendSupplierCommercial) {
        this.amendSupplierCommercial = amendSupplierCommercial;
    }
}
