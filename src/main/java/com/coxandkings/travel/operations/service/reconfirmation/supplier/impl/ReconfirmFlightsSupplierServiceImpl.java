package com.coxandkings.travel.operations.service.reconfirmation.supplier.impl;

import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationBookingAttribute;
import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationConfigFor;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;
import com.coxandkings.travel.operations.repository.reconfirmation.client.ClientReconfirmationRepository;
import com.coxandkings.travel.operations.repository.reconfirmation.supplier.SupplierReconfirmationRepository;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.reconfirmation.batchjob.ReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.common.*;
import com.coxandkings.travel.operations.service.reconfirmation.mdm.ReconfirmationMDMService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.ReconfirmFlightsSupplierService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.SupplierReconfirmationService;
import com.coxandkings.travel.operations.service.template.TemplateLoaderService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.adapter.OpsBookingAdapter;
import com.coxandkings.travel.operations.utils.adapter.OpsFlightAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

/**
 *
 */
@Service("reconfirmFlightsSupplierService")
public class ReconfirmFlightsSupplierServiceImpl implements ReconfirmFlightsSupplierService {

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
    private OpsFlightAdapter opsFlightAdapter;

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
    private HashGenerator hashGenerator;

    @Value(value = "${reconfirmation.supplier.communication.subject}")
    private String supplierCommunicationSubject;


    /**
     * @param bookingDetails
     * @param flights
     * @param aSupplierReconDetails
     * @return
     * @throws OperationException
     */
    @Override
    public SupplierReconfirmationDetails reconfirmSupplierForFlights(OpsBooking bookingDetails, OpsProduct flights, SupplierReconfirmationDetails aSupplierReconDetails) throws OperationException {
        SupplierConfiguration supplierConfig = reconfirmationMDMService.getReconfirmationConfigForSupplier(null, flights.getProductCategory(), flights.getProductSubCategory(), null, null, flights.getOrderID(), null, null);

        ReconfirmationConfigFor reconfirmationConfigFor = ReconfirmationConfigFor.fromString(supplierConfig.getConfigurationFor());
        //  String productName = flights.getProductName( ) != null ? flights.getProductName( ) : "";
        //  supplierReconDetails.setProductName( productName );
        aSupplierReconDetails.setOrderID(flights.getOrderID());
        aSupplierReconDetails.setReconfirmedBy(reconfirmationConfigFor.getValue());
        aSupplierReconDetails.setBookRefNo(bookingDetails.getBookID());
        //  aSupplierReconDetails.setProductConfirmationNumber( "PCN"+System.currentTimeMillis() );
        return aSupplierReconDetails;
    }

    /**
     * @param aBookingDetails
     * @param flight
     * @param opsFlightDetails
     * @param reconfConfig
     * @param supplierReconfirmationDetailsDetails
     * @return
     * @throws OperationException
     */
    @Override
    public SupplierReconfirmationDetails flightProcess(OpsBooking aBookingDetails, OpsProduct flight, OpsFlightDetails opsFlightDetails, SupplierConfiguration reconfConfig,
                                                       SupplierReconfirmationDetails supplierReconfirmationDetailsDetails) throws OperationException {

        ReconfirmationConfigFor reconfirmationConfigFor = ReconfirmationConfigFor.fromString(reconfConfig.getConfigurationFor());
        CustomFlightDetails customFlightDetails = getCustomFlightDetails(opsFlightDetails);

        supplierReconfirmationDetailsDetails = this.forFlights(aBookingDetails, flight, customFlightDetails.getSupplierReconfirmationDate(), supplierReconfirmationDetailsDetails);
        return supplierReconfirmationDetailsDetails;


    }

