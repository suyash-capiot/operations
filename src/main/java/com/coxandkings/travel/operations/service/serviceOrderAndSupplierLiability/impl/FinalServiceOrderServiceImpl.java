package com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.impl;

import com.coxandkings.travel.operations.criteria.fileprofitability.FileProfSearchCriteria;
import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.enums.FileProfitability.FileProfTypes;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityType;
import com.coxandkings.travel.operations.enums.supplierBillPassing.StopPaymentStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.generator.IdGenerator;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.FileProfitabilityBooking;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.*;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.FinalServiceOrderRepository;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.PassengerDetailsRepository;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.ProvisionalServiceOrderRepository;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.SupplierPricingRepository;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.SupplierPricingResource;
import com.coxandkings.travel.operations.resource.supplierbillpassing.StopPaymentResource;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierBillPassingResource;
import com.coxandkings.travel.operations.service.FileProfitability.FileProfitabilityService;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.FinalServiceOrderService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.FinalSupplierLiabilityService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.serviceOrderAndSupplierLiability.ServiceOrderVersionComparator;
import com.coxandkings.travel.operations.validations.ServiceOrderAndSupplierLiabilityValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FinalServiceOrderServiceImpl implements FinalServiceOrderService {

    @Autowired
    private FinalServiceOrderRepository finalServiceOrderRepository;

    @Autowired
    private FinalSupplierLiabilityService finalSupplierLiabilityService;

    @Autowired
    private SupplierPricingRepository supplierPricingRepository;

    @Autowired
    private PassengerDetailsRepository passengerDetailsRepository;

    @Autowired
    private ProvisionalServiceOrderRepository provisionalServiceOrderRepository;

    @Autowired
    private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private FileProfitabilityService fileProfitabilityService;

    @Autowired
    private MDMToken mdmToken;

    @Autowired
    private IdGenerator idGenerator;

    @Value(value = "${final_service_order.finance.update_FSO_entries_on_creation}")
    private String updateFSOEntryOnCreation;

    private static Logger logger = LogManager.getLogger(FinalServiceOrderServiceImpl.class);

    @Override
    public FinalServiceOrder generateFSO(ServiceOrderResource resource) throws OperationException, IOException, JSONException {

        if (resource.getType() != null && resource.getType().equals(ServiceOrderAndSupplierLiabilityType.FSO)) {
            logger.info("Entered FinalServiceOrderServiceImpl::generateFSO() method to generate FSO for order " + resource.getOrderId());
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

            criteria.setServiceOrderType(ServiceOrderAndSupplierLiabilityType.PSO);
            criteria.setPageSize(provisionalServiceOrderRepository.getCount().intValue());
            List<ProvisionalServiceOrder> provisionalServiceOrders = (List<ProvisionalServiceOrder>) provisionalServiceOrderRepository.getProvisionalServiceOrders(criteria).get("result");

            /*if(resource.getProductCategorySubTypeId()!=null)
                productSubCategory = mdmRequirements.getProductSubCategoryById(resource.getProductCategorySubTypeId());*/
            //In case of GeneralInvoice i.e FSO getting generated from finance , PSO might not necessarily be created first before FSO.
            if(resource.getGeneralInvoice()==null || resource.getGeneralInvoice()==Boolean.FALSE) {
                if (!OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory().equals(resource.getProductCategorySubTypeId())) {
                    if (provisionalServiceOrders.size() == 0)
                        throw new OperationException("Cannot generate FSO as PSO and PSL does not exist");
                }
            }

            if (OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory().equals(resource.getProductCategorySubTypeId())) {

                if (provisionalServiceOrders.size() == 0) {
                    return generateFinalServiceOrder(resource, provisionalServiceOrders, criteria);
                } else if (provisionalServiceOrders != null && provisionalServiceOrders.size() >= 1 && provisionalServiceOrders.iterator().next().getStatus() != Status.PROVISIONAL_SERVICE_ORDER_CANCELLED) {
                    return generateFinalServiceOrder(resource, provisionalServiceOrders, criteria);
                } else
                    throw new OperationException("Cannot generate FSO as the PSO status for the product is PSO_CANCELLED");
            } else {
                return generateFinalServiceOrder(resource, provisionalServiceOrders, criteria);
            }

        } else
            throw new OperationException("Cannot save FSO record as the service order type is not FSO");

    }

    public FinalServiceOrder generateFinalServiceOrder(ServiceOrderResource resource, List<ProvisionalServiceOrder> provisionalServiceOrders, ServiceOrderSearchCriteria criteria) throws OperationException, IOException {

        criteria.setServiceOrderType(resource.getType());
        criteria.setPageSize(finalServiceOrderRepository.getCount().intValue());
        FinalServiceOrder searchResult = getFSO(criteria);

        BaseServiceOrderDetails fso = new FinalServiceOrder();
        CopyUtils.copy(resource, fso);
        SupplierPricing supplierPricingDetails = new SupplierPricing();
        CopyUtils.copy(resource.getSupplierPricingResource(), supplierPricingDetails);

        for(PassengersDetails passengersDetails : supplierPricingDetails.getPassengerDetails()) {
            passengersDetails.setSupplierPricing(supplierPricingDetails);
        }

        fso.setSupplierPricing(supplierPricingDetails);

        if (resource.getGeneralInvoice() == null || !resource.getGeneralInvoice()) {
            ServiceOrderAndSupplierLiabilityValidator.validateServiceOrderAndSupplierLiability(fso);
            ServiceOrderAndSupplierLiabilityValidator.validateSupplierPricing(fso.getSupplierPricing());
            ServiceOrderAndSupplierLiabilityValidator.validatePassengerDetails((Set<PassengersDetails>) fso.getSupplierPricing().getPassengerDetails());
        }

        if (searchResult != null) {
            fso.setUniqueId(searchResult.getUniqueId());
            fso.setVersionNumber(searchResult.getVersionNumber() + 0.1f);
        } else {
            fso.setUniqueId(idGenerator.generateFSOId(resource.getSupplierId()));
            fso.setVersionNumber(1.1f);
        }

        if (provisionalServiceOrders.size() >= 1) {
            if (provisionalServiceOrders.iterator().next() != null) {
                fso.setProvisionalServiceOrderID(provisionalServiceOrders.iterator().next().getProvisionalServiceOrderID());
                fso.setProvisionalSupplierLiabilityID(provisionalServiceOrders.iterator().next().getProvisionalSupplierLiabilityID());
                fso.setLinkedVersion(provisionalServiceOrders.iterator().next().getVersionNumber());
            }
        }

        fso.setStatus(Status.FINAL_SERVICE_ORDER_GENERATED);

        //Set Company Details(BU, SBU etc..) in the Database
        serviceOrderAndSupplierLiabilityService.setCompanyDetails(fso);
        FinalServiceOrder finalServiceOrder = finalServiceOrderRepository.generateFSO((FinalServiceOrder) fso);

        //alert finance user that FSO is generated
        try {
            InlineMessageResource inlineMessageResource = new InlineMessageResource();
            inlineMessageResource.setAlertName("FSO_GENERATED");
            inlineMessageResource.setNotificationType("System");
            ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
            entity.put("finalServiceOrderID", finalServiceOrder.getUniqueId());
            entity.put("orderID", finalServiceOrder.getOrderId());
            entity.put("bookID", finalServiceOrder.getBookingRefNo());
            inlineMessageResource.setDynamicVariables(entity);
            alertService.sendInlineMessageAlert(inlineMessageResource);
        } catch (Exception e) {
            logger.debug("inline alert notification is not sent");
        }

        resource.setType(ServiceOrderAndSupplierLiabilityType.FSL);
        resource.setFinalServiceOrderID(finalServiceOrder.getUniqueId());
        resource.setProvisionalServiceOrderID(finalServiceOrder.getProvisionalServiceOrderID());
        resource.setProvisionalSupplierLiabilityID(finalServiceOrder.getProvisionalSupplierLiabilityID());
        resource.setLinkedVersion(finalServiceOrder.getVersionNumber());
        FinalSupplierLiability finalSupplierLiability = finalSupplierLiabilityService.generateFSL(resource);
        finalServiceOrder.setFinalServiceOrderID(finalServiceOrder.getUniqueId());
        finalServiceOrder.setFinalSupplierLiabilityID(finalSupplierLiability.getUniqueId());
        finalServiceOrder.setLinkedVersion(finalSupplierLiability.getVersionNumber());
        finalServiceOrder = finalServiceOrderRepository.updateFSO(finalServiceOrder);
        if (provisionalServiceOrders.size() >= 1) {
            if (provisionalServiceOrders.iterator().next() != null) {
                provisionalServiceOrders.iterator().next().setFinalServiceOrderID(finalServiceOrder.getUniqueId());
                provisionalServiceOrders.iterator().next().setFinalSupplierLiabilityID(finalSupplierLiability.getUniqueId());
                provisionalServiceOrderRepository.updatePSO(provisionalServiceOrders.iterator().next());
            }
        }

        if (!StringUtils.isEmpty(resource.getBookingRefNo()) && !StringUtils.isEmpty(resource.getOrderId())) {
            try {
                OpsProduct product = opsBookingService.getProduct(resource.getBookingRefNo(), resource.getOrderId());
                if (product != null) {
                    FileProfSearchCriteria fileProfSearchCriteria = new FileProfSearchCriteria();
                    fileProfSearchCriteria.setBookingRefNumber(resource.getBookingRefNo());
                    fileProfSearchCriteria.setOrderId(resource.getOrderId());
                    List<FileProfitabilityBooking> fileProfitabilityBookings = fileProfitabilityService.getListOfFileProfsWRTCriteria(fileProfSearchCriteria);
                    fileProfitabilityService.calculationMethodForServiceOrder(product, fileProfitabilityBookings.iterator().next(), FileProfTypes.FINAL_PROFITABILITY);
                }
            } catch (Exception e) {
                logger.debug("File profitability is not updated");
            }
        }
        logger.info("FSO is generated successfully for order " + resource.getOrderId());
        updateFSOEntries(finalServiceOrder);
        return finalServiceOrder;
    }


    private void updateFSOEntries(FinalServiceOrder finalServiceOrder) {
        try {
            logger.info("calling finance api to updateFSOEntries ");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", mdmToken.getToken());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("FSOId", finalServiceOrder.getFinalServiceOrderID());
            jsonObject.put("supplierId", finalServiceOrder.getSupplierId());
            jsonObject.put("supplierName", finalServiceOrder.getSupplierName());
            jsonObject.put("amountPaidToSupplier", finalServiceOrder.getSupplierPricing().getAmountPaidToSupplier());
            jsonObject.put("supplierGst", finalServiceOrder.getSupplierPricing().getSupplierGst());
            jsonObject.put("supplierCurrency", finalServiceOrder.getSupplierCurrency());
            jsonObject.put("bookingRefNo", finalServiceOrder.getBookingRefNo());
            if (finalServiceOrder.getCompanyMarketId() != null && finalServiceOrder.getCompanyMarketId().trim().length() > 0) {
                jsonObject.put("companyMarketId", finalServiceOrder.getCompanyMarketId());
            } else {
                jsonObject.put("companyMarketId", "GC22");  // temp fix
            }
            jsonObject.put("netPayableToSupplier", finalServiceOrder.getNetPayableToSupplier());
            jsonObject.put("generalInvoice", finalServiceOrder.getGeneralInvoice());
            jsonObject.put("provisionalServiceOrderNumber", finalServiceOrder.getProvisionalServiceOrderID());
            jsonObject.put("serviceOrderType", finalServiceOrder.getType());
            jsonObject.put("invoiceId", finalServiceOrder.getInvoiceId());
            jsonObject.put("productCategoryId", finalServiceOrder.getProductCategoryId());
            jsonObject.put("productCategorySubTypeId", finalServiceOrder.getProductCategorySubTypeId());
            jsonObject.put("dateOfGeneration", finalServiceOrder.getDateOfGeneration());

            jsonObject.put("supplierCost", finalServiceOrder.getSupplierPricing().getSupplierCost());
            jsonObject.put("hsnSacCode", finalServiceOrder.getSupplierBillPassing().getHsn_SAS_code());

            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            logger.info("url->" + updateFSOEntryOnCreation);
            logger.info("payload->" + jsonObject.toString());
            HttpEntity httpEntity = new HttpEntity(jsonObject.toString(), httpHeaders);
            RestTemplate restTemplate = RestUtils.getTemplate();

            restTemplate.exchange(updateFSOEntryOnCreation, HttpMethod.PUT, httpEntity, String.class);
        } catch (Exception e) {
            logger.error("Error in calling finance api in updateFSOEntries ", e);

        }
    }

    @Override
    public FinalServiceOrder updateFSO(ServiceOrderResource resource) throws OperationException {
        logger.info("Entered FinalServiceOrderServiceImpl::updateFSO() method to update FSO");
        if (resource.getUniqueId() == null)
            throw new OperationException("Provide unique id to update the record");
        if (resource.getVersionNumber() == null)
            throw new OperationException("Provide version number to update the record");

        if (resource.getType() != null && resource.getType().equals(ServiceOrderAndSupplierLiabilityType.FSO)) {
            FinalServiceOrder fso = getFSOById(resource.getUniqueId());

            CopyUtils.copy(resource, fso);
            SupplierPricing supplierPricing = new SupplierPricing();
            CopyUtils.copy(resource.getSupplierPricingResource(), supplierPricing);
            fso.setSupplierPricing(supplierPricing);

            if (resource.getGeneralInvoice() == null || !resource.getGeneralInvoice()) {
                ServiceOrderAndSupplierLiabilityValidator.validateServiceOrderAndSupplierLiability(fso);
                ServiceOrderAndSupplierLiabilityValidator.validateSupplierPricing(fso.getSupplierPricing());
                ServiceOrderAndSupplierLiabilityValidator.validatePassengerDetails((Set<PassengersDetails>) fso.getSupplierPricing().getPassengerDetails());
            }
            logger.info("Final Service Order updated successfully");
            return finalServiceOrderRepository.updateFSO((FinalServiceOrder) fso);
        } else
            throw new OperationException("Cannot update record as the service order type is not FSO");


    }

    @Override
    public FinalServiceOrder getFSOById(String uniqueId) throws OperationException {
        if (!StringUtils.isEmpty(uniqueId)) {
            ServiceOrderSearchCriteria criteria = new ServiceOrderSearchCriteria();
            criteria.setUniqueId(uniqueId);
            List<FinalServiceOrder> finalServiceOrder = (List<FinalServiceOrder>) finalServiceOrderRepository.getFinalServiceOrders(criteria).get("result");
            if (finalServiceOrder.size() == 1)
                return finalServiceOrder.iterator().next();
            else
                throw new OperationException(Constants.ER01);
        } else
            throw new OperationException("Provide FSO unique id to get the details");
    }

    @Override
    public ServiceOrderResource getFSOResourceById(String uniqueId) throws OperationException {
        FinalServiceOrder finalServiceOrder = getFSOById(uniqueId);
        ServiceOrderResource serviceOrderResource = new ServiceOrderResource();
        CopyUtils.copy(finalServiceOrder, serviceOrderResource);
        SupplierPricingResource supplierPricingResource = new SupplierPricingResource();
        CopyUtils.copy(finalServiceOrder.getSupplierPricing(), supplierPricingResource);
        serviceOrderResource.setSupplierPricingResource(supplierPricingResource);
        serviceOrderResource.setNetPayableToSupplier(supplierPricingResource.getNetPayableToSupplier());
        serviceOrderResource.setServiceOrderStatus(finalServiceOrder.getStatus().getValue());
        if(finalServiceOrder.getSupplierBillPassing()!=null) {
            SupplierBillPassingResource supplierBillPassingResource = new SupplierBillPassingResource();
            CopyUtils.copy(finalServiceOrder.getSupplierBillPassing(), supplierBillPassingResource);
            serviceOrderResource.setSupplierBillPassingResource(supplierBillPassingResource);
            serviceOrderResource.setSupplierBillPassingStatus(finalServiceOrder.getSupplierBillPassing().getSupplierBillPassingStatus());
        }
        if (finalServiceOrder.getStopPaymentStatus() != null && finalServiceOrder.getStopPaymentStatus() == StopPaymentStatus.ACTIVE) {
            StopPaymentResource stopPaymentResource = new StopPaymentResource();
            stopPaymentResource.setDate(finalServiceOrder.getStopPaymentTillDate());
            if (finalServiceOrder.getStopPaymentTillDate() != null)
                stopPaymentResource.setStopPaymentTill(finalServiceOrder.getStopPaymentTill().getValue());
            stopPaymentResource.setStopPaymentTill(finalServiceOrder.getStopPaymentStatus().getValue());
            serviceOrderResource.setStopPaymentResource(stopPaymentResource);
        }
        if (finalServiceOrder.getPaymentAdviceSet() != null && finalServiceOrder.getPaymentAdviceSet().size() >= 1) {
            serviceOrderResource.setPaymentAdviceStatus(finalServiceOrder.getPaymentAdviceSet().iterator().next().getPaymentAdviceStatus().getPaymentAdviseStatus());
        }
        return serviceOrderResource;
    }

    @Override
    public FinalServiceOrder getFSOByVersionId(VersionId versionId) throws OperationException {
        if (versionId.getUniqueId() == null)
            throw new OperationException("Provide uniqueId to search Final Service Order");
        if (versionId.getVersionNumber() == null)
            throw new OperationException("Provide version number to search Final Service Order");

        FinalServiceOrder finalServiceOrder = finalServiceOrderRepository.getFSOByVersionId(versionId);
        if (finalServiceOrder != null)
            return finalServiceOrder;
        else
            throw new OperationException(Constants.ER01);
    }

    @Override
    public Map<String, Object> getFinalServiceOrders(ServiceOrderSearchCriteria searchCriteria) throws OperationException, JSONException, IOException {
         return getFinalServiceOrders(searchCriteria,true);
    }

    @Override
    public Map<String, Object> getFinalServiceOrders(ServiceOrderSearchCriteria searchCriteria, Boolean checkForUserCompany) throws OperationException, JSONException, IOException {
        logger.info("Entered FinalServiceOrderServiceImpl::getFinalServiceOrders() method to search FSO details");
        if (searchCriteria.getToGenerationDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
            String value = dateFormat.format(new Date());
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(value, DateTimeFormatter
                    .ofPattern("yyyy-MM-dd'T'HH:mm:ssz").withZone(ZoneId.systemDefault()));
            if (searchCriteria.getToGenerationDate().isAfter(zonedDateTime))
                throw new OperationException(Constants.ER582);
        }

        Map<String, Object> result = finalServiceOrderRepository.getFinalServiceOrders(searchCriteria, checkForUserCompany);
        List<FinalServiceOrder> serviceOrders = (List<FinalServiceOrder>) result.get("result");
        List<ServiceOrderResource> resourceList = new ArrayList<>();
        if (serviceOrders.size() == 0) {
            logger.warn("No records found");
            return result;
        }

        for (FinalServiceOrder serviceOrder : serviceOrders) {
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
                if (serviceOrder.getStopPaymentTillDate() != null)
                    stopPaymentResource.setStopPaymentTill(serviceOrder.getStopPaymentTill().getValue());
                stopPaymentResource.setStopPaymentTill(serviceOrder.getStopPaymentStatus().getValue());
                resource.setStopPaymentResource(stopPaymentResource);
            }
            if (serviceOrder.getPaymentAdviceSet() != null && serviceOrder.getPaymentAdviceSet().size() >= 1) {
                resource.setPaymentAdviceStatus(serviceOrder.getPaymentAdviceSet().iterator().next().getPaymentAdviceStatus().getPaymentAdviseStatus());
            }
            /*if (!StringUtils.isEmpty(resource.getSupplierId()))
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
        logger.info("Retrieved FSO details");
        result.put("result", resourceList);
        return result;

    }

    @Override
    public FinalServiceOrder getFSO(ServiceOrderSearchCriteria criteria) throws OperationException {
        logger.info("Entered FinalServiceOrderServiceImpl::getFSO() method to get FSO details");
        List<FinalServiceOrder> searchResult = (List<FinalServiceOrder>) finalServiceOrderRepository.getFinalServiceOrders(criteria).get("result");
        if (searchResult.size() == 1)
            return searchResult.iterator().next();
        else if (searchResult.size() > 1) {
            Collections.sort(searchResult, new ServiceOrderVersionComparator());
            return searchResult.iterator().next();
        } else
            return null;
    }
}
