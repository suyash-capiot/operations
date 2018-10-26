package com.coxandkings.travel.operations.service.whitelabel.impl;

import com.coxandkings.travel.operations.enums.whitelabel.WhiteLabelConfigurationType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.CustomWhiteLabel;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.repository.whitelabel.ConfigurationTypeRepository;
import com.coxandkings.travel.operations.repository.whitelabel.WhiteLabelRepository;
import com.coxandkings.travel.operations.resource.whitelabel.CustomWhiteLabelResource;
import com.coxandkings.travel.operations.service.whitelabel.WhiteLabelCustomService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("WhiteLabelCustom")
public class WhiteLabelCustomServiceImpl implements WhiteLabelCustomService {

    private static Logger log = LogManager.getLogger(WhiteLabelServiceImpl.class);

    @Autowired
    WhiteLabelRepository whiteLabelRepository;

    @Autowired
    ConfigurationTypeRepository configurationTypeRepository;


    @Override
    public CustomWhiteLabel createOrUpdateCustomWhiteLabel(CustomWhiteLabelResource customWhiteLabelResource)
            throws OperationException {

        WhiteLabel existingWhiteLabel = null;
        try
        {
            String whiteLabelTemplateId = customWhiteLabelResource.getWhiteLabelId();
            existingWhiteLabel = whiteLabelTemplateId != null ?
                    whiteLabelRepository.getWhiteLabelById(whiteLabelTemplateId) : null;
            if(existingWhiteLabel == null){
                throw new OperationException("WhiteLabelTemplateId not found with id" + existingWhiteLabel.getId());
            }
            if(!(existingWhiteLabel.getConfigurationTypeEnumHandler().getCode().
                    equals(WhiteLabelConfigurationType.CUSTOM))){
                throw new OperationException("WhiteLabelConfigurationType should be "
                        + WhiteLabelConfigurationType.CUSTOM);
            }

            CustomWhiteLabel customWhiteLabel = new CustomWhiteLabel();

            if( !StringUtils.isEmpty(customWhiteLabelResource.getId()) ) {
                if(log.isDebugEnabled()) {
                    log.debug("Custom WhiteLabel ResourceId:" + customWhiteLabelResource.getId());
                }
                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }
                CopyUtils.copy(customWhiteLabelResource, customWhiteLabel);
                existingWhiteLabel.setCustomWhiteLabel(customWhiteLabel);
            } else {
                CopyUtils.copy(customWhiteLabelResource, customWhiteLabel);
                customWhiteLabel.setWhiteLabel(existingWhiteLabel);
                existingWhiteLabel.setCustomWhiteLabel(customWhiteLabel);
            }

            WhiteLabel updatedWhiteLabel =  whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
            return updatedWhiteLabel.getCustomWhiteLabel();
        } catch (OperationException e) {
            throw new OperationException(Constants.ER01);
        }  catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
