package com.coxandkings.travel.operations.validations;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.BaseServiceOrderDetails;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.PassengersDetails;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.SupplierPricing;

import javax.validation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServiceOrderAndSupplierLiabilityValidator {

    static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    static Validator validator = factory.getValidator();

    public static void validateServiceOrderAndSupplierLiability(BaseServiceOrderDetails serviceOrderAndSupplierLiability) throws OperationException {
        Set<ConstraintViolation<BaseServiceOrderDetails>> constraintViolations = validator.validate(serviceOrderAndSupplierLiability);
        Map<Path, String> validations = new HashMap<>();
        constraintViolations.stream().forEach(supplierInvoiceConstraintViolation -> validations.put(supplierInvoiceConstraintViolation.getPropertyPath(), supplierInvoiceConstraintViolation.getMessage()));
        if (validations.size() >= 1) {
            throw new OperationException(validations);
        }
    }

    public static void validateSupplierPricing(SupplierPricing supplierPricing) throws OperationException {
        Set<ConstraintViolation<SupplierPricing>> constraintViolations = validator.validate(supplierPricing);
        Map<Path, String> validations = new HashMap<>();
        constraintViolations.stream().forEach(supplierInvoiceConstraintViolation -> validations.put(supplierInvoiceConstraintViolation.getPropertyPath(), supplierInvoiceConstraintViolation.getMessage()));
        if (validations.size() >= 1) {
            throw new OperationException(validations);
        }
    }

    public static void validatePassengerDetails(Set<PassengersDetails> passengersDetailsList) throws OperationException {
        for (PassengersDetails passengersDetails : passengersDetailsList) {
            Set<ConstraintViolation<PassengersDetails>> constraintViolations = validator.validate(passengersDetails);
            Map<Path, String> validations = new HashMap<>();
            constraintViolations.stream().forEach(supplierInvoiceConstraintViolation -> validations.put(supplierInvoiceConstraintViolation.getPropertyPath(), supplierInvoiceConstraintViolation.getMessage()));
            if (validations.size() >= 1) {
                throw new OperationException(validations);
            }
        }
    }
}