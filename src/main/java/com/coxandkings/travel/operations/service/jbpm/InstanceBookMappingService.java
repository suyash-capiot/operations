package com.coxandkings.travel.operations.service.jbpm;

import com.coxandkings.travel.operations.criteria.jbpm.InstanceBookMappingCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.jbpm.InstanceBookMapping;
import com.coxandkings.travel.operations.resource.jbpm.InstanceBookMappingResource;

import java.util.List;

public interface InstanceBookMappingService {
    public List<InstanceBookMapping> getByCriteria(InstanceBookMappingCriteria criteria);
    public InstanceBookMapping getById(String id);
    public InstanceBookMapping save(InstanceBookMappingResource resource) throws OperationException;
    public void remove(String id);
}
