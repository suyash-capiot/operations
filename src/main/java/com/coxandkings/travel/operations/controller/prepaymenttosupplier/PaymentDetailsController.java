package com.coxandkings.travel.operations.controller.prepaymenttosupplier;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PaymentDetails;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentdetails.MediaResource;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentdetails.PaymentDetailsResource;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentdetails.PaymentDetailsService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Enumeration;

@RestController
@RequestMapping(value = "/paymentDetails")
@CrossOrigin(value = "*")
public class PaymentDetailsController {

    private static Logger logger = LogManager.getLogger(PaymentDetailsController.class);

    @Autowired
    private PaymentDetailsService paymentDetailsService;


    @PostMapping(value = "/v1/save")
    public ResponseEntity<PaymentDetails> savePaymentDetails(@RequestBody PaymentDetails resource) throws OperationException, IOException {
        PaymentDetails paymentDetails = paymentDetailsService.savePaymentDetails(resource);
        return new ResponseEntity<>(paymentDetails, HttpStatus.CREATED);

    }

    @GetMapping(value = "/v1/getPaymentDetails/{paymentAdviceNumber}")
    public ResponseEntity<PaymentDetails> getPaymentDetails(@PathVariable("paymentAdviceNumber") String paymentAdviceNumber) throws OperationException, ParseException {
         PaymentDetails  paymentDetailsresource = paymentDetailsService.getPaymentDetails(paymentAdviceNumber);
        return new ResponseEntity<>(paymentDetailsresource, HttpStatus.OK);


    }

    @GetMapping(value = "/v1/getRoe")
    public ResponseEntity<BigDecimal> getRoe(@RequestParam(name = "supplierCurrency") String supplierCurrency,
                                             @RequestParam(name = "remittanceCurrency") String remittanceCurrency,
                                             @RequestParam(name = "dateOfPayment") String dateOfPayment,
                                             @RequestParam(name = "supplierId") String supplierId,
                                             @RequestParam(name = "roeType") String roeType) throws OperationException {
        return new ResponseEntity<>(paymentDetailsService.getRoe(supplierCurrency, remittanceCurrency, dateOfPayment, supplierId, roeType), HttpStatus.OK);
    }

    @GetMapping(value = "/v1/{id}")
    public ResponseEntity<PaymentDetails> getPaymentDetailById(@PathVariable("id") String id) throws OperationException {
            PaymentDetails paymentDetails = paymentDetailsService.getPaymentDetailsById(id);
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
    }


    @PostMapping(value = "/v1/sendAnEmailToSupplier")
    public ResponseEntity<EmailResponse> sendAnEmailToSupplier(@RequestBody PaymentDetails paymentDetails) throws OperationException {
        return new ResponseEntity<EmailResponse>(paymentDetailsService.sendAnEmailToSupplier(paymentDetails),HttpStatus.OK);
    }

}

