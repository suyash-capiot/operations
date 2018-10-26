package com.coxandkings.travel.operations.utils.adapter;

import com.coxandkings.travel.ext.model.be.*;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.DateTimeUtil;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.supplier.SupplierDetailsService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component(value = "OpsBookingAdapter")
public class OpsBookingAdapter {

    @Autowired
    private OpsFlightAdapter opsFlightAdapterService;

    @Autowired
    private OpsAccommodationAdapter opsAccommodationAdapterService;

    @Autowired
    private OpsActivitiesAdapter opsActivitiesAdapter;

    @Autowired
    private OpsHolidaysAdapter OpsHolidaysAdapterService;

    @Autowired
    private SupplierDetailsService supplierDetailsService;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    private static final Logger logger = LogManager.getLogger(OpsBookingAdapter.class);


    public OpsBooking getOpsBooking(Booking booking) {

        BookingResponseBody aBookingResponseBody = null;

        BookingResponseHeader aBookingResponseHeader = null;

        aBookingResponseBody = booking.getBookingResponseBody();

        aBookingResponseHeader = booking.getBookingResponseHeader();

        OpsBooking opsBooking = new OpsBooking();

        if (aBookingResponseHeader != null && aBookingResponseHeader.getClientContext() != null &&
                aBookingResponseHeader.getClientContext().getPointOfSale() != null) {
            opsBooking.setPointOfSale(aBookingResponseHeader.getClientContext().getPointOfSale());
        }

        opsBooking.setClientID(aBookingResponseHeader.getClientContext().getClientID());

        opsBooking.setClientType(aBookingResponseHeader.getClientContext().getClientType());

        opsBooking.setClientLanguage(aBookingResponseHeader.getClientContext().getClientLanguage());

        opsBooking.setClientCategory(aBookingResponseHeader.getClientContext().getClientCategory());

        opsBooking.setClientSubCategory(aBookingResponseHeader.getClientContext().getClientSubCategory());

        opsBooking.setClientMarket(aBookingResponseHeader.getClientContext().getClientMarket());

        opsBooking.setCompanyMarket(aBookingResponseHeader.getCompanyMarket());

        opsBooking.setClientCurrency(aBookingResponseHeader.getClientContext().getClientCurrency());

        if (aBookingResponseHeader.getClientContext().getCompany() != null) {
            opsBooking.setCompanyId(aBookingResponseHeader.getClientContext().getCompany());
        }

        opsBooking.setSessionID(aBookingResponseHeader.getSessionID());

        opsBooking.setUserID(aBookingResponseHeader.getUserID());

        opsBooking.setStaffId(aBookingResponseBody.getStaffID());

        opsBooking.setTransactionID(aBookingResponseHeader.getTransactionID());

        opsBooking.setBookID(aBookingResponseBody.getBookID());

        opsBooking.setHolidayBooking(aBookingResponseBody.getIsHolidayBooking());

        opsBooking.setBookingDateZDT(DateTimeUtil.formatBEDateTimeZone(aBookingResponseBody.getBookingDate()));

        opsBooking.setPaymentInfo(aBookingResponseBody.getPaymentInfo().stream().map(aPaymentInfo ->
                getOpsPaymentInfo(aPaymentInfo)).collect(Collectors.toList()));

        HashMap<String, String> suppIdToName = new HashMap<>();
        opsBooking.setProducts(aBookingResponseBody.getProducts().stream().map(product ->
                getOpsProduct(product, suppIdToName)).collect(Collectors.toList()));

        opsBooking.setStatus(OpsBookingStatus.fromString(aBookingResponseBody.getStatus()));

        opsBooking.setQcStatus(aBookingResponseBody.getQcStatus());

        opsBooking.setBu(aBookingResponseHeader.getClientContext().getBu());

        opsBooking.setSbu(aBookingResponseHeader.getClientContext().getSbu());

        opsBooking.setGroupOfCompaniesID(aBookingResponseHeader.getClientContext().getGroupOfCompaniesID());

        opsBooking.setGroupCompanyID(aBookingResponseHeader.getClientContext().getGroupCompanyID());

//        if(aBookingResponseBody.getBookingType()!=null)
//            opsBooking.setBookingType(aBookingResponseBody.getBookingType());
//        else
//            opsBooking.setBookingType("online");

        opsBooking.setContainsProducts(aBookingResponseBody.getContainsProducts());

        return opsBooking;
    }

