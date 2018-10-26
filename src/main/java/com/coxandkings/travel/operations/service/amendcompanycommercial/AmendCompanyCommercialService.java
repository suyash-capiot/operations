package com.coxandkings.travel.operations.service.amendcompanycommercial;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.companycommercial.AmendCompanyCommercial;
import com.coxandkings.travel.operations.model.core.OpsOrderDetails;
import com.coxandkings.travel.operations.resource.amendentitycommercial.AmendCommercialResource;
import com.coxandkings.travel.operations.resource.amendentitycommercial.ChangeApprovalStatusResource;
import com.coxandkings.travel.operations.resource.amendentitycommercial.CommercialResource;

public interface AmendCompanyCommercialService {

    AmendCommercialResource apply(AmendCommercialResource amendCompanyCommercialResource) throws OperationException;

    void update(AmendCommercialResource amendCompanyCommercialResource) throws OperationException;

    void save(AmendCommercialResource amendCompanyCommercialResource) throws OperationException;
	AmendCompanyCommercial getCommercial(String id);
	String changeApprovalStatus(ChangeApprovalStatusResource changeApprovalStatusResource) throws OperationException;
	CommercialResource getCommercialResourceForOrder(OpsOrderDetails opsOrder, OpsProductSubCategory opsProductSubCategory, String uniqueId);
    /*MarginDetails calculateMargin(CommercialResource commercialResource, String companyMarket);*/

}
