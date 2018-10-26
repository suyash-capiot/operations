package com.coxandkings.travel.operations.service.reconfirmation.mdm;

import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationConfigFor;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationFilter;
import com.coxandkings.travel.operations.service.reconfirmation.common.*;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.jayway.jsonpath.JsonPath;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service("reconfirmationMDMService")
public class ReconfirmationMDMServiceImpl implements ReconfirmationMDMService {

    @Autowired
    private ReconfirmationUtilityService reconfirmationUtilityService;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    //Reconfirmation Configuration
    @Value(value = "${reconfirmation.mdm.reconfirmation-config-search}")
    private String reconfigurationConfigSearchUrl;

    //Client
    @Value(value = "${reconfirmation.mdm.clientB2B}")
    private String getB2BClientById;


    @Value(value = "${reconfirmation.mdm.clientB2C}")
    private String getB2CClientById;

    //Supplier
    @Value(value = "${reconfirmation.mdm.supplier}")
    private String getSupplierById;

    //Client Path expression
    @Value(value = "${reconfirmation.path-expression.client.reconfirmationCutOff}")
    private String clientReconfirmationCutOff;

    @Value(value = "${reconfirmation.path-expression.client.durationType}")
    private String clientReconfirmationDurationType;

    @Value(value = "${reconfirmation.path-expression.client.duration}")
    private String clientReconfirmationDuration;

    @Value(value = "${reconfirmation.path-expression.client.reconfirmationSentTo}")
    private String clientReconfirmationToBeSentTo;

    @Value(value = "${reconfirmation.path-expression.client.configurationFor}")
    private String clientConfigurationFor;

    //Supplier Path expression
    @Value(value = "${reconfirmation.path-expression.supplier.reconfirmationCutOff}")
    private String supplierReconfirmationCutOff;

    @Value(value = "${reconfirmation.path-expression.supplier.durationType}")
    private String supplierReconfirmationDurationType;

    @Value(value = "${reconfirmation.path-expression.supplier.duration}")
    private String supplierReconfirmationDuration;

    @Value(value = "${reconfirmation.path-expression.supplier.reconfirmationToBeSentTo}")
    private String supplierReconfirmationToBeSentTo;

    @Value(value = "${reconfirmation.path-expression.supplier.configurationFor}")
    private String supplierConfigurationFor;

    @Value("${mdm.common.client.b2c_client}")
    private String clientB2CUrl;

    @Autowired
    private MDMRestUtils mdmRestUtil;

    private static Logger logger = LogManager.getLogger(ReconfirmationMDMServiceImpl.class);




    /**
     * @param configurationFor
     * @param productCategory
     * @param productCatSubtype
     * @param productNameSubType
     * @param productFlavor
     * @param orderID
     * @param supplierName
     * @param supplierId
     * @return
     * @throws OperationException
     */
    @Override
    public SupplierConfiguration getReconfirmationConfigForSupplier(String configurationFor, String productCategory, String productCatSubtype,
                                                                    String productNameSubType, String productFlavor, String orderID, String supplierName, String supplierId) throws OperationException {
        SupplierConfiguration supplierConfiguration = new SupplierConfiguration();
        String supplierDetails = getConfiguration(null, productCategory, productCatSubtype, orderID, null, null);
        return getReconfirmationConfigForSupplier(supplierDetails);
    }

