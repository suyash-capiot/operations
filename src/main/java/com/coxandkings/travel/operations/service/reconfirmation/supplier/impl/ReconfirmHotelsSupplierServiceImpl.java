package com.coxandkings.travel.operations.service.reconfirmation.supplier.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsHotelDetails;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsRoom;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;
import com.coxandkings.travel.operations.repository.reconfirmation.client.ClientReconfirmationRepository;
import com.coxandkings.travel.operations.repository.reconfirmation.supplier.SupplierReconfirmationRepository;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.reconfirmation.batchjob.ReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmationUtilityService;
import com.coxandkings.travel.operations.service.reconfirmation.common.SupplierConfiguration;
import com.coxandkings.travel.operations.service.reconfirmation.mdm.ReconfirmationMDMService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.ReconfirmHotelsSupplierService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.SupplierReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.SupplierReconfirmationUtilityService;
import com.coxandkings.travel.operations.service.template.TemplateLoaderService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.DateTimeUtil;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.adapter.OpsBookingAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 *
 */
@Service("reconfirmHotelsSupplierService")
public class ReconfirmHotelsSupplierServiceImpl implements ReconfirmHotelsSupplierService {

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
    private SupplierReconfirmationUtilityService supplierReconfirmationUtilityService;

    /**
     * @param bookingDetails
     * @param hotels
     * @param hotelDetails
     * @param rooms
     * @param supplierReconDetails
     * @return
     * @throws OperationException
     */
    @Override
    public SupplierReconfirmationDetails handleSupplierReconfirmationForHotels(OpsBooking bookingDetails, OpsProduct hotels, OpsHotelDetails hotelDetails, List<OpsRoom> rooms, SupplierReconfirmationDetails supplierReconDetails) throws OperationException {
        boolean isConfigure = false;

        try {
            supplierReconDetails = this.getCustomHotelDetails(rooms, hotelDetails, supplierReconDetails);
            if (isConfigure == false) {

                SupplierConfiguration supplierConfig = reconfirmationMDMService.getReconfirmationConfigForSupplier("supplier", hotels.getProductCategory(), hotels.getProductSubCategory(), null, null, hotels.getOrderID(), null, hotels.getSupplierID());
                if (supplierConfig != null) {
                    isConfigure = true;
                }
            }
            if (isConfigure == false) {
                //  String reconfirmationConfiguredForSupplierAndClient = reconfirmationMDMService.isReconfirmationConfiguredForSupplier( "supplier&client" , hotels.getProductCategory( ) , hotels.getProductSubCategory( ) , hotels.getOrderID( ) , null , null );
                SupplierConfiguration supplierConfig = reconfirmationMDMService.getReconfirmationConfigForSupplier("supplier&client", hotels.getProductCategory(), hotels.getProductSubCategory(), null, null, hotels.getOrderID(), null, hotels.getSupplierID());

                if (supplierConfig != null) {
                    isConfigure = true;
                }
            }
        } catch (OperationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isConfigure) {
            supplierReconDetails = this.supplierReconfirmationUtilityService.emailProcessForSupplier(bookingDetails, hotels, supplierReconDetails, supplierReconDetails.getSupplierReconfirmationDate());

        } else {
            throw new OperationException(Constants.ER333);
        }
        return supplierReconDetails;
    }

    /**
     * @param stringDate
     * @return
     */
    public ZonedDateTime getDate(String stringDate) {
        final String dateTime = stringDate + "T00:00:00.00Z";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        final ZonedDateTime parsed = ZonedDateTime.parse(dateTime, formatter.withZone(ZoneId.of("UTC")));
        System.out.println(parsed.toLocalDateTime());
        return parsed;
    }

    /**
     * @param rooms
     * @param hotelDetails
     * @param supplierReconfirmationDetails
     * @return
     * @throws OperationException
     */
    @Override
    public SupplierReconfirmationDetails getCustomHotelDetails(List<OpsRoom> rooms, OpsHotelDetails hotelDetails, SupplierReconfirmationDetails supplierReconfirmationDetails) throws OperationException {
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
                ZonedDateTime travelDate = getDate(rooms.get(0).getCheckIn());
                supplierReconfirmationDetails.setTravelDate(travelDate);
            }
            if (hotelDetails.getClientReconfirmationDateZDT() != null) {
                supplierReconfirmationDetails.setClientReconfirmationDate(hotelDetails.getClientReconfirmationDateZDT());
            }
            if (hotelDetails.getSupplierReconfirmationDateZDT() != null) {
                supplierReconfirmationDetails.setSupplierReconfirmationDate(hotelDetails.getSupplierReconfirmationDateZDT());
            }
        } catch (Exception e) {
            supplierReconfirmationDetails.setTravelDate(ZonedDateTime.now());
            e.printStackTrace();
            //TODO
            //  throw new OperationException( Constants.ER305 );
        }
        return supplierReconfirmationDetails;
    }

    /**
     * @param bookingDetails
     * @param hotels
     * @param supplierReconfirmation
     * @param clientReconfirmationDetails
     * @return
     * @throws OperationException
     */
    @Override
    public SupplierReconfirmationDetails reconfirmSupplierForHotels(OpsBooking bookingDetails, OpsProduct hotels,
                                                                    SupplierReconfirmationDetails supplierReconfirmation,
                                                                    ClientReconfirmationDetails clientReconfirmationDetails) throws OperationException {
        if (hotels == null) {
            throw new OperationException(Constants.ER323);
        }
        OpsHotelDetails hotelDetails = hotels.getOrderDetails().getHotelDetails();
        List<OpsRoom> rooms = hotelDetails.getRooms();
        //  String productName = hotels.getProductName( ) != null ? hotels.getProductName( ) : "";
        if (supplierReconfirmation != null) {
            //   supplierReconfirmation.setProductName( productName );
            supplierReconfirmation.setCity(hotelDetails.getCityName() != null ? hotelDetails.getCityName() : "");
            supplierReconfirmation.setCountry(hotelDetails.getCountryName() != null ? hotelDetails.getCountryName() : "");
            supplierReconfirmation.setOrderID(hotels.getOrderID());
            supplierReconfirmation.setBookRefNo(bookingDetails.getBookID());
        }
        return supplierReconfirmation;
    }

}
