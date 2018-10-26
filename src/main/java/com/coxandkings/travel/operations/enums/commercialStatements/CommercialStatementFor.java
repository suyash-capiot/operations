package com.coxandkings.travel.operations.enums.commercialStatements;

public enum CommercialStatementFor {
        CLIENT("Client"),
        SUPPLIER("Supplier");

        private String name;
        CommercialStatementFor(String name){
            this.name=name;
        }
        public String getName() {
            return name;
        }
    }

