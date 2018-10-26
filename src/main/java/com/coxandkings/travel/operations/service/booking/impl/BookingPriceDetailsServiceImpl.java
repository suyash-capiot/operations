package com.coxandkings.travel.operations.service.booking.impl;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsOrderDetails;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.pricedetails.PriceDetailsResource;
import com.coxandkings.travel.operations.service.booking.BookingPriceDetailsService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.utils.TotalSellingPriceUtil;
import com.coxandkings.travel.operations.utils.TotalSupplierPriceUtil;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingPriceDetailsServiceImpl implements BookingPriceDetailsService {

    private static Logger logger = LogManager.getLogger(BookingPriceDetailsServiceImpl.class);

    @Autowired
    private TotalSupplierPriceUtil supplierPriceUtil;

    @Autowired
    private TotalSellingPriceUtil sellingPriceUtil;

    @Autowired
    private OpsBookingService bookingService;

    @Override
    public PriceDetailsResource getTotalSellingPriceDetails(String bookID, String orderID ) throws OperationException {

        PriceDetailsResource priceDetails = new PriceDetailsResource();
        try {
            OpsBooking aBooking = bookingService.getBooking(bookID);
            List<OpsProduct> bookingProducts = aBooking.getProducts();
            OpsOrderDetails anOrder = null;
            for( OpsProduct aProduct : bookingProducts )    {
                String aProductOrderID = aProduct.getOrderID();
                if( orderID.equalsIgnoreCase( aProductOrderID ) )  {
                    anOrder = aProduct.getOrderDetails();
                    OpsProductSubCategory subCategory = aProduct.getOpsProductSubCategory();
                    priceDetails = sellingPriceUtil.getSellingPriceBreakup( anOrder, subCategory );
                }
            }
        }
        catch( Exception e )    {
            e.printStackTrace();
            logger.error( "Error in loading Booking2 details for preparing price details" );
        }

        return priceDetails;
    }

    @Override
    public PriceDetailsResource getTotalSupplierPriceDetails(String bookID, String orderID) throws OperationException {
        PriceDetailsResource priceDetails = new PriceDetailsResource();
        try {
            OpsBooking aBooking = bookingService.getBooking(bookID);
            List<OpsProduct> bookingProducts = aBooking.getProducts();
            OpsOrderDetails anOrder = null;
            for( OpsProduct aProduct : bookingProducts )    {
                String aProductOrderID = aProduct.getOrderID();
                if( orderID.equalsIgnoreCase( aProductOrderID ) )  {
                    anOrder = aProduct.getOrderDetails();
                    OpsProductSubCategory subCategory = aProduct.getOpsProductSubCategory();
                    priceDetails = supplierPriceUtil.getSupplierPriceBreakup( anOrder, subCategory );
                }
            }
        }
        catch( Exception e )    {
            e.printStackTrace();
            logger.error( "Error in loading Booking2 details for preparing supplier Price details" );
        }
        return priceDetails;
    }
}
