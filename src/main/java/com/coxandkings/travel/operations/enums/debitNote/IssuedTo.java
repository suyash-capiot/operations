package com.coxandkings.travel.operations.enums.debitNote;

public enum IssuedTo {
    CLIENT("Client"),
    SUPPLIER("Supplier");
    private String name;
    IssuedTo(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
