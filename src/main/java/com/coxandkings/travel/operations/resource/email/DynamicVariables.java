package com.coxandkings.travel.operations.resource.email;

public class DynamicVariables {

    private String name ;
    private String value ;

    public DynamicVariables() {
    }

    public DynamicVariables(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {


        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
