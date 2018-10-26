package com.coxandkings.travel.operations.service.manageproductupdates;

import com.coxandkings.travel.operations.criteria.manageproductupdates.FlightUpdateSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.manageproductupdates.UpdatedFlightInfo;
import com.coxandkings.travel.operations.resource.manageproductupdates.CustomerOrClientEmailResource;
import com.coxandkings.travel.operations.resource.manageproductupdates.EticketResource;
import com.coxandkings.travel.operations.resource.manageproductupdates.FlightProductSearchResult;
import com.coxandkings.travel.operations.resource.manageproductupdates.ProductUpdateFlightResource;

import java.util.List;
import java.util.Map;

public interface ManageProductUpdatesService {

    public void handleProductUpdates() throws OperationException;

    public void handleProductUpdatesForFlights() throws OperationException;

    public FlightProductSearchResult getFlightProductsByCriteria(FlightUpdateSearchCriteria criteria);

    public Map<String, List<String>> updateFlightInfo(List<ProductUpdateFlightResource> resourceList) throws OperationException;

    public Map<String, List<String>> sendEmailToClientOrCustomer(List<CustomerOrClientEmailResource> emailResourceList) throws OperationException;

    List<UpdatedFlightInfo> saveUpdatedFlightInfo(List<ProductUpdateFlightResource> resourceList);

    public Map<String, Object> getUpdatedFlightInfo(Integer size, Integer page);

    Map<String, List<String>> generateEticket(List<EticketResource> eticketResource) throws OperationException;

     void alertManageProductUpdates(String payload);
}
