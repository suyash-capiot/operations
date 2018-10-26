package com.coxandkings.travel.operations.service.booking;

import com.coxandkings.travel.ext.model.be.Booking;
import com.coxandkings.travel.operations.criteria.booking.becriteria.BookingSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsActionItemDetails;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsUniqueProductSubCategory;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.booking.AssignStaffResource;
import com.coxandkings.travel.operations.resource.outbound.be.BookingOverview;
import com.coxandkings.travel.operations.resource.searchviewfilter.BookingSearchResponseItem;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

//import jdk.nashorn.internal.runtime.regexp.joni.constants.OPSize;

public interface OpsBookingService {
    OpsBooking getBooking(String bookingRefId) throws OperationException;

    OpsBooking getBooking(Booking aBooking) throws OperationException;

    OpsProduct getProduct(String bookingrefid, String productId) throws OperationException;

    List<OpsUniqueProductSubCategory> getUniqueProductSubCategories(String bookingId);

    List<OpsProduct> getProductsBySubCategory(String subCategory, String bookingId);

    public List<OpsProduct> getProductsBySubCategory(String subCategory, OpsBooking opsBooking);

    List<OpsBooking> getBookingByCriteria(BookingSearchCriteria bookingSearchCriteria);

    public List<BookingSearchResponseItem> searchBookings(BookingSearchCriteria bookingSearchCriteria);

    public void getInvoiceDetails(String bookingID) throws OperationException;

    public OpsActionItemDetails getOrderActionItems(String bookingID, String orderID) throws OperationException;

    public BookingOverview getBookingOverview(OpsBooking aBooking) throws OperationException;

    public Booking getRawBooking(String bookingRefId) throws OperationException;

    MessageResource assignStaff(AssignStaffResource assignStaffResource) throws OperationException;

    //OpsBooking updateBookingForCancellationCharges(OpsBooking aBooking);

    OpsProduct getOpsProduct(OpsBooking opsBooking, String orderId) throws OperationException;

    //OpsBooking updateBookingForAmendmentCharges(OpsBooking aBooking);

    List<JSONObject> getAutoSuggestions(InputStream inputStream, String autoSuggestionType) ;

    OpsBooking getBookingByPostCall(String bookId) throws OperationException;

    OpsBooking getBookingByPostCall(String bookId, JSONObject request) throws OperationException;
}
