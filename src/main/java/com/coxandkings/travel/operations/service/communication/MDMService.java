package com.coxandkings.travel.operations.service.communication;

import com.coxandkings.travel.operations.exceptions.OperationException;

import java.util.List;

public interface MDMService {
    List<String> searchSupplierEmail(String criteria) throws OperationException;

    //$.data[:1].adminUserDetails.users[0].email
    List<String> searchB2BEmail(String criteria) throws OperationException;

    //http://10.24.2.5:10010/usermgmt/v1/user?filter={userDetails.email":"/a/"}
    List<String> searchB2EEmail(String criteria) throws OperationException;

    List<String> searchB2CEmail(String criteria) throws OperationException;
}
