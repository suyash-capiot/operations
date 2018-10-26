package com.coxandkings.travel.operations.controller.sms;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.model.sms.InboundSMS;
import com.coxandkings.travel.operations.model.sms.OutboundSMS;
import com.coxandkings.travel.operations.resource.communication.UpdateCommunicationTagsResource;
import com.coxandkings.travel.operations.resource.sms.OutboundSMSResource;
import com.coxandkings.travel.operations.service.sms.SMSService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/sms")
public class SMSController {

    @Autowired
    private SMSService SMSService;


    @PostMapping(value = "/v1/create")
    public OutboundSMS sendMessage(@RequestBody (required = false)OutboundSMSResource outboundSMSResource) throws OperationException{
        try {
            return SMSService.sendMsg(outboundSMSResource);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_30701);
        }
    }

    @PostMapping(value = "/v1/receive")
    public void receiveMessage(HttpServletRequest request, HttpServletResponse response) throws OperationException {
        try {
            InboundSMS inboundSMS = SMSService.receiveMessage(request);
            response.getWriter().print(inboundSMS.toString());
        } catch (IOException e) {
            throw new OperationException(Constants.OPS_ERR_30702);
        }
    }

    @GetMapping(value="/v1/{id}")
    public ResponseEntity<OutboundSMS> gettById(@PathVariable("id") String id) throws OperationException {
        try {
            OutboundSMS outboundSMS = SMSService.getSMSById(id);
            return new ResponseEntity<>(outboundSMS, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_30703);
        }
    }

    @PutMapping(path="/v1/markasread")
    public ResponseEntity<OutboundSMS> markAsRead(@RequestParam String id) throws OperationException {
        try {
            OutboundSMS sms = SMSService.markAsRead(id);
            return new ResponseEntity<>(sms, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_30704);
        }
    }

    @GetMapping(value="/v1/communicationtags/{id}")
    public ResponseEntity<CommunicationTags> getCommunicationTags(@PathVariable("id") String id) throws OperationException {
        try {
            CommunicationTags communicationTags = SMSService.getAssociatedTags(id);
            return new ResponseEntity<>(communicationTags, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_30705);
        }
    }

    @PutMapping(value = "/v1/updatecommunicationtags")
    public ResponseEntity<OutboundSMS> getCommunicationTags(@RequestBody UpdateCommunicationTagsResource updateCommunicationTagsResource) throws OperationException {
        try {
            OutboundSMS outboundSMS = SMSService.updateCommunicationTags(updateCommunicationTagsResource.getId(), updateCommunicationTagsResource.getCommunicationTags());
            return new ResponseEntity<>(outboundSMS, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_30706);
        }
    }
}
