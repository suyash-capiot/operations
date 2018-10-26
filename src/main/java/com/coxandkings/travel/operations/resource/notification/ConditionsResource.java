package com.coxandkings.travel.operations.resource.notification;

import java.util.List;

public class ConditionsResource {

    private String _id;
    private String attribute;
    private String operatorList;
    private String operator;
    private String value;
    private List<SubConditions> subConditions;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<SubConditions> getSubConditions() {
        return subConditions;
    }

    public void setSubConditions(List<SubConditions> subConditions) {
        this.subConditions = subConditions;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getOperatorList() {
        return operatorList;
    }

    public void setOperatorList(String operatorList) {
        this.operatorList = operatorList;
    }
}
