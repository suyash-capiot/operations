package com.coxandkings.travel.operations.service.mailroomanddispatch.Impl;

import com.coxandkings.travel.operations.service.mailroomanddispatch.SupplierDetailsService;
import com.coxandkings.travel.operations.service.mailroomanddispatch.SupplierNamesService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class SupplierNamesServiceImpl implements SupplierNamesService {

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private SupplierDetailsService supplierDetailsService;

    @Value(value = "${mailRoomAndDispatch.path-expression.supplier.supplierNames}")
    private String supplierNamesPathExpression;

    @Override
    public String getSupplierNames() {
        String mdmSupplierDetails= "";
        mdmSupplierDetails = supplierDetailsService.getSupplerDetails();
        String supplierNames = "";
       // List<String> supplierNames = new ArrayList<>();

        if (mdmSupplierDetails != null)
        {
            supplierNames = JsonPath.parse( mdmSupplierDetails ).read( supplierNamesPathExpression ).toString();

          // supplierNames= jsonObjectProvider.getChildrenCollection(mdmSupplierDetails,supplierNamesPathExpression,String.class);
        }
        return supplierNames;
    }
}