    private OpsPaymentInfo getOpsPaymentInfo(PaymentInfo paymentInfo) {
        OpsPaymentInfo opsPaymentInfo = new OpsPaymentInfo();

        CopyUtils.copy(paymentInfo, opsPaymentInfo);

        opsPaymentInfo.setPayStatus(paymentInfo.getPaymentStatus());

        return opsPaymentInfo;
    }

    @Deprecated
    public OpsProduct getOpsProduct(Product product) {

        OpsProduct opsProduct = new OpsProduct();

        opsProduct.setCancelDateZDT(DateTimeUtil.formatBEDateTimeZone(product.getCancelDate()));

        opsProduct.setSupplierID(product.getSupplierID());

        opsProduct.setOrderID(product.getOrderID());

        opsProduct.setAmendDateZDT(DateTimeUtil.formatBEDateTimeZone(product.getAmendDate()));

        opsProduct.setLastModifiedBy(product.getLastUpdatedBy());

        opsProduct.setInventory(product.getInventory());

        opsProduct.setProductCategory(product.getProductCategory());

        opsProduct.setProductSubCategory(product.getProductSubCategory());

        OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(product.getProductCategory());
        opsProduct.setOpsProductCategory(OpsProductCategory.getProductCategory(product.getProductCategory()));

        OpsProductSubCategory aProductSubCategory = OpsProductSubCategory.getProductSubCategory(opsProductCategory,
                product.getProductSubCategory());

        opsProduct.setOpsProductSubCategory(aProductSubCategory);

        opsProduct.setOrderDetails(getOpsOrderDetails(product.getOrderDetails(), product));

        opsProduct.setProductName(getProductName(opsProduct.getOrderDetails(), opsProductCategory, aProductSubCategory));

        JSONObject credDetails = supplierDetailsService.getSupplierCredentialDetails(product.getCredentialsName());

        if (credDetails != null) {
            opsProduct.setCredentialsName(credDetails.getJSONObject("credentials").optString("name"));
        }

        //TODO: Might need to change these later
        opsProduct.setTicketingPCC(product.getTicketingPCC());
        opsProduct.setBookingPCC(product.getPseudoCityCode());
        opsProduct.setPseudoCityCode(product.getPseudoCityCode());

        opsProduct.setSupplierRateType(product.getSupplierRateType());

        opsProduct.setSupplierReservationId(product.getSupplierReservationId());

//        opsProduct.setCredentialsName(product.getCredentialsName());

        opsProduct.setSupplierRefNumber(product.getSupplierReferenceId());

        opsProduct.setRoe(product.getRoe());

        opsProduct.setAveragePriced(false);

        opsProduct.setSupplierCancellationId(product.getSupplierCancellationId());

        opsProduct.setSupplierReferenceId(product.getSupplierReferenceId());

        opsProduct.setClientReferenceId(product.getClientReferenceId());

        opsProduct.setVouchers(product.getVouchers());

        //product.getSourceSupplierName() gives ID rather than name From Booking Engine
        opsProduct.setSourceSupplierName(getSupplierNameForId(product.getSourceSupplierName()));

        opsProduct.setEnamblerSupplierName(getSupplierNameForId(product.getEnamblerSupplierName()));

        opsProduct.setSourceSupplierID(product.getSourceSupplierName());

        opsProduct.setEnamblerSupplierID(product.getEnamblerSupplierName());

        opsProduct.setCreatedAt(DateTimeUtil.formatBEDateTimeZone(product.getCreatedAt()));

         /*  String[] supplierDetails = checkSupplierNameType(opsProduct.getSupplierID());
        if(supplierDetails[0] != null) {
            switch (supplierDetails[0].toLowerCase()) {

                case "enabler":
                    opsProduct.setEnamblerSupplierName(supplierDetails[1]);
                    break;

                case "source":
                    opsProduct.setSourceSupplierName(supplierDetails[1]);
                    break;

                case "both":
                case "enabler + source":
                case "source + enabler":
                    opsProduct.setEnamblerSupplierName(product.getEnamblerSupplierName());
                    opsProduct.setSourceSupplierName(product.getSourceSupplierName());
                    break;
            }
        }*/


        opsProduct.setFinanceControlId(product.getFinanceControlId());

        opsProduct.setSupplierType(product.getSupplierType());

        return opsProduct;
    }

