package com.coxandkings.travel.operations.systemlogin;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.refund.mdm.UserLogin;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * MDMDataSource class is used for system login and logout.When application start it will login and hold token and give back to calling method.
 * We are using force login so need to logout
 */

@Component(value = "mDMDataSource")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MDMDataSource {

    private static final Logger logger = LogManager.getLogger(MDMDataSource.class);

    @Autowired
    @Qualifier(value = "mDMToken")
    private MDMToken mdmToken;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Value(value = "${username}")
    private String systemUsername;

    @Value(value = "${password}")
    private String systemUserPassword;

    @Value(value = "${user_management.system_user.login}")
    private String loginUrl;

    @Value(value = "${user_management.system_user.logout}")
    private String logoutUrl;

    @Value("${user_management.system_user.auth_token_prefix}")
    private String authTokenPrefix;


    @PostConstruct
    private void loginToMDM() throws OperationException {
		/*1) connect to MDM
		2) Create MDMToken, assign to mdmToken
		3) patch (to be removed later)
			- Write token to a text file to System.getProperty("java.io.tmpdir")
			- if MDM returns user already logged in, then read the file, call logout( data_from_file ) method*/

        RestTemplate restTemplate = RestUtils.getTemplate();
        logger.info("Before Login to MDM as: " + systemUsername);
        UserLogin userLogin = new UserLogin(systemUsername, systemUserPassword);

        String loginJson = null;
        try {
            loginJson = restTemplate.postForObject(loginUrl, userLogin, String.class);
        } catch (Exception e) {
            logger.error(e);
            logger.info("Login to MDM failed - proceeding to logout using existing token");

        }

        if (null == loginJson) {
            throw new OperationException("Unable to login");
        }

        String token = authTokenPrefix + jsonObjectProvider.getAttributeValue(loginJson, "$.token", String.class);
        Long expireUtcTime = (Long) jsonObjectProvider.getChildObject(loginJson, "$.expireIn", Long.class);

        logger.info("Token " + token);

        //ToDo: remove after mmd provide system login
        ZoneId zoneId = ZoneId.of("UTC").normalized();
        ZonedDateTime expireTime = Instant.ofEpochMilli((expireUtcTime)).atZone(zoneId);
        mdmToken.setToken(token);
        mdmToken.setTokenExpiryTimestamp(expireTime);
        mdmToken.setTokenExpired(false);


        logger.info("Login is successful ***");
    }


    public MDMToken getToken() throws OperationException {
        if (mdmToken.isTokenExpired()) {
            try {
                loginToMDM();
            } catch (OperationException e) {
                throw new OperationException("Unable able to login to MDM");
            }
        }
//        logger.info("mdmToken->" + mdmToken);
        return mdmToken;
    }


    @PreDestroy
    private void logout() throws OperationException {

        logger.info("***Logout***");

        RestTemplate restTemplate = RestUtils.getTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", mdmToken.getToken());
        HttpEntity httpEntity;
        UserLogin userLogin = new UserLogin(systemUsername, systemUserPassword);
        httpEntity = new HttpEntity(userLogin, httpHeaders);
        try {
            restTemplate.exchange(logoutUrl, HttpMethod.POST, httpEntity, String.class);

        } catch (Exception e) {
            logger.error("Not able to logout", e);

            throw new OperationException("Not able to logout");
        }
        logger.info("System logout Successfully");

    }
}