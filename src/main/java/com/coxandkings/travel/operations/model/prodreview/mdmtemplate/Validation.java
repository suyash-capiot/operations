package com.coxandkings.travel.operations.model.prodreview.mdmtemplate;

import java.util.List;

public class Validation {

    private String validationId;

    private String valueRequired;

    private String valueNotRequired;

    private List<String> values;

    public String getValueRequired() {
        return valueRequired;
    }

    public void setValueRequired(String valueRequired) {
        this.valueRequired = valueRequired;
    }

    public String getValueNotRequired() {
        return valueNotRequired;
    }

    public void setValueNotRequired(String valueNotRequired) {
        this.valueNotRequired = valueNotRequired;
    }

    public String getValidationId() {
        return validationId;
    }

    public void setValidationId(String validationId) {
        this.validationId = validationId;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
