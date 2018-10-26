package com.coxandkings.travel.operations.controller.callrecord;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.generator.IdGenerator;
import com.coxandkings.travel.operations.model.callrecord.CallRecord;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.resource.callrecord.CallRecordResource;
import com.coxandkings.travel.operations.resource.communication.UpdateCommunicationTagsResource;
import com.coxandkings.travel.operations.service.callrecord.CallRecordService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/callrecords")
@CrossOrigin(origins = "*")
public class CallRecordController {
    @Autowired
    private CallRecordService callRecordService;
    @Autowired
    private IdGenerator idGenerator;

    @PostMapping(value = "/v1/add")
    public ResponseEntity<CallRecord> createCallRecord(@RequestBody(required = false) CallRecordResource callRecordResource) throws OperationException {
        try {
            CallRecord callRecord = callRecordService.saveOrUpdateCallRecord(callRecordResource);
            return new ResponseEntity<>(callRecord, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10800);
        }
    }

    @PostMapping(value = "/v1/update")
    public ResponseEntity<CallRecord> updateCallRecord(@RequestBody(required = false) CallRecordResource callRecordResource) throws OperationException {
        try {
            CallRecord callRecord = callRecordService.updateCallRecord(callRecordResource);
            return new ResponseEntity<>(callRecord, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10801);
        }
    }

    @GetMapping(value = "/v1/{id}")
    public ResponseEntity<CallRecord> getCallRecordById(@PathVariable("id") String id) throws OperationException {
        try {
            CallRecord callRecord = callRecordService.getCallRecordById(id);
            return new ResponseEntity<>(callRecord, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10802);
        }
    }

    @PutMapping(path = "/v1/markasread")
    public ResponseEntity<CallRecord> markAsRead(@RequestParam String id) throws OperationException {
        try {
            CallRecord callRecord = callRecordService.markAsRead(id);
            return new ResponseEntity<>(callRecord, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10803);
        }
    }

    @GetMapping(value = "/v1/communicationtags/{id}")
    public ResponseEntity<CommunicationTags> getCommunicationTags(@PathVariable("id") String id) throws OperationException {
        try {
            CommunicationTags communicationTags = callRecordService.getAssociatedTags(id);
            return new ResponseEntity<>(communicationTags, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10804);
        }
    }

    @PutMapping(value = "/updatecommunicationtags")
    public ResponseEntity<CallRecord> getCommunicationTags(@RequestBody UpdateCommunicationTagsResource updateCommunicationTagsResource) throws OperationException {
        try {
            CallRecord callRecord = callRecordService.updateCommunicationTags(updateCommunicationTagsResource.getId(), updateCommunicationTagsResource.getCommunicationTags());
            return new ResponseEntity<>(callRecord, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_10805);
        }
    }

    @GetMapping(value = "/v1/fso/{id}")
    public ResponseEntity<String> getFSOId(@PathVariable("id") String id) throws OperationException {
        try {

            return new ResponseEntity<>(idGenerator.generateFSOId(id), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException("Failed to generate id");
        }
    }
}
