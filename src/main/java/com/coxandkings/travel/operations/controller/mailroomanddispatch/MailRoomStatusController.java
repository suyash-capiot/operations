package com.coxandkings.travel.operations.controller.mailroomanddispatch;


import com.coxandkings.travel.operations.criteria.mailroomanddispatch.DispatchStatusCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.mailroomanddispatch.OutboundStatus;
import com.coxandkings.travel.operations.resource.mailroomMaster.DicpatchStatusResource;
import com.coxandkings.travel.operations.service.mailroomanddispatch.DispatchStatusServie;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/mailrooms/status")
@CrossOrigin(value = "*")
public class MailRoomStatusController {

    @Autowired
    private DispatchStatusServie dispatchStatusservice;

    @PostMapping("/v1/dispatch/add")
    public OutboundStatus addDispatchStatus(@RequestBody DicpatchStatusResource resource) throws OperationException {
        try {
            return dispatchStatusservice.save(resource);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20526);
        }
    }

    @PostMapping("/v1/dispatch/update")
    public OutboundStatus updateDispatchStatus(@RequestBody DicpatchStatusResource resource) throws OperationException {
        try {
            return dispatchStatusservice.save(resource);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20527);
        }
    }

    @GetMapping("/v1/dispatch/status/{id}")
    public OutboundStatus getDispatchStatusById(@PathVariable(name = "id") String typeId) throws OperationException {
        try {
            return dispatchStatusservice.getById(typeId);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20528);
        }
    }

    @PostMapping("/v1/dispatch/list/searchByCriteria")
    public List<OutboundStatus> getAllDispatchStatuss(@RequestBody DispatchStatusCriteria criteria) throws OperationException {
        try {
            return dispatchStatusservice.getByCriteria(criteria);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20529);
        }
    }

  }
