package com.coxandkings.travel.operations.resource.offlinecancellationsystem;

import java.util.List;

public class OfflineCancellationRespon {
    private List<OfflineCancellation> offlineSupplierCredentials;

    public List<OfflineCancellation> getOfflineSupplierCredentials() {
        return offlineSupplierCredentials;
    }

    public void setOfflineSupplierCredentials(List<OfflineCancellation> offlineSupplierCredentials) {
        this.offlineSupplierCredentials = offlineSupplierCredentials;
    }
}
