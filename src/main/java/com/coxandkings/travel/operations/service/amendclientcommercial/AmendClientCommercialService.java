package com.coxandkings.travel.operations.service.amendclientcommercial;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.clientcommercial.ClientCommercial;
import com.coxandkings.travel.operations.resource.amendentitycommercial.AmendCommercialResource;
import com.coxandkings.travel.operations.resource.amendentitycommercial.ChangeApprovalStatusResource;


public interface AmendClientCommercialService {

    AmendCommercialResource apply(AmendCommercialResource amendClientCommercialResource) throws OperationException;

    void update(AmendCommercialResource amendClientCommercialResource) throws OperationException;

    void save(AmendCommercialResource amendClientCommercialResource) throws OperationException;
	ClientCommercial getCommercial(String id);
	String changeApprovalStatus(ChangeApprovalStatusResource changeApprovalStatusResource) throws OperationException;

}
