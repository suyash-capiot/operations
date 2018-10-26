package com.coxandkings.travel.operations.resource.sellingPrice;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SellingPrice {
    private SellingPriceTable sellingPriceTable;
    private BigDecimal totalAmount;
    private List<SellingPriceComponent> sellingPriceComponents;

    public SellingPriceTable getSellingPriceTable() {
        return sellingPriceTable;
    }

    public void setSellingPriceTable(SellingPriceTable sellingPriceTable) {
        this.sellingPriceTable = sellingPriceTable;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<SellingPriceComponent> getSellingPriceComponents() {
        return sellingPriceComponents;
    }

    public void setSellingPriceComponents(List<SellingPriceComponent> sellingPriceComponents) {
        this.sellingPriceComponents = sellingPriceComponents;
    }
}
