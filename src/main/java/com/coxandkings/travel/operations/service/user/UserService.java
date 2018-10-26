package com.coxandkings.travel.operations.service.user;

import com.coxandkings.travel.operations.enums.user.UserType;
import com.coxandkings.travel.operations.exceptions.InvalidTokenException;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.user.MdmUserInfo;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.resource.user.role.Role;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Set;

public interface UserService {

    MdmUserInfo assertToken(String jwtToken) throws OperationException;

    OpsUser getOpsUser(MdmUserInfo mdmUser);

    OpsUser getOpsUser(MdmUserInfo mdmUser, String jwtToken);

    MdmUserInfo createUserDetailsFromToken(String authToken) throws OperationException;

    Set<Role> getAllPermission(String authToken, List<String> roleIds);

    List<GrantedAuthority> preparePermissionString(Set<Role> roles);

    String getUserType(String roleName);

    default  String getLoggedInUserId() {
        String userID = null;
        try {
            UsernamePasswordAuthenticationToken userPwdAuth = (UsernamePasswordAuthenticationToken)
                    SecurityContextHolder.getContext().getAuthentication();

            if(userPwdAuth == null){
                return null;
            }

            OpsUser loggedInUser = (OpsUser) userPwdAuth.getPrincipal();
            userID = loggedInUser.getUserID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userID;
    }

    default String getLoggedInUserToken() {
        String token = null;
        try {
            UsernamePasswordAuthenticationToken userPwdAuth = (UsernamePasswordAuthenticationToken)
                    SecurityContextHolder.getContext().getAuthentication();

            if(userPwdAuth == null){
                return null;
            }

            OpsUser loggedInUser = (OpsUser) userPwdAuth.getPrincipal();
            token = loggedInUser.getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    default OpsUser getLoggedInUser() {
        OpsUser loggedInUser = null;
        try {
            UsernamePasswordAuthenticationToken userPwdAuth = (UsernamePasswordAuthenticationToken)
                    SecurityContextHolder.getContext().getAuthentication();

            if(userPwdAuth == null){
                return null;
            }
            loggedInUser = (OpsUser) userPwdAuth.getPrincipal();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return loggedInUser;
    }

    UserType getOpsUserType(String roleName );

    OpsUser getSystemUser();

    String getSystemUserIdFromMDMToken();

    UserType getLoggedInUserType();

    JSONObject getUserDetailsByCompanyId(String jwtToken, String id) throws InvalidTokenException;
}
