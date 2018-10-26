package com.coxandkings.travel.operations.enums.doTicketing;

import org.springframework.util.StringUtils;

public enum AbsorbRetainScenarios {

    PASS_ON_TO_CLIENT("Pass on to Client"),
    RETAIN_BY_COMPANY("Retain By Company"),
    CHARGE_TO_CLIENT("Charge to Client"),
    ABSORB_BY_COMPANY("Absorb By Company");

    private String value;

    AbsorbRetainScenarios(String value) {this.value = value; }

    public String getValue() {
        return value;
    }

    public static AbsorbRetainScenarios fromString(String scenario) {
        AbsorbRetainScenarios absorbRetainScenarios = null;

        if(StringUtils.isEmpty(scenario)) {
            return absorbRetainScenarios;
        }

        for(AbsorbRetainScenarios diffAmountLowerScenario : AbsorbRetainScenarios.values()) {
            if(scenario.equalsIgnoreCase(diffAmountLowerScenario.getValue())) {
                absorbRetainScenarios = diffAmountLowerScenario;
            }
        }

        return absorbRetainScenarios;
    }

}
