package com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability;

public enum ServiceOrderAndSupplierLiabilityType {
    PSO("ProvisionalServiceOrder"),
    FSO("FinalServiceOrder"),
    PSL("ProvisionalSupplierLiability"),
    FSL("FinalSupplierLiability");

    private String value;

    ServiceOrderAndSupplierLiabilityType(String value){
        this.value=value;
    }

    public String getValue(){
        return this.value;
    }

    public static ServiceOrderAndSupplierLiabilityType getEnumValueFromString(String status) {
        ServiceOrderAndSupplierLiabilityType serviceOrderAndSupplierLiabilityType = null;
        for (ServiceOrderAndSupplierLiabilityType serviceOrderAndSupplierLiability : ServiceOrderAndSupplierLiabilityType.values()) {
            if (serviceOrderAndSupplierLiability.getValue().equalsIgnoreCase(status)) {
                serviceOrderAndSupplierLiabilityType = serviceOrderAndSupplierLiability;
                break;
            }
        }
        return serviceOrderAndSupplierLiabilityType;
    }

}
