package com.coxandkings.travel.operations.resource.offlinecancellationsystem;

import java.util.List;

public class OfflineCancellationResponse {
    private List<OfflineSupplierCredentials> offlineSupplierCredentialsList;

    public List<OfflineSupplierCredentials> getOfflineSupplierCredentialsList() {
        return offlineSupplierCredentialsList;
    }

    public void setOfflineSupplierCredentialsList(List<OfflineSupplierCredentials> offlineSupplierCredentialsList) {
        this.offlineSupplierCredentialsList = offlineSupplierCredentialsList;
    }


}
