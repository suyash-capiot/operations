package com.coxandkings.travel.operations.repository.managedocumentation;

import com.coxandkings.travel.operations.criteria.managedocumentation.MDMUnConfiguredDataSearchCriteria;
import com.coxandkings.travel.operations.model.managedocumentation.MDMUnConfiguredData;

import java.util.List;

public interface MDMUnConfiguredDataRepository {

    public MDMUnConfiguredData saveDetails(MDMUnConfiguredData mdmUnConfiguredData);

    public MDMUnConfiguredData updateDetails(MDMUnConfiguredData mdmUnConfiguredData);

    public MDMUnConfiguredData getDetails(String id);

    public List<MDMUnConfiguredData> searchDetails(MDMUnConfiguredDataSearchCriteria searchCriteria);

    public void deleteDetails(String id);

    public List<MDMUnConfiguredData> findAllDetails();

}
