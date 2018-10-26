package com.coxandkings.travel.operations.service.whitelabel;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.ColorScheme;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.resource.whitelabel.ColorSchemeResource;
import org.springframework.stereotype.Service;

@Service
public interface WhiteLabelColorSchemeService {

    ColorScheme createOrUpdateColorScheme(ColorSchemeResource colorSchemeResource) throws OperationException;
    WhiteLabel deleteColorScheme(ColorSchemeResource colorSchemeResource) throws OperationException;

}
