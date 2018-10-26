package com.coxandkings.travel.operations.service.mdmservice;

import com.coxandkings.travel.operations.exceptions.OperationException;

import java.util.HashMap;
import java.util.List;

public interface UserMasterDataService {

    public HashMap<String,String> getUserInfo(List<String> userIDList ) throws OperationException;
}
