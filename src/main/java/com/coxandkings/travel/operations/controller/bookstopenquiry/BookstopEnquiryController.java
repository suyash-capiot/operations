package com.coxandkings.travel.operations.controller.bookstopenquiry;

import com.coxandkings.travel.operations.criteria.bookstopenquiry.BookstopEnquiryCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.bookstopenquiry.BookstopEnquiry;
import com.coxandkings.travel.operations.resource.bookstopenquiry.BookstopEnquiryResource;
import com.coxandkings.travel.operations.service.bookstopenquiry.BookstopEnquiryService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookstop/enquiries")
public class BookstopEnquiryController {

    @Autowired
    private BookstopEnquiryService bookstopEnquiryService;

    @PostMapping("/v1/list")
    public ResponseEntity<List<BookstopEnquiry>> getBookstopEnquiry(@RequestBody BookstopEnquiryCriteria bookstopEnquiryCriteria) throws OperationException {
        try {
            List<BookstopEnquiry> bookstopEnquiries = bookstopEnquiryService.getByCriteria(bookstopEnquiryCriteria);
            return new ResponseEntity<List<BookstopEnquiry>>(bookstopEnquiries, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10700);
        }
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<BookstopEnquiry> getBookstopEnquuiry(@PathVariable(name = "id") String bookstopEnquiryID) throws OperationException {
        try {
            BookstopEnquiry bookstopEnquiry = bookstopEnquiryService.getById(bookstopEnquiryID);
            return new ResponseEntity<>(bookstopEnquiry, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10701);
        }
    }


    @PostMapping("/v1/save")
    public ResponseEntity<BookstopEnquiry> saveBookstopEnquiry(@RequestBody BookstopEnquiryResource bookstopEnquiryResource) throws OperationException {
        try {
            BookstopEnquiry bookstopEnquiry = bookstopEnquiryService.save(bookstopEnquiryResource);
            return new ResponseEntity<>(bookstopEnquiry, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10702);
        }
    }

    @PutMapping("/v1/update")
    public ResponseEntity<BookstopEnquiry> updateBookstopEnquiry(@RequestBody BookstopEnquiryResource bookstopEnquiryResource) throws OperationException {
        try {
            BookstopEnquiry bookstopEnquiry = bookstopEnquiryService.update(bookstopEnquiryResource);
            return new ResponseEntity<>(bookstopEnquiry, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_10703);
        }
    }

    @PutMapping("/v1/closeEnquiry/{id}")
    public ResponseEntity<BookstopEnquiry> updateBookstopEnquiryStatusToClosed(@PathVariable(name = "id") String bookstopEnquiryID) throws OperationException {
        //Before closing it should actually check if a booking has been created or not.
        try {
            BookstopEnquiry bookstopEnquiry = bookstopEnquiryService.updateBookstopEnquiryStatusToClosed(bookstopEnquiryID);
            return new ResponseEntity<>(bookstopEnquiry, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10704);
        }
    }
}