    public OpsProduct getOpsProduct(Product product, HashMap<String, String> suppIdToName) {

        OpsProduct opsProduct = new OpsProduct();

        opsProduct.setCancelDateZDT(DateTimeUtil.formatBEDateTimeZone(product.getCancelDate()));

        opsProduct.setSupplierID(product.getSupplierID());

        opsProduct.setOrderID(product.getOrderID());

        opsProduct.setAmendDateZDT(DateTimeUtil.formatBEDateTimeZone(product.getAmendDate()));

        opsProduct.setLastModifiedBy(product.getLastUpdatedBy());

        opsProduct.setInventory(product.getInventory());

        opsProduct.setProductCategory(product.getProductCategory());

        opsProduct.setProductSubCategory(product.getProductSubCategory());

        OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(product.getProductCategory());
        opsProduct.setOpsProductCategory(OpsProductCategory.getProductCategory(product.getProductCategory()));

        OpsProductSubCategory aProductSubCategory = OpsProductSubCategory.getProductSubCategory(opsProductCategory,
                product.getProductSubCategory());

        opsProduct.setOpsProductSubCategory(aProductSubCategory);

        opsProduct.setOrderDetails(getOpsOrderDetails(product.getOrderDetails(), product));

        opsProduct.setProductName(getProductName(opsProduct.getOrderDetails(), opsProductCategory, aProductSubCategory));

        JSONObject credDetails = supplierDetailsService.getSupplierCredentialDetails(product.getCredentialsName());

        if (credDetails != null) {
            opsProduct.setCredentialsName(credDetails.getJSONObject("credentials").optString("name"));
        }

        //TODO: Might need to change these later
        opsProduct.setTicketingPCC(product.getTicketingPCC());
        opsProduct.setBookingPCC(product.getPseudoCityCode());
        opsProduct.setPseudoCityCode(product.getPseudoCityCode());

        opsProduct.setSupplierRateType(product.getSupplierRateType());

        opsProduct.setSupplierReservationId(product.getSupplierReservationId());

//        opsProduct.setCredentialsName(product.getCredentialsName());

        opsProduct.setSupplierRefNumber(product.getSupplierReferenceId());

        opsProduct.setRoe(product.getRoe());

        opsProduct.setAveragePriced(false);

        opsProduct.setSupplierCancellationId(product.getSupplierCancellationId());

        opsProduct.setSupplierReferenceId(product.getSupplierReferenceId());

        opsProduct.setClientReferenceId(product.getClientReferenceId());

        opsProduct.setVouchers(product.getVouchers());

        String sourceSupplierID = product.getSourceSupplierName();
        String enablerSupplierID = product.getEnamblerSupplierName();
        //product.getSourceSupplierName() gives ID rather than name From Booking Engine
        opsProduct.setSourceSupplierID(sourceSupplierID);
        opsProduct.setEnamblerSupplierID(enablerSupplierID);

        String suppName = suppIdToName.get(sourceSupplierID);
        if(suppName == null){
            suppName = getSupplierNameForId(sourceSupplierID);
            suppIdToName.put(sourceSupplierID, suppName);
        }
        opsProduct.setSourceSupplierName(suppName);
        suppName = suppIdToName.get(enablerSupplierID);
        if(suppName == null){
            suppName = getSupplierNameForId(enablerSupplierID);
            suppIdToName.put(enablerSupplierID, suppName);
        }
        opsProduct.setEnamblerSupplierName(suppName);

        //As per Dhananjay's Comment on Redmine #17088.
        Boolean isGDS = isGDS(enablerSupplierID);
        opsProduct.setSettlementSupplierID(isGDS ? "BSP" : sourceSupplierID);
        opsProduct.setSettlementSupplierName(isGDS ? "BSP" : opsProduct.getSourceSupplierName());

        opsProduct.setCreatedAt(DateTimeUtil.formatBEDateTimeZone(product.getCreatedAt()));

         /*  String[] supplierDetails = checkSupplierNameType(opsProduct.getSupplierID());
        if(supplierDetails[0] != null) {
            switch (supplierDetails[0].toLowerCase()) {

                case "enabler":
                    opsProduct.setEnamblerSupplierName(supplierDetails[1]);
                    break;

                case "source":
                    opsProduct.setSourceSupplierName(supplierDetails[1]);
                    break;

                case "both":
                case "enabler + source":
                case "source + enabler":
                    opsProduct.setEnamblerSupplierName(product.getEnamblerSupplierName());
                    opsProduct.setSourceSupplierName(product.getSourceSupplierName());
                    break;
            }
        }*/


        opsProduct.setFinanceControlId(product.getFinanceControlId());

        opsProduct.setSupplierType(product.getSupplierType());

        return opsProduct;
    }

