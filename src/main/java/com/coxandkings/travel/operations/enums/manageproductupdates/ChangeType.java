package com.coxandkings.travel.operations.enums.manageproductupdates;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.manageproductupdates.ProductUpdateFlightResource;
import org.springframework.util.StringUtils;

public enum ChangeType {

    FLIGHT_NUMBER_AND_TIME() {
        @Override
        public void process(ProductUpdateFlightResource productUpdateFlightResource) throws OperationException {
            if(StringUtils.isEmpty(productUpdateFlightResource.getFlightNumber()))
            {
                throw new OperationException("Flight Number must not be null");
            }
            if(StringUtils.isEmpty(productUpdateFlightResource.getDepartureDateAndTime()))
            {
                throw new OperationException("Departure Date and Time must not be null");
            }
            if(StringUtils.isEmpty(productUpdateFlightResource.getArrivalDateAndTime()))
            {
                throw new OperationException("Arrival Date and Time must not be null");
            }
        }
    },
    FLIGHT_TIME {
        @Override
        public void process(ProductUpdateFlightResource productUpdateFlightResource) throws OperationException {
            if(StringUtils.isEmpty(productUpdateFlightResource.getDepartureDateAndTime()))
            {
                throw new OperationException("Departure Date and Time must not be null");
            }
            if(StringUtils.isEmpty(productUpdateFlightResource.getArrivalDateAndTime()))
            {
                throw new OperationException("Arrival Date and Time must not be null");
            }
        }
    },
    FLIGHT_NUMBER() {
        @Override
        public void process(ProductUpdateFlightResource productUpdateFlightResource) throws OperationException {
            if(StringUtils.isEmpty(productUpdateFlightResource.getFlightNumber()))
            {
                throw new OperationException("Flight Number must not be null");
            }
        }
    };
    public abstract void process(ProductUpdateFlightResource productUpdateFlightResource) throws OperationException;
}
