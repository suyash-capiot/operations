package com.coxandkings.travel.operations.controller.user;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.user.MdmUserInfo;
import com.coxandkings.travel.operations.resource.user.OpsLoggedInUserDetails;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/operations")
@CrossOrigin("*")
public class UserController {

    @Value("${operation.security.tokenHeader}")
    private String tokenHeader;
    private static Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public HttpEntity<OpsLoggedInUserDetails> assertToken(HttpServletRequest request) throws OperationException {
        // try {
        logger.info("In assertToken of UserController ");
        String jwtToken = request.getHeader(this.tokenHeader);
        logger.info("jwtToken->" + jwtToken);
        MdmUserInfo mdmUserInfo = userService.assertToken(jwtToken);
        OpsUser opsUser = userService.getOpsUser(mdmUserInfo);
        opsUser.setUserID(mdmUserInfo.getUser().getId());

        OpsLoggedInUserDetails loggedInUser = new OpsLoggedInUserDetails(); //for UI to build screen
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(opsUser, null, opsUser.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        logger.info("authenticated user " + opsUser.getUsername() + ", setting security context");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>(loggedInUser, HttpStatus.OK);

    }

    @GetMapping("/companies/{id}")
    public HttpEntity<JSONObject> getDefaultCompanyDetails(HttpServletRequest request, @PathVariable String id) throws OperationException {

        logger.info("In assertToken of UserController ");
        String jwtToken = request.getHeader(this.tokenHeader);
        logger.info("jwtToken->" + jwtToken);
        JSONObject userCompanyDetails = userService.getUserDetailsByCompanyId(jwtToken, id);
        MdmUserInfo mdmUserInfo = userService.createUserDetailsFromToken(jwtToken);
        userCompanyDetails.put("mdmUserInfo", mdmUserInfo);
        return new ResponseEntity<>(userCompanyDetails, HttpStatus.OK);
    }
}
