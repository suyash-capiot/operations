package com.coxandkings.travel.operations.service.managenoshow;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.managenoshow.NoShowResource;

public interface NoShowService {
    public String updateNoShow(NoShowResource noShowResource) throws OperationException;
    public Boolean isNoShowAttributeAlreadyApplied(String bookId,String orderId);
}