    private String getSettlementSupplierID(String sourceSupplierID, String enablerSupplierID) {

        if(isGDS(enablerSupplierID))
            return "BSP";
        return sourceSupplierID;

    }

    private String getProductName(OpsOrderDetails orderDetails, OpsProductCategory opsProductCategory, OpsProductSubCategory aProductSubCategory) {

        String productName = null;
        if (opsProductCategory == OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION) {
            if (aProductSubCategory == OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT) {
                productName = orderDetails.getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment().get(0).getMarketingAirline().getAirlineName();
                if (productName == null || productName.isEmpty())
                    productName = orderDetails.getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment().get(0).getOperatingAirline().getAirlineName();
            }
        } else if (opsProductCategory == OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION) {
            if (aProductSubCategory == OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS)
                productName = orderDetails.getHotelDetails().getHotelName();
        }
        return productName;
    }

    //modify here
    private OpsOrderDetails getOpsOrderDetails(OrderDetails orderDetails, Product product) {

        OpsOrderDetails opsOrderDetails = new OpsOrderDetails();

        String productCategory = product.getProductCategory();

        String productSubCategory = product.getProductSubCategory();

        opsOrderDetails.setHotelDetails(null);

        opsOrderDetails.setFlightDetails(null);

        opsOrderDetails.setPackageDetails(null);

        OpsProductCategory aProductCategory = OpsProductCategory.getProductCategory(productCategory);

        OpsProductSubCategory aProductSubCategory = OpsProductSubCategory.getProductSubCategory(aProductCategory, productSubCategory);

        if (aProductSubCategory != null) {
            switch (aProductCategory) {
                case PRODUCT_CATEGORY_ACCOMMODATION: {
                    switch (aProductSubCategory) {

                        case PRODUCT_SUB_CATEGORY_HOTELS: {
                            opsAccommodationAdapterService.getOpsHotelOrderDetails(orderDetails, opsOrderDetails, product);
                        }
                        break;
                    }
                }
                break;

                case PRODUCT_CATEGORY_TRANSPORTATION: {
                    switch (aProductSubCategory) {
                        case PRODUCT_SUB_CATEGORY_FLIGHT: {
                            opsFlightAdapterService.getOpsFlightDetails(orderDetails, opsOrderDetails, product);
                        }
                        break;
                    }
                }
                break;

                case PRODUCT_CATEGORY_ACTIVITIES: {
                    switch (aProductSubCategory) {
                        case PRODUCT_SUB_CATEGORY_EVENTS: {
                            opsActivitiesAdapter.getOpsActivitiesDetails(orderDetails, opsOrderDetails);
                        }
                        break;
                    }
                }
                break;

                case PRODUCT_CATEGORY_HOLIDAYS: {
                    switch (aProductSubCategory) {
                        case PRODUCT_SUB_CATEGORY_HOLIDAYS: {
                            OpsHolidaysAdapterService.getOpsHolidaysDetails(orderDetails, opsOrderDetails);
                        }
                        break;
                    }
                }
                break;
            }

            opsOrderDetails.setSupplierType(OpsSupplierType.SUPPLIER_TYPE_ONLINE.fromString(product.getSupplierType()));
            opsOrderDetails.setOpsOrderStatus(OpsOrderStatus.fromString(product.getStatus()));
            List<OpsBookingAttribute> opsBookingAttribute = new ArrayList<>();
            opsOrderDetails.setOpsBookingAttribute(opsBookingAttribute);
            opsOrderDetails.setClientCommercials(orderDetails.getOrderClientCommercials().stream().map(
                    clientCommercial -> getOpsOrderClientCommercial(clientCommercial)).collect(Collectors.toList()));

            opsOrderDetails.setSupplierCommercials(orderDetails.getOrderSupplierCommercials().stream().
                    map(supplierCommercial -> getOpsOrderSupplierCommercial(supplierCommercial)).
                    collect(Collectors.toList()));


            opsOrderDetails.setOpsBookingAttribute(product.getBookingAttribute()
                    .stream().map(aBookingAttributeMap -> aBookingAttributeMap.keySet().iterator().next()).collect(Collectors.toList()));

            opsOrderDetails.setOpsBookingAttributeValues(product.getBookingAttribute()
                    .stream().map(aBookingAttributeMap -> aBookingAttributeMap.values().iterator().next()).collect(Collectors.toSet()));
        }

        return opsOrderDetails;
    }


