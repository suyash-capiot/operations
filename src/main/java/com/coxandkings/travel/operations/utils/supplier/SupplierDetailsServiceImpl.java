package com.coxandkings.travel.operations.utils.supplier;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service("supplierDetailsService")
public class SupplierDetailsServiceImpl implements SupplierDetailsService {

    @Value(value = "${mdm.common.supplier.supplier-by-id}")
    private String getSupplierDetailsByIdUrl;

    @Value(value = "${mdm.common.supplier.credentials}")
    private String getSupplierCredDetails;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Override
    public CommunicationType getSupplierCommunicationTypeBySupplierId(String supplierID) throws OperationException {
        if (supplierID == null || supplierID.isEmpty() == true) {
            throw new OperationException(Constants.ER39);
        }
        String communicationType = null;
        try {
            String supplierDetails = mdmRestUtils.getResponseJSON(getSupplierDetailsByIdUrl + supplierID);
            if (supplierDetails != null) {
                //TODO: Real
                // communicationType = jsonObjectProvider.getChildJSON(supplierDetails, "$.supplier.communicationType");

                //Dummy
                communicationType = "email";
                CommunicationType eCommunicationType = CommunicationType.fromString(communicationType);
                return eCommunicationType;
            }
        } catch (OperationException e) {
            e.printStackTrace();
        }
        //By Default
        return CommunicationType.EMAIL;
    }

    @Override
    public String getSupplierDetails(String supplierID) throws OperationException {
        if (supplierID == null || supplierID.isEmpty() == true) {
            throw new OperationException(Constants.ER39);
        }
        String supplierDetails = null;
        try {
            supplierDetails = mdmRestUtils.getResponseJSON(getSupplierDetailsByIdUrl + supplierID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplierDetails;
    }

    public JSONObject getSupplierCredentialDetails(String credId){
        if (credId == null || credId.isEmpty()) {
            return null;
        }
        String supplierDetails = null;
        JSONObject details = null;
        JSONObject filter = new JSONObject();
        filter.put("_id", credId);
        try {
            supplierDetails = mdmRestUtils.exchange(getSupplierCredDetails +"/"+credId, HttpMethod.GET, String.class);
            JSONObject data = new JSONObject(supplierDetails);
            details = data.length()!=0 ? data : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }



    @Override
    public CommunicationType getSupplierCommunicationType(String supplierDetails) throws OperationException {
        if (supplierDetails == null || supplierDetails.isEmpty() == true) {
            throw new OperationException(Constants.ER39);
        }
        String communicationType = null;
        try {
            //TODO: Real
            // communicationType = jsonObjectProvider.getChildJSON(supplierDetails, "$.supplier.communicationType");

            //Dummy
            communicationType = "email";
            CommunicationType eCommunicationType = CommunicationType.fromString(communicationType);
            return eCommunicationType;

        } catch (Exception e) {
            e.printStackTrace();
        }
        //By Default
        return CommunicationType.EMAIL;
    }


}
