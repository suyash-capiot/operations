package com.coxandkings.travel.operations.service.user.impl;

import com.coxandkings.travel.operations.enums.user.UserType;
import com.coxandkings.travel.operations.exceptions.InvalidTokenException;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.user.Company;
import com.coxandkings.travel.operations.resource.user.MdmUserInfo;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.resource.user.UserDetails;
import com.coxandkings.travel.operations.resource.user.role.Role;
import com.coxandkings.travel.operations.service.booking.impl.OpsBookingServiceImpl;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static org.apache.logging.log4j.Logger log = LogManager.getLogger(OpsBookingServiceImpl.class);

    @Value("${user_management.mdm.user_token_validation_url}")
    private String mdmTokenValidationUrl;
    @Value(value = "${mdm.common.get.roles}")
    private String roleUrl;

    @Value("${operation.security.secretKey}")
    private String secretKey;

    @Value("${operation.security.tokenHeader}")
    private String authorizationKey;

    @Value("${user_management.ops_user_type.OpsUser}")
    private String opsUser;

    @Value("${user_management.ops_user_type.OpsApprovalUser}")
    private String approverUser;

    @Value("${user_management.ops_user_type.OpsSupplierExtranet}")
    private String OpsSupplierExtranet;

    @Value("${user_management.ops_user_type.OpsMarketingUser}")
    private String OpsMarketingUser;

    @Value("${user_management.ops_user_type.OpsProductUser}")
    private String OpsProductUser;

    @Value("${user_management.ops_user_type.OpsEmailMonitorUser}")
    private String OpsEmailMonitorUser;

    @Autowired
    @Qualifier("mDMToken")
    private MDMToken mdmToken;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public MdmUserInfo assertToken(String jwtToken) throws OperationException {
        log.info("In side UserServiceImpl of assertToken method");
        MdmUserInfo mdmUserInfo = null;
        OpsUser opsUser = null;
        if (StringUtils.isEmpty(jwtToken)) {
            log.error("Token is empty");
            throw new OperationException("Please Add the Authorization Header");
        }
      /*  if (validateToken(jwtToken) || true) {
            mdmUserInfo = createUserDetailsFromToken(jwtToken);
        }*/
        mdmUserInfo = createUserDetailsFromToken(jwtToken);
        return mdmUserInfo;

    }

    @Override
    public MdmUserInfo createUserDetailsFromToken(String authToken) throws OperationException {
        MdmUserInfo mdmUser = null;
        try {
            String token = null;
            Set<Role> roles = null;
            String username = null;
            List<GrantedAuthority> grantedAuthorities = null;
            //TODo
            if (authToken != null && authToken.startsWith("Bearer ")) {
                token = authToken.substring(7);
                if (StringUtils.isEmpty(token)) {
                    throw new OperationException("Token is missing");
                }
                Claims claims = null;
                claims = Jwts.parser()
                        .setSigningKey(SignatureAlgorithm.HS256.getValue())
                        .setSigningKey(secretKey.getBytes("UTF-8"))
                        .parseClaimsJws(token)
                        .getBody();

                mdmUser = objectMapper.convertValue(claims, MdmUserInfo.class);
                /*List<Company> companies = mdmUser.getUser().getUserDetails().getCompanies();
                username = mdmUser.getUser().getId();
                List<String> roleIds = new ArrayList<>();
                if (companies == null) {
                    throw new RuntimeException("Company is null");
                }
                //ToDo uncomment to get all the privileges
           *//* for (Company company : companies) {
                roleIds.add(company.getRoleName());
            }*//*
                //  roles = getAllPermission(authToken, roleIds);
                //  grantedAuthorities = preparePermissionString(roles);
                //   grantedAuthorities= companies.stream().map(company -> new SimpleGrantedAuthority(company.getRoleName())).collect(Collectors.toList());
                grantedAuthorities = companies.stream().map(company -> company.getRoleName()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());*/
            }
        } catch (Exception e) {
            log.error("Error occurrred while parsing token - probably a Invalid or Expired Token: ");
            throw new OperationException(Constants.OPS_ERR_10300);
        }
        return mdmUser;
    }

    @Override
    public OpsUser getOpsUser(MdmUserInfo mdmUser, String token) {

        String userName = mdmUser.getUser().getUserDetails().getFirstName();
        UserDetails userDetails = mdmUser.getUser().getUserDetails();
        log.info(mdmUser.getUser().getUserDetails().getFirstName());
        List<Company> companies = mdmUser.getUser().getUserDetails().getCompanies();
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        List<Role> roles = new ArrayList<>();
        if (companies != null) {
            for (Company company : companies) {
                roles = company.getRoles();
                if (roles != null) {
                    for (Role role : roles) {

                        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));

                    }
                }
            }
        }
     /*   List<SimpleGrantedAuthority> simpleGrantedAuthorities = companies.stream().
                map(company -> company.getRoles().stream().map()).map(SimpleGrantedAuthority::new).
                collect(Collectors.toList());*/
        OpsUser anUser = new OpsUser(userName, null, simpleGrantedAuthorities, token);

        anUser.setBU(userDetails.getBU());
        anUser.setSBU(userDetails.getSBU());
        anUser.setCompanyId(mdmUser.getCurrentCompany().getId());
        anUser.setCompanyName(mdmUser.getCurrentCompany().getName());
        anUser.setCompanyGroupId(mdmUser.getCurrentCompany().getGc().getId());
        anUser.setCompanyGroupName(mdmUser.getCurrentCompany().getGc().getName());
        anUser.setGroupOfCompanyId(mdmUser.getCurrentCompany().getGoc().getId());
        anUser.setGroupOfCompanyName(mdmUser.getCurrentCompany().getGoc().getName());
        //TODO: To set branch

        anUser.setRoles(roles);
        anUser.setUserID(mdmUser.getUser().getId());
        return anUser;
    }

    @Override
    public OpsUser getOpsUser(MdmUserInfo mdmUser) {

        String userName = mdmUser.getUser().getUserDetails().getFirstName();
        log.info(mdmUser.getUser().getUserDetails().getFirstName());
        List<Company> companies = mdmUser.getUser().getUserDetails().getCompanies();
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        List<Role> roles = null;
        if (companies != null) {
            for (Company company : companies) {
                roles = company.getRoles();
                if (roles != null) {
                    for (Role role : roles) {

                        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
                    }
                }
            }
        }

        OpsUser anUser = new OpsUser(userName, null, simpleGrantedAuthorities);
        // anUser.setUserRole(companies.stream().map(company -> company.getRoleName()).findFirst().get());
        anUser.setUserID(mdmUser.getUser().getId());
        return anUser;
    }


    private HttpHeaders createHttpHeaders(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(this.authorizationKey, authToken);
        return headers;
    }

    private boolean validateToken(String authToken) throws InvalidTokenException {
        String theUrl = this.mdmTokenValidationUrl;
        RestTemplate restTemplate = RestUtils.getTemplate();
        log.info(" in side validateToken method");
        try {
            HttpHeaders headers = createHttpHeaders(authToken);
            HttpEntity<String> entity = new HttpEntity<String>(null, headers);
            log.info(" before calling mdm validate token method");
            log.info("theUrl " + theUrl);
            long start = System.currentTimeMillis();


            ResponseEntity<Object> response = restTemplate.exchange(theUrl, HttpMethod.GET, entity, Object.class);
            long elapsedTime = System.currentTimeMillis() - start;
            log.info("MDM Validation taken time:" + elapsedTime);
            log.info("Result - status (" + response.getStatusCode() + ") has body: " + response.hasBody());
        } catch (Exception ex) {
            log.info("UserServiceImpl.validateToken() failed");
            throw new InvalidTokenException(ex.getMessage());
        }
        return true;
    }

    @Override
    public Set<Role> getAllPermission(String authToken, List<String> roleIds) {
        String theUrl = roleUrl;
        Set<Role> roles = new HashSet<>();
        for (String roleId : roleIds) {
            RestTemplate restTemplate = RestUtils.getTemplate();
            ResponseEntity<Role> response = null;
            try {
                HttpHeaders headers = createHttpHeaders(authToken);
                HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
                response = restTemplate.exchange(theUrl + roleId, HttpMethod.GET, entity, Role.class);
                roles.add(response.getBody());
            } catch (Exception eek) {
                eek.printStackTrace();
                // thow operationException(code,message,eek);
                return null;
            }
        }


        return roles;
    }

    @Override
    public List<GrantedAuthority> preparePermissionString(Set<Role> roles) {
        //roleName_businessProcess_functionName_screenName_actionName_permission
        /* List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
         if (roles != null) {
             for (Role role : roles) {
                 StringBuilder stringBuilder = new StringBuilder();
                 role.getRoleName();
                 stringBuilder.append(role.getRoleName());
                 List<Permission> permissions = role.getPermissions();
                 if (permissions != null) {
                     for (Permission permission : permissions) {
                         permission.getBusinessProcess();
                         stringBuilder.append("_" + permission.getBusinessProcess());
                         List<Function> functions = permission.getFunctions();
                         if (functions != null) {
                             for (Function function : functions) {
                                 function.getFunctionName();
                                 stringBuilder.append("_" + function.getFunctionName());
                                 List<Screen> screens = function.getScreens();
                                 for (Screen screen : screens) {
                                     screen.getScreenName();
                                     stringBuilder.append("_" + screen.getScreenName());
                                     List<Action> actions = screen.getActions();
                                     if (actions != null) {
                                         for (Action action : actions) {
                                             action.getActionName();
                                             stringBuilder.append("_" + action.getActionName());
                                             grantedAuthorities.add(new SimpleGrantedAuthority(stringBuilder.toString()));
                                             System.out.println(stringBuilder.toString());
                                         }
                                     }
                                 }
                             }
                         }
                     }
                 }
             }
         }
*/
        return null;
    }

    @Override
    public String getUserType(String roleName) {
        if (roleName.equalsIgnoreCase("OpsUser")) {
            return this.opsUser;
        } else if (roleName.equalsIgnoreCase("OpsApprovalUser")) {
            return this.approverUser;
        } else if (roleName.equalsIgnoreCase("OpsSupplierExtranet")) {
            return this.OpsSupplierExtranet;
        } else if (roleName.equalsIgnoreCase("Marketing User")) {
            return this.OpsMarketingUser;
        } else if (roleName.equalsIgnoreCase("Product User")) {
            return this.OpsProductUser;
        } else if (roleName.equalsIgnoreCase("OpsEmailMonitorUser")) {
            return this.OpsEmailMonitorUser;
        }
        return null;
    }

    @Override
    public UserType getOpsUserType(String roleName) {
        UserType aUsrType = UserType.fromString(roleName);
        if (aUsrType == null) {
            aUsrType = UserType.OPS_USER;
        }
        return aUsrType;
    }

    @Override
    public OpsUser getSystemUser() {
        OpsUser aOpsUser = null;
        try {
            MdmUserInfo mdmUser = createUserDetailsFromToken(mdmToken.getToken());
            aOpsUser = getOpsUser(mdmUser);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return aOpsUser;
    }

    @Override
    public String getSystemUserIdFromMDMToken() {
        try {
            MdmUserInfo mdmUser = createUserDetailsFromToken(mdmToken.getToken());
            OpsUser aOpsUser = getOpsUser(mdmUser);
            String userID = aOpsUser.getUserID();
            return userID;
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
        return null;
    }

    public UserType getLoggedInUserType() {
        UserType usrType = null;
        Optional<String> opsRole=null;
        String userRole =null;
        try {
            UsernamePasswordAuthenticationToken userPwdAuth = (UsernamePasswordAuthenticationToken)
                    SecurityContextHolder.getContext().getAuthentication();
            OpsUser loggedInUser = (OpsUser) userPwdAuth.getPrincipal();
            List<Role> roles = loggedInUser.getRoles();
            if(roles!=null){
                opsRole = roles.stream().filter(role -> role.getRoleName().equalsIgnoreCase("OpsUser")).map(role -> role.getRoleName()).findFirst();
            }
            if(opsRole.isPresent()){
                userRole = opsRole.get();
            }

            usrType = UserType.fromString(userRole.trim());
        } catch (Exception e) {
            log.error("Exception in getLoggedInUserType is " , e);
        }
        return usrType;
    }

    @Override
    public JSONObject getUserDetailsByCompanyId(String jwtToken, String id) throws InvalidTokenException {

        RestTemplate restTemplate = new RestTemplate();
        log.info(" in side getUserDetailsByCompanyId method");
        JSONObject jsonObject = null;
        try {
            HttpHeaders headers = createHttpHeaders(jwtToken);
            HttpEntity<String> entity = new HttpEntity<String>(null, headers);
            log.info(" before calling mdm validate token method");

            ResponseEntity<Object> response = restTemplate.exchange("http://10.25.6.103:10010/usermgmt/v1/default-company-sync/" + id, HttpMethod.GET, entity, Object.class);
            log.info("Result - status (" + response.getStatusCode() + ") has body: " + response.hasBody());
            jsonObject = new JSONObject();
            jsonObject.put("currentCompanyUserDetail", response.getBody());
            return jsonObject;


        } catch (Exception ex) {
//            log.debug(ex.getMessage());
//            ex.printStackTrace();
            log.info("UserServiceImpl.validateToken() failed");
            throw new InvalidTokenException(ex.getMessage());
        }
    }

}
