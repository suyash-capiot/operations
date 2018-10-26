package com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.impl;

import com.coxandkings.travel.operations.criteria.fileprofitability.FileProfSearchCriteria;
import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.enums.FileProfitability.FileProfTypes;
import com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityType;
import com.coxandkings.travel.operations.enums.supplierBillPassing.StopPaymentStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.generator.IdGenerator;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.FileProfitabilityBooking;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.*;
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
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ProvisionalServiceOrderService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ProvisionalSupplierLiabilityService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class ProvisionalServiceOrderServiceImpl implements ProvisionalServiceOrderService {

    @Autowired
    private ProvisionalServiceOrderRepository provisionalServiceOrderRepository;

    @Autowired
    private ProvisionalSupplierLiabilityService provisionalSupplierLiabilityService;

    @Autowired
    private FinalServiceOrderService finalServiceOrderService;

    @Autowired
    private SupplierPricingRepository supplierPricingRepository;

    @Autowired
    private PassengerDetailsRepository passengerDetailsRepository;

    @Autowired
    private AlertService alertService;

    DecimalFormat df = new DecimalFormat(".#");

    @Autowired
    OpsBookingService opsBookingService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private FileProfitabilityService fileProfitabilityService;

    @Autowired
    private MDMToken mdmToken;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;

    @Value(value = "${final_service_order.finance.update_FSO_entries_on_creation}")
    private String updateFSOEntryOnCreation;

    @Value(value = "${final_service_order.finance.update_FSO_entries_on_cancellation}")
    private String update_FSO_entries_on_cancellation;

    private static Logger logger = LogManager.getLogger(ProvisionalServiceOrderServiceImpl.class);

    @Override
    public ProvisionalServiceOrder generatePSO(ServiceOrderResource resource) throws OperationException, IOException {

        if (resource.getType() != null && resource.getType().equals(ServiceOrderAndSupplierLiabilityType.PSO)) {
            logger.info("Entered ProvisionalServiceOrderServiceImpl::generatePSO() method to generate PSO for order " + resource.getOrderId());
            ServiceOrderSearchCriteria criteria = new ServiceOrderSearchCriteria();
            criteria.setBookingRefNo(resource.getBookingRefNo());
            criteria.setOrderId(resource.getOrderId());
            criteria.setCompanyMarketId(resource.getCompanyMarketId());
            criteria.setProductCategoryId(resource.getProductCategoryId());
            criteria.setProductCategorySubTypeId(resource.getProductCategorySubTypeId());
            criteria.setServiceOrderType(ServiceOrderAndSupplierLiabilityType.FSO);
            /*criteria.setProductNameOrFlavourNameId(resource.getProductNameOrFlavourNameId());
            criteria.setSupplierId(resource.getSupplierId());
            criteria.setSupplierCurrency(resource.getSupplierCurrency());*/
            criteria.setInvoiceId(resource.getInvoiceId());
            FinalServiceOrder fso = finalServiceOrderService.getFSO(criteria);
            if (fso == null) {

                criteria.setUniqueId(resource.getUniqueId());
                criteria.setVersionNumber(resource.getVersionNumber());
                criteria.setServiceOrderType(resource.getType());
                criteria.setPageSize(provisionalServiceOrderRepository.getCount().intValue());
                BaseServiceOrderDetails searchResult = getPSO(criteria);
                //Temporary solution for finance, as they don't send orderId or bookId to generate PSO.
                //So Each time results are found, as bookId and OrderId not their to make it unique, hence PSO is not generated.
                if(StringUtils.isEmpty(resource.getBookingRefNo()) && StringUtils.isEmpty(resource.getOrderId()))
                    searchResult = null;

                String id=null;
                SupplierPricing supplierPricingDetails;
                BaseServiceOrderDetails pso;
                Float versionNo = 1.1f;
                String uniqueId = null;
                if(searchResult==null) {
                    pso = new ProvisionalServiceOrder();
                    supplierPricingDetails = new SupplierPricing();
                }else{
                    uniqueId = searchResult.getUniqueId();
                    versionNo = searchResult.getVersionNumber();
                    pso = searchResult;
                    supplierPricingDetails = pso.getSupplierPricing();
                    id = supplierPricingDetails.getId();
                }

//                BaseServiceOrderDetails pso = new ProvisionalServiceOrder();
                CopyUtils.copy(resource, pso);
                CopyUtils.copy(resource.getSupplierPricingResource(), supplierPricingDetails);
                supplierPricingDetails.setId(id);
                
                for(PassengersDetails passengersDetails : supplierPricingDetails.getPassengerDetails()) {
                    passengersDetails.setSupplierPricing(supplierPricingDetails);
                }
                pso.setSupplierPricing(supplierPricingDetails);

                if (resource.getGeneralInvoice() == null || !resource.getGeneralInvoice()) {
                    ServiceOrderAndSupplierLiabilityValidator.validateServiceOrderAndSupplierLiability(pso);
                    ServiceOrderAndSupplierLiabilityValidator.validateSupplierPricing(pso.getSupplierPricing());
                    ServiceOrderAndSupplierLiabilityValidator.validatePassengerDetails((Set<PassengersDetails>) pso.getSupplierPricing().getPassengerDetails());
                }

               if (searchResult != null) {
                    pso.setUniqueId(uniqueId);
                    pso.setVersionNumber(Float.valueOf(df.format(versionNo + 0.1f)));
                    pso.setLastModifiedTime(ZonedDateTime.now());
                } else {
                    pso.setUniqueId(idGenerator.generatePSOId(resource.getSupplierId()));
                    pso.setVersionNumber(versionNo);
                }

                pso.setStatus(Status.PROVISIONAL_SERVICE_ORDER_GENERATED);

                //Set Company Details(BU, SBU etc..) in the Database
                serviceOrderAndSupplierLiabilityService.setCompanyDetails(pso);
                ProvisionalServiceOrder provisionalServiceOrder = provisionalServiceOrderRepository.generatePSO((ProvisionalServiceOrder) pso);

                //alert finance user that PSO is generated
                try {
                    InlineMessageResource inlineMessageResource = new InlineMessageResource();
                    inlineMessageResource.setAlertName("PSO_GENERATED");
                    inlineMessageResource.setNotificationType("System");
                    ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
                    entity.put("provisionalServiceOrderID", provisionalServiceOrder.getUniqueId());
                    entity.put("orderID", provisionalServiceOrder.getOrderId());
                    entity.put("bookID", provisionalServiceOrder.getBookingRefNo());
                    inlineMessageResource.setDynamicVariables(entity);
                    alertService.sendInlineMessageAlert(inlineMessageResource);
                } catch (Exception e) {
                    logger.debug("inline alert notification is not sent");
                }

                resource.setProvisionalServiceOrderID(provisionalServiceOrder.getUniqueId());
                resource.setType(ServiceOrderAndSupplierLiabilityType.PSL);
                resource.setLinkedVersion(provisionalServiceOrder.getVersionNumber());
                ProvisionalSupplierLiability provisionalSupplierLiability = provisionalSupplierLiabilityService.generatePSL(resource);
                provisionalServiceOrder.setProvisionalServiceOrderID(provisionalServiceOrder.getUniqueId());
                provisionalServiceOrder.setProvisionalSupplierLiabilityID(provisionalSupplierLiability.getUniqueId());
                provisionalServiceOrder.setLinkedVersion(provisionalSupplierLiability.getLinkedVersion());
                provisionalServiceOrder = provisionalServiceOrderRepository.updatePSO(provisionalServiceOrder);

                if (!StringUtils.isEmpty(resource.getBookingRefNo()) && !StringUtils.isEmpty(resource.getOrderId())) {
                    try {
                        OpsProduct product = opsBookingService.getProduct(resource.getBookingRefNo(), resource.getOrderId());
                        if (product != null) {
                            FileProfSearchCriteria fileProfSearchCriteria = new FileProfSearchCriteria();
                            fileProfSearchCriteria.setBookingRefNumber(resource.getBookingRefNo());
                            fileProfSearchCriteria.setOrderId(resource.getOrderId());
                            List<FileProfitabilityBooking> fileProfitabilityBookings = fileProfitabilityService.getListOfFileProfsWRTCriteria(fileProfSearchCriteria);
                            fileProfitabilityService.calculationMethodForServiceOrder(product, fileProfitabilityBookings.iterator().next(), FileProfTypes.OPERATIONAL_PROFITABILITY);
                        }
                    } catch (Exception e) {
                        logger.debug("File profitability is not updated");
                    }
                }
                logger.info("PSO is generated successfully for order " + resource.getOrderId());
                updateFSOEntriesForPSO(provisionalServiceOrder);
                return provisionalServiceOrder;
            } else {
                throw new OperationException("Cannot generate PSO as FSO has already been generated for this product");
            }
        } else
            throw new OperationException("Cannot save PSO record as the service order type is not PSO");

    }

    private void updateFSOEntriesForPSO(ProvisionalServiceOrder provisionalServiceOrder) {
        try {
            logger.info("calling finance api to updateFSOEntries ");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", mdmToken.getToken());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("PSOId", provisionalServiceOrder.getProvisionalServiceOrderID());
            jsonObject.put("supplierId", provisionalServiceOrder.getSupplierId());
            jsonObject.put("supplierName", provisionalServiceOrder.getSupplierName());
            jsonObject.put("amountPaidToSupplier", provisionalServiceOrder.getSupplierPricing().getAmountPaidToSupplier());
            jsonObject.put("supplierGst", provisionalServiceOrder.getSupplierPricing().getSupplierGst());
            jsonObject.put("supplierCurrency", provisionalServiceOrder.getSupplierCurrency());
            jsonObject.put("bookingRefNo", provisionalServiceOrder.getBookingRefNo());
            if (provisionalServiceOrder.getCompanyMarketId() != null && provisionalServiceOrder.getCompanyMarketId().trim().length() > 0) {
                jsonObject.put("companyMarketId", provisionalServiceOrder.getCompanyMarketId());
            } else {
                jsonObject.put("companyMarketId", "GC22");  // temp fix
            }
            jsonObject.put("netPayableToSupplier", provisionalServiceOrder.getNetPayableToSupplier());
            jsonObject.put("generalInvoice", provisionalServiceOrder.getGeneralInvoice());
            jsonObject.put("serviceOrderType", provisionalServiceOrder.getType());
            jsonObject.put("versionNumber", provisionalServiceOrder.getVersionNumber());
            jsonObject.put("invoiceId", provisionalServiceOrder.getInvoiceId());
            jsonObject.put("productCategoryId", provisionalServiceOrder.getProductCategoryId());
            jsonObject.put("productCategorySubTypeId", provisionalServiceOrder.getProductCategorySubTypeId());
            jsonObject.put("dateOfGeneration", provisionalServiceOrder.getDateOfGeneration());
            jsonObject.put("supplierCost", provisionalServiceOrder.getSupplierPricing().getSupplierCost());
            if(provisionalServiceOrder.getSupplierBillPassing()!=null) {
                jsonObject.put("hsnSacCode", provisionalServiceOrder.getSupplierBillPassing().getHsn_SAS_code());
            }

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
    public ProvisionalServiceOrder updatePSO(ServiceOrderResource resource) throws OperationException {
       return updatePSO(resource, true);
    }

    @Override
    public ProvisionalServiceOrder updatePSO(ServiceOrderResource resource, Boolean checkForUserCompany) throws OperationException {
        //TODO: To check whteher to update the corresponding PSL.
        logger.info("Entered ProvisionalServiceOrderServiceImpl::updatePSO() method to update PSO");
        if (resource.getUniqueId() == null)
            throw new OperationException("Provide unique id to update the record");
        if (resource.getVersionNumber() == null)
            throw new OperationException("Provide version number to update the record");

        if (resource.getType() != null && resource.getType().equals(ServiceOrderAndSupplierLiabilityType.PSO)) {
            BaseServiceOrderDetails pso = getPSOById(resource.getUniqueId(), checkForUserCompany);

            SupplierPricing supplierPricingDetails = pso.getSupplierPricing();
            CopyUtils.copy(resource, pso);
            CopyUtils.copy(resource.getSupplierPricingResource(), supplierPricingDetails);
            for(PassengersDetails passengersDetails : supplierPricingDetails.getPassengerDetails()) {
                passengersDetails.setSupplierPricing(supplierPricingDetails);
            }
            pso.setSupplierPricing(supplierPricingDetails);
            if (resource.getGeneralInvoice() == null || !resource.getGeneralInvoice()) {
                ServiceOrderAndSupplierLiabilityValidator.validateServiceOrderAndSupplierLiability(pso);
                ServiceOrderAndSupplierLiabilityValidator.validateSupplierPricing(pso.getSupplierPricing());
                ServiceOrderAndSupplierLiabilityValidator.validatePassengerDetails((Set<PassengersDetails>) pso.getSupplierPricing().getPassengerDetails());
            }
            ProvisionalServiceOrder provisionalServiceOrder = provisionalServiceOrderRepository.updatePSO((ProvisionalServiceOrder) pso);
            if (provisionalServiceOrder.getStatus().equals(Status.PROVISIONAL_SERVICE_ORDER_CANCELLED)) {
                updateFSOEntriesForPSOCancellation(provisionalServiceOrder);
            }
            logger.info("PSO is updated successfully");
            return provisionalServiceOrder;
        } else
            throw new OperationException("Cannot update PSO record as the service order type is not PSO");
    }

    private void updateFSOEntriesForPSOCancellation(ProvisionalServiceOrder provisionalServiceOrder) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", mdmToken.getToken());
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            JSONObject jsonObject = new JSONObject();
            logger.info("calling finance api in updateFSOEntriesOnCancellation");
            jsonObject.put("PSOId", provisionalServiceOrder.getProvisionalServiceOrderID());
            jsonObject.put("supplierId", provisionalServiceOrder.getSupplierId());
            jsonObject.put("supplierName", provisionalServiceOrder.getSupplierName());
            jsonObject.put("amountPaidToSupplier", provisionalServiceOrder.getSupplierPricing().getAmountPaidToSupplier());
            jsonObject.put("supplierGst", provisionalServiceOrder.getSupplierPricing().getSupplierGst());
            jsonObject.put("supplierCurrency", provisionalServiceOrder.getSupplierCurrency());
            jsonObject.put("bookingRefNo", provisionalServiceOrder.getBookingRefNo());
            if (provisionalServiceOrder.getCompanyMarketId() != null && provisionalServiceOrder.getCompanyMarketId().trim().length() > 0) {
                jsonObject.put("companyMarketId", provisionalServiceOrder.getCompanyMarketId());
            } else {
                jsonObject.put("companyMarketId", "GC22");  // temp fix
            }
            jsonObject.put("netPayableToSupplier", provisionalServiceOrder.getNetPayableToSupplier());
            jsonObject.put("generalInvoice", provisionalServiceOrder.getGeneralInvoice());
            jsonObject.put("serviceOrderType", provisionalServiceOrder.getType());
            jsonObject.put("invoiceId", provisionalServiceOrder.getInvoiceId());
            jsonObject.put("productCategoryId", provisionalServiceOrder.getProductCategoryId());
            jsonObject.put("productCategorySubTypeId", provisionalServiceOrder.getProductCategorySubTypeId());
            jsonObject.put("dateOfGeneration", provisionalServiceOrder.getDateOfGeneration());

            jsonObject.put("supplierCost", provisionalServiceOrder.getSupplierPricing().getSupplierCost());
            if(provisionalServiceOrder.getSupplierBillPassing()!=null) {
                jsonObject.put("hsnSacCode", provisionalServiceOrder.getSupplierBillPassing().getHsn_SAS_code());
            }

            HttpEntity httpEntity = new HttpEntity(jsonObject.toString(), httpHeaders);
            RestTemplate restTemplate = RestUtils.getTemplate();
            restTemplate.exchange(update_FSO_entries_on_cancellation, HttpMethod.PUT, httpEntity, String.class);
        } catch (Exception e) {
            logger.error("Error in calling finance api in updateFSOEntriesOnCancellation");
            e.printStackTrace();
        }
    }

    @Override
    public ProvisionalServiceOrder getPSOById(String uniqueId) throws OperationException {
        return getPSOById(uniqueId, true);
    }

    @Override
    public ProvisionalServiceOrder getPSOById(String uniqueId, Boolean checkForUserCompany) throws OperationException {
        logger.info("Entered ProvisionalServiceOrderServiceImpl::getPSOById() method to get PSO details");
        if (!StringUtils.isEmpty(uniqueId)) {
            ServiceOrderSearchCriteria criteria = new ServiceOrderSearchCriteria();
            criteria.setUniqueId(uniqueId);
            List<ProvisionalServiceOrder> serviceOrders = (List<ProvisionalServiceOrder>) provisionalServiceOrderRepository.getProvisionalServiceOrders(criteria, checkForUserCompany).get("result");
            if (serviceOrders.size() == 1) {
                logger.info("Retrieved PSO details");
                return serviceOrders.iterator().next();
            }
            else
                throw new OperationException(Constants.ER01);
        } else
            throw new OperationException("Provide PSO unique id to get the details");
    }

    @Override
    public ServiceOrderResource getPSOResourceById(String uniqueId) throws OperationException {
        ProvisionalServiceOrder provisionalServiceOrder = getPSOById(uniqueId);
        ServiceOrderResource serviceOrderResource = new ServiceOrderResource();
        CopyUtils.copy(provisionalServiceOrder, serviceOrderResource);
        SupplierPricingResource supplierPricingResource = new SupplierPricingResource();
        CopyUtils.copy(provisionalServiceOrder.getSupplierPricing(), supplierPricingResource);
        serviceOrderResource.setSupplierPricingResource(supplierPricingResource);
        serviceOrderResource.setNetPayableToSupplier(supplierPricingResource.getNetPayableToSupplier());
        serviceOrderResource.setServiceOrderStatus(provisionalServiceOrder.getStatus().getValue());
        if(provisionalServiceOrder.getSupplierBillPassing()!=null) {
            SupplierBillPassingResource supplierBillPassingResource = new SupplierBillPassingResource();
            CopyUtils.copy(provisionalServiceOrder.getSupplierBillPassing(), supplierBillPassingResource);
            serviceOrderResource.setSupplierBillPassingResource(supplierBillPassingResource);
            serviceOrderResource.setSupplierBillPassingStatus(provisionalServiceOrder.getSupplierBillPassing().getSupplierBillPassingStatus());
        }
        if (provisionalServiceOrder.getStopPaymentStatus() != null && provisionalServiceOrder.getStopPaymentStatus() == StopPaymentStatus.ACTIVE) {
            StopPaymentResource stopPaymentResource = new StopPaymentResource();
            stopPaymentResource.setDate(provisionalServiceOrder.getStopPaymentTillDate());
            if (provisionalServiceOrder.getStopPaymentTillDate() != null)
                stopPaymentResource.setStopPaymentTill(provisionalServiceOrder.getStopPaymentTill().getValue());
            stopPaymentResource.setStopPaymentTill(provisionalServiceOrder.getStopPaymentStatus().getValue());
            serviceOrderResource.setStopPaymentResource(stopPaymentResource);
        }
        if (provisionalServiceOrder.getPaymentAdviceSet() != null && provisionalServiceOrder.getPaymentAdviceSet().size() >= 1) {
            serviceOrderResource.setPaymentAdviceStatus(provisionalServiceOrder.getPaymentAdviceSet().iterator().next().getPaymentAdviceStatus().getPaymentAdviseStatus());
        }
        return serviceOrderResource;
    }

    @Override
    public ProvisionalServiceOrder getPSOByVersionId(VersionId versionId) throws OperationException {
        if (versionId.getUniqueId() == null)
            throw new OperationException("Provide uniqueId to search Provisional Service Order");
        if (versionId.getVersionNumber() == null)
            throw new OperationException("Provide version number to search Provisional Service Order");

        ProvisionalServiceOrder provisionalServiceOrder = provisionalServiceOrderRepository.getPSOByVersionId(versionId);
        if (provisionalServiceOrder != null)
            return provisionalServiceOrder;
        else
            throw new OperationException(Constants.ER01);

    }

    @Override
    public Map<String, Object> getProvisionalServiceOrders(ServiceOrderSearchCriteria searchCriteria, Boolean checkForUserCompany) throws OperationException, IOException, JSONException {
        logger.info("Entered ProvisionalServiceOrderServiceImpl::getProvisionalServiceOrders() to search PSO");
        if (searchCriteria.getToGenerationDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
            String value = dateFormat.format(new Date());
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(value, DateTimeFormatter
                    .ofPattern("yyyy-MM-dd'T'HH:mm:ssz").withZone(ZoneId.systemDefault()));
            if (searchCriteria.getToGenerationDate().isAfter(zonedDateTime))
                throw new OperationException(Constants.ER582);
        }

        Map<String, Object> result = provisionalServiceOrderRepository.getProvisionalServiceOrders(searchCriteria, checkForUserCompany);
        List<ProvisionalServiceOrder> serviceOrders = (List<ProvisionalServiceOrder>) result.get("result");
        if (serviceOrders.size() == 0) {
            logger.warn("No records found");
            return result;
        }
        List<ServiceOrderResource> resourceList = new ArrayList<>();
        for (ProvisionalServiceOrder serviceOrder : serviceOrders) {
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
        logger.info("Retrieved details of PSO");
        result.put("result", resourceList);
        return result;
    }


    @Override
    public Map<String, Object> getProvisionalServiceOrders(ServiceOrderSearchCriteria searchCriteria) throws OperationException, IOException, JSONException {
        return getProvisionalServiceOrders(searchCriteria ,true);
    }

    @Override
    public Map<String, String> updateProvisionalServiceOrderStatusToCancelled(ServiceOrderResource resource) throws OperationException {
        ServiceOrderSearchCriteria criteria = new ServiceOrderSearchCriteria();
        criteria.setBookingRefNo(resource.getBookingRefNo());
        criteria.setOrderId(resource.getOrderId());
        criteria.setCompanyMarketId(resource.getCompanyMarketId());
        criteria.setProductCategoryId(resource.getProductCategoryId());
        criteria.setProductCategorySubTypeId(resource.getProductCategorySubTypeId());
        criteria.setProductNameId(resource.getProductNameId());
        criteria.setServiceOrderType(ServiceOrderAndSupplierLiabilityType.PSO);
        criteria.setSupplierId(resource.getSupplierId());
        criteria.setInvoiceId(resource.getInvoiceId());
        criteria.setPageSize(provisionalServiceOrderRepository.getCount().intValue());
        ProvisionalServiceOrder pso = getPSO(criteria);
        Map<String, String> result = new HashMap<>();
        if (pso != null) {
            pso.setStatus(Status.PROVISIONAL_SERVICE_ORDER_CANCELLED);
            provisionalServiceOrderRepository.updatePSO(pso);
            result.put("result", "PSO status is updated");
            return result;
        } else
            throw new OperationException("cannot update status as PSO does not exist");
    }

    @Override
    public List<ProvisionalServiceOrder> supplierBillPassingScheduler() {
        return provisionalServiceOrderRepository.supplierBillPassingScheduler();
    }

    @Override
    public void releasePaymentScheduler() {
        provisionalServiceOrderRepository.releasePaymentsScheduler();
    }

    @Override
    public ProvisionalServiceOrder updateProvisionalServiceOrder(ProvisionalServiceOrder provisionalServiceOrder) {
        return provisionalServiceOrderRepository.updatePSO(provisionalServiceOrder);
    }

    @Override
    public void generatePSO(String bookId, String orderId) throws OperationException{

        OpsBooking opsBooking = opsBookingService.getBooking(bookId);
        OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking, orderId);
        try {
            serviceOrderAndSupplierLiabilityService.generateServiceOrder(opsProduct, opsBooking, false);
        } catch (IOException e) {
           logger.error("Unable to generate PSO");
           throw new OperationException(Constants.OPS_ERR_30611);
        }

    }

    public ProvisionalServiceOrder getPSO(ServiceOrderSearchCriteria criteria) throws OperationException {
        logger.info("Entered ProvisionalServiceOrderServiceImpl::getPSO() method to get PSO details");
        List<ProvisionalServiceOrder> searchResult = (List<ProvisionalServiceOrder>) provisionalServiceOrderRepository.getProvisionalServiceOrders(criteria).get("result");
        if (searchResult.size() == 1)
            return searchResult.iterator().next();
        else if (searchResult.size() > 1) {
            Collections.sort(searchResult, new ServiceOrderVersionComparator());
            return searchResult.iterator().next();
        } else
            return null;
    }

}
