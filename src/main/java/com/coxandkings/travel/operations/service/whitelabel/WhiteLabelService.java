package com.coxandkings.travel.operations.service.whitelabel;

import com.coxandkings.travel.operations.criteria.whitelabel.WhiteLabelCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.resource.whitelabel.LandingPageResponse;
import com.coxandkings.travel.operations.resource.whitelabel.WhiteLabelResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WhiteLabelService {

    WhiteLabel createOrUpdateWhiteLabel(WhiteLabelResource whiteLabelResource) throws OperationException;

    WhiteLabel getWhiteLabelId(String id) throws OperationException;

    List<LandingPageResponse> getAllWhiteLabels() throws OperationException;

    void deleteWhiteLabel(String id);

    List<WhiteLabel> sortByCriteria(WhiteLabelCriteria criteria);
}
