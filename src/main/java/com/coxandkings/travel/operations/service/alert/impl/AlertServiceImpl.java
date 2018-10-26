package com.coxandkings.travel.operations.service.alert.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.notification.*;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.remarks.MDMUserService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AlertServiceImpl implements AlertService {

    private static RestTemplate restTemplate = new RestTemplate();

    @Value(value = "${mdm.alert}")
    private String mdmAlert;

    @Value(value = "${mdm.job-monitoring}")
    private String mdmJobMonitoring;

    @Value(value = "${mdm.user-group}")
    private String userGroupApi;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private MDMUserService mdmUserService;

    /*@Autowired
    private UserService userService;*/

    private static final Logger logger = LogManager.getLogger(AlertService.class);

    @Override
    public NotificationResource sendInlineMessageAlert(InlineMessageResource inlineMessageResource) throws OperationException {
        try {
            logger.info("In send Inline Message alert");
            logger.info(inlineMessageResource);
            NotificationResource notificationResource = new NotificationResource();
            JSONObject filter = new JSONObject();
            filter.put("alertName", inlineMessageResource.getAlertName());
            MDMResponse response = mdmRestUtils.getForObject(mdmAlert + "?filter={filter}", MDMResponse.class, filter.toString());
            AlertNotificationResource alertNotificationResource = null;
            if (response.getData().size() > 0) {
                alertNotificationResource = response.getData().get(0);
            } else {
                throw new OperationException(Constants.OPS_ERR_10103);
            }
            String message = alertNotificationResource.getNotification().stream().filter(notificationAlert -> notificationAlert.getNotificationType().
                    equalsIgnoreCase(inlineMessageResource.getNotificationType())).findFirst().get().getInlineText();
            Pattern pattern = Pattern.compile("\\$\\{(.+?)}");
            Matcher matcher = pattern.matcher(message);
            StringBuffer buffer = new StringBuffer();
            while (matcher.find()) {
                if (inlineMessageResource.getDynamicVariables().containsKey(matcher.group(1))) {
                    String replacement = inlineMessageResource.getDynamicVariables().get(matcher.group(1));
                    matcher.appendReplacement(buffer, replacement != null ? Matcher.quoteReplacement(replacement) : "null");
                }
            }
            matcher.appendTail(buffer);
            CompanyNameResource companyNameResource = new CompanyNameResource();
            companyNameResource.setName(alertNotificationResource.getCompany());
            notificationResource.setCompany(companyNameResource);
            notificationResource.setBusinessProcess(alertNotificationResource.getBusinessProcess());
            notificationResource.setFunction(alertNotificationResource.getFunction());

            AlertNameResource alertNameResource = new AlertNameResource();
            alertNameResource.setName(alertNotificationResource.getAlertName());
            notificationResource.setAlert(alertNameResource);
            List<RecipientNotification> recipientNotifications = new ArrayList<>();
            List<String> userIds = new ArrayList<>();
            List<String> groups = alertNotificationResource.getRecipients().getRoles().stream().map(a -> a.getGroup()).collect(Collectors.toList());
            for (String group : groups) {
                List<String> userId = getUserId(group);
                userIds.addAll(userId);
            }
            for (NotificationAlert notificationAlert : alertNotificationResource.getNotification()) {
                //get list of userd from group

                for (String user : userIds) {
                    //loo thriough users set user id inrciepiebnt notification
                    if (notificationAlert.getNotificationType().equals("System")) {
                        RecipientNotification recipientNotification = new RecipientNotification();
                        recipientNotification.setAlertType(inlineMessageResource.getNotificationType());
                        recipientNotification.setMessage(buffer.toString());
                        recipientNotification.setAcknowledged(false);
                        recipientNotification.setUser(user);
                        recipientNotifications.add(recipientNotification);

                    }
                }

            }
            notificationResource.setRecipients(recipientNotifications);
            URI uri = URI.create(mdmJobMonitoring);
            ResponseEntity<NotificationResource> exchange = mdmRestUtils.exchange(uri, HttpMethod.POST, notificationResource, NotificationResource.class);
            return exchange.getBody();
        } catch (Exception e) {
            logger.info("Error occurred while creating Alert ", e);
            throw new OperationException(Constants.OPS_ERR_10101);
        }
    }

    private List<String> getUserId(String groupName) throws OperationException {
        String url = userGroupApi + "?filter={\"groupName\":\"" + groupName + "\"}";

        String response = mdmRestUtils.exchange(UriComponentsBuilder.fromUriString(url).build().encode().toUri(), HttpMethod.GET, null, String.class).getBody();
        String[] childObject = (String[]) jsonObjectProvider.getChildObject(response, "$.data[:1].users[*].user", String[].class);
        return Arrays.asList(childObject);


    }


    //TODO temporary fix needs to modify once mdm configures the alerts properly
    @Override
    public NotificationResource createAlert(String businessProcess, String function, String companyName, String alertName, String userId, String message) throws OperationException {

        NotificationResource resource = new NotificationResource();
        resource.setBusinessProcess(businessProcess);
        resource.setFunction(function);
        AlertNameResource alertNameResource = new AlertNameResource();
        alertNameResource.setName(alertName);
        resource.setAlert(alertNameResource);
        CompanyNameResource companyNameResource = new CompanyNameResource();
        companyNameResource.setName(companyName);
        resource.setCompany(companyNameResource);
        RecipientNotification recipientNotification = new RecipientNotification();
        recipientNotification.setMessage(message);
        recipientNotification.setAlertType("System");
        if (!StringUtils.isEmpty(userId)) {
            recipientNotification.setUser(userId);
        }
        resource.setRecipients(Collections.singletonList(recipientNotification));

        URI uri = URI.create(mdmJobMonitoring);
        ResponseEntity<NotificationResource> exchange = null;
        try {
            exchange = mdmRestUtils.exchange(uri, HttpMethod.POST, resource, NotificationResource.class);

        } catch (Exception e) {
            logger.error("unable to create alert");
            e.printStackTrace();
            throw new OperationException("unable to create alert");
        }
        assert exchange != null;
        return exchange.getBody();
    }


    ///TODO temporary fix needs to modify once mdm configures the alerts properly
    @Override
    public List<OpsAlertResponse> getNotificationsByBusinessProcess(String businessProcess, String userId, String companyName) throws OperationException {
        try {
            //add one more filter to fetch only the non dismissed notifications.

            JSONArray orFilter = new JSONArray();

            JSONObject processFilter = new JSONObject();
            processFilter.put("businessProcess", businessProcess);

            JSONObject userFilter = new JSONObject();
            userFilter.put("recipients.user", userId);

            JSONObject companyFilter = new JSONObject();
            companyFilter.put("company.name", companyName);

            orFilter.put(processFilter);
            orFilter.put(userFilter);

            JSONArray andFilter = new JSONArray();

            JSONObject existsFilter = new JSONObject();
            existsFilter.put("$exists", true);

            JSONObject nameFilter = new JSONObject();
            nameFilter.put("alert.name", existsFilter);

            JSONObject messageFilter = new JSONObject();
            messageFilter.put("recipients.message", existsFilter);

            JSONObject functionFilter = new JSONObject();
            functionFilter.put("function", existsFilter);

            JSONObject lastUpdatedFilter = new JSONObject();
            lastUpdatedFilter.put("lastUpdated", existsFilter);

            JSONObject alertTypeFilter = new JSONObject();
            alertTypeFilter.put("recipients.alertType", existsFilter);

            andFilter.put(nameFilter);
            andFilter.put(messageFilter);
            andFilter.put(functionFilter);
            andFilter.put(functionFilter);
            andFilter.put(lastUpdatedFilter);
            andFilter.put(alertTypeFilter);
            andFilter.put(companyFilter);

            JSONObject filter = new JSONObject();
            filter.put("$or", orFilter);
            filter.put("$and", andFilter);


            //to fetch notifications ordering by latest to top
            String url = mdmJobMonitoring + "?filter={filter}&sort=-lastUpdated";
            String response = mdmRestUtils.getForObject(url, String.class, filter.toString());
            List<NotificationResource> notificationResources = new ArrayList<>();
            try {
                notificationResources = jsonObjectProvider.getChildrenCollection(response, "$.data", NotificationResource.class);
            } catch (Exception e) {
                logger.error("No results found with the provided business process " + businessProcess);
                e.printStackTrace();
                throw new OperationException("No results found with the provided business process " + businessProcess);
            }

            List<OpsAlertResponse> opsAlertResponses = new ArrayList<>();
            for (NotificationResource notificationResource : notificationResources) {
                //TODO need to identify if read or unread and increase count
                OpsAlertResponse opsAlertResponse = new OpsAlertResponse();
                opsAlertResponse.setNotification(notificationResource.getAlert().getName());
                opsAlertResponse.setBusinessProcess(notificationResource.getBusinessProcess());
                opsAlertResponse.setFunction(notificationResource.getFunction());
                opsAlertResponse.setNotificationOn(notificationResource.getLastUpdated());
                opsAlertResponse.setCompanyName(notificationResource.getCompany().getName());
                //opsAlertResponse.setAlertID(notificationResource.get_id());
                if (notificationResource.getRecipients().size() > 0) {
                    for (RecipientNotification recipientNotification : notificationResource.getRecipients()) {
                        if (!StringUtils.isEmpty(recipientNotification.getAlertType())) {
                            if (recipientNotification.getAlertType().equals("System")) {
                                opsAlertResponse.setAcknowledgedOn(recipientNotification.getAcknowledgedOn());
                                opsAlertResponse.setNotificationMessage(recipientNotification.getMessage());
                            }
                        }
                    }
                }
                opsAlertResponses.add(opsAlertResponse);
            }
            //opsAlertResponses.sort(Comparator.comparing(OpsAlertResponse::getNotificationOn));
            return opsAlertResponses;
        } catch (Exception e) {
            logger.info(e);
            e.printStackTrace();
            throw new OperationException(Constants.OPS_ERR_10102);
        }
    }

    @Override
    public List<OpsAlertResponse> getByUserId(String userId) throws OperationException {
        try {
            JSONObject filter = new JSONObject();
            filter.put("recipients.user", userId);
            String url = mdmJobMonitoring + "?filter={filter}";
            String response = mdmRestUtils.getForObject(url, String.class, filter.toString());
            List<NotificationResource> notificationResources = new ArrayList<>();
            try {
                notificationResources = jsonObjectProvider.getChildrenCollection(response, "$.data", NotificationResource.class);
            } catch (Exception e) {
                logger.error("No results found with the provided user id " + userId);
                e.printStackTrace();
                throw new OperationException("No results found with the provided user id " + userId);
            }

            List<OpsAlertResponse> opsAlertResponses = new ArrayList<>();
            for (NotificationResource notificationResource : notificationResources) {
                //TODO need to identify if read or unread and increase count
                OpsAlertResponse opsAlertResponse = new OpsAlertResponse();
                opsAlertResponse.setNotification(notificationResource.getAlert().getName());
                opsAlertResponse.setBusinessProcess(notificationResource.getBusinessProcess());
                opsAlertResponse.setFunction(notificationResource.getFunction());
                opsAlertResponse.setNotificationOn(notificationResource.getLastUpdated());
                for (RecipientNotification recipientNotification : notificationResource.getRecipients()) {
                    //comparing user id and also the acknowledged or not
                    if (recipientNotification.getUser().equalsIgnoreCase(userId) && !recipientNotification.getAcknowledged()) {
                        opsAlertResponse.setAcknowledgedOn(recipientNotification.getAcknowledgedOn());
                        opsAlertResponse.setNotificationMessage(recipientNotification.getMessage());
                    }
                }
                opsAlertResponses.add(opsAlertResponse);
            }
            return opsAlertResponses;
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException(Constants.OPS_ERR_10102);
        }
    }


    @Override
    public List<OpsAlertResponse> searchAlertByCriteria(NotificationSearchCriteria notificationSearchCriteria) throws OperationException {
        JSONObject filter = new JSONObject();
        if (!StringUtils.isEmpty(notificationSearchCriteria.getNotificationName())) {
            filter.put("alert.name", notificationSearchCriteria.getNotificationName());
        }
        if (!StringUtils.isEmpty(notificationSearchCriteria.getBusinessProcess())) {
            filter.put("businessProcess", notificationSearchCriteria.getBusinessProcess());
        }
        if (!StringUtils.isEmpty(notificationSearchCriteria.getFunction())) {
            filter.put("function", notificationSearchCriteria.getFunction());
        }
        if (!StringUtils.isEmpty(notificationSearchCriteria.getUserId())) {
            filter.put("recipients.user", notificationSearchCriteria.getUserId());
        }

        JSONObject timeRange = new JSONObject();
        if (!StringUtils.isEmpty(notificationSearchCriteria.getNotificationFrom())) {
            timeRange.put("$gte", notificationSearchCriteria.getNotificationFrom());
        }
        if (!StringUtils.isEmpty(notificationSearchCriteria.getNotificationTo())) {
            timeRange.put("$lte", notificationSearchCriteria.getNotificationTo());
        }

        if (timeRange.length() > 0) {
            filter.put("triggeredOn", timeRange);
        }

        MDMToken mdmToken = new MDMToken();
        String token = mdmToken.getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        String url = mdmJobMonitoring + "?page=" + notificationSearchCriteria.getPage() + "&count=" +
                notificationSearchCriteria.getCount() + "&sort=" + notificationSearchCriteria.getSort() + "?filter={filter}";
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, filter.toString());
        } catch (Exception e) {
            logger.info("No notifications found for the given criteria");
            e.printStackTrace();
        }
        List<NotificationResource> notificationResources = null;
        try {
            notificationResources = jsonObjectProvider.getChildrenCollection(response.getBody(), "$.data[*]", NotificationResource.class);
        } catch (Exception e) {
            logger.error("No results found for the given criteria");
            e.printStackTrace();
            throw new OperationException("No results found for the given criteria");
        }
        List<OpsAlertResponse> opsAlertResponses = new ArrayList<>();
        for (NotificationResource notificationResource : notificationResources) {
            OpsAlertResponse opsAlertResponse = new OpsAlertResponse();
            opsAlertResponse.setNotification(notificationResource.getAlert().getName());
            opsAlertResponse.setBusinessProcess(notificationResource.getBusinessProcess());
            opsAlertResponse.setFunction(notificationResource.getFunction());
            opsAlertResponse.setNotificationOn(notificationResource.getLastUpdated());
            for (RecipientNotification recipientNotification : notificationResource.getRecipients()) {
                if (recipientNotification.getUser().equalsIgnoreCase(notificationSearchCriteria.getUserId())) {
                    opsAlertResponse.setAcknowledgedOn(recipientNotification.getAcknowledgedOn());
                    opsAlertResponse.setNotificationMessage(recipientNotification.getMessage());
                }
            }
            opsAlertResponses.add(opsAlertResponse);
        }
        return opsAlertResponses;
    }

    @Override
    public Map dismissNotification(String alertID, String userID) throws OperationException {
        // TODO: 05-06-2018  Need to get API from MDM
        //need to update the acknowledged field to true
        String message = "";
        NotificationResource notificationResource = null;
        JSONObject filter = new JSONObject();
        filter.put("_id", alertID);
        filter.put("recipients.user", userID);
        String response = null;
        try {
            response = mdmRestUtils.getForObject(mdmJobMonitoring + "filter={filter}", String.class, filter.toString());
        } catch (Exception e) {
            logger.error("unable to find alert");
            e.printStackTrace();
        }
        notificationResource = (NotificationResource) jsonObjectProvider.getChildObject(response, "$.data[0]", NotificationResource.class);
        List<RecipientNotification> recipientNotifications = new ArrayList<>();
        for (RecipientNotification recipientNotification : notificationResource.getRecipients()) {
            if (recipientNotification.getUser().equals(userID)) {
                recipientNotification.setAcknowledged(true);
            }
            recipientNotifications.add(recipientNotification);
        }
        notificationResource.setRecipients(recipientNotifications);

        ResponseEntity<NotificationResource> entity = null;
        try {
            entity = mdmRestUtils.postForEntity(mdmJobMonitoring, notificationResource, NotificationResource.class);
            message = "Successfully dismissed the alert";
        } catch (Exception e) {
            logger.error("unable to dismiss the notification");
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }
}