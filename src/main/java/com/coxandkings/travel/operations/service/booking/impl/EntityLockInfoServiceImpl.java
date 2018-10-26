package com.coxandkings.travel.operations.service.booking.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.booking.EntityLockResource;
import com.coxandkings.travel.operations.service.booking.EntityLockInfoService;
import com.coxandkings.travel.operations.service.mdmservice.UserMasterDataService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class EntityLockInfoServiceImpl implements EntityLockInfoService {

    @Autowired
    private UserService userService;

    @Value(value = "${booking_engine.lock.acquire_lock}")
    private String acquireLockUrl;

    @Value(value = "${booking_engine.lock.release_lock}")
    private String releaseLockUrl;

    @Autowired
    private UserMasterDataService userMasterDataService;

    @Override
    public String acquireLock(EntityLockResource acquireLock) throws OperationException {
        acquireLock.setUserId(userService.getLoggedInUserId());
        acquireLock.setAppId("Operations");
        acquireLock.setSessionId("");

        ResponseEntity<String> acquireLockResponseEntity = null;
        try {
            acquireLockResponseEntity = RestUtils.postForEntity(acquireLockUrl, acquireLock, String.class);
            JSONObject acquireLockEntity = new JSONObject(acquireLockResponseEntity.getBody());

            if (!(acquireLockEntity.isNull("errorMessage"))) {
                String userId = acquireLockEntity.getString("acquiredByUser");
                List<String> userListArray = new ArrayList<String>();
                userListArray.add(userId);
                HashMap<String, String> userInfoMap = userMasterDataService.getUserInfo(userListArray);
                String userName = userInfoMap.get(userId);
                if(StringUtils.isEmpty(userInfoMap.get(userId))) {
                    acquireLockEntity.put("acquiredByUser", "Undefined User");
                }else {
                    acquireLockEntity.put("acquiredByUser", userName);
                }
                return acquireLockEntity.toString();
            }
        } catch (RestClientException e) {
            throw new OperationException(String.format("Unable to acquire lock for OrderId <%s> ", acquireLock.getOrderId()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return acquireLockResponseEntity.getBody();
    }

    @Override
    public String releaseLock(EntityLockResource releaseLock) throws OperationException {

        releaseLock.setUserId(userService.getLoggedInUserId());
        releaseLock.setAppId("Operations");
        releaseLock.setSessionId("");
        ResponseEntity<String> acquireLockResponseEntity = null;

        try {
            acquireLockResponseEntity = RestUtils.postForEntity(releaseLockUrl, releaseLock, String.class);
        } catch (RestClientException e) {
            throw new OperationException(String.format("Unable to release lock for OrderId <%s> ", releaseLock.getOrderId()));
        }

        return acquireLockResponseEntity.getBody();

    }
}