    /**
     * @param supplierDetails
     * @return
     * @throws OperationException
     */
    public SupplierConfiguration getReconfirmationConfigForSupplier(String supplierDetails) throws OperationException {
        SupplierConfiguration supplierConfiguration = new SupplierConfiguration();
        String reconfirmationCutOff = jsonObjectProvider.getAttributeValue(supplierDetails, supplierReconfirmationCutOff, String.class);
        String dureationType = jsonObjectProvider.getAttributeValue(supplierDetails, supplierReconfirmationDurationType, String.class);
        Object duration = null;
        try {
            duration = JsonPath.parse(supplierDetails).read(supplierReconfirmationDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String reconfirmationToBeSentTo = jsonObjectProvider.getAttributeValue(supplierDetails, supplierReconfirmationToBeSentTo, String.class);
        String configuredFor = jsonObjectProvider.getAttributeValue(supplierDetails, supplierConfigurationFor, String.class);

        supplierConfiguration.setReconfirmationCutOff(reconfirmationCutOff);
        supplierConfiguration.setDurationType(dureationType);
        if (duration != null) {
            supplierConfiguration.setDuration((int) duration);
        }
        supplierConfiguration.setReconfirmationToBeSentTo(reconfirmationToBeSentTo);
        supplierConfiguration.setConfigurationFor(configuredFor);
        return supplierConfiguration;
    }

    /**
     * @param configurationFor
     * @param productCategory
     * @param productCatSubtype
     * @param productNameSubType
     * @param productFlavor
     * @param orderID
     * @param clientId
     * @param clientType
     * @return
     * @throws OperationException
     */
    @Override
    public ClientConfiguration getReconfirmationConfigurationForClient(String configurationFor, String productCategory, String productCatSubtype,
                                                                       String productNameSubType, String productFlavor, String orderID, String clientId, String clientType) throws OperationException {
        String clientConfigurationDetails = getConfiguration(null, productCategory, productCatSubtype, orderID, null, null);
        return getReconfirmationConfigurationForClient(clientConfigurationDetails);
    }

    /**
     * @param clientConfigurationDetails
     * @return
     * @throws OperationException
     */
    public ClientConfiguration getReconfirmationConfigurationForClient(String clientConfigurationDetails) throws OperationException {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        String reconfirmationCutOff = jsonObjectProvider.getAttributeValue(clientConfigurationDetails, clientReconfirmationCutOff, String.class);
        String dureationType = jsonObjectProvider.getAttributeValue(clientConfigurationDetails, clientReconfirmationDurationType, String.class);
        Object duration = null;
        try {
            duration = JsonPath.parse(clientConfigurationDetails).read(clientReconfirmationDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String reconfirmationToBeSentTo = jsonObjectProvider.getAttributeValue(clientConfigurationDetails, clientReconfirmationToBeSentTo, String.class);
        String configuredFor = jsonObjectProvider.getAttributeValue(clientConfigurationDetails, clientReconfirmationToBeSentTo, String.class);
        clientConfiguration.setReconfirmationCutOff(reconfirmationCutOff);
        clientConfiguration.setDurationType(dureationType);
        if (duration != null) {
            clientConfiguration.setDuration((int) duration);
        }
        clientConfiguration.setReconfirmationToBeSentTo(reconfirmationToBeSentTo);
        clientConfiguration.setConfigurationFor(configuredFor);
        return clientConfiguration;
    }

    /**
     * @param configurationFor
     * @param productCategory
     * @param productCatSubtype
     * @param productNameSubType
     * @param productFlavor
     * @param orderID
     * @param supplierName
     * @param supplierId
     * @return
     */
    private String makeURLForReconfirmationConfig(String configurationFor, String productCategory, String productCatSubtype,
                                                  String productNameSubType, String productFlavor, String orderID, String supplierName, String supplierId) {

        SupplierReconfirmationFilter filter = new SupplierReconfirmationFilter();
        filter.setProductCatSubtype(productCatSubtype);
        filter.setProductCategory(productCategory);
        filter.setConfigurationFor(configurationFor);
        filter.setProductFlavor(productFlavor);
        filter.setProductId(orderID);
        filter.setProductNameSubType(productNameSubType);
        filter.setSupplierId(supplierId);
        filter.setSupplierName(supplierName);
        return filter.getUrl(filter);
    }

    /**
     * @param supplierId
     * @return
     * @throws OperationException
     */
    @Override
    public CustomSupplierDetails getSupplierContactDetails(String supplierId) throws OperationException {
        CustomSupplierDetails customSupplierDetails = new CustomSupplierDetails();
        try {
            String supplierDetails = this.supplierDetails(supplierId);
            String supplierEmail = jsonObjectProvider.getAttributeValue(supplierDetails, "$.contactInfo.contactDetails.email", String.class);
            String supplierName = jsonObjectProvider.getAttributeValue(supplierDetails, "$.supplier.name", String.class);
            customSupplierDetails.setEmailId(supplierEmail != null ? supplierEmail : "");
            customSupplierDetails.setSupplierName(supplierName != null ? supplierName : "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customSupplierDetails;
    }

    /**
     * @param clientId
     * @param clientType
     * @return
     * @throws OperationException //getB2CClientById
     */
    @Override
    public CustomClientDetails getB2BClientContactDetails(String clientId, String clientType) throws OperationException {
        CustomClientDetails customClientDetails = new CustomClientDetails();

        if (clientType.equalsIgnoreCase("B2B")) {
            try {
                String clientDetails = getB2BClientById(clientId);
                String email = jsonObjectProvider.getAttributeValue(clientDetails, "$.adminUserDetails.users[0].email", String.class);
                String firstName = jsonObjectProvider.getAttributeValue(clientDetails, "$.adminUserDetails.users[0].firstName", String.class);
                String title = jsonObjectProvider.getAttributeValue(clientDetails, "$.adminUserDetails.users[0].title", String.class);
                String lastName = jsonObjectProvider.getAttributeValue(clientDetails, "$.adminUserDetails.users[0].lastName", String.class);
                customClientDetails.setEmailId(email);
                customClientDetails.setFirstName(title.concat(firstName));
                customClientDetails.setLastName(lastName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (clientType.equalsIgnoreCase("B2C")) {
            try {
                String email = getB2CClientEmail(clientId);
                customClientDetails.setEmailId(email);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return customClientDetails;
    }




    public String getB2CClientEmail(String clientID) throws OperationException {

        return jsonObjectProvider.getAttributeValue(getB2CClient(clientID), "  $.data[:1].travellerDetails.employmentDetails.officeEmailId", String.class);
    }

    public String getB2CClient(String clientId) throws OperationException {
        String b2cUrl = clientB2CUrl.concat("?filter={ \"_id\":\"").concat(clientId).concat("\"}");
        URI url = UriComponentsBuilder.fromUriString(b2cUrl).build().encode().toUri();
        String responseJson = null;
        try {
            responseJson = mdmRestUtil.exchange(url, HttpMethod.GET, String.class);
        } catch (Exception e) {
            logger.error("Not able to get data from MDM ", e);
            throw new OperationException("Not able to get data from MDM");
        }
        if (responseJson != null) {
            responseJson = jsonObjectProvider.getChildJSON(responseJson, "$.data[0]");
        } else {
            throw new OperationException("No client data found for client id " + clientId);
        }

        return responseJson;
    }

    /**
     * @param clientId
     * @param clientType
     * @return
     * @throws OperationException
     */
    public CustomClientDetails getB2CClientContactDetails(String clientId, String clientType) throws OperationException {
        CustomClientDetails customClientDetails = new CustomClientDetails();
        try {
            String clientDetails = getB2BClientById(clientId);
            String supplierEmail = jsonObjectProvider.getAttributeValue(clientDetails, "$.contactInfo.contactDetails.email", String.class);
            customClientDetails.setEmailId(supplierEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customClientDetails;
    }


    /**
     * @param supplierId
     * @return
     * @throws OperationException
     */

    @Override
    public String supplierDetails(String supplierId) throws OperationException {
        String jsonObject = null;
        try {
            String URL = getSupplierById;
            if (URL == null) {
                throw new OperationException(com.coxandkings.travel.operations.utils.Constants.ER01);
            }
            Object completeSupplierObject = reconfirmationUtilityService.exchange(URL, null, HttpMethod.GET, Object.class, supplierId);
            jsonObject = reconfirmationUtilityService.prettyJSON(completeSupplierObject);
            return jsonObject;
        } catch (OperationException e) {
            throw new OperationException(com.coxandkings.travel.operations.utils.Constants.ER01);
        } catch (RestClientException e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.toString());
            }
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.toString());
            }
            return jsonObject;
        }
    }

    /**
     * @param clientId
     * @return
     * @throws OperationException
     */

    @Override
    public String getB2BClientById(String clientId) throws OperationException {
        String client = null;
        try {
            String URL = getB2BClientById;
            if (URL == null) {
                throw new OperationException(com.coxandkings.travel.operations.utils.Constants.ER01);
            }
            Object completeClientDetailsObject = this.reconfirmationUtilityService.exchange(URL, null, HttpMethod.GET, Object.class, clientId);
            client = reconfirmationUtilityService.prettyJSON(completeClientDetailsObject);
            return client;
        } catch (OperationException e) {
            throw new OperationException(com.coxandkings.travel.operations.utils.Constants.ER01);
        } catch (RestClientException e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.toString());
            }
            return client;
        } catch (Exception e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.toString());
            }
            return client;
        }
    }


    /**
     * @param clientId
     * @return
     * @throws OperationException
     */
    public String getB2CClientById(String clientId) throws OperationException {
        String client = null;
        try {
            String URL = getB2CClientById;
            if (URL == null) {
                throw new OperationException(com.coxandkings.travel.operations.utils.Constants.ER01);
            }
            Object completeClientDetailsObject = this.reconfirmationUtilityService.exchange(URL, null, HttpMethod.GET, Object.class, clientId);
            client = reconfirmationUtilityService.prettyJSON(completeClientDetailsObject);
            return client;
        } catch (OperationException e) {
            throw new OperationException(com.coxandkings.travel.operations.utils.Constants.ER01);
        } catch (RestClientException e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.toString());
            }
            return client;
        } catch (Exception e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.toString());
            }
            return client;
        }
    }

    /**
     * @param configurationFor
     * @param productCategory
     * @param productCatSubtype
     * @param productId
     * @param productNameSubType
     * @param productFlavor
     * @return
     * @throws OperationException
     */
    @Override
    public String isReconfirmationConfiguredForSupplier(String configurationFor, String productCategory, String productCatSubtype, String productId, String productNameSubType, String productFlavor) throws OperationException {
        String supplierReconfirmation = null;
        String configurationDetails = getConfiguration(configurationFor, productCategory, productCatSubtype, productId, productNameSubType, productFlavor);
        if (configurationDetails != null) {
            supplierReconfirmation = this.jsonObjectProvider.getAttributeValue(configurationDetails, "$.data.[0].supplierReconfirmation.supplierReconfirmation[0].reconfirmationCutOff", String.class);
            return supplierReconfirmation;
        } else {
            return supplierReconfirmation;
        }

    }

    /**
     * @param configurationFor
     * @param productCategory
     * @param productCatSubtype
     * @param productId
     * @param productNameSubType
     * @param productFlavor
     * @return
     * @throws OperationException
     */
    @Override
    public String getConfiguration(String configurationFor, String productCategory, String productCatSubtype, String productId, String productNameSubType, String productFlavor) throws OperationException {

        String data = null;
        try {
            String URL = reconfigurationConfigSearchUrl;
            logger.info(URL);

            String placeHolderKey = this.makeURLForReconfirmationConfig(configurationFor, productCategory, productCatSubtype, productId, productNameSubType, productFlavor, null, null);
            org.apache.commons.codec.net.URLCodec codec = new org.apache.commons.codec.net.URLCodec();
            String uri = URL + codec.encode(placeHolderKey);
            Object response = this.reconfirmationUtilityService.exchange(uri, null, HttpMethod.GET, Object.class);
            data = reconfirmationUtilityService.prettyJSON(response);
            return data;
        } catch (OperationException e) {
            throw new OperationException(com.coxandkings.travel.operations.utils.Constants.ER01);
        } catch (RestClientException e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.toString());
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.toString());
            }
            return data;
        }
    }

    /**
     * @param supplierReconfirmationFilter
     * @return
     * @throws OperationException
     */
    @Override
    public ReconfirmationConfiguration getConfiguration(SupplierReconfirmationFilter supplierReconfirmationFilter) throws OperationException {
        ReconfirmationConfiguration config = new ReconfirmationConfiguration();
        boolean configForClient = false;
        boolean configForSupplier = false;
        String completeConfiguration = null;
        try {
            String URL = reconfigurationConfigSearchUrl;
            logger.info(URL);
            String placeHolderKey = supplierReconfirmationFilter.getUrl(supplierReconfirmationFilter);
            URI url = UriComponentsBuilder.fromUriString(URL + placeHolderKey).build().encode().toUri();
            Object response = this.reconfirmationUtilityService.exchange(url, null, HttpMethod.GET, Object.class);
            completeConfiguration = reconfirmationUtilityService.prettyJSON(response);
            String supplierReconfirmation = this.jsonObjectProvider.getAttributeValue(completeConfiguration, supplierReconfirmationCutOff, String.class);
            if (supplierReconfirmation != null) {
                SupplierConfiguration supplierConfig = getReconfirmationConfigForSupplier(completeConfiguration);
                config.setSupplierConfiguration(supplierConfig);
                config.setConfigFor(ReconfirmationConfigFor.SUPPLIER);
                configForSupplier = true;
            }
            String clientReconfirmationCutOff = jsonObjectProvider.getAttributeValue(completeConfiguration, "$.data.[0].clientOrCustomerConfig.clientOrCustomerReconfirmation[0].reconfirmationCutOff", String.class);
            if (clientReconfirmationCutOff != null) {
                ClientConfiguration clientConfig = getReconfirmationConfigurationForClient(completeConfiguration);
                config.setClientConfiguration(clientConfig);
                config.setConfigFor(ReconfirmationConfigFor.CLIENT);
                configForClient = true;
            }
            if (configForSupplier && configForClient) {
                config.setConfigFor(ReconfirmationConfigFor.CLIENT_AND_SUPPLIER);
            }
            return config;
        } catch (OperationException e) {
            throw new OperationException(com.coxandkings.travel.operations.utils.Constants.ER01);
        } catch (RestClientException e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.toString());
            }
            return config;
        } catch (Exception e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.toString());
            }
            return config;
        }
    }
}
