package com.coxandkings.travel.operations.service.whitelabel;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.MarketingSetupStandardDetail;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.resource.whitelabel.MarketingSetupParameterResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface WhiteLabelMarketingSetupService {

    MarketingSetupStandardDetail createOrUpdateMarketingSetupParameter(
            MarketingSetupParameterResource marketingSetupParameterResource, MultipartFile file) throws OperationException;

    WhiteLabel deleteMarketingSetup(MarketingSetupParameterResource marketingSetupParameterResource) throws OperationException;

}
