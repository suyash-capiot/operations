package com.coxandkings.travel.operations.service.whitelabel.impl;

import com.coxandkings.travel.operations.enums.whitelabel.WhiteLabelConfigurationType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.ClientLoginParameter;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.repository.whitelabel.ConfigurationTypeRepository;
import com.coxandkings.travel.operations.repository.whitelabel.WhiteLabelRepository;
import com.coxandkings.travel.operations.resource.whitelabel.ClientLoginParameterResource;
import com.coxandkings.travel.operations.service.whitelabel.WhiteLabelClientLoginParamsService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service("WhiteLabelClientLoginParams")
public class WhiteLabelClientLoginParamsServiceImpl implements WhiteLabelClientLoginParamsService {

    private static Logger log = LogManager.getLogger(WhiteLabelServiceImpl.class);

    @Autowired
    WhiteLabelRepository whiteLabelRepository;

    @Autowired
    ConfigurationTypeRepository configurationTypeRepository;



    @Override
    public ClientLoginParameter createOrUpdateClientLoginParameter(
            ClientLoginParameterResource clientLoginParameterResource) throws OperationException {
        WhiteLabel existingWhiteLabel = null;
        try
        {
            String whiteLabelTemplateId = clientLoginParameterResource.getWhiteLabelId();
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

            if( !StringUtils.isEmpty(clientLoginParameterResource.getId()) ) {
                if(log.isDebugEnabled()) {
                    log.debug("ClientLoginParameterResource ResourceId:" + clientLoginParameterResource.getId());
                }
                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }


                ClientLoginParameter updateClientLoginParameter =  null;
                Set<ClientLoginParameter> clientLoginParameters
                        = existingWhiteLabel.getClientLoginParameters();
                if(clientLoginParameters != null) {
                    Optional<ClientLoginParameter> clientLoginParameter = clientLoginParameters.stream()
                            .filter(clientLoginParameterMatch -> clientLoginParameterMatch.getId()
                                    .equalsIgnoreCase(clientLoginParameterResource.getId())).findFirst();

                    if (clientLoginParameter.isPresent()) {
                        updateClientLoginParameter = clientLoginParameter.get();
                        CopyUtils.copy(clientLoginParameterResource, updateClientLoginParameter);
                    } else {
                        throw new OperationException("ClientLoginParameter not found for id" + clientLoginParameterResource.getId());
                    }

                }

                whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                return updateClientLoginParameter;
            } else {
                ClientLoginParameter clientLoginParameter = new ClientLoginParameter();
                CopyUtils.copy(clientLoginParameterResource, clientLoginParameter);
                clientLoginParameter.setWhiteLabel(existingWhiteLabel);
                Set<ClientLoginParameter> oldClientLoginParameters = new HashSet<>();

                if (existingWhiteLabel.getClientLoginParameters() == null) {
                    oldClientLoginParameters.add(clientLoginParameter);
                    existingWhiteLabel.setClientLoginParameters(oldClientLoginParameters);
                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                    return existingWhiteLabel.getClientLoginParameters().iterator().next();
                } else {
                    oldClientLoginParameters.addAll(existingWhiteLabel.getClientLoginParameters());
                    existingWhiteLabel.getClientLoginParameters().add(clientLoginParameter);
                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                    Set<ClientLoginParameter> newClientLoginParameters = new HashSet<>();
                    newClientLoginParameters.addAll(existingWhiteLabel.getClientLoginParameters());
                    newClientLoginParameters.removeAll(oldClientLoginParameters);
                    return newClientLoginParameters.iterator().next();
                }
            }
        } catch (OperationException e) {
            throw new OperationException(Constants.ER01);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
