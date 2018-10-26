package com.coxandkings.travel.operations.controller.alert;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.notification.NotificationResource;
import com.coxandkings.travel.operations.resource.notification.NotificationSearchCriteria;
import com.coxandkings.travel.operations.resource.notification.OpsAlertResponse;
import com.coxandkings.travel.operations.service.alert.AlertService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/alerts")
@CrossOrigin(origins = "*")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @PostMapping(path = "/v1/sendInlineMessageAlert")
    public ResponseEntity<NotificationResource> sendInlineMessageAlert(@RequestBody InlineMessageResource inlineMessageResource) throws OperationException, ParseException {
        NotificationResource notificationResource = alertService.sendInlineMessageAlert(inlineMessageResource);
        return new ResponseEntity<NotificationResource>(notificationResource, HttpStatus.OK);
    }

//    @PostMapping(path = "/v1/sendAlertEmail")
//    public ResponseEntity<String> sendAlertEmail(@RequestBody String alertDefinitionId , String templateId , Map<String, String> templateData) throws OperationException, ParseException {
//        String msg = alertService.sendAlertEmail(alertDefinitionId, templateId,templateData);
//        return new ResponseEntity<String>(msg, HttpStatus.OK);
//    }


    //fetching alerts based on user id
    @GetMapping(path = "/v1/notifications/{userId}")
    public ResponseEntity getByUserId(@PathVariable String userId) throws JSONException, OperationException {
        return new ResponseEntity(alertService.getByUserId(userId), HttpStatus.OK);
    }

    //fetching alerts based on business process
    @GetMapping(path = "/v1/notifications")
    public ResponseEntity getNotificationsByBusinessProcess(@RequestParam String businessProcess, @RequestParam String userId, @RequestParam String companyId) throws JSONException, OperationException {
        return new ResponseEntity(alertService.getNotificationsByBusinessProcess(businessProcess, userId, companyId), HttpStatus.OK);
    }

    @PostMapping("/v1/search")
    public ResponseEntity<NotificationResource> searchAlertByCriteria(@RequestBody NotificationSearchCriteria notificationSearchCriteria) throws OperationException {
        List<OpsAlertResponse> opsAlertResponses = alertService.searchAlertByCriteria(notificationSearchCriteria);
        return new ResponseEntity(opsAlertResponses, HttpStatus.OK);
    }

    @GetMapping("/v1/")
    public NotificationResource createAlert(@RequestParam String businessProcess, @RequestParam String function, @RequestParam String companyName,
                                            @RequestParam String alertName, @RequestParam String userId, String message) throws OperationException {
        return alertService.createAlert(businessProcess, function, companyName, alertName, userId, message);
    }


    @PostMapping(path = "/v1/dismiss")
    public ResponseEntity dismissNotification(@RequestParam String alertID, @RequestParam String userID) throws OperationException {
        return new ResponseEntity(alertService.dismissNotification(alertID, userID), HttpStatus.OK);
    }
}