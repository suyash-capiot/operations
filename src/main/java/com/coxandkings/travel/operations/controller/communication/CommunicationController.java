package com.coxandkings.travel.operations.controller.communication;

import com.coxandkings.travel.operations.criteria.communication.CommunicationCriteria;
import com.coxandkings.travel.operations.criteria.communication.CommunicationTagCriteria;
import com.coxandkings.travel.operations.criteria.communication.CommunicationUnreadCriteria;
import com.coxandkings.travel.operations.enums.communication.UserProfileType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.communication.BaseCommunication;
import com.coxandkings.travel.operations.resource.communication.CommunicationHistoryResponse;
import com.coxandkings.travel.operations.service.communication.CommunicationService;
import com.coxandkings.travel.operations.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/communications")
@CrossOrigin("*")
public class CommunicationController {

    @Autowired
    private CommunicationService communicationService;

    @GetMapping(path = "/v1/bookId/{bookId}")
    public ResponseEntity<Map<String, Object>> getCommunicaitonByBookingID(@PathVariable String bookId,
                                                                           @RequestParam Integer size, @RequestParam Integer page) throws OperationException {
        try {
            Map<String, Object> map = communicationService.getCommunicationByBookingId(bookId, size, page);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11100);
        }
    }

    @GetMapping(path = "/v1/userId/{userId}")
    public ResponseEntity<Map<String, Object>> getCommunicaitonByUserID(@PathVariable String userId, @RequestParam Integer size, @RequestParam Integer page) throws OperationException {
        try {
            Map<String, Object> map = communicationService.getCommunicationByUserId(userId, size, page);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11101);
        }
    }

    @GetMapping(path = "/v1/bookId/{bookId}/{sort}")
    public ResponseEntity<Map<String, Object>> getCommunicaitonByBookingIDWithSortCriteria(@PathVariable String bookId, @RequestParam Integer size, @RequestParam Integer page, @PathVariable String sort) throws OperationException {
        try {
            Map<String, Object> map = communicationService.getCommunicationByBookingId(bookId, size, page, sort);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11102);
        }
    }

    @GetMapping(path = "/v1/userId/{userId}/{sort}")
    public ResponseEntity<Map<String, Object>> getCommunicaitonByUserIDWithSortCriteria(@PathVariable String userId, @RequestParam Integer size, @RequestParam Integer page, @PathVariable String sort) throws OperationException {
        try {
            Map<String, Object> map = communicationService.getCommunicationByUserId(userId, size, page, sort);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11103);
        }
    }

    @GetMapping(path = "/v1/template/{templateId}")
    public ResponseEntity<String> getCommunicationTemplate(@PathVariable String templateId) throws OperationException, JSONException {
        try {
            String templateBody = communicationService.getCommunicationTemplate(templateId);
            return new ResponseEntity<>(templateBody, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11104);
        }
    }

    @GetMapping(path="/v1/listTemplates/businessProcess/{businessProcess}/function/{function}")
    public ResponseEntity<JSONObject> getTemplateList(@PathVariable("businessProcess") String businessProcess , @PathVariable("function") String function  ) throws OperationException, JSONException {
        try {
            String data = communicationService.getTemplateDetails(businessProcess, function);
            JSONObject jsonData = new JSONObject(data);
            return new ResponseEntity<>(jsonData, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11105);
        }
    }

    @PostMapping(path="/v1/unread")
    public ResponseEntity<Map> getUnreadCounts(@RequestBody(required = false) CommunicationUnreadCriteria communicationUnreadCriteria ) throws OperationException, JSONException {
        try {
            Map<String, Long> data = communicationService.getUnreadCounts(communicationUnreadCriteria);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11106);
        }
    }

    @PostMapping(path = "/v1/criteria")
    public ResponseEntity<Map<String,Object>> getCommunicationByCriteria(@RequestBody(required = false) CommunicationCriteria criteria) throws OperationException {
        try {
            Map<String, Object> map = communicationService.getCommunicationByCriteria(criteria);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11107);
        }
    }

    @PostMapping(path = "/v1/criteria/communicationTags")
    public ResponseEntity<CommunicationHistoryResponse> getCommunicationByCriteria(@RequestBody(required = false) CommunicationTagCriteria criteria) throws OperationException {
        try {
            List<BaseCommunication> communications = communicationService.getCommunicationByCommunicationTagsCriteria(criteria);
            CommunicationHistoryResponse communicationHistoryResponse = new CommunicationHistoryResponse();
            communicationHistoryResponse.setCommunications(communications);
            communicationHistoryResponse.setTotalUnread(communicationService.countTotalUnread(communications));
            return new ResponseEntity<>(communicationHistoryResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11108);
        }
    }

    @GetMapping(path = "/v1/listEmailIds/{userProfileType}")
    public ResponseEntity<List<String>> getSupplierEmail(@PathVariable("userProfileType") UserProfileType userProfileType, @RequestParam("criteria") String criteria) throws OperationException {
        try {
            return new ResponseEntity<>(communicationService.getListEmailIds(criteria, userProfileType), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11109);
        }
    }

}
