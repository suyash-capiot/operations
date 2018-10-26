package com.coxandkings.travel.operations.resource.failure.threshold;

import java.util.List;

public class ThresholdConfigurationResource {

    private String configurationId;

    private List<ThresholdConfigurationCompanyDetailsResource> thresholdConfigurationCompanyDetailsResources;

    private List<ThresholdConfigurationProductDetailsResource> thresholdConfigurationProductDetails;

    private String thresholdOfferProcess;

    private String higherLimitThreshold;

    private String lowerLimitThreshold;

    private String status;

    public String getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(String configurationId) {
        this.configurationId = configurationId;
    }

    public List<ThresholdConfigurationProductDetailsResource> getThresholdConfigurationProductDetails() {
        return thresholdConfigurationProductDetails;
    }

    public void setThresholdConfigurationProductDetails(List<ThresholdConfigurationProductDetailsResource> thresholdConfigurationProductDetails) {
        this.thresholdConfigurationProductDetails = thresholdConfigurationProductDetails;
    }

    public String getThresholdOfferProcess() {
        return thresholdOfferProcess;
    }

    public void setThresholdOfferProcess(String thresholdOfferProcess) {
        this.thresholdOfferProcess = thresholdOfferProcess;
    }

    public String getHigherLimitThreshold() {
        return higherLimitThreshold;
    }

    public void setHigherLimitThreshold(String higherLimitThreshold) {
        this.higherLimitThreshold = higherLimitThreshold;
    }

    public String getLowerLimitThreshold() {
        return lowerLimitThreshold;
    }

    public void setLowerLimitThreshold(String lowerLimitThreshold) {
        this.lowerLimitThreshold = lowerLimitThreshold;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
