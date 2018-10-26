package com.coxandkings.travel.operations.service.mailroomanddispatch.Impl;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.DispatchStatusCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.mailroomanddispatch.OutboundStatus;
import com.coxandkings.travel.operations.repository.mailroomanddispatch.DispatchStatusRepository;
import com.coxandkings.travel.operations.resource.mailroomMaster.DicpatchStatusResource;
import com.coxandkings.travel.operations.service.mailroomanddispatch.DispatchStatusServie;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.coxandkings.travel.operations.utils.Constants.ER01;
@Service
public class DispatchStatusServieImpl implements DispatchStatusServie {

    private static final Logger log = LogManager.getLogger(DispatchStatusServie.class);

    @Autowired
    private DispatchStatusRepository dispatchStatusRepository;

    public OutboundStatus save(DicpatchStatusResource resource) throws OperationException {
        String id = resource.getId();
        OutboundStatus dispatchStatus = null;
        if(!StringUtils.isEmpty(id)) {
            // checking task exists or not
            OutboundStatus existingStatus = dispatchStatusRepository.getById(id);
            if(existingStatus == null) {
                log.error("mail room not exists for id:"+id);
                throw new OperationException(ER01);
            }
            CopyUtils.copy(resource, existingStatus);
            dispatchStatus = existingStatus;

        }else {
            dispatchStatus = new OutboundStatus();
            CopyUtils.copy(resource, dispatchStatus);
        }

        return dispatchStatusRepository.saveOrUpdate(dispatchStatus);
    }

    public OutboundStatus getById(String id){
        return dispatchStatusRepository.getById(id);
    }
    public List<OutboundStatus> getByCriteria(DispatchStatusCriteria criteria) {

        return dispatchStatusRepository.getByCriteria(criteria);
    }



}
