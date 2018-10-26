package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.systemlogin.MDMDataSource;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.net.URI;


@Component
public class MDMRestUtils {

    @Autowired
    @Qualifier(value = "mDMDataSource")
    private MDMDataSource mdmDataSource;

    @Autowired
    private RestUtils restUtils;

    private static final Logger logger = LogManager.getLogger(MDMRestUtils.class);

    /**
     * used for post request
     *
     * @param url          {
     *                     mdmUserInfo = userService.createUserDetailsFromToken(jwtToken);
     *                     user = userService.getOpsUser(mdmUserInfo);
     *                     mdmUserInfo = userService.createUserDetailsFromToken(jwtToken);
     *                     if (mdmUserInfo == null) {
     *                     filterChain.doFilter(request, response);
     *                     return;
     *                     }
     * @param request
     * @param responseType return type of object
     * @param <T>
     * @return
     * @throws OperationException
     */
    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType) throws OperationException {
        logger.info("MDMRestUtils.postForEntity() method:: URL is: " + url);
        RestTemplate template = restUtils.getTemplate();
        HttpEntity httpEntity = getHttpEntity(request);
        return template.exchange(url, HttpMethod.POST, httpEntity, responseType);
    }

    /**
     * use for get request
     *
     * @param url
     * @param responseType
     * @param <T>
     * @return
     * @throws RestClientException
     * @throws OperationException
     */

    public <T> T getForObject(String url, Class<T> responseType) throws OperationException {
        logger.info("MDMRestUtils.getForObject() method:: URL is: " + url);
        RestTemplate template = restUtils.getTemplate();
        HttpEntity httpEntity = getHttpEntity();
        return template.exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
    }

    /**
     * use for get request
     *
     * @param url
     * @param responseType
     * @param param
     * @param <T>
     * @return
     * @throws RestClientException
     * @throws OperationException
     */

    public <T> T getForObject(String url, Class<T> responseType, String param) throws OperationException {
        logger.info("MDMRestUtils.getForObject() method:: URL is: " + url);
        RestTemplate template = restUtils.getTemplate();
        HttpEntity httpEntity = getHttpEntity();
        return template.exchange(url, HttpMethod.GET, httpEntity, responseType, param).getBody();

    }

    /**
     * use this method for put request
     *
     * @param url
     * @param request
     * @throws RestClientException
     * @throws OperationException
     */
    public void put(String url, Object request) throws OperationException {
        logger.info("MDMRestUtils.put() method:: URL is: " + url);
        RestTemplate template = restUtils.getTemplate();
        HttpEntity httpEntity = getHttpEntity(request);
        template.exchange(url, HttpMethod.PUT, httpEntity, Object.class);
    }

    /**
     * Use this method to get JSONPath
     *
     * @param url
     * @return
     * @throws OperationException
     */
    public String getResponseJSON(String url) throws OperationException {
        logger.info("MDMRestUtils.getResponseJSON() method:: URL is: " + url);
        RestTemplate template = restUtils.getTemplate();
        HttpEntity entity = getHttpEntity();
        ResponseEntity<String> response = null;
        try {
            response = template.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.getBody();
    }

    /**
     * Use this method for use exchange
     *
     * @param url
     * @param httpMethod
     * @param responseType
     * @param args
     * @param <T>
     * @return
     * @throws OperationException
     */
    public <T> T exchange(String url, HttpMethod httpMethod, Class<T> responseType, Object... args) throws OperationException {
        HttpEntity<?> httpEntity = getHttpEntity();
        RestTemplate template = restUtils.getTemplate();
        ResponseEntity<T> exchange = template.exchange(url, httpMethod, httpEntity, responseType, args);
        T body = exchange.getBody();
        return body;
    }


    public <T> T exchange(URI url, HttpMethod httpMethod, Class<T> responseType, Object... args) throws OperationException {
        HttpEntity<?> httpEntity = getHttpEntity();
        RestTemplate template = restUtils.getTemplate();
        ResponseEntity<T> exchange = template.exchange(url, httpMethod, httpEntity, responseType);
        T body = exchange.getBody();
        return body;
    }

    /**
     * This method user can pass URI to use exchange.Need to use when you want to pass value binding with URL
     *
     * @param url
     * @param httpMethod
     * @param requestBody
     * @param responseType
     * @param <T>
     * @return
     * @throws RestClientException
     * @throws OperationException
     */

    public <T> ResponseEntity<T> exchange(URI url, HttpMethod httpMethod, Object requestBody,
                                          Class<T> responseType) throws RestClientException, OperationException {
        RestTemplate template = restUtils.getTemplate();
        HttpEntity httpEntity = getHttpEntity(requestBody);
        logger.info("MDMRestUtils.exchange() method:: URL is: " + url);
        ResponseEntity<T> exchange = template.exchange(url, httpMethod, httpEntity, responseType);

        return exchange;
    }

    public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
                                          HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType,
                                          Object... uriVariables) throws RestClientException, OperationException {
        RestTemplate restTemplate = restUtils.getTemplate();
        Object requestBody = requestEntity.getBody();
        HttpEntity httpEntity = getHttpEntity(requestBody);
        return restTemplate.exchange(url, method, httpEntity, responseType, uriVariables);
    }

    public <T> ResponseEntity<T> exchange(URI url, HttpMethod httpMethod, Object requestBody,
                                          Class<T> responseType, MediaType mediaType) throws RestClientException, OperationException {
        RestTemplate template = restUtils.getTemplate();
        HttpEntity httpEntity = getHttpEntity(requestBody, mediaType);

        logger.info("MDMRestUtils.exchange() method:: URL is: " + url);
        ResponseEntity<T> exchange = template.exchange(url, httpMethod, httpEntity, responseType);

        return exchange;
    }

    private HttpEntity getHttpEntity(Object requestBody) throws OperationException {
        RestTemplate template = restUtils.getTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = mdmDataSource.getToken().getToken();
//        logger.info("In MDMRestUtils.exchange() method --> Is token null?" + (token == null));
        headers.add("Authorization", token);
        return new HttpEntity(requestBody, headers);
    }

    private HttpEntity getHttpEntity(Object requestBody, MediaType mediaType) throws OperationException {
        RestTemplate template = restUtils.getTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        String token = mdmDataSource.getToken().getToken();
//        logger.info("In MDMRestUtils.exchange() method --> Is token null?" + (token == null));
        headers.add("Authorization", token);
        return new HttpEntity(requestBody, headers);
    }

    private HttpEntity getHttpEntity() throws OperationException {
        RestTemplate template = restUtils.getTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = mdmDataSource.getToken().getToken();
//        logger.info("In MDMRestUtils.exchange() method --> Is token null?" + (token == null));
        headers.add("Authorization", token);
        return new HttpEntity(headers);
    }

    public <T> ResponseEntity<T> exchange(URI url, HttpMethod httpMethod, Object requestBody,
                                          ParameterizedTypeReference<T> responseType) throws OperationException {
        RestTemplate template = restUtils.getTemplate();
        HttpEntity httpEntity = getHttpEntity(requestBody);
        logger.info("MDMRestUtils.exchange() method:: URL is: " + url);
        ResponseEntity<T> exchange = template.exchange(url, httpMethod, httpEntity, responseType);

        return exchange;

    }
}