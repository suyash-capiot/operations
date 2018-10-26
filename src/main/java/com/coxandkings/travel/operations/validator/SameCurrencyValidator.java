package com.coxandkings.travel.operations.validator;


import com.coxandkings.travel.operations.annotation.SameCurrency;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class SameCurrencyValidator implements ConstraintValidator<SameCurrency,PaymentAdvice> {
    @Override
    public void initialize(SameCurrency constraintAnnotation) {

    }

    @Override
    public boolean isValid(PaymentAdvice PaymentAdvice, ConstraintValidatorContext context) {
        if(PaymentAdvice ==null ){
            return false;
        }

        if(!PaymentAdvice.getSupplierCurrency().equalsIgnoreCase(PaymentAdvice.getSelectedSupplierCurrency())){

            return false;
        }

        return  true;
    }
}