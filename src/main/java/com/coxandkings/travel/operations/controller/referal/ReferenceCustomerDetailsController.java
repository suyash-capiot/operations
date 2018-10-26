package com.coxandkings.travel.operations.controller.referal;


import com.coxandkings.travel.operations.model.referal.ReferenceCustomerDetails;
import com.coxandkings.travel.operations.service.referal.ReferenceCustomerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/referenceCustomer")
public class ReferenceCustomerDetailsController {

    @Autowired
    private ReferenceCustomerDetailsService referenceCustomerDetailsService;

    @PutMapping("/v1/update")
    public ResponseEntity<ReferenceCustomerDetails> update(@RequestBody ReferenceCustomerDetails referenceCustomerDetails) {
        return new ResponseEntity<>(referenceCustomerDetailsService.saveOrUpdateReference(referenceCustomerDetails), HttpStatus.OK);
    }

    @GetMapping("/v1/get/{bookID}")
    public ResponseEntity<ReferenceCustomerDetails> getReferenceDetailsById(@PathVariable("bookID") String bookID) {
        return new ResponseEntity<>(referenceCustomerDetailsService.getReferenceById(bookID), HttpStatus.OK);
    }
}
