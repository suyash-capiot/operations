package com.coxandkings.travel.operations.repository.managedocumentation;

import com.coxandkings.travel.operations.criteria.managedocumentation.CutOffDateConfigurationSearchCriteria;
import com.coxandkings.travel.operations.model.managedocumentation.CutOffDateConfiguration;

import java.util.List;

public interface CutOffDateConfigurationRepository {

    public CutOffDateConfiguration saveDetails(CutOffDateConfiguration cutOffDateConfiguration);

    public CutOffDateConfiguration getDetails(String id);

    public List<CutOffDateConfiguration> searchDetails(CutOffDateConfigurationSearchCriteria searchCriteria);

    public void deleteDetails(String id);

    public List<CutOffDateConfiguration> findAllDetails();
}
