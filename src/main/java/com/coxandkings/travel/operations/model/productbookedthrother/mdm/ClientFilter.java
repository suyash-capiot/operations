package com.coxandkings.travel.operations.model.productbookedthrother.mdm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class ClientFilter {

    @JsonProperty("clientDetails.clientId")
    private String clientId;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public static String getUrl(ClientFilter clientFilter ) {
        String url = null;
        try {
            ObjectMapper aMapper = new ObjectMapper( );
            url = aMapper.writeValueAsString( clientFilter );
            return url;
        } catch ( Exception e ) {
            e.printStackTrace( );
            return url;
        }
    }
}
