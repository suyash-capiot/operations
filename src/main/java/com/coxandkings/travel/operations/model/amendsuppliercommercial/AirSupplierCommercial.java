package com.coxandkings.travel.operations.model.amendsuppliercommercial;

import com.coxandkings.travel.operations.model.core.OpsFlightSupplierPriceInfo;
import com.coxandkings.travel.operations.model.core.OpsFlightTotalPriceInfo;
import com.coxandkings.travel.operations.model.core.OpsOrderSupplierCommercial;
import java.math.BigDecimal;
import java.util.List;

public class AirSupplierCommercial {
	private boolean marginIncreased;
    private BigDecimal margin;
    private BigDecimal totalSellingPrice;
    private BigDecimal netPayableToSupplier;
    private String currencyCode;
    //private List<OpsFlightPaxInfo> paxInfoList;
    private OpsFlightTotalPriceInfo opsFlightTotalPriceInfo;
    private OpsFlightSupplierPriceInfo opsFlightSupplierPriceInfo;
    private List<OpsOrderSupplierCommercial> opsFlightSupplierCommercials;

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public OpsFlightTotalPriceInfo getOpsFlightTotalPriceInfo() {
        return opsFlightTotalPriceInfo;
    }

    public void setOpsFlightTotalPriceInfo(OpsFlightTotalPriceInfo opsFlightTotalPriceInfo) {
        this.opsFlightTotalPriceInfo = opsFlightTotalPriceInfo;
    }

    public OpsFlightSupplierPriceInfo getOpsFlightSupplierPriceInfo() {
        return opsFlightSupplierPriceInfo;
    }

    public void setOpsFlightSupplierPriceInfo(OpsFlightSupplierPriceInfo opsFlightSupplierPriceInfo) {
        this.opsFlightSupplierPriceInfo = opsFlightSupplierPriceInfo;
    }

    public List<OpsOrderSupplierCommercial> getOpsFlightSupplierCommercials() {
        return opsFlightSupplierCommercials;
    }

    public void setOpsFlightSupplierCommercials(List<OpsOrderSupplierCommercial> opsFlightSupplierCommercials) {
        this.opsFlightSupplierCommercials = opsFlightSupplierCommercials;
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

	public boolean isMarginIncreased() {
		return marginIncreased;
	}

	public void setMarginIncreased(boolean marginIncreased) {
		this.marginIncreased = marginIncreased;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
}