    public OpsOrderSupplierCommercial getOpsOrderSupplierCommercial(OrderSupplierCommercial orderSupplierCommercial) {

        OpsOrderSupplierCommercial opsOrderSupplierCommercial = new OpsOrderSupplierCommercial();

        opsOrderSupplierCommercial.setCommercialCurrency(orderSupplierCommercial.getCommercialCurrency());

        opsOrderSupplierCommercial.setCommercialType(orderSupplierCommercial.getCommercialType());

        opsOrderSupplierCommercial.setCommercialAmount(orderSupplierCommercial.getCommercialAmount());

        opsOrderSupplierCommercial.setCommercialName(orderSupplierCommercial.getCommercialName());

        opsOrderSupplierCommercial.setSuppCommId(orderSupplierCommercial.getSuppCommId());

        opsOrderSupplierCommercial.setSupplierID(orderSupplierCommercial.getSupplierID());

        opsOrderSupplierCommercial.setEligible(orderSupplierCommercial.isEligible());

        return opsOrderSupplierCommercial;
    }

    public OpsOrderClientCommercial getOpsOrderClientCommercial(OrderClientCommercial orderClientCommercial) {

        OpsOrderClientCommercial opsClientCommercial = new OpsOrderClientCommercial();

        opsClientCommercial.setCommercialEntityID(orderClientCommercial.getCommercialEntityID());

        opsClientCommercial.setCompanyFlag(orderClientCommercial.getCompanyFlag());

        opsClientCommercial.setCommercialCurrency(orderClientCommercial.getCommercialCurrency());

        opsClientCommercial.setCommercialAmount(orderClientCommercial.getCommercialAmount());

        opsClientCommercial.setCommercialType(orderClientCommercial.getCommercialType());

        opsClientCommercial.setCommercialName(orderClientCommercial.getCommercialName());

        opsClientCommercial.setClientCommId(orderClientCommercial.getClientCommId());
        opsClientCommercial.setClientID(orderClientCommercial.getClientID());
        opsClientCommercial.setParentClientID(orderClientCommercial.getParentClientID());
        opsClientCommercial.setCommercialEntityType(orderClientCommercial.getCommercialEntityType());
        opsClientCommercial.setEligible(orderClientCommercial.isEligible());

        return opsClientCommercial;
    }

    public OpsAddressDetails getOpsAddressDetails(AddressDetails addressDetails) {
        OpsAddressDetails opsAddressDetails = new OpsAddressDetails();
        if (addressDetails != null) {
            opsAddressDetails.setZipCode(addressDetails.getZip());
            opsAddressDetails.setCountryName(addressDetails.getCountry());
            opsAddressDetails.setCityName(addressDetails.getCity());

            List<String> addressLines = new ArrayList<>();
            if(addressDetails.getAddrLine1() != null) {
                addressLines.add(addressDetails.getAddrLine1());
                addressLines.add(addressDetails.getAddrLine2());
            }

            addressLines.add(addressDetails.getAddressLine());
            opsAddressDetails.setAddressLines(addressLines);
            opsAddressDetails.setState(addressDetails.getState());

            opsAddressDetails.setCountryCode(addressDetails.getCountryCode());
            opsAddressDetails.setBldgRoom(addressDetails.getBldgRoom());
        }
        return opsAddressDetails;
    }

