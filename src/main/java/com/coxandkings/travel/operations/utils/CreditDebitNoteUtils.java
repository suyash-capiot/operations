package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.creditDebitNote.CreditDebitNote;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CreditDebitNoteUtils {

    @Value("${refund.finance.credit_debit_note.get_credit_note}")
    private  String debitNoteUrl;

    @Autowired
    @Qualifier( value = "mDMToken" )
    private MDMToken mdmToken;

    private Logger logger=LogManager.getLogger(CreditDebitNoteUtils.class);

    public CreditDebitNote generate(CreditDebitNote creditDebitNote) throws OperationException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization",mdmToken.getToken());
        HttpEntity httpEntity= new HttpEntity(creditDebitNote, httpHeaders);
        RestTemplate template = RestUtils.getTemplate();
        ResponseEntity<CreditDebitNote> responseEntity=template.exchange(debitNoteUrl, HttpMethod.POST,httpEntity,CreditDebitNote.class);
        return responseEntity.getBody();
    }


    public ResponseEntity<CreditDebitNote> get(String id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization",mdmToken.getToken());
        HttpEntity httpEntity= new HttpEntity(httpHeaders);
        RestTemplate template = RestUtils.getTemplate();
        ResponseEntity<CreditDebitNote> responseEntity=template.exchange(debitNoteUrl+id, HttpMethod.GET,httpEntity,CreditDebitNote.class);
        return responseEntity;
    }

}
