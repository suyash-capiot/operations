package com.coxandkings.travel.operations.controller.email;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.model.email.Email;
import com.coxandkings.travel.operations.resource.communication.UpdateCommunicationTagsResource;
import com.coxandkings.travel.operations.resource.email.*;
import com.coxandkings.travel.operations.service.email.EmailService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/emails")
@CrossOrigin(origins = "*")
public class EmailController {

    private static Logger logger = LogManager.getLogger(EmailController.class);

    @Autowired
    private EmailService emailService;

    @PostMapping(path = "/sendEmailWithBody")
    public ResponseEntity<EmailResponse> sendEmailWithBody(@RequestBody(required = false) EmailWithBodyResource resource) throws OperationException, SQLException, IOException {
        try {
            EmailResponse response = emailService.sendEmail(resource);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while sending sendEmailWithBody(). ", e);
            throw new OperationException(Constants.OPS_ERR_20016);
        }
    }

    @PostMapping(path = "/sendEmailUsingTemplate")
    public ResponseEntity<EmailResponse> sendEmailUsingTemplate(@RequestBody(required = false) EmailUsingTemplateResource resource) throws IOException, OperationException, SQLException, JSONException, MessagingException {
        try {
            EmailResponse response = emailService.sendEmail(resource);
            if (response.getStatus().toUpperCase().equals("ERROR")) {
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            } else
                return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while sending sendEmailUsingTemplate(). ", e);
            throw new OperationException(Constants.OPS_ERR_20016);
        }
    }

    @PostMapping(path = "/sendEmailUsingTemplateAndDocuments")
    public ResponseEntity<EmailResponse> sendEmailUsingTemplateAndDocuments(@RequestBody(required = false) EmailUsingTemplateAndDocumentsResource resource) throws IOException, OperationException, SQLException, JSONException, MessagingException {
        try {
            EmailResponse response = emailService.sendEmail(resource);
            if (response.getStatus().toUpperCase().equals("ERROR")) {
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            } else
                return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (OperationException e) {
            logger.error("Error occurred while sending sendEmailUsingTemplateAndDocuments(). ", e);
            throw new OperationException(Constants.OPS_ERR_20016);
        }
    }

    @PostMapping(path = "/sendEmailUsingBodyAndDocuments")
    public ResponseEntity<EmailResponse> sendEmailUsingBodyAndDocuments(@RequestBody(required = false) EmailUsingBodyAndDocumentsResource resource) throws OperationException {
        try {
            EmailResponse response = emailService.sendEmail(resource);
            if (response.getStatus().toUpperCase().equals("ERROR")) {
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            } else
                return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while sending sendEmailUsingBodyAndDocuments(). ", e);
            throw new OperationException(Constants.OPS_ERR_20016);
        }
    }

    @PostMapping(path = "/v1/sendEmailWithBody")
    public ResponseEntity<EmailResponse> sendEmailWithBodyV1(@RequestBody(required = false) EmailWithBodyResource resource) throws MessagingException, OperationException, SQLException, IOException {
        try {
            EmailResponse response = emailService.sendEmail(resource);
            if (response.getStatus().toUpperCase().equals("ERROR")) {
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            } else
                return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while sending sendEmailWithBodyV1(). ", e);
            throw new OperationException(Constants.OPS_ERR_20016);
        }

    }

    @PostMapping(path = "/v1/sendEmailUsingTemplate")
    public ResponseEntity<EmailResponse> sendEmailUsingTemplateV1(@RequestBody(required = false) EmailUsingTemplateResource resource) throws IOException, OperationException, SQLException, JSONException, MessagingException {
        try {
            EmailResponse response = emailService.sendEmail(resource);
            if (response.getStatus().toUpperCase().equals("ERROR")) {
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            } else
                return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while sending sendEmailUsingTemplateV1(). ", e);
            throw new OperationException(Constants.OPS_ERR_20016);
        }
    }

    @PostMapping(path = "/v1/sendEmailUsingTemplateAndDocuments")
    public ResponseEntity<EmailResponse> sendEmailUsingTemplateAndDocumentsV1(@RequestBody(required = false) EmailUsingTemplateAndDocumentsResource resource) throws IOException, OperationException, SQLException, JSONException, MessagingException {
        try {
            EmailResponse response = emailService.sendEmail(resource);
            if (response.getStatus().toUpperCase().equals("ERROR")) {
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            } else
                return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while sending sendEmailUsingTemplateAndDocumentsV1(). ", e);
            throw new OperationException(Constants.OPS_ERR_20016);
        }
    }

    @PostMapping(path = "/v1/sendEmailUsingBodyAndDocuments")
    public ResponseEntity<EmailResponse> sendEmailUsingBodyAndDocumentsV1(@RequestBody(required = false) EmailUsingBodyAndDocumentsResource resource) throws IOException, OperationException, SQLException, MessagingException {
        try {
            EmailResponse response = emailService.sendEmail(resource);
            if (response.getStatus().toUpperCase().equals("ERROR")) {
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            } else
                return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while sending sendEmailUsingBodyAndDocumentsV1(). ", e);
            throw new OperationException(Constants.OPS_ERR_20016);
        }
    }

    @GetMapping(path = "/v1/{emailId}")
    public ResponseEntity<Email> getEmailById(@PathVariable(required = false) String emailId
    ) throws OperationException {
        Email email = null;
        try {
            email = emailService.getEmailById(emailId);
            return new ResponseEntity<>(email, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while fetching mail info - getEmailById(). ", e);
            throw new OperationException(Constants.OPS_ERR_20017);
        }
    }

    @GetMapping(path = "/v1/messageid/{id}")
    public ResponseEntity<List<Email>> getEmailByMessageId(@PathVariable("id") String id
    ) throws OperationException {
        try {

            return new ResponseEntity<List<Email>>(emailService.getEmailByMessageId(id), HttpStatus.OK);
        } catch (OperationException e) {
            logger.error("Error occurred while fetching mail info - getEmailByMessageId(). ", e);
            throw new OperationException(Constants.OPS_ERR_20018);
        }
    }

    @PutMapping(path = "/v1/markasread")
    public ResponseEntity<Email> markAsRead(@RequestParam String id) throws OperationException {
        try {
            Email email = emailService.markAsRead(id);
            return new ResponseEntity<>(email, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while marking email as Read - markasread(). ", e);
            throw new OperationException(Constants.OPS_ERR_20019);
        }
    }

    @GetMapping(value = "/v1/communicationtags/{id}")
    public ResponseEntity<CommunicationTags> getCommunicationTags(@PathVariable("id") String id) throws OperationException {
        try {
            CommunicationTags communicationTags = emailService.getAssociatedTags(id);
            return new ResponseEntity<>(communicationTags, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while fetching communication tags getCommunicationTags()", e);
            throw new OperationException(Constants.OPS_ERR_20020);
        }
    }

    @PutMapping(value = "/v1/updatecommunicationtags")
    public ResponseEntity<Email> getCommunicationTags(@RequestBody UpdateCommunicationTagsResource updateCommunicationTagsResource) throws OperationException {
        try {
            Email email = emailService.updateCommunicationTags(updateCommunicationTagsResource.getId(), updateCommunicationTagsResource.getCommunicationTags());
            return new ResponseEntity<>(email, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while update communication tags updatecommunicationtags()", e);
            throw new OperationException(Constants.OPS_ERR_20021);
        }
    }
}
