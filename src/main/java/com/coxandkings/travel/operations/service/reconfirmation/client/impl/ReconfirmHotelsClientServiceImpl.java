package com.coxandkings.travel.operations.service.reconfirmation.client.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsHotelDetails;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsRoom;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import com.coxandkings.travel.operations.repository.reconfirmation.client.ClientReconfirmationRepository;
import com.coxandkings.travel.operations.repository.reconfirmation.supplier.SupplierReconfirmationRepository;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.reconfirmation.batchjob.ReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ClientReconfirmationUtilityService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ReconfirmHotelsClientService;
import com.coxandkings.travel.operations.service.reconfirmation.common.CustomHotelDetails;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmationUtilityService;
import com.coxandkings.travel.operations.service.reconfirmation.mdm.ReconfirmationMDMService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.SupplierReconfirmationService;
import com.coxandkings.travel.operations.service.template.TemplateLoaderService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.utils.DateTimeUtil;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.adapter.OpsBookingAdapter;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

/**
 *
 */

@Service("reconfirmHotelsClientService")
public class ReconfirmHotelsClientServiceImpl implements ReconfirmHotelsClientService {

    @Autowired
    private ReconfirmationUtilityService reconfirmationUtilityService;

    @Autowired
    private ClientReconfirmationRepository clientReconfirmationRepository;

    @Autowired
    private SupplierReconfirmationRepository supplierReconfirmationRepository;

    @Autowired
    private SupplierReconfirmationService supplierReconfirmationService;

    @Autowired
    private TemplateLoaderService templateLoaderService;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private OpsBookingAdapter opsBookingAdapter;

    @Autowired
    private ReconfirmationService reconfirmationService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private ReconfirmationMDMService reconfirmationMDMService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private ClientReconfirmationUtilityService clientReconfirmationUtilityService;

    private static final Logger logger = LogManager.getLogger(ReconfirmHotelsClientServiceImpl.class);

    /**
     * @param bookingDetails
     * @param hotels
     * @param hotelDetails
     * @param rooms
     * @param clientReconDetails
     * @return
     * @throws OperationException
     */
    @Override
    public ClientReconfirmationDetails handleClientReconfirmationForHotels(OpsBooking bookingDetails, OpsProduct hotels, OpsHotelDetails hotelDetails, List<OpsRoom> rooms, ClientReconfirmationDetails clientReconDetails) throws OperationException {
        try {
            // CustomClientDetails b2BClientContactDetails = reconfirmationMDMService.getB2BClientContactDetails(bookingDetails.getClientID(), bookingDetails.getClientType());
            clientReconDetails = this.clientReconfirmationUtilityService.emailProcessForClient(bookingDetails, hotels, clientReconDetails, clientReconDetails.getClientOrCustomerReconfirmationDate());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ReconfirmHotelsClientServiceImpl.handleClientReconfirmationForHotels".concat(" Exception is : ").concat(e.toString()));
        }
        return clientReconDetails;
    }

    /**
     * @param rooms
     * @param hotelDetails
     * @param clientReconDetails
     * @return
     * @throws OperationException
     */
    @Override
    public ClientReconfirmationDetails getCustomHotelDetails(List<OpsRoom> rooms, OpsHotelDetails hotelDetails, ClientReconfirmationDetails clientReconDetails) throws OperationException {
        CustomHotelDetails customHotelDetails = new CustomHotelDetails();
        try {
            Collections.sort(rooms, (r1, r2) -> {
                try {
                    return DateTimeUtil.formatBEDateTime(r1.getCheckIn()).compareTo(DateTimeUtil.formatBEDateTime(r2.getCheckIn()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            });
            if (rooms.get(0) != null && rooms.get(0).getCheckIn() != null) {
                ZonedDateTime travelDate = DateTimeUtil.formatBEDateTimeZone(rooms.get(0).getCheckIn());
                clientReconDetails.setTravelDate(travelDate);
            }
            if (hotelDetails.getClientReconfirmationDateZDT() != null) {
                clientReconDetails.setClientOrCustomerReconfirmationDate(hotelDetails.getClientReconfirmationDateZDT());
            }
        } catch (Exception e) {
            clientReconDetails.setTravelDate(ZonedDateTime.now());
            e.printStackTrace();
            logger.error("ReconfirmHotelsClientServiceImpl.getCustomHotelDetails".concat(" Exception is : ").concat(e.toString()));
            //  throw new OperationException( Constants.ER305 );
        }
        return clientReconDetails;
    }

    /**
     * @param bookingDetails
     * @param hotels
     * @param hotelDetails
     * @param clientReconfirmationDetails
     * @return
     * @throws OperationException
     */
    @Override
    public ClientReconfirmationDetails reconfirmClientForHotels(OpsBooking bookingDetails, OpsProduct hotels, OpsHotelDetails hotelDetails, ClientReconfirmationDetails clientReconfirmationDetails) throws OperationException {
        logger.info("ReconfirmHotelsClientServiceImpl.reconfirmClientForHotels");
        String productName = hotelDetails.getHotelName() != null ? hotelDetails.getHotelName() : "";
        clientReconfirmationDetails.setProductName(productName);
        clientReconfirmationDetails.setCity(hotelDetails.getCityName() != null ? hotelDetails.getCityName() : "");
        clientReconfirmationDetails.setCountry(hotelDetails.getCountryName() != null ? hotelDetails.getCountryName() : "");
        clientReconfirmationDetails.setOrderID(hotels.getOrderID());
        clientReconfirmationDetails.setBookRefNo(bookingDetails.getBookID());
        return clientReconfirmationDetails;
    }

}
