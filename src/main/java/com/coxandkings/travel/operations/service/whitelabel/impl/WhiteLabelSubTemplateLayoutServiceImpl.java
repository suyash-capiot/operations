package com.coxandkings.travel.operations.service.whitelabel.impl;

import com.coxandkings.travel.operations.enums.whitelabel.WhiteLabelConfigurationType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.TemplateLayoutStandardDetail;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.repository.whitelabel.ConfigurationTypeRepository;
import com.coxandkings.travel.operations.repository.whitelabel.WhiteLabelRepository;
import com.coxandkings.travel.operations.resource.whitelabel.TemplateLayoutResource;
import com.coxandkings.travel.operations.service.whitelabel.WhiteLabelSubTemplateLayoutService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service("WhiteLabelSubTemplateLayout")
public class WhiteLabelSubTemplateLayoutServiceImpl implements WhiteLabelSubTemplateLayoutService{

    private static Logger log = LogManager.getLogger(WhiteLabelServiceImpl.class);

    @Autowired
    WhiteLabelRepository whiteLabelRepository;

    @Autowired
    ConfigurationTypeRepository configurationTypeRepository;

    @Override
    public TemplateLayoutStandardDetail createOrUpdateTemplateLayout(TemplateLayoutResource templateLayoutResource)
            throws OperationException {
        WhiteLabel existingWhiteLabel = null;
        try
        {
            String whiteLabelTemplateId = templateLayoutResource.getWhiteLabelId();
            existingWhiteLabel = whiteLabelTemplateId != null ?
                    whiteLabelRepository.getWhiteLabelById(whiteLabelTemplateId) : null;

            existingWhiteLabel = whiteLabelTemplateId != null ?
                    whiteLabelRepository.getWhiteLabelById(whiteLabelTemplateId) : null;



            if(existingWhiteLabel == null) {
                throw new OperationException("WhiteLabelTemplateId not found with id" + existingWhiteLabel.getId());
            }

            if(!(existingWhiteLabel.getConfigurationTypeEnumHandler().getCode().
                    equals(WhiteLabelConfigurationType.STANDARD_DETAIL))){
                throw new OperationException("WhiteLabelConfigurationType should be "
                        + WhiteLabelConfigurationType.STANDARD_DETAIL);
            }

            if( !StringUtils.isEmpty(templateLayoutResource.getId()) ) {
                if(log.isDebugEnabled()) {
                    log.debug("PaymentInformationStandardDetail ResourceId:" + templateLayoutResource.getId());
                }
                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }

                TemplateLayoutStandardDetail updateTemplateLayout =  null;
                Set<TemplateLayoutStandardDetail> templateLayouts =
                        existingWhiteLabel.getTemplateLayoutStandardDetail();

                if(templateLayouts != null) {
                    Optional<TemplateLayoutStandardDetail> templateLayout = templateLayouts.stream()
                            .filter(templateLayoutMatch -> templateLayoutMatch.getId()
                                    .equalsIgnoreCase(templateLayoutResource.getId())).findFirst();

                    if(templateLayout.isPresent()) {
                        updateTemplateLayout = templateLayout.get();
                        CopyUtils.copy(templateLayoutResource, updateTemplateLayout);
                    }else {
                        throw new OperationException("TemplateLayoutStandardDetail not found for id "
                                + templateLayoutResource.getId());
                    }
                }

                whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                return updateTemplateLayout;
            } else {

                TemplateLayoutStandardDetail templateLayoutStandardDetail = new TemplateLayoutStandardDetail();
                CopyUtils.copy(templateLayoutResource, templateLayoutStandardDetail);
                templateLayoutStandardDetail.setWhiteLabel(existingWhiteLabel);

                Set<TemplateLayoutStandardDetail> oldTemplateLayouts = new HashSet<>();
                if (existingWhiteLabel.getTemplateLayoutStandardDetail() == null) {
                    oldTemplateLayouts.add(templateLayoutStandardDetail);
                    existingWhiteLabel.setTemplateLayoutStandardDetail(oldTemplateLayouts);
                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                    return existingWhiteLabel.getTemplateLayoutStandardDetail().iterator().next();
                } else {

                    oldTemplateLayouts.addAll(existingWhiteLabel.getTemplateLayoutStandardDetail());
                    existingWhiteLabel.getTemplateLayoutStandardDetail().add(templateLayoutStandardDetail);
                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                    Set<TemplateLayoutStandardDetail> newTemplateLayouts = new HashSet<>();
                    newTemplateLayouts.addAll(existingWhiteLabel.getTemplateLayoutStandardDetail());
                    newTemplateLayouts.removeAll(oldTemplateLayouts);
                    return newTemplateLayouts.iterator().next();
                }
            }
        } catch (OperationException e) {
            throw new OperationException(Constants.ER01);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    @Transactional
    public WhiteLabel deleteTemplateLayout(TemplateLayoutResource templateLayoutResource) throws OperationException {
        WhiteLabel existingWhiteLabel =  null;
        String whiteLabelTemplateId = templateLayoutResource.getWhiteLabelId();
        existingWhiteLabel = whiteLabelTemplateId != null ?
                whiteLabelRepository.getWhiteLabelById(whiteLabelTemplateId) : null;

        try {
            if (existingWhiteLabel == null) {
                throw new OperationException("WhiteLabelTemplateId not found with id" + existingWhiteLabel.getId());
            }

            if( !StringUtils.isEmpty(templateLayoutResource.getId()) ) {
                if(log.isDebugEnabled()) {
                    log.debug("TemplateLayout ResourceId:" + templateLayoutResource.getId());
                }
                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }

                TemplateLayoutStandardDetail deleteTemplateLayoutStandardDetail =  null;
                Set<TemplateLayoutStandardDetail> templateLayoutStandardDetailSet =
                        existingWhiteLabel.getTemplateLayoutStandardDetail();

                if(templateLayoutStandardDetailSet != null) {
                    Optional<TemplateLayoutStandardDetail> templateLayoutStandardDetail =
                            templateLayoutStandardDetailSet.stream()
                                    .filter(templateLayoutStandardDetailMatch -> templateLayoutStandardDetailMatch.getId()
                                            .equalsIgnoreCase(templateLayoutResource.getId())).findFirst();

                    if(templateLayoutStandardDetail.isPresent()) {
                        deleteTemplateLayoutStandardDetail = templateLayoutStandardDetail.get();
                        templateLayoutStandardDetailSet.remove(deleteTemplateLayoutStandardDetail);
                    }else {
                        throw new OperationException("Template Layout not found for id "
                                + templateLayoutResource.getId());
                    }
                }

                WhiteLabel whiteLabel = whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                return whiteLabel;
            }
            else{
                throw new OperationException("Marketing Setup Parameter is empty for "
                        + templateLayoutResource.getId());
            }

        } catch (OperationException e) {
            throw new OperationException(Constants.ER01);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
