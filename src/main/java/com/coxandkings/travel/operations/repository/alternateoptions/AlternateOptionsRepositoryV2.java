package com.coxandkings.travel.operations.repository.alternateoptions;

import java.util.List;
import java.util.Map;
import com.coxandkings.travel.operations.criteria.alternateoptions.SearchCriteria;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptions;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsProductDetails;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsV2;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionResponse;

public interface AlternateOptionsRepositoryV2 {

	List<AlternateOptionsV2> saveAlternateOptions(List<AlternateOptionsV2> alternateOptions);

	List<AlternateOptions> searchAlternateOptions(AlternateOptions alternateOptions);

    AlternateOptionResponse searchAlternateOptions(AlternateOptions alternateOptions, int pageNumber, int pageSize);

    List<AlternateOptions> fetchAlternateoptions();

	List<AlternateOptionsProductDetails> fetchAlternateoptionProductDetails();

	AlternateOptionsV2 fetchAlternateOptionsForConfigurationId(String configurationId);

	AlternateOptionsV2 update(AlternateOptionsV2 alternateOptions);
	
	Map<String, Object> getAlternateOptionsByCriteria(SearchCriteria searchCriteria);

	int deleteMasterRecord(String configurationId);
}
