package com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.SupplierPayableReceivableAmt;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.SupplierPaymentResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface PaymentAdviceService {
	
	public PaymentAdvice savePaymentAdvice(SupplierPaymentResource resource) throws OperationException, ParseException;

	public PaymentAdvice updatePaymentAdvice(SupplierPaymentResource resource) throws OperationException;

	public List<PaymentAdvice> searchSupplierPayment(PaymentCriteria paymentCriteria) throws OperationException;

	public PaymentAdvice getById(String id) throws OperationException;

	public PaymentAdvice approvePaymentAdvise(String paymentAdviceNumber, String remarks) throws OperationException, JSONException, ParseException;

	public PaymentAdvice rejected(String paymentAdviceNumber, String remarks) throws JSONException, OperationException, ParseException, InvocationTargetException, IllegalAccessException;

	public void updateProductForAction(OpsBooking opsBooking);

    public ToDoTaskResource getTodoForFinance(PaymentAdvice paymentAdvice) throws OperationException, ParseException;

	public ToDoTaskResource getTodoForOps(PaymentAdvice paymentAdvice) throws OperationException, ParseException;

    public String getNewPaymentAdviceID();

    public SupplierPayableReceivableAmt getSupplierPayableAndReceivableAmt(String supplierId) throws OperationException, IOException;

//	public BigDecimal getSupplierPayableAmt(String supplierId) throws OperationException;

//	public BigDecimal getSupplierReceivableAmt(String supplierId) throws OperationException;

}
