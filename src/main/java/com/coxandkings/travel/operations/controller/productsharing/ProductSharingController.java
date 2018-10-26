package com.coxandkings.travel.operations.controller.productsharing;

import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsAccommodationPaxInfo;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsRoom;
import com.coxandkings.travel.operations.model.productsharing.ProductSharing;
import com.coxandkings.travel.operations.resource.productsharing.*;
import com.coxandkings.travel.operations.service.beconsumer.acco.AccoCancellationService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.productsharing.AddPaxService;
import com.coxandkings.travel.operations.service.productsharing.ProductSharingService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("productSharing/")
public class ProductSharingController {

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private ProductSharingService productSharingService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AccoCancellationService accoCancellationService;

    @Autowired
    private AddPaxService addPaxService;



    @GetMapping(value = "/v1/{bookRefNo}/{orderId}/{id}")
    public ResponseEntity<?> getSharedInProgressProductSharing(@PathVariable("bookRefNo") String bookRefNo, @PathVariable("orderId") String orderId, @PathVariable("id") String id) throws OperationException {
        System.out.println("bookRefNo = [" + bookRefNo + "], orderId = [" + orderId + "], roomId = [" + id + "]");
        OpsBooking aOpsBooking = productSharingService.getBookingById(bookRefNo);
        OpsProduct aOpsProduct = productSharingService.getProductById(aOpsBooking, orderId);
        ProductSharingResource productSharingResource = new ProductSharingResource();
        productSharingResource.setBookingReferenceNo(bookRefNo);
        productSharingResource.setOrderID(orderId);
        productSharingResource.setRoomID(id);
        ProductSharingMainResource paxInfo = productSharingService.getPaxInfo(aOpsBooking, aOpsProduct, id, null, null);

        productSharingResource.setPaxInfo(paxInfo);
        List<ProductSharing> acceptList = productSharingService.findByStatus(ProductSharingStatus.ACCEPT.getProductSharingStatus());
        List<ProductSharing> rejectList = productSharingService.findByStatus(ProductSharingStatus.REJECT.getProductSharingStatus());
        ProductSharingBookingCriteria productSharingBookingCriteria = this.productSharingService.convert(paxInfo);
        List<ProductSharingMainResource> listOfSharedInProgress = productSharingService.getListOfSharedInProgress(productSharingBookingCriteria);
        listOfSharedInProgress.remove(paxInfo);

        if (listOfSharedInProgress.size() > 0) {
            productSharingService.sendAlertToUser(null, paxInfo.getPassengerName());
            productSharingService.createToDoTask(aOpsProduct, aOpsBooking, null, new ProductSharing());
        }
        listOfSharedInProgress = productSharingService.removeAcceptedItemsFromProgress(acceptList, listOfSharedInProgress);
        listOfSharedInProgress = productSharingService.removeRejectItemsFromProgress(rejectList, listOfSharedInProgress);

        List<ProductSharingMainResource> productSharingMainResources = removeDuplicates(listOfSharedInProgress);
        productSharingMainResources = removeSameSearchOccurance(productSharingMainResources, paxInfo);
        productSharingResource.setSharedInProgress(productSharingMainResources);

        productSharingResource.setProductCategory(OpsProductCategory.getProductCategory(aOpsProduct.getProductCategory()));
        productSharingResource.setProductSubCategory(OpsProductSubCategory.getProductSubCategory(OpsProductCategory.getProductCategory(aOpsProduct.getProductCategory()),
                aOpsProduct.getProductSubCategory()));

        List<ProductSharingMainResource> listOfAccepted = productSharingService.getListOfAccepted(acceptList);
        List<ProductSharingMainResource> listOfRejected = productSharingService.getListOfRejected(rejectList);
        productSharingResource.setSharedAccepted(removeDuplicates(listOfAccepted));
        productSharingResource.setSharedRejected(removeDuplicates(listOfRejected));
        return new ResponseEntity(productSharingResource, OK);
    }


