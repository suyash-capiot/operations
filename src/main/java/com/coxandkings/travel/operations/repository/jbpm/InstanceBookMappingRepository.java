package com.coxandkings.travel.operations.repository.jbpm;

import com.coxandkings.travel.operations.criteria.jbpm.InstanceBookMappingCriteria;
import com.coxandkings.travel.operations.model.jbpm.InstanceBookMapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface InstanceBookMappingRepository {

    public List<InstanceBookMapping> getByCriteria(InstanceBookMappingCriteria criteria);
    public InstanceBookMapping getById(String id);
    public InstanceBookMapping saveOrUpdate(InstanceBookMapping instanceBookMapping);
    public void remove(String id);
}
