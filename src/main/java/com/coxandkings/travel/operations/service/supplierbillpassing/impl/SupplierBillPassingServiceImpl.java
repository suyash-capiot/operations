package com.coxandkings.travel.operations.service.supplierbillpassing.impl;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.enums.debitNote.*;
import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentAdviceStatusValues;
import com.coxandkings.travel.operations.enums.prepaymenttosupplier.ServiceOrderValue;
import com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityType;
import com.coxandkings.travel.operations.enums.supplierBillPassing.ApprovalStatus;
import com.coxandkings.travel.operations.enums.supplierBillPassing.StopPaymentUntil;
import com.coxandkings.travel.operations.enums.supplierBillPassing.SupplierBillPassingStatus;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdviceOrderInfo;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.*;
import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierBillPassing;
import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierInvoiceOCR;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.FinalServiceOrderRepository;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.FinalSupplierLiabilityRepository;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.ProvisionalServiceOrderRepository;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.ProvisionalSupplierLiabilityRepository;
import com.coxandkings.travel.operations.repository.supplierbillpassing.SupplierBillPassingRepository;
import com.coxandkings.travel.operations.repository.supplierbillpassing.SupplierInvoiceOCRRepo;
import com.coxandkings.travel.operations.resource.creditDebitNote.CreditDebitNote;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.PaymentAdviceResource;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.SupplierPaymentResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.SupplierPricingResource;
import com.coxandkings.travel.operations.resource.supplierbillpassing.AttachedServiceOrder;
import com.coxandkings.travel.operations.resource.supplierbillpassing.PaymentAdviceGeneration;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierBillPassingResource;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierInvoiceSearchCriteria;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.PaymentAdviceService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.*;
import com.coxandkings.travel.operations.service.supplierbillpassing.SupplierBillPassingService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.*;
import com.coxandkings.travel.operations.validations.HibernateValidator;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SupplierBillPassingServiceImpl implements SupplierBillPassingService {

    @Autowired
    private SupplierBillPassingRepository supplierBillPassingRepository;
    @Autowired
    private ProvisionalServiceOrderService provisionalServiceOrderService;
    @Autowired
    private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;
    @Autowired
    private FinalServiceOrderService finalServiceOrderService;
    @Autowired
    private FinalSupplierLiabilityService finalSupplierLiabilityService;
    @Autowired
    private ProvisionalSupplierLiabilityService provisionalSupplierLiabilityService;
    @Autowired
    private FinalServiceOrderRepository finalServiceOrderRepository;
    @Autowired
    private FinalSupplierLiabilityRepository finalSupplierLiabilityRepository;
    @Autowired
    private ProvisionalServiceOrderRepository provisionalServiceOrderRepository;
    @Autowired
    private ProvisionalSupplierLiabilityRepository provisionalSupplierLiabilityRepository;
    @Autowired
    private SupplierInvoiceOCRRepo supplierInvoiceOCRRepo;
    @Autowired
    private AlertService alertService;
    @Autowired
    private ToDoTaskService toDoTaskService;
    @Autowired
    private PaymentAdviceService paymentAdviceService;
    @Autowired
    private MDMToken mdmToken;
    @Autowired
    private CreditDebitNoteUtils creditDebitNoteUtils;
    @Value("${finance-general-invoice.supplier-bill-passing}")
    private String financeGeneralInvoiceUrl;
    @Value("${finance-general-invoice.paymentAdvice}")
    private String financePaymentAdviceUrl;
    @Value(value = "${flight_discrepancy.get_final_service_orders}")
    private String getFinanceFinalServieOrders;
    @Value(value = "${supplier-bill-passing.alert.approval}")
    private String approvalAlert;
    @Value(value = "${supplier-bill-passing.alert.completed}")
    private String completedAlert;
    private Logger logger = Logger.getLogger(SupplierBillPassingServiceImpl.class);
    @Autowired
    private JsonObjectProvider jsonObjectProvider;
    @Autowired
    private UserService userService;
    @Value(value = "${final_service_order.finance.update_FSO_entries_on_cancellation}")
    private String update_FSO_entries_on_cancellation;
    @Autowired
    private RestUtils restUtils;


    @Override
    public Map commonBillEntry(SupplierBillPassingResource supplierBillPassingResource) throws OperationException, IOException, JSONException {

        SupplierBillPassing supplierBillPassing = null;
        if (StringUtils.isEmpty(supplierBillPassingResource.getId()))
            supplierBillPassing = new SupplierBillPassing();
        else {
            supplierBillPassing = supplierBillPassingRepository.get(supplierBillPassingResource.getId());
            if (supplierBillPassing == null) throw new OperationException(Constants.ER441);
            if (!supplierBillPassing.getProvisionalServiceOrders().isEmpty())
                throw new OperationException(Constants.ER411);
        }
        CopyUtils.copy(supplierBillPassingResource, supplierBillPassing);
        HibernateValidator.supplierBillPassingValidator(supplierBillPassing);

        SupplierInvoiceOCR supplierInvoiceOCR = null;
        if (supplierBillPassing.getManualEntry() != null && !supplierBillPassing.getManualEntry()) {
            SupplierInvoiceSearchCriteria supplierInvoiceSearchCriteria = new SupplierInvoiceSearchCriteria();
            supplierInvoiceSearchCriteria.setInvoiceNumber(supplierBillPassingResource.getInvoiceNumber());
            supplierInvoiceSearchCriteria.setSupplierId(supplierBillPassingResource.getSupplierId());
            List<SupplierInvoiceOCR> supplierInvoiceOCRList = supplierInvoiceOCRRepo.getAvailableInvoice(supplierInvoiceSearchCriteria);
            if (supplierInvoiceOCRList.isEmpty()) throw new OperationException(Constants.ER01);
            else supplierInvoiceOCR = supplierInvoiceOCRList.get(0);
        }

        Set<ProvisionalServiceOrder> provisionalServiceOrders = new HashSet<>();
        BigDecimal totalNetPayableToSupplier = BigDecimal.ZERO, equivalentServiceOrderAmount = BigDecimal.ZERO;

        for (AttachedServiceOrder attachedServiceOrder : supplierBillPassingResource.getAttachedServiceOrders()) {
            totalNetPayableToSupplier = totalNetPayableToSupplier.add(attachedServiceOrder.getNetPayableToSupplier());
            ProvisionalServiceOrder provisionalServiceOrder = provisionalServiceOrderService.getPSOById(attachedServiceOrder.getServiceOrderId());
            if (provisionalServiceOrder == null) throw new OperationException(Constants.ER40);
            if (provisionalServiceOrder.getFinalServiceOrderID() != null || provisionalServiceOrder.getSupplierBillPassing() != null)
                throw new OperationException(Constants.ER411);
            if (!((supplierBillPassing.getSupplierId()).equalsIgnoreCase(provisionalServiceOrder.getSupplierId()) /*&& attachedServiceOrder.getProductName().equalsIgnoreCase(provisionalServiceOrder.getProductNameOrFlavourNameId())*/))
                throw new OperationException(Constants.ER491);
            equivalentServiceOrderAmount = equivalentServiceOrderAmount.add(provisionalServiceOrder.getSupplierPricing().getNetPayableToSupplier());
            attachedServiceOrder.setDifferenceInAmt(provisionalServiceOrder.getSupplierPricing().getNetPayableToSupplier().subtract(attachedServiceOrder.getNetPayableToSupplier()));
            provisionalServiceOrder.setAttachedNetPayableToSupplier(attachedServiceOrder.getNetPayableToSupplier());
            provisionalServiceOrders.add(provisionalServiceOrder);
        }

        if (totalNetPayableToSupplier.compareTo(supplierBillPassingResource.getNetPayableToSupplier()) != 0)
            throw new OperationException(Constants.ER601);
        if (equivalentServiceOrderAmount.compareTo(supplierBillPassingResource.getEquivalentServiceOrderAmount()) != 0)
            throw new OperationException(Constants.ER691);

        Map<String, Object> entity = new HashMap<>();
        for (AttachedServiceOrder attachedServiceOrder : supplierBillPassingResource.getAttachedServiceOrders()) {
            if (attachedServiceOrder.getDifferenceInAmt().compareTo(BigDecimal.ZERO) != 0) {
                entity.put("error", supplierBillPassingResource);
                throw new OperationException(entity);
            }
        }

        ServiceOrderResource serviceOrderResource = null;
        Set<FinalServiceOrder> finalServiceOrders = new HashSet<>();
        Set<ProvisionalSupplierLiability> provisionalSupplierLiabilities = new HashSet<>();
        Set<FinalSupplierLiability> finalSupplierLiabilities = new HashSet<>();

        for (ProvisionalServiceOrder provisionalServiceOrder : provisionalServiceOrders) {
            provisionalServiceOrder.setTotalDiffAmount(BigDecimal.ZERO);
            serviceOrderResource = new ServiceOrderResource();
            CopyUtils.copy(provisionalServiceOrder, serviceOrderResource);
            SupplierPricingResource supplierPricingResource = new SupplierPricingResource();
            CopyUtils.copy(provisionalServiceOrder.getSupplierPricing(), supplierPricingResource);
            serviceOrderResource.setSupplierPricingResource(supplierPricingResource);
            serviceOrderResource.setNetPayableToSupplier(supplierPricingResource.getNetPayableToSupplier());
            serviceOrderResource.setType(ServiceOrderAndSupplierLiabilityType.FSO);
            FinalServiceOrder finalServiceOrder = finalServiceOrderService.generateFSO(serviceOrderResource);
            ProvisionalSupplierLiability provisionalSupplierLiability = provisionalSupplierLiabilityService.getPSLById(provisionalServiceOrder.getProvisionalSupplierLiabilityID());
            provisionalSupplierLiabilities.add(provisionalSupplierLiability);
            FinalSupplierLiability finalSupplierLiability = finalSupplierLiabilityService.getFSLById(finalServiceOrder.getFinalSupplierLiabilityID());
            finalSupplierLiabilities.add(finalSupplierLiability);
            provisionalServiceOrder.setFinalServiceOrderID(finalServiceOrder.getUniqueId());
            provisionalServiceOrder.setFinalSupplierLiabilityID(finalServiceOrder.getFinalSupplierLiabilityID());
            finalServiceOrders.add(finalServiceOrder);
        }

        for (ProvisionalServiceOrder provisionalServiceOrder : provisionalServiceOrders)
            provisionalServiceOrder.setSupplierBillPassing(supplierBillPassing);

        for (ProvisionalSupplierLiability provisionalSupplierLiability : provisionalSupplierLiabilities)
            provisionalSupplierLiability.setSupplierBillPassing(supplierBillPassing);

        for (FinalServiceOrder finalServiceOrder : finalServiceOrders)
            finalServiceOrder.setSupplierBillPassing(supplierBillPassing);

        for (FinalSupplierLiability finalSupplierLiability : finalSupplierLiabilities)
            finalSupplierLiability.setSupplierBillPassing(supplierBillPassing);

        supplierBillPassing.setProvisionalServiceOrders(provisionalServiceOrders);
        supplierBillPassing.setFinalServiceOrders(finalServiceOrders);
        supplierBillPassing.setFinalSupplierLiabilities(finalSupplierLiabilities);
        supplierBillPassing.setProvisionalSupplierLiabilities(provisionalSupplierLiabilities);
        supplierBillPassing.setSupplierBillPassingStatus(SupplierBillPassingStatus.DONE.getValue());
        supplierBillPassingRepository.add(supplierBillPassing);
        if (supplierBillPassing.getManualEntry() != null && !supplierBillPassing.getManualEntry())
            updateSupplierInvoiceOCRDetails(supplierInvoiceOCR);
        createAlertandOperationsToDoTask(supplierBillPassing);

        for (ProvisionalServiceOrder provisionalServiceOrder : provisionalServiceOrders) {
            if (provisionalServiceOrder.getGeneralInvoice() != null && provisionalServiceOrder.getGeneralInvoice())
                callFinanceAfterSupplierBullPassing(provisionalServiceOrder.getInvoiceId(), provisionalServiceOrder.getFinalServiceOrderID());
        }
        entity.put("message", "BillPassing is done successfully");
        return entity;
    }

    private void callFinanceAfterSupplierBullPassing(String invoiceId, String fsoId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", mdmToken.getToken());
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        try {
            restUtils.exchange(financeGeneralInvoiceUrl + invoiceId +"/"+ fsoId, HttpMethod.PUT, httpEntity, Object.class);
        } catch (Exception e) {
            logger.error("Error in calling finance api");
        }
    }

    private void updateSupplierInvoiceOCRDetails(SupplierInvoiceOCR supplierInvoiceOCR) throws OperationException {
        supplierInvoiceOCR.setUsed(true);
        supplierInvoiceOCRRepo.update(supplierInvoiceOCR);
    }

    @Override
    public Map singleBillEntry(SupplierBillPassingResource supplierBillPassingResource) throws OperationException, IOException, JSONException {
        if (supplierBillPassingResource.getAttachedServiceOrders().isEmpty())
            throw new OperationException(Constants.ER391);
        SupplierBillPassing supplierBillPassing = null;
        if (StringUtils.isEmpty(supplierBillPassingResource.getId()))
            supplierBillPassing = new SupplierBillPassing();
        else {
            supplierBillPassing = supplierBillPassingRepository.get(supplierBillPassingResource.getId());
            if (supplierBillPassing == null) throw new OperationException(Constants.ER441);
            if (!supplierBillPassing.getProvisionalServiceOrders().isEmpty())
                throw new OperationException(Constants.ER411);
        }
        CopyUtils.copy(supplierBillPassingResource, supplierBillPassing);
        HibernateValidator.supplierBillPassingValidator(supplierBillPassing);

        SupplierInvoiceOCR supplierInvoiceOCR = null;
        if (supplierBillPassing.getManualEntry() != null && !supplierBillPassing.getManualEntry()) {
            SupplierInvoiceSearchCriteria supplierInvoiceSearchCriteria = new SupplierInvoiceSearchCriteria();
            supplierInvoiceSearchCriteria.setInvoiceNumber(supplierBillPassingResource.getInvoiceNumber());
            supplierInvoiceSearchCriteria.setSupplierId(supplierBillPassingResource.getSupplierId());
            List<SupplierInvoiceOCR> supplierInvoiceOCRList = supplierInvoiceOCRRepo.getAvailableInvoice(supplierInvoiceSearchCriteria);
            if (supplierInvoiceOCRList.isEmpty())
                throw new OperationException(Constants.ER01);
            else supplierInvoiceOCR = supplierInvoiceOCRList.get(0);
        }

        AttachedServiceOrder attachedServiceOrder = supplierBillPassingResource.getAttachedServiceOrders().stream().iterator().next();
        ProvisionalServiceOrder provisionalServiceOrder = provisionalServiceOrderService.getPSOById(attachedServiceOrder.getServiceOrderId());
        if (provisionalServiceOrder == null) throw new OperationException(Constants.ER401);
        if (provisionalServiceOrder.getFinalServiceOrderID() != null || provisionalServiceOrder.getSupplierBillPassing() != null)
            throw new OperationException(Constants.ER411);
        if (!((supplierBillPassingResource.getSupplierId()).equalsIgnoreCase(provisionalServiceOrder.getSupplierId()))) //&& supplierBillPassingResource.getProductName().iterator().next().equalsIgnoreCase(provisionalServiceOrder.getProductNameOrFlavourNameId())))
            throw new OperationException(Constants.ER491);
        if (supplierBillPassingResource.getNetPayableToSupplier().compareTo(attachedServiceOrder.getNetPayableToSupplier()) != 0)
            throw new OperationException(Constants.ER601);
        if (supplierBillPassingResource.getEquivalentServiceOrderAmount().compareTo(provisionalServiceOrder.getSupplierPricing().getNetPayableToSupplier()) != 0)
            throw new OperationException(Constants.ER691);

        /*BigDecimal diffAmount=provisionalServiceOrder.getSupplierPricing().getSupplierCost().subtract(supplierBillPassing.getSupplierInvoiceTotalCost());
        provisionalServiceOrder.setDiffAmount(diffAmount);
        BigDecimal diffInGst=provisionalServiceOrder.getSupplierPricing().getSupplierGst().subtract(supplierBillPassing.getSupplierGst());
        provisionalServiceOrder.setDiffInGst(diffInGst);*/
        provisionalServiceOrder.setTotalDiffAmount(provisionalServiceOrder.getSupplierPricing().getNetPayableToSupplier().subtract(supplierBillPassing.getNetPayableToSupplier()));

        ServiceOrderResource finalServiceOrderResource = new ServiceOrderResource();
        CopyUtils.copy(provisionalServiceOrder, finalServiceOrderResource);
        SupplierPricingResource supplierPricingResource = new SupplierPricingResource();
        CopyUtils.copy(provisionalServiceOrder.getSupplierPricing(), supplierPricingResource);
        finalServiceOrderResource.setSupplierPricingResource(supplierPricingResource);
        finalServiceOrderResource.setNetPayableToSupplier(supplierPricingResource.getNetPayableToSupplier());
        finalServiceOrderResource.setType(ServiceOrderAndSupplierLiabilityType.FSO);

        if (!(provisionalServiceOrder.getSupplierPricing().getSupplierCost().compareTo(supplierBillPassing.getNetPayableToSupplier()) == 0))
            finalServiceOrderResource = finalSupplierPricing(finalServiceOrderResource, supplierBillPassing);
        FinalServiceOrder finalServiceOrder = finalServiceOrderService.generateFSO(finalServiceOrderResource);
        provisionalServiceOrder.setFinalServiceOrderID(finalServiceOrder.getUniqueId());
        provisionalServiceOrder.setFinalSupplierLiabilityID(finalServiceOrder.getFinalSupplierLiabilityID());
        provisionalServiceOrder.setAttachedNetPayableToSupplier(attachedServiceOrder.getNetPayableToSupplier());
        supplierBillPassing = setMappings(provisionalServiceOrder, finalServiceOrder, supplierBillPassing);
        if (provisionalServiceOrder.getSupplierPricing().getNetPayableToSupplier().compareTo(supplierBillPassing.getNetPayableToSupplier()) < 0)
            return supplierInvoiceCostGtPSO(provisionalServiceOrder, supplierInvoiceOCR, supplierBillPassing);
        else return supplierInvoiceCostLtOrEqToPSO(provisionalServiceOrder, supplierInvoiceOCR, supplierBillPassing);

    }

    @Override
    public SupplierBillPassingResource get(String id) throws OperationException {
        SupplierBillPassing supplierBillPassing = supplierBillPassingRepository.get(id);
        if (supplierBillPassing == null) throw new OperationException(Constants.ER01);

        SupplierBillPassingResource supplierBillPassingResource = new SupplierBillPassingResource();
        CopyUtils.copy(supplierBillPassing, supplierBillPassingResource);
        Set<AttachedServiceOrder> attachedServiceOrders = new HashSet<>();
        supplierBillPassing.getProvisionalServiceOrders().stream().forEach(provisionalServiceOrder -> {
            AttachedServiceOrder attachedServiceOrder = new AttachedServiceOrder();
            attachedServiceOrder.setSupplierId(provisionalServiceOrder.getSupplierId());
            attachedServiceOrder.setSupplierName(provisionalServiceOrder.getSupplierName());
            attachedServiceOrder.setProductName(provisionalServiceOrder.getProductNameId());
            attachedServiceOrder.setServiceOrderValue(provisionalServiceOrder.getSupplierPricing().getSupplierCost());
            attachedServiceOrder.setGst(provisionalServiceOrder.getSupplierPricing().getSupplierGst());
            attachedServiceOrder.setTotalCost(provisionalServiceOrder.getSupplierPricing().getNetPayableToSupplier());
            attachedServiceOrder.setServiceOrderType(provisionalServiceOrder.getType().getValue());
            attachedServiceOrder.setServiceOrderId(provisionalServiceOrder.getUniqueId());
            attachedServiceOrder.setNetPayableToSupplier(provisionalServiceOrder.getAttachedNetPayableToSupplier());
            attachedServiceOrder.setCurrency(provisionalServiceOrder.getSupplierCurrency());
            attachedServiceOrders.add(attachedServiceOrder);
        });

        supplierBillPassingResource.setAttachedServiceOrders(attachedServiceOrders);
        return supplierBillPassingResource;
    }

    @Override
    public List<SupplierBillPassing> getAll() {
        return supplierBillPassingRepository.getAll();
    }

    @Override
    public Map updateStatus(String billPassingId, String status, String remarks) throws OperationException, IOException {
        if (StringUtils.isEmpty(status)) throw new OperationException(Constants.ER35);
        SupplierBillPassing supplierBillPassing = supplierBillPassingRepository.get(billPassingId);
        if (supplierBillPassing == null) throw new OperationException(Constants.ER441);
        if (!supplierBillPassing.getSupplierBillPassingStatus().equalsIgnoreCase(SupplierBillPassingStatus.PENDING_APPROVAL.getValue()))
            throw new OperationException(Constants.ER631);
        if (Arrays.stream(ApprovalStatus.values()).noneMatch(approvalStatus -> approvalStatus.getValue().equalsIgnoreCase(status)))
            throw new OperationException(Constants.ER35);
        if (status.equalsIgnoreCase(ApprovalStatus.APPROVED.getValue())) approved(supplierBillPassing);
        else if (status.equalsIgnoreCase(ApprovalStatus.REJECTED.getValue())) rejected(supplierBillPassing);

        if (!StringUtils.isEmpty(remarks))
            supplierBillPassing.setRemarks(remarks);

        Map<String, String> entity = new HashMap<>();
        entity.put("message", "billPassing status updated successfully");
        return entity;
    }

    public void approved(SupplierBillPassing supplierBillPassing) {
        supplierBillPassing.setSupplierBillPassingStatus(SupplierBillPassingStatus.APPROVED.getValue());
        supplierBillPassingRepository.update(supplierBillPassing);
        try {
            toDoTaskService.updateToDoTaskStatus(supplierBillPassing.getId(), ToDoTaskSubTypeValues.SUPPLIER_BILL_PASSING, ToDoTaskStatusValues.CLOSED);
        } catch (Exception e) {
            logger.debug("Error in updating to do task status");
        }
        createAlertandOperationsToDoTask(supplierBillPassing);

    }

    public void rejected(SupplierBillPassing supplierBillPassing) throws OperationException, IOException {
        supplierBillPassing.setSupplierBillPassingStatus(SupplierBillPassingStatus.REJECTED.getValue());
        ProvisionalServiceOrder provisionalServiceOrder = supplierBillPassing.getProvisionalServiceOrders().iterator().next();
        supplierBillPassing.setNetPayableToSupplier(provisionalServiceOrder.getSupplierPricing().getNetPayableToSupplier());
        supplierBillPassing.setSupplierInvoiceTotalCost(provisionalServiceOrder.getSupplierPricing().getSupplierCost());
        supplierBillPassing.setSupplierGst(provisionalServiceOrder.getSupplierPricing().getSupplierGst());
        provisionalServiceOrder.setDiffAmount(BigDecimal.ZERO);
        provisionalServiceOrder.setDiffInGst(BigDecimal.ZERO);
        provisionalServiceOrder.setTotalDiffAmount(BigDecimal.ZERO);

        FinalServiceOrder finalServiceOrder = supplierBillPassing.getFinalServiceOrders().iterator().next();
        FinalSupplierLiability finalSupplierLiability = supplierBillPassing.getFinalSupplierLiabilities().iterator().next();

        ServiceOrderResource finalServiceOrderResource = new ServiceOrderResource();
        CopyUtils.copy(finalServiceOrder, finalServiceOrderResource);
        SupplierPricingResource pricingResource = new SupplierPricingResource();
        CopyUtils.copy(provisionalServiceOrder.getSupplierPricing(), pricingResource);
        finalServiceOrderResource.setSupplierPricingResource(pricingResource);
        finalServiceOrderResource.setNetPayableToSupplier(pricingResource.getNetPayableToSupplier());

        finalServiceOrder.setStatus(com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.Status.FINAL_SERVICE_ORDER_CANCELLED);
        finalServiceOrder.setSupplierBillPassing(null);
        finalSupplierLiability.setSupplierBillPassing(null);
        supplierBillPassing.getFinalServiceOrders().remove(finalServiceOrder);
        supplierBillPassing.getFinalSupplierLiabilities().remove(finalSupplierLiability);
        updateFSOEntriesOnCancellation(finalServiceOrder);
        finalServiceOrderRepository.updateFSO(finalServiceOrder);
        finalSupplierLiabilityRepository.updateFSL(finalSupplierLiability);

        finalServiceOrder = finalServiceOrderService.generateFSO(finalServiceOrderResource);
        finalServiceOrder.setSupplierBillPassing(supplierBillPassing);
        finalSupplierLiability = finalSupplierLiabilityService.getFSLById(finalServiceOrder.getFinalSupplierLiabilityID());
        finalSupplierLiability.setSupplierBillPassing(supplierBillPassing);

        provisionalServiceOrder.setFinalServiceOrderID(finalServiceOrder.getUniqueId());
        provisionalServiceOrder.setFinalSupplierLiabilityID(finalServiceOrder.getFinalSupplierLiabilityID());

        Set<FinalServiceOrder> finalServiceOrders = new HashSet<>();
        finalServiceOrders.add(finalServiceOrder);
        supplierBillPassing.setFinalServiceOrders(finalServiceOrders);

        Set<FinalSupplierLiability> finalSupplierLiabilities = new HashSet<>();
        finalSupplierLiabilities.add(finalSupplierLiability);
        supplierBillPassing.setFinalSupplierLiabilities(finalSupplierLiabilities);
        supplierBillPassingRepository.update(supplierBillPassing);
        try {
            toDoTaskService.updateToDoTaskStatus(supplierBillPassing.getId(), ToDoTaskSubTypeValues.SUPPLIER_BILL_PASSING, ToDoTaskStatusValues.CLOSED);
        } catch (Exception e) {
            logger.debug("Error in updating to do task status");
        }
        createAlertandOperationsToDoTask(supplierBillPassing);
    }


    private void updateFSOEntriesOnCancellation(FinalServiceOrder finalServiceOrder) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", mdmToken.getToken());
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            JSONObject jsonObject = new JSONObject();
            logger.info("calling finance api in updateFSOEntriesOnCancellation");
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
            jsonObject.put("invoiceId", finalServiceOrder.getInvoiceId());
            jsonObject.put("productCategoryId", finalServiceOrder.getProductCategoryId());
            jsonObject.put("productCategorySubTypeId", finalServiceOrder.getProductCategorySubTypeId());
            jsonObject.put("dateOfGeneration", finalServiceOrder.getDateOfGeneration());
            HttpEntity httpEntity = new HttpEntity(jsonObject.toString(), httpHeaders);
            RestTemplate restTemplate = RestUtils.getTemplate();
            restTemplate.exchange(update_FSO_entries_on_cancellation, HttpMethod.PUT, httpEntity, String.class);
        } catch (Exception e) {
            logger.error("Error in calling finance api in updateFSOEntriesOnCancellation");
            e.printStackTrace();
        }
    }

    public ServiceOrderResource finalSupplierPricing(ServiceOrderResource finalServiceOrderResource, SupplierBillPassing supplierBillPassing) {
        SupplierPricingResource finalSupplierPricing = new SupplierPricingResource();
        CopyUtils.copy(finalServiceOrderResource.getSupplierPricingResource(), finalSupplierPricing);
        finalSupplierPricing.setId(null);
        finalSupplierPricing.setNetPayableToSupplier(supplierBillPassing.getNetPayableToSupplier());
        finalSupplierPricing.setSupplierGst(supplierBillPassing.getSupplierGst());
        finalSupplierPricing.setSupplierCost(supplierBillPassing.getSupplierInvoiceTotalCost());
        finalSupplierPricing.setTotalBalanceAmountPayable(finalSupplierPricing.getNetPayableToSupplier().subtract(finalSupplierPricing.getAmountPaidToSupplier()));
        finalServiceOrderResource.setSupplierPricingResource(finalSupplierPricing);
        finalServiceOrderResource.setNetPayableToSupplier(finalSupplierPricing.getNetPayableToSupplier());
        return finalServiceOrderResource;
    }

    public Map supplierInvoiceCostLtOrEqToPSO(ProvisionalServiceOrder provisionalServiceOrder, SupplierInvoiceOCR supplierInvoiceOCR, SupplierBillPassing supplierBillPassing) throws OperationException {
        supplierBillPassing.setSupplierBillPassingStatus(SupplierBillPassingStatus.DONE.getValue());
        supplierBillPassing = supplierBillPassingRepository.add(supplierBillPassing);
        if (supplierBillPassing.getManualEntry() != null && !supplierBillPassing.getManualEntry())
            updateSupplierInvoiceOCRDetails(supplierInvoiceOCR);
        createAlertandOperationsToDoTask(supplierBillPassing);
        if (provisionalServiceOrder.getGeneralInvoice() != null && provisionalServiceOrder.getGeneralInvoice())
            callFinanceAfterSupplierBullPassing(provisionalServiceOrder.getInvoiceId(), provisionalServiceOrder.getFinalServiceOrderID());
        Map<String, String> response = new HashMap<>();
        response.put("message", "BillPassing is done successfully");
        return response;
    }

    public Map supplierInvoiceCostGtPSO(ProvisionalServiceOrder provisionalServiceOrder, SupplierInvoiceOCR supplierInvoiceOCR, SupplierBillPassing supplierBillPassing) throws OperationException {
        supplierBillPassing.setSupplierBillPassingStatus(SupplierBillPassingStatus.PENDING_APPROVAL.getValue());
        supplierBillPassing = supplierBillPassingRepository.add(supplierBillPassing);
        if (supplierBillPassing.getManualEntry() != null && !supplierBillPassing.getManualEntry())
            updateSupplierInvoiceOCRDetails(supplierInvoiceOCR);
        createAlertandApprovalToDoTask(supplierBillPassing);
        if (provisionalServiceOrder.getGeneralInvoice() != null && provisionalServiceOrder.getGeneralInvoice())
            callFinanceAfterSupplierBullPassing(provisionalServiceOrder.getInvoiceId(), provisionalServiceOrder.getFinalServiceOrderID());
        Map<String, String> response = new HashMap<>();
        response.put("message", "BillPassing is done successfully");
        return response;
    }

    public SupplierBillPassing setMappings(ProvisionalServiceOrder provisionalServiceOrder, FinalServiceOrder finalServiceOrder, SupplierBillPassing supplierBillPassing) throws OperationException {
        Set<ProvisionalServiceOrder> provisionalServiceOrders = new HashSet<>();
        Set<FinalServiceOrder> finalServiceOrders = new HashSet<>();
        Set<ProvisionalSupplierLiability> provisionalSupplierLiabilities = new HashSet<>();
        Set<FinalSupplierLiability> finalSupplierLiabilities = new HashSet<>();
        ProvisionalSupplierLiability provisionalSupplierLiability = provisionalSupplierLiabilityService.getPSLById(provisionalServiceOrder.getProvisionalSupplierLiabilityID());
        FinalSupplierLiability finalSupplierLiability = finalSupplierLiabilityService.getFSLById(finalServiceOrder.getFinalSupplierLiabilityID());

        provisionalSupplierLiability.setDiffAmount(provisionalServiceOrder.getDiffAmount());
        provisionalSupplierLiability.setDiffInGst(provisionalServiceOrder.getDiffInGst());
        provisionalSupplierLiability.setTotalDiffAmount(provisionalServiceOrder.getTotalDiffAmount());

        provisionalSupplierLiability.setSupplierBillPassing(supplierBillPassing);
        finalSupplierLiability.setSupplierBillPassing(supplierBillPassing);
        provisionalServiceOrder.setSupplierBillPassing(supplierBillPassing);
        finalServiceOrder.setSupplierBillPassing(supplierBillPassing);
        finalServiceOrders.add(finalServiceOrder);
        provisionalServiceOrders.add(provisionalServiceOrder);
        provisionalSupplierLiabilities.add(provisionalSupplierLiability);
        finalSupplierLiabilities.add(finalSupplierLiability);

        supplierBillPassing.setProvisionalServiceOrders(provisionalServiceOrders);
        supplierBillPassing.setFinalServiceOrders(finalServiceOrders);
        supplierBillPassing.setProvisionalSupplierLiabilities(provisionalSupplierLiabilities);
        supplierBillPassing.setFinalSupplierLiabilities(finalSupplierLiabilities);
        return supplierBillPassing;
    }

    public void createAlertandApprovalToDoTask(SupplierBillPassing supplierBillPassing) {
        InlineMessageResource inlineMessageResource = new InlineMessageResource();
        inlineMessageResource.setNotificationType("System");
        inlineMessageResource.setAlertName(approvalAlert);
        ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
        entity.put("supplierBillPassingId", supplierBillPassing.getId());
        inlineMessageResource.setDynamicVariables(entity);

        ToDoTaskResource todo = new ToDoTaskResource();
        todo.setTaskSubTypeId(ToDoTaskSubTypeValues.SUPPLIER_BILL_PASSING.toString());
        todo.setTaskPriorityId(ToDoTaskPriorityValues.MEDIUM.getValue());
        todo.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
        todo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        todo.setTaskOrientedTypeId(ToDoTaskOrientedValues.APPROVAL_ORIENTED.getValue());
        todo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
        todo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
        todo.setReferenceId(supplierBillPassing.getId());
        todo.setCreatedByUserId(userService.getLoggedInUserId());
        todo.setDueOnDate(ZonedDateTime.now().plusDays(5));

        try {
            toDoTaskService.save(todo);
        } catch (Exception e) {
            logger.error("Error in saving todo task");
        }
        try {
            alertService.sendInlineMessageAlert(inlineMessageResource);
        } catch (OperationException e) {
            logger.error("Error in sending inline message");
        }
    }

    public void createAlertandOperationsToDoTask(SupplierBillPassing supplierBillPassing) {
        InlineMessageResource inlineMessageResource = new InlineMessageResource();
        inlineMessageResource.setAlertName(completedAlert);
        inlineMessageResource.setNotificationType("System");
        ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
        entity.put("supplierBillPassingId", supplierBillPassing.getId());
        inlineMessageResource.setDynamicVariables(entity);

        ToDoTaskResource todo = new ToDoTaskResource();
        todo.setTaskSubTypeId(ToDoTaskSubTypeValues.SUPPLIER_BILL_PASSING.toString());
        todo.setTaskPriorityId(ToDoTaskPriorityValues.MEDIUM.getValue());
        todo.setTaskNameId(ToDoTaskNameValues.SETTLEMENT.getValue());
        todo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        todo.setTaskOrientedTypeId(ToDoTaskOrientedValues.ACTION_ORIENTED.getValue());
        todo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
        todo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
        todo.setReferenceId(supplierBillPassing.getId());
        todo.setCreatedByUserId(userService.getLoggedInUserId());
        todo.setDueOnDate(ZonedDateTime.now().plusDays(5));

        try {
            toDoTaskService.save(todo);
        } catch (Exception e) {
            logger.error("Error in saving todo task");
        }
        try {
            alertService.sendInlineMessageAlert(inlineMessageResource);
        } catch (Exception e) {
            logger.error("Error in sending inline message");
        }
    }

    private void updateCreditDebitMemo(String claimNo, ProvisionalServiceOrder provisionalServiceOrder) throws OperationException {
        ProvisionalSupplierLiability provisionalSupplierLiability = provisionalSupplierLiabilityService.getPSLById(provisionalServiceOrder.getProvisionalSupplierLiabilityID());
        provisionalServiceOrder.setCreditOrDebitNoteNumber(claimNo);
        provisionalSupplierLiability.setCreditOrDebitNoteNumber(claimNo);
        provisionalServiceOrderRepository.updatePSO(provisionalServiceOrder);
        provisionalSupplierLiabilityRepository.updatePSL(provisionalSupplierLiability);
        if (!StringUtils.isEmpty(provisionalServiceOrder.getFinalServiceOrderID())) {
            FinalServiceOrder finalServiceOrder = finalServiceOrderService.getFSOById(provisionalServiceOrder.getFinalServiceOrderID());
            finalServiceOrder.setCreditOrDebitNoteNumber(claimNo);
            finalServiceOrderRepository.updateFSO(finalServiceOrder);
            FinalSupplierLiability finalSupplierLiability = finalSupplierLiabilityService.getFSLById(finalServiceOrder.getFinalSupplierLiabilityID());
            finalSupplierLiability.setCreditOrDebitNoteNumber(claimNo);
            finalSupplierLiabilityRepository.updateFSL(finalSupplierLiability);
        }
    }

    @Override
    public Map generateCreditDebit(String provisionalServiceOrderId) throws OperationException {
        ProvisionalServiceOrder provisionalServiceOrder = provisionalServiceOrderService.getPSOById(provisionalServiceOrderId);
        if (provisionalServiceOrder == null) throw new OperationException(Constants.ER01);
        if (!StringUtils.isEmpty(provisionalServiceOrder.getCreditOrDebitNoteNumber()))
            throw new OperationException(Constants.ER531);
        if (provisionalServiceOrder.getTotalDiffAmount() == null) throw new OperationException(Constants.ER541);
        if (provisionalServiceOrder.getTotalDiffAmount().compareTo(BigDecimal.ZERO) == 0)
            throw new OperationException(Constants.ER541);
        if (provisionalServiceOrder.getSupplierBillPassing().getSupplierBillPassingStatus().equalsIgnoreCase(SupplierBillPassingStatus.PENDING_APPROVAL.getValue()))
            throw new OperationException(Constants.ER541);
        CreditDebitNote creditDebitNote = new CreditDebitNote();
        if (provisionalServiceOrder.getTotalDiffAmount().compareTo(BigDecimal.ZERO) > 0)
            creditDebitNote.setNoteType(NoteType.DEBIT);
        else
            creditDebitNote.setNoteType(NoteType.CREDIT);

        List<String> bookingRefNo = new ArrayList<>();
        bookingRefNo.add(provisionalServiceOrder.getBookingRefNo());
        creditDebitNote.setBookingRefNumber(bookingRefNo);
        creditDebitNote.setServiceOrderNumber(provisionalServiceOrderId);
        creditDebitNote.setClient_supplierID(provisionalServiceOrder.getSupplierId());
        creditDebitNote.setClient_supplierName(provisionalServiceOrder.getSupplierId());
        creditDebitNote.setConsumed(false);
        creditDebitNote.setInvoiceNumber(provisionalServiceOrder.getSupplierBillPassing().getInvoiceNumber());
        creditDebitNote.setVersionNumber(provisionalServiceOrder.getVersionNumber().intValue());
        creditDebitNote.setIssuedTo(IssuedTo.SUPPLIER);
        creditDebitNote.setNotePhase(NotePhase.Final);
        creditDebitNote.setProductCategory(provisionalServiceOrder.getProductCategoryId());
        creditDebitNote.setProductCategorySubType(provisionalServiceOrder.getProductCategorySubTypeId());
        creditDebitNote.setProductID(provisionalServiceOrder.getProductNameId());
        creditDebitNote.setProductName(provisionalServiceOrder.getProductNameId());
        creditDebitNote.setLatest(true);
        creditDebitNote.setCurrency(provisionalServiceOrder.getSupplierCurrency());
        creditDebitNote.setFundType(FundType.REFUNDABLE);
        creditDebitNote.setTaxType(TaxType.GST);
        creditDebitNote.setTotalAmount(provisionalServiceOrder.getTotalDiffAmount().doubleValue());
        creditDebitNote.setTransactionDate(System.currentTimeMillis());
        String claimNo = creditDebitNoteUtils.generate(creditDebitNote).getId();
        updateCreditDebitMemo(claimNo, provisionalServiceOrder);

        Map<String, String> entity = new HashMap<>();
        entity.put("message", "credit/debit note generated successfully");
        return entity;
    }

    @Override
    public List<String> getApprovalStatusList() {
        List<String> statusList = new ArrayList<>();
        Arrays.stream(ApprovalStatus.values()).forEach(approvalStatus -> statusList.add(approvalStatus.getValue()));
        return statusList;
    }

    @Override
    public List<String> getStatusList() {
        List<String> statusList = new ArrayList<>();
        Arrays.stream(SupplierBillPassingStatus.values()).forEach(supplierBillPassingStatus -> statusList.add(supplierBillPassingStatus.getValue()));
        return statusList;
    }

    @Override
    public Map generatePaymentAdvice(PaymentAdviceGeneration paymentAdviceGeneration) throws OperationException {
        SupplierPaymentResource supplierPaymentResource = new SupplierPaymentResource();
        supplierPaymentResource.setBalanceAmtPayableToSupplier(paymentAdviceGeneration.getBalanceAmtPayableToSupplier());
        supplierPaymentResource.setNetPayableToSupplier(paymentAdviceGeneration.getNetPayableToSupplier());
        supplierPaymentResource.setPrePaymentApplicable(true);
        //Set<FinalServiceOrder> finalServiceOrders=getFinalServiceOrders(paymentAdviceGeneration);
        //Set<PaymentAdviceOrderInfo> paymentAdviceOrderInfoSet=getPaymentAdviceOrderinfo(paymentAdviceGeneration.getAttachedServiceOrderSet());
        BigDecimal netPayableToSupplier = BigDecimal.ZERO;
        BigDecimal amountPaidToSupplier = BigDecimal.ZERO;
        BigDecimal balanceAmtPayable = BigDecimal.ZERO;
        BigDecimal amountTobepaid = BigDecimal.ZERO;
        String supplierCurrency = null;
        List<FinalServiceOrder> finalServiceOrders = new ArrayList();
        Set<PaymentAdviceOrderInfo> paymentAdviceOrderInfoSet = new HashSet<>();
        for (AttachedServiceOrder attachedServiceOrder : paymentAdviceGeneration.getAttachedServiceOrderSet()) {
            FinalServiceOrder finalServiceOrder = finalServiceOrderService.getFSOById(attachedServiceOrder.getServiceOrderId());
            if (finalServiceOrder == null) throw new OperationException("Please give valid FSO idS");
            if (attachedServiceOrder.getAmountToBePaid().compareTo(finalServiceOrder.getSupplierPricing().getTotalBalanceAmountPayable()) > 0)
                throw new OperationException(Constants.ER1026);
            if (!attachedServiceOrder.getSupplierId().equalsIgnoreCase(finalServiceOrder.getSupplierId()))
                throw new OperationException(Constants.ER1025);
            if (!attachedServiceOrder.getCurrency().equalsIgnoreCase(finalServiceOrder.getSupplierCurrency()))
                throw new OperationException(Constants.ER1031);
            supplierCurrency = attachedServiceOrder.getCurrency();
            PaymentAdviceOrderInfo paymentAdviceOrderInfo = new PaymentAdviceOrderInfo();
            paymentAdviceOrderInfo.setBookingRefId(finalServiceOrder.getBookingRefNo());
            paymentAdviceOrderInfo.setOrderId(finalServiceOrder.getOrderId());
            paymentAdviceOrderInfo.setOrderLevelNetPayableToSupplier(finalServiceOrder.getSupplierPricing().getNetPayableToSupplier());
            paymentAdviceOrderInfo.setOrderLevelBalanceAmtPayableToSupplier(finalServiceOrder.getSupplierPricing().getTotalBalanceAmountPayable());
            paymentAdviceOrderInfo.setServiceOrderId(finalServiceOrder.getUniqueId());
            paymentAdviceOrderInfo.setOrderLevelAmountPayableForSupplier(attachedServiceOrder.getAmountToBePaid());
            paymentAdviceOrderInfo.setServiceOrderValue(ServiceOrderValue.FSO);
            paymentAdviceOrderInfoSet.add(paymentAdviceOrderInfo);
            netPayableToSupplier = netPayableToSupplier.add(finalServiceOrder.getSupplierPricing().getNetPayableToSupplier());
            amountPaidToSupplier = amountPaidToSupplier.add(finalServiceOrder.getSupplierPricing().getAmountPaidToSupplier());
            balanceAmtPayable = balanceAmtPayable.add(finalServiceOrder.getSupplierPricing().getTotalBalanceAmountPayable());
            finalServiceOrders.add(finalServiceOrder);
        }

        if (paymentAdviceGeneration.getBalanceAmtPayableToSupplier().compareTo(balanceAmtPayable) != 0 || paymentAdviceGeneration.getNetPayableToSupplier().compareTo(netPayableToSupplier) != 0 || amountPaidToSupplier.compareTo(paymentAdviceGeneration.getAmountPaidToSupplier()) != 0)
            throw new OperationException(Constants.ER1027);

        if (amountTobepaid.compareTo(balanceAmtPayable) > 0)
            throw new OperationException(Constants.ER1032);

        supplierPaymentResource.setPaymentAdviceOrderInfoSet(paymentAdviceOrderInfoSet);
        supplierPaymentResource.setPaymentDueDate(paymentAdviceGeneration.getPaymentDueDate());
        supplierPaymentResource.setSupplierRefId(paymentAdviceGeneration.getSupplierRefId());
        supplierPaymentResource.setSupplierName(paymentAdviceGeneration.getSupplierRefId());
        supplierPaymentResource.setSupplierCurrency(supplierCurrency);

        PaymentAdviceResource paymentAdviceResource = new PaymentAdviceResource();
        paymentAdviceResource.setPaymentAdviceGenerationDueDate(paymentAdviceGeneration.getPaymentDueDate());
        paymentAdviceResource.setAmountPayableForSupplier(paymentAdviceGeneration.getAmountToBePaid());
        if (!StringUtils.isEmpty(paymentAdviceGeneration.getModeOfPayment()))
            paymentAdviceResource.setModeOfPayment(paymentAdviceGeneration.getModeOfPayment());
        paymentAdviceResource.setSelectedSupplierCurrency(supplierCurrency);
        // paymentAdviceResource.setSupplierCurrency(supplierCurrency);
        paymentAdviceResource.setPaymentAdviceStatusId(PaymentAdviceStatusValues.APPROVAL_PENDING);
        paymentAdviceResource.setPayToSupplier(true);
        supplierPaymentResource.setPaymentAdviceResource(paymentAdviceResource);

        PaymentAdvice paymentAdvice = null;
        try {
            paymentAdvice = paymentAdviceService.savePaymentAdvice(supplierPaymentResource);
        } catch (OperationException e) {
            logger.error("Error in save payment advice method");
            throw e;
        }catch(Exception e){
            logger.error("Error in savePaymentAdvice method");
            throw new OperationException("Error occured in payment advice module");
        }

        updateServiceOrders(paymentAdviceGeneration, paymentAdvice);

        for (FinalServiceOrder finalServiceOrder:finalServiceOrders){
            if (finalServiceOrder.getGeneralInvoice()!=null && finalServiceOrder.getGeneralInvoice())
                callFinanceAfterPaymentAdviceGenerated(finalServiceOrder);
        }

        Map<String, Object> entity = new HashMap<>();
        entity.put("message", "payment advice generated successfully");
        entity.put("paymentAdviceNumber", paymentAdvice.getPaymentAdviceNumber());
        entity.put("paymentAdviceId", paymentAdvice.getId());
        return entity;
    }

    @Override
    public void paymentAdviceAutoGeneration() {
        List<ProvisionalServiceOrder> provisionalServiceOrders = provisionalServiceOrderService.supplierBillPassingScheduler();
        for (ProvisionalServiceOrder provisionalServiceOrder : provisionalServiceOrders) {
            try {
                if (StringUtils.isEmpty(provisionalServiceOrder.getFinalServiceOrderID()))
                    autoGeneratePaymentAdvice(provisionalServiceOrder);
                else
                    autoGeneratePaymentAdvice(finalServiceOrderService.getFSOById(provisionalServiceOrder.getFinalServiceOrderID()));
            } catch (OperationException e) {
                logger.error("error in getting service order");
            }
        }
    }

    @Override
    public Set<String> getStopPaymentUntilValues() {
        Set<String> stopPaymentUntilValues = new HashSet<>();
        Arrays.asList(StopPaymentUntil.values()).stream().forEach(stopPaymentUntil -> stopPaymentUntilValues.add(stopPaymentUntil.getValue()));
        return stopPaymentUntilValues;
    }

    @Override
    public Set<ServiceOrderResource> getFinalServiceOrdersByDiscrepancyId(String id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", mdmToken.getToken());
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<Set> responseEntity = RestUtils.getTemplate().exchange(getFinanceFinalServieOrders + id, HttpMethod.GET, httpEntity, Set.class);
        Set<String> finalServiceOrderIds = responseEntity.getBody();

        Set<ServiceOrderResource> finalServiceOrders = new HashSet<>();
        try {
            for (String fsoId : finalServiceOrderIds) {
                ServiceOrderSearchCriteria serviceOrderSearchCriteria = new ServiceOrderSearchCriteria();
                serviceOrderSearchCriteria.setUniqueId(fsoId);
                serviceOrderSearchCriteria.setServiceOrderType(ServiceOrderAndSupplierLiabilityType.FSO);

                Map<String, Object> result = serviceOrderAndSupplierLiabilityService.getServiceOrdersAndSupplierLiabilities(serviceOrderSearchCriteria);
                List<ServiceOrderResource> serviceOrderResourceList = (List<ServiceOrderResource>) result.get("result");
                finalServiceOrders.add(serviceOrderResourceList.get(0));
            }
        } catch (Exception e) {
            logger.debug("error in getting final service orders from search criteria");
        }
        return finalServiceOrders;
    }

   /* private Set<FinalServiceOrder> getFinalServiceOrders(PaymentAdviceGeneration paymentAdviceGeneration) throws OperationException {
        if (paymentAdviceGeneration.getAttachedServiceOrderSet().size()==0) throw new OperationException("Please select atleast one FSO");
        Set<FinalServiceOrder> finalServiceOrders=new HashSet<>();
        for (AttachedServiceOrder attachedServiceOrder:paymentAdviceGeneration.getAttachedServiceOrderSet()){
            FinalServiceOrder finalServiceOrder=finalServiceOrderService.getFSOById(attachedServiceOrder.getServiceOrderId());
            if (finalServiceOrder==null) throw new OperationException("Please give valid FSO idS");
            finalServiceOrders.add(finalServiceOrder);
        }
        return finalServiceOrders;
    }*/

    private Set<PaymentAdviceOrderInfo> getPaymentAdviceOrderinfo(Set<AttachedServiceOrder> attachedServiceOrderSet) throws OperationException {
        Set<PaymentAdviceOrderInfo> paymentAdviceOrderInfoSet = new HashSet<>();
        for (AttachedServiceOrder attachedServiceOrder : attachedServiceOrderSet) {
            FinalServiceOrder finalServiceOrder = finalServiceOrderService.getFSOById(attachedServiceOrder.getServiceOrderId());
            if (finalServiceOrder == null) throw new OperationException("Please give valid FSO idS");
            PaymentAdviceOrderInfo paymentAdviceOrderInfo = new PaymentAdviceOrderInfo();
            paymentAdviceOrderInfo.setBookingRefId(finalServiceOrder.getBookingRefNo());
            paymentAdviceOrderInfo.setOrderId(finalServiceOrder.getOrderId());
            paymentAdviceOrderInfo.setOrderLevelNetPayableToSupplier(finalServiceOrder.getSupplierPricing().getNetPayableToSupplier());
            paymentAdviceOrderInfo.setOrderLevelBalanceAmtPayableToSupplier(finalServiceOrder.getSupplierPricing().getTotalBalanceAmountPayable());
            paymentAdviceOrderInfo.setServiceOrderId(finalServiceOrder.getUniqueId());
            paymentAdviceOrderInfo.setOrderLevelAmountPayableForSupplier(attachedServiceOrder.getAmountToBePaid());
            paymentAdviceOrderInfoSet.add(paymentAdviceOrderInfo);
        }
        return paymentAdviceOrderInfoSet;
    }

    private void autoGeneratePaymentAdvice(BaseServiceOrderDetails serviceOrder) {
        SupplierPaymentResource supplierPaymentResource = new SupplierPaymentResource();
        supplierPaymentResource.setBalanceAmtPayableToSupplier(serviceOrder.getSupplierPricing().getTotalBalanceAmountPayable());
        supplierPaymentResource.setNetPayableToSupplier(serviceOrder.getSupplierPricing().getNetPayableToSupplier());

        Set<PaymentAdviceOrderInfo> paymentAdviceOrderInfoSet = new HashSet<>();
        PaymentAdviceOrderInfo paymentAdviceOrderInfo = new PaymentAdviceOrderInfo();
        paymentAdviceOrderInfo.setOrderId(serviceOrder.getOrderId());
        paymentAdviceOrderInfo.setBookingRefId(serviceOrder.getBookingRefNo());
        paymentAdviceOrderInfo.setOrderLevelAmountPayableForSupplier(serviceOrder.getSupplierPricing().getTotalBalanceAmountPayable());
        paymentAdviceOrderInfo.setServiceOrderId(serviceOrder.getUniqueId());
        paymentAdviceOrderInfo.setOrderLevelNetPayableToSupplier(serviceOrder.getSupplierPricing().getNetPayableToSupplier());
        paymentAdviceOrderInfoSet.add(paymentAdviceOrderInfo);

        supplierPaymentResource.setPaymentAdviceOrderInfoSet(paymentAdviceOrderInfoSet);
        supplierPaymentResource.setSupplierRefId(serviceOrder.getSupplierId());
        supplierPaymentResource.setSupplierName(serviceOrder.getSupplierName());

        supplierPaymentResource.setSupplierCurrency(serviceOrder.getSupplierCurrency());

        PaymentAdviceResource paymentAdviceResource = new PaymentAdviceResource();
        paymentAdviceResource.setPaymentAdviceGenerationDueDate(ZonedDateTime.now());
        paymentAdviceResource.setAmountPayableForSupplier(serviceOrder.getSupplierPricing().getTotalBalanceAmountPayable());
        paymentAdviceResource.setSelectedSupplierCurrency(supplierPaymentResource.getSupplierCurrency());
        paymentAdviceResource.setPaymentAdviceStatusId(PaymentAdviceStatusValues.APPROVED);
        supplierPaymentResource.setPaymentAdviceResource(paymentAdviceResource);

        PaymentAdvice paymentAdvice = null;
        try {
            paymentAdvice = paymentAdviceService.savePaymentAdvice(supplierPaymentResource);
            FinalServiceOrder finalServiceOrder = null;
            Set<PaymentAdvice> paymentAdviceIds = null;
            for (PaymentAdviceOrderInfo attachedServiceOrder : paymentAdviceOrderInfoSet) {

                ProvisionalServiceOrder provisionalServiceOrder = provisionalServiceOrderService.getPSOById(serviceOrder.getProvisionalServiceOrderID());
                if (provisionalServiceOrder.getPaymentAdviceSet() != null)
                    paymentAdviceIds = provisionalServiceOrder.getPaymentAdviceSet();
                else paymentAdviceIds = new HashSet<>();
                paymentAdviceIds.add(paymentAdvice);
                provisionalServiceOrder.setPaymentAdviceSet(paymentAdviceIds);
                provisionalServiceOrderRepository.updatePSO(provisionalServiceOrder);

                if (!StringUtils.isEmpty(serviceOrder.getFinalServiceOrderID())) {
                    finalServiceOrder = finalServiceOrderService.getFSOById(serviceOrder.getFinalServiceOrderID());
                    if (finalServiceOrder.getPaymentAdviceSet() != null)
                        paymentAdviceIds = finalServiceOrder.getPaymentAdviceSet();
                    else paymentAdviceIds = new HashSet<>();
                    paymentAdviceIds.add(paymentAdvice);
                    finalServiceOrder.setPaymentAdviceSet(paymentAdviceIds);
                    finalServiceOrderRepository.updateFSO(finalServiceOrder);
                }

                ProvisionalSupplierLiability provisionalSupplierLiability = provisionalSupplierLiabilityService.getPSLById(serviceOrder.getProvisionalSupplierLiabilityID());
                if (provisionalSupplierLiability.getPaymentAdviceSet() != null)
                    paymentAdviceIds = provisionalSupplierLiability.getPaymentAdviceSet();
                else paymentAdviceIds = new HashSet<>();
                paymentAdviceIds.add(paymentAdvice);
                provisionalSupplierLiability.setPaymentAdviceSet(paymentAdviceIds);
                provisionalSupplierLiabilityRepository.updatePSL(provisionalSupplierLiability);

                if (!StringUtils.isEmpty(serviceOrder.getFinalSupplierLiabilityID())) {
                    FinalSupplierLiability finalSupplierLiability = finalSupplierLiabilityService.getFSLById(serviceOrder.getFinalSupplierLiabilityID());
                    if (finalSupplierLiability.getPaymentAdviceSet() != null)
                        paymentAdviceIds = finalSupplierLiability.getPaymentAdviceSet();
                    else paymentAdviceIds = new HashSet<>();
                    paymentAdviceIds.add(paymentAdvice);
                    finalSupplierLiability.setPaymentAdviceSet(paymentAdviceIds);
                    finalSupplierLiabilityRepository.updateFSL(finalSupplierLiability);
                    // String id=paymentAdvice.getId();
                    // updateServiceOrders(serviceOrder,id);
                }
            }
        } catch (Exception e) {
            logger.error("error in saving payment advice");
        }
        if (serviceOrder.getGeneralInvoice()!=null && serviceOrder.getGeneralInvoice())
            callFinanceAfterPaymentAdviceGenerated(serviceOrder);

    }

    private void updateServiceOrders(PaymentAdviceGeneration paymentAdviceGeneration, PaymentAdvice paymentAdvice) {
        try {
            FinalServiceOrder finalServiceOrder = null;
            Set<PaymentAdvice> paymentAdviceIds = null;
            for (AttachedServiceOrder attachedServiceOrder : paymentAdviceGeneration.getAttachedServiceOrderSet()) {

                finalServiceOrder = finalServiceOrderService.getFSOById(attachedServiceOrder.getServiceOrderId());
                if (finalServiceOrder.getPaymentAdviceSet() != null)
                    paymentAdviceIds = finalServiceOrder.getPaymentAdviceSet();
                else paymentAdviceIds = new HashSet<>();
                paymentAdviceIds.add(paymentAdvice);
                finalServiceOrder.setPaymentAdviceSet(paymentAdviceIds);
                finalServiceOrderRepository.updateFSO(finalServiceOrder);

                ProvisionalServiceOrder provisionalServiceOrder = provisionalServiceOrderService.getPSOById(finalServiceOrder.getProvisionalServiceOrderID());
                if (provisionalServiceOrder.getPaymentAdviceSet() != null)
                    paymentAdviceIds = provisionalServiceOrder.getPaymentAdviceSet();
                else paymentAdviceIds = new HashSet<>();
                paymentAdviceIds.add(paymentAdvice);
                provisionalServiceOrder.setPaymentAdviceSet(paymentAdviceIds);
                provisionalServiceOrderRepository.updatePSO(provisionalServiceOrder);

                ProvisionalSupplierLiability provisionalSupplierLiability = provisionalSupplierLiabilityService.getPSLById(finalServiceOrder.getProvisionalSupplierLiabilityID());
                if (provisionalSupplierLiability.getPaymentAdviceSet() != null)
                    paymentAdviceIds = provisionalSupplierLiability.getPaymentAdviceSet();
                else paymentAdviceIds = new HashSet<>();
                paymentAdviceIds.add(paymentAdvice);
                provisionalSupplierLiability.setPaymentAdviceSet(paymentAdviceIds);
                provisionalSupplierLiabilityRepository.updatePSL(provisionalSupplierLiability);

                FinalSupplierLiability finalSupplierLiability = finalSupplierLiabilityService.getFSLById(finalServiceOrder.getFinalSupplierLiabilityID());
                if (finalSupplierLiability.getPaymentAdviceSet() != null)
                    paymentAdviceIds = finalSupplierLiability.getPaymentAdviceSet();
                else paymentAdviceIds = new HashSet<>();
                paymentAdviceIds.add(paymentAdvice);
                finalSupplierLiability.setPaymentAdviceSet(paymentAdviceIds);
                finalSupplierLiabilityRepository.updateFSL(finalSupplierLiability);

            }
        } catch (Exception e) {
            logger.error("error occured in updating service orders");
        }
    }

    private void callFinanceAfterPaymentAdviceGenerated(BaseServiceOrderDetails serviceOrder) {
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Authorization", mdmToken.getToken());
        HttpEntity httpEntity=new HttpEntity(httpHeaders);
        RestTemplate restTemplate= RestUtils.getTemplate();
        String url=financePaymentAdviceUrl+serviceOrder.getInvoiceId()+"/status/"+serviceOrder.getSupplierSettlementStatus();
        try {
            restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Object.class);
        }catch (Exception e){
            logger.error("Error in calling finance api");
        }
    }


    @Override
    public String getUserRole(String token) throws UnsupportedEncodingException, OperationException {
        return null;
    }

    @Override
    public Map getCommonBillPassingResource(ServiceOrderSearchCriteria serviceOrderSearchCriteria) throws OperationException {
        serviceOrderSearchCriteria.setBillPassingResource(true);
        Map<String, Object> result = new HashMap<>();
        try {
            result = serviceOrderAndSupplierLiabilityService.getServiceOrdersAndSupplierLiabilities(serviceOrderSearchCriteria);
        } catch (OperationException e) {
            logger.debug("no records found from service order search criteria");
            List<ServiceOrderResource> serviceOrderResourceList = new ArrayList<>();
            result.put("result", serviceOrderResourceList);
            result.put("noOfPages", 1);
        } catch (Exception e) {
            logger.debug("Error in getting details from service order search criteria");
            throw new OperationException(Constants.ER1030);
        }
        try {
            Set<AttachedServiceOrder> attachedServiceOrderList = new HashSet<>();
            SupplierBillPassingResource supplierBillPassingResource = new SupplierBillPassingResource();
            BigDecimal equivalentServiceOrderAmount = BigDecimal.ZERO, supplierCost = BigDecimal.ZERO, supplierGst = BigDecimal.ZERO, supplierCommercials = BigDecimal.ZERO;
            ZonedDateTime paymentDueDate = null;

            for (String id : serviceOrderSearchCriteria.getAttachedServiceOrderIds()) {
                ProvisionalServiceOrder provisionalServiceOrder = provisionalServiceOrderService.getPSOById(id);
                if (provisionalServiceOrder == null) throw new OperationException(Constants.ER01);
                AttachedServiceOrder attachedServiceOrder = new AttachedServiceOrder();
                attachedServiceOrder.setSupplierId(provisionalServiceOrder.getSupplierId());
                attachedServiceOrder.setSupplierName(provisionalServiceOrder.getSupplierName());
                attachedServiceOrder.setProductName(provisionalServiceOrder.getProductNameId());
                attachedServiceOrder.setServiceOrderValue(provisionalServiceOrder.getSupplierPricing().getSupplierCost());
                attachedServiceOrder.setGst(provisionalServiceOrder.getSupplierPricing().getSupplierGst());
                attachedServiceOrder.setTotalCost(provisionalServiceOrder.getSupplierPricing().getNetPayableToSupplier());
                attachedServiceOrder.setServiceOrderType(provisionalServiceOrder.getType().getValue());
                attachedServiceOrder.setServiceOrderId(provisionalServiceOrder.getUniqueId());
                attachedServiceOrder.setNetPayableToSupplier(attachedServiceOrder.getTotalCost());
                attachedServiceOrder.setCurrency(provisionalServiceOrder.getSupplierCurrency());
                attachedServiceOrderList.add(attachedServiceOrder);
                equivalentServiceOrderAmount = equivalentServiceOrderAmount.add(attachedServiceOrder.getTotalCost());
                supplierCost = supplierCost.add(provisionalServiceOrder.getSupplierPricing().getSupplierCost());
                supplierGst = supplierGst.add(provisionalServiceOrder.getSupplierPricing().getSupplierGst());
                supplierCommercials = supplierCommercials.add(provisionalServiceOrder.getSupplierPricing().getSupplierCommercials());
                if (provisionalServiceOrder.getPaymentDueDate() != null) {
                    if (paymentDueDate == null) paymentDueDate = provisionalServiceOrder.getPaymentDueDate();
                    else if (paymentDueDate.compareTo(provisionalServiceOrder.getPaymentDueDate()) < 0)
                        paymentDueDate = provisionalServiceOrder.getPaymentDueDate();
                }
            }

            Set<String> productName = new HashSet<>();
            attachedServiceOrderList.stream().forEach(attachedServiceOrder -> productName.add(attachedServiceOrder.getProductName()));
            supplierBillPassingResource.setProductName(productName);

            if(attachedServiceOrderList!=null && attachedServiceOrderList.size()!=0) {
                supplierBillPassingResource.setAttachedServiceOrders(attachedServiceOrderList);
                supplierBillPassingResource.setSupplierInvoiceCurrency(attachedServiceOrderList.stream().iterator().next().getCurrency());
                supplierBillPassingResource.setSupplierId(attachedServiceOrderList.stream().iterator().next().getSupplierId());
                supplierBillPassingResource.setSupplierName(attachedServiceOrderList.stream().iterator().next().getSupplierName());
            }
            supplierBillPassingResource.setEquivalentServiceOrderAmount(equivalentServiceOrderAmount);
            supplierBillPassingResource.setPaymentDueDate(paymentDueDate);
            supplierBillPassingResource.setNetPayableToSupplier(equivalentServiceOrderAmount);
            supplierBillPassingResource.setSupplierCost(supplierCost);
            supplierBillPassingResource.setSupplierGst(supplierGst);
            supplierBillPassingResource.setSupplierInvoiceTotalCost(supplierCost.add(supplierGst));
            supplierBillPassingResource.setSupplierInvoiceTotalCommission(supplierCommercials);
            supplierBillPassingResource.setManualEntry(true);
            if (supplierBillPassingResource.getPaymentDueDate() == null)
                supplierBillPassingResource.setPaymentDueDate(ZonedDateTime.now().plusDays(5));
            result.put("supplierBillPassingResource", supplierBillPassingResource);
            return result;
        } catch (Exception e) {
            logger.debug("Error creating supplier bill passing resource method");
            throw new OperationException(Constants.ER1030);
        }
    }

    @Override
    public Map getPaymentAdviceResource(ServiceOrderSearchCriteria serviceOrderSearchCriteria) throws OperationException {
        serviceOrderSearchCriteria.setPaymentAdviceResource(true);
        Map<String, Object> result = new HashMap<>();
        try {
            result = serviceOrderAndSupplierLiabilityService.getServiceOrdersAndSupplierLiabilities(serviceOrderSearchCriteria);
        } catch (OperationException e) {
            logger.debug("no records found from service order search criteria");
            List<ServiceOrderResource> serviceOrderResourceList = new ArrayList<>();
            result.put("result", serviceOrderResourceList);
            result.put("noOfPages", 1);
        } catch (Exception e) {
            logger.debug("Error in getting details from service order search criteria");
            throw new OperationException(Constants.ER1030);
        }

        try {
            Set<AttachedServiceOrder> attachedServiceOrderList = new HashSet<>();
            PaymentAdviceGeneration paymentAdviceResource = new PaymentAdviceGeneration();
            BigDecimal netPayableToSupplier = BigDecimal.ZERO, amountPaid = BigDecimal.ZERO, balanceAmountPayable = BigDecimal.ZERO, amountToBePaid = BigDecimal.ZERO;
            ZonedDateTime paymentDueDate = null;

            for (String id : serviceOrderSearchCriteria.getAttachedServiceOrderIds()) {
                FinalServiceOrder finalServiceOrder = finalServiceOrderService.getFSOById(id);
                if (finalServiceOrder == null) throw new OperationException(Constants.ER01);
                AttachedServiceOrder attachedServiceOrder = new AttachedServiceOrder();
                attachedServiceOrder.setSupplierId(finalServiceOrder.getSupplierId());
                attachedServiceOrder.setSupplierName(finalServiceOrder.getSupplierName());
                attachedServiceOrder.setProductName(finalServiceOrder.getProductNameId());
                attachedServiceOrder.setServiceOrderType(finalServiceOrder.getType().getValue());
                attachedServiceOrder.setServiceOrderId(finalServiceOrder.getUniqueId());
                attachedServiceOrder.setAmountToBePaid(finalServiceOrder.getSupplierPricing().getTotalBalanceAmountPayable());
                attachedServiceOrder.setCurrency(finalServiceOrder.getSupplierCurrency());
                attachedServiceOrderList.add(attachedServiceOrder);
                netPayableToSupplier = netPayableToSupplier.add(finalServiceOrder.getSupplierPricing().getNetPayableToSupplier());
                amountPaid = amountPaid.add(finalServiceOrder.getSupplierPricing().getAmountPaidToSupplier());
                balanceAmountPayable = balanceAmountPayable.add(finalServiceOrder.getSupplierPricing().getTotalBalanceAmountPayable());
                amountToBePaid = amountToBePaid.add(attachedServiceOrder.getAmountToBePaid());
                if (finalServiceOrder.getPaymentDueDate() != null) {
                    if (paymentDueDate == null) paymentDueDate = finalServiceOrder.getPaymentDueDate();
                    else if (paymentDueDate.compareTo(finalServiceOrder.getPaymentDueDate()) < 0)
                        paymentDueDate = finalServiceOrder.getPaymentDueDate();
                }
            }
            paymentAdviceResource.setAttachedServiceOrderSet(attachedServiceOrderList);
            paymentAdviceResource.setNetPayableToSupplier(netPayableToSupplier);
            paymentAdviceResource.setAmountToBePaid(amountToBePaid);
            paymentAdviceResource.setAmountPaidToSupplier(amountPaid);
            paymentAdviceResource.setBalanceAmtPayableToSupplier(balanceAmountPayable);
            paymentAdviceResource.setPaymentDueDate(paymentDueDate);
            paymentAdviceResource.setSupplierRefId(attachedServiceOrderList.stream().iterator().next().getSupplierId());
            paymentAdviceResource.setSupplierName(attachedServiceOrderList.stream().iterator().next().getSupplierName());
            paymentAdviceResource.setCurrency(attachedServiceOrderList.stream().iterator().next().getCurrency());
            if (paymentAdviceResource.getPaymentDueDate() == null)
                paymentAdviceResource.setPaymentDueDate(ZonedDateTime.now().plusDays(5));
            result.put("paymentAdviceResource", paymentAdviceResource);
            return result;
        } catch (Exception e) {
            logger.debug("Error in get paymentAdviceResource method");
            throw new OperationException(Constants.ER1030);
        }
    }

    @Override
    public SupplierBillPassingResource getSingleEntryResource(String provisionalServiceOrderId) throws OperationException {
        try {
            SupplierBillPassingResource supplierBillPassingResource = new SupplierBillPassingResource();
            ProvisionalServiceOrder provisionalServiceOrder = provisionalServiceOrderService.getPSOById(provisionalServiceOrderId);
            if (provisionalServiceOrder == null) throw new OperationException(Constants.ER01);
            supplierBillPassingResource.setEquivalentServiceOrderAmount(provisionalServiceOrder.getSupplierPricing().getNetPayableToSupplier());
            supplierBillPassingResource.setPaymentDueDate(provisionalServiceOrder.getPaymentDueDate());
            supplierBillPassingResource.setSupplierInvoiceTotalCost(provisionalServiceOrder.getSupplierPricing().getSupplierCost().add(provisionalServiceOrder.getSupplierPricing().getSupplierGst()));
            supplierBillPassingResource.setNetPayableToSupplier(provisionalServiceOrder.getSupplierPricing().getNetPayableToSupplier());
            supplierBillPassingResource.setSupplierInvoiceCurrency(provisionalServiceOrder.getSupplierCurrency());
            supplierBillPassingResource.setSupplierCost(provisionalServiceOrder.getSupplierPricing().getSupplierCost());
            supplierBillPassingResource.setSupplierGst(provisionalServiceOrder.getSupplierPricing().getSupplierGst());
            supplierBillPassingResource.setSupplierInvoiceTotalCommission(provisionalServiceOrder.getSupplierPricing().getSupplierCommercials());
            supplierBillPassingResource.setSupplierName(provisionalServiceOrder.getSupplierName());
            supplierBillPassingResource.setSupplierId(provisionalServiceOrder.getSupplierId());

            Set<AttachedServiceOrder> attachedServiceOrderList = new HashSet<>();

            AttachedServiceOrder attachedServiceOrder = new AttachedServiceOrder();
            attachedServiceOrder.setSupplierId(provisionalServiceOrder.getSupplierId());
            attachedServiceOrder.setSupplierName(provisionalServiceOrder.getSupplierName());
            attachedServiceOrder.setProductName(provisionalServiceOrder.getProductNameId());
            attachedServiceOrder.setServiceOrderValue(provisionalServiceOrder.getSupplierPricing().getSupplierCost());
            attachedServiceOrder.setGst(provisionalServiceOrder.getSupplierPricing().getSupplierGst());
            attachedServiceOrder.setTotalCost(provisionalServiceOrder.getSupplierPricing().getNetPayableToSupplier());
            attachedServiceOrder.setServiceOrderType(provisionalServiceOrder.getType().getValue());
            attachedServiceOrder.setServiceOrderId(provisionalServiceOrder.getUniqueId());
            attachedServiceOrder.setNetPayableToSupplier(attachedServiceOrder.getTotalCost());
            attachedServiceOrder.setCurrency(provisionalServiceOrder.getSupplierCurrency());
            attachedServiceOrderList.add(attachedServiceOrder);

            Set<String> productName = new HashSet<>();
            productName.add(attachedServiceOrder.getProductName());

            supplierBillPassingResource.setManualEntry(true);
            supplierBillPassingResource.setProductName(productName);
            supplierBillPassingResource.setAttachedServiceOrders(attachedServiceOrderList);
            if (supplierBillPassingResource.getPaymentDueDate() == null)
                supplierBillPassingResource.setPaymentDueDate(ZonedDateTime.now().plusDays(5));
            return supplierBillPassingResource;
        } catch (Exception e) {
            logger.debug("Error creating supplier bill passing resource method");
            throw new OperationException(Constants.ER1030);
        }
    }

    @Override
    public PaymentAdviceGeneration getPaymentAdviceById(String paymentAdviceNumber) throws OperationException {
        PaymentAdvice paymentAdvice = null;
        try {
            PaymentCriteria paymentCriteria = new PaymentCriteria();
            paymentCriteria.setPaymentAdviceNumber(paymentAdviceNumber);
            List<PaymentAdvice> paymentAdvices = paymentAdviceService.searchSupplierPayment(paymentCriteria);
            if (paymentAdvices == null) {
                throw new OperationException("Failed to get payment Advice with payment advice number " + paymentAdviceNumber);
            }
            paymentAdvice = paymentAdvices.get(0);
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
        PaymentAdviceGeneration paymentAdviceGeneration = new PaymentAdviceGeneration();
        paymentAdviceGeneration.setCurrency(paymentAdvice.getSelectedSupplierCurrency());
        paymentAdviceGeneration.setSupplierRefId(paymentAdvice.getSupplierRefId());
        paymentAdviceGeneration.setSupplierName(paymentAdvice.getSupplierName());
        paymentAdviceGeneration.setAmountToBePaid(paymentAdvice.getAmountPayableForSupplier());
        paymentAdviceGeneration.setNetPayableToSupplier(paymentAdvice.getNetPayableToSupplier());
        paymentAdviceGeneration.setBalanceAmtPayableToSupplier(paymentAdvice.getBalanceAmtPayableToSupplier());
        paymentAdviceGeneration.setAmountPaidToSupplier(paymentAdvice.getNetPayableToSupplier().subtract(paymentAdvice.getBalanceAmtPayableToSupplier()));
        paymentAdviceGeneration.setModeOfPayment(paymentAdvice.getModeOfPayment());
        paymentAdviceGeneration.setPaymentAdviceNumber(paymentAdvice.getPaymentAdviceNumber());
        paymentAdviceGeneration.setPaymentDueDate(paymentAdvice.getPaymentDueDate());
        Set<AttachedServiceOrder> attachedServiceOrders = new HashSet<>();
        for (PaymentAdviceOrderInfo paymentAdviceOrderInfo : paymentAdvice.getPaymentAdviceOrderInfoSet()) {
            AttachedServiceOrder attachedServiceOrder = new AttachedServiceOrder();
            attachedServiceOrder.setSupplierName(paymentAdvice.getSupplierName());
            attachedServiceOrder.setSupplierId(paymentAdvice.getSupplierRefId());
            attachedServiceOrder.setCurrency(paymentAdvice.getSelectedSupplierCurrency());
            attachedServiceOrder.setBookingRefNo(paymentAdviceOrderInfo.getBookingRefId());
            attachedServiceOrder.setServiceOrderId(paymentAdviceOrderInfo.getServiceOrderId());
            attachedServiceOrder.setNetPayableToSupplier(paymentAdviceOrderInfo.getOrderLevelNetPayableToSupplier());
            attachedServiceOrder.setAmountToBePaid(paymentAdviceOrderInfo.getOrderLevelAmountPayableForSupplier());
            attachedServiceOrder.setServiceOrderType(ServiceOrderAndSupplierLiabilityType.FSO.getValue());
            attachedServiceOrders.add(attachedServiceOrder);
        }
        paymentAdviceGeneration.setAttachedServiceOrderSet(attachedServiceOrders);
        paymentAdviceGeneration.setPaymentAdviceStatus(paymentAdvice.getPaymentAdviceStatus());

        return paymentAdviceGeneration;
    }

    @Override
    public Map getAttachedServiceOrders(List<String> attachedServiceOrderIds, Integer page, Integer size, String sortCriteria, boolean descending) throws OperationException {
        try {
            List<AttachedServiceOrder> attachedServiceOrderList = new ArrayList<>();
            ZonedDateTime paymentDueDate = null;

            Map<String, Object> provisionalServiceOrderMap = provisionalServiceOrderRepository.getPSOByIds(attachedServiceOrderIds, page, size, sortCriteria, descending);
            List<ProvisionalServiceOrder> provisionalServiceOrderList = (List<ProvisionalServiceOrder>) provisionalServiceOrderMap.get("result");

            for (ProvisionalServiceOrder provisionalServiceOrder : provisionalServiceOrderList) {
                AttachedServiceOrder attachedServiceOrder = new AttachedServiceOrder();
                attachedServiceOrder.setSupplierId(provisionalServiceOrder.getSupplierId());
                attachedServiceOrder.setSupplierName(provisionalServiceOrder.getSupplierName());
                attachedServiceOrder.setProductName(provisionalServiceOrder.getProductNameId());
                attachedServiceOrder.setServiceOrderValue(provisionalServiceOrder.getSupplierPricing().getSupplierCost());
                attachedServiceOrder.setGst(provisionalServiceOrder.getSupplierPricing().getSupplierGst());
                attachedServiceOrder.setTotalCost(provisionalServiceOrder.getSupplierPricing().getNetPayableToSupplier());
                attachedServiceOrder.setServiceOrderType(provisionalServiceOrder.getType().getValue());
                attachedServiceOrder.setServiceOrderId(provisionalServiceOrder.getUniqueId());
                attachedServiceOrder.setNetPayableToSupplier(attachedServiceOrder.getTotalCost());
                attachedServiceOrder.setCurrency(provisionalServiceOrder.getSupplierCurrency());
                attachedServiceOrder.setBookingRefNo(provisionalServiceOrder.getBookingRefNo());
                attachedServiceOrderList.add(attachedServiceOrder);
                if (provisionalServiceOrder.getPaymentDueDate() != null) {
                    if (paymentDueDate == null) paymentDueDate = provisionalServiceOrder.getPaymentDueDate();
                    else if (paymentDueDate.compareTo(provisionalServiceOrder.getPaymentDueDate()) < 0)
                        paymentDueDate = provisionalServiceOrder.getPaymentDueDate();
                }
            }
            Map<String, Object> entity = new HashMap<>();
            entity.put("result", attachedServiceOrderList);
            entity.put("noOfPages", provisionalServiceOrderMap.get("noOfPages"));
            entity.put("page", provisionalServiceOrderMap.get("page"));
            entity.put("size", provisionalServiceOrderMap.get("size"));
            return entity;
        } catch (Exception e) {
            logger.debug("Error creating supplier bill passing resource method");
            throw new OperationException(Constants.ER1030);
        }
    }

    @Override
    public Map<String, Object> getPaymentAdviceAttachedServiceOrders(List<String> attachedServiceOrderIds, Integer page, Integer size, String sortCriteria, boolean descending) throws OperationException {

        try {
            List<AttachedServiceOrder> attachedServiceOrderList = new ArrayList<>();
            ZonedDateTime paymentDueDate = null;

            Map<String, Object> finalServiceOrderMap = finalServiceOrderRepository.getFSOByIds(attachedServiceOrderIds, page, size, sortCriteria, descending);
            List<FinalServiceOrder> finalServiceOrderList = (List<FinalServiceOrder>) finalServiceOrderMap.get("result");

            BigDecimal netPayableToSupplier = BigDecimal.ZERO, amountPaid = BigDecimal.ZERO,
                    balanceAmountPayable = BigDecimal.ZERO, amountToBePaid = BigDecimal.ZERO;

            for (FinalServiceOrder finalServiceOrder : finalServiceOrderList) {
                if (finalServiceOrder == null) throw new OperationException(Constants.ER01);
                AttachedServiceOrder attachedServiceOrder = new AttachedServiceOrder();
                attachedServiceOrder.setSupplierId(finalServiceOrder.getSupplierId());
                attachedServiceOrder.setSupplierName(finalServiceOrder.getSupplierName());
                attachedServiceOrder.setProductName(finalServiceOrder.getProductNameId());
                attachedServiceOrder.setServiceOrderType(finalServiceOrder.getType().getValue());
                attachedServiceOrder.setServiceOrderId(finalServiceOrder.getUniqueId());
                attachedServiceOrder.setAmountToBePaid(finalServiceOrder.getSupplierPricing().getTotalBalanceAmountPayable());
                attachedServiceOrder.setCurrency(finalServiceOrder.getSupplierCurrency());
                attachedServiceOrderList.add(attachedServiceOrder);
                netPayableToSupplier = netPayableToSupplier.add(finalServiceOrder.getSupplierPricing().getNetPayableToSupplier());
                amountPaid = amountPaid.add(finalServiceOrder.getSupplierPricing().getAmountPaidToSupplier());
                balanceAmountPayable = balanceAmountPayable.add(finalServiceOrder.getSupplierPricing().getTotalBalanceAmountPayable());
                amountToBePaid = amountToBePaid.add(attachedServiceOrder.getAmountToBePaid());
                if (finalServiceOrder.getPaymentDueDate() != null) {
                    if (paymentDueDate == null) paymentDueDate = finalServiceOrder.getPaymentDueDate();
                    else if (paymentDueDate.compareTo(finalServiceOrder.getPaymentDueDate()) < 0)
                        paymentDueDate = finalServiceOrder.getPaymentDueDate();
                }
            }
            Map<String, Object> entity = new HashMap<>();
            entity.put("result", attachedServiceOrderList);
            entity.put("noOfPages", finalServiceOrderMap.get("noOfPages"));
            entity.put("page", finalServiceOrderMap.get("page"));
            entity.put("size", finalServiceOrderMap.get("size"));
            return entity;
        } catch (Exception e) {
            logger.debug("Error creating payment Advice resource method");
            throw new OperationException(Constants.ER1030);
        }
    }

    @Override
    public Map<String, Object> getPaymentAdviceResourceByDiscrepancyId(String id) {

        Map<String, Object> result = new HashMap<>();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", mdmToken.getToken());
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<String> responseEntity = RestUtils.getTemplate().exchange(getFinanceFinalServieOrders + id, HttpMethod.GET, httpEntity, String.class);

        String response = responseEntity.getBody();
        JSONObject res = new JSONObject(new JSONTokener(response));
        JSONArray finalServiceOrderIds = res.getJSONArray("serviceOrderList");

        Set<AttachedServiceOrder> attachedServiceOrderList = new HashSet<>();
        PaymentAdviceGeneration paymentAdviceResource = new PaymentAdviceGeneration();
        BigDecimal netPayableToSupplier = BigDecimal.ZERO, amountPaid = BigDecimal.ZERO, balanceAmountPayable = BigDecimal.ZERO, amountToBePaid = BigDecimal.ZERO;
        ZonedDateTime paymentDueDate = null;
        try {
            for (int i=0;i<finalServiceOrderIds.length();i++) {

                String fsoId = finalServiceOrderIds.getString(i);
                FinalServiceOrder finalServiceOrder = finalServiceOrderService.getFSOById(fsoId);
                if (finalServiceOrder == null) throw new OperationException(Constants.ER01);
                AttachedServiceOrder attachedServiceOrder = new AttachedServiceOrder();
                attachedServiceOrder.setSupplierId(finalServiceOrder.getSupplierId());
                attachedServiceOrder.setSupplierName(finalServiceOrder.getSupplierName());
                attachedServiceOrder.setProductName(finalServiceOrder.getProductNameId());
                attachedServiceOrder.setServiceOrderType(finalServiceOrder.getType().getValue());
                attachedServiceOrder.setServiceOrderId(finalServiceOrder.getUniqueId());
                attachedServiceOrder.setAmountToBePaid(finalServiceOrder.getSupplierPricing().getTotalBalanceAmountPayable());
                attachedServiceOrder.setCurrency(finalServiceOrder.getSupplierCurrency());
                attachedServiceOrderList.add(attachedServiceOrder);
                netPayableToSupplier = netPayableToSupplier.add(finalServiceOrder.getSupplierPricing().getNetPayableToSupplier());
                amountPaid = amountPaid.add(finalServiceOrder.getSupplierPricing().getAmountPaidToSupplier());
                balanceAmountPayable = balanceAmountPayable.add(finalServiceOrder.getSupplierPricing().getTotalBalanceAmountPayable());
                amountToBePaid = amountToBePaid.add(attachedServiceOrder.getAmountToBePaid());
                if (finalServiceOrder.getPaymentDueDate() != null) {
                    if (paymentDueDate == null) paymentDueDate = finalServiceOrder.getPaymentDueDate();
                    else if (paymentDueDate.compareTo(finalServiceOrder.getPaymentDueDate()) < 0)
                        paymentDueDate = finalServiceOrder.getPaymentDueDate();
                }
            }
            paymentAdviceResource.setAttachedServiceOrderSet(attachedServiceOrderList);
            paymentAdviceResource.setNetPayableToSupplier(netPayableToSupplier);
            paymentAdviceResource.setAmountToBePaid(amountToBePaid);
            paymentAdviceResource.setAmountPaidToSupplier(amountPaid);
            paymentAdviceResource.setBalanceAmtPayableToSupplier(balanceAmountPayable);
            paymentAdviceResource.setPaymentDueDate(paymentDueDate);
            paymentAdviceResource.setSupplierRefId(attachedServiceOrderList.stream().iterator().next().getSupplierId());
            paymentAdviceResource.setSupplierName(attachedServiceOrderList.stream().iterator().next().getSupplierName());
            paymentAdviceResource.setCurrency(attachedServiceOrderList.stream().iterator().next().getCurrency());
            if (paymentAdviceResource.getPaymentDueDate() == null)
                paymentAdviceResource.setPaymentDueDate(ZonedDateTime.now().plusDays(5));
            result.put("paymentAdviceResource", paymentAdviceResource);
            return result;
        } catch (Exception e) {
            logger.debug("Error in getting payment Advice Resource for discrepancy Id" + id);
        }
        return result;
    }
}
