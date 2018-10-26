package com.coxandkings.travel.operations.service.mailroomanddispatch.Impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.mailroomanddispatch.SupplierDetailsService;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class SupplierDetailsServiceImpl implements SupplierDetailsService {

    @Value(value = "${new-supplier-first-booking.mdm.supplierDetails}")
    private String urlToGetSupplierDetails;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Override
    public String getSupplerDetails() {
       String JsonString="";
        try {
          JsonString=mdmRestUtils.exchange(urlToGetSupplierDetails, HttpMethod.GET,String.class);
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return JsonString;

    }
}
