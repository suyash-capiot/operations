package com.coxandkings.travel.operations.service.whitelabel;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.TemplateLayoutStandardDetail;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.resource.whitelabel.TemplateLayoutResource;
import org.springframework.stereotype.Service;

@Service
public interface WhiteLabelSubTemplateLayoutService {

    TemplateLayoutStandardDetail createOrUpdateTemplateLayout(
            TemplateLayoutResource templateLayoutResource) throws OperationException;

    WhiteLabel deleteTemplateLayout(TemplateLayoutResource templateLayoutResource) throws OperationException;

}
