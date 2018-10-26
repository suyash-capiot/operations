package com.coxandkings.travel.operations.service.whitelabel.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.repository.whitelabel.WhiteLabelRepository;
import com.coxandkings.travel.operations.resource.whitelabel.WhiteLabelTemplateResource;
import com.coxandkings.travel.operations.service.whitelabel.WhiteLabelTemplateService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;

@Service("WhiteLabelTemplate")
public class WhiteLabelTemplateServiceImpl implements WhiteLabelTemplateService{

    private static Logger log = LogManager.getLogger(WhiteLabelServiceImpl.class);

    @Autowired
    WhiteLabelRepository whiteLabelRepository;



    @Override
    public WhiteLabel saveWhiteLabelTemplate(WhiteLabelTemplateResource whiteLabelTemplateResource)
            throws OperationException {
        try {
            String whiteLabelId = whiteLabelTemplateResource.getWhiteLabelId();
            WhiteLabel existingWhiteLabel =
                    whiteLabelId != null ? whiteLabelRepository.getWhiteLabelById(whiteLabelId) : null;

            if (existingWhiteLabel == null) {
                throw new Exception("WhiteLabel not found for Id " + whiteLabelId);
            }
            if (StringUtils.isEmpty(whiteLabelTemplateResource.getId())) {
                if (log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabel Id:" + existingWhiteLabel.getId());
                }
                existingWhiteLabel.setTemplateId(whiteLabelTemplateResource.getTemplateId());
            }
            return whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
        } catch (OperationException e) {
            throw new OperationException(Constants.ER01);
        }  catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
