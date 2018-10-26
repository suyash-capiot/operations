package com.coxandkings.travel.operations.service.jbpm.impl;

import com.coxandkings.travel.operations.criteria.jbpm.InstanceBookMappingCriteria;
import com.coxandkings.travel.operations.enums.Status;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.jbpm.InstanceBookMapping;
import com.coxandkings.travel.operations.repository.jbpm.InstanceBookMappingRepository;
import com.coxandkings.travel.operations.resource.jbpm.InstanceBookMappingResource;
import com.coxandkings.travel.operations.service.jbpm.InstanceBookMappingService;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public class InstanceBookMappingServiceImpl implements InstanceBookMappingService {

    private static Logger log = LogManager.getLogger(InstanceBookMappingService.class);

    @Autowired
    private InstanceBookMappingRepository repository;

    @Override
    public List<InstanceBookMapping> getByCriteria(InstanceBookMappingCriteria criteria) {
        return repository.getByCriteria(criteria);
    }

    @Override
    public InstanceBookMapping getById(String id) {
        return repository.getById(id);
    }

    @Override
    @Transactional
    public InstanceBookMapping save(InstanceBookMappingResource resource) throws OperationException {
        InstanceBookMapping instanceBookMapping = null;
        try
        {
            if(!StringUtils.isEmpty(resource.getId())) {
                if(log.isDebugEnabled()) {
                    log.debug("mapping id:"+resource.getId());
                }
                InstanceBookMapping existingBookMapping = repository.getById(resource.getId());
                if(log.isDebugEnabled()) {
                    log.debug("mapping Details:"+existingBookMapping);
                }
                if(existingBookMapping == null) {
                    //TODO: throw exception driver not found based on given id
                    throw new Exception("mapping not found with id"+resource.getId());
                }
                CopyUtils.copy(resource, existingBookMapping);
                instanceBookMapping = existingBookMapping;
            } else {
                instanceBookMapping = new InstanceBookMapping();
                // checking duplicate
                InstanceBookMappingCriteria criteria = new InstanceBookMappingCriteria();
                criteria.setBookingRefIds(resource.getBookingRefId());
                criteria.setWorkFlow(resource.getWorkFlow());
                if(!(StringUtils.isEmpty(resource.getUserTaskName())))
                    criteria.setUserTaskName(resource.getUserTaskName());
                if(!(StringUtils.isEmpty(resource.getEntityRefId())))
                    criteria.setEntityRefId(resource.getEntityRefId());
                criteria.setStatus(Status.ACTIVE);
                List <InstanceBookMapping> driversByCriteria = repository.getByCriteria(criteria);
                if(!CollectionUtils.isEmpty(driversByCriteria)) {
                    throw new OperationException("Mapping is  available for booking id"+resource.getBookingRefId());
                }
                CopyUtils.copy(resource, instanceBookMapping);
            }


            instanceBookMapping = repository.saveOrUpdate(instanceBookMapping);

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return instanceBookMapping;
    }

    @Override
    public void remove(String id) {
        repository.remove(id);
    }
}
