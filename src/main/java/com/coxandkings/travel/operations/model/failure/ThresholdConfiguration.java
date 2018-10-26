package com.coxandkings.travel.operations.model.failure;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class ThresholdConfiguration {

    public ThresholdConfiguration(String configurationId, String companyMarket, String clientType) {
        this.configurationId = configurationId;
        this.companyMarket = companyMarket;
        this.clientType = clientType;
    }

    public ThresholdConfiguration() {
    }

    @Id
    @Column(name = "configurationId")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String configurationId;

    @Column
    private String companyMarket;

    @Column
    private String clientType;

    @OneToMany(mappedBy = "thresholdConfiguration", cascade = CascadeType.ALL)
    private List<ThresholdConfigurationProductDetails> thresholdConfigurationProductDetails;

    @Column
    private String thresholdOfferProcess; // Auto , manual

    @Column
    private String maxThresholdValue; //maximum price until which we are allowed to book though background process

    @Column
    private String clientCurrency;

    @Column
    private String status;


    public String getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(String configurationId) {
        this.configurationId = configurationId;
    }

    public List<ThresholdConfigurationProductDetails> getThresholdConfigurationProductDetails() {
        return thresholdConfigurationProductDetails;
    }

    public void setThresholdConfigurationProductDetails(List<ThresholdConfigurationProductDetails> thresholdConfigurationProductDetails) {
        this.thresholdConfigurationProductDetails = thresholdConfigurationProductDetails;
    }

    public String getCompanyMarket() {
        return companyMarket;
    }

    public void setCompanyMarket(String companyMarket) {
        this.companyMarket = companyMarket;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getMaxThresholdValue() {
        return maxThresholdValue;
    }

    public void setMaxThresholdValue(String maxThresholdValue) {
        this.maxThresholdValue = maxThresholdValue;
    }

    public String getClientCurrency() {
        return clientCurrency;
    }

    public void setClientCurrency(String clientCurrency) {
        this.clientCurrency = clientCurrency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThresholdOfferProcess() {
        return thresholdOfferProcess;
    }

    public void setThresholdOfferProcess(String thresholdOfferProcess) {
        this.thresholdOfferProcess = thresholdOfferProcess;
    }
}
