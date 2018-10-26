package com.coxandkings.travel.operations.service.productsharing;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.productsharing.ProductSharing;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.resource.productsharing.*;

import java.util.List;
import java.util.Locale;

/**
 *
 */
public interface ProductSharingService {
    /**
     * @param actionResource
     * @return
     * @throws OperationException
     */
    ProductSharing saveProductSharing(ActionResource actionResource) throws OperationException;

    /**
     * @param productSharing
     * @return
     * @throws OperationException
     */
    ProductSharing updateProductSharing(ProductSharing productSharing) throws OperationException;

    /**
     * @param opsBooking
     * @param opsProduct
     * @param roomId
     * @param serialNo
     * @param passengerId
     * @return
     * @throws OperationException
     */
    ProductSharingMainResource getPaxInfo(OpsBooking opsBooking, OpsProduct opsProduct, String roomId, String serialNo, String passengerId) throws OperationException;

    /**
     * @param bookingId
     * @return
     * @throws OperationException
     */
    OpsBooking getBookingById(String bookingId) throws OperationException;

    /**
     * @param bookingDetails
     * @param orderID
     * @return
     * @throws OperationException
     */
    OpsProduct getProductById(OpsBooking bookingDetails, final String orderID) throws OperationException;

    /**
     * @param bookRefNo
     * @param orderID
     * @param serialNumber
     * @param fromPassengerId
     * @param toSerialNumber
     * @return
     */
    ProductSharing findByBookRefAndOrderNoAndSerialNo(String bookRefNo, String orderID, String serialNumber, String fromPassengerId, String toSerialNumber);

    /**
     * @param status
     * @return
     */
    List<ProductSharing> findByStatus(String status);

    /**
     * @param status
     * @param fromSerialNumber
     * @return
     */
    List<ProductSharing> findByStatusAndFromSerialNumber(String status, String fromSerialNumber);

    /**
     * @param rejectedList
     * @param progressList
     * @return
     */
    List<ProductSharingMainResource> removeRejectItemsFromProgress(List<ProductSharing> rejectedList, List<ProductSharingMainResource> progressList);

    /**
     * @param rejectedList
     * @return
     */
    List<ProductSharingMainResource> getListOfRejected(List<ProductSharing> rejectedList);

    /**
     * @param acceptedList
     * @param progressList
     * @return
     */
    List<ProductSharingMainResource> removeAcceptedItemsFromProgress(List<ProductSharing> acceptedList, List<ProductSharingMainResource> progressList);

    /**
     * @param acceptedList
     * @return
     */
    List<ProductSharingMainResource> getListOfAccepted(List<ProductSharing> acceptedList);

    /**
     * @param sharingStatus
     * @param hash
     * @return
     * @throws OperationException
     */
    String checkClientResponse(ProductSharingStatus sharingStatus, String hash) throws OperationException;

    /**
     * @param message
     * @param color
     * @return
     */
    String convertToHtml(String message, String color);

    /**
     * @param msgCode
     * @param locale
     * @param args
     * @return
     */
    String getMessage(String msgCode, Locale locale, String... args);

    /**
     * @param productSharingBookingCriteria
     * @return
     * @throws OperationException
     */
    List<SharedBooking> getSharedBookings(ProductSharingBookingCriteria productSharingBookingCriteria) throws OperationException;

    /**
     * @param productSharingBookingCriteria
     * @return
     * @throws OperationException
     */
    List<ProductSharingMainResource> getListOfSharedInProgress(ProductSharingBookingCriteria productSharingBookingCriteria) throws OperationException;

    /**
     * @param productSharingMainResource
     * @return
     */
    ProductSharingBookingCriteria convert(ProductSharingMainResource productSharingMainResource);

    /**
     * @param toDoTask
     * @param passengerName
     * @return
     */
    Boolean sendAlertToUser(ToDoTask toDoTask, String passengerName);

    /**
     * @param opsProduct
     * @param opsBooking
     * @param referenceID
     * @param productSharing
     * @return
     */
    ProductSharing createToDoTask(OpsProduct opsProduct, OpsBooking opsBooking, String referenceID, ProductSharing productSharing);

    /**
     * @param sharableRequest
     * @return
     */
    boolean updateIsSharableFlag(SharableRequest sharableRequest);
}
