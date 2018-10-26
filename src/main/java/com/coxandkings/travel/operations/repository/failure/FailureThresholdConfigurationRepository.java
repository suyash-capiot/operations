package com.coxandkings.travel.operations.repository.failure;

import com.coxandkings.travel.operations.model.failure.ThresholdConfiguration;
import com.coxandkings.travel.operations.model.failure.ThresholdConfigurationProductDetails;

import java.util.List;

public interface FailureThresholdConfigurationRepository {

    List<ThresholdConfiguration> saveThresholdConfiguration(List<ThresholdConfiguration> alternateOptions);

    List<ThresholdConfiguration> searchThresholdConfiguration(ThresholdConfiguration thresholdConfiguration);

    List<ThresholdConfiguration> fetchThresholdConfiguration();

    List<ThresholdConfigurationProductDetails> fetchThresholdConfigurationProductDetails();

    ThresholdConfiguration fetchThresholdForConfigurationId(String configurationId);

}
