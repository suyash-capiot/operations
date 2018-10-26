package com.coxandkings.travel.operations.service.supplierbillpassing.impl;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementFor;
import com.coxandkings.travel.operations.enums.commercialStatements.CommercialType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.SupplierCommercialStatement;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.ProvisionalServiceOrder;
import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierInvoiceOCR;
import com.coxandkings.travel.operations.repository.supplierbillpassing.SupplierInvoiceOCRRepo;
import com.coxandkings.travel.operations.resource.commercialStatement.AttachedCommercialStatement;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementSearchCriteria;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementsBillPassingResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.resource.supplierbillpassing.AttachedServiceOrder;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierBillPassingResource;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierInvoiceId;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierInvoiceSearchCriteria;
import com.coxandkings.travel.operations.service.commercialstatements.SupplierCommercialStatementService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ProvisionalServiceOrderService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityService;
import com.coxandkings.travel.operations.service.supplierbillpassing.SupplierInvoiceOCRService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.supplierBillPassing.DateConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class SupplierInvoiceOCRServiceImpl implements SupplierInvoiceOCRService {

    @Autowired
    private SupplierInvoiceOCRRepo supplierInvoiceOCRRepo;

    @Autowired
    private SupplierCommercialStatementService supplierCommercialStatementService;

    @Value("${supplier-invoice-xpath.basePath}")
    private String basePath;

    @Autowired
    private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;

    @Autowired
    private ProvisionalServiceOrderService provisionalServiceOrderService;

    private Logger logger = Logger.getLogger(SupplierInvoiceOCRServiceImpl.class);

    @Override
    public void save(SupplierInvoiceOCR supplierInvoiceOCR) {
        supplierInvoiceOCR.setUsed(false);

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream((supplierInvoiceOCR.getData()).getBytes()));

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            Element invoiceNumberAttribute = (Element) xpath.compile(basePath + "'invoiceNumber']").evaluate(doc, XPathConstants.NODE);
            supplierInvoiceOCR.setInvoiceNumber(invoiceNumberAttribute.getAttribute("ElementVal"));

            supplierInvoiceOCR.setData(supplierInvoiceOCR.getData());
            supplierInvoiceOCR.setCreationDate(ZonedDateTime.now());
            supplierInvoiceOCRRepo.add(supplierInvoiceOCR);
        } catch (Exception e) {
            logger.error("error in getting invoice number from OCR-CNK");
            supplierInvoiceOCR.setCreationDate(ZonedDateTime.now());
            supplierInvoiceOCRRepo.add(supplierInvoiceOCR);
        }
    }

    @Override
    public Set<SupplierInvoiceId> getAvailableInvoice(SupplierInvoiceSearchCriteria supplierInvoiceSearchCriteria) throws OperationException {
        List<SupplierInvoiceOCR> supplierInvoiceOCRList=supplierInvoiceOCRRepo.getAvailableInvoice(supplierInvoiceSearchCriteria);
        Set<SupplierInvoiceId> supplierInvoices =new HashSet<>();
        SupplierInvoiceId supplierInvoiceId=null;

       for (SupplierInvoiceOCR supplierInvoiceOCR:supplierInvoiceOCRList){
            supplierInvoiceId=new SupplierInvoiceId();
            supplierInvoiceId.setId(supplierInvoiceOCR.getId());
            supplierInvoiceId.setInvoiceNumber(supplierInvoiceOCR.getInvoiceNumber());
            supplierInvoices.add(supplierInvoiceId);
        }
        return supplierInvoices;
    }

    @Override
    public SupplierInvoiceOCR update(SupplierInvoiceOCR supplierInvoiceOCR) {
        return supplierInvoiceOCRRepo.update(supplierInvoiceOCR);
    }

    @Override
    public Map getSupplierBillPassingResource(String id, ServiceOrderSearchCriteria serviceOrderSearchCriteria) throws OperationException {

        Map<String, Object> result=new HashMap<>();

        if (serviceOrderSearchCriteria.getAttachedServiceOrderIds().size()>1) {
            try {
                serviceOrderSearchCriteria.setBillPassingResource(true);
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
        }
        SupplierBillPassingResource supplierBillPassingResource= new SupplierBillPassingResource();

        try {
            SupplierInvoiceOCR supplierInvoiceOCR = supplierInvoiceOCRRepo.getById(id);
            if (supplierInvoiceOCR == null) throw new OperationException(Constants.ER01);
            String data = supplierInvoiceOCR.getData();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream((data).getBytes()));

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            Element invoiceNumberAttribute = (Element) xpath.compile(basePath + "'invoiceNumber']").evaluate(doc, XPathConstants.NODE);
            Element totalAmountAttribute = (Element) xpath.compile(basePath + "'supplierInvoiceTotalCost']").evaluate(doc, XPathConstants.NODE);
            Element supplierNameAttribute = (Element) xpath.compile(basePath + "'supplierName']").evaluate(doc, XPathConstants.NODE);
            Element invoiceDateAttribute = (Element) xpath.compile(basePath + "'invoiceDate']").evaluate(doc, XPathConstants.NODE);
            Element invoiceCurrencyAttribute = (Element) xpath.compile(basePath + "'supplierInvoiceCurrency']").evaluate(doc, XPathConstants.NODE);
            Element hsn_sac_codeAttribute = (Element) xpath.compile(basePath + "'HSN_SAS_code']").evaluate(doc, XPathConstants.NODE);
            Element taxableAmountAttribute = (Element) xpath.compile(basePath + "'taxableAmount']").evaluate(doc, XPathConstants.NODE);
            //Element taxAmountAttribute = (Element) xpath.compile(basePath + "'Tax Amount']").evaluate(doc, XPathConstants.NODE);
            Element gstIN_numberAttribute = (Element) xpath.compile(basePath + "'GSTIN_number']").evaluate(doc, XPathConstants.NODE);
            Element supplierStateAttribute = (Element) xpath.compile(basePath + "'supplierState']").evaluate(doc, XPathConstants.NODE);
            Element purchaseRefNoAttribute = (Element) xpath.compile(basePath + "'purchaseRefNo']").evaluate(doc, XPathConstants.NODE);
            Element paymentDueDateAttribute = (Element) xpath.compile(basePath + "'paymentDueDate']").evaluate(doc, XPathConstants.NODE);
            Element invoiceReceivedDateAttribute = (Element) xpath.compile(basePath + "'invoiceReceivedDate']").evaluate(doc, XPathConstants.NODE);
            Element supplierCostAttribute = (Element) xpath.compile(basePath + "'supplierCost']").evaluate(doc, XPathConstants.NODE);
            Element supplierGstAttribute = (Element) xpath.compile(basePath + "'supplierGst']").evaluate(doc, XPathConstants.NODE);
            Element productDetailsAttribute = (Element) xpath.compile(basePath + "'netPayableToSupplier']").evaluate(doc, XPathConstants.NODE);
            Element netPayableToSupplierAttribute = (Element) xpath.compile(basePath + "'productName']").evaluate(doc, XPathConstants.NODE);
            Element supplierInvoiceTotalCommissionAttribute = (Element) xpath.compile(basePath + "'supplierInvoiceTotalCommission']").evaluate(doc, XPathConstants.NODE);


            supplierBillPassingResource.setInvoiceNumber(invoiceNumberAttribute.getAttribute("ElementVal"));
            supplierBillPassingResource.setSupplierInvoiceTotalCost(new BigDecimal(totalAmountAttribute.getAttribute("ElementVal")));
            supplierBillPassingResource.setSupplierInvoiceCurrency(invoiceCurrencyAttribute.getAttribute("ElementVal"));
            supplierBillPassingResource.setInvoiceDate(DateConverter.stringToZonedDateTime(invoiceDateAttribute.getAttribute("ElementVal")));
            supplierBillPassingResource.setSupplierId(supplierInvoiceOCR.getSupplierId());
            supplierBillPassingResource.setGstIN_number(gstIN_numberAttribute.getAttribute("ElementVal"));
            supplierBillPassingResource.setGstIN_number(hsn_sac_codeAttribute.getAttribute("ElementVal"));
            supplierBillPassingResource.setSupplierName(supplierNameAttribute.getAttribute("ElementVal"));
            supplierBillPassingResource.setGstIN_number(gstIN_numberAttribute.getAttribute("ElementVal"));
            supplierBillPassingResource.setSupplierState(supplierStateAttribute.getAttribute("ElementVal"));
            supplierBillPassingResource.setPurchaseRefNo(purchaseRefNoAttribute.getAttribute("ElementVal"));

            String supplierCost = supplierCostAttribute.getAttribute("ElementVal");
            if (!StringUtils.isEmpty(supplierCost)) {
                supplierBillPassingResource.setSupplierCost(new BigDecimal(supplierCost));
            }

            String supplierGst = supplierGstAttribute.getAttribute("ElementVal");
            if (!StringUtils.isEmpty(supplierGst))
                supplierBillPassingResource.setSupplierGst(new BigDecimal(supplierGst));

            String invoiceReceivedDate = invoiceReceivedDateAttribute.getAttribute("ElementVal");
            if (!StringUtils.isEmpty(invoiceReceivedDate))
                supplierBillPassingResource.setInvoiceReceivedDate(DateConverter.stringToZonedDateTime(invoiceReceivedDate));

            String productDetails = productDetailsAttribute.getAttribute("ElementVal");
            if (!StringUtils.isEmpty(productDetails)) {
                Set<String> productName = new HashSet<>();
                productName.add(productDetails);
                supplierBillPassingResource.setProductName(productName);
            }

            String taxableAmount = taxableAmountAttribute.getAttribute("ElementVal");
            if (!StringUtils.isEmpty(taxableAmount))
                supplierBillPassingResource.setTaxableAmount(taxableAmount);

            /*String taxAmount = taxAmountAttribute.getAttribute("ElementVal");
            if (!StringUtils.isEmpty(taxAmount))
                supplierBillPassingResource.setTaxAmount(taxAmount);*/


            String paymentDueDate = paymentDueDateAttribute.getAttribute("ElementVal");
            if (!StringUtils.isEmpty(paymentDueDate))
                supplierBillPassingResource.setPaymentDueDate(DateConverter.stringToZonedDateTime(paymentDueDate));

            String netPayableToSupplier=null;
            if (netPayableToSupplierAttribute!=null) {
                netPayableToSupplier = netPayableToSupplierAttribute.getAttribute("ElementVal");
                if (!StringUtils.isEmpty(netPayableToSupplier))
                    supplierBillPassingResource.setNetPayableToSupplier(new BigDecimal(netPayableToSupplier));
            }

            String supplierInvoiceTotalCommission=null;
            if (supplierInvoiceTotalCommissionAttribute!=null) {
                supplierInvoiceTotalCommission = supplierInvoiceTotalCommissionAttribute.getAttribute("ElementVal");
                if (!StringUtils.isEmpty(supplierInvoiceTotalCommission))
                    supplierBillPassingResource.setSupplierInvoiceTotalCommission(new BigDecimal(supplierInvoiceTotalCommission));
            }

            Set<AttachedServiceOrder> attachedServiceOrderList = new HashSet<>();
            BigDecimal equivalentServiceOrderAmount = BigDecimal.ZERO,supplierCommercials=BigDecimal.ZERO;
            for (String provisionalServiceOrderId : serviceOrderSearchCriteria.getAttachedServiceOrderIds()) {
                ProvisionalServiceOrder provisionalServiceOrder = provisionalServiceOrderService.getPSOById(provisionalServiceOrderId);
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
                supplierCommercials = supplierCommercials.add(provisionalServiceOrder.getSupplierPricing().getSupplierCommercials());
                equivalentServiceOrderAmount = equivalentServiceOrderAmount.add(attachedServiceOrder.getTotalCost());
                attachedServiceOrderList.add(attachedServiceOrder);
            }

            supplierBillPassingResource.setManualEntry(false);
            supplierBillPassingResource.setEquivalentServiceOrderAmount(equivalentServiceOrderAmount);
            if (supplierBillPassingResource.getNetPayableToSupplier()==null)
                supplierBillPassingResource.setNetPayableToSupplier(equivalentServiceOrderAmount);
            supplierBillPassingResource.setAttachedServiceOrders(attachedServiceOrderList);
            if (supplierBillPassingResource.getPaymentDueDate()==null) supplierBillPassingResource.setPaymentDueDate(ZonedDateTime.now().plusDays(5));
            result.put("supplierBillPassingResource",supplierBillPassingResource);
            return result;

        } catch (Exception e) {
            logger.error("error in getting invoice details from OCR invoice");
            throw new OperationException(Constants.ER1030);
        }
    }

    @Override
    public Map getCommercialStatementInvoice(String id, CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {

        commercialStatementSearchCriteria.setBillPassingResource(true);
        Map result=new HashMap<>();
        try {
            if (commercialStatementSearchCriteria.getAttachedStatementIds().size()>1)
                result=supplierCommercialStatementService.searchByCriteria(commercialStatementSearchCriteria);
            SupplierInvoiceOCR supplierInvoiceOCR = supplierInvoiceOCRRepo.getById(id);
            if (supplierInvoiceOCR == null) throw new OperationException(Constants.ER01);
            String data = supplierInvoiceOCR.getData();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream((data).getBytes()));

            XPathFactory xPathfactory = XPathFactory.newInstance();

            XPath xpath = xPathfactory.newXPath();

            Element invoiceNumberAttribute = (Element) xpath.compile(basePath + "'invoiceNumber']").evaluate(doc, XPathConstants.NODE);
            Element totalAmountAttribute = (Element) xpath.compile(basePath + "'supplierInvoiceTotalCost']").evaluate(doc, XPathConstants.NODE);
            Element supplierNameAttribute = (Element) xpath.compile(basePath + "'supplierName']").evaluate(doc, XPathConstants.NODE);
            Element invoiceDateAttribute = (Element) xpath.compile(basePath + "'invoiceDate']").evaluate(doc, XPathConstants.NODE);
            Element invoiceCurrencyAttribute = (Element) xpath.compile(basePath + "'supplierInvoiceCurrency']").evaluate(doc, XPathConstants.NODE);
            Element paymentDueDateAttribute = (Element) xpath.compile(basePath + "'paymentDueDate']").evaluate(doc, XPathConstants.NODE);
            Element invoiceReceivedDateAttribute = (Element) xpath.compile(basePath + "'invoiceReceivedDate']").evaluate(doc, XPathConstants.NODE);
            Element productDetailsAttribute = (Element) xpath.compile(basePath + "'productName']").evaluate(doc, XPathConstants.NODE);
            Element netPayableToSupplierAttribute = (Element) xpath.compile(basePath + "'netPayableToSupplier']").evaluate(doc, XPathConstants.NODE);

            CommercialStatementsBillPassingResource supplierInvoiceOCRDetails = new CommercialStatementsBillPassingResource();
            supplierInvoiceOCRDetails.setInvoiceNumber(invoiceNumberAttribute.getAttribute("ElementVal"));
            supplierInvoiceOCRDetails.setInvoiceAmount(new BigDecimal(totalAmountAttribute.getAttribute("ElementVal")));
            supplierInvoiceOCRDetails.setInvoiceCurrency(invoiceCurrencyAttribute.getAttribute("ElementVal"));
            supplierInvoiceOCRDetails.setInvoiceDate(DateConverter.stringToZonedDateTime(invoiceDateAttribute.getAttribute("ElementVal")));
            supplierInvoiceOCRDetails.setSupplierOrClientId(supplierInvoiceOCR.getSupplierId());
            supplierInvoiceOCRDetails.setSupplierOrClientName(supplierNameAttribute.getAttribute("ElementVal"));

            String invoiceReceivedDate = invoiceReceivedDateAttribute.getAttribute("ElementVal");
            if (!StringUtils.isEmpty(invoiceReceivedDate))
                supplierInvoiceOCRDetails.setInvoiceReceivedDate(DateConverter.stringToZonedDateTime(invoiceReceivedDate));

            String productDetails = productDetailsAttribute.getAttribute("ElementVal");
            if (!StringUtils.isEmpty(productDetails)) {
                Set<String> productName = new HashSet<>();
                productName.add(productDetails);
                supplierInvoiceOCRDetails.setProductName(productName);
            }

            String paymentDueDate = paymentDueDateAttribute.getAttribute("ElementVal");
            if (!StringUtils.isEmpty(paymentDueDate))
                supplierInvoiceOCRDetails.setPaymentDueDate(DateConverter.stringToZonedDateTime(paymentDueDate));

            String netPayableToSupplier=null;
            if (netPayableToSupplierAttribute!=null){
                netPayableToSupplier=netPayableToSupplierAttribute.getAttribute("ElementVal");
                if (!StringUtils.isEmpty(netPayableToSupplier))
                    supplierInvoiceOCRDetails.setNetPayableToSupplierOrClient(new BigDecimal(netPayableToSupplier));
            }

            Set<AttachedCommercialStatement> attachedCommercialStatementSet = new HashSet<>();
            BigDecimal commercialValueAsPerStatement = BigDecimal.ZERO, clientServiceTaxAmount = BigDecimal.ZERO;

            for (String statementId : commercialStatementSearchCriteria.getAttachedStatementIds()) {
                AttachedCommercialStatement attachedCommercialStatement = new AttachedCommercialStatement();
                SupplierCommercialStatement supplierCommercialStatement = supplierCommercialStatementService.get(statementId);
                if (supplierCommercialStatement == null) throw new OperationException(Constants.ER01);
                if (!supplierCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.PAYABLE.getValue()))
                    throw new OperationException(Constants.ER1002);
                if (supplierCommercialStatement.getCommercialStatementsBillPassing() != null)
                    throw new OperationException(Constants.ER1029);
                attachedCommercialStatement.setSupplierorClientId(supplierCommercialStatement.getSupplierOrClientId());
                attachedCommercialStatement.setSupplierOrClientName(supplierCommercialStatement.getSupplierOrClientName());
                attachedCommercialStatement.setProductName(supplierCommercialStatement.getProductName());
                attachedCommercialStatement.setTotalPayable(supplierCommercialStatement.getTotalPayable());
                attachedCommercialStatement.setCommercialHead(supplierCommercialStatement.getCommercialHead());
                attachedCommercialStatement.setStatementName(supplierCommercialStatement.getStatementName());
                attachedCommercialStatement.setStatementId(supplierCommercialStatement.getStatementId());
                attachedCommercialStatementSet.add(attachedCommercialStatement);

                if (supplierCommercialStatement.getTotalServiceTax() != null)
                    clientServiceTaxAmount = clientServiceTaxAmount.add(supplierCommercialStatement.getTotalServiceTax());

                commercialValueAsPerStatement = commercialValueAsPerStatement.add(supplierCommercialStatement.getTotalPayable());
            }
            supplierInvoiceOCRDetails.setAttachedCommercialStatements(attachedCommercialStatementSet);
            supplierInvoiceOCRDetails.setEquivalentCommercialStatementAmount(commercialValueAsPerStatement);
            if (supplierInvoiceOCRDetails.getNetPayableToSupplierOrClient()==null)
                supplierInvoiceOCRDetails.setNetPayableToSupplierOrClient(supplierInvoiceOCRDetails.getEquivalentCommercialStatementAmount());
            supplierInvoiceOCRDetails.setClientServiceTaxAmount(clientServiceTaxAmount);
            supplierInvoiceOCRDetails.setManualEntry(false);
            supplierInvoiceOCRDetails.setCommercialStatementFor(CommercialStatementFor.SUPPLIER.getName());
            result.put("billPassingResource", supplierInvoiceOCRDetails);
            return result;

        }catch (OperationException e){
            throw  e;
        }
        catch (Exception e) {
            logger.error("error in getting invoice details from OCR invoice");
            throw new OperationException(Constants.ER1030);
        }
    }
}
