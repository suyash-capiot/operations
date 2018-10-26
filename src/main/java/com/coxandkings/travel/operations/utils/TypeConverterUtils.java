package com.coxandkings.travel.operations.utils;

import java.math.BigDecimal;
import java.math.MathContext;

public class TypeConverterUtils {

    public static BigDecimal convertToBigDecimal(String strVal, int dftVal) {
        return convertToBigDecimal(strVal, 6, dftVal);
    }

    public static BigDecimal convertToBigDecimal(String strVal, int precision, int dftVal) {
        if (isStringNullOrEmpty(strVal)) {
            return new BigDecimal(dftVal).setScale(1,BigDecimal.ROUND_HALF_UP);
        }

        try {
            return new BigDecimal(strVal, new MathContext(precision)).setScale(1,BigDecimal.ROUND_HALF_UP);
        }
        catch (NumberFormatException nfx) {
            return new BigDecimal(dftVal).setScale(1,BigDecimal.ROUND_HALF_UP);
        }
    }
    public static boolean isStringNullOrEmpty(String str) {
        return (str == null || str.isEmpty());
    }
/*
    public static double round(double value, int numberOfDigitsAfterDecimalPoint) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,
                BigDecimal.ROUND_HALF_UP);
        return bigDecimal.doubleValue();
    }*/
}
