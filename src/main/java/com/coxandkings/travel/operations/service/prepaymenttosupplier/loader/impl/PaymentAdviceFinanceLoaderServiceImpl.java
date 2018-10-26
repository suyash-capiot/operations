package com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.impl;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdviceOrderInfo;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PayToSupplier;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PaymentDetails;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentadvice.PaymentAdviceRepository;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentdetails.PaymentDetailsRepository;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviceFinanceLoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviseBELoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.PaymentAdviceLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class PaymentAdviceFinanceLoaderServiceImpl implements PaymentAdviceFinanceLoaderService {


    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private PaymentAdviceRepository paymentAdviceRepository;

    @Autowired
    private PaymentAdviseBELoaderService paymentAdviseBELoaderService;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private PaymentAdviceLoaderService paymentAdviceLoaderService;

    @Override
    public BigDecimal getBalanceAmtPayableToSupplier(String bookingRefId, String orderId, String supplierId, BigDecimal netPayableToSupplier) throws OperationException, IOException {
        BigDecimal netAmtToSupplier = null;
        BigDecimal balAmtPayableToSupplier = null;
        boolean flag = false;

        if (netPayableToSupplier != null) {
            netAmtToSupplier = netPayableToSupplier;
        } else {
            String netAmt = String.valueOf(paymentAdviceLoaderService.getNetPayableToSupplier(bookingRefId, orderId));
            netAmtToSupplier = new BigDecimal(netAmt);
        }

        PaymentCriteria paymentCriteria = new PaymentCriteria();
        paymentCriteria.setBookingRefId(bookingRefId);
        paymentCriteria.setOrderId(orderId);
        paymentCriteria.setSupplierReferenceId(supplierId);

        List<PaymentAdvice> paymentAdviceList = paymentAdviceRepository.searchPaymentAdvise(paymentCriteria);

        if (paymentAdviceList != null && paymentAdviceList.size() > 0) {
            BigDecimal amountPayableForSupplier = BigDecimal.ZERO;
            for (PaymentAdvice paymentAdvice : paymentAdviceList)
            {
                String paymentNumber = paymentAdvice.getPaymentAdviceNumber();
                PaymentDetails paymentDetails = paymentDetailsRepository.serachPaymentDetails(paymentNumber);

                if (paymentDetails != null) {
                    List<PayToSupplier> payToSuppliers = paymentDetails.getPayToSupplierSet();
                    if (payToSuppliers != null) {
                        System.out.println("Size of pay to suppplier" + paymentDetails.getPayToSupplierSet().size());

                     //   amountPayableForSupplier = payToSuppliers.stream().map(PayToSupplier::getAmountPayableForSupplier).reduce(BigDecimal::add);

                        for(PayToSupplier payToSupplier:payToSuppliers){
                            amountPayableForSupplier= amountPayableForSupplier.add( payToSupplier.getAmountPayableForSupplier());
                        }

                    }


                }

            }

           /* if (amountPayableForSupplier.isPresent()) {
                balAmtPayableToSupplier = netPayableToSupplier.subtract(amountPayableForSupplier.get());

            }
*/
            balAmtPayableToSupplier=netPayableToSupplier.subtract(amountPayableForSupplier);

        } else {

            balAmtPayableToSupplier = netPayableToSupplier;
        }

        return balAmtPayableToSupplier;

    }


}