    /**
     * @param flights
     * @return
     */
    @Override
    public CustomFlightDetails getCustomFlightDetails(OpsFlightDetails flights) {
        CustomFlightDetails customFlightDetails = new CustomFlightDetails();
        List<OpsOriginDestinationOption> originDestinationOptions = flights.getOriginDestinationOptions();
        for (OpsOriginDestinationOption originDestinationOption : originDestinationOptions) {
            List<OpsFlightSegment> flightSegments = originDestinationOption.getFlightSegment();
            for (OpsFlightSegment opsFlightSegment : flightSegments) {
                if (opsFlightSegment.getDepartureDateZDT() != null) {
                    customFlightDetails.setTravelDate(opsFlightSegment.getDepartureDateZDT());
                }
                break;
            }
        }
        if (flights.getClientReconfirmationDateZDT() != null) {
            customFlightDetails.setClientReconfirmationDate(flights.getClientReconfirmationDateZDT());
        }
        if (flights.getSupplierReconfirmationDateZDT() != null) {
            customFlightDetails.setSupplierReconfirmationDate(flights.getSupplierReconfirmationDateZDT());
        }

        return customFlightDetails;
    }

    /**
     * @param bookingDetails
     * @param flight
     * @param supplierReconfirmationDate
     * @param supplierReconfDetails
     * @return
     * @throws OperationException
     */
    public SupplierReconfirmationDetails forFlights(OpsBooking bookingDetails, OpsProduct flight, ZonedDateTime supplierReconfirmationDate, SupplierReconfirmationDetails supplierReconfDetails) throws OperationException {
        //check gds
        String gdsPNR = flight.getOrderDetails().getFlightDetails().getgDSPNR();
        String tripType = flight.getOrderDetails().getFlightDetails().getTripType();
        if (tripType.equalsIgnoreCase("return") && gdsPNR.equalsIgnoreCase("")) {
            throw new OperationException(Constants.ER304);
            //reconfirmation not required
        } else if (tripType.equalsIgnoreCase("return") && gdsPNR.equalsIgnoreCase("gds")) {
            //Reconfirmation required
            //continue
        }

        CustomSupplierDetails supplierDetails = this.reconfirmationMDMService.getSupplierContactDetails(flight.getSupplierID());
        if (supplierDetails == null) {
            throw new OperationException(Constants.ER303);
        }
        supplierReconfDetails.setSupplierReconfirmationDate(supplierReconfirmationDate);
        supplierReconfDetails.setSupplierName(supplierDetails.getSupplierName());
        try {
            supplierReconfDetails.setHash(this.hashGenerator.getHash());
            boolean status = reconfirmationUtilityService.composeEmailForSupplier(supplierReconfDetails, bookingDetails, flight, supplierDetails, supplierReconfDetails.getHash(), false, true);
            if (status) {
                supplierReconfDetails.setReconfirmationSentToSupplier(status);
                supplierReconfDetails.setNumberOfTimesReconfirmationSent(supplierReconfDetails.getNumberOfTimesReconfirmationSent() == null ? 1 : supplierReconfDetails.getNumberOfTimesReconfirmationSent() + 1);
                supplierReconfDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_PENDING);
                supplierReconfDetails.setSupplierReconfirmationDate(supplierReconfirmationDate);
                supplierReconfDetails.setExpiredLink(false);
                supplierReconfDetails.setNumberOfTimesReconfirmationSent(supplierReconfDetails.getNumberOfTimesReconfirmationSent() == null ? 1 : supplierReconfDetails.getNumberOfTimesReconfirmationSent() + 1);
                supplierReconfDetails = this.supplierReconfirmationRepository.saveOrUpdateSupplierReconfirmation(supplierReconfDetails);
                return supplierReconfDetails;
            } else {
                supplierReconfDetails.setReconfirmationSentToSupplier(status);
                supplierReconfDetails.setSupplierReconfirmationDate(supplierReconfirmationDate);
                // clientReconfDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_PENDING);
                supplierReconfDetails = this.supplierReconfirmationRepository.saveOrUpdateSupplierReconfirmation(supplierReconfDetails);
                return supplierReconfDetails;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException(Constants.ER306);
        }
    }

}
