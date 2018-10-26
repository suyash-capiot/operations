package com.coxandkings.travel.operations.service.prodreview.impl;

import com.coxandkings.travel.operations.criteria.prodreview.MDMTemplateCriteria;
import com.coxandkings.travel.operations.criteria.prodreview.TemplateCriteria;
import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.enums.prodreview.ClientType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.prodreview.mdmtemplate.Template;
import com.coxandkings.travel.operations.repository.prodreview.TemplateRepository;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.prodreview.MailTemplateResource;
import com.coxandkings.travel.operations.resource.prodreview.ProductTemplateReferenceResource;
import com.coxandkings.travel.operations.resource.prodreview.mdmtemplateresource.MDMTemplateResource;
import com.coxandkings.travel.operations.response.prodreview.MDMTemplateResponse;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.booking.impl.OpsBookingServiceImpl;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.prodreview.TemplateService;
import com.coxandkings.travel.operations.utils.EmailUtils;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service("mdmTemplateService")
public class TemplateServiceImpl implements TemplateService {

    private static Logger logger = LogManager.getLogger(OpsBookingServiceImpl.class);

    @Autowired
    private OpsBookingService opsBookingService;

    @Value("${review.template.base_url}")
    private String templateUrl;

    @Value("${mdm.feedback-review-configuration}")
    private String feedbackReviewConfiguration;

    @Value("${review.email.function}")
    private String function;

    @Value("${review.email.send_client.scenario}")
    private String scenario;

    @Value("${review.operations_base_url}")
    private String operationsBaseUrl;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private ClientMasterDataService clientMasterDataService;

    @Autowired
    private TemplateRepository templateRepository;


    @Override
    public MDMTemplateResource getMDMTemplate(String id) {
        Template template = templateRepository.getById(id);
        return getFromMDM(template.getTemplateId());
    }

    @Override
    public Template save(Template template) {
        if (!templateRepository.getExists(template.getTemplateId())) {
            return templateRepository.saveTemplate(template);
        } else {
            return templateRepository.getByTemplateId(template.getTemplateId());
        }
    }

    private MDMTemplateResource getFromMDM(String id) {
        JSONObject filter = new JSONObject();
        filter.put("_id", id);
        MDMTemplateResponse response = null;
        try {
            response = mdmRestUtils.getForObject((feedbackReviewConfiguration + "?filter={filter}"), MDMTemplateResponse.class, filter.toString());
        } catch (Exception e) {
            logger.info("Unable to get template from MDM");
        }
        return response != null ? response.getData().get(0) : null;
    }

    /**
     * This method will search template and generate unique reference which will we will send to client email when client click on that link it will open WEM screen to submit product review.
     * WEM will fetch the template using this unique reference number
     *
     * @param mdmTemplateCriteria
     * @return
     * @throws OperationException
     * @throws JSONException
     */

    @Override
    public String searchTemplate(MDMTemplateCriteria mdmTemplateCriteria) throws OperationException, JSONException {

        JSONObject filter = new JSONObject();

        if (!StringUtils.isEmpty(mdmTemplateCriteria.getTemplateName())) {
            filter.put("templateName", mdmTemplateCriteria.getTemplateName());
        }
        if (!StringUtils.isEmpty(mdmTemplateCriteria.getTemplateType())) {
            filter.put("templateType", mdmTemplateCriteria.getTemplateType());
        }
        if (!StringUtils.isEmpty(mdmTemplateCriteria.getFromDate())) {
            filter.put("createdAt", ZonedDateTime.parse(mdmTemplateCriteria.getFromDate()).format(DateTimeFormatter.ISO_INSTANT));
        }
        if (!StringUtils.isEmpty(mdmTemplateCriteria.getClientName())) {
            filter.put("products.clientName", mdmTemplateCriteria.getClientName());
        }
        if (!StringUtils.isEmpty(mdmTemplateCriteria.getClientType())) {
            filter.put("products.clientType", mdmTemplateCriteria.getClientType());
        }
        if (!StringUtils.isEmpty(mdmTemplateCriteria.getProductCategory())) {
            filter.put("products.productCategory", mdmTemplateCriteria.getProductCategory());
        }
        if (!StringUtils.isEmpty(mdmTemplateCriteria.getProductCategorySubType())) {
            filter.put("products.productCategorySubType", mdmTemplateCriteria.getProductCategorySubType());
        }
        MDMTemplateResponse response = mdmRestUtils.exchange(feedbackReviewConfiguration + "filter={filter}", HttpMethod.GET, MDMTemplateResponse.class, filter);
        MDMTemplateResource mdmTemplateResource = null;
        try {
            mdmTemplateResource = response.getData().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Unable to get template results from mdm");
        }


        return mdmTemplateResource != null ? mdmTemplateResource.get_id() : null;

    }


