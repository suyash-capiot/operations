package com.coxandkings.travel.operations.service.managedocumentation.impl;

import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.managedocumentation.DocumentMDMService;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DocumentMDMServiceImpl implements DocumentMDMService {

    private static final Logger logger = LogManager.getLogger(DocumentMDMServiceImpl.class);

    @Value("${mdm.company}")
    private String companyURL;

    @Value("${mdm.common.supplier.supplier-by-id}")
    private String supplierURL;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Value("${offline-booking.product-accommodation}")
    private String accoUrl;

    @Autowired
    private ClientMasterDataService clientMasterDataService;


    @Override
    public String getCompanyName(String companyId) throws OperationException {
        String url = companyURL.concat("?filter={\"_id\":\"" + companyId + "\"}");
        String companyResponse = null;
        URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();

        try {
            companyResponse = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
        } catch (Exception e) {
            logger.error("Unable to get company details:" + companyId, e);
        }

        if (StringUtils.isEmpty(companyResponse)) {
            logger.error("No Company found:" + companyId);
            return "NA";
        }
        try {
            return ((String[]) jsonObjectProvider.getChildObject(companyResponse, "$.data[:1].name", String[].class))[0];
        } catch (Exception e) {
            logger.error("Unable to get company details:" + companyId);
            return "NA";
        }


    }

    @Override
    public String getSupplierName(String supplierId) throws OperationException {

        try {
            return jsonObjectProvider.getAttributeValue(getSupplier(supplierId), "$.supplier.name", String.class);
        } catch (Exception e) {
            logger.error("Unable to get Supplier details:" + supplierId, e);
            return "NA";
        }

    }

    private String getSupplier(String supplierId) throws OperationException {

        String supplierAPI = supplierURL.concat(supplierId);
        String supplierResponse;
        try {
            supplierResponse = mdmRestUtils.exchange(supplierAPI, HttpMethod.GET, String.class);
        } catch (Exception e) {
            throw new OperationException("Unable to get data for supplier:" + supplierId);
        }


        if (StringUtils.isEmpty(supplierResponse)) {
            logger.error("No Supplier found:" + supplierId);
            return "NA";
        }
        return supplierResponse;
    }

    @Override
    public String supplierContact(String supplierId) throws OperationException {
        try {
            String supplierResponse = getSupplier(supplierId);
            return ((String[]) jsonObjectProvider.getChildObject(supplierResponse, "$.contactInfo.contactDetails.numbers[:1].phone.countryCode", String[].class))[0]
                    + "-" + ((String[]) jsonObjectProvider.getChildObject(supplierResponse, " $.contactInfo.contactDetails.numbers[:1].phone.cityCode", String[].class))[0]
                    + "-" + ((String[]) jsonObjectProvider.getChildObject(supplierResponse, "$.contactInfo.contactDetails.numbers[:1].phone.number", String[].class))[0];
        } catch (Exception e) {
            logger.error("Unable to get Supplier details:" + supplierId, e);
            return "NA";
        }
    }

    @Override
    public Map<String, String> getHotelDetails(String hotelName) {
        String accoBuildUrl = accoUrl.concat("?filter= {\"accomodationInfo.name\" :  \"" + hotelName + "\"}");
        URI accoUri = UriComponentsBuilder.fromUriString(accoBuildUrl).build().encode().toUri();
        String accoResponse = null;
        try {
            accoResponse = mdmRestUtils.exchange(accoUri, HttpMethod.GET, String.class);
        } catch (OperationException e) {
            logger.error("Unable to get Hotel Details from MDM", e);
        }

        String name[] = (String[]) jsonObjectProvider.getChildObject(accoResponse, "$.data[:1].accomodationInfo.displayName", String[].class);
        String hotelAddressStreet[] = (String[]) jsonObjectProvider.getChildObject(accoResponse, "$.data[:1].accomodationInfo.address.street", String[].class);
        String hotelAddressCountry[] = (String[]) jsonObjectProvider.getChildObject(accoResponse, "$.data[:1].accomodationInfo.address.country", String[].class);
        String hotelAddressState[] = (String[]) jsonObjectProvider.getChildObject(accoResponse, "$.data[:1].accomodationInfo.address.state", String[].class);
        String hotelAddressCity[] = (String[]) jsonObjectProvider.getChildObject(accoResponse, "$.data[:1].accomodationInfo.address.city", String[].class);

        String address = hotelAddressStreet + "<br/>" + hotelAddressCity + ", " + hotelAddressState + ", " + hotelAddressCountry;

        String contact = "";
        Map<String, String> hotelMap = new ConcurrentHashMap<>();
        hotelMap.put("name", name[0]);
        hotelMap.put("address", hotelAddressStreet[0]);
        hotelMap.put("city", hotelAddressCity[0]);
        hotelMap.put("state", hotelAddressState[0]);
        hotelMap.put("country", hotelAddressCountry[0]);
        //Todo:content details not getting
        hotelMap.put("contact", contact);

        return hotelMap;
    }

    @Override
    public String getClientName(String clientId, MDMClientType clientType) throws OperationException {
        String clientName = null;
        if (MDMClientType.B2B == clientType) {
            clientName = clientMasterDataService.getB2BClientNames(Collections.singletonList(clientId)).get(clientId);
        } else if (MDMClientType.B2C == clientType) {
            clientName = clientMasterDataService.getB2CClientNames(Collections.singletonList(clientId)).get(clientId);
        }
        return clientName;

    }
}