    public OpsAncillaryInfo getOpsAncillaryInfo(AncillaryInfo ancillaryInfo) {
        OpsAncillaryInfo opsAncillaryInfo = new OpsAncillaryInfo();
        opsAncillaryInfo.setUnit(ancillaryInfo.getUnit());
        opsAncillaryInfo.setAmount(ancillaryInfo.getAmount());
        opsAncillaryInfo.setQuantity(ancillaryInfo.getQuantity());
        opsAncillaryInfo.setDescription(ancillaryInfo.getDescription());
        opsAncillaryInfo.setType(ancillaryInfo.getType());
        return opsAncillaryInfo;
    }

    public OpsAncillaryServices getOpsAncillaryServices(AncillaryServices ancillaryServices) {
        OpsAncillaryServices opsAncillaryServices = new OpsAncillaryServices();
        if (ancillaryServices != null && ancillaryServices.getAncillaryInfo() != null) {
            List<AncillaryInfo> ancillaryInfoList = ancillaryServices.getAncillaryInfo();
            if (ancillaryInfoList.size() > 0) {
                ArrayList<OpsAncillaryInfo> opsAncillaryInfos = new ArrayList<>();
                for (AncillaryInfo anAncillaryInfo : ancillaryInfoList) {
                    opsAncillaryInfos.add(getOpsAncillaryInfo(anAncillaryInfo));
                }
                opsAncillaryServices.setAncillaryInfo(opsAncillaryInfos);
            }
        }
        return opsAncillaryServices;
    }

    public OpsContactDetails getOpsContactDetail(ContactDetail contactDetail) {
        OpsContactDetails opsContactDetail = new OpsContactDetails();
        opsContactDetail.setContactInfo(getOpsContactInfo(contactDetail.getContactInfo()));
        return opsContactDetail;
    }

    public OpsContactInfo getOpsContactInfo(ContactInfo contactInfo) {
        OpsContactInfo opsContactInfo = new OpsContactInfo();
        if(contactInfo != null) {
            opsContactInfo.setAreaCityCode(contactInfo.getAreaCityCode());
            opsContactInfo.setCountryCode(contactInfo.getCountryCode());
            opsContactInfo.setContactType(contactInfo.getContactType());
            opsContactInfo.setMobileNo(contactInfo.getMobileNo());
            opsContactInfo.setEmail(contactInfo.getEmail());
        }
        return opsContactInfo;
    }

    public OpsTaxes getOpsTaxes(Taxes taxes) {

        OpsTaxes opsTaxes = new OpsTaxes();
        if (taxes != null) {
            opsTaxes.setCurrencyCode(taxes.getCurrencyCode());
            if (taxes.getAmount() != null) {
                opsTaxes.setAmount(taxes.getAmount());
            } else {
                opsTaxes.setAmount(taxes.getTotal());
            }


            if (taxes.getTax() != null && taxes.getTax().size() > 0)
                opsTaxes.setTax(taxes.getTax().stream().map(tax1 -> getOpsTax(tax1)).collect(Collectors.toList()));
            else
                opsTaxes.setTax(new ArrayList<OpsTax>());
        }
        return opsTaxes;
    }


    public OpsTotalFare getOpsTotalFare(TotalFare totalFare) {
        OpsTotalFare opsTotalFare = new OpsTotalFare();
        opsTotalFare.setCurrencyCode(totalFare.getCurrencyCode());
        opsTotalFare.setAmount(Double.valueOf(totalFare.getAmount()));
        return opsTotalFare;
    }


    public OpsTaxBreakUp getOpsTax(Tax tax) {

        OpsTaxBreakUp opsTax = new OpsTaxBreakUp();

        opsTax.setCurrencyCode(tax.getCurrencyCode());

        opsTax.setTaxCode(tax.getTaxCode());

        opsTax.setAmount(tax.getAmount().doubleValue());

        return opsTax;
    }


    public OpsMealInfo getOpsMealInfo(MealInfo mealInfo) {

        OpsMealInfo opsMealInfo = new OpsMealInfo();

        opsMealInfo.setMealID(mealInfo.getMealID());

        opsMealInfo.setMealName(mealInfo.getMealName());

        return opsMealInfo;

    }

