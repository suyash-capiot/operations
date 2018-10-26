package com.coxandkings.travel.operations.service.whitelabel;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.resource.whitelabel.WhiteLabelTemplateResource;
import org.springframework.stereotype.Service;

@Service
public interface WhiteLabelTemplateService {
    WhiteLabel saveWhiteLabelTemplate(WhiteLabelTemplateResource whiteLabelTemplateResource) throws OperationException;
}
