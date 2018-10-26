package com.coxandkings.travel.operations.service.prodreview.impl;

import com.coxandkings.travel.operations.criteria.prodreview.MDMTemplateCriteria;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.model.prodreview.mdmtemplate.Template;
import com.coxandkings.travel.operations.resource.prodreview.MailTemplateResource;
import com.coxandkings.travel.operations.response.SchedulerResponse;
import com.coxandkings.travel.operations.service.booking.impl.OpsBookingServiceImpl;
import com.coxandkings.travel.operations.service.prodreview.ProductReviewSchedulerService;
import com.coxandkings.travel.operations.service.prodreview.ProductReviewService;
import com.coxandkings.travel.operations.service.prodreview.TemplateService;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class ProductReviewSchedulerServiceImpl implements ProductReviewSchedulerService {

    @Value("${operations_db_config.url}")
    private String url;

    @Value("${operations_db_config.username]")
    private String username;

    @Value("${operations_db_config.password}")
    private String password;

    @Value("${review.scheduler-url}")
    private String schedulerUrl;

    @Value("${review.template.base_url}")
    private String templateUrl;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ProductReviewService productReviewService;

    private static Logger logger = LogManager.getLogger(OpsBookingServiceImpl.class);

    private RestTemplate restTemplate = RestUtils.getTemplate();

    @Override
    public void sendReviewToClient() throws JSONException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime date = LocalDateTime.now();
        ResponseEntity<List<SchedulerResponse>> schedulerResponses = null;
        try {
            schedulerResponses = restTemplate.exchange(schedulerUrl + dtf.format(date), HttpMethod.GET, null, new ParameterizedTypeReference<List<SchedulerResponse>>() {
            });
        } catch (Exception e) {
            logger.error("Could not able to fetch bookings ended", e);

        }
        if (schedulerResponses != null) {
            for (SchedulerResponse schedulerResponse : schedulerResponses.getBody()) {
                MDMTemplateCriteria criteria = new MDMTemplateCriteria();
                criteria.setClientName(schedulerResponse.getClientId());
                criteria.setClientType(schedulerResponse.getClientType());
                OpsProductSubCategory subCategory = OpsProductSubCategory.fromString(schedulerResponse.getProductSubCategory());
                criteria.setProductCategorySubType(subCategory.getSubCategory());
                criteria.setProductCategory(subCategory.getCategory().getCategory());
                criteria.setClientGroup(schedulerResponse.getClientGroup());
                Template template = null;
                try {
                    String templateId = templateService.searchTemplate(criteria);
                    if (null != template) {
                        template = new Template();
                        template.setTemplateId(templateId);
                        template.setBookId(schedulerResponse.getBookId());
                        template.setOrderId(schedulerResponse.getOrderId());
                        templateService.save(template);
                        String refNumber = template.getId();
                        String url = templateUrl + refNumber;
                        MailTemplateResource mailTemplateResource = new MailTemplateResource();
                        mailTemplateResource.setBookID(schedulerResponse.getBookId());
                        mailTemplateResource.setUrls(Collections.singletonList(url));
                        try {
                            templateService.sendMailToClient(mailTemplateResource);
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("mail could not be sent to the client ", e);
                        }
                    } else {
                        logger.info("No template found for :" + schedulerResponse);
                    }
                } catch (Exception e) {

                    logger.info("Cannot be able to fetch template from MDM " + schedulerResponse, e);
                    //throw new OperationException("Cannot be able to fetch template from MDM");
                }
            }
        } else {
            logger.debug("No booking found for the current end date");
        }
    }
}
