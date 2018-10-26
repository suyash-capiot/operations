package com.coxandkings.travel.operations.service.FileProfitability.impl;

import com.coxandkings.travel.ext.model.be.BookingActionConstants;
import com.coxandkings.travel.ext.model.be.Incentives;
import com.coxandkings.travel.operations.criteria.fileprofitability.FileProfSearchCriteria;
import com.coxandkings.travel.operations.criteria.fileprofitability.FileProfSearchReportCriteria;
import com.coxandkings.travel.operations.enums.FileProfitability.FileProfTypes;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.commercialstatements.*;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.*;
import com.coxandkings.travel.operations.repository.commercialstatements.SupplierCommercialStatementRepo;
import com.coxandkings.travel.operations.repository.commercialstatements.impl.ClientCommercialStatementRepoImpl;
import com.coxandkings.travel.operations.repository.commercialstatements.impl.SupplierCommercialStatementRepoImpl;
import com.coxandkings.travel.operations.repository.fileProfitabilityModified.FileProfitabilityModifiedRepository;
import com.coxandkings.travel.operations.resource.FileProfitabilityModified.Client;
import com.coxandkings.travel.operations.service.FileProfitability.FileProfitabilityService;
import com.coxandkings.travel.operations.service.booking.impl.OpsBookingServiceImpl;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileProfitabilityServiceImpl implements FileProfitabilityService {
    private static final Logger logger = Logger.getLogger(FileProfitabilityServiceImpl.class);
    @Autowired
    PassWiseFileProfitabilityDivision passWiseFileProfitabilityDivision;
    @Autowired
    FileProfitabilityModifiedRepository fileProfitabilityModifiedRepository;
    @Autowired
    UpdatedPricing updatedPricing;
    @Autowired
    OperationalService operationalService;
 /*   @Autowired
    FileProfitabilityService fileProfitabilityService;*/
 /*   @Autowired
    OpsBookingAdapter opsBookingAdapter;*/
    @Autowired
    com.coxandkings.travel.operations.systemlogin.MDMToken mdmToken;
    @Value(value = "${file-profitability.client.getClientB2B}")
    private String getClientB2BUrl;
    @Value(value = "${file-profitability.client.getClientB2C}")
    private String getClientB2CUrl;
    @Value(value = "${file-profitability.client.B2B}")
    private String getClientB2BFilter;
    @Value(value = "${file-profitability.client.B2C}")
    private String getClientB2CFilter;
    @Autowired
    OpsBookingServiceImpl opsBookingService;
    @Autowired
    MDMRestUtils mdmRestUtils;
/*    @Autowired
    private JsonObjectProvider jsonFilter;*/
    @Value(value = "${mdm.company}")
    private String company_url;

    @Autowired
    SupplierCommercialStatementRepoImpl supplierCommercialStatementRepo;
    @Autowired
    ClientCommercialStatementRepoImpl clientCommercialStatementRepo;

    public void opsBookingFromKafka(KafkaBookingMessage kafkaBookingMessage) throws OperationException {
        logger.info("in opsBookingFromKafka() in FileProfitabilityServiceImpl class");
        OpsBooking opsBookings = null;
        String customerAmtCurrency = null;
       /* if (kafkaBookingMessage != null && kafkaBookingMessage.getActionType().equalsIgnoreCase(BookingActionConstants.JSON_PROP_NEW_BOOKING)) {
            logger.info("in opsBookingFromKafka() - checking kafkaMessage data " + kafkaBookingMessage.getBookId());

            opsBookings = opsBookingService.getBooking(kafkaBookingMessage.getBookId());
        }*/

       /* try {
            opsBookings = opsBookingService.getBooking("B2C-2018-241018162102");
        }catch(Exception e)
        {
            e.printStackTrace();
        }*/
//----------X_------------------X-----------------X---------------Operaional Calculations----------------X-------------X--------------X-------------X
        FileProfSearchCriteria operationalCheckObj = new FileProfSearchCriteria();
        operationalCheckObj.setBookingRefNumber(kafkaBookingMessage.getBookId());
//        operationalCheckObj.setBookingRefNumber("B2C-2018-241018162102");
        List<FileProfitabilityBooking> fileProfitabilityBookings = fileProfitabilityModifiedRepository.getListOfFileProfsWRTCriteria(operationalCheckObj);
        if(fileProfitabilityBookings!=null && fileProfitabilityBookings.size()>0) {
            operationalService.computeOperationalCalculations(opsBookings, fileProfitabilityBookings);
            return; // no need for further calculations
        }
//----------X_------------------X-----------------X---------------X---------------X-----------------X---------------X_---------------------X-----------------------X
        //opsBookings.getBookingDateZDT().toString();
        //http://10.25.6.103:10050/client/v1/clientB2B/B2BCLIENT126?select=clientProfile.orgHierarchy.companyMarkets
        FileProfitabilityBooking fileProfitabilityBooking = new FileProfitabilityBooking();
        List<OpsProduct> opsProductList = new ArrayList<OpsProduct>();
        if (opsBookings != null) {
            List<OpsPaymentInfo> opsPaymentInfos = opsBookings.getPaymentInfo();
            if (opsPaymentInfos != null && !opsPaymentInfos.isEmpty()) {
                for (OpsPaymentInfo opsPaymentInfo : opsPaymentInfos) {
                    customerAmtCurrency = opsPaymentInfo.getAmountCurrency();
                }
            }

            logger.info("in opsBookingFromKafka() - opsBookings are not null ");
            fileProfitabilityBooking.setBookingReferenceNumber(opsBookings.getBookID());
            fileProfitabilityBooking.setClientId(opsBookings.getClientID());
            fileProfitabilityBooking.setBookingDateZDT(opsBookings.getBookingDateZDT());
            fileProfitabilityBooking.setClientType(opsBookings.getClientType());
            opsProductList = opsBookings.getProducts();
        }

        //Added bu,sbu and companyid
        fileProfitabilityBooking.setBU(opsBookings.getBu());
        fileProfitabilityBooking.setSBU(opsBookings.getSbu());
        fileProfitabilityBooking.setCompanyId(opsBookings.getCompanyId());
        setCompanyDetails(fileProfitabilityBooking);

        if (!opsProductList.isEmpty() && opsProductList != null) {
            logger.info("in opsBookingFromKafka() - opsProductList are not null ");
            for (OpsProduct opsProduct : opsProductList) {
                if (opsProduct.getProductCategory().equalsIgnoreCase(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.getCategory())) {

                    Integer adultCount = 0, childCount = 0;
                    BigDecimal baseFare = new BigDecimal(0), taxesAmt = new BigDecimal(0), clientCommercialsPayable = new BigDecimal(0), clientCommercialsReceivable = new BigDecimal(0), receivablesTotAmt = new BigDecimal(0);
                    BigDecimal supplierCommercialsReceivable = new BigDecimal(0), supplierCommercialsPayable = new BigDecimal(0);
                    BigDecimal percentage = new BigDecimal(0), forPercentage = new BigDecimal(0), feeAmt = new BigDecimal(0);
                    fileProfitabilityBooking.setOrderId(opsProduct.getOrderID());
                    fileProfitabilityBooking.setProductName(opsProduct.getSourceSupplierName());
                    String uri = null;
                    if (opsBookings.getClientType().equalsIgnoreCase("B2B")) {
                        uri = getClientB2BUrl + opsBookings.getClientID() + getClientB2BFilter;
                        logger.info("in opsBookingFromKafka() - B2B Client URL: " + uri);

                    }
                    if (opsBookings.getClientType().equalsIgnoreCase("B2C")) {
                        uri = getClientB2CUrl + opsBookings.getClientID() + getClientB2CFilter;
                        logger.info("in opsBookingFromKafka() - B2C Client URL: " + uri);
                    }


                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", mdmToken.getToken());
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
                    ResponseEntity<Client> result = null;
                    try {
                        result = restTemplate.exchange(uri, HttpMethod.GET, entity, Client.class);
                    } catch (Exception e) {
                        StringWriter stack = new StringWriter();
                        e.printStackTrace(new PrintWriter(stack));
                        logger.debug("Caught exception : " + stack.toString());
                    }
                    Client client = null;
                    if (result != null)
                        client = result.getBody();
                    if (client != null && client.getClientProfile() != null && client.getClientProfile().getClientDetails() != null && client.getClientProfile().getClientDetails().getClientName() != null)
                        fileProfitabilityBooking.setClientName(client.getClientProfile().getClientDetails().getClientName());
                    OpsOrderDetails opsOrderDetails = opsProduct.getOrderDetails();
                    OpsFlightDetails opsFlightDetails = opsOrderDetails.getFlightDetails();
                    OpsFlightSupplierPriceInfo OpsFlightSupplierPriceInfo = null;
                    List<OpsOriginDestinationOption> originDestinationOptions = null;
                    if (opsFlightDetails != null) {
                        OpsFlightSupplierPriceInfo = opsFlightDetails.getOpsFlightSupplierPriceInfo();
                        originDestinationOptions = opsFlightDetails.getOriginDestinationOptions();
                    }
                    if (originDestinationOptions != null) {
                        for (OpsOriginDestinationOption opsOriginDestinationOption : originDestinationOptions) {
                            if (opsOriginDestinationOption != null) {
                                List<OpsFlightSegment> flightSegment = opsOriginDestinationOption.getFlightSegment();
                                if (flightSegment != null) {
                                    for (OpsFlightSegment opsFlightSegment : flightSegment) {
                                        fileProfitabilityBooking.setDepartureLocation(opsFlightSegment.getOriginLocation());
                                        fileProfitabilityBooking.setDestinationLocation(opsFlightSegment.getDestinationLocation());
                                    }
                                }
                            }

                        }
                    }

                    PaxBreakDown paxBreakDown = new PaxBreakDown();
                    if (opsFlightDetails != null) {
                        logger.info("in opsBookingFromKafka() - opsFlightDetails are not null");
                        List<OpsFlightPaxInfo> opsFlightPaxInfos = opsFlightDetails.getPaxInfo();
                        if (!opsFlightPaxInfos.isEmpty() && opsFlightPaxInfos != null) {
                            for (OpsFlightPaxInfo opsFlightPaxInfo : opsFlightPaxInfos) {
                                if (opsFlightPaxInfo.getPaxType().equals("ADT")) {
                                    adultCount++;
                                    paxBreakDown.setPassengerName(opsFlightPaxInfo.getFirstName() + " " + opsFlightPaxInfo.getLastName());
                                } else {
                                    childCount++;
                                    paxBreakDown.setPassengerName(opsFlightPaxInfo.getFirstName() + " " + opsFlightPaxInfo.getLastName());
                                }
                                if (opsFlightPaxInfo.getLeadPax()) {
                                    paxBreakDown.setLeadPassengerName(opsFlightPaxInfo.getFirstName() + " " + opsFlightPaxInfo.getLastName());
                                    fileProfitabilityBooking.setLeadPassName(opsFlightPaxInfo.getFirstName() + " " + opsFlightPaxInfo.getLastName());
                                }
                            }
                        }
                        paxBreakDown.setNoOfchildren(childCount);
                        paxBreakDown.setNoOfAdtls(adultCount);
                    }

                    fileProfitabilityBooking.setPaxBreakDown(paxBreakDown);
                    Map<String, BigDecimal> setFees = new HashMap<>();
                    Map<String, BigDecimal> taxMap = new HashMap<>();
                    List<OpsPaxTypeFareFlightClient> paxTypeFareFlightClientList = null;
                    List<OpsPaxTypeFareFlightSupplier> opsPaxTypeFareFlightSuppliers = null;

                    BigDecimal totalNetSellingPrice = new BigDecimal(0.0);

                    if (opsFlightDetails != null) {

                        logger.info("in opsBookingFromKafka() - opsFlightDetails are not null ");
                        OpsFlightTotalPriceInfo opsFlightTotalPriceInfo = opsFlightDetails.getTotalPriceInfo();
                        if (opsFlightTotalPriceInfo != null) {
                            logger.info("in opsBookingFromKafka() - opsFlightTotalPriceInfo are not null");
                            paxTypeFareFlightClientList = opsFlightTotalPriceInfo.getPaxTypeFares();
                            baseFare = BigDecimal.valueOf(opsFlightTotalPriceInfo.getBaseFare().getAmount());
                            OpsFees opsFeesList = opsFlightTotalPriceInfo.getFees();

                            if (opsFeesList != null) {
                                logger.info("in opsBookingFromKafka() - opsFeesList are not null");
                                List<OpsFee> fee = opsFeesList.getFee();

                                if (fee != null && !fee.isEmpty()) {
                                    logger.info("in opsBookingFromKafka() - opsFeesList are not null");
                                    for (OpsFee opsFee : fee) {
                                        if (opsFee != null) {

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


                                for (BigDecimal bd : setFees.values()) {
                                    feeAmt = feeAmt.add(bd);
                                }
                                //Added By Ashley
                                feeAmt = opsFeesList.getTotal()!=null?BigDecimal.valueOf(opsFeesList.getTotal()) : new BigDecimal(0.0);
                            }


                            OpsTaxes taxes = opsFlightTotalPriceInfo.getTaxes();
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
                                //Added By Ashley
                                taxesAmt = taxes.getAmount()!=null?BigDecimal.valueOf(taxes.getAmount()):new BigDecimal(0.0);

                            }
                            //Added By Ashley
                            OpsReceivables opsReceivables = opsFlightTotalPriceInfo.getReceivables();
                            List<OpsOrderClientCommercial> opsClientEntityCommercial = opsOrderDetails.getClientCommercials();

                            if(opsReceivables!=null)
                            {
                                BigDecimal receivableAmt = opsReceivables!=null?BigDecimal.valueOf(opsReceivables.getAmount()):new BigDecimal(0.0);
                                clientCommercialsReceivable = receivableAmt;
                            }
                            //Added By Ashley
                            Incentives incentives = opsFlightTotalPriceInfo.getIncentives();
                            if(incentives!=null)
                            {
                                BigDecimal incentiveAmt = incentives.getAmount()!=null?incentives.getAmount():new BigDecimal(0.0);
                                clientCommercialsPayable = incentiveAmt;
                            }

                            totalNetSellingPrice = BigDecimal.valueOf(Double.valueOf(opsFlightTotalPriceInfo.getTotalPrice()));

//                            List<OpsOrderClientCommercial> opsOrderClientCommercialList = opsOrderDetails.getClientCommercials();
//                            for(OpsOrderClientCommercial opsCommercials : opsOrderClientCommercialList) {
//                                BigDecimal amount = BigDecimal.valueOf(Double.valueOf(opsCommercials.getCommercialAmount()));
//                                if(opsCommercials.getCommercialType().equalsIgnoreCase("Receivable"))
//                                    clientCommercialsReceivable = clientCommercialsReceivable.add(amount);
//                                else
//                                    clientCommercialsPayable = clientCommercialsPayable.add(amount);
//                            }
                        }
                    }
                    ProfSellingPrice profSellingPrice = new ProfSellingPrice();
                    SellingPrice sellingPrice = new SellingPrice();
                    sellingPrice.setBasFare(baseFare.doubleValue());
                    sellingPrice.setFees(setFees);

                    sellingPrice.setTotalFees(feeAmt);
                    sellingPrice.setTotalTaxesAmt(taxesAmt);
                    sellingPrice.setTaxes(taxMap);
                    profSellingPrice.setSellingPrice(sellingPrice);

                    TotalInCompanyMarketCurrency totalInCompanyMarketCurrency = new TotalInCompanyMarketCurrency();
                    totalInCompanyMarketCurrency.setBasFare(baseFare.doubleValue());
                    totalInCompanyMarketCurrency.setFees(setFees);
                    totalInCompanyMarketCurrency.setTotalFees(feeAmt);
                    totalInCompanyMarketCurrency.setTotalTaxesAmt(taxesAmt);
                    totalInCompanyMarketCurrency.setTaxes(taxMap);
                    profSellingPrice.setTotalInCompanyMarketCurrency(totalInCompanyMarketCurrency);

                    profSellingPrice.setClientCPUpdated(false);
                    profSellingPrice.setClientCRUpdated(false);
                    profSellingPrice.setClientCommercialsPayable(clientCommercialsPayable);
                    profSellingPrice.setClientCommercialsReceivable(clientCommercialsReceivable);
                    profSellingPrice.setDiscountsOffers(new BigDecimal(0));//need to discuss
                    profSellingPrice.setComissionToClient(new BigDecimal(0));//need to Discuss
                    // double btf = baseFare + taxesAmt + feeAmt;

                    //Changed By ASHLEY
//                    profSellingPrice.setTotalNetSellingPrice((baseFare.add(taxesAmt).add(feeAmt).add(supplierCommercialsReceivable)).subtract(supplierCommercialsPayable));
                    profSellingPrice.setTotalNetSellingPrice(totalNetSellingPrice.add(clientCommercialsPayable).subtract(clientCommercialsReceivable));

                    BigDecimal suppBaseFare = BigDecimal.valueOf(Double.valueOf(opsOrderDetails.getFlightDetails().getOpsFlightSupplierPriceInfo().getSupplierPrice()));
                    ProfSupplierCostPrice profSupplierCostPrice = new ProfSupplierCostPrice();
                    SellingPrice sellingPrice1 = new SellingPrice();
                    sellingPrice1.setBasFare(suppBaseFare.doubleValue());
//                    sellingPrice1.setFees(setFees);
//                    sellingPrice1.setTotalFees(feeAmt);
//                    sellingPrice1.setTotalTaxesAmt(taxesAmt);
//                    sellingPrice1.setTaxes(taxMap);
                    profSupplierCostPrice.setGrossSupplierCost(sellingPrice1);
                    if (opsOrderDetails != null) {
                        logger.info("in opsBookingFromKafka() - opsOrderDetails are not null ");
                        List<OpsOrderSupplierCommercial> opsOrderSupplierCommercialList = opsOrderDetails.getSupplierCommercials();
                        if (opsOrderSupplierCommercialList != null) {
                            logger.info("in opsBookingFromKafka() - opsOrderSupplierCommercialList are not null ");
                            for (OpsOrderSupplierCommercial opsOrderSupplierCommercial : opsOrderSupplierCommercialList) {
                                BigDecimal foo = BigDecimal.valueOf(Double.parseDouble(opsOrderSupplierCommercial.getCommercialAmount()));
                                if (opsOrderSupplierCommercial.getCommercialType().equals("Receivable")) {
                                    supplierCommercialsReceivable = supplierCommercialsReceivable.add(foo);
                                } else {
                                    supplierCommercialsPayable = supplierCommercialsPayable.add(foo);
                                }
                            }
                        }
                    }
                    opsPaxTypeFareFlightSuppliers = opsFlightDetails.getOpsFlightSupplierPriceInfo().getPaxTypeFares();

                    profSupplierCostPrice.setSupplierCommercialsReceivable(supplierCommercialsReceivable);
                    profSupplierCostPrice.setSupplierCommercialsPayable(supplierCommercialsPayable);
                    profSupplierCostPrice.setTotalNetPayableToSupplier(suppBaseFare.add(supplierCommercialsPayable).subtract(supplierCommercialsReceivable));

                    //   fileProfitabilityType.setProfSupplierCostPrice(profSupplierCostPrice);

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


                    fileProfitabilityBooking.setBudgetedFileProf(budgetedProfitability);
                    fileProfitabilityBooking.setOperationalFileProf(operationalProfitability);
                    fileProfitabilityBooking.setFinaFileProf(finalProfitability);
                    fileProfitabilityBooking.setBookingType(FileProfTypes.TRANSPORTATION);
                    fileProfitabilityBooking.setTranspotation(true);
                    fileProfitabilityBooking.setProductCategory(opsProduct.getProductCategory());
                    fileProfitabilityBooking.setProductSubCategory(opsProduct.getProductSubCategory());

                    Variance variance = new Variance();
                    variance.setBudgetedVsFinal(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
                    variance.setBudgetedVsOperational(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(operationalProfitability.getProfMargin().getNetMarginAmt()));
                    variance.setOperationalVsFinal(operationalProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
                    fileProfitabilityBooking.setVariance(variance);
                    fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(fileProfitabilityBooking);

                    passengerWiseFileProfitability(opsFlightDetails, paxTypeFareFlightClientList, opsPaxTypeFareFlightSuppliers, fileProfitabilityBooking, null);

                }
                if (opsProduct.getProductCategory().equalsIgnoreCase(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION.getCategory())) {
                    accomodationFileProfitability(fileProfitabilityBooking, opsProduct, opsBookings.getBookID(), opsBookings.getClientType(), opsBookings.getClientID(), false, null);
                }
            }
        }

    }

    public void setCompanyDetails(FileProfitabilityBooking fileProfBook) throws OperationException {

        String companyId = fileProfBook.getCompanyId();

        String url = company_url + "?filter=" + new JSONObject().put("_id", fileProfBook.getCompanyId()).toString();
        String companyResponse;
        URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();

        try {
            companyResponse = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
        } catch (Exception e) {
            logger.error("Unable to get company details:" + companyId, e);
            return;
            //TODO: throw an exception
        }

        if (StringUtils.isEmpty(companyResponse)) {
            logger.error("No Company found:" + companyId);
            return;
            //TODO: throw an exception
        }

        JSONObject response = new JSONObject(companyResponse);
        if(response.optJSONArray("data")!=null && response.getJSONArray("data").length()!=0) {
            response = response.getJSONArray("data").getJSONObject(0);
            if(response.has("name"))
                fileProfBook.setCompanyName(response.getString("name"));
            if(response.has("groupCompanyId"))
                fileProfBook.setCompanyGroupId(response.getString("groupCompanyId"));
            if(response.has("groupCompanyName"))
                fileProfBook.setCompanyGroupName(response.getString("groupCompanyName"));
            if(response.has("groupOfCompaniesId"))
                fileProfBook.setGroupOfCompanyId(response.getString("groupOfCompaniesId"));
            if(response.has("groupOfCompaniesName"))
                fileProfBook.setGroupOfCompanyName(response.getString("groupOfCompaniesName"));
        }
    }

    public void accomodationFileProfitability(FileProfitabilityBooking fileProfitabilityBookingParam, OpsProduct opsProduct, String bookId, String clientType, String clientId, boolean roomWise, FileProfTypes fileProfType) {
        logger.info("in accomodationFileProfitability() ");

        Integer adultCount = 0, childCount = 0;
        BigDecimal baseFare = new BigDecimal(0), taxesAmt = new BigDecimal(0), supplierCommercialsPayable = new BigDecimal(0), supplierCommercialsReceivable = new BigDecimal(0);
        BigDecimal percentage = new BigDecimal(0), forPercentage = new BigDecimal(0), feeAmt = new BigDecimal(0);
        List<String> passNames = new ArrayList<String>();
        List<FileProfitabilityBooking> fileProfitabilityBookingList = new ArrayList<FileProfitabilityBooking>();

        FileProfitabilityBooking fileProfitabilityBooking = new FileProfitabilityBooking();
        fileProfitabilityBooking.setOrderId(opsProduct.getOrderID());
        fileProfitabilityBooking.setClientType(clientType);
        fileProfitabilityBooking.setClientId(fileProfitabilityBookingParam.getClientId());
        fileProfitabilityBooking.setProductName(opsProduct.getSourceSupplierName());
        fileProfitabilityBooking.setClientId(fileProfitabilityBookingParam.getClientId());

        if (opsProduct.getOrderDetails() != null && opsProduct.getOrderDetails().getHotelDetails() != null && opsProduct.getOrderDetails().getHotelDetails().getOpsAccommodationTotalPriceInfo() != null) {
            OpsAccommodationTotalPriceInfo opsAccommodationTotalPriceInfo = opsProduct.getOrderDetails().getHotelDetails().getOpsAccommodationTotalPriceInfo();
            if (opsAccommodationTotalPriceInfo != null) {
                String k = opsAccommodationTotalPriceInfo.getTotalPrice();
                baseFare = BigDecimal.valueOf(Double.parseDouble(k));
            }

        }

        //Added bu,sbu and companyid
        fileProfitabilityBooking.setBU(fileProfitabilityBookingParam.getBU());
        fileProfitabilityBooking.setSBU(fileProfitabilityBookingParam.getSBU());
        fileProfitabilityBooking.setCompanyId(fileProfitabilityBookingParam.getCompanyId());
        try {
            setCompanyDetails(fileProfitabilityBooking);
        } catch (OperationException e) {
            logger.debug("Error Occurred while setting Company Details : " + e.toString());
        }

        String uri = null;

        if (clientType.equalsIgnoreCase("B2B")) {
            //uri = getClientB2BUrl + clientId + "?select=clientProfile.clientDetails.clientName";
            uri = getClientB2BUrl + clientId + getClientB2BFilter;
            logger.info("in accomodationFileProfitability() - B2B Uri " + uri);
        }
        if (clientType.equalsIgnoreCase("B2C")) {
            //uri = getClientB2CUrl + clientId + "?select=clientDetails.clientName";
            uri = getClientB2CUrl + clientId + getClientB2CFilter;
            logger.info("in accomodationFileProfitability() - B2c Uri " + uri);
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", mdmToken.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<Client> result = null;
        try {
            result = restTemplate.exchange(uri, HttpMethod.GET, entity, Client.class);
        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.debug("Caught exception; decorating with appropriate status template : " + stack.toString());
        }
        Client client = null;
        if (result != null)
            client = result.getBody();
        // client = searchResult.getBody();
        if (client != null && client.getClientProfile() != null && client.getClientProfile().getClientDetails() != null && client.getClientProfile().getClientDetails().getClientName() != null)
            fileProfitabilityBooking.setClientName(client.getClientProfile().getClientDetails().getClientName());

        HashMap<String, BigDecimal> taxMap = new HashMap<String, BigDecimal>();
        BudgetedProfitability fileProfitabilityType = new BudgetedProfitability();
        PaxBreakDown paxBreakDown = new PaxBreakDown();
        List<OpsRoom> opsRoomList = null;
        if (opsProduct.getOrderDetails() != null && opsProduct.getOrderDetails().getHotelDetails() != null && opsProduct.getOrderDetails().getHotelDetails().getRooms() != null) {

            logger.info("in accomodationFileProfitability() - rooms list is not null ");
            opsRoomList = opsProduct.getOrderDetails().getHotelDetails().getRooms();
            // OpsOrderDetails opsOrderDetails = opsProduct.getOrderDetails();


            if (opsRoomList != null) {
                logger.info("in accomodationFileProfitability() - opsRoomList is not null ");
                for (OpsRoom opsRoom : opsRoomList) {
                    if (opsRoom != null) {
                        List<OpsAccommodationPaxInfo> opsAccomodationPaxInfos = opsRoom.getPaxInfo();
                        if (opsAccomodationPaxInfos != null) {

                            //Added By Ashley
                            List<OpsAccommodationPaxInfo> opsAccommodationPaxInfoSet = opsAccomodationPaxInfos.stream().filter(x -> x.getLeadPax() == true).collect(Collectors.toList());
                            OpsAccommodationPaxInfo leadPaxObj = opsAccommodationPaxInfoSet.get(0);
                            String leadPaxName = String.format("%s %s",leadPaxObj.getFirstName(), leadPaxObj.getLastName());


                            for (OpsAccommodationPaxInfo opsAccomodationPaxInfo : opsAccomodationPaxInfos) {
                                if (opsAccomodationPaxInfo.getPaxType().equals("ADT")) {
                                    adultCount++;
                                    paxBreakDown.setPassengerName(String.format("%s %s",opsAccomodationPaxInfo.getFirstName(),opsAccomodationPaxInfo.getLastName()));

                                    //Added By Ashley
                                    paxBreakDown.setLeadPassengerName(leadPaxName);
                                    fileProfitabilityBooking.setLeadPassName(leadPaxName);
                                } else {
                                    childCount++;
                                    paxBreakDown.setPassengerName(String.format("%s %s",opsAccomodationPaxInfo.getFirstName(),opsAccomodationPaxInfo.getLastName()));

                                    //Added By Ashley
                                    paxBreakDown.setLeadPassengerName(leadPaxName);
                                    fileProfitabilityBooking.setLeadPassName(leadPaxName);
                                }
                                //Commented By Ashley
//                                if (opsAccomodationPaxInfo.getLeadPax()) {
//                                    paxBreakDown.setLeadPassengerName(opsAccomodationPaxInfo.getFirstName() + " " + opsAccomodationPaxInfo.getLastName());
//                                    fileProfitabilityBooking.setLeadPassName(opsAccomodationPaxInfo.getFirstName() + " " + opsAccomodationPaxInfo.getLastName());
//                                }

                            }
                        }

                        /*OpsRoomSuppPriceInfo opsRoomSuppPriceInfo = opsRoom.getRoomSuppPriceInfo();
                        if (opsRoomSuppPriceInfo != null) {
                            logger.info("in accomodationFileProfitability() - opsRoomSuppPriceInfo is not null ");
                            OpsTaxes opsTax = opsRoomSuppPriceInfo.getTaxes();

                            if (opsTax != null) {
                                List<OpsTax> opsTaxList = opsTax.getTax();
                                if (!opsTaxList.isEmpty() && opsTaxList != null) {
                                    for (OpsTax opsTaxDetails : opsTaxList) {

                                        taxesAmt = taxesAmt + opsTaxDetails.getAmount();
                                        if (taxMap.size() != 0) {
                                            if (taxMap.containsKey(opsTaxDetails.getTaxCode())) {
                                                taxMap.put(opsTaxDetails.getTaxCode(), taxMap.get(opsTaxDetails.getTaxCode()).add(new BigDecimal(opsTaxDetails.getAmount())));
                                            } else {
                                                //  double foo = Integer.parseInt(opsTaxDetails.getTaxValue());
                                                taxMap.put(opsTaxDetails.getTaxCode(), new BigDecimal(opsTaxDetails.getAmount()));
                                            }
                                        } else {
                                            taxMap.put(opsTaxDetails.getTaxCode(), new BigDecimal(opsTaxDetails.getAmount()));
                                        }
                                    }
                                }


                            }
                        }*/

                    }

                }
            }

        }
        BigDecimal receivables = new BigDecimal(0.0),payables = new BigDecimal(0.0);
        OpsOrderDetails opsOrderDetails = opsProduct.getOrderDetails();
        List<OpsOrderClientCommercial> opsOrderClientCommercials = opsOrderDetails.getClientCommercials();

        for(OpsOrderClientCommercial orderClientCommercial : opsOrderClientCommercials)
        {
             if(orderClientCommercial.getCommercialType().equalsIgnoreCase("Receivable"))
                receivables = receivables.add(BigDecimal.valueOf(Double.valueOf(orderClientCommercial.getCommercialAmount())));
             else
                 payables = payables.add(BigDecimal.valueOf(Double.valueOf(orderClientCommercial.getCommercialAmount())));
        }

        OpsHotelDetails hotelDetails = opsOrderDetails.getHotelDetails();
        OpsAccommodationTotalPriceInfo accommodationTotalPriceInfo = hotelDetails.getOpsAccommodationTotalPriceInfo();

        OpsTaxes opsTaxes = accommodationTotalPriceInfo.getOpsTaxes();
        taxesAmt = BigDecimal.valueOf(opsTaxes.getAmount());
        List<OpsTax> taxList = opsTaxes.getTax();
        for(OpsTax opsTax : taxList)
        {
            taxMap.put(opsTax.getTaxCode(), BigDecimal.valueOf(opsTax.getAmount()));
        }

        BigDecimal totalFare =BigDecimal.valueOf(Double.valueOf(accommodationTotalPriceInfo.getTotalPrice()));

        // opsProduct.getOrderDetails();
        ProfSellingPrice profSellingPrice = new ProfSellingPrice();
        SellingPrice sellingPrice = new SellingPrice();
        sellingPrice.setBasFare(baseFare.doubleValue());

        sellingPrice.setTotalTaxesAmt(taxesAmt);
        sellingPrice.setTotalFees(feeAmt);
        sellingPrice.setTaxes(taxMap);
        profSellingPrice.setSellingPrice(sellingPrice);

        TotalInCompanyMarketCurrency totalInCompanyMarketCurrency = new TotalInCompanyMarketCurrency();
        totalInCompanyMarketCurrency.setBasFare(baseFare.doubleValue());
        totalInCompanyMarketCurrency.setTotalFees(feeAmt);
        totalInCompanyMarketCurrency.setTotalTaxesAmt(taxesAmt);
        totalInCompanyMarketCurrency.setTaxes(taxMap);

        profSellingPrice.setTotalInCompanyMarketCurrency(totalInCompanyMarketCurrency);
        profSellingPrice.setDiscountsOffers(new BigDecimal(0));//need to discuss
        profSellingPrice.setComissionToClient(new BigDecimal(0));//need to Discuss
        profSellingPrice.setClientCommercialsPayable(payables);
        profSellingPrice.setClientCommercialsReceivable(receivables);
        profSellingPrice.setTotalNetSellingPrice(totalFare.add(payables).subtract(receivables));
        fileProfitabilityType.setProfSellingPrice(profSellingPrice);

        List<OpsOrderSupplierCommercial> opsOrderSupplierCommercialList = opsProduct.getOrderDetails().getSupplierCommercials();

        if (!opsOrderSupplierCommercialList.isEmpty()) {
            for (OpsOrderSupplierCommercial opsOrderSupplierCommercial : opsOrderSupplierCommercialList) {
                BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(opsOrderSupplierCommercial.getCommercialAmount()));
                if (opsOrderSupplierCommercial.getCommercialType().equalsIgnoreCase("receivable")) {
                    supplierCommercialsReceivable = supplierCommercialsReceivable.add(amount);
                } else {
                    supplierCommercialsPayable = supplierCommercialsPayable.add(amount);
                }
            }
        }

        OpsAccoOrderSupplierPriceInfo orderSupplierPriceInfo = hotelDetails.getOpsAccoOrderSupplierPriceInfo();
        BigDecimal supplierBaseFare = BigDecimal.valueOf(Double.valueOf(orderSupplierPriceInfo.getSupplierPrice()));


        OpsTaxes supplierTaxes = orderSupplierPriceInfo.getTaxes();
        BigDecimal supplierTotTaxAmt = BigDecimal.valueOf(supplierTaxes.getAmount());
        List<OpsTax> supplierTaxList = supplierTaxes.getTax();
        Map<String, BigDecimal> supplierTaxMap = new HashMap<String, BigDecimal>();
        for(OpsTax opsTax : supplierTaxList) {
            supplierTaxMap.put(opsTax.getTaxCode(), BigDecimal.valueOf(opsTax.getAmount()));
        }

        ProfSupplierCostPrice profSupplierCostPrice = new ProfSupplierCostPrice();
        SellingPrice supplierSellingPrice = new SellingPrice();
        supplierSellingPrice.setBasFare(supplierBaseFare.doubleValue());
        supplierSellingPrice.setTotalFees(feeAmt);
        supplierSellingPrice.setTotalTaxesAmt(supplierTotTaxAmt);
        supplierSellingPrice.setTaxes(supplierTaxMap);
        profSupplierCostPrice.setGrossSupplierCost(supplierSellingPrice);

        profSupplierCostPrice.setSupplierCommercialsPayable(supplierCommercialsPayable);
        profSupplierCostPrice.setSupplierCommercialsReceivable(supplierCommercialsReceivable);
        profSupplierCostPrice.setTotalNetPayableToSupplier(supplierBaseFare.add(supplierCommercialsPayable).subtract(supplierCommercialsReceivable));
        fileProfitabilityType.setProfSupplierCostPrice(profSupplierCostPrice);

        paxBreakDown.setNoOfchildren(childCount);
        paxBreakDown.setNoOfAdtls(adultCount);
        fileProfitabilityBooking.setPaxBreakDown(paxBreakDown);


        ProfMargin profMargin = new ProfMargin();
        profMargin.setNetMarginAmt(profSellingPrice.getTotalNetSellingPrice().subtract(profSupplierCostPrice.getTotalNetPayableToSupplier()));
        forPercentage = (profSellingPrice.getTotalNetSellingPrice().subtract(profSupplierCostPrice.getTotalNetPayableToSupplier()));
        try {
            percentage = forPercentage.divide(profSupplierCostPrice.getTotalNetPayableToSupplier(), 3, BigDecimal.ROUND_CEILING);
        } catch (Exception e) {
            //  logger.error(e.printStackTrace(),"sdfgjn");
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            logger.debug("Caught exception : " + stack.toString());
        }
        profMargin.setNetMarginPercentage(percentage);
        fileProfitabilityType.setProfMargin(profMargin);

        BudgetedProfitability budgetedProfitability = fileProfitabilityType;
        budgetedProfitability.setProfSellingPrice(profSellingPrice);
        budgetedProfitability.setProfSupplierCostPrice(profSupplierCostPrice);
        budgetedProfitability.setProfMargin(profMargin);

        OperationalProfitability operationalProfitability = new OperationalProfitability();
        operationalProfitability.setProfMargin(profMargin);
        operationalProfitability.setProfSellingPrice(profSellingPrice);
        operationalProfitability.setProfSupplierCostPrice(profSupplierCostPrice);

        FinalProfitability finalProfitability = new FinalProfitability();
        finalProfitability.setProfSellingPrice(profSellingPrice);
        finalProfitability.setProfSupplierCostPrice(profSupplierCostPrice);
        finalProfitability.setProfMargin(profMargin);

        fileProfitabilityBooking.setBudgetedFileProf(budgetedProfitability);
        fileProfitabilityBooking.setOperationalFileProf(operationalProfitability);
        fileProfitabilityBooking.setFinaFileProf(finalProfitability);
        fileProfitabilityBooking.setAccomodation(true);
        fileProfitabilityBooking.setBookingDateZDT(fileProfitabilityBookingParam.getBookingDateZDT());
        Variance variance = new Variance();
        variance.setBudgetedVsFinal(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
        variance.setBudgetedVsOperational(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(operationalProfitability.getProfMargin().getNetMarginAmt()));
        variance.setOperationalVsFinal(operationalProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
        fileProfitabilityBooking.setVariance(variance);
        fileProfitabilityBooking.setProductCategory(opsProduct.getProductCategory());
        fileProfitabilityBooking.setProductSubCategory(opsProduct.getProductSubCategory());
        fileProfitabilityBooking.setBookingReferenceNumber(fileProfitabilityBookingParam.getBookingReferenceNumber());
        fileProfitabilityBooking.setBookingType(FileProfTypes.ACCOMMODATION);

        if (fileProfType != null) {
            FileProfSearchCriteria fileProfBookingCriteria = new FileProfSearchCriteria();
            fileProfBookingCriteria.setIsAccomodation(true);
            fileProfBookingCriteria.setFileProfTypes(FileProfTypes.ACCOMMODATION);
            fileProfBookingCriteria.setOrderId(opsProduct.getOrderID());
            fileProfBookingCriteria.setBookingRefNumber(bookId);
            List<FileProfitabilityBooking> fileProfitabilityBookDb = fileProfitabilityModifiedRepository.getFileProfBookByCriteria(fileProfBookingCriteria);
            FileProfitabilityBooking fileProfitabilityBookDb12 = null;
            if (fileProfType.equals(FileProfTypes.OPERATIONAL_PROFITABILITY)) {


                if (fileProfitabilityBookDb != null) {
                    //   fileProfitabilityBookDb.setOperationalFileProf(operationalProfitability);
                    for (FileProfitabilityBooking fileProfitabilityBookDb123 : fileProfitabilityBookDb) {
                        fileProfitabilityBookDb12 = updatedPricing.pricingComparison(fileProfitabilityBooking, fileProfitabilityBookDb123, FileProfTypes.OPERATIONAL_PROFITABILITY);
                        if (fileProfitabilityBookDb12 != null)
                            fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(fileProfitabilityBookDb12);
                    }
                }

            } else if (fileProfType.equals(FileProfTypes.FINAL_PROFITABILITY)) {
                // fileProfitabilityBookDb.setFinaFileProf(finalProfitability);
                for (FileProfitabilityBooking fileProfitabilityBookDb123 : fileProfitabilityBookDb) {
                    fileProfitabilityBookDb12 = updatedPricing.pricingComparison(fileProfitabilityBooking, fileProfitabilityBookDb123, FileProfTypes.FINAL_PROFITABILITY);
                    if (fileProfitabilityBookDb12 != null)
                        fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(fileProfitabilityBookDb12);
                }
            }

        } else {
            fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(fileProfitabilityBooking);
        }
        if (opsRoomList != null && opsRoomList.size() >= 1) {
            roomWiseProfitability(opsRoomList, fileProfitabilityBooking, fileProfType);
        }
        // fileProfitabilityBookingList.add(fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(fileProfitabilityBooking)) ;


    }

//*****************************************************************************************************************************************************


    public void passengerWiseFileProfitability(OpsFlightDetails opsFlightDetails, List<OpsPaxTypeFareFlightClient> paxTypeFareFlightClientList, List<OpsPaxTypeFareFlightSupplier> opsPaxTypeFareFlightSuppliers,FileProfitabilityBooking fileProfitabilityBooking, FileProfTypes fileProfType) {
        logger.info("in passengerWiseFileProfitability()  ");

        List<OpsPaxTypeFareFlightClient> paxTypeFares = paxTypeFareFlightClientList;

        if (paxTypeFares != null) {
            logger.info("in passengerWiseFileProfitability() - paxTypeFares are not null ");
            for (OpsPaxTypeFareFlightClient opsPaxTypeFareFlightClient : paxTypeFares) {
               /* Double baseFare = new Double(0), taxesAmt = new Double(0), supplierCommercialsPayable = new Double(0), supplierCommercialsReceivable = new Double(0);
                BigDecimal percentage = new BigDecimal(0), forPercentage = new BigDecimal(0), feeAmt = new BigDecimal(0);
                Map<String, BigDecimal> setFees = new HashMap<String, BigDecimal>();
                Map<String, BigDecimal> taxMap = new HashMap<String, BigDecimal>();*/

               //ADDED BY ASHLEY AS A QUICK FIX! CHANGE THIS LATER
                List<OpsPaxTypeFareFlightSupplier> opsPaxTypeFareFlightSupplier1 = opsPaxTypeFareFlightSuppliers.stream().filter(x->x.getPaxType().equalsIgnoreCase(opsPaxTypeFareFlightClient.getPaxType())).collect(Collectors.toList());
                OpsPaxTypeFareFlightSupplier opsPaxTypeFareFlightSupplier = opsPaxTypeFareFlightSupplier1.get(0);


                if (opsFlightDetails != null) {
                    logger.info("in passengerWiseFileProfitability() -  opsFlightDetails are not null ");
                    List<OpsFlightPaxInfo> opsFlightPaxInfos = opsFlightDetails.getPaxInfo();
                    if (opsFlightPaxInfos != null) {
                        String leadPaxName = "";

                        for(OpsFlightPaxInfo opsFlightPaxInfo : opsFlightPaxInfos)
                        {
//                            if (opsFlightPaxInfo.getPaxType().equals(opsPaxTypeFareFlightSupplier.getPaxType())) {
                                if (opsFlightPaxInfo.getLeadPax()) {
                                    leadPaxName = String.format("%s %s",opsFlightPaxInfo.getFirstName(),opsFlightPaxInfo.getLastName());
                                    break;
                                }
//                            }
                        }

                        for (OpsFlightPaxInfo opsFlightPaxInfo : opsFlightPaxInfos) {
                            PaxBreakDown paxBreakDown = new PaxBreakDown();
                            if (opsFlightPaxInfo.getPaxType().equals(opsPaxTypeFareFlightClient.getPaxType())) {
//                                if (opsFlightPaxInfo.getLeadPax()) {
//                                    paxBreakDown.setLeadPassengerName(opsFlightPaxInfo.getFirstName() + " " + opsFlightPaxInfo.getLastName());
//                                    fileProfitabilityBooking.setLeadPassName(opsFlightPaxInfo.getFirstName() + " " + opsFlightPaxInfo.getLastName());
                                    paxBreakDown.setLeadPassengerName(leadPaxName);
                                    fileProfitabilityBooking.setLeadPassName(leadPaxName);
//                                }
                                paxBreakDown.setPassengerName(opsFlightPaxInfo.getFirstName() + " " + opsFlightPaxInfo.getLastName());
                                passWiseFileProfitabilityDivision.passengerWiseFileProfitabilityDivision(opsPaxTypeFareFlightClient,opsPaxTypeFareFlightSupplier, fileProfitabilityBooking, fileProfType, paxBreakDown);
                            }


                        }

                    }

                }

            }

        }

    }


    public void roomWiseProfitability(List<OpsRoom> opsRoomList, FileProfitabilityBooking fileProfitabilityBooking, FileProfTypes fileProfType) {
        logger.info("in roomWiseProfitability()  ");
        List<FileProfitabilityBooking> fileProfitabilityBookingList = new ArrayList<FileProfitabilityBooking>();

        if (opsRoomList != null) {
            for (OpsRoom opsRoom : opsRoomList) {
                if (opsRoom != null) {
                    int adultCount = 0, childCount = 0, feeAmt = 0;
                    double baseFare = 0, taxesAmt = 0, supplierCommercialsPayable = 0, supplierCommercialsReceivable = 0;
                    BigDecimal percentage = new BigDecimal(0), forPercentage = new BigDecimal(0);
                    HashMap<String, BigDecimal> taxes = new HashMap<String, BigDecimal>();
                    PaxBreakDown paxBreakDown = new PaxBreakDown();
                    List<OpsAccommodationPaxInfo> opsAccomodationPaxInfos = opsRoom.getPaxInfo();
                    if (opsAccomodationPaxInfos != null) {
                        for (OpsAccommodationPaxInfo opsAccomodationPaxInfo : opsAccomodationPaxInfos) {
                            if (opsAccomodationPaxInfo.getPaxType().equals("ADT")) {
                                adultCount++;
                                paxBreakDown.setPassengerName(opsAccomodationPaxInfo.getFirstName() + " " + opsAccomodationPaxInfo.getLastName());
                            } else {
                                childCount++;
                                paxBreakDown.setPassengerName(opsAccomodationPaxInfo.getFirstName() + " " + opsAccomodationPaxInfo.getLastName());
                            }
                            if (opsAccomodationPaxInfo.getLeadPax()) {
                                paxBreakDown.setLeadPassengerName(opsAccomodationPaxInfo.getFirstName() + " " + opsAccomodationPaxInfo.getLastName());
                                fileProfitabilityBooking.setLeadPassName(opsAccomodationPaxInfo.getFirstName() + " " + opsAccomodationPaxInfo.getLastName());
                            }

                        }
                    }

                    OpsRoomSuppPriceInfo opsRoomSuppPriceInfo = opsRoom.getRoomSuppPriceInfo();
                    if (opsRoom.getRoomTotalPriceInfo() != null && opsRoom.getRoomTotalPriceInfo().getRoomTotalPrice() != null) {
                        baseFare = Double.parseDouble(opsRoom.getRoomTotalPriceInfo().getRoomTotalPrice());
                    }


                    if (opsRoomSuppPriceInfo != null) {

                        OpsTaxes opsTax = opsRoomSuppPriceInfo.getTaxes();
                        List<OpsTax> opsTaxList = null;
                        if (opsTax != null) {
                            opsTaxList = opsTax.getTax();
                        }
                        if (!opsTaxList.isEmpty() && opsTaxList != null) {
                            for (OpsTax opsTaxDetails : opsTaxList) {
                                taxesAmt = taxesAmt + opsTaxDetails.getAmount();
                                if (taxes.size() != 0) {
                                    if (taxes.containsKey(opsTaxDetails.getTaxCode())) {
                                        taxes.put(opsTaxDetails.getTaxCode(), taxes.get(opsTaxDetails.getTaxCode()).add(new BigDecimal(opsTaxDetails.getAmount())));
                                    } else {
                                        //  double foo = Integer.parseInt(opsTaxDetails.getTaxValue());
                                        taxes.put(opsTaxDetails.getTaxCode(), new BigDecimal(opsTaxDetails.getAmount()));
                                    }
                                } else {
                                    taxes.put(opsTaxDetails.getTaxCode(), new BigDecimal(opsTaxDetails.getAmount()));
                                }
                            }
                        }

                    }

                    ProfSellingPrice profSellingPrice = new ProfSellingPrice();
                    profSellingPrice.setTotalNetSellingPrice(new BigDecimal(baseFare + taxesAmt));
                    profSellingPrice.setDiscountsOffers(new BigDecimal(0));
                    profSellingPrice.setComissionToClient(new BigDecimal(0));
                    SellingPrice sellingPrice = new SellingPrice();
                    sellingPrice.setBasFare(baseFare);
                    sellingPrice.setFees(null);
                    sellingPrice.setTotalTaxesAmt(new BigDecimal(taxesAmt));
                    sellingPrice.setTaxes(taxes);
                    profSellingPrice.setSellingPrice(sellingPrice);

                    TotalInCompanyMarketCurrency totalInCompanyMarketCurrency = new TotalInCompanyMarketCurrency();
                    totalInCompanyMarketCurrency.setBasFare(baseFare);
                    totalInCompanyMarketCurrency.setTaxes(taxes);
                    totalInCompanyMarketCurrency.setTotalTaxesAmt(new BigDecimal(taxesAmt));
                    profSellingPrice.setTotalInCompanyMarketCurrency(totalInCompanyMarketCurrency);
                    ProfSupplierCostPrice profSupplierCostPrice = new ProfSupplierCostPrice();
                    SellingPrice sellingPrice1 = new SellingPrice();
                    sellingPrice1.setBasFare(baseFare);
                    sellingPrice1.setTotalTaxesAmt(new BigDecimal(taxesAmt));
                    sellingPrice1.setTaxes(taxes);
                    profSupplierCostPrice.setGrossSupplierCost(sellingPrice1);


                    List<OpsRoomSuppCommercial> opsRoomSuppCommercialList = opsRoom.getRoomSuppCommercials();

                    if (opsRoomSuppCommercialList != null) {
                        for (OpsRoomSuppCommercial opsRoomSuppCommercial : opsRoomSuppCommercialList) {
                            if (opsRoomSuppCommercial.getCommercialType().equalsIgnoreCase("Receivable")) {
                                supplierCommercialsReceivable = supplierCommercialsReceivable + opsRoomSuppCommercial.getCommercialAmount();
                            } else {
                                supplierCommercialsPayable = supplierCommercialsPayable + opsRoomSuppCommercial.getCommercialAmount();
                            }
                        }
                    }

                    profSupplierCostPrice.setSupplierCommercialsReceivable(new BigDecimal(supplierCommercialsReceivable));
                    profSupplierCostPrice.setSupplierCommercialsPayable(new BigDecimal(supplierCommercialsPayable));
                    profSupplierCostPrice.setTotalNetPayableToSupplier(new BigDecimal(supplierCommercialsPayable + baseFare + taxesAmt - supplierCommercialsReceivable));

                    //   fileProfitabilityType.setProfSupplierCostPrice(profSupplierCostPrice);

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
                    // budgetedProfitability.setType("Budgeted_Profitability");
                    OperationalProfitability operationalProfitability = new OperationalProfitability();
                    operationalProfitability.setProfMargin(profMargin);
                    operationalProfitability.setProfSellingPrice(profSellingPrice);
                    operationalProfitability.setProfSupplierCostPrice(profSupplierCostPrice);
                    // operationalProfitability.setType("Operational_Profitability");

                    FinalProfitability finalProfitability = new FinalProfitability();
                    finalProfitability.setProfMargin(profMargin);
                    finalProfitability.setProfSellingPrice(profSellingPrice);
                    finalProfitability.setProfSupplierCostPrice(profSupplierCostPrice);

                    FileProfitabilityBooking fileProfitabilityBooking1 = new FileProfitabilityBooking();

                    //adding company details
                    fileProfitabilityBooking1.setBU(fileProfitabilityBooking.getBU());
                    fileProfitabilityBooking1.setSBU(fileProfitabilityBooking.getSBU());
                    fileProfitabilityBooking1.setCompanyName(fileProfitabilityBooking.getCompanyName());
                    fileProfitabilityBooking1.setCompanyId(fileProfitabilityBooking.getCompanyId());
                    fileProfitabilityBooking1.setGroupOfCompanyName(fileProfitabilityBooking.getGroupOfCompanyName());
                    fileProfitabilityBooking1.setGroupOfCompanyId(fileProfitabilityBooking.getGroupOfCompanyId());
                    fileProfitabilityBooking1.setCompanyGroupName(fileProfitabilityBooking.getCompanyGroupName());
                    fileProfitabilityBooking1.setCompanyGroupId(fileProfitabilityBooking.getCompanyGroupId());

                    fileProfitabilityBooking1.setOperationalFileProf(operationalProfitability);
                    fileProfitabilityBooking1.setFinaFileProf(finalProfitability);
                    if (fileProfType != null) {
                        FileProfSearchCriteria fileProfBookingCriteria = new FileProfSearchCriteria();
                        fileProfBookingCriteria.setIsRoomwise(true);
                        fileProfBookingCriteria.setOrderId(fileProfitabilityBooking.getOrderId());
                        fileProfBookingCriteria.setBookingRefNumber(fileProfitabilityBooking.getBookingReferenceNumber());
                        fileProfBookingCriteria.setFileProfTypes(FileProfTypes.ROOM_WISE);
                        List<FileProfitabilityBooking> fileProfitabilityBookDb = fileProfitabilityModifiedRepository.getFileProfBookByCriteria(fileProfBookingCriteria);
                        if (fileProfType.equals(FileProfTypes.OPERATIONAL_PROFITABILITY)) {

                            if (fileProfitabilityBookDb != null) {
                                for (FileProfitabilityBooking fileProfitabilityBookingdb2 : fileProfitabilityBookDb) {
                                    FileProfitabilityBooking fileProfitabilityBookDbToSave = updatedPricing.pricingComparison(fileProfitabilityBooking1, fileProfitabilityBookingdb2, FileProfTypes.OPERATIONAL_PROFITABILITY);
                                    if (fileProfitabilityBookDbToSave != null)
                                        fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(fileProfitabilityBookDbToSave);
                                }
                            }
                        } else if (fileProfType.equals(FileProfTypes.FINAL_PROFITABILITY)) {
                            if (fileProfitabilityBookDb != null) {
                                //  fileProfitabilityBookDb.setFinaFileProf(finalProfitability);
                                for (FileProfitabilityBooking fileProfitabilityBookingdb2 : fileProfitabilityBookDb) {
                                    FileProfitabilityBooking fileProfitabilityBookDbrt = updatedPricing.pricingComparison(fileProfitabilityBooking1, fileProfitabilityBookingdb2, FileProfTypes.FINAL_PROFITABILITY);
                                    fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(fileProfitabilityBookDbrt);
                                }
                            }
                        }

                    } else {
                        fileProfitabilityBooking1.setBookingType(FileProfTypes.ROOM_WISE);
                        fileProfitabilityBooking1.setPaxBreakDown(paxBreakDown);
                        fileProfitabilityBooking1.setBookingReferenceNumber(fileProfitabilityBooking.getBookingReferenceNumber());
                        fileProfitabilityBooking1.setOrderId(fileProfitabilityBooking.getOrderId());
                        fileProfitabilityBooking1.setClientType(fileProfitabilityBooking.getClientType());
                        fileProfitabilityBooking1.setClientName(fileProfitabilityBooking.getClientName());
                        fileProfitabilityBooking1.setClientId(fileProfitabilityBooking.getClientId());
                        fileProfitabilityBooking1.setBudgetedFileProf(budgetedProfitability);
                        fileProfitabilityBooking1.setOperationalFileProf(operationalProfitability);
                        fileProfitabilityBooking1.setFinaFileProf(finalProfitability);
                        fileProfitabilityBooking1.setRoomwise(true);
                        fileProfitabilityBooking1.setBookingDateZDT(fileProfitabilityBooking.getBookingDateZDT());
                        fileProfitabilityBooking1.setClientType(fileProfitabilityBooking.getClientType());
                        fileProfitabilityBooking1.setClientId(fileProfitabilityBooking.getClientId());
                        fileProfitabilityBooking1.setProductName(fileProfitabilityBooking.getProductName());
                        Variance variance = new Variance();
                        variance.setBudgetedVsFinal(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
                        variance.setBudgetedVsOperational(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(operationalProfitability.getProfMargin().getNetMarginAmt()));
                        variance.setOperationalVsFinal(operationalProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
                        fileProfitabilityBooking1.setVariance(variance);
                        fileProfitabilityBooking1.setProductSubCategory(fileProfitabilityBooking.getProductSubCategory());
                        fileProfitabilityBooking1.setProductCategory(fileProfitabilityBooking.getProductCategory());
                        fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(fileProfitabilityBooking1);

                    }

                }

            }


        }

    }


    //********************************************************************************************************************************************
    @Override
    public void calculationMethodForServiceOrder(OpsProduct opsProduct, FileProfitabilityBooking fromServiceOrder, FileProfTypes fileProfType) {
        logger.info("in calculationMethodForServiceOrder()  ");

        Integer adultCount = 0, childCount = 0;
        BigDecimal baseFare = new BigDecimal(0), taxesAmt = new BigDecimal(0), supplierCommercialsPayable = new BigDecimal(0), supplierCommercialsReceivable = new BigDecimal(0);
        BigDecimal percentage = new BigDecimal(0), forPercentage = new BigDecimal(0), feeAmt = new BigDecimal(0), clientCommercialsReceivable = new BigDecimal(0), clientCommercialsPayable = new BigDecimal(0);
        OpsFlightDetails opsFlightDetails = null;
        OpsOrderDetails opsOrderDetails1 = null;
        if (opsProduct != null && opsProduct.getProductCategory().equalsIgnoreCase(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.getCategory())) {


            opsOrderDetails1 = opsProduct.getOrderDetails();
            if (opsOrderDetails1 != null)
                opsFlightDetails = opsOrderDetails1.getFlightDetails();
            // OpsFlightSupplierPriceInfo opsFlightSupplierPriceInfo = new OpsFlightSupplierPriceInfo();
            // OpsFlightSupplierPriceInfo OpsFlightSupplierPriceInfo = null;
           /* if(opsFlightDetails != null)
            OpsFlightSupplierPriceInfo = opsFlightDetails.getOpsFlightSupplierPriceInfo();*/

            // OpsFlightSupplierPriceInfo.getPaxTypeFares();


            PaxBreakDown paxBreakDown = new PaxBreakDown();
            if (opsFlightDetails != null) {
                logger.info("in calculationMethodForServiceOrder() - opsFlightDetails are not null ");
                List<OpsFlightPaxInfo> opsFlightPaxInfos = opsFlightDetails.getPaxInfo();
                if (opsFlightPaxInfos != null) {
                    for (OpsFlightPaxInfo opsFlightPaxInfo : opsFlightPaxInfos) {
                        if (opsFlightPaxInfo.getPaxType().equals("ADT")) {
                            adultCount++;

                            paxBreakDown.setPassengerName(opsFlightPaxInfo.getFirstName());
                        } else {
                            childCount++;
                            paxBreakDown.setPassengerName(opsFlightPaxInfo.getFirstName());
                        }
                        if (opsFlightPaxInfo.getLeadPax()) {
                            paxBreakDown.setLeadPassengerName(opsFlightPaxInfo.getFirstName());

                        }

                    }

                }

                paxBreakDown.setNoOfchildren(childCount);
                paxBreakDown.setNoOfAdtls(adultCount);
            }

            Map<String, BigDecimal> setFees = new HashMap<String, BigDecimal>();
            Map<String, BigDecimal> taxMap = new HashMap<String, BigDecimal>();
            List<OpsPaxTypeFareFlightClient> paxTypeFareFlightClientList = null;
            OpsFees opsFeesList = null;
            OpsTaxes taxes = null;
            if (opsFlightDetails != null) {

                OpsFlightTotalPriceInfo opsFlightTotalPriceInfo = opsFlightDetails.getTotalPriceInfo();


                if (opsFlightTotalPriceInfo != null)
                    paxTypeFareFlightClientList = opsFlightTotalPriceInfo.getPaxTypeFares();
                if (opsFlightTotalPriceInfo.getBaseFare() != null && opsFlightTotalPriceInfo.getBaseFare().getAmount() != 0)
                    baseFare = BigDecimal.valueOf(opsFlightTotalPriceInfo.getBaseFare().getAmount());

                if (opsFlightTotalPriceInfo != null)
                    opsFeesList = opsFlightTotalPriceInfo.getFees();

                if (opsFeesList != null) {
                    List<OpsFee> fee = opsFeesList.getFee();

                    if (fee != null && !fee.isEmpty()) {
                        for (OpsFee opsFee : fee) {
                            if (opsFee != null) {
                                feeAmt = feeAmt.add(new BigDecimal(opsFee.getAmount()));
                                if (setFees.size() != 0) {
                                    //   setFees.containsKey()
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

                }
                if (opsFlightTotalPriceInfo.getTaxes() != null)
                    taxes = opsFlightTotalPriceInfo.getTaxes();
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

                List<OpsOrderClientCommercial> opsOrderClientCommercials = opsOrderDetails1.getClientCommercials();
                if (opsOrderClientCommercials != null) {
                    for (OpsOrderClientCommercial opsOrderClientCommercial : opsOrderClientCommercials) {
                        BigDecimal foo = BigDecimal.valueOf(Double.parseDouble(opsOrderClientCommercial.getCommercialAmount()));
                        if (opsOrderClientCommercial.getCommercialType().equals("Receivable")) {
                            clientCommercialsReceivable = clientCommercialsReceivable.add(foo);
                        } else {
                            clientCommercialsPayable = clientCommercialsPayable.add(foo);
                        }
                    }
                }

            }


            ProfSellingPrice profSellingPrice = new ProfSellingPrice();
            profSellingPrice.setTotalNetSellingPrice(baseFare.add(taxesAmt).add(feeAmt));
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
            totalInCompanyMarketCurrency.setTotalTaxesAmt(taxesAmt);
            totalInCompanyMarketCurrency.setTotalFees(feeAmt);
            totalInCompanyMarketCurrency.setTaxes(taxMap);
            profSellingPrice.setTotalInCompanyMarketCurrency(totalInCompanyMarketCurrency);


            ProfSupplierCostPrice profSupplierCostPrice = new ProfSupplierCostPrice();
            SellingPrice sellingPrice1 = new SellingPrice();
            sellingPrice1.setBasFare(baseFare.doubleValue());
            sellingPrice1.setFees(setFees);
            sellingPrice1.setTotalFees(feeAmt);
            sellingPrice1.setTotalTaxesAmt(taxesAmt);
            sellingPrice1.setTaxes(taxMap);
            profSupplierCostPrice.setGrossSupplierCost(sellingPrice1);


            List<OpsPaxTypeFareFlightSupplier> opsFlightSupplierPriceInfo = opsFlightDetails.getOpsFlightSupplierPriceInfo().getPaxTypeFares();
            List<OpsOrderSupplierCommercial> opsOrderSupplierCommercialList = opsOrderDetails1.getSupplierCommercials();
            if (opsOrderSupplierCommercialList != null) {
                for (OpsOrderSupplierCommercial opsOrderSupplierCommercial : opsOrderSupplierCommercialList) {
                    BigDecimal foo = BigDecimal.valueOf(Double.parseDouble(opsOrderSupplierCommercial.getCommercialAmount()));
                    if (opsOrderSupplierCommercial.getCommercialType().equals("Receivable")) {
                        supplierCommercialsReceivable = supplierCommercialsReceivable.add(foo);
                    } else {
                        supplierCommercialsPayable = supplierCommercialsPayable.add(foo);
                    }
                }
            }
            profSupplierCostPrice.setSupplierCommercialsReceivable(supplierCommercialsReceivable);
            profSupplierCostPrice.setSupplierCommercialsPayable(supplierCommercialsPayable);
            profSupplierCostPrice.setTotalNetPayableToSupplier(supplierCommercialsReceivable.add(baseFare).add(taxesAmt).add(feeAmt).subtract(supplierCommercialsPayable));


            ProfMargin profMargin = new ProfMargin();
            profMargin.setNetMarginAmt(profSellingPrice.getTotalNetSellingPrice().subtract(profSupplierCostPrice.getTotalNetPayableToSupplier()));
            forPercentage = profSellingPrice.getTotalNetSellingPrice().subtract(profSupplierCostPrice.getTotalNetPayableToSupplier());

            if (!profSupplierCostPrice.getTotalNetPayableToSupplier().equals(0)) {

                try {
                    percentage = forPercentage.divide(profSupplierCostPrice.getTotalNetPayableToSupplier(), 3, BigDecimal.ROUND_CEILING);
                } catch (Exception e) {
                    StringWriter stack = new StringWriter();
                    e.printStackTrace(new PrintWriter(stack));
                    logger.debug("Caught exception : " + stack.toString());
                }
                profMargin.setNetMarginPercentage(percentage);
            }

            FinalProfitability finalProfitability = new FinalProfitability();
            finalProfitability.setProfMargin(profMargin);
            finalProfitability.setProfSellingPrice(profSellingPrice);
            finalProfitability.setProfSupplierCostPrice(profSupplierCostPrice);


            OperationalProfitability operationalProfitability = new OperationalProfitability();
            operationalProfitability.setProfMargin(profMargin);
            operationalProfitability.setProfSellingPrice(profSellingPrice);
            operationalProfitability.setProfSupplierCostPrice(profSupplierCostPrice);

            FileProfitabilityBooking fileProfitabilityBookingUpdated = new FileProfitabilityBooking();
            fileProfitabilityBookingUpdated.setFinaFileProf(finalProfitability);
            fileProfitabilityBookingUpdated.setOperationalFileProf(operationalProfitability);

            Variance variance = new Variance();
            variance.setBudgetedVsFinal(fileProfitabilityBookingUpdated.getBudgetedFileProf().getProfMargin().getNetMarginAmt().subtract(fileProfitabilityBookingUpdated.getFinaFileProf().getProfMargin().getNetMarginAmt()));
            variance.setBudgetedVsOperational(fileProfitabilityBookingUpdated.getBudgetedFileProf().getProfMargin().getNetMarginAmt().subtract(fileProfitabilityBookingUpdated.getOperationalFileProf().getProfMargin().getNetMarginAmt()));
            variance.setOperationalVsFinal(fileProfitabilityBookingUpdated.getOperationalFileProf().getProfMargin().getNetMarginAmt().subtract(fileProfitabilityBookingUpdated.getFinaFileProf().getProfMargin().getNetMarginAmt()));
            fileProfitabilityBookingUpdated.setVariance(variance);
            fileProfitabilityBookingUpdated.setPaxBreakDown(paxBreakDown);

            if (fileProfType != null) {
                FileProfSearchCriteria fileProfBookingCriteria = new FileProfSearchCriteria();
                fileProfBookingCriteria.setIsTransportation(true);
                fileProfBookingCriteria.setOrderId(fromServiceOrder.getOrderId());
                fileProfBookingCriteria.setBookingRefNumber(fromServiceOrder.getBookingReferenceNumber());

                List<FileProfitabilityBooking> fileProfitabilityBookDb = fileProfitabilityModifiedRepository.getFileProfBookByCriteria(fileProfBookingCriteria);


                if (fileProfType.equals(FileProfTypes.OPERATIONAL_PROFITABILITY)) {
                    if (fileProfitabilityBookDb != null) {

                        //fileProfitabilityBookDb.setOperationalFileProf(operationalProfitability);
                        for (FileProfitabilityBooking fileProfitabilityBooking : fileProfitabilityBookDb) {
                            // updatedPricing.pricingComparison(fileProfitabilityBookingUpdated, fileProfitabilityBooking, FileProfTypes.OPERATIONAL_PROFITABILITY);
                            fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(updatedPricing.pricingComparison(fileProfitabilityBookingUpdated, fileProfitabilityBooking, FileProfTypes.OPERATIONAL_PROFITABILITY));
                        }
                    }

                } else if (fileProfType.equals(FileProfTypes.FINAL_PROFITABILITY)) {
                    // fileProfitabilityBookDb.setFinaFileProf(finalProfitability);
                    for (FileProfitabilityBooking fileProfitabilityBooking : fileProfitabilityBookDb) {
                        updatedPricing.pricingComparison(fileProfitabilityBookingUpdated, fileProfitabilityBooking, FileProfTypes.FINAL_PROFITABILITY);
                        fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(updatedPricing.pricingComparison(fileProfitabilityBookingUpdated, fileProfitabilityBooking, FileProfTypes.FINAL_PROFITABILITY));
                    }
                }
            }

            passengerWiseFileProfitability(opsFlightDetails, paxTypeFareFlightClientList, opsFlightSupplierPriceInfo, fromServiceOrder, fileProfType);


        }
        if (opsProduct.getProductCategory().equalsIgnoreCase(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION.getCategory())) {
            accomodationFileProfitability(fromServiceOrder, opsProduct, fromServiceOrder.getBookingReferenceNumber(), fromServiceOrder.getClientType(), fromServiceOrder.getClientId(), false, fileProfType);
        }

    }

    @Override
    public List<FileProfitabilityBooking> getListOfFileProfsWRTCriteria(FileProfSearchCriteria fileProfBookingCriteria) throws OperationException{
        try {
            return fileProfitabilityModifiedRepository.getListOfFileProfsWRTCriteria(fileProfBookingCriteria);
        }catch(OperationException e) {
            throw e;
        }
    }


    public HashMap<String, Object> applyPagination(List<FileProfitabilityBooking> fileProfBookingsList, Integer pageNo, Integer pageSize) throws OperationException {
        try {
            HashMap<String, Object> result = new HashMap<>();
            List<FileProfitabilityBooking> list = new ArrayList<>();
            int value = (pageNo - 1) * pageSize;
            int maxSize = value + pageSize;
            if (fileProfBookingsList != null && fileProfBookingsList.size() <= maxSize) {
                maxSize = fileProfBookingsList.size();
            }
            if (fileProfBookingsList != null && value >= fileProfBookingsList.size()) {
                //  throw new OperationException(Constants.ER01);
            } else {
                for (int i = value; i < maxSize; i++) {
                    list.add(fileProfBookingsList.get(i));
                }
                result.put("result", list);
                result.put("noOfPages", FileProfitabilityServiceImpl.getNoOfPages(pageSize, fileProfBookingsList.size()));

            }
            return result;
        }catch(Exception e){
            throw new OperationException(Constants.OPS_ERR_20100);
        }
    }

    public static Integer getNoOfPages(Integer pageSize, Integer noOfRows) {
        if (pageSize != null && noOfRows != null) {
            if (!noOfRows.equals(0) || !pageSize.equals(0)) {
                Integer noOfPages = noOfRows / pageSize;
                if (noOfRows % pageSize == 0)
                    return noOfPages;
                else
                    return noOfPages + 1;
            } else
                return 1;
        } else
            return 1;
    }

    public List<FileProfitabilityBooking> getListOfFileProfsWRTCriteria(FileProfSearchReportCriteria bookingCriteria) {
        return fileProfitabilityModifiedRepository.getListOfFileProfsWRTCriteria(bookingCriteria);
    }


//---------------------------------------------------------FINAL PROFITABILITY IMPLEMENTATION--------------------------------------------------------------------------------------------

    @Override
    public void updateSupplierFinalProfitability(JSONObject commercialStatementJsn) throws OperationException{

        JSONArray commercialsStatementsArr = commercialStatementJsn.getJSONArray("commercialStatementIds");
        List<String> statementIds = commercialsStatementsArr.toList().stream()
                    .map(object -> Objects.toString(object, null))
                    .collect(Collectors.toList());

        List<SupplierCommercialStatement> commercialStatements = supplierCommercialStatementRepo.getAll(statementIds);

        for(SupplierCommercialStatement supplierStatement : commercialStatements){
            Set<CommercialStatementDetails> commercialStatementDetails = supplierStatement.getCommercialStatementDetails();

            String commercialType = supplierStatement.getCommercialType();

            for(CommercialStatementDetails commercialStatementDetail : commercialStatementDetails){

                FileProfSearchCriteria bookingCriteria = new FileProfSearchCriteria();
                String bookingRef = commercialStatementDetail.getBookingRefNum();
                bookingCriteria.setBookingRefNumber(bookingRef);
                List<FileProfitabilityBooking> fileProfitabilityBookings = fileProfitabilityModifiedRepository.getListOfFileProfsWRTCriteria(bookingCriteria);

                if(fileProfitabilityBookings==null || fileProfitabilityBookings.isEmpty())
                {
                    logger.warn(String.format("Could not find the booking : %s in File profitability" , bookingRef));
                    continue;
                }

                Set<OrderCommercialDetails> orderCommercialDetails = commercialStatementDetail.getOrderCommercialDetails();

                for(OrderCommercialDetails orderCommercialDetail : orderCommercialDetails){

                    String orderId = orderCommercialDetail.getOrderId();
                    List<FileProfitabilityBooking> orderProfitabilityBookings;

                    orderProfitabilityBookings = fileProfitabilityBookings.parallelStream().filter(x->x.getOrderId().equalsIgnoreCase(orderId)&&
                             (x.getBookingType().equals(FileProfTypes.ACCOMMODATION) ||
                                     x.getBookingType().equals(FileProfTypes.TRANSPORTATION))).collect(Collectors.toList());

                    if(orderProfitabilityBookings==null || orderProfitabilityBookings.isEmpty())
                        continue;

                    //Ideally there will always be 1, but due to the old implementation many could be there
                    for(FileProfitabilityBooking profitabilityBooking : orderProfitabilityBookings) {

                        FinalProfitability finalProfitability = profitabilityBooking.getFinaFileProf();
                        ProfSellingPrice profSellingPrice = finalProfitability.getProfSellingPrice();

                        ProfSupplierCostPrice profSupplierCostPrice = finalProfitability.getProfSupplierCostPrice();

                        //ADDING THE NON TRANSACTIONAL COMMERCIALS

                        if (commercialType.equalsIgnoreCase("Receivable")) {
                            profSupplierCostPrice.setSupplierCommercialsReceivable(profSupplierCostPrice.getSupplierCommercialsReceivable().add(orderCommercialDetail.getCommercialValue()));
                            profSupplierCostPrice.setTotalNetPayableToSupplier(profSupplierCostPrice.getTotalNetPayableToSupplier().subtract(orderCommercialDetail.getCommercialValue()));
                        } else {
                            profSupplierCostPrice.setSupplierCommercialsPayable(profSupplierCostPrice.getSupplierCommercialsPayable().add(orderCommercialDetail.getCommercialValue()));
                            profSupplierCostPrice.setTotalNetPayableToSupplier(profSupplierCostPrice.getTotalNetPayableToSupplier().add(orderCommercialDetail.getCommercialValue()));
                        }

                        //CALCULATING THE NEW MARGIN

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

                        finalProfitability.setProfMargin(profMargin);
                        finalProfitability.setProfSupplierCostPrice(profSupplierCostPrice);

                        //CALCULATING THE NEW VARIANCE

                        OperationalProfitability operationalProfitability = profitabilityBooking.getOperationalFileProf();
                        BudgetedProfitability budgetedProfitability = profitabilityBooking.getBudgetedFileProf();


                        Variance variance = new Variance();
                        variance.setBudgetedVsFinal(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
                        variance.setBudgetedVsOperational(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(operationalProfitability.getProfMargin().getNetMarginAmt()));
                        variance.setOperationalVsFinal(operationalProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
                        profitabilityBooking.setVariance(variance);
                        fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(profitabilityBooking);
                    }
                }

            }
        }
    }


    @Override
    public void updateClientFinalProfitability(JSONObject commercialStatementJsn) throws OperationException{

        JSONArray commercialsStatementsArr = commercialStatementJsn.getJSONArray("commercialStatementIds");
        List<String> statementIds = commercialsStatementsArr.toList().stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList());

        List<ClientCommercialStatement> commercialStatements = clientCommercialStatementRepo.getAll(statementIds);

        for(ClientCommercialStatement commercialStatement : commercialStatements){
            Set<CommercialStatementDetails> commercialStatementDetails = commercialStatement.getCommercialStatementDetails();

            String commercialType = commercialStatement.getCommercialType();

            for(CommercialStatementDetails commercialStatementDetail : commercialStatementDetails){

                FileProfSearchCriteria bookingCriteria = new FileProfSearchCriteria();
                String bookingRef = commercialStatementDetail.getBookingRefNum();
                bookingCriteria.setBookingRefNumber(bookingRef);
                List<FileProfitabilityBooking> fileProfitabilityBookings = fileProfitabilityModifiedRepository.getListOfFileProfsWRTCriteria(bookingCriteria);

                if(fileProfitabilityBookings==null || fileProfitabilityBookings.isEmpty())
                {
                    logger.warn(String.format("Could not find the booking : %s in File profitability" , bookingRef));
                    continue;
                }

                Set<OrderCommercialDetails> orderCommercialDetails = commercialStatementDetail.getOrderCommercialDetails();

                for(OrderCommercialDetails orderCommercialDetail : orderCommercialDetails){

                    String orderId = orderCommercialDetail.getOrderId();
                    List<FileProfitabilityBooking> orderProfitabilityBookings;

                    orderProfitabilityBookings = fileProfitabilityBookings.parallelStream().filter(x->x.getOrderId().equalsIgnoreCase(orderId)&&
                            (x.getBookingType().equals(FileProfTypes.ACCOMMODATION) ||
                                    x.getBookingType().equals(FileProfTypes.TRANSPORTATION))).collect(Collectors.toList());

                    if(orderProfitabilityBookings==null || orderProfitabilityBookings.isEmpty())
                        continue;

                    //Ideally there will always be 1, but due to the old implementation many could be there
                    for(FileProfitabilityBooking profitabilityBooking : orderProfitabilityBookings) {

                        FinalProfitability finalProfitability = profitabilityBooking.getFinaFileProf();

                        ProfSellingPrice profSellingPrice = finalProfitability.getProfSellingPrice();

                        ProfSupplierCostPrice profSupplierCostPrice = finalProfitability.getProfSupplierCostPrice();

                        //ADDING THE NON TRANSACTIONAL COMMERCIALS

                        if (commercialType.equalsIgnoreCase("Receivable")) {
                            profSellingPrice.setClientCommercialsReceivable(profSellingPrice.getClientCommercialsReceivable().add(orderCommercialDetail.getCommercialValue()));
                            profSellingPrice.setTotalNetSellingPrice(profSellingPrice.getTotalNetSellingPrice().subtract(orderCommercialDetail.getCommercialValue()));
                        } else {
                            profSellingPrice.setClientCommercialsPayable(profSellingPrice.getClientCommercialsPayable().add(orderCommercialDetail.getCommercialValue()));
                            profSellingPrice.setTotalNetSellingPrice(profSellingPrice.getTotalNetSellingPrice().add(orderCommercialDetail.getCommercialValue()));
                        }

                        //CALCULATING THE NEW MARGIN

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

                        finalProfitability.setProfMargin(profMargin);
                        finalProfitability.setProfSupplierCostPrice(profSupplierCostPrice);

                        //CALCULATING THE NEW VARIANCE

                        OperationalProfitability operationalProfitability = profitabilityBooking.getOperationalFileProf();
                        BudgetedProfitability budgetedProfitability = profitabilityBooking.getBudgetedFileProf();

                        Variance variance = new Variance();
                        variance.setBudgetedVsFinal(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
                        variance.setBudgetedVsOperational(budgetedProfitability.getProfMargin().getNetMarginAmt().subtract(operationalProfitability.getProfMargin().getNetMarginAmt()));
                        variance.setOperationalVsFinal(operationalProfitability.getProfMargin().getNetMarginAmt().subtract(finalProfitability.getProfMargin().getNetMarginAmt()));
                        profitabilityBooking.setVariance(variance);
                        fileProfitabilityModifiedRepository.saveOrUpdateFileProfitability(profitabilityBooking);
                    }
                }

            }
        }

    }

}
