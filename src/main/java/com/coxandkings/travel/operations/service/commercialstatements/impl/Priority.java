/**
 * 
 */
package com.coxandkings.travel.operations.service.commercialstatements.impl;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Priority {
	 int value();
}
