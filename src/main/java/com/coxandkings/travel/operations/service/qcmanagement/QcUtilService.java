package com.coxandkings.travel.operations.service.qcmanagement;

import org.springframework.http.HttpEntity;

public interface QcUtilService {
    public HttpEntity<String> createHeader(String jsonRes);
}
