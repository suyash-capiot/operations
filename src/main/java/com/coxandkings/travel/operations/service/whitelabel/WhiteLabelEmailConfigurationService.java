package com.coxandkings.travel.operations.service.whitelabel;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.EmailConfiguration;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.resource.whitelabel.EmailConfigurationResource;
import org.springframework.stereotype.Service;


@Service
public interface WhiteLabelEmailConfigurationService {

    EmailConfiguration createOrUpdateEmailConfiguration(EmailConfigurationResource emailConfigurationResource)
            throws OperationException;

    WhiteLabel deleteEmailConfiguration(EmailConfigurationResource emailConfigurationResource) throws OperationException;
}
