package com.coxandkings.travel.operations.validations;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.CommercialStatementsBillPassing;
import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierBillPassing;

import javax.validation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HibernateValidator {
    static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    static Validator validator = factory.getValidator();

    public static void supplierBillPassingValidator(SupplierBillPassing supplierBillPassing) throws OperationException {
        Set<ConstraintViolation<SupplierBillPassing>> constraintViolations = validator.validate(supplierBillPassing);
        Map<Path, String> validations = new HashMap<>();
        constraintViolations.stream().forEach(supplierInvoiceConstraintViolation -> validations.put(supplierInvoiceConstraintViolation.getPropertyPath(), supplierInvoiceConstraintViolation.getMessage()));
        if (validations.size() >= 1) {
            throw new OperationException(validations);
        }
    }

    public static void commercialSatementBillPassingValidator(CommercialStatementsBillPassing commercialStatementsBillPassing) throws OperationException {
        Set<ConstraintViolation<CommercialStatementsBillPassing>> constraintViolations= validator.validate(commercialStatementsBillPassing);
        Map<Path,String> validations=new HashMap<>();
        constraintViolations.stream().forEach(commercialStatementsBillPassingConstraintViolation -> validations.put(commercialStatementsBillPassingConstraintViolation.getPropertyPath(),commercialStatementsBillPassingConstraintViolation.getMessage()));
        if (validations.size()>=1){
            throw new OperationException(validations);
        }
    }

}
