package com.coxandkings.travel.operations.utils.serviceOrderAndSupplierLiability;

import java.util.UUID;

public class ServiceOrderGenerator  {

    public static String generateUniqueId() {

        UUID uuid=UUID.randomUUID();
        return uuid.toString();
    }

}
