package com.coxandkings.travel.operations.controller.prepaymenttosupplier;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.SupplierPayableReceivableAmt;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.SupplierPaymentResource;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviseMDMLoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.AutomaticPaymentAdviceGenerationService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.PaymentAdviceLoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.PaymentAdviceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/prepayments/paymentAdvices")
@CrossOrigin(value = "*")
public class PaymentAdviceController {

    private static Logger logger = LogManager.getLogger(PaymentAdviceController.class);

    @Autowired
    PaymentAdviceService paymentAdviceService;

    @Autowired
    PaymentAdviceLoaderService paymentAdviceLoaderService;

    @Autowired
    AutomaticPaymentAdviceGenerationService automaticPaymentAdviceGenerationService;

    @Autowired
    PaymentAdviseMDMLoaderService paymentAdviseMDMLoaderService;

    @Autowired
    OpsBookingService opsBookingService;

    @GetMapping(value = "/v1/loadSupplierDetails")
    public ResponseEntity<SupplierPaymentResource> loadSupplierDetails(@RequestParam(value = "bookingReferenceId", required = false) String bookingReferenceId,
                                                                       @RequestParam(value = "orderId", required = false) String orderId,
                                                                       @RequestParam(value = "supplierReferenceId", required = false) String supplierReferenceId,
                                                                       @RequestParam(value = "paymentAdviceId", required = false) String paymentAdviceId)
            throws OperationException {
        SupplierPaymentResource supplierPaymentResource = paymentAdviceLoaderService.loadSupplierDetails(bookingReferenceId, orderId, supplierReferenceId, paymentAdviceId);
         return new ResponseEntity<SupplierPaymentResource>(supplierPaymentResource, HttpStatus.OK);
    }

    @PostMapping(value = "/v1/save")
    public ResponseEntity<PaymentAdvice> savePaymentAdvice(@RequestBody SupplierPaymentResource resource) throws Exception {
        PaymentAdvice PaymentAdvice = paymentAdviceService.savePaymentAdvice(resource);
        return new ResponseEntity<PaymentAdvice>(PaymentAdvice, HttpStatus.OK);
    }

    @PutMapping(value = "/v1/update")
    public ResponseEntity<PaymentAdvice> updatePaymentAdvice(@RequestBody  SupplierPaymentResource resource) throws OperationException {
        PaymentAdvice paymentAdvice = paymentAdviceService.updatePaymentAdvice(resource);
        return new ResponseEntity<PaymentAdvice>(paymentAdvice, HttpStatus.OK);
    }

    @GetMapping(value = "/v1/getBy/bookings/{bookingRefId}/orders/{orderId}/suppliers/{supplierId}")
    public ResponseEntity<List<PaymentAdvice>> getAllPaymentAdvices(@PathVariable("bookingRefId") String bookingRefId, @PathVariable("orderId") String orderId, @PathVariable("supplierId") String supplierId) throws OperationException {
        List<PaymentAdvice> paymentAdvices = paymentAdviceLoaderService.getAllPaymentAdvices(bookingRefId, orderId, supplierId);
        return new ResponseEntity<List<PaymentAdvice>>(paymentAdvices, HttpStatus.OK);
    }

    @GetMapping(value = "/v1/{id}")
    public ResponseEntity<PaymentAdvice> getById(@PathVariable("id") String id) throws OperationException {
        return new ResponseEntity<PaymentAdvice>(paymentAdviceService.getById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/v1/{paymentAdviceId}/approve")
    public ResponseEntity<PaymentAdvice> approvePaymentAdvise(@PathVariable("paymentAdviceId") String paymentAdviceId, @RequestBody String remarks) throws OperationException, JSONException, JSONException, ParseException {
        PaymentAdvice paymentAdvice = paymentAdviceService.approvePaymentAdvise(paymentAdviceId, remarks);
        return new ResponseEntity<PaymentAdvice>(paymentAdvice, HttpStatus.OK);
    }

    @PostMapping(value = "/v1/{paymentAdviceId}/reject")
    public ResponseEntity<PaymentAdvice> rejected(@PathVariable("paymentAdviceId") String paymentAdviceId, @RequestBody String remarks) throws JSONException, OperationException, ParseException, InvocationTargetException, IllegalAccessException {
        PaymentAdvice paymentAdvice = paymentAdviceService.rejected(paymentAdviceId, remarks);
        return new ResponseEntity<PaymentAdvice>(paymentAdvice, HttpStatus.OK);
    }

    @PostMapping(value = "/v1/searchByPaymentCriteria")
    public ResponseEntity<List<PaymentAdvice>> search(@RequestBody PaymentCriteria paymentCriteria) throws OperationException {
        List<PaymentAdvice> PaymentAdvice = paymentAdviceService.searchSupplierPayment(paymentCriteria);
        return new ResponseEntity<List<PaymentAdvice>>(PaymentAdvice, HttpStatus.OK);
    }

    @PostMapping(value = "/v1/kafka")
    public ResponseEntity<PaymentAdvice> generatePa(@RequestBody KafkaBookingMessage kafkaBookingMessage) throws ParseException, IOException, OperationException, JSONException, InvocationTargetException, IllegalAccessException {
        OpsBooking opsBooking = opsBookingService.getBooking(kafkaBookingMessage.getBookId());
        PaymentAdvice paymentAdvice = automaticPaymentAdviceGenerationService.generatePaymentAdvice();
        return new ResponseEntity<>(paymentAdvice, HttpStatus.CREATED);
    }

    @GetMapping(value = "/v1/payableReceivableAmt/{supplierId}")
    public ResponseEntity<SupplierPayableReceivableAmt> getPayableAndReceivableAmt(@PathVariable("supplierId") String supplierId) throws OperationException, IOException {
        SupplierPayableReceivableAmt supplierPayableReceivableAmt = paymentAdviceService.getSupplierPayableAndReceivableAmt(supplierId);
        return new ResponseEntity<>(supplierPayableReceivableAmt, HttpStatus.OK);
    }


}