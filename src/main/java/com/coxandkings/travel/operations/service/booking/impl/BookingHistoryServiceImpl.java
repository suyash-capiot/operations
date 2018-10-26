package com.coxandkings.travel.operations.service.booking.impl;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsAccommodationPaxInfo;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsRoom;
import com.coxandkings.travel.operations.resource.booking.BookingHistoryItem;
import com.coxandkings.travel.operations.service.booking.BookingHistoryAccoService;
import com.coxandkings.travel.operations.service.booking.BookingHistoryAirService;
import com.coxandkings.travel.operations.service.booking.BookingHistoryService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.utils.BookingEngineElasticData;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BookingHistoryServiceImpl implements BookingHistoryService {
    private static final Logger logger = LogManager.getLogger(BookingHistoryServiceImpl.class);

    @Value("${elastic-search.booking-engine}")
    private String bookingEngineIndexUrl;

    @Autowired
    private BookingEngineElasticData bookingEngineElasticData;

    @Autowired
    BookingHistoryAccoService bookingHistoryAccoService;

    @Autowired
    BookingHistoryAirService bookingHistoryAirService;

    private static final String lineBreak = " ";
    @Autowired
    private OpsBookingService bookingService;

    List<String> paxIDs = new ArrayList<>();

    @Override
    public List<BookingHistoryItem> getBookingHistory(String bookID) throws OperationException {
        ArrayList<BookingHistoryItem> bookingHistoryInfoList = new ArrayList<>();
        OpsBooking aBooking = bookingService.getBooking(bookID);

        // first prepare Booking created info
        BookingHistoryItem newBookInfo = new BookingHistoryItem();
        newBookInfo.setBookID(aBooking.getBookID());
        newBookInfo.setTimestamp(aBooking.getBookingDateZDT().toString());
        newBookInfo.setAction("NEW");
        newBookInfo.setStatus(aBooking.getStatus().getBookingStatus());
        StringBuilder desc = new StringBuilder();
        desc.append("Order details are:" + lineBreak);
        List<OpsProduct> bookingProducts = aBooking.getProducts();
        if (bookingProducts != null && bookingProducts.size() > 0) {
            for (OpsProduct aProduct : bookingProducts) {
                String subCategory = aProduct.getOpsProductSubCategory().getSubCategory();
                String orderID = aProduct.getOrderID();
                desc.append("Product: " + subCategory + ", ");
                desc.append("Order ID: " + orderID + ", ");
                desc.append("Status: " + aProduct.getOrderDetails().getOpsOrderStatus().getProductStatus());
                desc.append(lineBreak);
                //to know the all paxIDs
                if (OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory().equalsIgnoreCase(aProduct.getProductSubCategory())) {
                    List<OpsRoom> rooms = aProduct.getOrderDetails().getHotelDetails().getRooms();
                    for (OpsRoom opsRoom : rooms) {
                        List<OpsAccommodationPaxInfo> paxInfo = opsRoom.getPaxInfo();
                        for (OpsAccommodationPaxInfo opsAccommodationPaxInfo : paxInfo) {
                            paxIDs.add(opsAccommodationPaxInfo.getPaxID());
                        }
                    }
                }
            }
        }
        newBookInfo.setDescription(desc.toString());
        bookingHistoryInfoList.add(newBookInfo);
        try {
            List<BookingHistoryItem> accoHistory = null;
            List<BookingHistoryItem> airHistory = null;
            List<OpsProduct> products = aBooking.getProducts();
            for (OpsProduct aProduct : products) {
                if (OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory().equalsIgnoreCase(aProduct.getProductSubCategory())) {
                    accoHistory = bookingHistoryAccoService.getAccoHistory(bookID, paxIDs);
                } else if (OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory().equalsIgnoreCase(aProduct.getProductSubCategory())) {
                    airHistory = bookingHistoryAirService.getAirHistory(bookID);
                }
            }
            if (accoHistory != null)
                bookingHistoryInfoList.addAll(accoHistory);
            if (airHistory != null)
                bookingHistoryInfoList.addAll(airHistory);

            // Sort based on timestamp
            sort(bookingHistoryInfoList);
        } catch (Exception e) {
            logger.error("Error While Retreiving logs from kibana" + e);
        }
        return bookingHistoryInfoList;
    }

    private void sort(List<BookingHistoryItem> historyDetailsList) {
        Collections.sort(historyDetailsList, (o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()));
    }
}
