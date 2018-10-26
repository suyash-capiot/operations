package com.coxandkings.travel.operations.controller.newsupplierfirstbooking;


import com.coxandkings.travel.operations.enums.newsupplierfirstbooking.NewSupplierCommunicationStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.newsupplierfirstbooking.NewSupplierCommunication;
import com.coxandkings.travel.operations.resource.SupplierEmailResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.service.newsupplierfirstbooking.NewSupplierFirstBookingService;
import com.coxandkings.travel.operations.service.remarks.MDMUserService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/newSupplierFirstBooking")
public class NewSupplierFirstBookingController {

    @Autowired
    private NewSupplierFirstBookingService newSupplierFirstBookingService;

    @Autowired
    MDMUserService mdmUserService;


    @PostMapping("/v1/sendEmailToSupplier")
    public ResponseEntity<EmailResponse> sendEmailToSupplierWithBooking(@RequestBody SupplierEmailResource supplierEmailResource) throws OperationException {
        try {
            return new ResponseEntity<EmailResponse>(newSupplierFirstBookingService.sendEmailToSupplier(supplierEmailResource), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_21200);
        }
    }


    @GetMapping(value = "/v1/accepted/{bookId}")
    public ResponseEntity<?> accepted(@PathVariable("bookId") String bookId) throws OperationException {
        try {
            NewSupplierCommunication communication = new NewSupplierCommunication();
            communication.setBookId(bookId);
            communication.setStatus(NewSupplierCommunicationStatus.ACCEPTED);
            String json = "{\"message\":\"Already Replied\"}";
            if (newSupplierFirstBookingService.saveOrUpdateCommunication(communication) != null)
                return ResponseEntity.status(HttpStatus.OK).body(communication);
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(json);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_21201);
        }
    }


    @GetMapping(value = "/v1/rejected/{bookId}")
    public ResponseEntity<?> rejected(@PathVariable("bookId") String bookId) throws OperationException {
        try {
            NewSupplierCommunication communication = new NewSupplierCommunication();
            communication.setBookId(bookId);
            communication.setStatus(NewSupplierCommunicationStatus.REJECTED);
            String json = "{\"message\":\"Already Replied\"}";
            if (newSupplierFirstBookingService.saveOrUpdateCommunication(communication) != null)
                return ResponseEntity.status(HttpStatus.OK).body(communication);
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(json);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_21202);
        }
    }
}
