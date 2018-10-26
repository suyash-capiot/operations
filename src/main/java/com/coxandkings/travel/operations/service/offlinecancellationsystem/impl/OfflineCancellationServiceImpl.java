package com.coxandkings.travel.operations.service.offlinecancellationsystem.impl;

import com.coxandkings.travel.operations.criteria.offlinecancellationsystem.OfflineCancellationCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.offlinecancellationsystem.OfflineCancellation;
import com.coxandkings.travel.operations.resource.offlinecancellationsystem.OfflineCancellationRespon;
import com.coxandkings.travel.operations.resource.offlinecancellationsystem.OfflineCancellationSupplierMarkets;
import com.coxandkings.travel.operations.resource.offlinecancellationsystem.OfflineSupplierCredentials;
import com.coxandkings.travel.operations.service.booking.impl.OpsBookingServiceImpl;
import com.coxandkings.travel.operations.service.offlinecancellationsystem.OfflineCancellationService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;


@Service
public class OfflineCancellationServiceImpl implements OfflineCancellationService {
    private static final Logger logger = Logger.getLogger(OfflineCancellationServiceImpl.class);
    @Autowired
    MDMRestUtils mdmRestUtils;
    @Value(value = "${supplier-amendments.mdm.get-suppliers}")
    String getSupplierUrl;
    @Value(value = "${supplier-amendments.mdm.supplier-credentials}")
    String getSupplierCredentials;
    @Value(value = "${supplier-amendments.mdm.get-supplier-markets}")
    String getSupplierMarkets;
    @Value(value = "${supplier-amendments.mdm.get-supplier-ancillary}")
    String getSupplierAncillary;
    @Value(value = "${supplier-amendments.mdm.get-supplier-ancillary-filter}")
    String getSupplierAncillaryFilter;
    @Autowired
    OpsBookingServiceImpl opsBookingService;
    @Autowired
    private JsonObjectProvider jsonFilter;

    @Override
    public OfflineCancellationRespon doCancellation(OfflineCancellationCriteria offlineCancellationCriteria) throws OperationException ,IOException, IllegalAccessException {
        String supplierDetails = null;
        String supplierId = null;
        List<OpsProduct> opsProductList = null;
        HashSet<String> supplierMarkets = null;
        OpsBooking opsBookings = null;
        opsBookings = opsBookingService.getBooking(offlineCancellationCriteria.getBookingId());
        if (opsBookings != null)
            opsProductList = opsBookings.getProducts();
        OfflineCancellationSupplierMarkets supplierMarketswe = null;

        if (!opsProductList.isEmpty() && opsProductList != null) {
            for (OpsProduct opsProduct : opsProductList) {
                if (opsProduct.getOrderID().equalsIgnoreCase(offlineCancellationCriteria.getOrderId())) {

                    supplierMarketswe = getSupplierMarketsList(opsProduct.getSupplierID());
                    supplierMarkets = supplierMarketswe.getSupplierMarkets();

                }
            }
        }
        // OfflineCancellationResponse offlineCancellationResponse = new OfflineCancellationResponse();
        OfflineCancellationRespon offlineCancellationRespon = new OfflineCancellationRespon();
        List<OfflineCancellation> offlineCancellations = new ArrayList<OfflineCancellation>();

        List<OfflineSupplierCredentials> offlineSupplierCredentialsList = new ArrayList<>();


        for (String supplierMarket : supplierMarkets) {
            OfflineCancellation offlineCancellation = new OfflineCancellation();
            offlineCancellation.setMarket(supplierMarket);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("supplier.supplierId", supplierId);
            jsonObject.put("supplierMarkets", supplierMarket);

            try {
                URI uri = UriComponentsBuilder.fromUriString(getSupplierUrl + jsonObject.toString()).build().encode().toUri();
                logger.info("url - " + uri);
                supplierDetails = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);

            } catch (Exception e) {
                // e.printStackTrace();
                logger.error("Error occurred while loading  Supplier details", e);
                throw new IllegalArgumentException();
            }
            Object credentials = jsonFilter.getChildObject(supplierDetails, getSupplierCredentials, Object.class);
            ArrayList<ArrayList<LinkedHashMap<String, String>>> credentialsList = (ArrayList<ArrayList<LinkedHashMap<String, String>>>) credentials;
//$.data[*].credentialDetails.endPointUrls


            for (ArrayList<LinkedHashMap<String, String>> credential : credentialsList) {
                OfflineSupplierCredentials offlineSupplierCredentials = new OfflineSupplierCredentials();
                for (LinkedHashMap<String, String> credit : credential) {
                    String value = credit.get("value");
                    String title = credit.get("title");

                    if (value != null && title != null && !value.isEmpty() && !title.isEmpty() || title.equalsIgnoreCase("Url") || title.equalsIgnoreCase("Username") || title.equalsIgnoreCase("Password")) {
                        if (title.equalsIgnoreCase("Url")) {
                            offlineSupplierCredentials.setUrl(value);
                        }
                        if (title.equalsIgnoreCase("Username")) {
                            offlineSupplierCredentials.setUserName(value);
                        }
                        if (title.equalsIgnoreCase("Password")) {
                            offlineSupplierCredentials.setPassword(value);
                        }


                    }
                }
                offlineSupplierCredentialsList.add(offlineSupplierCredentials);
            }
            offlineCancellation.setOfflineSupplierCredentialsList(offlineSupplierCredentialsList);

            offlineCancellations.add(offlineCancellation);
        }
        /*List<OfflineCancellationResource> credentialsList = jsonFilter.getChildrenCollection(supplierDetails, "$.credentialDetails.credentials[0:]", OfflineCancellationResource.class);*/


