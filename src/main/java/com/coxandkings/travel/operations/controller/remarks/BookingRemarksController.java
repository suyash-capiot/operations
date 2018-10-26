package com.coxandkings.travel.operations.controller.remarks;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.remarks.BookingRemarks;
import com.coxandkings.travel.operations.resource.remarks.RoleBasicInfo;
import com.coxandkings.travel.operations.resource.remarks.UserBasicInfo;
import com.coxandkings.travel.operations.service.remarks.BookingRemarksService;
import com.coxandkings.travel.operations.service.remarks.MDMUserService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/remarks")
public class BookingRemarksController {

    @Autowired
    private MDMUserService mdmUserService;

    @Autowired
    private BookingRemarksService bookingRemarksService;

    @PostMapping("/v1/addBookingRemarks")
    public ResponseEntity<BookingRemarks> saveBookingRemark(@RequestBody BookingRemarks bookingRemarks) throws OperationException {
        try {
            return new ResponseEntity<BookingRemarks>(bookingRemarksService.saveBookingRemarks(bookingRemarks), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30401);
        }
    }

    @PutMapping("/v1/updateBookingRemarks")
    public ResponseEntity<BookingRemarks> update(@RequestBody BookingRemarks bookingRemarks) throws OperationException {
        try {
            return new ResponseEntity<BookingRemarks>(bookingRemarksService.updateBookingRemarks(bookingRemarks), HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_30402);
        }
    }

    @GetMapping("/v1/getAllBookingRemarks")
    public ResponseEntity<List<BookingRemarks>> getAllBookingRemarks() throws OperationException {
        try {
            return new ResponseEntity<List<BookingRemarks>>(bookingRemarksService.getAllBookingRemarks(), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30403);
        }
    }

    @GetMapping("/v1/getBookingRemarksById")
    public ResponseEntity<BookingRemarks> getBookingRemarksById(@RequestParam String remarkId) throws OperationException {
        try {
            return new ResponseEntity<BookingRemarks>(bookingRemarksService.getBookingRemarksById(remarkId), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30404);
        }
    }

    @DeleteMapping("/v1/deleteById")
    public void deleteById(@RequestParam String remarkId) throws OperationException {
        try {
            bookingRemarksService.deleteByRemarkId(remarkId);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30405);
        }
    }

    @GetMapping("/v1/getAllUsers")
    public ResponseEntity<List<UserBasicInfo>> getAllUsers() throws OperationException {
        try {
            return new ResponseEntity<List<UserBasicInfo>>(mdmUserService.getAllUsers(), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30406);
        }
    }

    @GetMapping("/v1/getOpsUserUsingRole")
    public ResponseEntity<List<UserBasicInfo>> getOpsUserUsingRole(@RequestParam("roleName") String roleName)
            throws OperationException {
        try {
            return new ResponseEntity<List<UserBasicInfo>>(mdmUserService.getOpsUsersUsingRole(roleName), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30407);
        }
    }

    @GetMapping("/v1/searchRoles")
    public ResponseEntity<List<RoleBasicInfo>> searchRole(@RequestParam("roleName") StringBuilder roleName) throws OperationException {
        try {
            return new ResponseEntity<List<RoleBasicInfo>>(mdmUserService.searchRoles(roleName), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30408);
        }
    }

    @GetMapping("/v1/getRemarksByBookId/{bookId}")
    public ResponseEntity<List<BookingRemarks>> getRemarksByBookId(@PathVariable String bookId)throws OperationException{
        try {
            return new ResponseEntity<>(bookingRemarksService.getRemarksByBookId(bookId),HttpStatus.OK);
        }catch (OperationException  oe){
            throw oe;
        }catch (Exception e){
            throw new OperationException(Constants.OPS_ERR_30408);
        }
    }
}
