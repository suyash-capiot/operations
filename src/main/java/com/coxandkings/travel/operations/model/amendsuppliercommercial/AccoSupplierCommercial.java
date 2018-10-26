package com.coxandkings.travel.operations.model.amendsuppliercommercial;

import com.coxandkings.travel.operations.model.core.OpsAccoOrderSupplierPriceInfo;
import com.coxandkings.travel.operations.model.core.OpsAccommodationTotalPriceInfo;
import com.coxandkings.travel.operations.model.core.OpsOrderSupplierCommercial;
import com.coxandkings.travel.operations.model.core.OpsRoom;

import java.math.BigDecimal;
import java.util.List;

public class AccoSupplierCommercial {

    private boolean marginIncreased;
    private BigDecimal margin;
    private BigDecimal totalSellingPrice;
    private BigDecimal netPayableToSupplier;
    private String currencyCode;
    private OpsRoom room;
    private OpsAccoOrderSupplierPriceInfo opsAccoOrderSupplierPriceInfo;
    private OpsAccommodationTotalPriceInfo opsAccommodationTotalPriceInfo;
    private List<OpsOrderSupplierCommercial> opsOrderSupplierCommercials;

    public boolean isMarginIncreased() {
        return marginIncreased;
    }

    public void setMarginIncreased(boolean marginIncreased) {
        this.marginIncreased = marginIncreased;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public OpsRoom getRoom() {
        return room;
    }

    public void setRoom(OpsRoom room) {
        this.room = room;
    }

    public OpsAccoOrderSupplierPriceInfo getOpsAccoOrderSupplierPriceInfo() {
        return opsAccoOrderSupplierPriceInfo;
    }

    public void setOpsAccoOrderSupplierPriceInfo(OpsAccoOrderSupplierPriceInfo opsAccoOrderSupplierPriceInfo) {
        this.opsAccoOrderSupplierPriceInfo = opsAccoOrderSupplierPriceInfo;
    }

    public OpsAccommodationTotalPriceInfo getOpsAccommodationTotalPriceInfo() {
        return opsAccommodationTotalPriceInfo;
    }

    public void setOpsAccommodationTotalPriceInfo(OpsAccommodationTotalPriceInfo opsAccommodationTotalPriceInfo) {
        this.opsAccommodationTotalPriceInfo = opsAccommodationTotalPriceInfo;
    }

    public List<OpsOrderSupplierCommercial> getOpsOrderSupplierCommercials() {
        return opsOrderSupplierCommercials;
    }

    public void setOpsOrderSupplierCommercials(List<OpsOrderSupplierCommercial> opsOrderSupplierCommercials) {
        this.opsOrderSupplierCommercials = opsOrderSupplierCommercials;
    }

    public BigDecimal getTotalSellingPrice() {
        return totalSellingPrice;
    }

    public void setTotalSellingPrice(BigDecimal totalSellingPrice) {
        this.totalSellingPrice = totalSellingPrice;
    }

    public BigDecimal getNetPayableToSupplier() {
        return netPayableToSupplier;
    }

    public void setNetPayableToSupplier(BigDecimal netPayableToSupplier) {
        this.netPayableToSupplier = netPayableToSupplier;
    }

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
}