       /* Iterator<JsonNode> jsonNodeIterator = objectMapper.readTree(supplierDetails).path("supplierMarkets").elements();
        List<String> supplierMarketsList = new ArrayList<>();
        while (jsonNodeIterator.hasNext()) {
            supplierMarketsList.add(jsonNodeIterator.next().textValue());
        }*/

        offlineCancellationRespon.setOfflineSupplierCredentials(offlineCancellations);

        return offlineCancellationRespon;
    }


    @Override
    public OfflineCancellationSupplierMarkets getSupplierMarketsList(String supplierId) throws IllegalAccessException {
        OfflineCancellationSupplierMarkets offlineCancellationSupplierMarkets = new OfflineCancellationSupplierMarkets();
        String supplierDetails = null;
        HashSet<String> marketsList = new HashSet<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("supplier.supplierId", supplierId);

        try {
            URI uri = UriComponentsBuilder.fromUriString(getSupplierUrl + jsonObject.toString()).build().encode().toUri();
            supplierDetails = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);

        } catch (Exception e) {
            // e.printStackTrace();
            logger.error("Error occurred while loading Markets Details", e);
            throw new IllegalAccessException();
        }

        Object supplierMarketsList = jsonFilter.getChildObject(supplierDetails, getSupplierMarkets, Object.class);

        ArrayList<ArrayList<String>> supplierMarkets = (ArrayList<ArrayList<String>>) supplierMarketsList;


        for (ArrayList<String> supplierMarket : supplierMarkets) {
            for (String getMarket : supplierMarket) {
                marketsList.add(getMarket);
            }
        }
        offlineCancellationSupplierMarkets.setSupplierMarkets(marketsList);
        return offlineCancellationSupplierMarkets;
    }


    @Override
    public OfflineCancellationRespon doAmendment(OfflineCancellationCriteria offlineCancellationCriteria) throws OperationException,IOException, IllegalAccessException {
        URI uri = null;
        char arr[];
        Object credentials = null;
        String supplierDetails = null;
        ArrayList<String> al = null;
        String supplierId = null;
        List<OpsProduct> opsProductList = null;
        HashSet<String> supplierMarkets = null;
        OpsBooking opsBookings = opsBookingService.getBooking(offlineCancellationCriteria.getBookingId());
        if (opsBookings != null)
            opsProductList = opsBookings.getProducts();
        OfflineCancellationSupplierMarkets supplierMarketswe = null;

        OfflineCancellationRespon offlineCancellationRespon = new OfflineCancellationRespon();
        List<OfflineCancellation> offlineCancellations = new ArrayList<OfflineCancellation>();

//$.[0].data.value
        if (!opsProductList.isEmpty() && opsProductList != null) {
            for (OpsProduct opsProduct : opsProductList) {
                if (opsProduct.getOrderID().equalsIgnoreCase(offlineCancellationCriteria.getOrderId())) {
                    supplierId = opsProduct.getSupplierID();
                    supplierMarketswe = getSupplierMarketsList(opsProduct.getSupplierID());
                    supplierMarkets = supplierMarketswe.getSupplierMarkets();
                }
            }
        }


        for (String marketId : supplierMarkets) {
            List<OfflineSupplierCredentials> offlineSupplierCredentialsList = new ArrayList<>();
            OfflineCancellation offlineCancellation = new OfflineCancellation();
            offlineCancellation.setMarket(marketId);

            OfflineSupplierCredentials offlineSupplierCredentials = new OfflineSupplierCredentials();

            //  http://10.25.6.103:10002/ancillary/v1/SUPP100111_India_username
            try {
                // uri = UriComponentsBuilder.fromUriString("http://10.25.6.103:10002/ancillary/v1/"+supplierId+"_"+marketId+"_username").build().encode().toUri();
                uri = UriComponentsBuilder.fromUriString(getSupplierAncillary + supplierId + "_" + marketId + "_username").build().encode().toUri();
                logger.info("url - " + uri);
                supplierDetails = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                arr = supplierDetails.toCharArray();
            } catch (Exception e) {
                // e.printStackTrace();
                logger.error("Error occurred while loading  Supplier details", e);
                throw new IllegalArgumentException();
            }
            if (supplierDetails != null && !supplierDetails.isEmpty() && arr.length > 2) {
                credentials = jsonFilter.getChildObject(supplierDetails, getSupplierAncillaryFilter, Object.class);
                // al = (ArrayList<String>) credentials;
                String aasad = (String) credentials;
                if (aasad != null)
                    offlineSupplierCredentials.setUserName(aasad);
            }
//******************************************for-Password--------------------------------------------------------------

            try {
                uri = UriComponentsBuilder.fromUriString(getSupplierAncillary + supplierId + "_" + marketId + "_password").build().encode().toUri();
                logger.info("url - " + uri);
                supplierDetails = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                arr = supplierDetails.toCharArray();
            } catch (Exception e) {
                // e.printStackTrace();
                logger.error("Error occurred while loading  Supplier details", e);
                throw new IllegalArgumentException();
            }
            if (supplierDetails != null && !supplierDetails.isEmpty() && arr.length > 2) {
                credentials = jsonFilter.getChildObject(supplierDetails, getSupplierAncillaryFilter, Object.class);
                String aasader = (String) credentials;
                if (aasader != null)
                    offlineSupplierCredentials.setPassword(aasader);
            }

//***********************************URL**********************************************************************************

            try {
                uri = UriComponentsBuilder.fromUriString(getSupplierAncillary + supplierId + "_" + marketId + "_URL").build().encode().toUri();
                logger.info("url - " + uri);
                supplierDetails = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                arr = supplierDetails.toCharArray();

            } catch (Exception e) {
                // e.printStackTrace();
                logger.error("Error occurred while loading  Supplier details", e);
                throw new IllegalArgumentException();
            }
            if (supplierDetails != null && !supplierDetails.isEmpty() && arr.length > 2) {
                credentials = jsonFilter.getChildObject(supplierDetails, getSupplierAncillaryFilter, Object.class);
                String aasadasf = (String) credentials;
                if (aasadasf != null)
                    offlineSupplierCredentials.setUrl(aasadasf);
            }
//***********************************************Form_UserName field*****************************************************
            try {
                uri = UriComponentsBuilder.fromUriString(getSupplierAncillary + supplierId + "_" + marketId + "_form_username_field").build().encode().toUri();
                logger.info("url - " + uri);
                supplierDetails = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                arr = supplierDetails.toCharArray();
            } catch (Exception e) {
                // e.printStackTrace();
                logger.error("Error occurred while loading  Supplier details", e);
                throw new IllegalArgumentException();
            }

            if (supplierDetails != null && !supplierDetails.isEmpty() && arr.length > 2) {
                credentials = jsonFilter.getChildObject(supplierDetails, getSupplierAncillaryFilter, Object.class);
                String aasadasf12 = (String) credentials;
                if (aasadasf12 != null)
                    offlineSupplierCredentials.setForm_userName(aasadasf12);
            }
//*****************************************Form_Password_Field**********************************************************
            try {
                uri = UriComponentsBuilder.fromUriString(getSupplierAncillary + supplierId + "_" + marketId + "_form_password_field").build().encode().toUri();
                logger.info("url - " + uri);
                supplierDetails = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                arr = supplierDetails.toCharArray();
            } catch (Exception e) {
                // e.printStackTrace();
                logger.error("Error occurred while loading  Supplier details", e);
                throw new IllegalArgumentException();
            }
            if (supplierDetails != null && !supplierDetails.isEmpty() && arr.length > 2) {
                credentials = jsonFilter.getChildObject(supplierDetails, getSupplierAncillaryFilter, Object.class);
                String aasadasf123 = (String) credentials;
                if (aasadasf123 != null)
                    offlineSupplierCredentials.setForm_password(aasadasf123);
            }

            offlineSupplierCredentialsList.add(offlineSupplierCredentials);
            offlineCancellation.setOfflineSupplierCredentialsList(offlineSupplierCredentialsList);
            offlineCancellations.add(offlineCancellation);
        }

        offlineCancellationRespon.setOfflineSupplierCredentials(offlineCancellations);


        return offlineCancellationRespon;
    }


}
