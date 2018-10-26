package com.coxandkings.travel.operations.service.forex;

import com.coxandkings.travel.operations.criteria.forex.ForexCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsHolidaysPaxInfo;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.forex.ForexBooking;
import com.coxandkings.travel.operations.resource.forex.ForexBookingResource;
import com.coxandkings.travel.operations.resource.forex.ForexCountResource;
import com.coxandkings.travel.operations.resource.forex.PassengerNameResource;

import java.util.List;
import java.util.Map;

public interface ForexBookingService {

    Map<String, Object> getByCriteria(ForexCriteria forexCriteria) throws OperationException;

    ForexBooking saveOrUpdate(ForexBookingResource resource) throws OperationException;

    void process(OpsBooking opsBooking, OpsProduct opsProduct, List<OpsHolidaysPaxInfo> forexPaxList) throws OperationException;

    ForexBooking getForexByID(String id) throws OperationException;

    ForexBooking getForexByRequestID(String id) throws OperationException;

    List<String> getClientListForGivenName(String name) throws OperationException;

    List<String> getClientList() throws OperationException;

    List<String> getBookRefList() throws OperationException;

    List<String> getBookRefListForGivenString(String bookRef) throws OperationException;

    List<PassengerNameResource> getPaxListForGivenName(String name) throws OperationException;

    List<String> getReqOrIndentIds(String id) throws OperationException;

    ForexBooking approveForexRequest(String forexId, String remarks) throws OperationException;

    ForexBooking rejectForexRequest(String forexId, String remarks) throws OperationException;

    ForexCountResource getCountByStatus() throws OperationException;

    ForexBooking submit(ForexBookingResource resource) throws OperationException;

    ForexBooking updateRecord(String uid, ForexBookingResource resource,
                                     String workflow) throws OperationException;

    Map<String, Object> getWorkflowList(ForexCriteria forexCriteria) throws OperationException;

	
    ForexBooking releaseEditLock(String requestId) throws OperationException;
}
