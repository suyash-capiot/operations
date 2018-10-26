package com.coxandkings.travel.operations.service.reconfirmation.common;

import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationConfigFor;

public class ReconfirmationConfiguration {

    private ReconfirmationConfigFor configFor;
    private ClientConfiguration clientConfiguration;
    private SupplierConfiguration supplierConfiguration;

    public ReconfirmationConfigFor getConfigFor() {
        return configFor;
    }

    public void setConfigFor(ReconfirmationConfigFor configFor) {
        this.configFor = configFor;
    }

    public ClientConfiguration getClientConfiguration() {
        return clientConfiguration;
    }

    public void setClientConfiguration(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    public SupplierConfiguration getSupplierConfiguration() {
        return supplierConfiguration;
    }

    public void setSupplierConfiguration(SupplierConfiguration supplierConfiguration) {
        this.supplierConfiguration = supplierConfiguration;
    }
}
