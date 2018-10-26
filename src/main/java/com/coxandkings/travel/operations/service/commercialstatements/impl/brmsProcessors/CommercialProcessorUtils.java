package com.coxandkings.travel.operations.service.commercialstatements.impl.brmsProcessors;

import com.coxandkings.travel.operations.service.commercialstatements.impl.settlementTerms.SettlementTerms.SettlementDetails;
import com.coxandkings.travel.operations.service.commercialstatements.impl.slabbing.SlabbingTree;

public class CommercialProcessorUtils {
	private SlabbingTree slabData;
	private SettlementDetails settlementDtls;
	
	public SlabbingTree getSlabData() {
		return slabData;
	}
	
	public SettlementDetails getSettlementDetails() {
		return settlementDtls;
	}
	
	public void setSlabData(SlabbingTree slabData) {
		this.slabData = slabData;
	}
	
	public void setSettlementDetails(SettlementDetails settlementDtls) {
		this.settlementDtls = settlementDtls;
	}
}
