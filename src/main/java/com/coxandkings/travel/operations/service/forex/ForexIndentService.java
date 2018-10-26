package com.coxandkings.travel.operations.service.forex;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.forex.ForexBooking;
import com.coxandkings.travel.operations.model.forex.ForexIndent;
import com.coxandkings.travel.operations.resource.forex.ForexBookingResource;
import com.coxandkings.travel.operations.resource.forex.ForexIndentResource;

import java.util.List;

public interface ForexIndentService {

    ForexIndent saveOrUpdate(ForexIndentResource resource) throws OperationException;

    ForexIndent getIndentById(String id) throws OperationException;

    List<ForexIndent> getIndentsByForexId(String id) throws OperationException;

    List<ForexIndent> getIndentsByRequestId(String id) throws OperationException;

    void sendIndentToSupplier(String id) throws OperationException;

    ForexIndent getIndentByType(String id, String type) throws OperationException;

    List<String> getSupplierList() throws OperationException;

    List<String> getSupplierListForGivenName(String name) throws OperationException;

    ForexBooking updateIndent(String id, ForexBookingResource resource, String workflow) throws OperationException;

    void updateExistingForexIndent(ForexIndent indent, ForexIndentResource indentResource);
}
