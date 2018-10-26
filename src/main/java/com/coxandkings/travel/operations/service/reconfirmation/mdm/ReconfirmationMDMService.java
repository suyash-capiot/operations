package com.coxandkings.travel.operations.service.reconfirmation.mdm;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationFilter;
import com.coxandkings.travel.operations.service.reconfirmation.common.*;

/**
 *
 */
public interface ReconfirmationMDMService {
    /**
     *
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
    SupplierConfiguration getReconfirmationConfigForSupplier( String configurationFor , String productCategory , String productCatSubtype , String productNameSubType , String productFlavor , String orderID , String supplierName , String supplierId ) throws OperationException;

    /**
     *
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
    ClientConfiguration getReconfirmationConfigurationForClient( String configurationFor , String productCategory , String productCatSubtype , String productNameSubType , String productFlavor , String orderID , String clientId , String clientType ) throws OperationException;

    /**
     *
     * @param clientId
     * @return
     * @throws OperationException
     */
    String getB2BClientById( String clientId ) throws OperationException;

    /**
     *
     * @param supplierId
     * @return
     * @throws OperationException
     */
    CustomSupplierDetails getSupplierContactDetails( String supplierId ) throws OperationException;

    /**
     *
     * @param supplierId
     * @return
     * @throws OperationException
     */
    String supplierDetails( String supplierId ) throws OperationException;

    /**
     *
     * @param configurationFor
     * @param productCategory
     * @param productCatSubtype
     * @param productId
     * @param productNameSubType
     * @param productFlavor
     * @return
     * @throws OperationException
     */
    String getConfiguration( String configurationFor , String productCategory , String productCatSubtype , String productId , String productNameSubType , String productFlavor ) throws OperationException;

    /**
     *
     * @param configurationFor
     * @param productCategory
     * @param productCatSubtype
     * @param productId
     * @param productNameSubType
     * @param productFlavor
     * @return
     * @throws OperationException
     */
    String isReconfirmationConfiguredForSupplier( String configurationFor , String productCategory , String productCatSubtype , String productId , String productNameSubType , String productFlavor ) throws OperationException;

    /**
     *
     * @param clientId
     * @param clientType
     * @return
     * @throws OperationException
     */
    CustomClientDetails getB2BClientContactDetails( String clientId , String clientType ) throws OperationException;

    /**
     *
     * @param supplierReconfirmationFilter
     * @return
     * @throws OperationException
     */
    ReconfirmationConfiguration getConfiguration( SupplierReconfirmationFilter supplierReconfirmationFilter ) throws OperationException;
}
