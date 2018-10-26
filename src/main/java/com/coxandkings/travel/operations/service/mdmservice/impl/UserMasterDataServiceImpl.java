package com.coxandkings.travel.operations.service.mdmservice.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.mdmservice.UserMasterDataService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
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

@Service
public class UserMasterDataServiceImpl  implements UserMasterDataService {

    @Value( value = "${mdm.user-management}")
    private String getUserURL;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonFilter;

    @Override
    public HashMap<String,String> getUserInfo(List<String> userIDList ) throws OperationException {

        HashMap<String,String> userNamesMap = new HashMap<>();

        try {
            JSONObject filterParams = new JSONObject();
            JSONArray userIDsList = new JSONArray();
            for( String anUserID : userIDList ) {
                if( anUserID != null && anUserID.trim().length() > 0 ) {
                    userIDsList.put( anUserID );
                }
            }
            filterParams.put("_id", userIDsList );
            String URL = getUserURL + "/user?filter=" + filterParams.toString();
            URI uri = UriComponentsBuilder.fromUriString( URL ).build().encode().toUri();
            ResponseEntity<String> responseEntity = mdmRestUtils.exchange( uri, HttpMethod.GET,null, String.class );
            String responseInString = responseEntity.getBody();
            List<String> userDetailsList = jsonFilter.getChildrenCollection( responseInString, "$.data", String.class );
            if( userDetailsList != null && userDetailsList.size() > 0 ) {
                for( String anUserProfile : userDetailsList )  {
                    String firstName = jsonFilter.getAttributeValue( anUserProfile, "$.userDetails.firstName", String.class);
                    String lastName = jsonFilter.getAttributeValue( anUserProfile, "$.userDetails.lastName", String.class);
                    String userID = jsonFilter.getAttributeValue( anUserProfile, "$._id", String.class);
                    userNamesMap.put( userID, firstName + " " + lastName );
                }
            }
        }
        catch( Exception e )    {
            OperationException anErr = new OperationException( "", e.getMessage() );
            e.printStackTrace();
        }
        return userNamesMap;
    }
}