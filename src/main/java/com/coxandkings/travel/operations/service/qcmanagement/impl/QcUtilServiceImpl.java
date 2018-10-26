package com.coxandkings.travel.operations.service.qcmanagement.impl;

import com.coxandkings.travel.operations.service.qcmanagement.QcUtilService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class QcUtilServiceImpl implements QcUtilService {

    @Value(value = "${brms.username}")
    private String username;

    @Value(value = "${brms.password}")
    private String password;


    /**
     * This method is used to create Basic Authorization Headers for brms api call
     *
     * @param jsonRes
     * @return
     */
    @Override
    public HttpEntity<String> createHeader(String jsonRes) {
        //getting resOutput from brms
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(jsonRes, headers);
    }
}
