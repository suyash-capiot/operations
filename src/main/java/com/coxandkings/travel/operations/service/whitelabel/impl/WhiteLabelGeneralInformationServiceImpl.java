package com.coxandkings.travel.operations.service.whitelabel.impl;

import com.coxandkings.travel.operations.enums.whitelabel.WhiteLabelConfigurationType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.GeneralInformationStandardDetail;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.repository.whitelabel.ConfigurationTypeRepository;
import com.coxandkings.travel.operations.repository.whitelabel.WhiteLabelRepository;
import com.coxandkings.travel.operations.resource.whitelabel.GeneralInformationResource;
import com.coxandkings.travel.operations.service.whitelabel.WhiteLabelGeneralInformationService;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("WhiteLabelGeneralInformation")
public class WhiteLabelGeneralInformationServiceImpl implements WhiteLabelGeneralInformationService {

    private static Logger log = LogManager.getLogger(WhiteLabelServiceImpl.class);

    @Autowired
    WhiteLabelRepository whiteLabelRepository;

    @Autowired
    ConfigurationTypeRepository configurationTypeRepository;

    @Override
    public GeneralInformationStandardDetail createOrUpdateGeneralInformation(
            GeneralInformationResource generalInformationResource) throws OperationException {
        WhiteLabel existingWhiteLabel = null;
        try
        {
            String whiteLabelTemplateId = generalInformationResource.getWhiteLabelId();
            existingWhiteLabel = whiteLabelTemplateId != null ?
                    whiteLabelRepository.getWhiteLabelById(whiteLabelTemplateId) : null;
            if(existingWhiteLabel == null){
                throw new OperationException("WhiteLabelTemplateId not found with id" + existingWhiteLabel.getId());
            }
            if(!(existingWhiteLabel.getConfigurationTypeEnumHandler().getCode().
                    equals(WhiteLabelConfigurationType.STANDARD_DETAIL))){
                throw new OperationException("WhiteLabelConfigurationType should be "
                        + WhiteLabelConfigurationType.STANDARD_DETAIL);
            }

            GeneralInformationStandardDetail generalInformationStandardDetail = new GeneralInformationStandardDetail();

            if( !StringUtils.isEmpty(generalInformationResource.getId()) ) {
                if(log.isDebugEnabled()) {
                    log.debug("GeneralInformationStandardDetail ResourceId:" + generalInformationResource.getId());
                }
                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }
                CopyUtils.copy(generalInformationResource, generalInformationStandardDetail);
                existingWhiteLabel.setGeneralInformationStandardDetail(generalInformationStandardDetail);
            } else {
                CopyUtils.copy(generalInformationResource, generalInformationStandardDetail);
                generalInformationStandardDetail.setWhiteLabel(existingWhiteLabel);
                existingWhiteLabel.setGeneralInformationStandardDetail(generalInformationStandardDetail);
            }

            WhiteLabel updatedWhiteLabel =  whiteLabelRepository.saveOrUpdate(existingWhiteLabel);

            return updatedWhiteLabel.getGeneralInformationStandardDetail();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }




}
