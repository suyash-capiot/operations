package com.coxandkings.travel.operations.repository.alternateoptions;

import java.util.List;

import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptions;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsProductDetails;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionResponse;

public interface AlternateOptionsRepository {

	List<AlternateOptions> saveAlternateOptions(List<AlternateOptions> alternateOptions);

	List<AlternateOptions> searchAlternateOptions(AlternateOptions alternateOptions);

    AlternateOptionResponse searchAlternateOptions(AlternateOptions alternateOptions, int pageNumber, int pageSize);

    List<AlternateOptions> fetchAlternateoptions();

	List<AlternateOptionsProductDetails> fetchAlternateoptionProductDetails();

	AlternateOptions fetchAlternateOptionsForConfigurationId(String configurationId);

	AlternateOptions update(AlternateOptions alternateOptions);

}
