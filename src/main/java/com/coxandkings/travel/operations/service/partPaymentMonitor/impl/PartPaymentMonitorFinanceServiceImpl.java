package com.coxandkings.travel.operations.service.partPaymentMonitor.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.partpaymentmonitor.PartPaymentBookingsResource;
import com.coxandkings.travel.operations.service.partPaymentMonitor.PartPaymentMonitorFinanceService;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartPaymentMonitorFinanceServiceImpl implements PartPaymentMonitorFinanceService {

    @Value("${partPaymentMonitor.finance.get-partpayment-bookings}")
    private String partPaymentBookingsURL;

    private static final Logger logger = LogManager.getLogger( PartPaymentMonitorFinanceServiceImpl.class );

    public PartPaymentMonitorFinanceServiceImpl()   {
    }

    @Override
    public List<String> getPartPaymentBookings() throws OperationException  {
        List<String> bookingsList   = null;
        RestTemplate restTemplate = RestUtils.getTemplate();
        HttpHeaders headers         = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PartPaymentBookingsResource> httpEntity = new HttpEntity<PartPaymentBookingsResource>( headers );

        ResponseEntity<PartPaymentBookingsResource> responseEntity = null;
        PartPaymentBookingsResource bookingsInfo = null;
        try {
            responseEntity = restTemplate.exchange( partPaymentBookingsURL, HttpMethod.GET, httpEntity, PartPaymentBookingsResource.class );
            if( responseEntity != null )    {
                bookingsInfo = responseEntity.getBody();
                bookingsList = bookingsInfo.getPartPaymentBookings();
                if( bookingsList == null )  { //return empty list to caller
                    logger.info( "No Part payment Bookings returned from Finance modules" );
                    bookingsList = new ArrayList<String>(0);
                }
            }
        }
        catch( Exception e )    {
            e.printStackTrace();
            logger.error( "Error occurred while retrieving Part Payment Bookings from Finance module", e );
        }

        return bookingsList;
    }
}
