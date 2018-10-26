package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.user.UserService;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class RestUtils {

    private static final Logger logger = LogManager.getLogger(RestUtils.class);

    private static String proxyHost;

    private static int proxyPort;

    @Autowired
    private UserService userService;

    public static RestTemplate getTemplate() {
        RestTemplate template = new RestTemplate();

        if (proxyHost == null || proxyHost.trim().length() == 0) {
            return template;
        } else {
            HttpClientBuilder clientBuilder = HttpClientBuilder.create();
            clientBuilder.useSystemProperties();
            clientBuilder.setProxy(new HttpHost(proxyHost, proxyPort));
            clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

            CloseableHttpClient client = clientBuilder.build();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setHttpClient(client);

            template = new RestTemplate();
            template.setRequestFactory(factory);
        }

        return template;
    }

    @Value(value = "${proxy_info.url}")
    public void setProxyHost(String newProxyHost) {
        this.proxyHost = newProxyHost;
    }

    @Value(value = "${proxy_info.port}")
    public void setProxyPort(int newProxyPort) {
        proxyPort = newProxyPort;
    }

    public static <T> T getForObject(String url, Class<T> responseType, ClientHttpRequestInterceptor interceptor, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = getTemplate();

        if (interceptor != null) {
            restTemplate.getInterceptors().add(interceptor);
        }

        logger.info("RestUtils.getForObject() method:: URL is: " + url);

        T forObject = restTemplate.getForObject(url, responseType, uriVariables);

        if (interceptor != null) {
            restTemplate.getInterceptors().remove(interceptor);
        }
        return forObject;
    }

    public static <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Object... uriVariables) {
        RestTemplate restTemplate = getTemplate();
        logger.info("RestUtils.postForEntity() method:: URL is: " + url);
        return restTemplate.postForEntity(url, request, responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType) {
        RestTemplate restTemplate = getTemplate();
        logger.info("RestUtils.postForEntity() method:: URL is: " + url);
        return restTemplate.postForEntity(url, request, responseType);
    }

    public static <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, ClientHttpRequestInterceptor interceptor, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = getTemplate();
        logger.info("RestUtils.postForEntity() method:: URL is: " + url);
        if (interceptor != null) {
            restTemplate.getInterceptors().add(interceptor);
        }

        ResponseEntity<T> forObject = restTemplate.postForEntity(url, request, responseType, uriVariables);

        if (interceptor != null) {
            restTemplate.getInterceptors().remove(interceptor);
        }
        return forObject;
    }

    public static <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = getTemplate();
        logger.info("RestUtils.getForObject() method:: URL is: " + url);
        return restTemplate.getForObject(url, responseType, uriVariables);
    }

    public static <T> T getForObject(URI url, Class<T> responseType) throws RestClientException {
        RestTemplate restTemplate = getTemplate();
        logger.info("RestUtils.getForObject() method:: URL is: " + url);
        return restTemplate.getForObject(url, responseType);
    }

    public static void put(String url, Object request, ClientHttpRequestInterceptor interceptor, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = getTemplate();
        logger.info("RestUtils.put() method:: URL is: " + url);
        if (interceptor != null) {
            restTemplate.getInterceptors().add(interceptor);
        }

        restTemplate.put(url, request, uriVariables);

        if (interceptor != null) {
            restTemplate.getInterceptors().remove(interceptor);
        }
    }


    public static void put(String url, Object request, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = getTemplate();
        logger.info("RestUtils.put() method:: URL is: " + url);
        restTemplate.put(url, request, uriVariables);
    }

    public static <T> ResponseEntity<T> exchange(String url, HttpMethod method,
                                                 HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = getTemplate();
        logger.info("RestUtils.exchange() method:: URL is: " + url);
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> exchange(String url, HttpMethod method,
                                                 HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType,
                                                 Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = getTemplate();
        logger.info("RestUtils.exchange() method:: URL is: " + url);
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> exchange(URI url, HttpMethod httpMethod, HttpEntity httpEntity,
                                          Class<T> responseType) throws RestClientException, OperationException {
        RestTemplate template = getTemplate();


        logger.info("RestUtils.exchange() method:: URL is: " + url);
        ResponseEntity<T> exchange = template.exchange(url, httpMethod, httpEntity, responseType);

        return exchange;
    }

    public <T> HttpEntity<T> getHttpEntity() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = userService.getLoggedInUserToken();
        headers.add("Authorization", token);
        return new HttpEntity<T>(headers);
    }


    public <T> HttpEntity<T> getHttpEntity(Object object) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = userService.getLoggedInUserToken();
        headers.add("Authorization", token);
        return new HttpEntity(object, headers);
    }
}