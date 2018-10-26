package com.coxandkings.travel.operations.utils.serviceOrderAndSupplierLiability;

public class ServiceOrderAndSupplierLiabilityUtils {

    public static Integer getNoOfPages(Integer pageSize, Integer noOfRows) {
        if (pageSize != null && noOfRows != null) {
            if(!noOfRows.equals(0) || !pageSize.equals(0)) {
                Integer noOfPages = noOfRows / pageSize;
                if (noOfRows % pageSize == 0)
                    return noOfPages;
                else
                    return noOfPages + 1;
            }
            else
                return 1;
        } else
            return 1;
    }

}
