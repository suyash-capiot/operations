package com.coxandkings.travel.operations.resource.changesuppliername;

import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.SupplierCommercialPricingDetailResource;

import org.json.JSONObject;

public class ChangedSupplierPriceResource {
    private OpsProduct opsProduct;
    private JSONObject resource;
    private SupplierCommercialPricingDetailResource revisedPrice;
    private SupplierCommercialPricingDetailResource oldPrice;

    public OpsProduct getOpsProduct() {
        return opsProduct;
    }

    public void setOpsProduct(OpsProduct opsProduct) {
        this.opsProduct = opsProduct;
    }

    public JSONObject getResource() {
        return resource;
    }

    public void setResource(JSONObject resource) {
        this.resource = resource;
    }

	public SupplierCommercialPricingDetailResource getRevisedPrice() {
		return revisedPrice;
	}

	public void setRevisedPrice(SupplierCommercialPricingDetailResource revisedPrice) {
		this.revisedPrice = revisedPrice;
	}

	public SupplierCommercialPricingDetailResource getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(SupplierCommercialPricingDetailResource oldPrice) {
		this.oldPrice = oldPrice;
	}
}
