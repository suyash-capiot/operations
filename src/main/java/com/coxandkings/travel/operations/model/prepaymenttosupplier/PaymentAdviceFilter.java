package com.coxandkings.travel.operations.model.prepaymenttosupplier;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class PaymentAdviceFilter {

    @JsonProperty("supplierId")
    private String supplierId;


    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public static String getUrl( PaymentAdviceFilter paymentAdviceFilter ) {
        String url = null;
        try {
            ObjectMapper aMapper = new ObjectMapper( );
            url = aMapper.writeValueAsString( paymentAdviceFilter );
            return url;
        } catch ( Exception e ) {
            e.printStackTrace( );
            return url;
        }
    }
}
