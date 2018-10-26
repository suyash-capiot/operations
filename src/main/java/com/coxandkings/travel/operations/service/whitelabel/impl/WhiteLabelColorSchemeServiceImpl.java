package com.coxandkings.travel.operations.service.whitelabel.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.ColorScheme;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.repository.whitelabel.ConfigurationTypeRepository;
import com.coxandkings.travel.operations.repository.whitelabel.WhiteLabelRepository;
import com.coxandkings.travel.operations.resource.whitelabel.ColorSchemeResource;
import com.coxandkings.travel.operations.service.whitelabel.WhiteLabelColorSchemeService;
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

@Service("WhiteLabelColorScheme")
public class WhiteLabelColorSchemeServiceImpl implements WhiteLabelColorSchemeService {

    private static Logger log = LogManager.getLogger(WhiteLabelServiceImpl.class);

    @Autowired
    WhiteLabelRepository whiteLabelRepository;

    @Autowired
    ConfigurationTypeRepository configurationTypeRepository;


    @Override
    public ColorScheme createOrUpdateColorScheme(ColorSchemeResource colorSchemeResource) throws OperationException {
        WhiteLabel existingWhiteLabel = null;
        try
        {
            String whiteLabelTemplateId = colorSchemeResource.getWhiteLabelId();
            existingWhiteLabel = whiteLabelTemplateId != null ?
                    whiteLabelRepository.getWhiteLabelById(whiteLabelTemplateId) : null;

            if(existingWhiteLabel == null) {
                throw new OperationException("WhiteLabelTemplateId not found with id" + existingWhiteLabel.getId());
            }


            if( !StringUtils.isEmpty(colorSchemeResource.getId()) ) {
                if(log.isDebugEnabled()) {
                    log.debug("ColorSchemeResource ResourceId:" + colorSchemeResource.getId());
                }
                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }

                ColorScheme updateColorScheme =  null;
                Set<ColorScheme> colorSchemes = existingWhiteLabel.getColorScheme();

                if(colorSchemes != null) {
                    Optional<ColorScheme> colorScheme = colorSchemes.stream()
                            .filter(colorSchemeMatch -> colorSchemeMatch.getId()
                                    .equalsIgnoreCase(colorSchemeResource.getId())).findFirst();

                    if(colorScheme.isPresent()) {
                        updateColorScheme = colorScheme.get();
                        CopyUtils.copy(colorSchemeResource, updateColorScheme);
                    } else {
                        throw new OperationException("ColorScheme not found for id "
                                + colorSchemeResource.getId());
                    }
                }

                whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                return updateColorScheme;
            } else {
                ColorScheme colorScheme = new ColorScheme();
                CopyUtils.copy(colorSchemeResource, colorScheme);
                colorScheme.setWhiteLabel(existingWhiteLabel);

                Set<ColorScheme> oldColorScheme = new HashSet<>();
                if (existingWhiteLabel.getColorScheme() == null) {
                    oldColorScheme.add(colorScheme);
                    existingWhiteLabel.setColorScheme(oldColorScheme);
                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                    return existingWhiteLabel.getColorScheme().iterator().next();
                } else {

                    oldColorScheme.addAll(existingWhiteLabel.getColorScheme());
                    existingWhiteLabel.getColorScheme().add(colorScheme);
                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);

                    Set<ColorScheme> newColorSchemes = new HashSet<>();
                    newColorSchemes.addAll(existingWhiteLabel.getColorScheme());
                    newColorSchemes.removeAll(oldColorScheme);
                    //Note : exiting whitelabel in inconsistent state(don't hit it to repository).
                    return newColorSchemes.iterator().next();
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
    public WhiteLabel deleteColorScheme(ColorSchemeResource colorSchemeResource) throws OperationException{
        WhiteLabel existingWhiteLabel =  null;
        String whiteLabelTemplateId = colorSchemeResource.getWhiteLabelId();
        existingWhiteLabel = whiteLabelTemplateId != null ?
                whiteLabelRepository.getWhiteLabelById(whiteLabelTemplateId) : null;

        try {
            if (existingWhiteLabel == null) {
                throw new OperationException("WhiteLabelTemplateId not found with id" + existingWhiteLabel.getId());
            }

            if( !StringUtils.isEmpty(colorSchemeResource.getId()) ) {
                if(log.isDebugEnabled()) {
                    log.debug("ColorSchemeResource ResourceId:" + colorSchemeResource.getId());
                }
                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }

                ColorScheme deleteColorScheme =  null;
                Set<ColorScheme> colorSchemes = existingWhiteLabel.getColorScheme();

                if(colorSchemes != null) {
                    Optional<ColorScheme> colorScheme = colorSchemes.stream()
                            .filter(colorSchemeMatch -> colorSchemeMatch.getId()
                                    .equalsIgnoreCase(colorSchemeResource.getId())).findFirst();

                    if(colorScheme.isPresent()) {
                        deleteColorScheme = colorScheme.get();
                        colorSchemes.remove(deleteColorScheme);
                    }else {
                        throw new OperationException("ColorScheme not found for id "
                                + colorSchemeResource.getId());
                    }
                }

                WhiteLabel whiteLabel = whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                return whiteLabel;
            }
            else{
                throw new OperationException("ColorSchemeId is parameter is empty for "
                        + colorSchemeResource.getId());
            }

        }
        catch (OperationException e) {
            throw new OperationException(Constants.ER01);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

