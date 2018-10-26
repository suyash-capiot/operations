package com.coxandkings.travel.operations.zmock.controller;

import com.coxandkings.travel.operations.zmock.resource.ClientFinancialBackgroundInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/financeData")
public class FinancialBackgroundInfoController {

    @Autowired
    ObjectMapper mapper;
    TypeReference<List<ClientFinancialBackgroundInfo>>
            typeReference = new TypeReference<List<ClientFinancialBackgroundInfo>>(){};
    Logger logger = LogManager.getLogger(FinancialBackgroundInfoController.class);
    InputStream inputStream;


    @GetMapping(value = "/v1/getBackgroundInfoByClientId/{clientId}")
    public ClientFinancialBackgroundInfo refundClaimByClaimNumber(@PathVariable("clientId") String clientId) throws IOException {
        inputStream = FinancialBackgroundInfoController.class.getResourceAsStream("/zmock/json/financialBackgroundInfo.json");
        List<ClientFinancialBackgroundInfo>
                clientFinancialBackgroundInfoList = mapper.readValue(inputStream, typeReference);
        return  clientFinancialBackgroundInfoList.stream().filter(clientFinancialBackgroundInfo -> clientFinancialBackgroundInfo.getCliendId().equals(clientId)).findFirst().get();
    }
}
