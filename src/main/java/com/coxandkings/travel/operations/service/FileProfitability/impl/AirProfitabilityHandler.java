package com.coxandkings.travel.operations.service.FileProfitability.impl;

import com.coxandkings.travel.ext.model.be.Incentives;
import com.coxandkings.travel.operations.enums.FileProfitability.FileProfTypes;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.*;
import com.coxandkings.travel.operations.repository.fileProfitabilityModified.FileProfitabilityModifiedRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AirProfitabilityHandler {

    private static final Logger logger = Logger.getLogger(AirProfitabilityHandler.class);

    @Autowired
    FileProfitabilityModifiedRepository fileProfitabilityModifiedRepository;

    public void computeOperationalProfitability(OpsProduct opsProduct, List<FileProfitabilityBooking> profitabilityBookings)
    {
        try
        {
        OpsOrderDetails opsOrderDetails = opsProduct.getOrderDetails();
        OpsFlightDetails opsFlightDetails = opsOrderDetails.getFlightDetails();

        //Ideally this will have only one record but the old implementation is created a lot of duplicate records so this will handle it
        for(FileProfitabilityBooking profitabilityBooking : profitabilityBookings) {

            // SELLING PRICE CALCULATIONS STARTS


            OpsFlightTotalPriceInfo opsFlightTotalPriceInfo = opsFlightDetails.getTotalPriceInfo();
            Double baseFare = opsFlightTotalPriceInfo.getBaseFare().getAmount();
            BigDecimal totalFare = BigDecimal.valueOf(Double.valueOf(opsFlightTotalPriceInfo.getTotalPrice()));
//---------------------------------------------Receivable & Payables------------------------------------------------------------
            BigDecimal receivableTotAmt = new BigDecimal(0.0),payableTotAmt = new BigDecimal(0.0);
            OpsReceivables opsReceivables = opsFlightTotalPriceInfo.getReceivables();
            if(opsReceivables!=null)
                receivableTotAmt = BigDecimal.valueOf(opsReceivables.getAmount());

            Incentives incentives = opsFlightTotalPriceInfo.getIncentives();
            if(incentives!=null)
                payableTotAmt = opsFlightTotalPriceInfo.getIncentives().getAmount();

//-----------------------------------------------Taxes----------------------------------------------------------------------
            OpsTaxes opsTaxes = opsFlightTotalPriceInfo.getTaxes();
            List<OpsTax> opsTaxList = opsTaxes.getTax();
            Map<String, BigDecimal> taxMap = new HashMap<String, BigDecimal>();
            for (OpsTax opsTax : opsTaxList) {
                if (taxMap.containsKey(opsTax.getTaxCode()))
                    taxMap.put(opsTax.getTaxCode(), taxMap.get(opsTax.getTaxCode()).add(BigDecimal.valueOf(opsTax.getAmount())));
                else
                    taxMap.put(opsTax.getTaxCode(), BigDecimal.valueOf(opsTax.getAmount()));
            }
            BigDecimal taxesTotAmt = BigDecimal.valueOf(opsTaxes.getAmount());

//-----------------------------------------------Fees-----------------------------------------------------------------------
            OpsFees opsFees = opsFlightTotalPriceInfo.getFees();
            List<OpsFee> feesList = opsFees.getFee();
            Map<String, BigDecimal> feeMap = new HashMap<>();
            for (OpsFee fee : feesList) {
                if (feeMap.containsKey(fee.getFeeCode()))
                    feeMap.put(fee.getFeeCode(), feeMap.get(fee.getFeeCode()).add(BigDecimal.valueOf(fee.getAmount())));
                else
                    feeMap.put(fee.getFeeCode(), BigDecimal.valueOf(fee.getAmount()));
            }
            BigDecimal feesTotAmt = BigDecimal.valueOf(opsFees.getTotal());

//---------------------------------------------Company Taxes-----------------------------------------------------------------
            //BOOKing engine adds company taxes so even im adding it.
            OpsCompanyTaxes opsCompanyTaxes = opsFlightTotalPriceInfo.getCompanyTaxes();
            List<OpsCompanyTax> companyTaxes = opsCompanyTaxes.getCompanyTax();
//            Map<String, BigDecimal> companyMap = new HashMap<>();
            for (OpsCompanyTax companyTax : companyTaxes) {
                if (taxMap.containsKey(companyTax.getTaxCode()))
                    taxMap.put(companyTax.getTaxCode(), taxMap.get(companyTax.getTaxCode()).add(companyTax.getAmount()));
                else
                    taxMap.put(companyTax.getTaxCode(), companyTax.getAmount());
            }
            taxesTotAmt = taxesTotAmt.add(opsCompanyTaxes.getAmount());
//------------------------------------------------------------------------------------------------------------------------------
            ProfSellingPrice profSellingPrice = new ProfSellingPrice();
            SellingPrice sellingPrice = new SellingPrice();

            sellingPrice.setBasFare(baseFare);
            sellingPrice.setTaxes(taxMap);
            sellingPrice.setTotalTaxesAmt(taxesTotAmt);
            sellingPrice.setFees(feeMap);
            sellingPrice.setTotalFees(feesTotAmt);
            profSellingPrice.setSellingPrice(sellingPrice);

            TotalInCompanyMarketCurrency totalInCompanyMarketCurrency = new TotalInCompanyMarketCurrency();
            totalInCompanyMarketCurrency.setBasFare(baseFare);
            totalInCompanyMarketCurrency.setTaxes(taxMap);
            totalInCompanyMarketCurrency.setTotalTaxesAmt(taxesTotAmt);
            totalInCompanyMarketCurrency.setFees(feeMap);
            totalInCompanyMarketCurrency.setTotalFees(feesTotAmt);
            profSellingPrice.setTotalInCompanyMarketCurrency(totalInCompanyMarketCurrency);

            profSellingPrice.setClientCPUpdated(true);
            profSellingPrice.setClientCRUpdated(true);
            profSellingPrice.setClientCommercialsPayable(payableTotAmt);
            profSellingPrice.setClientCommercialsReceivable(receivableTotAmt);
            profSellingPrice.setDiscountsOffers(new BigDecimal(0));
            profSellingPrice.setComissionToClient(new BigDecimal(0));

            profSellingPrice.setTotalNetSellingPrice(totalFare.add(payableTotAmt).subtract(receivableTotAmt));


            //SUPPLIER COST PRICE CALCULATIONS STARTS


            OpsFlightSupplierPriceInfo opsFlightSupplierPriceInfo = opsFlightDetails.getOpsFlightSupplierPriceInfo();
            BigDecimal supplierBaseFare = BigDecimal.valueOf(Double.valueOf(opsFlightSupplierPriceInfo.getSupplierPrice()));

            BigDecimal supplierCommercialsReceivable = new BigDecimal(0.0), supplierCommercialsPayable = new BigDecimal(0.0);

            List<OpsOrderSupplierCommercial> opsOrderSupplierCommercialList = opsOrderDetails.getSupplierCommercials();
            if (opsOrderSupplierCommercialList != null) {
                for (OpsOrderSupplierCommercial opsOrderSupplierCommercial : opsOrderSupplierCommercialList) {
                    BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(opsOrderSupplierCommercial.getCommercialAmount()));
                    if (opsOrderSupplierCommercial.getCommercialType().equals("Receivable")) {
                        supplierCommercialsReceivable = supplierCommercialsReceivable.add(amount);
                    } else {
                        supplierCommercialsPayable = supplierCommercialsPayable.add(amount);
                    }
                }
            }

            ProfSupplierCostPrice profSupplierCostPrice = new ProfSupplierCostPrice();
            SellingPrice supplierSellingPrice = new SellingPrice();
            supplierSellingPrice.setBasFare(supplierBaseFare.doubleValue());
            profSupplierCostPrice.setGrossSupplierCost(supplierSellingPrice);

            profSupplierCostPrice.setSupplierCPUpdated(true);
            profSupplierCostPrice.setSupplierCRUpdated(true);
            profSupplierCostPrice.setSupplierCommercialsReceivable(supplierCommercialsReceivable);
            profSupplierCostPrice.setSupplierCommercialsPayable(supplierCommercialsPayable);
            profSupplierCostPrice.setTotalNetPayableToSupplier(supplierBaseFare.add(supplierCommercialsPayable).subtract(supplierCommercialsReceivable));//not sure whether receivables are already added in the base fare

//------------------------------------------------PROF MARGIN CALCULATIONS--------------------------------------------------------

            ProfMargin profMargin = new ProfMargin(); //I Picked up the old implementation that already works! Clean this up later
            profMargin.setNetMarginAmt(profSellingPrice.getTotalNetSellingPrice().subtract(profSupplierCostPrice.getTotalNetPayableToSupplier()));
            BigDecimal forPercentage = profSellingPrice.getTotalNetSellingPrice().subtract(profSupplierCostPrice.getTotalNetPayableToSupplier());

            if (!profSupplierCostPrice.getTotalNetPayableToSupplier().equals(0)) {
                BigDecimal percentage = new BigDecimal(0.0);
                try {
                    percentage = forPercentage.divide(profSupplierCostPrice.getTotalNetPayableToSupplier(), 3, BigDecimal.ROUND_CEILING);
                } catch (Exception e) {
                    //  logger.error(e.printStackTrace(),"sdfgjn");
                    StringWriter stack = new StringWriter();
                    e.printStackTrace(new PrintWriter(stack));
                }
                profMargin.setNetMarginPercentage(percentage);
            }

            BudgetedProfitability budgetedProfitability = profitabilityBooking.getBudgetedFileProf();

            OperationalProfitability operationalProfitability = new OperationalProfitability();
            operationalProfitability.setProfMargin(profMargin);
            operationalProfitability.setProfSellingPrice(profSellingPrice);
            operationalProfitability.setProfSupplierCostPrice(profSupplierCostPrice);

            FinalProfitability finalProfitability = new FinalProfitability();
            finalProfitability.setProfMargin(profMargin);
            finalProfitability.setProfSellingPrice(profSellingPrice);
            finalProfitability.setProfSupplierCostPrice(profSupplierCostPrice);

            profitabilityBooking.setOperationalFileProf(operationalProfitability);
            profitabilityBooking.setFinaFileProf(finalProfitability);

            Variance variance = new Variance();
            variance.setBudgetedVsFinal(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
            variance.setBudgetedVsOperational(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(operationalProfitability.getProfMargin().getNetMarginAmt()));
            variance.setOperationalVsFinal(operationalProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
            profitabilityBooking.setVariance(variance);
            fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(profitabilityBooking);
        }
        }
        catch(Exception e){
            logger.error("Error occurred while saving the kafka Booking in File Profitability");
        }

    }


    public void computeOperationalProfitabilityForPax(OpsProduct opsProduct, List<FileProfitabilityBooking> profitabilityBookings)
    {
        try {

            OpsOrderDetails opsOrderDetails = opsProduct.getOrderDetails();
            OpsFlightDetails opsFlightDetails = opsOrderDetails.getFlightDetails();

            List<OpsFlightPaxInfo> flightPaxInfo = opsFlightDetails.getPaxInfo();

            OpsFlightTotalPriceInfo totalPriceInfo = opsFlightDetails.getTotalPriceInfo();
            List<OpsPaxTypeFareFlightClient> totalPaxTypeFares = totalPriceInfo.getPaxTypeFares();
            Map<String, OpsPaxTypeFareFlightClient> clientMap = new HashMap<>();
            Iterator<OpsPaxTypeFareFlightClient> flightClientIterator = totalPaxTypeFares.iterator();
            while(flightClientIterator.hasNext())
            {
                OpsPaxTypeFareFlightClient fareFlightClient = flightClientIterator.next();
                clientMap.put(fareFlightClient.getPaxType(),fareFlightClient);
            }

            OpsFlightSupplierPriceInfo supplierPriceInfo = opsFlightDetails.getOpsFlightSupplierPriceInfo();
            List<OpsPaxTypeFareFlightSupplier> supplierPaxTypeFares = supplierPriceInfo.getPaxTypeFares();
            Map<String, OpsPaxTypeFareFlightSupplier> supplierMap = new HashMap<>();
            Iterator<OpsPaxTypeFareFlightSupplier> flightSupplierIterator = supplierPaxTypeFares.iterator();
            while(flightSupplierIterator.hasNext())
            {
                OpsPaxTypeFareFlightSupplier fareFlightSupplier = flightSupplierIterator.next();
                supplierMap.put(fareFlightSupplier.getPaxType(),fareFlightSupplier);
            }

            //We will check with Passenger Name in order to update it
            for(FileProfitabilityBooking profitabilityBooking : profitabilityBookings) {
                boolean foundPax = false;
                for(OpsFlightPaxInfo paxInfo : flightPaxInfo) {

                    //We are filtering by pax name coz there's no other way to do it as per current implementation
                    String paxName = profitabilityBooking.getPaxBreakDown().getPassengerName();

                    if(paxName.equalsIgnoreCase(profitabilityBooking.getPaxBreakDown().getPassengerName())) {
                        foundPax = true;
                        String paxType = paxInfo.getPaxType();


                        //SELLING PRICE CALCULATIONS START


                        OpsPaxTypeFareFlightClient paxClientDtls = clientMap.get(paxType);


                        BigDecimal clientTotFare = BigDecimal.valueOf(paxClientDtls.getTotalFare().getAmount());
                        BigDecimal clientBaseFare = BigDecimal.valueOf(paxClientDtls.getBaseFare().getAmount());
//-------------------------------------------------------CALCULATING TAX----------------------------------------------------------

                        Map<String, BigDecimal> taxMap = new HashMap<String, BigDecimal>();

                        OpsTaxes taxesObj = paxClientDtls.getTaxes();
                        BigDecimal totTaxAmt = BigDecimal.valueOf(taxesObj.getAmount());//Total Tax Amount
                        List<OpsTax> taxList = taxesObj.getTax();
                        for(OpsTax opsTax : taxList)
                        {
                            if(taxMap.containsKey(opsTax.getTaxCode()))
                                taxMap.put(opsTax.getTaxCode(), taxMap.get(opsTax.getTaxCode()).add(BigDecimal.valueOf(opsTax.getAmount())));
                            else
                                taxMap.put(opsTax.getTaxCode(), BigDecimal.valueOf(opsTax.getAmount()));
                        }

//-------------------------------------------------------CALCULATING FEES------------------------------------------------------------

                        Map<String, BigDecimal> feeMap = new HashMap<String, BigDecimal>();

                        OpsFees feeObj = paxClientDtls.getFees();
                        BigDecimal totFeeAmt = BigDecimal.valueOf(feeObj.getTotal());
                        List<OpsFee> feeList = feeObj.getFee();
                        for(OpsFee fee : feeList){
                            if(feeMap.containsKey(fee.getFeeCode()))
                                feeMap.put(fee.getFeeCode(), feeMap.get(fee.getFeeCode()).add(BigDecimal.valueOf(fee.getAmount())));
                            else
                                feeMap.put(fee.getFeeCode(), BigDecimal.valueOf(fee.getAmount()));
                        }
//-----------------------------------------------------RECEIVABLES AND PAYABLES---------------------------------------------------------
                        List<OpsClientEntityCommercial> entityCommercialsList = paxClientDtls.getOpsClientEntityCommercial();

                        BigDecimal clientReceivables = new BigDecimal(0.0), clientPayables = new BigDecimal(0.0);
                        for(OpsClientEntityCommercial clientEntityCommercial : entityCommercialsList)
                        {
                            List<OpsPaxRoomClientCommercial> clientCommercialList = clientEntityCommercial.getOpsPaxRoomClientCommercial();
                            for(OpsPaxRoomClientCommercial clientCommercial : clientCommercialList)
                            {
                                if(clientCommercial.getCommercialName().equalsIgnoreCase("Receivable"))
                                    clientReceivables = clientReceivables.add(BigDecimal.valueOf(clientCommercial.getCommercialAmount()));
                                else
                                    clientPayables = clientPayables.add(BigDecimal.valueOf(clientCommercial.getCommercialAmount()));
                            }
                        }

//---------------------------------------------------------------------------------------------------------------------------------------

                        ProfSellingPrice profSellingPrice = new ProfSellingPrice();
                        SellingPrice sellingPrice = new SellingPrice();
                        sellingPrice.setBasFare(clientBaseFare.doubleValue());
                        sellingPrice.setTotalTaxesAmt(totTaxAmt);
                        sellingPrice.setTaxes(taxMap);
                        sellingPrice.setFees(feeMap);
                        sellingPrice.setTotalFees(totFeeAmt);
                        profSellingPrice.setSellingPrice(sellingPrice);

                        TotalInCompanyMarketCurrency totalInCompanyMarketCurrency = new TotalInCompanyMarketCurrency();
                        totalInCompanyMarketCurrency.setBasFare(clientBaseFare.doubleValue());
                        totalInCompanyMarketCurrency.setTaxes(taxMap);
                        totalInCompanyMarketCurrency.setTotalTaxesAmt(totTaxAmt);
                        totalInCompanyMarketCurrency.setFees(feeMap);
                        totalInCompanyMarketCurrency.setTotalFees(totFeeAmt);
                        profSellingPrice.setTotalInCompanyMarketCurrency(totalInCompanyMarketCurrency);

                        profSellingPrice.setClientCPUpdated(true);
                        profSellingPrice.setClientCRUpdated(true);
                        profSellingPrice.setClientCommercialsReceivable(clientReceivables);
                        profSellingPrice.setClientCommercialsPayable(clientPayables);
                        profSellingPrice.setDiscountsOffers(new BigDecimal(0));
                        profSellingPrice.setComissionToClient(new BigDecimal(0));

                        profSellingPrice.setTotalNetSellingPrice(clientTotFare.add(clientPayables).subtract(clientReceivables));


                        //SUPPLIER COST PRICE CALCULATIONS START


                        OpsPaxTypeFareFlightSupplier paxSupplierDtls = supplierMap.get(paxType);

                        BigDecimal supplierBaseFare = BigDecimal.valueOf(paxSupplierDtls.getBaseFare().getAmount());
                        BigDecimal supplierTotalFare = BigDecimal.valueOf(paxSupplierDtls.getTotalFare().getAmount());

//-----------------------------------------CALCULATING FEES--------------------------------------------------------------------

                        Map<String, BigDecimal> suppFeeMap = new HashMap<>();

                        OpsFees opsFees = paxSupplierDtls.getFees();
                        BigDecimal supplierFeesTotal = BigDecimal.valueOf(opsFees.getTotal());
                        List<OpsFee> suppFeeList = opsFees.getFee();
                        for(OpsFee fee : suppFeeList){
                            if(suppFeeMap.containsKey(fee.getFeeCode()))
                                suppFeeMap.put(fee.getFeeCode(),suppFeeMap.get(fee.getFeeCode()).add(BigDecimal.valueOf(fee.getAmount())));
                            else
                                suppFeeMap.put(fee.getFeeCode(), BigDecimal.valueOf(fee.getAmount()));
                        }
//--------------------------------------CALCULATING TAXES--------------------------------------------------------------------------

                        Map<String, BigDecimal> suppTaxMap = new HashMap<>();

                        OpsTaxes opsTaxes = paxSupplierDtls.getTaxes();
                        BigDecimal supplierTaxTotal = BigDecimal.valueOf(opsTaxes.getAmount());
                        List<OpsTax> supplierTaxList = opsTaxes.getTax();
                        for(OpsTax opsTax : supplierTaxList){
                            if(suppTaxMap.containsKey(opsTax.getTaxCode()))
                                suppTaxMap.put(opsTax.getTaxCode(), suppTaxMap.get(opsTax.getTaxCode()).add(BigDecimal.valueOf(opsTax.getAmount())));
                            else
                                suppTaxMap.put(opsTax.getTaxCode(), BigDecimal.valueOf(opsTax.getAmount()));
                        }
//--------------------------------------RECEIVABLES AND PAYABLES---------------------------------------------------------------------

                        List<OpsFlightPaxSupplierCommercial> opsFlightPaxSupplierCommercials = paxSupplierDtls.getSupplierCommercials();
                        BigDecimal supplierReceivables = new BigDecimal(0.0), supplierPayables = new BigDecimal(0.0);
                        for(OpsFlightPaxSupplierCommercial opsFlightPaxSupplierCommercial : opsFlightPaxSupplierCommercials){
                            if(opsFlightPaxSupplierCommercial.getCommercialName().equalsIgnoreCase("Receivable"))
                                supplierReceivables = supplierReceivables.add(BigDecimal.valueOf(opsFlightPaxSupplierCommercial.getCommercialAmount()));
                            else
                                supplierPayables = supplierPayables.add(BigDecimal.valueOf(opsFlightPaxSupplierCommercial.getCommercialAmount()));
                        }

//--------------------------------------------------------------------------------------------------------------------------------------

                        ProfSupplierCostPrice profSupplierCostPrice = new ProfSupplierCostPrice();
                        SellingPrice supplierSellingPrice = new SellingPrice();
                        supplierSellingPrice.setBasFare(supplierBaseFare.doubleValue());
                        supplierSellingPrice.setFees(suppFeeMap);
                        supplierSellingPrice.setTotalFees(supplierFeesTotal);
                        supplierSellingPrice.setTaxes(suppTaxMap);
                        supplierSellingPrice.setTotalTaxesAmt(supplierTaxTotal);
                        profSupplierCostPrice.setGrossSupplierCost(supplierSellingPrice);

                        profSupplierCostPrice.setSupplierCPUpdated(true);
                        profSupplierCostPrice.setSupplierCRUpdated(true);
                        profSupplierCostPrice.setSupplierCommercialsReceivable(supplierReceivables);
                        profSupplierCostPrice.setSupplierCommercialsPayable(supplierPayables);
                        profSupplierCostPrice.setTotalNetPayableToSupplier(supplierBaseFare.add(supplierPayables).subtract(supplierReceivables));

//-------------------------------------------------------PROF MARGIN CALCULATION-----------------------------------------------------
                        BigDecimal forPercentage = new BigDecimal(0.0), percentage = new BigDecimal(0.0);
                        ProfMargin profMargin = new ProfMargin();
                        profMargin.setNetMarginAmt(profSellingPrice.getTotalNetSellingPrice().subtract(profSupplierCostPrice.getTotalNetPayableToSupplier()));
                        forPercentage = profSellingPrice.getTotalNetSellingPrice().subtract(profSupplierCostPrice.getTotalNetPayableToSupplier());

                        if (!profSupplierCostPrice.getTotalNetPayableToSupplier().equals(0)) {
                            try {
                                percentage = forPercentage.divide(profSupplierCostPrice.getTotalNetPayableToSupplier(), 3, BigDecimal.ROUND_CEILING);
                            } catch (Exception e) {
                                //  logger.error(e.printStackTrace(),"sdfgjn");
                                StringWriter stack = new StringWriter();
                                e.printStackTrace(new PrintWriter(stack));
                                logger.debug("Caught exception : " + stack.toString());
                            }
                            profMargin.setNetMarginPercentage(percentage);
                        }

                        BudgetedProfitability budgetedProfitability = profitabilityBooking.getBudgetedFileProf();

                        OperationalProfitability operationalProfitability = profitabilityBooking.getOperationalFileProf();
                        operationalProfitability.setProfMargin(profMargin);
                        operationalProfitability.setProfSellingPrice(profSellingPrice);
                        operationalProfitability.setProfSupplierCostPrice(profSupplierCostPrice);

                        FinalProfitability finalProfitability = profitabilityBooking.getFinaFileProf();
                        finalProfitability.setProfMargin(profMargin);
                        finalProfitability.setProfSellingPrice(profSellingPrice);
                        finalProfitability.setProfSupplierCostPrice(profSupplierCostPrice);

                        Variance variance = new Variance();
                        variance.setBudgetedVsFinal(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
                        variance.setBudgetedVsOperational(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(operationalProfitability.getProfMargin().getNetMarginAmt()));
                        variance.setOperationalVsFinal(operationalProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
                        profitabilityBooking.setVariance(variance);

                        fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(profitabilityBooking);

                    }

                }


            }


        }catch (Exception e)
        {
            logger.error("Error Occurred While saving Pax Details");
        }


    }

}
