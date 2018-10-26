package com.coxandkings.travel.operations.resource.offlinecancellationsystem;

import java.util.List;

public class OfflineCancellation {
    private String market;
    private List<OfflineSupplierCredentials> offlineSupplierCredentialsList;

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public List<OfflineSupplierCredentials> getOfflineSupplierCredentialsList() {
        return offlineSupplierCredentialsList;
    }

    public void setOfflineSupplierCredentialsList(List<OfflineSupplierCredentials> offlineSupplierCredentialsList) {
        this.offlineSupplierCredentialsList = offlineSupplierCredentialsList;
    }
}