    public String[] checkSupplierNameType(String supplierId) {
        String[] supplierDetails = new String[2];
        try {
            String jsonSupplier = supplierDetailsService.getSupplierDetails(supplierId);

            if (!StringUtils.isEmpty(jsonSupplier)) {
                supplierDetails[0] = String.valueOf(jsonObjectProvider.getChildObject(jsonSupplier,
                        "$.supplier.supplierType", String.class));

                supplierDetails[1] = String.valueOf(jsonObjectProvider.getChildObject(jsonSupplier,
                        "$.supplier.name", String.class));
            }
        } catch (OperationException e) {
            logger.error(e);
        }
        return supplierDetails;
    }


    public String getSupplierNameForId(String supplierId) {

        if (supplierId == null || supplierId.isEmpty())
            return null;

        String supplierName = null;
        try {
            String jsonSupplier = supplierDetailsService.getSupplierDetails(supplierId);
            if (!StringUtils.isEmpty(jsonSupplier)) {
                supplierName = String.valueOf(jsonObjectProvider.getChildObject(jsonSupplier,
                        "$.supplier.name", String.class));
            }
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return supplierName;
    }

    public Boolean isGDS(String supplierId) {

        if (supplierId == null || supplierId.isEmpty())
            return null;

        try {
            String jsonSupplier = supplierDetailsService.getSupplierDetails(supplierId);
            if (!StringUtils.isEmpty(jsonSupplier)) {
                JSONObject supplierDtls = new JSONObject(jsonSupplier);
                JSONArray enablerCategories = supplierDtls.getJSONObject("supplier").optJSONArray("enablerCategory");
                for(int i=0; enablerCategories!=null && i<enablerCategories.length();i++)
                    if(enablerCategories.getString(0).equalsIgnoreCase("GDS"))
                        return true;

            }
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return false;
    }

    public OpsCompanyTaxes getCompanyTaxes(CompanyTaxes companyTaxes) {

        OpsCompanyTaxes opsCompanyTaxes = null;

        if (companyTaxes != null) {
            opsCompanyTaxes = new OpsCompanyTaxes();
            opsCompanyTaxes.setCurrencyCode(companyTaxes.getCurrencyCode());
            opsCompanyTaxes.setAmount(companyTaxes.getAmount());

            if (companyTaxes.getCompanyTax() != null && companyTaxes.getCompanyTax().size() > 0)
                opsCompanyTaxes.setCompanyTax(companyTaxes.getCompanyTax().stream().map(tax1 -> getOpsCompanyTax(tax1)).collect(Collectors.toList()));
            else
                opsCompanyTaxes.setCompanyTax(new ArrayList<OpsCompanyTax>());
        }
        return opsCompanyTaxes;
    }

    public OpsCompanyTax getOpsCompanyTax(CompanyTax tax) {

        OpsCompanyTax opsTax = new OpsCompanyTax();

        opsTax.setCurrencyCode(tax.getCurrencyCode());
        opsTax.setAmount(tax.getAmount());
        opsTax.setTaxCode(tax.getTaxCode());
        opsTax.setHsnCode(tax.getHsnCode());
        opsTax.setSacCode(tax.getSacCode());
        opsTax.setTaxComponent(tax.getTaxComponent());
        opsTax.setTaxPercent(tax.getTaxPercent());
        return opsTax;
    }
    
    public OpsReceivables getOpsReceivables(Receivables receivables) {

        OpsReceivables opsReceivables = new OpsReceivables();

        opsReceivables.setAmount(receivables.getAmount());

        opsReceivables.setCurrencyCode(receivables.getCurrencyCode());

        opsReceivables.setReceivable(receivables.getReceivable().stream().map(aReceivable
                -> getOpsReceivable(aReceivable)).collect(Collectors.toList()));

        return opsReceivables;
    }

    private OpsReceivable getOpsReceivable(Receivable receivable) {

        OpsReceivable opsReceivable = new OpsReceivable();

        opsReceivable.setAmount(receivable.getAmount());

        opsReceivable.setCode(receivable.getCode());

        opsReceivable.setCurrencyCode(receivable.getCurrencyCode());

        return opsReceivable;
    }
}

