package com.coxandkings.travel.operations.service.fullcancellation.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.fullcancellation.clientKPI.ClientKPI;
import com.coxandkings.travel.operations.resource.fullcancellation.clientKPI.KpiDefinition;
import com.coxandkings.travel.operations.service.fullcancellation.MDMService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service("mDMService")
public class MDMServiceImpl implements MDMService {

    private static final Logger logger = LogManager.getLogger(MDMServiceImpl.class);

    @Value("${mdm.common.supplier.supplier-by-id}")
    private String supplierUrl;

    @Value("${mdm.common.client.b2b_client}")
    private String b2BClientURL;

    @Value("${mdm.common.client.client_kpi}")
    private String clientKPIURL;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Override
    public String getSupplierInfo(String supplierId) throws OperationException {
        return mdmRestUtils.getResponseJSON(supplierUrl + supplierId);

    }

    public String getClientEmail(String clientId) throws OperationException {
        return jsonObjectProvider.getAttributeValue(mdmRestUtils.getResponseJSON(b2BClientURL + clientId), "$.adminUserDetails.users[0].email", String.class);
    }

    public String getClientName(String clientId) throws OperationException {
        return jsonObjectProvider.getAttributeValue(mdmRestUtils.getResponseJSON(b2BClientURL + clientId), "$.clientProfile.clientDetails.clientName", String.class);
    }

    public KpiDefinition getKPIDate(String clientId) throws OperationException {
        String kpiResponse = null;
        String filter = "?filter={\"entityId\":\"" + clientId + "\"}";

        try {
            logger.info(clientKPIURL.concat(filter));

            URI url = UriComponentsBuilder.fromUriString(clientKPIURL.concat(filter)).build().encode().toUri();
            kpiResponse = mdmRestUtils.exchange(url, HttpMethod.GET, String.class);

        } catch (HttpClientErrorException he) {
            logger.error(" 408 Request Timeout for MDM server", he);
            throw new OperationException(" 408 Request Timeout for MDM server");
        } catch (Exception e) {
            logger.error("Not able to get KPI from MDM", e);
            throw new OperationException("KPI is not configured for client " + clientId);
        }
        List<ClientKPI> clientKPIResources =   jsonObjectProvider.getChildrenCollection(kpiResponse, "$.data.*", ClientKPI.class);
        if (null == clientKPIResources || clientKPIResources.size()==0) {
            logger.error("KPI is not configured for client " + clientId);
            throw new OperationException("KPI is not configured for client " + clientId);
        }
        List<KpiDefinition> kpiDefinitions = clientKPIResources.get(0).getKpiDefinition();
        for(KpiDefinition kpiDefinition:kpiDefinitions){
            if ("TAT".equalsIgnoreCase(kpiDefinition.getKpiMeasure().getMeasure())) {
                return kpiDefinition;
            }

        }

        return null;

    }


}
