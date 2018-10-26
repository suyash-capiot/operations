package com.coxandkings.travel.operations.generator;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IdGenerator {
    @Autowired
    private MDMRestUtils mdmRestUtils;
    @Value("${mdm.supplier-config}")
    private String supplierUrl;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    public String generateFSOId(String supplierId) {
        String documentType = "FS";
        String stateCode = null;
        String uid = gen().toString();

        String result = null;
        try {
            result = mdmRestUtils.getForObject(supplierUrl + "suppliers/" + supplierId, String.class);
        } catch (OperationException e) {
            e.printStackTrace();
        }
        stateCode = jsonObjectProvider.getAttributeValue(result, "$.contactInfo.supplierAddress.state", String.class);

        return documentType + stateCode + uid;
    }

    public String generatePSOId(String supplierId) {
        String documentType = "PS";
        String stateCode = null;
        String uid = gen().toString();

        String result = null;
        try {
            result = mdmRestUtils.getForObject(supplierUrl + "suppliers/" + supplierId, String.class);
        } catch (OperationException e) {
            e.printStackTrace();
        }
        stateCode = jsonObjectProvider.getAttributeValue(result, "$.contactInfo.supplierAddress.state", String.class);

        return documentType + stateCode + uid;
    }

    public String generatePSLId(String supplierId) {
        String documentType = "PSL";
        String stateCode = null;
        String uid = gen().toString();

        String result = null;
        try {
            result = mdmRestUtils.getForObject(supplierUrl + "suppliers/" + supplierId, String.class);
        } catch (OperationException e) {
            e.printStackTrace();
        }
        stateCode = jsonObjectProvider.getAttributeValue(result, "$.contactInfo.supplierAddress.state", String.class);

        return documentType + stateCode + uid;
    }

    public String generateFSLId(String supplierId) {
        String documentType = "FSL";
        String stateCode = null;
        String uid = gen().toString();

        String result = null;
        try {
            result = mdmRestUtils.getForObject(supplierUrl + "suppliers/" + supplierId, String.class);
        } catch (OperationException e) {
            e.printStackTrace();
        }
        stateCode = jsonObjectProvider.getAttributeValue(result, "$.contactInfo.supplierAddress.state", String.class);

        return documentType + stateCode + uid;
    }

    public Long gen() {
        // Random r = new Random(System.currentTimeMillis());
        Long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        return number;
    }
}
