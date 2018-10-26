package com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.impl;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityType;
import com.coxandkings.travel.operations.enums.supplierBillPassing.StopPaymentStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.generator.IdGenerator;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.*;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.PassengerDetailsRepository;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.ProvisionalSupplierLiabilityRepository;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.SupplierPricingRepository;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.SupplierPricingResource;
import com.coxandkings.travel.operations.resource.supplierbillpassing.StopPaymentResource;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierBillPassingResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ProvisionalSupplierLiabilityService;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProvisionalSupplierLiabilityServiceImpl implements ProvisionalSupplierLiabilityService {

    @Autowired
    private ProvisionalSupplierLiabilityRepository provisionalSupplierLiabilityRepository;

    @Autowired
    private SupplierPricingRepository supplierPricingRepository;

    DecimalFormat df = new DecimalFormat(".#");

    @Autowired
    private PassengerDetailsRepository passengerDetailsRepository;

    @Autowired
    AlertService alertService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;

    private static Logger logger = LogManager.getLogger(ProvisionalSupplierLiabilityServiceImpl.class);

    @Override
    public ProvisionalSupplierLiability generatePSL(ServiceOrderResource resource) throws OperationException {

        if (resource.getType() != null && resource.getType().equals(ServiceOrderAndSupplierLiabilityType.PSL)) {
            logger.info("Entered ProvisionalSupplierLiabilityServiceImpl::generatePSL() method to generate PSL for order " + resource.getOrderId());
            if (resource.getProvisionalServiceOrderID() != null) {

                ServiceOrderSearchCriteria criteria = new ServiceOrderSearchCriteria();
//                criteria.setUniqueId(resource.getUniqueId());
//                criteria.setVersionNumber(resource.getVersionNumber());
                criteria.setProvisionalServiceOrderID(resource.getProvisionalServiceOrderID());
                criteria.setBookingRefNo(resource.getBookingRefNo());
                criteria.setOrderId(resource.getOrderId());
                criteria.setCompanyMarketId(resource.getCompanyMarketId());
                criteria.setProductCategoryId(resource.getProductCategoryId());
                criteria.setProductCategorySubTypeId(resource.getProductCategorySubTypeId());
                //criteria.setProductNameOrFlavourNameId(resource.getProductNameOrFlavourNameId());
                criteria.setServiceOrderType(resource.getType());
                criteria.setInvoiceId(resource.getInvoiceId());
                criteria.setPageSize(provisionalSupplierLiabilityRepository.getCount().intValue());

                ProvisionalSupplierLiability searchResult = getPSL(criteria);
                SupplierPricing supplierPricingDetails;
                BaseServiceOrderDetails psl;
                String uniqueId=null; Float versionNo=null;
                String supplierPricingId = "";
                List<String> paxIds =  new ArrayList<>();
                Set<PassengersDetails> existingPaxSet = new HashSet<>();
                if(searchResult==null) {
                    psl = new ProvisionalSupplierLiability();
                    supplierPricingDetails = new SupplierPricing();

                }else{
                    psl = searchResult;
                    uniqueId = psl.getUniqueId();
                    versionNo = psl.getVersionNumber();
                    supplierPricingDetails = psl.getSupplierPricing();
                    supplierPricingId = supplierPricingDetails.getId();
                    existingPaxSet = supplierPricingDetails.getPassengerDetails();
                }

                CopyUtils.copy(resource, psl);
                CopyUtils.copy(resource.getSupplierPricingResource(), supplierPricingDetails);
                supplierPricingDetails.setId(supplierPricingId);

                if(searchResult!=null){
                    Iterator iterator = resource.getSupplierPricingResource().getPassengerDetails().iterator();
                    for (PassengersDetails existingPassenger : existingPaxSet) {
                        PassengersDetails passengerResource = (PassengersDetails) iterator.next();
                        existingPassenger.setNoOfPassengers(passengerResource.getNoOfPassengers());
                        existingPassenger.setRatePerPassenger(passengerResource.getRatePerPassenger());
                        existingPassenger.setPassengerType(passengerResource.getPassengerType());
                        existingPassenger.setSupplierCostPrice(passengerResource.getSupplierCostPrice());
                        existingPassenger.setRoomCategoryOrCabinCategory(passengerResource.getRoomCategoryOrCabinCategory());
                        existingPassenger.setRoomTypeOrCabinType(passengerResource.getRoomTypeOrCabinType());
                    }
                    supplierPricingDetails.setPassengerDetails(existingPaxSet);
                }else {
                    for (PassengersDetails passengersDetails : supplierPricingDetails.getPassengerDetails()) {
                        passengersDetails.setSupplierPricing(supplierPricingDetails);
                    }
                }
                psl.setSupplierPricing(supplierPricingDetails);

                if (resource.getGeneralInvoice() == null || !resource.getGeneralInvoice()) {
                    ServiceOrderAndSupplierLiabilityValidator.validateServiceOrderAndSupplierLiability(psl);
                    ServiceOrderAndSupplierLiabilityValidator.validateSupplierPricing(psl.getSupplierPricing());
                    ServiceOrderAndSupplierLiabilityValidator.validatePassengerDetails((Set<PassengersDetails>) psl.getSupplierPricing().getPassengerDetails());
                }

                if (searchResult != null) {
                    psl.setUniqueId(uniqueId);
                    psl.setVersionNumber(Float.valueOf(df.format(versionNo + 0.1f)));
                } else {
                    psl.setUniqueId(idGenerator.generatePSLId(resource.getSupplierId()));
                    psl.setVersionNumber(1.1f);
                }

                psl.setStatus(Status.PROVISIONAL_SUPPLIER_LIABILITY_GENERATED);
                //Set Company Details(BU, SBU etc..) in the Database
                serviceOrderAndSupplierLiabilityService.setCompanyDetails(psl);
                ProvisionalSupplierLiability provisionalSupplierLiability = provisionalSupplierLiabilityRepository.generatePSL((ProvisionalSupplierLiability) psl);

                //alert finance user that PSL is generated
                try {
                    InlineMessageResource inlineMessageResource = new InlineMessageResource();
                    inlineMessageResource.setAlertName("PSL_GENERATED");
                    inlineMessageResource.setNotificationType("System");
                    ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
                    entity.put("provisionalSupplierLiabilityID", provisionalSupplierLiability.getUniqueId());
                    entity.put("orderID", provisionalSupplierLiability.getOrderId());
                    entity.put("bookID", provisionalSupplierLiability.getBookingRefNo());
                    inlineMessageResource.setDynamicVariables(entity);
                    alertService.sendInlineMessageAlert(inlineMessageResource);
                } catch (Exception e) {
                    logger.debug("inline alert notification is not sent");
                }

                provisionalSupplierLiability.setProvisionalSupplierLiabilityID(provisionalSupplierLiability.getUniqueId());
                provisionalSupplierLiability = provisionalSupplierLiabilityRepository.updatePSL(provisionalSupplierLiability);
                logger.info("PSL is successfully generated for order " + resource.getOrderId());
                return provisionalSupplierLiability;
            } else {
                throw new OperationException("Cannot generate PSL as there is no PSO generated for this product");
            }

        } else
            throw new OperationException("Cannot save PSL record as the service order type is not PSL");

    }

    @Override
    public ProvisionalSupplierLiability updatePSL(ServiceOrderResource resource) throws OperationException {
        logger.info("Entered ProvisionalSupplierLiabilityServiceImpl::updatePSL() method to update PSL");
        if (resource.getUniqueId() == null)
            throw new OperationException("Provide unique id to update the record");
        if (resource.getVersionNumber() == null)
            throw new OperationException("Provide version number to update the record");

        if (resource.getType() != null && resource.getType().equals(ServiceOrderAndSupplierLiabilityType.PSL)) {
            ProvisionalSupplierLiability psl = getPSLById(resource.getUniqueId());

            CopyUtils.copy(resource, psl);
            SupplierPricing supplierPricing=new SupplierPricing();
            CopyUtils.copy(resource.getSupplierPricingResource(),supplierPricing);
            psl.setSupplierPricing(supplierPricing);

            if (resource.getGeneralInvoice() == null || !resource.getGeneralInvoice()) {
                ServiceOrderAndSupplierLiabilityValidator.validateServiceOrderAndSupplierLiability(psl);
                ServiceOrderAndSupplierLiabilityValidator.validateSupplierPricing(psl.getSupplierPricing());
                ServiceOrderAndSupplierLiabilityValidator.validatePassengerDetails((Set<PassengersDetails>) psl.getSupplierPricing().getPassengerDetails());
            }
            logger.info("PSL details are successfully updated");
            return provisionalSupplierLiabilityRepository.updatePSL((ProvisionalSupplierLiability) psl);
        } else
            throw new OperationException("Cannot update PSL record as the service order type is not PSL");

    }

    @Override
    public ProvisionalSupplierLiability getPSLById(String uniqueId) throws OperationException {
        if (!StringUtils.isEmpty(uniqueId)) {
            ServiceOrderSearchCriteria criteria = new ServiceOrderSearchCriteria();
            criteria.setUniqueId(uniqueId);
            List<ProvisionalSupplierLiability> provisionalSupplierLiabilities = (List<ProvisionalSupplierLiability>) provisionalSupplierLiabilityRepository.getProvisionalSupplierLiabilities(criteria).get("result");
            if (provisionalSupplierLiabilities.size() == 1)
                return provisionalSupplierLiabilities.iterator().next();
            else
                throw new OperationException(Constants.ER01);
        } else
            throw new OperationException("Provide PSL unique id to get the details");
    }

    @Override
    public ServiceOrderResource getPSLResourceById(String uniqueId) throws OperationException {
        ProvisionalSupplierLiability provisionalSupplierLiability = getPSLById(uniqueId);
        ServiceOrderResource serviceOrderResource = new ServiceOrderResource();
        CopyUtils.copy(provisionalSupplierLiability,serviceOrderResource);
        SupplierPricingResource supplierPricingResource = new SupplierPricingResource();
        CopyUtils.copy(provisionalSupplierLiability.getSupplierPricing(), supplierPricingResource);
        serviceOrderResource.setSupplierPricingResource(supplierPricingResource);
        serviceOrderResource.setNetPayableToSupplier(supplierPricingResource.getNetPayableToSupplier());
        serviceOrderResource.setServiceOrderStatus(provisionalSupplierLiability.getStatus().getValue());
        if(provisionalSupplierLiability.getSupplierBillPassing()!=null) {
            SupplierBillPassingResource supplierBillPassingResource = new SupplierBillPassingResource();
            CopyUtils.copy(provisionalSupplierLiability.getSupplierBillPassing(), supplierBillPassingResource);
            serviceOrderResource.setSupplierBillPassingResource(supplierBillPassingResource);
            serviceOrderResource.setSupplierBillPassingStatus(provisionalSupplierLiability.getSupplierBillPassing().getSupplierBillPassingStatus());
        }
        if (provisionalSupplierLiability.getStopPaymentStatus() != null && provisionalSupplierLiability.getStopPaymentStatus() == StopPaymentStatus.ACTIVE) {
            StopPaymentResource stopPaymentResource = new StopPaymentResource();
            stopPaymentResource.setDate(provisionalSupplierLiability.getStopPaymentTillDate());
            if (provisionalSupplierLiability.getStopPaymentTillDate() != null)
                stopPaymentResource.setStopPaymentTill(provisionalSupplierLiability.getStopPaymentTill().getValue());
            stopPaymentResource.setStopPaymentTill(provisionalSupplierLiability.getStopPaymentStatus().getValue());
            serviceOrderResource.setStopPaymentResource(stopPaymentResource);
        }
        if (provisionalSupplierLiability.getPaymentAdviceSet() != null && provisionalSupplierLiability.getPaymentAdviceSet().size() >= 1) {
            serviceOrderResource.setPaymentAdviceStatus(provisionalSupplierLiability.getPaymentAdviceSet().iterator().next().getPaymentAdviceStatus().getPaymentAdviseStatus());
        }
        return serviceOrderResource;
    }

    @Override
    public ProvisionalSupplierLiability getPSLByVersionId(VersionId versionId) throws OperationException {
        if (versionId.getUniqueId() == null)
            throw new OperationException("Provide uniqueId to search Provisional Supplier Liability");
        if (versionId.getVersionNumber() == null)
            throw new OperationException("Provide version number to search Provisional Supplier Liability");

        ProvisionalSupplierLiability provisionalSupplierLiability = provisionalSupplierLiabilityRepository.getPSLByVersionId(versionId);
        if (provisionalSupplierLiability != null)
            return provisionalSupplierLiability;
        else
            throw new OperationException(Constants.ER01);
    }

    @Override
    public Map<String, Object> getProvisionalSupplierLiabilities(ServiceOrderSearchCriteria searchCriteria) throws OperationException, IOException, JSONException {
        logger.info("Entered ProvisionalSupplierLiabilityServiceImpl::getProvisionalSupplierLiabilities() method to search PSL");
        if (searchCriteria.getToGenerationDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
            String value = dateFormat.format(new Date());
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(value, DateTimeFormatter
                    .ofPattern("yyyy-MM-dd'T'HH:mm:ssz").withZone(ZoneId.systemDefault()));
            if (searchCriteria.getToGenerationDate().isAfter(zonedDateTime))
                throw new OperationException(Constants.ER582);
        }

        Map<String, Object> result = provisionalSupplierLiabilityRepository.getProvisionalSupplierLiabilities(searchCriteria);
        List<ProvisionalSupplierLiability> supplierLiabilities = (List<ProvisionalSupplierLiability>) result.get("result");
        if (supplierLiabilities.size() == 0) {
            logger.warn("No records found");
            return result;
        }

        List<ServiceOrderResource> resourceList = new ArrayList<>();
        for (ProvisionalSupplierLiability serviceOrder : supplierLiabilities) {
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
        logger.info("Retrieved PSL details");
        result.put("result", resourceList);
        return result;
    }

    public ProvisionalSupplierLiability getPSL(ServiceOrderSearchCriteria criteria) throws OperationException {
        logger.info("Entered ProvisionalSupplierLiabilityServiceImpl::getPSL() method to get PSL");
        List<ProvisionalSupplierLiability> searchResult = (List<ProvisionalSupplierLiability>) provisionalSupplierLiabilityRepository.getProvisionalSupplierLiabilities(criteria).get("result");
        if (searchResult.size() == 1)
            return searchResult.iterator().next();
        else if (searchResult.size() > 1) {
            Collections.sort(searchResult, new ServiceOrderVersionComparator());
            return searchResult.iterator().next();
        } else
            return null;
    }
}
