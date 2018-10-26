package com.coxandkings.travel.operations.service.reconfirmation.client.impl;

import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationBookingAttribute;
import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationConfigFor;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import com.coxandkings.travel.operations.repository.reconfirmation.client.ClientReconfirmationRepository;
import com.coxandkings.travel.operations.repository.reconfirmation.supplier.SupplierReconfirmationRepository;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.reconfirmation.batchjob.ReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ReconfirmFlightsClientService;
import com.coxandkings.travel.operations.service.reconfirmation.common.*;
import com.coxandkings.travel.operations.service.reconfirmation.mdm.ReconfirmationMDMService;
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
import java.util.Collections;
import java.util.List;

@Service("reconfirmFlightsClientService")
public class ReconfirmFlightsClientServiceImpl implements ReconfirmFlightsClientService {

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


    @Value(value = "${reconfirmation.client.communication.subject}")
    private String clientCommunicationSubject;

    /**
     *
     * @param bookingDetails
     * @param flights
     * @param clientReconDetails
     * @return
     * @throws OperationException
     */
    @Override
    public ClientReconfirmationDetails reconfirmClientForFlights(OpsBooking bookingDetails, OpsProduct flights, ClientReconfirmationDetails clientReconDetails) throws OperationException {

        OpsFlightDetails flightDetails = flights.getOrderDetails().getFlightDetails();
        // String productName = flights.getProductName( ) != null ? flights.getProductName( ) : "";
        // clientReconDetails.setProductName( productName );
        clientReconDetails.setCity("");
        clientReconDetails.setCountry("");
        clientReconDetails.setOrderID(flights.getOrderID());
        clientReconDetails.setBookRefNo(bookingDetails.getBookID());
        return clientReconDetails;
    }

    /**
     *
     * @param bookingDetails
     * @param flight
     * @param opsFlightDetails
     * @param reconfConfig
     * @param clientReconfirmationDetails
     * @return
     * @throws OperationException
     */
    @Override
    public ClientReconfirmationDetails flightProcess(OpsBooking bookingDetails, OpsProduct flight, OpsFlightDetails opsFlightDetails, ClientConfiguration reconfConfig, ClientReconfirmationDetails clientReconfirmationDetails) throws OperationException {

        ReconfirmationConfigFor reconfirmationConfigFor = ReconfirmationConfigFor.fromString(reconfConfig.getConfigurationFor());
        CustomFlightDetails customFlightDetails = getCustomFlightDetails(opsFlightDetails);
        switch (reconfirmationConfigFor) {
            case CLIENT: {
                clientReconfirmationDetails = this.forFlights(bookingDetails, flight, customFlightDetails.getSupplierReconfirmationDate(), clientReconfirmationDetails);
                return clientReconfirmationDetails;
            }
        }
        return clientReconfirmationDetails;
    }

    /**
     *
     * @param flights
     * @return
     */
    @Override
    public CustomFlightDetails getCustomFlightDetails(OpsFlightDetails flights) {
        CustomFlightDetails customFlightDetails = new CustomFlightDetails();
        List<OpsOriginDestinationOption> originDestinationOptions = flights.getOriginDestinationOptions();
        for (OpsOriginDestinationOption originDestinationOption : originDestinationOptions) {
            List<OpsFlightSegment> flightSegments = originDestinationOption.getFlightSegment();
            Collections.sort(flightSegments, (flightSegment1, flightSegment2) -> flightSegment1.getDepartureDateZDT().compareTo(flightSegment2.getDepartureDateZDT()));
            customFlightDetails.setClientReconfirmationDate(flightSegments.get(0).getDepartureDateZDT());
            break;
        }
        return customFlightDetails;
    }

    /**
     *
     * @param bookingDetails
     * @param flight
     * @param clientReconfirmationDate
     * @param clientReconfDetails
     * @return
     * @throws OperationException
     */
    public ClientReconfirmationDetails forFlights(OpsBooking bookingDetails, OpsProduct flight, ZonedDateTime clientReconfirmationDate, ClientReconfirmationDetails clientReconfDetails) throws OperationException {
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

        CustomClientDetails clientContactDetails = reconfirmationMDMService.getB2BClientContactDetails(bookingDetails.getClientID(), bookingDetails.getClientType());
        if (clientContactDetails == null) {
            throw new OperationException(Constants.ER303);
        }
        clientReconfDetails.setClientOrCustomerReconfirmationDate(clientReconfirmationDate);
        try {
            clientReconfDetails.setHash(hashGenerator.getHash());
            boolean status = reconfirmationUtilityService.composeEmailForClient(clientReconfDetails, bookingDetails, flight, clientContactDetails, clientReconfDetails.getHash(), true, false);

            if (status) {
                clientReconfDetails.setExpiredLink(false);
                clientReconfDetails.setReconfirmationSentToClient(status);
                clientReconfDetails.setNumberOfTimesReconfirmationSent(clientReconfDetails.getNumberOfTimesReconfirmationSent() == null ? 1 : clientReconfDetails.getNumberOfTimesReconfirmationSent() + 1);
                clientReconfDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_PENDING);
                return clientReconfDetails;
            } else {
                clientReconfDetails.setReconfirmationSentToClient(status);
                // clientReconfDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_PENDING);
                return clientReconfDetails;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException(Constants.ER306);
        }
    }

}
