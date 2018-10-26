package com.coxandkings.travel.operations.service.whitelabel.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.EmailConfiguration;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.repository.whitelabel.ConfigurationTypeRepository;
import com.coxandkings.travel.operations.repository.whitelabel.WhiteLabelRepository;
import com.coxandkings.travel.operations.resource.whitelabel.EmailConfigurationResource;
import com.coxandkings.travel.operations.service.whitelabel.WhiteLabelEmailConfigurationService;
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

@Service("WhiteLabelEmailConfiguration")
public class WhiteLabelEmailConfigurationServiceImpl implements WhiteLabelEmailConfigurationService {

    private static Logger log = LogManager.getLogger(WhiteLabelServiceImpl.class);

    @Autowired
    WhiteLabelRepository whiteLabelRepository;

    @Autowired
    ConfigurationTypeRepository configurationTypeRepository;


    @Override
    public EmailConfiguration createOrUpdateEmailConfiguration(EmailConfigurationResource emailConfigurationResource)
            throws OperationException {
        WhiteLabel existingWhiteLabel = null;
        try
        {
            String whiteLabelTemplateId = emailConfigurationResource.getWhiteLabelId();
            existingWhiteLabel = whiteLabelTemplateId != null ?
                    whiteLabelRepository.getWhiteLabelById(whiteLabelTemplateId) : null;

            if(existingWhiteLabel == null) {
                throw new OperationException("WhiteLabelTemplateId not found with id" + existingWhiteLabel.getId());
            }

            if( !StringUtils.isEmpty(emailConfigurationResource.getId()) ) {
                if(log.isDebugEnabled()) {
                    log.debug("EmailConfiguration ResourceId:" + emailConfigurationResource.getId());
                }
                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }

                EmailConfiguration updateEmailConfiguration = null;
                Set<EmailConfiguration> emailConfigurations = existingWhiteLabel.getEmailConfigurations();
                if(emailConfigurations != null) {
                    Optional<EmailConfiguration> emailConfiguration = emailConfigurations.stream()
                            .filter(emailConfigurationMatch -> emailConfigurationMatch.getId()
                                    .equalsIgnoreCase(emailConfigurationResource.getId())).findFirst();

                    if(emailConfiguration != null) {
                        updateEmailConfiguration  = emailConfiguration.get();
                        CopyUtils.copy(emailConfigurationResource, updateEmailConfiguration );
                    }else {
                        throw new OperationException("EmailConfiguration not found for id" + emailConfigurationResource.getId());
                    }

                }

                whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                return updateEmailConfiguration;

            } else {
                EmailConfiguration emailConfiguration = new EmailConfiguration();
                CopyUtils.copy(emailConfigurationResource, emailConfiguration);
                emailConfiguration.setWhiteLabel(existingWhiteLabel);
                Set<EmailConfiguration> oldEmailConfigurations = new HashSet<>();

                if (existingWhiteLabel.getEmailConfigurations() == null) {
                    oldEmailConfigurations.add(emailConfiguration);
                    existingWhiteLabel.setEmailConfigurations(oldEmailConfigurations);
                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                    return existingWhiteLabel.getEmailConfigurations().iterator().next();
                } else {
                    oldEmailConfigurations.addAll(existingWhiteLabel.getEmailConfigurations());

                    existingWhiteLabel.getEmailConfigurations().add(emailConfiguration);

                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);

                    Set<EmailConfiguration> newEmailConfigurations = new HashSet<>();
                    newEmailConfigurations.addAll(existingWhiteLabel.getEmailConfigurations());

                    newEmailConfigurations.removeAll(oldEmailConfigurations);
                    return newEmailConfigurations.iterator().next();
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
    public WhiteLabel deleteEmailConfiguration(EmailConfigurationResource emailConfigurationResource)
            throws OperationException{
        WhiteLabel existingWhiteLabel =  null;
        String whiteLabelTemplateId = emailConfigurationResource.getWhiteLabelId();
        existingWhiteLabel = whiteLabelTemplateId != null ?
                whiteLabelRepository.getWhiteLabelById(whiteLabelTemplateId) : null;

        try {
            if (existingWhiteLabel == null) {
                throw new OperationException("WhiteLabelTemplateId not found with id" + existingWhiteLabel.getId());
            }

            if( !StringUtils.isEmpty(emailConfigurationResource.getId()) ) {
                if(log.isDebugEnabled()) {
                    log.debug("EmailConfiguration ResourceId:" + emailConfigurationResource.getId());
                }
                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }

                EmailConfiguration deleteEmailConfiguration =  null;
                Set<EmailConfiguration> emailConfigurationSet =
                        existingWhiteLabel.getEmailConfigurations();

                if(emailConfigurationSet != null) {
                    Optional<EmailConfiguration> emailConfiguration =
                            emailConfigurationSet.stream()
                                    .filter(emailConfigurationMatch -> emailConfigurationMatch.getId()
                                            .equalsIgnoreCase(emailConfigurationResource.getId())).findFirst();

                    if(emailConfiguration.isPresent()) {
                        deleteEmailConfiguration = emailConfiguration.get();
                        emailConfigurationSet.remove(deleteEmailConfiguration);
                    }else {
                        throw new OperationException("EmailConfiguration not found for id "
                                + emailConfigurationResource.getId());
                    }
                }

                WhiteLabel whiteLabel = whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                return whiteLabel;
            }
            else{
                throw new OperationException("Email Configuration is empty for "
                        + emailConfigurationResource.getId());
            }

        } catch (OperationException e) {
            throw new OperationException(Constants.ER01);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



}
