package com.coxandkings.travel.operations.service.FileProfitability.impl;

import com.coxandkings.travel.operations.enums.FileProfitability.FileProfTypes;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.FileProfitabilityBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class OperationalServiceImpl implements OperationalService {

    @Autowired
    private AirProfitabilityHandler airProfitabilityHandler;

    @Autowired
    private AccoProfitabilityHandler accoProfitabilityHandler;

    @Override
    public void computeOperationalCalculations(OpsBooking opsBookings, List<FileProfitabilityBooking> oldBookings) {

        List<OpsProduct> opsProductList = opsBookings.getProducts();
        for(OpsProduct opsProduct : opsProductList)
        {
            if (opsProduct.getProductCategory().equalsIgnoreCase(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.getCategory()))
            {
                List<FileProfitabilityBooking> profitabilityBookings = oldBookings.stream().filter(x->x.getBookingType().equals(FileProfTypes.TRANSPORTATION)&&x.getOrderId().equalsIgnoreCase(opsProduct.getOrderID())).collect(Collectors.toList());
                airProfitabilityHandler.computeOperationalProfitability(opsProduct, profitabilityBookings);

                List<FileProfitabilityBooking> profitabilityPaxBookings = oldBookings.stream().filter(x->x.getBookingType().equals(FileProfTypes.PASSENGER_WISE)&&x.getOrderId().equalsIgnoreCase(opsProduct.getOrderID())).collect(Collectors.toList());
                airProfitabilityHandler.computeOperationalProfitabilityForPax(opsProduct, profitabilityPaxBookings);
            }

            if (opsProduct.getProductCategory().equalsIgnoreCase(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION.getCategory()))
            {
                List<FileProfitabilityBooking> profitabilityBookings = oldBookings.stream().filter(x->x.getBookingType().equals(FileProfTypes.ACCOMMODATION)&&x.getOrderId().equalsIgnoreCase(opsProduct.getOrderID())).collect(Collectors.toList());
                accoProfitabilityHandler.computeOperationalProfitability(opsProduct, profitabilityBookings);

            }

        }


    }
}
