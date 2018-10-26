package com.coxandkings.travel.operations.controller.flightDiscrepancy;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.flightDiscrepancy.FlightDiscrepancyRecordUpdate;
import com.coxandkings.travel.operations.resource.flightDiscrepancy.FlightDiscrepancySearchCriteria;
import com.coxandkings.travel.operations.service.flightDiscrepancy.FlightDiscrepancyService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/flightDiscrepancy")
public class FlightDiscrepancyController {

    @Autowired
    private FlightDiscrepancyService flightDiscrepancyService;

    @PostMapping(value = "/v1/search")
    public Map search(@RequestBody(required = false) FlightDiscrepancySearchCriteria flightDiscrepancySearchCriteria) throws OperationException {
        try {
            return flightDiscrepancyService.search(flightDiscrepancySearchCriteria);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20200);
        }
    }

    @PostMapping(value = "/v1/update")
    public Map update(@RequestBody List<FlightDiscrepancyRecordUpdate> flightDiscrepancyRecordUpdates) throws OperationException {
        try {
            return flightDiscrepancyService.update(flightDiscrepancyRecordUpdates);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20201);
        }
    }

    @GetMapping(value = "/v1/getSourceAirlines")
    public Set<String> getSourceAirlines(@RequestParam String filterByName) throws OperationException {
        try {
            return flightDiscrepancyService.filterByName(filterByName);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20202);
        }
    }

    @GetMapping(value = "/v1/getIata")
    public List<Object> getIata() throws OperationException {
        try {
            return flightDiscrepancyService.getIata();
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20203);
        }
    }

    @GetMapping(value = "/v1/getDiscrepancyTypes")
    public List<String> getDiscrepancyTypes() throws OperationException {
        try {
            return flightDiscrepancyService.getDiscrepancyTypes();
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20204);
        }
    }

    @GetMapping(value = "/v1/getFilterByNames")
    public List<String> getFilterByNames() throws OperationException {
        try {
            return flightDiscrepancyService.getFilterByNames();
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20205);
        }
    }

    @GetMapping(value = "/v1/getDiscrepancyStatusList")
    public List<String> getDiscrepancyStatusList() throws OperationException {
        try {
            return flightDiscrepancyService.getDiscrepancyStatusList();
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20206);
        }
    }

    @GetMapping(value = "/v1/getTransactionTypes")
    public List<String> getTransactionTypes() throws OperationException {
        try {
            return flightDiscrepancyService.getTransactionTypes();
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20207);
        }
    }

}
