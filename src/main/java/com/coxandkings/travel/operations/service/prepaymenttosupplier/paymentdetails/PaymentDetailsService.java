package com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentdetails;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PaymentDetails;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentdetails.MediaResource;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentdetails.PaymentDetailsResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;

public interface PaymentDetailsService {

    String uploadDocs(MultipartFile file, MediaResource mediaResource);

    PaymentDetails savePaymentDetails(PaymentDetails resource) throws OperationException, IOException;

    PaymentDetails getPaymentDetails(String paymentAdviceNumber) throws ParseException, OperationException;

    BigDecimal getRoe(String supplierCurrency, String remittanceCurrency, String dateOfPayment, String supplierId, String roeType) throws OperationException;

    PaymentDetails getPaymentDetailsById(String id);

    EmailResponse sendAnEmailToSupplier(PaymentDetails paymentDetails) throws OperationException;
}
