package com.coxandkings.travel.operations.service.FileProfitability.impl;

import com.coxandkings.travel.operations.enums.FileProfitability.FileProfTypes;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.FileProfitabilityBooking;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.FinalProfitability;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.OperationalProfitability;
import com.coxandkings.travel.operations.resource.pricedetails.ROE;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UpdatedPricing {
    @Autowired
    private MDMRestUtils mdmRestUtils;
    @Value("${mdm.get-supplier-roe}")
    private String supplierROEUrl;
    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    public FileProfitabilityBooking pricingComparison(FileProfitabilityBooking updated, FileProfitabilityBooking fromDb, FileProfTypes type) {
        List<String> feeUpdated = new ArrayList<>();
        List<String> taxesUpdated = new ArrayList<>();
        Map<String, BigDecimal> feesDb = null;
        Map<String, BigDecimal> feesNew = null;
        Map<String, BigDecimal> taxesDb = null;
        Map<String, BigDecimal> taxesNew = null;
        if (type.equals(FileProfTypes.OPERATIONAL_PROFITABILITY)) {
            OperationalProfitability operationalProfitabilityUpd = updated.getOperationalFileProf();
            OperationalProfitability operationalProfitabilityDb = fromDb.getOperationalFileProf();

            if (operationalProfitabilityUpd.getProfSellingPrice().getSellingPrice().getBasFare() != operationalProfitabilityDb.getProfSellingPrice()
                    .getSellingPrice().getBasFare()) {
                operationalProfitabilityDb.getProfSellingPrice().getSellingPrice().setBaseFareUpdated(true);
            }
            feesDb = operationalProfitabilityDb.getProfSellingPrice().getSellingPrice().getFees();
            feesNew = operationalProfitabilityUpd.getProfSellingPrice().getSellingPrice().getFees();
            taxesDb = operationalProfitabilityDb.getProfSellingPrice().getSellingPrice().getTaxes();
            taxesNew = operationalProfitabilityUpd.getProfSellingPrice().getSellingPrice().getTaxes();
            if (!feesDb.isEmpty() && !feesNew.isEmpty() && feesDb.size() >= 1 && feesNew.size() >= 1) {
                for (Map.Entry<String, BigDecimal> entryDb : feesDb.entrySet()) {
                    System.out.println("Key = " + entryDb.getKey() +
                            ", Value = " + entryDb.getValue());

                    for (Map.Entry<String, BigDecimal> entryNew : feesNew.entrySet()) {
                        if (entryDb.getKey().equalsIgnoreCase(entryNew.getKey()) && entryDb.getValue() != entryNew.getValue()) {
                            feeUpdated.add(entryDb.getKey());
                        }
                    }
                }
            }
            if (!taxesDb.isEmpty() && taxesDb.size() >= 1 && !taxesNew.isEmpty() && taxesNew.size() >= 1) {
                for (Map.Entry<String, BigDecimal> taxesItr : taxesDb.entrySet()) {

                    for (Map.Entry<String, BigDecimal> taxesNewItr : taxesNew.entrySet()) {
                        if (taxesItr.getKey().equalsIgnoreCase(taxesNewItr.getKey()) && taxesItr.getValue() != taxesNewItr.getValue()) {
                            taxesUpdated.add(taxesItr.getKey());
                        }
                    }

                }
            }
            if (feeUpdated.size() >= 1) {
                operationalProfitabilityDb.getProfSellingPrice().getSellingPrice().setFeeUpdated(true);
                operationalProfitabilityDb.getProfSellingPrice().getSellingPrice().setTotalFeeUpdated(true);
                operationalProfitabilityDb.getProfSellingPrice().setTotalNSPriceUpdated(true);
                operationalProfitabilityDb.getProfSellingPrice().getSellingPrice().setModifiedFees(feeUpdated);
            }
            if (taxesUpdated.size() >= 1) {
                operationalProfitabilityDb.getProfSellingPrice().getSellingPrice().setTaxesUpdated(true);
                operationalProfitabilityDb.getProfSellingPrice().getSellingPrice().setTotalTaxesAmtUpdated(true);
                operationalProfitabilityDb.getProfSellingPrice().setTotalNSPriceUpdated(true);
                operationalProfitabilityDb.getProfSellingPrice().getSellingPrice().setModifiedTaxes(taxesUpdated);
            }

            // supplier-Cost-Price comparison

            if (operationalProfitabilityDb.getProfSupplierCostPrice().getSupplierCommercialsPayable() != operationalProfitabilityUpd.getProfSupplierCostPrice().getSupplierCommercialsPayable()) {
                operationalProfitabilityDb.getProfSupplierCostPrice().setSupplierCPUpdated(true);
                operationalProfitabilityDb.getProfSupplierCostPrice().setTotNetPayToSuppUpdated(true);
                operationalProfitabilityDb.getProfMargin().setMarginUpdated(true);

            }
            if (operationalProfitabilityDb.getProfSupplierCostPrice().getSupplierCommercialsReceivable() != operationalProfitabilityUpd.getProfSupplierCostPrice().getSupplierCommercialsReceivable()) {
                operationalProfitabilityDb.getProfSupplierCostPrice().setSupplierCRUpdated(true);
                operationalProfitabilityDb.getProfSupplierCostPrice().setTotNetPayToSuppUpdated(true);
                operationalProfitabilityDb.getProfMargin().setMarginUpdated(true);
            }


            fromDb.setOperationalFileProf(operationalProfitabilityDb);
            return fromDb;
        } else if (type.equals(FileProfTypes.FINAL_PROFITABILITY)) {

            //finalProfComp(fromDb.getFinaFileProf(), updated.getFinaFileProf());
            fromDb.setFinaFileProf(finalProfComp(fromDb.getFinaFileProf(), updated.getFinaFileProf()));
            return fromDb;
        } else {
            return null;
        }
    }


    public FinalProfitability finalProfComp(FinalProfitability fromDb, FinalProfitability updatedObj) {

        List<String> feeUpdated = new ArrayList<>();
        List<String> taxesUpdated = new ArrayList<>();


        Map<String, BigDecimal> feesDb = null;
        Map<String, BigDecimal> feesNew = null;
        Map<String, BigDecimal> taxesDb = null;
        Map<String, BigDecimal> taxesNew = null;
        FinalProfitability operationalProfitabilityUpd = null;
        FinalProfitability operationalProfitabilityDb = null;
        if (fromDb != null && updatedObj != null) {

            operationalProfitabilityUpd = updatedObj;
            operationalProfitabilityDb = fromDb;

            if (operationalProfitabilityUpd.getProfSellingPrice().getSellingPrice().getBasFare() != operationalProfitabilityDb.getProfSellingPrice()
                    .getSellingPrice().getBasFare()) {
                operationalProfitabilityDb.getProfSellingPrice().getSellingPrice().setBaseFareUpdated(true);
            }
            feesDb = operationalProfitabilityDb.getProfSellingPrice().getSellingPrice().getFees();
            feesNew = operationalProfitabilityUpd.getProfSellingPrice().getSellingPrice().getFees();
            taxesDb = operationalProfitabilityDb.getProfSellingPrice().getSellingPrice().getTaxes();
            taxesNew = operationalProfitabilityUpd.getProfSellingPrice().getSellingPrice().getTaxes();

            for (Map.Entry<String, BigDecimal> entryDb : feesDb.entrySet()) {
                System.out.println("Key = " + entryDb.getKey() +
                        ", Value = " + entryDb.getValue());

                for (Map.Entry<String, BigDecimal> entryNew : feesNew.entrySet()) {
                    if (entryDb.getKey().equalsIgnoreCase(entryNew.getKey()) && entryDb.getValue() != entryNew.getValue()) {
                        feeUpdated.add(entryDb.getKey());
                    }
                }
            }


            for (Map.Entry<String, BigDecimal> taxesItr : taxesDb.entrySet()) {

                for (Map.Entry<String, BigDecimal> taxesNewItr : taxesNew.entrySet()) {
                    if (taxesItr.getKey().equalsIgnoreCase(taxesNewItr.getKey()) && taxesItr.getValue() != taxesNewItr.getValue()) {
                        taxesUpdated.add(taxesItr.getKey());
                    }
                }

            }

            if (feeUpdated.size() >= 1) {
                operationalProfitabilityDb.getProfSellingPrice().getSellingPrice().setFeeUpdated(true);
                operationalProfitabilityDb.getProfSellingPrice().setTotalNSPriceUpdated(true);
            }
            if (taxesUpdated.size() >= 1) {
                operationalProfitabilityDb.getProfSellingPrice().getSellingPrice().setTaxesUpdated(true);
                operationalProfitabilityDb.getProfSellingPrice().setTotalNSPriceUpdated(true);
            }
            // supplier-Cost-Price comparison

            if (operationalProfitabilityDb.getProfSupplierCostPrice().getSupplierCommercialsPayable() != operationalProfitabilityUpd.getProfSupplierCostPrice().getSupplierCommercialsPayable()) {
                operationalProfitabilityDb.getProfSupplierCostPrice().setSupplierCPUpdated(true);
                operationalProfitabilityDb.getProfSupplierCostPrice().setTotNetPayToSuppUpdated(true);
                operationalProfitabilityDb.getProfMargin().setMarginUpdated(true);

            }
            if (operationalProfitabilityDb.getProfSupplierCostPrice().getSupplierCommercialsReceivable() != operationalProfitabilityUpd.getProfSupplierCostPrice().getSupplierCommercialsReceivable()) {
                operationalProfitabilityDb.getProfSupplierCostPrice().setSupplierCRUpdated(true);
                operationalProfitabilityDb.getProfSupplierCostPrice().setTotNetPayToSuppUpdated(true);
                operationalProfitabilityDb.getProfMargin().setMarginUpdated(true);
            }


        }

        return operationalProfitabilityDb;


    }
 /* @Autowired
    private ObjectMapper mapper;

    public <T> T convertToT(Object object, Class<T> responseType) {
        if (logger.isDebugEnabled()) {
            logger.debug
                    ("object = [" + object + "], responseType = [" + responseType + "]");
        }
        T t = null;
        try {
            t = this.mapper.readValue(object.toString(), responseType);
        } catch (IOException e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.toString());
            }
        } catch (Exception e) {
        }
        return t;
    }*/


    public BigDecimal getRoe(String supplierCurrency, String remittanceCurrency, String dateOfPayment, String supplierId, String roeType) throws OperationException {
        BigDecimal roeValue = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ROE.fromCurrency", supplierCurrency);
        jsonObject.put("ROE.toCurrency", remittanceCurrency);
        jsonObject.put("roeType", roeType);
        JSONArray dateJson = new JSONArray();
        JSONObject effectiveFrom = new JSONObject();
        JSONObject lteEffectiveFrom = new JSONObject();
        lteEffectiveFrom.put("$lte", dateOfPayment);
        effectiveFrom.put("effectiveFrom", lteEffectiveFrom);

        JSONObject effectiveTo = new JSONObject();
        JSONObject gteEffectiveTo = new JSONObject();
        gteEffectiveTo.put("$gte", dateOfPayment);
        effectiveTo.put("effectiveTo", gteEffectiveTo);

        dateJson.put(effectiveFrom);
        dateJson.put(effectiveTo);

        jsonObject.put("$and", dateJson);
        jsonObject.put("supplier.id", supplierId);


        String response = null;
        try {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(this.supplierROEUrl + "?filter=" + jsonObject);
            URI uri = URI.create(uriComponentsBuilder.toUriString());

            response = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);

            List<ROE> roeList = jsonObjectProvider.getChildrenCollection(response, "$.data[0].ROE.*", ROE.class);
            if (roeList == null) {
                throw new OperationException("Unable to get ROE from mdm");
            }
            for (ROE roe : roeList) {
                if (roe.getFromCurrency().equalsIgnoreCase(supplierCurrency) && roe.getToCurrency().equalsIgnoreCase(remittanceCurrency)) {
                    roeValue = roe.getRoe();
                    break;
                }
            }
        } catch (Exception e) {
            throw new OperationException("Unable to get ROE from mdm");
        }

        return roeValue;
    }




}
