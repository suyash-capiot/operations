package com.coxandkings.travel.operations.annotation;

import com.coxandkings.travel.operations.validator.SameCurrencyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {SameCurrencyValidator.class})
@Documented
public @interface SameCurrency
{
    String message() default " supplier currency and client currency are not same ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