    @Override
    public List<ProductTemplateReferenceResource> searchTemplateByCriteria(TemplateCriteria templateCriteria) throws OperationException {
        List<MDMTemplateResource> mdmTemplateResources = new ArrayList<>();
        String url = feedbackReviewConfiguration + "?filter={filter}";
        OpsBooking opsBooking = null;
        if (!StringUtils.isEmpty(templateCriteria.getBookingReferenceNumber())) {
            try {
                opsBooking = opsBookingService.getBooking(templateCriteria.getBookingReferenceNumber());
            } catch (Exception e) {
                logger.error("Unable to find booking with the provided book id:" + templateCriteria.getBookingReferenceNumber(), e);
                throw new OperationException("Unable to find booking with the provided book id:" + templateCriteria.getBookingReferenceNumber());

            }
            assert opsBooking != null;
            for (OpsProduct opsProduct : opsBooking.getProducts()) {
                TemplateCriteria templateCriteria1 = new TemplateCriteria();
                templateCriteria1.setProductCategory(opsProduct.getProductCategory());
                templateCriteria1.setProductCategorySubType(opsProduct.getProductSubCategory());
                templateCriteria1.setClientType(opsBooking.getClientType());
                String clientName = "";
                if (opsBooking.getClientType().equalsIgnoreCase("B2B")) {
                    clientName = clientMasterDataService.getB2BClientNames(Collections.singletonList(opsBooking.getClientID())).get(opsBooking.getClientID());
                } else {
                    clientName = clientMasterDataService.getB2CClientNames(Collections.singletonList(opsBooking.getClientID())).get(opsBooking.getClientID());
                }
                templateCriteria1.setClientName(clientName);
                templateCriteria1.setCompanyID(templateCriteria.getCompanyID());
                MDMTemplateResponse response = null;
                try {
                    response = mdmRestUtils.exchange(url, HttpMethod.GET, MDMTemplateResponse.class, getSearchRequest(templateCriteria1));
                } catch (Exception e) {

                    logger.info("Unable to get data form MDM", e);
                    throw new OperationException("Unable to get data form MDM");


                }
                if (response.getData().size() > 0) {
                    mdmTemplateResources.add(response.getData().get(0));
                }

            }
        } else {
            MDMTemplateResponse response = null;
            try {
                response = mdmRestUtils.exchange(url, HttpMethod.GET, MDMTemplateResponse.class, getSearchRequest(templateCriteria));
            } catch (Exception e) {
                logger.error("unable to fetch templates from MDM");
            }
            assert response != null;
            List<MDMTemplateResource> mdmTemplateResourceList = response.getData();
            if (mdmTemplateResourceList.size() == 0) {

                return new ArrayList<>();
            }
            mdmTemplateResources.addAll(mdmTemplateResourceList);
        }
        try {
            for (MDMTemplateResource mdmTemplateResource : mdmTemplateResources) {
                Template template = new Template();
                template.setTemplateId(mdmTemplateResource.get_id());
                template.setBookId(templateCriteria.getBookingReferenceNumber());
                template.setOrderId(templateCriteria.getOrderId());
                template = templateRepository.saveTemplate(template);
                mdmTemplateResource.setTemplateId(template.getId());
            }
            return mdmTemplateResources.stream().map(this::convert).collect(Collectors.toList());
        } catch (Exception e) {
            logger.info("Error in sending templates from MDM");
            return new ArrayList<>();
        }
    }


