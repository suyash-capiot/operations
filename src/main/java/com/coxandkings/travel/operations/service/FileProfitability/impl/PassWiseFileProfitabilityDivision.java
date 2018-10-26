package com.coxandkings.travel.operations.service.FileProfitability.impl;

import com.coxandkings.travel.operations.criteria.fileprofitability.FileProfSearchCriteria;
import com.coxandkings.travel.operations.enums.FileProfitability.FileProfTypes;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.*;
import com.coxandkings.travel.operations.repository.fileProfitabilityModified.FileProfitabilityModifiedRepository;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class PassWiseFileProfitabilityDivision {
    private static final Logger logger = Logger.getLogger(PassWiseFileProfitabilityDivision.class);
    @Autowired
    FileProfitabilityModifiedRepository fileProfitabilityModifiedRepository;
    @Autowired
    UpdatedPricing updatedPricing;

    public void passengerWiseFileProfitabilityDivision(OpsPaxTypeFareFlightClient opsPaxTypeFareFlightClient, OpsPaxTypeFareFlightSupplier opsPaxTypeFareFlightSupplier, FileProfitabilityBooking fileProfitabilityBooking, FileProfTypes fileProfType, PaxBreakDown paxBreakDown) {
        BigDecimal baseFare = new BigDecimal(0), taxesAmt = new BigDecimal(0), clientCommercialsPayable = new BigDecimal(0), clientCommercialsReceivable = new BigDecimal(0);
        BigDecimal percentage = new BigDecimal(0), forPercentage = new BigDecimal(0), feeAmt = new BigDecimal(0),supplierCommercialsPayable = new BigDecimal(0), supplierCommercialsReceivable = new BigDecimal(0);;
        Map<String, BigDecimal> setFees = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> taxMap = new HashMap<String, BigDecimal>();
        // PaxBreakDown paxBreakDown = new PaxBreakDown();

        if (opsPaxTypeFareFlightClient.getBaseFare() != null && opsPaxTypeFareFlightClient.getBaseFare().getAmount() != 0) {
            baseFare = BigDecimal.valueOf(opsPaxTypeFareFlightClient.getBaseFare().getAmount());
        }
        OpsFees opsFeesList = null;
        if (opsPaxTypeFareFlightClient.getFees() != null) {
            opsFeesList = opsPaxTypeFareFlightClient.getFees();
        }

        if (opsFeesList != null) {
            List<OpsFee> fee = opsFeesList.getFee();

            if (fee != null && !fee.isEmpty()) {
                for (OpsFee opsFee : fee) {
                    if (opsFee != null) {
                        feeAmt = feeAmt.add(new BigDecimal(opsFee.getAmount()));

                        if (setFees.size() != 0) {
                            if (setFees.containsKey(opsFee.getFeeCode())) {
                                setFees.put(opsFee.getFeeCode(), setFees.get(opsFee.getFeeCode()).add(new BigDecimal(opsFee.getAmount())));
                            } else {
                                setFees.put(opsFee.getFeeCode(), new BigDecimal(opsFee.getAmount()));
                            }
                        } else {
                            setFees.put(opsFee.getFeeCode(), new BigDecimal(opsFee.getAmount()));
                        }
                    }
                }
            }
            //Added by Ashley
            feeAmt = opsPaxTypeFareFlightClient.getFees().getTotal()!=null?BigDecimal.valueOf(opsPaxTypeFareFlightClient.getFees().getTotal()):new BigDecimal(0.0);

        }
        OpsTaxes taxes = null;
        if (opsPaxTypeFareFlightClient.getTaxes() != null) {
            taxes = opsPaxTypeFareFlightClient.getTaxes();
        }
        if (taxes != null) {
            List<OpsTax> opsTaxList = taxes.getTax();

            if (opsTaxList != null && !opsTaxList.isEmpty()) {
                for (OpsTax opsTax : opsTaxList) {
                    taxesAmt = taxesAmt.add(BigDecimal.valueOf(opsTax.getAmount()));
                    if (taxMap.size() != 0) {
                        if (taxMap.containsKey(opsTax.getTaxCode())) {
                            taxMap.put(opsTax.getTaxCode(), taxMap.get(opsTax.getTaxCode()).add(new BigDecimal(opsTax.getAmount())));
                        } else {
                            taxMap.put(opsTax.getTaxCode(), new BigDecimal(opsTax.getAmount()));
                        }
                    } else {
                        taxMap.put(opsTax.getTaxCode(), new BigDecimal(opsTax.getAmount()));
                    }
                }
            }
        }

        //ADDED BY ASHLEY
        taxesAmt = opsPaxTypeFareFlightClient.getTaxes().getAmount()!=null?BigDecimal.valueOf(opsPaxTypeFareFlightClient.getTaxes().getAmount()): new BigDecimal(0.0);


        ProfSellingPrice profSellingPrice = new ProfSellingPrice();

        profSellingPrice.setDiscountsOffers(new BigDecimal(0));
        profSellingPrice.setComissionToClient(new BigDecimal(0));
        SellingPrice sellingPrice = new SellingPrice();
        sellingPrice.setBasFare(baseFare.doubleValue());
        sellingPrice.setFees(setFees);
        sellingPrice.setTotalTaxesAmt(taxesAmt);
        sellingPrice.setTotalFees(feeAmt);
        sellingPrice.setTaxes(taxMap);
        profSellingPrice.setSellingPrice(sellingPrice);

        TotalInCompanyMarketCurrency totalInCompanyMarketCurrency = new TotalInCompanyMarketCurrency();
        totalInCompanyMarketCurrency.setBasFare(baseFare.doubleValue());
        totalInCompanyMarketCurrency.setFees(setFees);
        totalInCompanyMarketCurrency.setTotalFees(feeAmt);
        totalInCompanyMarketCurrency.setTotalTaxesAmt(taxesAmt);
        totalInCompanyMarketCurrency.setTaxes(taxMap);
        profSellingPrice.setTotalInCompanyMarketCurrency(totalInCompanyMarketCurrency);

//--------------X-----------------------X---------------------X-----------------------ADDED BY ASHLEY for Client receivable CALCULATIONS------------------X---------------------X---------------------------X-----------------------X
        //Adding receivables and payable Added by ASHLEY
        BigDecimal clientTotalFare = BigDecimal.valueOf(opsPaxTypeFareFlightClient.getTotalFare().getAmount());
        List<OpsClientEntityCommercial> opsClientEntityCommercials = opsPaxTypeFareFlightClient.getOpsClientEntityCommercial();

        for(OpsClientEntityCommercial clientEntityCommercials : opsClientEntityCommercials)
        {
            List<OpsPaxRoomClientCommercial> clientCommercials = clientEntityCommercials.getOpsPaxRoomClientCommercial();
            for(OpsPaxRoomClientCommercial clientCommercial : clientCommercials)
            {
                if(clientCommercial.getCommercialName().equalsIgnoreCase("receivable"))
                    clientCommercialsReceivable = clientCommercialsReceivable.add(BigDecimal.valueOf(clientCommercial.getCommercialAmount()));
                else
                    clientCommercialsPayable = clientCommercialsPayable.add(BigDecimal.valueOf(clientCommercial.getCommercialAmount()));
            }
        }

        profSellingPrice.setClientCRUpdated(false);
        profSellingPrice.setClientCPUpdated(false);
        profSellingPrice.setClientCommercialsReceivable(clientCommercialsReceivable);
        profSellingPrice.setClientCommercialsPayable(clientCommercialsPayable);
        profSellingPrice.setTotalNetSellingPrice(clientTotalFare.add(clientCommercialsPayable).subtract(clientCommercialsReceivable));//Subtracting receivables because it is already added in total fare
                                                                                                                                      //Adding payables because it is NOT added in total fare

//--------------X-----------------------X---------------------X-----------------------ADDED BY ASHLEY for SUPPLIER CALCULATIONS------------------X---------------------X---------------------------X-----------------------X

        BigDecimal supplierTotalPrice = BigDecimal.valueOf(opsPaxTypeFareFlightSupplier.getTotalFare().getAmount());
        BigDecimal suppBaseFareAmt = BigDecimal.valueOf(opsPaxTypeFareFlightSupplier.getBaseFare().getAmount());
        BigDecimal totalSuppFees = opsPaxTypeFareFlightSupplier.getFees().getTotal()!=null?BigDecimal.valueOf(opsPaxTypeFareFlightSupplier.getFees().getTotal()):new BigDecimal(0.0);
        BigDecimal totalSuppTaxes = opsPaxTypeFareFlightSupplier.getTaxes().getAmount()!=null?BigDecimal.valueOf(opsPaxTypeFareFlightSupplier.getTaxes().getAmount()):new BigDecimal(0.0);
        List<OpsFlightPaxSupplierCommercial> opsFlightPaxSupplierCommercial = opsPaxTypeFareFlightSupplier.getSupplierCommercials();
        for(OpsFlightPaxSupplierCommercial supplierCommercial : opsFlightPaxSupplierCommercial)
        {
            if(supplierCommercial.getCommercialName().equalsIgnoreCase("receivable"))
                supplierCommercialsReceivable = supplierCommercialsReceivable.add(BigDecimal.valueOf(supplierCommercial.getCommercialAmount()));
            else
                supplierCommercialsPayable = supplierCommercialsReceivable.add(BigDecimal.valueOf(supplierCommercial.getCommercialAmount()));
        }

        ProfSupplierCostPrice profSupplierCostPrice = new ProfSupplierCostPrice();
        SellingPrice sellingPrice1 = new SellingPrice();
        sellingPrice1.setBasFare(suppBaseFareAmt.doubleValue());
        sellingPrice1.setFees(setFees);
        sellingPrice1.setTotalFees(totalSuppFees);
        sellingPrice1.setTotalTaxesAmt(totalSuppTaxes);
        sellingPrice1.setTaxes(taxMap);
        profSupplierCostPrice.setGrossSupplierCost(sellingPrice1);

        profSupplierCostPrice.setSupplierCommercialsReceivable(supplierCommercialsReceivable);
        profSupplierCostPrice.setSupplierCommercialsPayable(supplierCommercialsPayable);
        profSupplierCostPrice.setTotalNetPayableToSupplier(supplierTotalPrice.add(supplierCommercialsPayable).subtract(supplierCommercialsReceivable));//Subtracting receivables because it is already added in total fare
                                                                                                                                                //Adding payables because it is NOT added in total fare
//-----------X------------------------------------X------------------------------------X-------------------------------------X------------------------X--------------------------X--------------------------X---
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


        BudgetedProfitability budgetedProfitability = new BudgetedProfitability();
        budgetedProfitability.setProfMargin(profMargin);
        budgetedProfitability.setProfSellingPrice(profSellingPrice);
        budgetedProfitability.setProfSupplierCostPrice(profSupplierCostPrice);

        OperationalProfitability operationalProfitability = new OperationalProfitability();
        operationalProfitability.setProfMargin(profMargin);
        operationalProfitability.setProfSellingPrice(profSellingPrice);
        operationalProfitability.setProfSupplierCostPrice(profSupplierCostPrice);

        FinalProfitability finalProfitability = new FinalProfitability();
        finalProfitability.setProfMargin(profMargin);
        finalProfitability.setProfSellingPrice(profSellingPrice);
        finalProfitability.setProfSupplierCostPrice(profSupplierCostPrice);
        FileProfitabilityBooking fileProfitabilityBooking1 = new FileProfitabilityBooking();

        fileProfitabilityBooking1.setFinaFileProf(finalProfitability);
        fileProfitabilityBooking1.setOperationalFileProf(operationalProfitability);

        //adding company details
        fileProfitabilityBooking1.setBU(fileProfitabilityBooking.getBU());
        fileProfitabilityBooking1.setSBU(fileProfitabilityBooking.getSBU());
        fileProfitabilityBooking1.setCompanyName(fileProfitabilityBooking.getCompanyName());
        fileProfitabilityBooking1.setCompanyId(fileProfitabilityBooking.getCompanyId());
        fileProfitabilityBooking1.setGroupOfCompanyName(fileProfitabilityBooking.getGroupOfCompanyName());
        fileProfitabilityBooking1.setGroupOfCompanyId(fileProfitabilityBooking.getGroupOfCompanyId());
        fileProfitabilityBooking1.setCompanyGroupName(fileProfitabilityBooking.getCompanyGroupName());
        fileProfitabilityBooking1.setCompanyGroupId(fileProfitabilityBooking.getCompanyGroupId());

        if (fileProfType != null) {
            FileProfSearchCriteria fileProfBookingCriteria = new FileProfSearchCriteria();
            fileProfBookingCriteria.setIsTransportation(true);
            fileProfBookingCriteria.setOrderId(fileProfitabilityBooking.getOrderId());
            fileProfBookingCriteria.setBookingRefNumber(fileProfitabilityBooking.getBookingReferenceNumber());
            List<FileProfitabilityBooking> fileProfitabilityBookDb = fileProfitabilityModifiedRepository.getFileProfBookByCriteria(fileProfBookingCriteria);
            FileProfitabilityBooking fielProfAfterCalc = null;
            if (fileProfType.equals(FileProfTypes.OPERATIONAL_PROFITABILITY)) {
                if (fileProfitabilityBookDb != null) {
                    //fileProfitabilityBookDb.setOperationalFileProf(operationalProfitability);
                    for (FileProfitabilityBooking fileProfitabilityBooking2 : fileProfitabilityBookDb) {
                        fielProfAfterCalc = updatedPricing.pricingComparison(fileProfitabilityBooking1, fileProfitabilityBooking2, FileProfTypes.OPERATIONAL_PROFITABILITY);
                        if (fielProfAfterCalc != null)
                            fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(fielProfAfterCalc);
                    }
                }

            } else if (fileProfType.equals(FileProfTypes.FINAL_PROFITABILITY)) {
                if (fileProfitabilityBookDb != null) {
                    // fileProfitabilityBookDb.setFinaFileProf(finalProfitability);
                    for (FileProfitabilityBooking fileProfitabilityBooking2 : fileProfitabilityBookDb) {
                        fielProfAfterCalc = updatedPricing.pricingComparison(fileProfitabilityBooking1, fileProfitabilityBooking2, FileProfTypes.FINAL_PROFITABILITY);
                        if (fielProfAfterCalc != null)
                            fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(fielProfAfterCalc);
                    }
                }
            }

        } else {

            fileProfitabilityBooking1.setBookingType(FileProfTypes.PASSENGER_WISE);
            fileProfitabilityBooking1.setPaxBreakDown(paxBreakDown);
            fileProfitabilityBooking1.setBookingDateZDT(fileProfitabilityBooking.getBookingDateZDT());
            fileProfitabilityBooking1.setBookingReferenceNumber(fileProfitabilityBooking.getBookingReferenceNumber());
            fileProfitabilityBooking1.setOrderId(fileProfitabilityBooking.getOrderId());
            fileProfitabilityBooking1.setClientName(fileProfitabilityBooking.getClientName());
            fileProfitabilityBooking1.setClientType(fileProfitabilityBooking.getClientType());
            fileProfitabilityBooking1.setClientId(fileProfitabilityBooking.getClientId());
            // fileProfitabilityBooking1.setPaxBreakDown(fileProfitabilityBooking.getPaxBreakDown());
            fileProfitabilityBooking1.setBudgetedFileProf(budgetedProfitability);
            fileProfitabilityBooking1.setOperationalFileProf(operationalProfitability);
            fileProfitabilityBooking1.setFinaFileProf(finalProfitability);
            fileProfitabilityBooking1.setPassengerwise(true);
            fileProfitabilityBooking1.setProductName(fileProfitabilityBooking.getProductName());
            Variance variance = new Variance();
            variance.setBudgetedVsFinal(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
            variance.setBudgetedVsOperational(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(operationalProfitability.getProfMargin().getNetMarginAmt()));
            variance.setOperationalVsFinal(operationalProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
            fileProfitabilityBooking1.setVariance(variance);
            fileProfitabilityBooking1.setProductCategory(fileProfitabilityBooking.getProductCategory());
            fileProfitabilityBooking1.setProductSubCategory(fileProfitabilityBooking.getProductSubCategory());
            fileProfitabilityBooking1.setDestinationLocation(fileProfitabilityBooking.getDestinationLocation());
            fileProfitabilityBooking1.setDepartureLocation(fileProfitabilityBooking.getDepartureLocation());
            fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(fileProfitabilityBooking1);
        }
    }
}
