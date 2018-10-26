package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.ext.model.be.SpecialServiceRequest;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.pricedetails.PriceComponentInfo;
import com.coxandkings.travel.operations.resource.pricedetails.PriceDetailsResource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class TotalSupplierPriceUtil {

    private String TOTAL_PRICE = "Total";

    public TotalSupplierPriceUtil() {
    }

    private void computeFares( OpsPaxTypeFareFlightSupplier aPaxTypeFare, LinkedHashMap<String,String> outputMap, int noOfPax ) {
        // Get base fare
        Double aBaseFare = aPaxTypeFare.getBaseFare().getAmount();
        Double totalBaseFare = aBaseFare * noOfPax;
        if( outputMap.containsKey( "Base Fare" ))   {
            Double existingBaseFare = new Double( outputMap.get( "Base Fare" ) );
            totalBaseFare = totalBaseFare + existingBaseFare;
        }
        outputMap.put("Base Fare", totalBaseFare.toString());

        // Get fee
        OpsFees paxFees = aPaxTypeFare.getFees();
        if (paxFees != null && paxFees.getFee() != null) {
            Double aFee = 0.0;
            for (OpsFee aPaxFee : paxFees.getFee()) {
                aFee = aFee + aPaxFee.getAmount();

                String feeCode = aPaxFee.getFeeCode();
                Double feeAmount = new Double( aPaxFee.getAmount() );
                feeAmount = feeAmount * noOfPax;

                if( outputMap.containsKey( feeCode ))   {
                    Double existingFeeAmount = new Double( outputMap.get( feeCode ));
                    feeAmount = feeAmount + existingFeeAmount;
                }
                outputMap.put( feeCode, feeAmount.toString() );
            }
        }

        // Get tax
        OpsTaxes taxesInfo = aPaxTypeFare.getTaxes();
        if (taxesInfo != null && taxesInfo.getTax() != null) {
            Double taxValue = 0.0;
            for (OpsTax aOpsTax : taxesInfo.getTax()) {
                taxValue = taxValue + aOpsTax.getAmount();
                String taxCode = aOpsTax.getTaxCode();
                Double taxAmount = aOpsTax.getAmount();
                taxAmount = taxAmount * noOfPax;

                if( outputMap.containsKey( taxCode ))   {
                    Double existingTaxAmount = new Double( outputMap.get( taxCode ));
                    taxAmount = taxAmount + existingTaxAmount;
                }
                outputMap.put( taxCode, taxAmount.toString() );
            }
        }
    }

    public PriceDetailsResource getSupplierPriceBreakup(OpsOrderDetails anOrder, OpsProductSubCategory forCategory) {
        LinkedHashMap<String, String> supplierPriceBreakup = new LinkedHashMap<>();

        PriceDetailsResource priceDetails = new PriceDetailsResource();

        switch (forCategory) {
            case PRODUCT_SUB_CATEGORY_FLIGHT: {
                OpsFlightDetails flightDetails = anOrder.getFlightDetails();
                OpsFlightSupplierPriceInfo supplierPriceInfo = flightDetails.getOpsFlightSupplierPriceInfo();
                OpsFlightTotalPriceInfo totalPriceInfo = flightDetails.getTotalPriceInfo();
                List<SpecialServiceRequest> specialServiceRequests = totalPriceInfo.getSpecialServiceRequests();
                BigDecimal totalSSRAmount = BigDecimal.ZERO;
                if(specialServiceRequests!=null){
                    for(int i =0;i< specialServiceRequests.size();i++){
                        SpecialServiceRequest specialServiceRequest = specialServiceRequests.get(i);
                        if(specialServiceRequest.getAmount()!=null)
                            totalSSRAmount = totalSSRAmount.add(new BigDecimal(specialServiceRequest.getAmount()));
                    }
                }

                // Compute how many Adults and Children are travelling!
                List<OpsFlightPaxInfo> paxInfoList = flightDetails.getPaxInfo();
                int adultsCount = 0;
                int childrenCount = 0;

                for (OpsFlightPaxInfo aPaxInfo : paxInfoList) {
                    String paxType = aPaxInfo.getPaxType();
                    if (paxType.equalsIgnoreCase("ADT")) {
                        adultsCount = adultsCount + 1;
                    } else {
                        childrenCount = childrenCount + 1;
                    }
                }

                List<OpsPaxTypeFareFlightSupplier> paxTypeFareList = supplierPriceInfo.getPaxTypeFares();

                for (OpsPaxTypeFareFlightSupplier aPaxTypeFare : paxTypeFareList) {
                    if (aPaxTypeFare.getPaxType().equalsIgnoreCase("ADT")) {
                        computeFares( aPaxTypeFare, supplierPriceBreakup, adultsCount );
                    } else {
                        computeFares( aPaxTypeFare, supplierPriceBreakup, childrenCount );
                    }
                }


                ArrayList <PriceComponentInfo> supplierPricesList = new ArrayList<>();
                for( Map.Entry<String,String> anEntry : supplierPriceBreakup.entrySet() ) {
                    PriceComponentInfo aPriceElement = new PriceComponentInfo();
                    aPriceElement.setName( anEntry.getKey() );
                    aPriceElement.setAmount( anEntry.getValue() );
                    supplierPricesList.add( aPriceElement );
                }
                if(totalSSRAmount.compareTo(BigDecimal.ZERO) > 0)
                    supplierPricesList.add(new PriceComponentInfo("SSR", totalSSRAmount.toString()));

                priceDetails.setPriceComponentInfo( supplierPricesList );
                priceDetails.setTotal( supplierPriceInfo.getSupplierPrice() );
            }
            break;

            case PRODUCT_SUB_CATEGORY_HOTELS: {
                OpsHotelDetails hotelDetails = anOrder.getHotelDetails();
                OpsAccoOrderSupplierPriceInfo orderSupplierPriceInfo = hotelDetails.getOpsAccoOrderSupplierPriceInfo();
                String totalSupplierPrice = orderSupplierPriceInfo.getSupplierPrice();
                OpsTaxes allTaxes = orderSupplierPriceInfo.getTaxes();

                HashMap<String, String> taxesMap = new HashMap<>();
                if (allTaxes != null) {
                    List<OpsTax> taxesList = allTaxes.getTax();
                    for (OpsTax aTax : taxesList) {
                        taxesMap.put(aTax.getTaxCode(), aTax.getAmount().toString());
                    }
                }
                TreeMap<String, String> sortedTaxesMap = new TreeMap<>(taxesMap);
                supplierPriceBreakup.put("Supplier Price", totalSupplierPrice );
                supplierPriceBreakup.putAll(sortedTaxesMap);

                /*** TODO RECEIVABLES, FEES NOT AVAILABLE SO FAR - WIP from BE ***
                 // Add Fees
                 OpsFees allFees = totalPriceInfo.getFees();
                 if( allFees != null ) {
                 List<OpsFee> feesList = allFees.getFee();
                 for( OpsFee aFee : feesList )   {
                 supplierPriceBreakup.put( aFee.getFeeCode(), aFee.getAmount().toString() );
                 }
                 }

                 // Add Receivables
                 OpsReceivables allReceivables = totalPriceInfo.getReceivables();
                 if( allReceivables != null && allReceivables.getReceivable() != null )    {
                 List<OpsReceivable> receivableList = allReceivables.getReceivable();
                 for( OpsReceivable aReceivable : receivableList )   {
                 supplierPriceBreakup.put( aReceivable.getCode(), aReceivable.getAmount().toString() );
                 }
                 }
                 *** RECEIVABLES NOT AVAILABLE SO FAR - WIP from BE ***/

                ArrayList <PriceComponentInfo> supplierPricesList = new ArrayList<>();
                for( Map.Entry<String,String> anEntry : supplierPriceBreakup.entrySet() ) {
                    PriceComponentInfo aPriceElement = new PriceComponentInfo();
                    aPriceElement.setName( anEntry.getKey() );
                    aPriceElement.setAmount( anEntry.getValue() );
                    supplierPricesList.add( aPriceElement );
                }

                priceDetails.setPriceComponentInfo( supplierPricesList );
                priceDetails.setTotal( totalSupplierPrice );
            }
            break;
        }

        return priceDetails;
    }
}
