package com.coxandkings.travel.operations.service.FileProfitability.impl;

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
import java.util.List;
import java.util.Map;


@Service
public class AccoProfitabilityHandler {


    private static final Logger logger = Logger.getLogger(AccoProfitabilityHandler.class);

    @Autowired
    FileProfitabilityModifiedRepository fileProfitabilityModifiedRepository;

    public void computeOperationalProfitability(OpsProduct opsProduct, List<FileProfitabilityBooking> profitabilityBookings) {

        try {

            OpsOrderDetails opsOrderDetails = opsProduct.getOrderDetails();
            OpsHotelDetails opsHotelDetails = opsOrderDetails.getHotelDetails();

            for(FileProfitabilityBooking profitabilityBooking : profitabilityBookings)
            {


                //SELLING PRICE CALCULATION STARTS HERE


                OpsAccommodationTotalPriceInfo totalPriceInfo = opsHotelDetails.getOpsAccommodationTotalPriceInfo();
                BigDecimal clientTotalPrice =BigDecimal.valueOf(Double.valueOf(totalPriceInfo.getTotalPrice()));

//-------------------------------------------TAXES---------------------------------------------------------------------------
                Map<String, BigDecimal> clientTaxMap = new HashMap<>();
                OpsTaxes totalTaxes = totalPriceInfo.getOpsTaxes();

                List<OpsTax> clientTaxList = totalTaxes.getTax();
                for(OpsTax opsTax : clientTaxList){
                    if(clientTaxMap.containsKey(opsTax.getTaxCode()))
                        clientTaxMap.put(opsTax.getTaxCode(), clientTaxMap.get(opsTax.getTaxCode()).add(BigDecimal.valueOf(opsTax.getAmount())));
                    else
                        clientTaxMap.put(opsTax.getTaxCode(), BigDecimal.valueOf(opsTax.getAmount()));
                }

                BigDecimal clientTotalTaxAmt = BigDecimal.valueOf(totalTaxes.getAmount());
//------------------------------------------COMPANY TAXES----------------------------------------------------------------------

                OpsCompanyTaxes clientCompanyTaxes = opsHotelDetails.getRooms().get(0).getRoomTotalPriceInfo().getCompanyTaxes();
                List<OpsCompanyTax> companyTaxList = clientCompanyTaxes.getCompanyTax();

                for(OpsCompanyTax companyTax : companyTaxList)
                {
                    if(clientTaxMap.containsKey(companyTax.getTaxCode()))
                        clientTaxMap.put(companyTax.getTaxCode(), clientTaxMap.get(companyTax.getTaxCode()).add(companyTax.getAmount()));
                    else
                        clientTaxMap.put(companyTax.getTaxCode(), companyTax.getAmount());
                }

                clientTotalTaxAmt = clientTotalTaxAmt.add(clientCompanyTaxes.getAmount());

//-------------------------------------------------------------RECEIVABLES AND PAYABLES------------------------------------------------------------------

                BigDecimal clientReceivables = new BigDecimal(0.0), clientPayables = new BigDecimal(0.0);
                List<OpsOrderClientCommercial> clientCommercials = opsOrderDetails.getClientCommercials();
                for(OpsOrderClientCommercial clientCommercial : clientCommercials)
                {
                    if(clientCommercial.getCommercialName().equalsIgnoreCase("Receivable"))
                        clientReceivables = clientReceivables.add(BigDecimal.valueOf(Double.valueOf(clientCommercial.getCommercialAmount())));
                    else
                        clientPayables = clientPayables.add(BigDecimal.valueOf(Double.valueOf(clientCommercial.getCommercialAmount())));
                }

//-------------------------------------------------------------------------------------------------------------------------------
                ProfSellingPrice profSellingPrice = new ProfSellingPrice();
                SellingPrice clientSellingPrice = new SellingPrice();
                clientSellingPrice.setBasFare(clientTotalPrice.subtract(clientTotalTaxAmt).doubleValue());
                clientSellingPrice.setTaxes(clientTaxMap);
                clientSellingPrice.setTotalTaxesAmt(clientTotalTaxAmt);
                profSellingPrice.setSellingPrice(clientSellingPrice);

                TotalInCompanyMarketCurrency totalInCompanyMarketCurrency = new TotalInCompanyMarketCurrency();
                totalInCompanyMarketCurrency.setBasFare(clientTotalPrice.subtract(clientTotalTaxAmt).doubleValue());
                totalInCompanyMarketCurrency.setTaxes(clientTaxMap);
                totalInCompanyMarketCurrency.setTotalTaxesAmt(clientTotalTaxAmt);
                profSellingPrice.setTotalInCompanyMarketCurrency(totalInCompanyMarketCurrency);

                profSellingPrice.setClientCPUpdated(true);
                profSellingPrice.setClientCRUpdated(true);
                profSellingPrice.setClientCommercialsPayable(clientPayables);
                profSellingPrice.setClientCommercialsReceivable(clientReceivables); //Booking engine does not send base price right now only total price and taxes
                profSellingPrice.setTotalNetSellingPrice(clientTotalPrice.add(clientPayables).subtract(clientReceivables));



                //SUPPLIER COST PRICE CALCULATION STARTS HERE


                OpsAccoOrderSupplierPriceInfo supplierPriceInfo = opsHotelDetails.getOpsAccoOrderSupplierPriceInfo();
                BigDecimal totalSupplierPrice = BigDecimal.valueOf(Double.valueOf(supplierPriceInfo.getSupplierPrice()));

//--------------------------------------------------------TAXES---------------------------------------------------------------------------
                Map<String, BigDecimal> supplierTaxMap = new HashMap<>();

                OpsTaxes supplierTaxes = supplierPriceInfo.getTaxes();
                List<OpsTax> supplierTaxList = supplierTaxes.getTax();

                for(OpsTax opsTax : supplierTaxList){
                    if(supplierTaxMap.containsKey(opsTax.getTaxCode()))
                        supplierTaxMap.put(opsTax.getTaxCode(), supplierTaxMap.get(opsTax.getTaxCode()).add(BigDecimal.valueOf(opsTax.getAmount())));
                    else
                        supplierTaxMap.put(opsTax.getTaxCode(), BigDecimal.valueOf(opsTax.getAmount()));
                }
                BigDecimal supplierTotalTax = BigDecimal.valueOf(supplierTaxes.getAmount());

//------------------------------------------------------RECEIVABLES AND PAYABLES---------------------------------------------------------------------------
                BigDecimal supplierReceivables = new BigDecimal(0.0), supplierPayables = new BigDecimal(0.0);
                List<OpsOrderSupplierCommercial> supplierCommercials = opsOrderDetails.getSupplierCommercials();
                for(OpsOrderSupplierCommercial supplierCommercial : supplierCommercials){

                    if(supplierCommercial.getCommercialType().equalsIgnoreCase("Receivable"))
                        supplierReceivables = supplierReceivables.add(BigDecimal.valueOf(Double.valueOf(supplierCommercial.getCommercialAmount())));
                    else
                        supplierPayables = supplierPayables.add(BigDecimal.valueOf(Double.valueOf(supplierCommercial.getCommercialAmount())));
                }

//-------------------------------------------------------------------------------------------------------------------------------------
                ProfSupplierCostPrice profSupplierCostPrice = new ProfSupplierCostPrice();
                profSupplierCostPrice.setSupplierCPUpdated(true);
                profSupplierCostPrice.setSupplierCRUpdated(true);
                profSupplierCostPrice.setSupplierCommercialsReceivable(supplierReceivables);
                profSupplierCostPrice.setSupplierCommercialsPayable(supplierPayables);
                profSupplierCostPrice.setTotalNetPayableToSupplier(supplierTotalTax.add(supplierPayables).subtract(supplierReceivables));

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




        }catch (Exception e){

        }

    }
}
