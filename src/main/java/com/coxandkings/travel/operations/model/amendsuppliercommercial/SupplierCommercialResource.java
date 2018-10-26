package com.coxandkings.travel.operations.model.amendsuppliercommercial;

import java.math.BigDecimal;
import java.util.List;

import com.coxandkings.travel.operations.model.core.OpsOrderClientCommercial;
import com.coxandkings.travel.operations.model.core.OpsOrderSupplierCommercial;

public class SupplierCommercialResource {
    private AccoSupplierCommercial accoSupplierCommercial;
    private AirSupplierCommercial airSupplierCommercial;

    private BigDecimal differenceInAmount;
    private BigDecimal retainOrAbsorbedByCompanyAmount;
    private BigDecimal passOnOrChargeClientAmount;
    private Boolean isPassOnOrRetain;
    private List<OpsOrderSupplierCommercial> orderSupplierCommercials;
    private List<OpsOrderClientCommercial> orderClientCommercials;
    
    
    public AccoSupplierCommercial getAccoSupplierCommercial() {
        return accoSupplierCommercial;
    }

    public void setAccoSupplierCommercial(AccoSupplierCommercial accoSupplierCommercial) {
        this.accoSupplierCommercial = accoSupplierCommercial;
    }

    public AirSupplierCommercial getAirSupplierCommercial() {
        return airSupplierCommercial;
    }

    public void setAirSupplierCommercial(AirSupplierCommercial airSupplierCommercial) {
        this.airSupplierCommercial = airSupplierCommercial;
    }

    public BigDecimal getDifferenceInAmount() {
        return differenceInAmount;
    }

    public void setDifferenceInAmount(BigDecimal differenceInAmount) {
        this.differenceInAmount = differenceInAmount;
    }

	public List<OpsOrderSupplierCommercial> getOrderSupplierCommercials() {
		return orderSupplierCommercials;
	}

	public void setOrderSupplierCommercials(List<OpsOrderSupplierCommercial> orderSupplierCommercials) {
		this.orderSupplierCommercials = orderSupplierCommercials;
	}

	public BigDecimal getRetainOrAbsorbedByCompanyAmount() {
		return retainOrAbsorbedByCompanyAmount;
	}

	public void setRetainOrAbsorbedByCompanyAmount(BigDecimal retainOrAbsorbedByCompanyAmount) {
		this.retainOrAbsorbedByCompanyAmount = retainOrAbsorbedByCompanyAmount;
	}

	public BigDecimal getPassOnOrChargeClientAmount() {
		return passOnOrChargeClientAmount;
	}

	public void setPassOnOrChargeClientAmount(BigDecimal passOnOrChargeClientAmount) {
		this.passOnOrChargeClientAmount = passOnOrChargeClientAmount;
	}

	public Boolean getIsPassOnOrRetain() {
		return isPassOnOrRetain;
	}

	public void setIsPassOnOrRetain(Boolean isPassOnOrRetain) {
		this.isPassOnOrRetain = isPassOnOrRetain;
	}

	public List<OpsOrderClientCommercial> getOrderClientCommercials() {
		return orderClientCommercials;
	}

	public void setOrderClientCommercials(List<OpsOrderClientCommercial> orderClientCommercials) {
		this.orderClientCommercials = orderClientCommercials;
	}
}
