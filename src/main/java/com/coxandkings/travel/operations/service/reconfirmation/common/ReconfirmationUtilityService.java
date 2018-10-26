package com.coxandkings.travel.operations.service.reconfirmation.common;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;
import com.coxandkings.travel.operations.model.template.request.TemplateInfo;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.util.Locale;

/**
 *
 */
public interface ReconfirmationUtilityService {
    /**
     * @param object
     * @return
     */
    String prettyJSON(Object object);

    /**
     * @param URL
     * @param token
     * @param httpMethod
     * @param responseType
     * @param args
     * @param <T>
     * @return
     * @throws OperationException
     */
    <T> T exchange(String URL, String token, HttpMethod httpMethod, Class<T> responseType, Object... args) throws OperationException;

    /**
     * @param URL
     * @param token
     * @param httpMethod
     * @param responseType
     * @param args
     * @param <T>
     * @return
     * @throws OperationException
     */
    <T> T exchange(URI URL, String token, HttpMethod httpMethod, Class<T> responseType, Object... args) throws OperationException;

    /**
     * @param duration
     * @param durationType
     * @return
     */
    long convertToHours(Integer duration, String durationType);

    /**
     * @param msgCode
     * @param locale
     * @param args
     * @return
     */
    String getMessage(String msgCode, Locale locale, String... args);

    /**
     * @param message
     * @param color
     * @return
     */
    String convertToHtml(String message, String color);

    /**
     * @param opsBooking
     * @param opsProduct
     * @return
     */
    CommunicationTagResource getCommunicationTagResource(OpsBooking opsBooking, OpsProduct opsProduct);

    /**
     * @param businessProcess
     * @param scenario
     * @param function
     * @return
     */
    TemplateInfo getTemplateInfo(String businessProcess, String scenario, String function);

    /**
     * @param details
     * @param opsBooking
     * @param opsProduct
     * @param customSupplierDetails
     * @param hash
     * @param isClient
     * @param isSupplier
     * @return
     */
    boolean composeEmailForSupplier(SupplierReconfirmationDetails details, OpsBooking opsBooking, OpsProduct opsProduct, CustomSupplierDetails customSupplierDetails, String hash, boolean isClient, boolean isSupplier);

    /**
     * @param details
     * @param opsBooking
     * @param opsProduct
     * @param customClientDetails
     * @param hash
     * @param isClient
     * @param isSupplier
     * @return
     */
    boolean composeEmailForClient(ClientReconfirmationDetails details, OpsBooking opsBooking, OpsProduct opsProduct, CustomClientDetails customClientDetails, String hash, boolean isClient, boolean isSupplier);

    /**
     * @param details
     * @param opsBooking
     * @param opsProduct
     * @param customSupplierDetails
     * @param hash
     * @param isClient
     * @param isSupplier
     * @return
     */
    boolean composeEmailForServiceProvider(SupplierReconfirmationDetails details, OpsBooking opsBooking, OpsProduct opsProduct, CustomSupplierDetails customSupplierDetails, String hash, boolean isClient, boolean isSupplier);

}
