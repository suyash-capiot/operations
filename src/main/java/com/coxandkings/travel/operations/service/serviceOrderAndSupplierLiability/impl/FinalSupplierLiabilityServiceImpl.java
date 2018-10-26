package com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.impl;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityType;
import com.coxandkings.travel.operations.enums.supplierBillPassing.StopPaymentStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.generator.IdGenerator;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.*;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.FinalSupplierLiabilityRepository;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.PassengerDetailsRepository;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.SupplierPricingRepository;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.SupplierPricingResource;
import com.coxandkings.travel.operations.resource.supplierbillpassing.StopPaymentResource;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierBillPassingResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.FinalServiceOrderService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.FinalSupplierLiabilityService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.serviceOrderAndSupplierLiability.ServiceOrderVersionComparator;
import com.coxandkings.travel.operations.validations.ServiceOrderAndSupplierLiabilityValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FinalSupplierLiabilityServiceImpl implements FinalSupplierLiabilityService {

    @Autowired
    private FinalSupplierLiabilityRepository finalSupplierLiabilityRepository;

    @Autowired
    private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;

    @Autowired
    private FinalServiceOrderService finalServiceOrderService;

    @Autowired
    private SupplierPricingRepository supplierPricingRepository;

    @Autowired
    private PassengerDetailsRepository passengerDetailsRepository;

    @Autowired
    private AlertService alertService;

    @Autowired
    private IdGenerator idGenerator;

    private static Logger logger = LogManager.getLogger(FinalSupplierLiabilityServiceImpl.class);

    @Override
    public FinalSupplierLiability generateFSL(ServiceOrderResource resource) throws OperationException, IOException, JSONException {

        if (resource.getType() != null && resource.getType().equals(ServiceOrderAndSupplierLiabilityType.FSL)) {
            logger.info("Entered FinalSupplierLiabilityServiceImpl::generateFSL() method to generate FSL for order " + resource.getOrderId());
            ServiceOrderSearchCriteria criteria = new ServiceOrderSearchCriteria();
            criteria.setBookingRefNo(resource.getBookingRefNo());
            criteria.setOrderId(resource.getOrderId());
            criteria.setCompanyMarketId(resource.getCompanyMarketId());
            criteria.setProductCategoryId(resource.getProductCategoryId());
            criteria.setProductCategorySubTypeId(resource.getProductCategorySubTypeId());
            /*criteria.setProductNameOrFlavourNameId(resource.getProductNameOrFlavourNameId());
            criteria.setSupplierId(resource.getSupplierId());
            criteria.setSupplierCurrency(resource.getSupplierCurrency());*/
            criteria.setInvoiceId(resource.getInvoiceId());
            criteria.setServiceOrderType(ServiceOrderAndSupplierLiabilityType.FSO);

            Map<String, Object> result = finalServiceOrderService.getFinalServiceOrders(criteria);
            List<ServiceOrderResource> fso = (List<ServiceOrderResource>) result.get("result");
            if (fso.size() == 0)
                throw new OperationException("Cannot generate FSL as there is no FSO generated for this product");
            else {
                if (StringUtils.isEmpty(resource.getProvisionalServiceOrderID()))
                    resource.setProvisionalServiceOrderID(fso.iterator().next().getProvisionalServiceOrderID());
                if (StringUtils.isEmpty(resource.getProvisionalSupplierLiabilityID()))
                    resource.setProvisionalSupplierLiabilityID(fso.iterator().next().getProvisionalSupplierLiabilityID());
                if (StringUtils.isEmpty(resource.getFinalServiceOrderID()))
                    resource.setFinalServiceOrderID(fso.iterator().next().getFinalServiceOrderID());
            }

            criteria.setServiceOrderType(resource.getType());
            criteria.setPageSize(finalSupplierLiabilityRepository.getCount().intValue());

            FinalSupplierLiability searchResult = getFSL(criteria);

            BaseServiceOrderDetails fsl = new FinalSupplierLiability();
            CopyUtils.copy(resource, fsl);
            SupplierPricing supplierPricingDetails = new SupplierPricing();
            CopyUtils.copy(resource.getSupplierPricingResource(), supplierPricingDetails);

            for(PassengersDetails passengersDetails : supplierPricingDetails.getPassengerDetails()) {
                passengersDetails.setSupplierPricing(supplierPricingDetails);
            }

            fsl.setSupplierPricing(supplierPricingDetails);

            if(resource.getGeneralInvoice() == null || !resource.getGeneralInvoice()) {
                ServiceOrderAndSupplierLiabilityValidator.validateServiceOrderAndSupplierLiability(fsl);
                ServiceOrderAndSupplierLiabilityValidator.validateSupplierPricing(fsl.getSupplierPricing());
                ServiceOrderAndSupplierLiabilityValidator.validatePassengerDetails((Set<PassengersDetails>) fsl.getSupplierPricing().getPassengerDetails());
            }

            /*criteria.setUniqueId(resource.getUniqueId());
            criteria.setVersionNumber(resource.getVersionNumber());*/

            if (searchResult != null) {
                fsl.setUniqueId(searchResult.getUniqueId());
                fsl.setVersionNumber(searchResult.getVersionNumber() + 0.1f);
            } else {
                fsl.setUniqueId(idGenerator.generateFSLId(resource.getSupplierId()));
                fsl.setVersionNumber(1.1f);
            }

            fsl.setStatus(Status.FINAL_SUPPLIER_LIABILITY_GENERATED);
            //Set Company Details(BU, SBU etc..) in the Database
            serviceOrderAndSupplierLiabilityService.setCompanyDetails(fsl);
            FinalSupplierLiability finalSupplierLiability = finalSupplierLiabilityRepository.generateFSL((FinalSupplierLiability) fsl);

            //alert finance user that FSL is generated
            try {
                InlineMessageResource inlineMessageResource = new InlineMessageResource();
                inlineMessageResource.setAlertName("FSL_GENERATED");
                inlineMessageResource.setNotificationType("System");
                ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
                entity.put("finalSupplierLiabilityID", finalSupplierLiability.getUniqueId());
                entity.put("orderID", finalSupplierLiability.getOrderId());
                entity.put("bookID", finalSupplierLiability.getBookingRefNo());
                inlineMessageResource.setDynamicVariables(entity);
                alertService.sendInlineMessageAlert(inlineMessageResource);
            } catch (Exception e) {
                logger.debug("inline alert notification is not sent");
            }

            finalSupplierLiability.setFinalSupplierLiabilityID(finalSupplierLiability.getUniqueId());
            finalSupplierLiability = finalSupplierLiabilityRepository.updateFSL(finalSupplierLiability);
            logger.info("FSL is successfully generated for order " + resource.getOrderId());
            return finalSupplierLiability;
        } else
            throw new OperationException("Cannot save FSL record as the service order type is not FSL");
    }

    @Override
    public FinalSupplierLiability updateFSL(ServiceOrderResource resource) throws OperationException {
        logger.info("Entered FinalSupplierLiabilityServiceImpl::updateFSL() method to update FSL");
        if (resource.getUniqueId() == null)
            throw new OperationException("Provide unique id to update the record");
        if (resource.getVersionNumber() == null)
            throw new OperationException("Provide version number to update the record");

        if (resource.getType() != null && resource.getType().equals(ServiceOrderAndSupplierLiabilityType.FSL)) {
            FinalSupplierLiability fsl = getFSLById(resource.getUniqueId());

            CopyUtils.copy(resource, fsl);
            SupplierPricing supplierPricing=new SupplierPricing();
            CopyUtils.copy(resource.getSupplierPricingResource(),supplierPricing);
            fsl.setSupplierPricing(supplierPricing);

            if(resource.getGeneralInvoice() == null || !resource.getGeneralInvoice()) {
                ServiceOrderAndSupplierLiabilityValidator.validateServiceOrderAndSupplierLiability(fsl);
                ServiceOrderAndSupplierLiabilityValidator.validateSupplierPricing(fsl.getSupplierPricing());
                ServiceOrderAndSupplierLiabilityValidator.validatePassengerDetails((Set<PassengersDetails>) fsl.getSupplierPricing().getPassengerDetails());
            }
            logger.info("FSL details are updated successfully");
            return finalSupplierLiabilityRepository.updateFSL((FinalSupplierLiability) fsl);
        } else
            throw new OperationException("Cannot update FSL record as the service order type is not FSL");

    }

    @Override
    public FinalSupplierLiability getFSLById(String uniqueId) throws OperationException {
        if (!StringUtils.isEmpty(uniqueId)) {
            ServiceOrderSearchCriteria criteria = new ServiceOrderSearchCriteria();
            criteria.setUniqueId(uniqueId);
            List<FinalSupplierLiability> supplierLiabilities = (List<FinalSupplierLiability>) finalSupplierLiabilityRepository.getFinalSupplierLiabilities(criteria).get("result");
            if (supplierLiabilities.size() == 1)
                return supplierLiabilities.iterator().next();
            else
                throw new OperationException(Constants.ER01);
        } else
            throw new OperationException("Provide FSL unique id to get the details");
    }

    @Override
    public ServiceOrderResource getFSLResourceById(String uniqueId) throws OperationException {
        FinalSupplierLiability finalSupplierLiability = getFSLById(uniqueId);
        ServiceOrderResource serviceOrderResource = new ServiceOrderResource();
        CopyUtils.copy(finalSupplierLiability,serviceOrderResource);
        SupplierPricingResource supplierPricingResource = new SupplierPricingResource();
        CopyUtils.copy(finalSupplierLiability.getSupplierPricing(), supplierPricingResource);
        serviceOrderResource.setSupplierPricingResource(supplierPricingResource);
        serviceOrderResource.setNetPayableToSupplier(supplierPricingResource.getNetPayableToSupplier());
        serviceOrderResource.setServiceOrderStatus(finalSupplierLiability.getStatus().getValue());
        if(finalSupplierLiability.getSupplierBillPassing()!=null) {
            SupplierBillPassingResource supplierBillPassingResource = new SupplierBillPassingResource();
            CopyUtils.copy(finalSupplierLiability.getSupplierBillPassing(), supplierBillPassingResource);
            serviceOrderResource.setSupplierBillPassingResource(supplierBillPassingResource);
            serviceOrderResource.setSupplierBillPassingStatus(finalSupplierLiability.getSupplierBillPassing().getSupplierBillPassingStatus());
        }
        if (finalSupplierLiability.getStopPaymentStatus() != null && finalSupplierLiability.getStopPaymentStatus() == StopPaymentStatus.ACTIVE) {
            StopPaymentResource stopPaymentResource = new StopPaymentResource();
            stopPaymentResource.setDate(finalSupplierLiability.getStopPaymentTillDate());
            if (finalSupplierLiability.getStopPaymentTillDate() != null)
                stopPaymentResource.setStopPaymentTill(finalSupplierLiability.getStopPaymentTill().getValue());
            stopPaymentResource.setStopPaymentTill(finalSupplierLiability.getStopPaymentStatus().getValue());
            serviceOrderResource.setStopPaymentResource(stopPaymentResource);
        }
        if (finalSupplierLiability.getPaymentAdviceSet() != null && finalSupplierLiability.getPaymentAdviceSet().size() >= 1) {
            serviceOrderResource.setPaymentAdviceStatus(finalSupplierLiability.getPaymentAdviceSet().iterator().next().getPaymentAdviceStatus().getPaymentAdviseStatus());
        }
        return serviceOrderResource;
    }

    @Override
    public FinalSupplierLiability getFSLByVersionId(VersionId versionId) throws OperationException {
        if (versionId.getUniqueId() == null)
            throw new OperationException("Provide uniqueId to search Final Supplier Liability");
        if (versionId.getVersionNumber() == null)
            throw new OperationException("Provide version number to search Final Supplier Liability");

        FinalSupplierLiability finalSupplierLiability = finalSupplierLiabilityRepository.getFSLByVersionId(versionId);
        if (finalSupplierLiability != null)
            return finalSupplierLiability;
        else
            throw new OperationException(Constants.ER01);

    }

    @Override
    public Map<String, Object> getFinalSupplierLiabilities(ServiceOrderSearchCriteria searchCriteria) throws OperationException, IOException, JSONException {
        logger.info("Entered FinalSupplierLiabilityServiceImpl::getFinalSupplierLiabilities() method to search FSL");
        if (searchCriteria.getToGenerationDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
            String value = dateFormat.format(new Date());
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(value, DateTimeFormatter
                    .ofPattern("yyyy-MM-dd'T'HH:mm:ssz").withZone(ZoneId.systemDefault()));
            if (searchCriteria.getToGenerationDate().isAfter(zonedDateTime))
                throw new OperationException(Constants.ER582);
        }

        Map<String, Object> result = finalSupplierLiabilityRepository.getFinalSupplierLiabilities(searchCriteria);
        List<FinalSupplierLiability> supplierLiabilities = (List<FinalSupplierLiability>) result.get("result");
        if (supplierLiabilities.size() == 0) {
            logger.warn("No records found");
            return result;
        }
        List<ServiceOrderResource> resourceList = new ArrayList<>();
        for (FinalSupplierLiability serviceOrder : supplierLiabilities) {
            ServiceOrderResource resource = new ServiceOrderResource();
            CopyUtils.copy(serviceOrder, resource);

            if (serviceOrder.getSupplierBillPassing() != null) {
                SupplierBillPassingResource supplierBillPassingResource = new SupplierBillPassingResource();
                CopyUtils.copy(serviceOrder.getSupplierBillPassing(), supplierBillPassingResource);
                resource.setSupplierBillPassingResource(supplierBillPassingResource);
                resource.setSupplierBillPassingStatus(serviceOrder.getSupplierBillPassing().getSupplierBillPassingStatus());
            }

            if (serviceOrder.getStopPaymentStatus() != null && serviceOrder.getStopPaymentStatus() == StopPaymentStatus.ACTIVE) {
                StopPaymentResource stopPaymentResource = new StopPaymentResource();
                stopPaymentResource.setDate(serviceOrder.getStopPaymentTillDate());
                if (serviceOrder.getStopPaymentTillDate()!=null)
                    stopPaymentResource.setStopPaymentTill(serviceOrder.getStopPaymentTill().getValue());
                stopPaymentResource.setStopPaymentTill(serviceOrder.getStopPaymentStatus().getValue());
                resource.setStopPaymentResource(stopPaymentResource);
            }
            if (serviceOrder.getPaymentAdviceSet() != null && serviceOrder.getPaymentAdviceSet().size() >= 1) {
                resource.setPaymentAdviceStatus(serviceOrder.getPaymentAdviceSet().iterator().next().getPaymentAdviceStatus().getPaymentAdviseStatus());
            }
           /* if (!StringUtils.isEmpty(resource.getSupplierId()))
                resource.setSupplierName(mdmRequirements.getSupplierNameById(resource.getSupplierId()));*/
            /*if (!StringUtils.isEmpty(resource.getProductCategoryId()))
                resource.setProductCategory(mdmRequirements.getProductCategoryById(resource.getProductCategoryId()));
            if (!StringUtils.isEmpty(resource.getProductCategorySubTypeId()))
                resource.setProductCategorySubType(mdmRequirements.getProductSubCategoryById(resource.getProductCategorySubTypeId()));*/
            SupplierPricingResource supplierPricingResource = new SupplierPricingResource();
            CopyUtils.copy(serviceOrder.getSupplierPricing(), supplierPricingResource);
            resource.setSupplierPricingResource(supplierPricingResource);
            resource.setNetPayableToSupplier(supplierPricingResource.getNetPayableToSupplier());
            resourceList.add(resource);
        }
        logger.info("Retrieved FSL details");
        result.put("result", resourceList);
        return result;

    }

    public FinalSupplierLiability getFSL(ServiceOrderSearchCriteria criteria) throws OperationException {
        logger.info("Entered FinalSupplierLiabilityServiceImpl::getFSL() method to get FSL");
        List<FinalSupplierLiability> searchResult = (List<FinalSupplierLiability>) finalSupplierLiabilityRepository.getFinalSupplierLiabilities(criteria).get("result");
        if (searchResult.size() == 1)
            return searchResult.iterator().next();
        else if (searchResult.size() > 1) {
            Collections.sort(searchResult, new ServiceOrderVersionComparator());
            return searchResult.iterator().next();
        } else
            return null;
    }
}
