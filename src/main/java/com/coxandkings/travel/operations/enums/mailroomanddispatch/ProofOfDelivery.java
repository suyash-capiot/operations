package com.coxandkings.travel.operations.enums.mailroomanddispatch;

public enum ProofOfDelivery {
    SIGNATURE_PAD("Signature Pad"),
    PDA("PDA"),
    MANUAL("Manual");

    private String value;

    ProofOfDelivery(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
