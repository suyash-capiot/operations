package com.coxandkings.travel.operations.model.manageofflinebooking.manualpnrsync;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsRequestBody {
	List<OpsSupplierBookReferences> supplierBookReferences;

	public void setSupplierBookReferences(List<OpsSupplierBookReferences> supplierBookReferences) {
		this.supplierBookReferences = supplierBookReferences;
	}

	public List<OpsSupplierBookReferences> getSupplierBookReferences() {
		return supplierBookReferences;
	}
}
