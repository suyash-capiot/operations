package com.coxandkings.travel.operations.service.mdmservice.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.mdmservice.CompanyMasterDataService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyMasterDataServiceImpl implements CompanyMasterDataService {

    private static Logger logger = LogManager.getLogger( CompanyMasterDataServiceImpl.class );

    @Value(value = "${mdm.company}")
    private String getCompaniesURL;

    @Value(value = "${mdm.market}")
    private String getCompaniesMarketURL;

    @Value(value = "${get_salesoffice_details}")
    private String salesOfficeURL;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonFilter;

    @Override
    public Map<String,String> getCompanyNames( List<String> companyIDs ) throws OperationException {
        HashMap<String,String> companyNamesMap = new HashMap<>();

        try {
            JSONObject filterParams = new JSONObject();
            JSONArray companyIDsList = new JSONArray();
            for( String aClientID : companyIDs ) {
                if( aClientID != null && aClientID.trim().length() > 0 ) {
                    companyIDsList.put(aClientID);
                }
            }

            filterParams.put("_id", companyIDsList );

            String URL = this.getCompaniesURL + "?filter=" + filterParams.toString();
            logger.info( "URL to getCompanyNames: " + URL );
            URI uri = UriComponentsBuilder.fromUriString( URL ).build().encode().toUri();
            ResponseEntity<String> responseEntity = mdmRestUtils.exchange( uri, HttpMethod.GET,null, String.class );
            String responseInString = responseEntity.getBody();
            List<String> clientDetailsList = jsonFilter.getChildrenCollection( responseInString, "$.data", String.class );
            if( clientDetailsList != null && clientDetailsList.size() > 0 ) {
                for( String aCustomerProfile : clientDetailsList )  {
                    String companyName = jsonFilter.getAttributeValue( aCustomerProfile, "$.name", String.class);
                    String companyID = jsonFilter.getAttributeValue( aCustomerProfile, "$._id", String.class);
                    companyNamesMap.put( companyID, companyName );
                }
            }
        }
        catch( Exception e )    {
            OperationException anErr = new OperationException( "", e.getMessage() );
            e.printStackTrace();
        }
        return companyNamesMap;
    }

    @Override
    public String getCompanyDetails(String companyID) throws OperationException {
        String companyDetails = null;

        try {
            JSONObject filterParams = new JSONObject();
            JSONArray companyIDsList = new JSONArray();
            if( companyID != null && companyID.trim().length() > 0 ) {
                companyIDsList.put(companyID);
            }

            filterParams.put("_id", companyIDsList );

            String URL = this.getCompaniesURL + "?filter=" + filterParams.toString();
            URI uri = UriComponentsBuilder.fromUriString( URL ).build().encode().toUri();
            logger.info( "URL to getCompanyDetails: " + URL );
            ResponseEntity<String> responseEntity = mdmRestUtils.exchange( uri, HttpMethod.GET,null, String.class );
            companyDetails = responseEntity.getBody();
            companyDetails = jsonFilter.getChildJSON( companyDetails, "$.data[0]" );
        }
        catch( Exception e )    {
            OperationException anErr = new OperationException( "", e.getMessage() );
            e.printStackTrace();
        }

        return companyDetails;
    }

    @Override
    public String getDivisionDetails(String companyID, String buID) throws OperationException {

        JSONObject filterParams = new JSONObject();
        filterParams.put("companyId", companyID);


        // define params to return!!
        String selectParams = "&select=divisionName,division,distributionChannel,distributionChannelName,address.location,name";

        String URL = this.salesOfficeURL + filterParams.toString() + selectParams;

        URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
        ResponseEntity<String> responseEntity = mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
        String divisionData = responseEntity.getBody();
        divisionData = jsonFilter.getChildJSON(divisionData, "$.data[0]");

        return divisionData;
    }

    @Override
    public String getCompanyMarketNameByID(String companyMarketId){

        String companyName = "";
        if(companyMarketId==null || companyMarketId.isEmpty())
            return companyName;

        String URL = this.getCompaniesMarketURL + "/" + companyMarketId;

        URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
        logger.info( "URL to getCompanyMarketNameByID: " + URL );
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
            String responseEntityBody = responseEntity.getBody();
            companyName = new JSONObject(responseEntityBody).optString("name");
        } catch (Exception e) {
            logger.warn("Error while fetching company Market Name From: " + URL);
            e.printStackTrace();
        }
        return companyName;
    }
}
