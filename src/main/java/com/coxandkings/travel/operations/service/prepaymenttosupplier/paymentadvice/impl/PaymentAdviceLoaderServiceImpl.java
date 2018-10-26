package com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.impl;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentAdviceStatusValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdviceOrderInfo;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentadvice.PaymentAdviceRepository;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.DateWisePaymentPercentage;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.PaymentAdviceResource;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.SupplierPaymentResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviceFinanceLoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviseBELoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviseMDMLoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.PaymentAdviceLoaderService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class PaymentAdviceLoaderServiceImpl implements PaymentAdviceLoaderService {

    @Autowired
    PaymentAdviceRepository paymentAdviceRepository;

    @Autowired
    private PaymentAdviceFinanceLoaderService paymentAdviceFinanceLoaderService;

    @Autowired
    private PaymentAdviseMDMLoaderService paymentAdviseMDMLoaderService;

    @Autowired
    private PaymentAdviseBELoaderService paymentAdviseBELoaderService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;

    private static Logger logger = LogManager.getLogger(PaymentAdviceLoaderServiceImpl.class);


    @Override
    public SupplierPaymentResource loadSupplierDetails(String bookingReferenceId, String orderId,
                                                       String supplierReferenceId, String paymentAdviceNumber)
            throws OperationException
    {
        logger.info("in loadSupplierDetails service method method ");
        int loggerIndex = 0;

        SupplierPaymentResource supplierPaymentResource = new SupplierPaymentResource();
        PaymentAdviceResource paymentAdviceResource = new PaymentAdviceResource();
        PaymentCriteria paymentCriteria = null;

       BigDecimal totalAmtPayableToSupplier = new BigDecimal(0);
       BigDecimal diffAmt = new BigDecimal(0);
       BigDecimal netPayable = new BigDecimal(0);


        try
        {
            if (StringUtils.isEmpty(paymentAdviceNumber))
            {
                paymentCriteria = new PaymentCriteria();
                paymentCriteria.setBookingRefId(bookingReferenceId);
                paymentCriteria.setOrderId(orderId);
                paymentCriteria.setSupplierReferenceId(supplierReferenceId);

                List<PaymentAdvice> paymentAdviceList = paymentAdviceRepository.searchPaymentAdvise(paymentCriteria);

                if (paymentAdviceList!=null && paymentAdviceList.size()>0)
                {
                    for (PaymentAdvice paymentAdvice:paymentAdviceList)
                    {
                        totalAmtPayableToSupplier = BigDecimal.valueOf(totalAmtPayableToSupplier.add(paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getOrderLevelAmountPayableForSupplier()).doubleValue());
//                        netPayable = paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getOrderLevelNetPayableToSupplier();
                    }
                }
                logger.info("in getNetPayableToSupplier service method method ");
                BigDecimal netPayableToSupplier = getNetPayableToSupplier(bookingReferenceId, orderId);

                diffAmt = netPayableToSupplier.subtract(totalAmtPayableToSupplier);


/*
                int i = diffAmt.compareTo(new BigDecimal(0));
                if (i==0)
                {
                    diffAmt = netPayableToSupplier;
                }*/

                logger.info("in getBalanceAmtPayableToSupplier");
                BigDecimal balanceAmtPayableToSupplier = paymentAdviceFinanceLoaderService.getBalanceAmtPayableToSupplier(bookingReferenceId, orderId, supplierReferenceId, netPayableToSupplier);

                logger.info(" step->"+loggerIndex++);
                String paymentAdviceGenerationDueDate = paymentAdviseMDMLoaderService.getPaymentAdviceGenerationDueDate(supplierReferenceId); //TODO: need  changes.

                logger.info(" step->"+loggerIndex++);
                String typeOfSettalment = paymentAdviseMDMLoaderService.getTypeOfSettlement(supplierReferenceId,true);

                logger.info(" step->"+loggerIndex++);
                List<String> supplierCurrency = paymentAdviseMDMLoaderService.getSupplierCurrenciesList(supplierReferenceId);

                logger.info(" step->"+loggerIndex++);

                PaymentAdviceOrderInfo paymentAdviceOrderInfo = new PaymentAdviceOrderInfo();
                paymentAdviceOrderInfo.setBookingRefId(bookingReferenceId);
                paymentAdviceOrderInfo.setOrderId(orderId);
                paymentAdviceOrderInfo.setServiceOrderId(null);
                paymentAdviceOrderInfo.setOrderLevelNetPayableToSupplier(netPayableToSupplier);
                paymentAdviceOrderInfo.setOrderLevelBalanceAmtPayableToSupplier(balanceAmtPayableToSupplier);
                paymentAdviceOrderInfo.setOrderLevelAmountPayableForSupplier(diffAmt);
                Set<PaymentAdviceOrderInfo> paymentAdviceOrderInfoSet = new HashSet<>();
                paymentAdviceOrderInfoSet.add(paymentAdviceOrderInfo);

                supplierPaymentResource.setPaymentAdviceOrderInfoSet(paymentAdviceOrderInfoSet);
                supplierPaymentResource.setSupplierRefId(supplierReferenceId);
                ZoneId defaultZoneId = ZoneId.systemDefault();
                DateWisePaymentPercentage paymentDueDate = paymentAdviseMDMLoaderService.getPaymentDueDate(bookingReferenceId, orderId, supplierReferenceId);
                supplierPaymentResource.setPaymentDueDate(paymentDueDate.getPaymentDueDate().toInstant().atZone(defaultZoneId)); //TODO: should get From BE
                supplierPaymentResource.setPrePaymentApplicable(true);
                supplierPaymentResource.setNetPayableToSupplier(netPayableToSupplier);
                supplierPaymentResource.setBalanceAmtPayableToSupplier(balanceAmtPayableToSupplier);

                paymentAdviceResource.setUiTrigger(true);
                paymentAdviceResource.setPaymentAdviceGenerationDueDate(ZonedDateTime.parse(paymentAdviceGenerationDueDate).withZoneSameInstant(ZoneId.systemDefault()));
                paymentAdviceResource.setTypeOfSettlement(typeOfSettalment);
                paymentAdviceResource.setAmountPayableForSupplier(diffAmt);
                paymentAdviceResource.setPaymentAdviceStatusId(PaymentAdviceStatusValues.APPROVAL_PENDING);
                supplierPaymentResource.setPaymentAdviceResource(paymentAdviceResource);
                supplierPaymentResource.setAvailableSupplierCurrencies(supplierCurrency);

                logger.info(" step->"+loggerIndex++);
            }
            else {
                paymentCriteria = new PaymentCriteria();
//                paymentCriteria.setBookingRefId(bookingReferenceId);
//                paymentCriteria.setSupplierReferenceId(supplierReferenceId);
                paymentCriteria.setPaymentAdviceNumber(paymentAdviceNumber);
//                paymentCriteria.setOrderId(orderId);
                logger.info(" step->"+loggerIndex++);
                List<PaymentAdvice> existingPaymentDetailsInfo = paymentAdviceRepository.searchPaymentAdvise(paymentCriteria);
                logger.info(" step->"+loggerIndex++);
                List<String> supplierCurrency = paymentAdviseMDMLoaderService.getSupplierCurrenciesList(supplierReferenceId);
                logger.info(" step->"+loggerIndex++);
                if (existingPaymentDetailsInfo != null) {
                    PaymentAdvice savedPaymentAdviseInfo = existingPaymentDetailsInfo.get(0);

                    supplierPaymentResource.setId(savedPaymentAdviseInfo.getId());
                    supplierPaymentResource.setPaymentAdviceOrderInfoSet(savedPaymentAdviseInfo.getPaymentAdviceOrderInfoSet());
                    supplierPaymentResource.setSupplierName(savedPaymentAdviseInfo.getSupplierName());
                    supplierPaymentResource.setSupplierRefId(savedPaymentAdviseInfo.getSupplierRefId());
                    supplierPaymentResource.setPaymentDueDate(savedPaymentAdviseInfo.getPaymentDueDate());
                    supplierPaymentResource.setDayOfPayment(savedPaymentAdviseInfo.getDayOfPayment());
                    supplierPaymentResource.setSupplierCurrency(savedPaymentAdviseInfo.getSupplierCurrency());
                    supplierPaymentResource.setNetPayableToSupplier(savedPaymentAdviseInfo.getNetPayableToSupplier());
                    supplierPaymentResource.setBalanceAmtPayableToSupplier(savedPaymentAdviseInfo.getBalanceAmtPayableToSupplier());
                    supplierPaymentResource.setPrePaymentApplicable(savedPaymentAdviseInfo.getPrePaymentApplicable());
                    supplierPaymentResource.setApproverRemark(savedPaymentAdviseInfo.getApproverRemark());

                    paymentAdviceResource.setPaymentAdviceNumber(savedPaymentAdviseInfo.getPaymentAdviceNumber());
                    paymentAdviceResource.setPaymentAdviceStatusId(savedPaymentAdviseInfo.getPaymentAdviceStatus());
                    paymentAdviceResource.setGuaranteeToSupplier(savedPaymentAdviseInfo.isGuaranteeToSupplier());
                    paymentAdviceResource.setPayToSupplier(savedPaymentAdviseInfo.isPayToSupplier());
                    paymentAdviceResource.setPaymentAdviceGenerationDueDate(savedPaymentAdviseInfo.getPaymentAdviceGenerationDueDate());
                    paymentAdviceResource.setDayOfPaymentAdviceGeneration(savedPaymentAdviseInfo.getDayOfPaymentAdviceGeneration());
                    paymentAdviceResource.setTypeOfSettlement(savedPaymentAdviseInfo.getTypeOfSettlement());
                    paymentAdviceResource.setSelectedSupplierCurrency(savedPaymentAdviseInfo.getSelectedSupplierCurrency());
                    paymentAdviceResource.setAmountPayableForSupplier(savedPaymentAdviseInfo.getPaymentAdviceOrderInfoSet().iterator().next().getOrderLevelAmountPayableForSupplier());

                    paymentAdviceResource.setModeOfPayment(savedPaymentAdviseInfo.getModeOfPayment());
                    paymentAdviceResource.setAdvancedType(savedPaymentAdviseInfo.getAdvancedType());
                    paymentAdviceResource.setPaymentRemark(savedPaymentAdviseInfo.getPaymentRemark());

                    supplierPaymentResource.setDocumentReferenceId(savedPaymentAdviseInfo.getDocumentReferenceId());
                    supplierPaymentResource.setAvailableSupplierCurrencies(supplierCurrency);
                    supplierPaymentResource.setPaymentAdviceResource(paymentAdviceResource);
                    logger.info(" step->"+loggerIndex++);
                } else {
                    logger.info(" step->"+loggerIndex++);
                    throw new OperationException(Constants.ER01);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info(" step->"+loggerIndex++);
        return supplierPaymentResource;

    }

    @Override
    public List<PaymentAdvice> getAllPaymentAdvices(String bookingID, String orderID, String supplierId) throws OperationException {
        PaymentCriteria paymentCriteria = new PaymentCriteria();
        paymentCriteria.setBookingRefId(bookingID);
        paymentCriteria.setOrderId(orderID);
        paymentCriteria.setSupplierReferenceId(supplierId);

        List<PaymentAdvice> existedPaymentAdvices = paymentAdviceRepository.searchPaymentAdvise(paymentCriteria);
        if (existedPaymentAdvices.size() > 0)
            return existedPaymentAdvices;
        else
            throw new OperationException(Constants.ER01);
    }

    public BigDecimal getNetPayableToSupplier(String bookingRef, String orderId)
    {
        ServiceOrderResource serviceOrderResource = null;
        Map<String, Object> serviceOrdersAndSupplierLiabilities = new HashMap<>();
        ServiceOrderSearchCriteria criteria = new ServiceOrderSearchCriteria();
        criteria.setBookingRefNo(bookingRef);
        criteria.setOrderId(orderId);
        try {
            serviceOrdersAndSupplierLiabilities = serviceOrderAndSupplierLiabilityService.getServiceOrdersAndSupplierLiabilities(criteria, false);
        } catch (OperationException e) {
            logger.error("Error while fetching Net Payable Value from ServiceOrder");
        } catch (IOException e) {
            logger.error("IO Error while fetching Net Payable Value from ServiceOrder");
        }
        if (serviceOrdersAndSupplierLiabilities!=null) {
            List<ServiceOrderResource> orderResource = (List<ServiceOrderResource>) serviceOrdersAndSupplierLiabilities.get("result");
            if (orderResource.size() != 0) {
                serviceOrderResource = orderResource.get(0);
                return serviceOrderResource.getSupplierPricingResource().getNetPayableToSupplier();
            }
        }
        return null;
    }


}