    private JSONObject getSearchRequest(TemplateCriteria templateCriteria) {
        JSONObject criteriaFilter = new JSONObject();
        if (!StringUtils.isEmpty(templateCriteria.getCompanyID())) {
            criteriaFilter.put("companyID", templateCriteria.getCompanyID());
        }
        if (!StringUtils.isEmpty(templateCriteria.getTemplateType())) {
            criteriaFilter.put("templateType", templateCriteria.getTemplateType());
        }
        if (!StringUtils.isEmpty(templateCriteria.getSubTypeForm())) {
            criteriaFilter.put("subTypeForm", templateCriteria.getSubTypeForm());
        }
        if (!StringUtils.isEmpty(templateCriteria.getClientType())) {
            criteriaFilter.put("products.clientType", templateCriteria.getClientType());
        }
        if (!StringUtils.isEmpty(templateCriteria.getClientGroup())) {
            criteriaFilter.put("products.clientGroup", templateCriteria.getClientGroup());
        }
        if (!StringUtils.isEmpty(templateCriteria.getClientName())) {
            criteriaFilter.put("products.clientName", templateCriteria.getClientName());
        }
        if (!StringUtils.isEmpty(templateCriteria.getProductCategory())) {
            criteriaFilter.put("products.productCategory", templateCriteria.getProductCategory());
        }
        if (!StringUtils.isEmpty(templateCriteria.getProductCategorySubType())) {
            criteriaFilter.put("products.productCategorySubType", templateCriteria.getProductCategorySubType());
        }
        JSONObject timeRange = new JSONObject();
        if (!StringUtils.isEmpty(templateCriteria.getFromDate())) {
            timeRange.put("$gte", ZonedDateTime.parse(templateCriteria.getFromDate()).format(DateTimeFormatter.ISO_INSTANT));
        }
        if (!StringUtils.isEmpty(templateCriteria.getToDate())) {
            timeRange.put("$lte", ZonedDateTime.parse(templateCriteria.getToDate()).format(DateTimeFormatter.ISO_INSTANT));
        }
        if (timeRange.length() > 0) {
            criteriaFilter.put("createdAt", timeRange);
        }
        JSONArray andFilter = new JSONArray();
        andFilter.put(criteriaFilter);


        JSONObject filter = new JSONObject();
        filter.put("$and", andFilter);
        return filter;
    }

    @Override
    public Map<String, String> sendMailToClient(MailTemplateResource mailTemplateResource) throws OperationException {
        OpsBooking opsBooking = null;
        String clientId;
        ClientType clientType;
        if (StringUtils.isEmpty(mailTemplateResource.getBookID())) {
            opsBooking = opsBookingService.getBooking(mailTemplateResource.getBookID());
            clientId = opsBooking.getClientID();
            clientType = ClientType.valueOf(opsBooking.getClientType());


        } else {
            clientId = mailTemplateResource.getClientId();
            clientType = mailTemplateResource.getClientType();
        }
        String message = "Email communication is not successful";
        Map<String, String> response = new HashMap<>();
        String clientMail = "";
        String clientName = null;


        if (clientType == ClientType.B2B) {
            clientMail = clientMasterDataService.getClientEmailId(clientId, MDMClientType.fromString(clientType.name()));
            clientName = clientMasterDataService.getB2BClientNames(Collections.singletonList(clientId)).get(clientId);
        } else if (clientType == ClientType.B2C) {
            clientMail = clientMasterDataService.getClientEmailId(clientId, MDMClientType.fromString(clientType.name()));
            clientName = clientMasterDataService.getB2CClientNames(Collections.singletonList(clientId)).get(clientId);
        }
        for (String url : mailTemplateResource.getUrls()) {
            Map<String, String> map = new HashMap<>();
            map.put("clientId", clientName);
            map.put("review_url", url);
            String subject = "Please share your review";
            EmailResponse emailResponse = null;
            if (clientMail == null || clientName == null) {
                clientMail = "seshu.thota@coxandkings.com";
                clientName = "Dear client";
                message = "Client Mail or Client name is empty";
            }
            try {
                emailResponse = emailUtils.buildClientMail(function, scenario, clientMail, subject, map, null, null);
                if (emailResponse.getStatus().equalsIgnoreCase("SUCCESS")) {
                    logger.info(emailResponse.getMesssage());
                    message = emailResponse.getMesssage();
                }
            } catch (Exception e) {

                message = "Email is not successful";
                logger.error(message, e);
            }
        }
        response.put("message", message);
        return response;
    }

    private ProductTemplateReferenceResource convert(MDMTemplateResource mdmTemplateResource) {
        ProductTemplateReferenceResource productTemplateReferenceResource = new ProductTemplateReferenceResource();
        productTemplateReferenceResource.setTemplateId(mdmTemplateResource.getTemplateId());
        productTemplateReferenceResource.setProductCategory(mdmTemplateResource.getProducts().getProductCategory());
        productTemplateReferenceResource.setProductSubCategory(mdmTemplateResource.getProducts().getProductCategorySubType());
        productTemplateReferenceResource.setTemplateName(mdmTemplateResource.getTemplateName());
        productTemplateReferenceResource.setTemplateType(mdmTemplateResource.getTemplateType());
        return productTemplateReferenceResource;
    }

}
