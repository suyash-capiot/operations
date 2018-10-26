package com.coxandkings.travel.operations.service.remarks;

import com.coxandkings.travel.operations.resource.remarks.RoleBasicInfo;
import com.coxandkings.travel.operations.resource.remarks.UserBasicInfo;

import java.util.List;

public interface MDMUserService {

    public List<UserBasicInfo> getAllUsers();

    public List<UserBasicInfo> getOpsUsersUsingRole( String roleName );

    public List<RoleBasicInfo> searchRoles(StringBuilder roleName);
}
