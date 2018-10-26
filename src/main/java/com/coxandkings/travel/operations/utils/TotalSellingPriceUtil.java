package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.ext.model.be.Discount;
import com.coxandkings.travel.ext.model.be.Discounts;
import com.coxandkings.travel.ext.model.be.Incentives;
import com.coxandkings.travel.ext.model.be.SpecialServiceRequest;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.pricedetails.PriceComponentInfo;
import com.coxandkings.travel.operations.resource.pricedetails.PriceDetailsResource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class TotalSellingPriceUtil {

    private String TOTAL_PRICE = "Total";

    public TotalSellingPriceUtil() {
    }

    public PriceDetailsResource getSellingPriceBreakup(OpsOrderDetails anOrder, OpsProductSubCategory forCategory) {
        PriceDetailsResource priceDetailsBreakup = new PriceDetailsResource();

        LinkedHashMap<String, String> sellingPriceBreakup = null;

        switch (forCategory) {
            case PRODUCT_SUB_CATEGORY_FLIGHT: {
                OpsFlightDetails flightDetails = anOrder.getFlightDetails();
                OpsFlightTotalPriceInfo totalPriceInfo = flightDetails.getTotalPriceInfo();

                sellingPriceBreakup = new LinkedHashMap<>();
                // Add base fare
                OpsBaseFare baseFare = totalPriceInfo.getBaseFare();
                sellingPriceBreakup.put("Base Fare", baseFare.getAmount().toString());
                List<SpecialServiceRequest> specialServiceRequests = totalPriceInfo.getSpecialServiceRequests();
                BigDecimal totalSSRAmount = BigDecimal.ZERO;
                if(specialServiceRequests!=null){
                    for(int i =0;i< specialServiceRequests.size();i++){
                        SpecialServiceRequest specialServiceRequest = specialServiceRequests.get(i);
                        if(specialServiceRequest.getAmount()!=null)
                            totalSSRAmount = totalSSRAmount.add(new BigDecimal(specialServiceRequest.getAmount()));
                    }
                }
                // Add Fees
                OpsFees allFees = totalPriceInfo.getFees();
                if (allFees != null) {
                    List<OpsFee> feesList = allFees.getFee();
                    for (OpsFee aFee : feesList) {
                        sellingPriceBreakup.put(aFee.getFeeCode(), aFee.getAmount().toString());
                    }
                }

                // Add Receivables
                OpsReceivables allReceivables = totalPriceInfo.getReceivables();
                if (allReceivables != null && allReceivables.getReceivable() != null) {
                    List<OpsReceivable> receivableList = allReceivables.getReceivable();
                    for (OpsReceivable aReceivable : receivableList) {
                        sellingPriceBreakup.put(aReceivable.getCode(), aReceivable.getAmount().toString());
                    }
                }

                // Add all taxes
                OpsTaxes allTaxes = totalPriceInfo.getTaxes();
                if (allTaxes != null) {
                    List<OpsTax> taxesList = allTaxes.getTax();
                    for (OpsTax aTax : taxesList) {
                        sellingPriceBreakup.put(aTax.getTaxCode(), aTax.getAmount().toString());
                    }
                }
                OpsCompanyTaxes companyTaxes = totalPriceInfo.getCompanyTaxes();

                if (companyTaxes != null && companyTaxes.getCompanyTax() != null) {
                    sellingPriceBreakup.put("Company Taxes", companyTaxes.getAmount().toString());
                }

                if(totalSSRAmount.compareTo(BigDecimal.ZERO) > 0)
                    sellingPriceBreakup.put("SSR", totalSSRAmount.toString());
                Incentives incentives = totalPriceInfo.getIncentives();
                if(incentives!=null && incentives.getAmount().compareTo(BigDecimal.ZERO)>0){
                    sellingPriceBreakup.put("Incentive",incentives.getAmount().toString());
                }
                Discounts discounts = totalPriceInfo.getDiscounts();
                if(discounts!=null){
                    List<Discount> discountList = discounts.getDiscounts();
                    if(discountList!=null ){
                       for(Discount aDiscount:discountList){
                           sellingPriceBreakup.put(aDiscount.getDiscountCode(),aDiscount.getAmount().toString());
                       }
                    }
                }

                ArrayList<PriceComponentInfo> sellingPricesList = new ArrayList<>();
                for (Map.Entry<String, String> anEntry : sellingPriceBreakup.entrySet()) {
                    PriceComponentInfo aPriceElement = new PriceComponentInfo();
                    aPriceElement.setName(anEntry.getKey());
                    aPriceElement.setAmount(anEntry.getValue());
                    sellingPricesList.add(aPriceElement);
                }

                priceDetailsBreakup.setPriceComponentInfo(sellingPricesList);
                priceDetailsBreakup.setTotal(totalPriceInfo.getTotalPrice());
            }
            break;

            case PRODUCT_SUB_CATEGORY_HOTELS: {
                OpsHotelDetails hotelDetails = anOrder.getHotelDetails();
                OpsAccommodationTotalPriceInfo totalPriceInfo = hotelDetails.getOpsAccommodationTotalPriceInfo();
                OpsCompanyTaxes companyTaxes = totalPriceInfo.getCompanyTaxes();

                if (companyTaxes != null && companyTaxes.getCompanyTax() != null) {
                    sellingPriceBreakup.put("Company Taxes", companyTaxes.getAmount().toString());
                }

                OpsIncentives incentives = totalPriceInfo.getIncentives();
                if(incentives!=null && incentives.getAmount().compareTo(BigDecimal.ZERO)>0){
                    sellingPriceBreakup.put("Incentive",incentives.getAmount().toString());
                }

                OpsDiscounts discounts = totalPriceInfo.getDiscounts();
                if(discounts!=null){
                    List<OpsDiscount> discountList = discounts.getDiscounts();
                    if(discountList!=null ){
                        for(OpsDiscount aDiscount:discountList){
                            sellingPriceBreakup.put(aDiscount.getDiscountCode(),aDiscount.getAmount().toString());
                        }
                    }
                }

                OpsTaxes allTaxes = totalPriceInfo.getOpsTaxes();

                /*** RECEIVABLES, FEES NOT AVAILABLE SO FAR - WIP from BE ***
                 // Add Fees
                 OpsFees allFees = totalPriceInfo.getFees();
                 if( allFees != null ) {
                 List<OpsFee> feesList = allFees.getFee();
                 for( OpsFee aFee : feesList )   {
                 sellingPriceBreakup.put( aFee.getFeeCode(), aFee.getAmount().toString() );
                 }
                 }

                 // Add Receivables
                 OpsReceivables allReceivables = totalPriceInfo.getReceivables();
                 if( allReceivables != null && allReceivables.getReceivable() != null )    {
                 List<OpsReceivable> receivableList = allReceivables.getReceivable();
                 for( OpsReceivable aReceivable : receivableList )   {
                 sellingPriceBreakup.put( aReceivable.getCode(), aReceivable.getAmount().toString() );
                 }
                 }
                 *** RECEIVABLES NOT AVAILABLE SO FAR - WIP from BE ***/

                HashMap<String, String> taxesMap = new HashMap<>();
                if (allTaxes != null) {
                    List<OpsTax> taxesList = allTaxes.getTax();
                    for (OpsTax aTax : taxesList) {
                        String taxCode = aTax.getTaxCode();
                        Double taxAmount = aTax.getAmount();
                        if (taxesMap.containsKey(taxCode)) {
                            Double existingTaxAmount = new Double(taxesMap.get(taxCode));
                            taxAmount = existingTaxAmount + taxAmount;
                        }
                        taxesMap.put(taxCode, taxAmount.toString());
                    }
                }

                TreeMap<String, String> sortedTaxesMap = new TreeMap<>(taxesMap);
                sellingPriceBreakup = new LinkedHashMap<>();
                sellingPriceBreakup.put("Total Price", totalPriceInfo.getTotalPrice());
                sellingPriceBreakup.putAll(sortedTaxesMap);

                ArrayList<PriceComponentInfo> sellingPricesList = new ArrayList<>();
                for (Map.Entry<String, String> anEntry : sellingPriceBreakup.entrySet()) {
                    PriceComponentInfo aPriceElement = new PriceComponentInfo();
                    aPriceElement.setName(anEntry.getKey());
                    aPriceElement.setAmount(anEntry.getValue());
                    sellingPricesList.add(aPriceElement);
                }

                priceDetailsBreakup.setPriceComponentInfo(sellingPricesList);
                priceDetailsBreakup.setTotal(totalPriceInfo.getTotalPrice());
            }
            break;
        }
        return priceDetailsBreakup;
    }
}