    public List<ProductSharingMainResource> removeDuplicates(List<ProductSharingMainResource> source) {
        try {
            Set<ProductSharingMainResource> tempSet = new LinkedHashSet<>();
            tempSet.addAll(source);
            source.clear();
            source.addAll(tempSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }

    @PostMapping(value = "/v1/action")
    public ResponseEntity<?> action(@RequestBody ActionResource actionResource) throws OperationException {
        ProductSharing productSharingMainResource = null;
        ProductSharing productSharing = productSharingService.findByBookRefAndOrderNoAndSerialNo(actionResource.getBookRefIDOfFirstPassenger(), actionResource.getOrderIDOfFirstPassenger(),
                actionResource.getSerialNumberOfFirstPassenger(), actionResource.getPassengerIDOfFirstPassenger(), actionResource.getToShare().getPassengerID());
        if (productSharing == null) {
            productSharingMainResource = productSharingService.saveProductSharing(actionResource);
        } else {
            productSharing.setStatus(actionResource.getToShare().getStatus());
            productSharing.setToSerialNumber(actionResource.getToShare().getSerialNumber());
            productSharing.setBookRefNo(actionResource.getToShare().getBookRefID());
            productSharing.setOrderID(actionResource.getToShare().getOrderID());
            productSharing.setPassengerId(actionResource.getToShare().getPassengerID());
            productSharing.setFromPassengerId(actionResource.getPassengerIDOfFirstPassenger());
            if (actionResource.getComment()!=null) {
                productSharing.setSecondRef(actionResource.getComment());
            }
            productSharingMainResource = productSharingService.updateProductSharing(productSharing);
        }
        if (productSharingMainResource != null) {
            ProductSharingResponse productSharingResponse = new ProductSharingResponse();
            productSharingResponse.setMessage(productSharingService.getMessage(Constants.ER901, Locale.ENGLISH));
            return new ResponseEntity(productSharingResponse, OK);
        } else {
            throw new OperationException(Constants.ER902);
        }
    }

    @PostMapping(value = "/updateIsSharableFlag")
    public ResponseEntity<?> updateIsSharable(@RequestBody SharableRequest sharableRequest) {
        boolean status = productSharingService.updateIsSharableFlag(sharableRequest);
        if (status) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Update successful!");
            return new ResponseEntity(response, OK);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Update Failed!");
            return new ResponseEntity(response, INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "/v1/updateIsSharableFlag")
    public ResponseEntity<?> updateIsSharableVOne(@RequestBody SharableRequest sharableRequest) {
        boolean status = productSharingService.updateIsSharableFlag(sharableRequest);
        if (status) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Update successful!");
            return new ResponseEntity(response, OK);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Update Failed!");
            return new ResponseEntity(response, INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/fullCancellation/{bookRefNo}/{orderId}/{id}")
    public ResponseEntity<?> fullCancellation(@PathVariable("bookRefNo") String bookRefNo, @PathVariable("orderId") String orderId) throws OperationException {
        Object o = null;
        OpsBooking aOpsBooking = productSharingService.getBookingById(bookRefNo);
        OpsProduct aOpsProduct = productSharingService.getProductById(aOpsBooking, orderId);
        try {
            o = accoCancellationService.processCancellation(aOpsBooking, aOpsProduct);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(o, INTERNAL_SERVER_ERROR);
    }


    @GetMapping(value = "/addPaxService/{bookRefNo}/{orderId}/{roomId}")
    public ResponseEntity<?> addPaxService(@PathVariable("bookRefNo") String bookRefNo, @PathVariable("orderId") String orderId, @PathVariable("roomId") String roomId) throws OperationException {
        Object o = null;
        OpsBooking aOpsBooking = productSharingService.getBookingById(bookRefNo);
        OpsProduct aOpsProduct = productSharingService.getProductById(aOpsBooking, orderId);
        try {
            for (OpsRoom opsRoom : aOpsProduct.getOrderDetails().getHotelDetails().getRooms()) {
                if (opsRoom.getRoomID().equalsIgnoreCase(roomId)) {
                    List<OpsAccommodationPaxInfo> paxInfo = opsRoom.getPaxInfo();
                    o = addPaxService.addPaxDetails(aOpsBooking, aOpsProduct, opsRoom, paxInfo.get(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(o, INTERNAL_SERVER_ERROR);
    }

    //TODO: Just sake of testing and after testing it needs to remove
    @GetMapping(value = "/updateCancellationsCharges/{bookRefNo}")
    public ResponseEntity<?> addPaxService(@PathVariable("bookRefNo") String bookRefNo) throws OperationException {
        OpsBooking aOpsBooking = productSharingService.getBookingById(bookRefNo);
        /*OpsBooking opsBooking = opsBookingService.updateBookingForCancellationCharges(aOpsBooking);
        opsBooking = opsBookingService.updateBookingForAmendmentCharges(opsBooking);*/
        return new ResponseEntity(aOpsBooking, OK);
    }


    @GetMapping(value = "/todo/{bookRefNo}/{productId}")
    public ResponseEntity<?> todo(@PathVariable("bookRefNo") String bookRefNo, @PathVariable("productId") String productId) throws OperationException {
        OpsBooking aOpsBooking = productSharingService.getBookingById(bookRefNo);
        OpsProduct productById = productSharingService.getProductById(aOpsBooking, productId);
        ProductSharing toDoTask = this.productSharingService.createToDoTask(productById, aOpsBooking, "123456", new ProductSharing());
        return new ResponseEntity(aOpsBooking, OK);
    }


    @GetMapping(value = "/alert/{passengerName}")
    public ResponseEntity<?> alert(@PathVariable("passengerName") String passengerName) throws OperationException {
        Boolean aBoolean = productSharingService.sendAlertToUser(null, passengerName);
        return new ResponseEntity(aBoolean + "", OK);
    }

    public List<ProductSharingMainResource> removeSameSearchOccurance(List<ProductSharingMainResource> list, ProductSharingMainResource paxInfo) {
        for (int i = 0; i < list.size(); i++) {
            ProductSharingMainResource details = list.get(i);
            if (details.getOrderId().equalsIgnoreCase(paxInfo.getOrderId())
                    && details.getSerialNumber().equalsIgnoreCase(paxInfo.getSerialNumber())
                    && details.getBookingReferenceNo().equalsIgnoreCase(paxInfo.getBookingReferenceNo())) {
                list.remove(i);
            }
        }
        return list;

    }


}


