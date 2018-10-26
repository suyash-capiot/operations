package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsOrderClientCommercial;
import com.coxandkings.travel.operations.model.core.OpsOrderSupplierCommercial;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.amendentitycommercial.MarginDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Component
public class MarginCalculatorUtil {

    @Value(value = "${ROE.booking-date}")
    private String getROEUrl;


    public MarginDetails calculateMargin(OpsBooking opsBooking, OpsProduct opsProduct) throws OperationException {
    	
    	
        MarginDetails marginDetails = new MarginDetails();
        BigDecimal netMargin = null;
        BigDecimal netSupplierCost = null;
        BigDecimal netSellingPrice = null;
        BigDecimal supplierCost = null;
        BigDecimal supplierCommercialPayables = new BigDecimal(0);
        BigDecimal supplierCommercialsReveivables = new BigDecimal(0);
        BigDecimal clientCommercialPayables = new BigDecimal(0);
        BigDecimal clientCommercialsReveivables = new BigDecimal(0);
        String supplierCurrency = "";
        String clientCurrency = "";
        BigDecimal roe = opsProduct.getRoe();
        String bookingDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(opsBooking.getBookingDateZDT());
        OpsProductCategory productCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
        OpsProductSubCategory productSubCategory = OpsProductSubCategory.getProductSubCategory(productCategory, opsProduct.getProductSubCategory());
        switch (productSubCategory) {
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                supplierCost = new BigDecimal(opsProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getSupplierPrice());
                supplierCurrency = opsProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getCurrencyCode();
                clientCurrency = opsProduct.getOrderDetails().getFlightDetails().getTotalPriceInfo().getCurrencyCode();
                // clientCost=  new BigDecimal(opsProduct.getOrderDetails().getFlightDetails().getTotalPriceInfo().getTotalPrice());
                break;
            case PRODUCT_SUB_CATEGORY_HOTELS:
                supplierCost = new BigDecimal(opsProduct.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getSupplierPrice());
                supplierCurrency = opsProduct.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getCurrencyCode();
                clientCurrency = opsProduct.getOrderDetails().getHotelDetails().getOpsAccommodationTotalPriceInfo().getCurrencyCode();
                // clientCost=new BigDecimal(opsProduct.getOrderDetails().getHotelDetails().getOpsAccommodationTotalPriceInfo().getTotalPrice());
                break;
            case PRODUCT_SUB_CATEGORY_HOLIDAYS:
                supplierCost = new BigDecimal(opsProduct.getOrderDetails().getPackageDetails().getOrderSupplierPriceInfo().getAmountAfterTax());
                supplierCurrency = opsProduct.getOrderDetails().getPackageDetails().getOrderSupplierPriceInfo().getCurrencyCode();
                clientCurrency = opsProduct.getOrderDetails().getPackageDetails().getOrderClientTotalPriceInfo().getCurrencyCode();
                // clientCost=new BigDecimal(opsProduct.getOrderDetails().getHotelDetails().getOpsAccommodationTotalPriceInfo().getTotalPrice());
                break;
            default:
                break;
        }


        supplierCost = supplierCost.multiply(roe);
        
        
        for (OpsOrderSupplierCommercial supplierCommercial : opsProduct.getOrderDetails().getSupplierCommercials()) {
        	if(supplierCommercial.isEligible()) {
            BigDecimal convertedValue = null;
            if (!supplierCommercial.getCommercialCurrency().equalsIgnoreCase(clientCurrency)) {
                //TODO get ROE of Commercial Currency and Client Currency from BE API
                BigDecimal commercialToClientROE = null;
                try {
                    UriComponents getROE = UriComponentsBuilder.fromUriString(getROEUrl).pathSegment(supplierCommercial.getCommercialCurrency()).
                            pathSegment(clientCurrency).pathSegment(opsBooking.getClientMarket()).pathSegment(bookingDate).build();
                    commercialToClientROE = RestUtils.getForObject(getROE.toUriString(), BigDecimal.class);
                } catch (Exception e) {
                    throw new OperationException("Cannot Get ROE for Commercial");
                }
                convertedValue = new BigDecimal(supplierCommercial.getCommercialAmount()).multiply(commercialToClientROE);
            } else {
                convertedValue = new BigDecimal(supplierCommercial.getCommercialAmount());
            }
            if (supplierCommercial.getCommercialType().equals("Receivable")) {
                supplierCommercialsReveivables = supplierCommercialsReveivables.add(convertedValue);
            } else if (supplierCommercial.getCommercialType().equals("Payable")) {

                supplierCommercialPayables = supplierCommercialPayables.add(convertedValue);
            }
        	}

        }

        for (OpsOrderClientCommercial clientCommercial : opsProduct.getOrderDetails().getClientCommercials().stream().filter(commercial -> commercial.getCompanyFlag() == true).collect(Collectors.toList())) {
            if(clientCommercial.isEligible()) {
        	BigDecimal convertedValue = null;
            if (!clientCommercial.getCommercialCurrency().equalsIgnoreCase(clientCurrency)) {
                BigDecimal commercialToClientROE = null;
                try {
                    UriComponents getROE = UriComponentsBuilder.fromUriString(getROEUrl).pathSegment(clientCommercial.getCommercialCurrency()).
                            pathSegment(clientCurrency).pathSegment(opsBooking.getClientMarket()).pathSegment(bookingDate).build();
                    commercialToClientROE = RestUtils.getForObject(getROE.toUriString(), BigDecimal.class);

                } catch (Exception e) {
                    throw new OperationException("Cannot Get ROE for Commercial");
                }
                convertedValue = new BigDecimal(clientCommercial.getCommercialAmount()).multiply(commercialToClientROE);
            } else {
                convertedValue = new BigDecimal(clientCommercial.getCommercialAmount());
            }
            if (clientCommercial.getCommercialType().equals("Receivable"))
                clientCommercialsReveivables = clientCommercialsReveivables.add(convertedValue);
            else if (clientCommercial.getCommercialType().equals("Payable"))
                clientCommercialPayables = clientCommercialPayables.add(convertedValue);
            }
            
        }


        netSupplierCost = supplierCost.add(supplierCommercialPayables).subtract(supplierCommercialsReveivables);
        netSellingPrice = supplierCost.add(clientCommercialsReveivables).subtract(clientCommercialPayables);
        netMargin = netSellingPrice.subtract(netSupplierCost);
        marginDetails.setCurrencyCode(clientCurrency);
        marginDetails.setNetMargin(netMargin);
        marginDetails.setNetSellingPrice(netSellingPrice);
        marginDetails.setNetSupplierCost(netSupplierCost);
        marginDetails.setClientCommercialPayables(clientCommercialPayables);
        marginDetails.setClientCommercialsReveivables(clientCommercialsReveivables);
        marginDetails.setSupplierCommercialPayables(supplierCommercialPayables);
        marginDetails.setSupplierCommercialsReveivables(supplierCommercialsReveivables);
        return marginDetails;

    }
}
