package com.coxandkings.travel.operations.service.flightDiscrepancy;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.flightDiscrepancy.FlightDiscrepancyRecord;
import com.coxandkings.travel.operations.resource.flightDiscrepancy.FlightDiscrepancyRecordUpdate;
import com.coxandkings.travel.operations.resource.flightDiscrepancy.FlightDiscrepancySearchCriteria;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FlightDiscrepancyService {
    Map search(FlightDiscrepancySearchCriteria flightDiscrepancySearchCriteria) throws IOException, OperationException, ParseException;
    Map update(List<FlightDiscrepancyRecordUpdate> flightDiscrepancyRecordUpdates) throws OperationException, ParseException;

    Set<String> filterByName(String filterByName) throws OperationException;
    List<String> getDiscrepancyStatusList();
    List<String> getTransactionTypes();
    List<String> getDiscrepancyTypes();
    List<String> getFilterByNames();

    List<Object> getIata() throws OperationException;

    FlightDiscrepancyRecord getById(String id) throws OperationException;
}
