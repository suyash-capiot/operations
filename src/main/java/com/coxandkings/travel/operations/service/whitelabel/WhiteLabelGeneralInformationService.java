package com.coxandkings.travel.operations.service.whitelabel;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.GeneralInformationStandardDetail;
import com.coxandkings.travel.operations.resource.whitelabel.GeneralInformationResource;
import org.springframework.stereotype.Service;

@Service
public interface WhiteLabelGeneralInformationService {

    GeneralInformationStandardDetail createOrUpdateGeneralInformation(
            GeneralInformationResource generalInformationResource) throws OperationException;

}
