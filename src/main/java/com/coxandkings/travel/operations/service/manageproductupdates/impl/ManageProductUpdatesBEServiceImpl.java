package com.coxandkings.travel.operations.service.manageproductupdates.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.manageproductupdates.CheaperPriceUpdatesFlightInfo;
import com.coxandkings.travel.operations.model.manageproductupdates.CheaperUpdatesHotelInfo;
import com.coxandkings.travel.operations.service.manageproductupdates.ManageProductUpdatesBEService;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManageProductUpdatesBEServiceImpl implements ManageProductUpdatesBEService {

    @Value(value = "${booking_engine.base_url}")
    private String bookingEngineBaseURL;

    @Value(value = "${updatesToProductMaster.flight-url}")
    private String cheaperPriceFlightsURL;

    @Value(value = "${updatesToProductMaster.hotel-url}")
    private String cheaperPriceHotelsURL;

    /**
     * Get Bookings from Booking2 Engine:
     * Exclude Criteria: Exclude Flights done using Online supplier, Exclude Flights which are in Ticketed status
     * Include Criteria: Include all Non Air Products made with Online Suppliers, Include all Flight Bookings made with Offline Suppliers
     *
     * @return
     */
    @Override
    public List<CheaperPriceUpdatesFlightInfo> getFlightBookingsForProductUpdates() throws OperationException {
        List<CheaperPriceUpdatesFlightInfo> flightsInfo = null;
        try {
            ObjectMapper objMapper = new ObjectMapper();
            objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            String cheaperPriceBookingDetails = RestUtils.getForObject(cheaperPriceFlightsURL, String.class);
            flightsInfo = objMapper.readValue(cheaperPriceBookingDetails, new TypeReference<List<CheaperPriceUpdatesFlightInfo>>() {
            });

            // Proceed to check if ToDo is already created or not
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flightsInfo;
    }

    @Override
    public List<CheaperUpdatesHotelInfo> getHotelBookingsForProductUpdates() throws OperationException {
        List<CheaperUpdatesHotelInfo> hotelsInfo = null;
        try {
            ObjectMapper objMapper = new ObjectMapper();
            objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            String cheaperPriceBookingDetails = RestUtils.getForObject(cheaperPriceHotelsURL, String.class);
            hotelsInfo = objMapper.readValue(cheaperPriceBookingDetails, new TypeReference<List<CheaperUpdatesHotelInfo>>() {
            });

            // Proceed to check if ToDo is already created or not
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotelsInfo;
    }


}
