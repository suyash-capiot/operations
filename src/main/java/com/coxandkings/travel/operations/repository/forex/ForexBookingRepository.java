package com.coxandkings.travel.operations.repository.forex;

import com.coxandkings.travel.operations.criteria.forex.ForexCriteria;
import com.coxandkings.travel.operations.enums.forex.RequestStatus;
import com.coxandkings.travel.operations.model.forex.ForexBooking;
import com.coxandkings.travel.operations.resource.forex.PassengerNameResource;

import java.util.List;
import java.util.Map;

public interface ForexBookingRepository {


    Map<String, Object> getForexBookByCriteria(ForexCriteria criteria);

    ForexBooking saveOrUpdate(ForexBooking booking);

    ForexBooking getById(String id);

    ForexBooking getByRequestId(String id);

    List<String> getClientListForGivenName(String name);

    List<String> getClientList();

    List<String> getBookRefList();

    List<String> getBookRefListForGivenString(String bookRef);

    List<PassengerNameResource> getPaxListForGivenName(String name);

    List<String> getReqOrIndentIdsForGivenValue(String id);

    Integer getRequestStatusCount(RequestStatus status);

    ForexBooking getRecordByBookId(String bookID);
}
