package com.coxandkings.travel.operations.service.whitelabel;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.CustomWhiteLabel;
import com.coxandkings.travel.operations.resource.whitelabel.CustomWhiteLabelResource;
import org.springframework.stereotype.Service;

@Service
public interface WhiteLabelCustomService {
    CustomWhiteLabel createOrUpdateCustomWhiteLabel(CustomWhiteLabelResource customWhiteLabelResource) throws OperationException;
}
