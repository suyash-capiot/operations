package com.coxandkings.travel.operations.service.prodreview.impl;

import com.coxandkings.travel.operations.criteria.prodreview.ProductAverageCriteria;
import com.coxandkings.travel.operations.criteria.prodreview.ProductReviewCriteria;
import com.coxandkings.travel.operations.criteria.todo.ToDoCriteria;
import com.coxandkings.travel.operations.enums.Status;
import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.enums.prodreview.CustomerResponseFlg;
import com.coxandkings.travel.operations.enums.prodreview.ProdReviewStatus;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.enums.user.UserType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.prodreview.*;
import com.coxandkings.travel.operations.model.prodreview.mdmtemplate.Template;
import com.coxandkings.travel.operations.repository.prodreview.ProductReviewAverageRepository;
import com.coxandkings.travel.operations.repository.prodreview.ProductReviewRepository;
import com.coxandkings.travel.operations.repository.prodreview.TemplateRepository;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.prodreview.*;
import com.coxandkings.travel.operations.resource.todo.ToDoResponse;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResponse;
import com.coxandkings.travel.operations.response.*;
import com.coxandkings.travel.operations.response.prodreview.ProductReviewResposneClient;
import com.coxandkings.travel.operations.response.prodreview.ReviewFormSubmittedResponse;
import com.coxandkings.travel.operations.response.prodreview.ReviewResponse;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.booking.impl.OpsBookingServiceImpl;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.prodreview.ProductReviewService;
import com.coxandkings.travel.operations.service.prodreview.TemplateService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.utils.*;
import com.jayway.jsonpath.JsonPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.coxandkings.travel.operations.enums.user.UserType.*;


@Service
public class ProductReviewServiceImpl implements ProductReviewService {

    private static Logger logger = LogManager.getLogger(OpsBookingServiceImpl.class);

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Autowired
    private ProductReviewAverageRepository productReviewAverageRepository;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private MDMRestUtils mdmRestUtils;
    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private ClientMasterDataService clientMasterDataService;

    @Autowired
    private EmailUtils emailUtils;

    @Value("${review.email.function}")
    private String function;

    @Value("${review.email.businessprocess}")
    private String businessprocess;

    @Value("${review.email.send_client.scenario}")
    private String scenario;

    @Value("${review.operations_base_url}")
    private String operationsBaseUrl;

    @Value("${review.email.send_client_approval.scenario}")
    private String approvalScenario;

    @Value("${mdm.user-management}")
    private String userManagementUrl;

    @Value("${review.template.base_url}")
    private String templateUrl;


    @Override
    public ProductReview saveAndUpdate(ProductReviewResource productReviewResource) throws OperationException {

        Template template = templateRepository.getById(productReviewResource.getUniqueReferenceNumber());
        OpsBooking opsBooking = null;
        if (StringUtils.isEmpty(template.getBookId())) {
            opsBooking = opsBookingService.getBooking(template.getBookId());
        }
        ProductReview productReview = new ProductReview();
        if (null != template && !template.getDone()) {
            CopyUtils.copy(productReviewResource, productReview);
            if (StringUtils.isEmpty(productReviewResource.getSubmitDate())) {
                productReview.setSubmitDate(ZonedDateTime.now());
            }
            List<ProductReviewUser> productReviewUsers = productReviewResource.getProductReviewUsers();
            List<ProductReviewUser> productReviewUserList = new ArrayList<>();
            for (ProductReviewUser productReviewUser : productReviewUsers) {
                ProductReviewUser productReviewUser1 = new ProductReviewUser();
                CopyUtils.copy(productReviewUser, productReviewUser1);
                productReviewUserList.add(productReviewUser1);
            }
            for (ProductReviewUser productReviewUser : productReviewUserList)
                productReviewUser.setProductReview(productReview);
            productReview.setProductReviewUsers(productReviewUserList);
            productReview.setVersionNumber(1f);
            String clientId = "";
            if (null != opsBooking) {
                clientId = opsBooking.getClientID();
            }
            productReview.setClientId(clientId);
            productReview = productReviewRepository.saveOrUpdate(productReview);
            createToDoTask(OPS_USER, productReview, null);
            templateRepository.update(template.getId());
        } else {
            logger.error("Duplicate submission for the review");
            throw new OperationException("Duplicate submission for the review");
        }
        return productReview;
    }

    public ProductReview updateProductReview(ReviewResponse reviewResponse) throws OperationException {
        ProductReview productReview = convert(reviewResponse);
        productReview.setReviewCompleted(true);
        productReviewRepository.saveOrUpdate(productReview);
        return productReview;
    }

    //need to find efficient way search is taking time
    public SearchProductReviewResponse getManageProductCriteria(ProductReviewCriteria productReviewCriteria) {
        SearchProductReviewResponse searchResponse = productReviewRepository.getManageProductCriteria(productReviewCriteria);
        //List<ProductReview> productReviews = productReviewRepository.getManageProductCriteria(productReviewCriteria);
        List<ProductReview> productReviews = searchResponse.getProductReviews();
        List<ReviewFormSubmittedResponse> reviewFormSubmittedResponses = new ArrayList<>();
        for (ProductReview productReview : productReviews) {
            ReviewFormSubmittedResponse reviewFormSubmittedResponse = new ReviewFormSubmittedResponse();
            reviewFormSubmittedResponse.setAging(reviewAging(productReview.getSubmitDate()));
            reviewFormSubmittedResponse.setClientGroup(productReview.getClientGroup());
            reviewFormSubmittedResponse.setClientName(productReview.getClientName());
            reviewFormSubmittedResponse.setClientType(productReview.getClientType());
            reviewFormSubmittedResponse.setProductCategory(productReview.getProductCategory());
            reviewFormSubmittedResponse.setProductCategorySubType(productReview.getProductSubCategory());
            reviewFormSubmittedResponse.setReviewId(productReview.getId());
            reviewFormSubmittedResponse.setProductReviewStatus(productReview.getProductReviewStatus());
            reviewFormSubmittedResponse.setProductName(productReview.getProductName());
            reviewFormSubmittedResponse.setTemplateSubType(productReview.getTemplateSubType());
            reviewFormSubmittedResponse.setTemplateType(productReview.getTemplateType());
            reviewFormSubmittedResponse.setTemplateSubType(productReview.getTemplateSubType());
            reviewFormSubmittedResponse.setSubmitDate(productReview.getSubmitDate());
            reviewFormSubmittedResponse.setToDoTasks(getToDoTasksByReviewId(productReview.getId()));
            reviewFormSubmittedResponses.add(reviewFormSubmittedResponse);
        }
        searchResponse.setReviewFormSubmittedResponses(reviewFormSubmittedResponses);
        searchResponse.setProductReviews(null);
        return searchResponse;
        //return reviewFormSubmittedResponses;
    }


    private String getToDoTasksByReviewId(String id) {
        ToDoCriteria toDoCriteria = new ToDoCriteria();
        toDoCriteria.setReferenceId(id);
        toDoCriteria.setStatus(Status.ACTIVE);
        ToDoResponse toDoResponse = null;
        try {
            toDoResponse = toDoTaskService.getByCriteria(toDoCriteria);
        } catch (OperationException e) {
            e.printStackTrace();
        }
        ToDoTaskResponse toDoTaskResponse = null;
        if (toDoResponse != null) {
            toDoTaskResponse = toDoResponse.getData().get(0);
        }
        return (toDoTaskResponse == null) ? "" : toDoTaskResponse.getId();
    }

    private Long reviewAging(ZonedDateTime submitDate) {
        if (submitDate == null) {
            submitDate = ZonedDateTime.now(ZoneId.systemDefault());
        }
        LocalDate reviewCreateDate = submitDate.toLocalDate();
        LocalDate today = LocalDate.now();
        return ChronoUnit.DAYS.between(reviewCreateDate, today);
    }

    @Override
    public ProductReview getProductReviewById(String reviewId) {
        return productReviewRepository.getReviewByLatestVersion(reviewId);
    }

    @Override
    public String changeProductReviewStatus(ProductReviewStatusResource productReviewStatusResource) throws OperationException {

        String message = "Status of the review has been successfully updated and calculation of average rating for the review has been done";
        ProductReview productReview = getProductReviewById(productReviewStatusResource.getReviewId());

        if (productReview == null) {
            throw new OperationException(Constants.ER01, "No review found");
        }
        if (productReviewStatusResource.getProductReviewStatus().equals(ProdReviewStatus.PUBLISHED) && !productReview.getProductReviewStatus().equals(ProdReviewStatus.PUBLISHED)) {
            calculateAverageRating(productReview);
        } else {
            return "Review has already submitted and calculated rating";
        }

        productReview.setProductReviewStatus(productReviewStatusResource.getProductReviewStatus());

        productReviewRepository.saveOrUpdate(productReview);
        return message;
    }


    //calculating average review and storing in db
    private void calculateAverageRating(ProductReview productReview) {
        ProductReviewAverage productReviewAverage;
        ProductAverageCriteria productAverageCriteria = new ProductAverageCriteria();
        productAverageCriteria.setProductCategory(productReview.getProductCategory());
        productAverageCriteria.setProductName(productReview.getProductName());
        productAverageCriteria.setProductSubCategory(productReview.getProductSubCategory());
        productAverageCriteria.setCompanyID(productReview.getCompany());
        try {
            productReviewAverage = productReviewAverageRepository.getByCriteria(productAverageCriteria).get(0);
        } catch (Exception e) {
            productReviewAverage = new ProductReviewAverage();
            productReviewAverage.setCompanyID(productReview.getCompany());
            productReviewAverage.setProductCategory(productReview.getProductCategory());
            productReviewAverage.setProductCategorySubType(productReview.getProductSubCategory());
            productReviewAverage.setProductName(productReview.getProductName());
        }
        if (productReviewAverage.getAggregate() > 0.0) {
            productReviewAverage.setAggregate((productReviewAverage.getAggregate() * productReviewAverage.getCount() + Double.parseDouble(productReview.getReviewFormSubmitted().getOverallRating().getAnswer())) / (productReviewAverage.getCount() + 1));
        } else {
            productReviewAverage.setAggregate(Double.parseDouble(productReview.getReviewFormSubmitted().getOverallRating().getAnswer()));
        }
        List<QuestionAvgCount> questionAvgCounts = new ArrayList<>();
        for (RatingAnswer ratingAnswer : productReview.getReviewFormSubmitted().getRatingAnswer().getSubQuestions()) {
            try {
                for (QuestionAvgCount questionAvgCount : productReviewAverage.getQuestionAvgCounts()) {
                    if (questionAvgCount.getQuestion().equalsIgnoreCase(ratingAnswer.getQuestion())) {
                        questionAvgCount.setAverage((questionAvgCount.getAverage() * questionAvgCount.getCount() + Double.parseDouble(ratingAnswer.getAnswer())) / (questionAvgCount.getCount() + 1));
                        questionAvgCount.setProductReviewAverage(productReviewAverage);
                        questionAvgCount.setCount(questionAvgCount.getCount() + 1);
                    }
                }
            } catch (NullPointerException e) {
                QuestionAvgCount questionAvgCount1 = new QuestionAvgCount();
                questionAvgCount1.setQuestion(ratingAnswer.getQuestion());
                questionAvgCount1.setAverage(Double.valueOf(ratingAnswer.getAnswer()));
                questionAvgCount1.setCount(questionAvgCount1.getCount() + 1);
                questionAvgCount1.setProductReviewAverage(productReviewAverage);
                questionAvgCounts.add(questionAvgCount1);
            }
        }
        productReviewAverage.setQuestionAvgCounts(questionAvgCounts);
        productReviewAverage.setCount(productReviewAverage.getCount() + 1);
        for (QuestionAvgCount questionAvgCount : productReviewAverage.getQuestionAvgCounts()) {
            questionAvgCount.setProductReviewAverage(productReviewAverage);
        }
        productReviewAverageRepository.saveAndUpdate(productReviewAverage);
        productReview.getReviewFormSubmitted().setAggregate(productReviewAverage.getAggregate());
    }


    /*@Override
    public String deleteReview(String reviewId) throws OperationException {
        if (this.getProductReviewById(reviewId) != null) {
            productReviewRepository.deleteProductReview(reviewId);
            return "Review deleted successfully";
        } else {
            throw new OperationException(Constants.ER01, "No review Found");
        }
    }
*/
/*    @Transactional(noRollbackFor = Exception.class)
    public Map<String, String> submit(ReviewResponse reviewResponse) throws OperationException {
        CommentUpdateResource commentUpdateResource = new CommentUpdateResource();
        commentUpdateResource.setId(reviewResponse.getId());
        commentUpdateResource.setProductReviewUsers(reviewResponse.getProductReviewUsers());
        Map<String, String> map = new HashMap<>();
        ProductReview productReview = convert(reviewResponse);
        for (ProductReviewUser user : reviewResponse.getProductReviewUsers()) {
            if (user.getUserType().equals(OPS_USER) && !user.getDone()) {
                map = submitByOps(user, productReview);
                break;
            } else if (user.getUserType().equals(OPS_PRODUCT_USER) && !user.getDone()) {
                map = submitByProductUser(user, productReview);
                break;
            } else if (user.getUserType().equals(OPS_MARKETING_USER) && !user.getDone()) {
                //map = submitByMarketingUser(user, productReview);
                break;
            }
        }
        productReview.setProductReviewUsers(commentUpdateResource.getProductReviewUsers());
        productReviewRepository.saveOrUpdate(productReview);
        return map;
    }*/

    @Override
    public ProductReview updateStatus(String id, ProdReviewStatus prodReviewStatus) throws OperationException {
        ProductReview productReview = null;
        if (!StringUtils.isEmpty(id)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Product Review Id:" + id);
            }
            productReview = productReviewRepository.getReviewByLatestVersion(id);
            logger.info("Product Review Details" + productReview);
            if (productReview == null) {
                throw new OperationException(Constants.ER01, "Product Review not found with id" + id);

            }
            productReview.setProductReviewStatus(prodReviewStatus);
            productReviewRepository.saveOrUpdate(productReview);
        }
        return productReview;
    }

    @Override
    public Map<String, String> sendMailToCustomer(ClientMailResource clientMailResource) throws OperationException {
        MailTemplateResource mailTemplateResource = new MailTemplateResource();
        boolean flg = false;
        if (!StringUtils.isEmpty(clientMailResource.getBookID())) {

            mailTemplateResource.setBookID(clientMailResource.getBookID());
            flg = true;
        }
        if (!StringUtils.isEmpty(clientMailResource.getClientId()) && null != clientMailResource.getClientType()) {
            flg = true;
            mailTemplateResource.setClientId(clientMailResource.getClientId());
            mailTemplateResource.setClientType(clientMailResource.getClientType());
        }

        if (!flg) {
            throw new OperationException("Booking or Client Id required to send mail to client");

        }
        List<String> urls = new ArrayList<>();
        for (String id : clientMailResource.getIds()) {
            urls.add(templateUrl + id);
        }
        mailTemplateResource.setUrls(urls);
        Map<String, String> map = new HashMap<>();
        try {
            map = templateService.sendMailToClient(mailTemplateResource);
        } catch (OperationException e) {

            logger.error("Error while sending mail to client", e);
        }
        return map;
    }

    @Override
    public ReviewResponse getUIResponse(String id) {
        ProductReview productReview = getProductReviewById(id);
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setId(id);
        reviewResponse.setVersionNumber(productReview.getVersionNumber());
        QuestionResponse productTypeQuestions = new QuestionResponse();
        QuestionResponse productStyleQuestions = new QuestionResponse();
        QuestionResponse ratingQuestion = new QuestionResponse();
        QuestionRatingResponse overallRating = new QuestionRatingResponse();
        reviewResponse.setTemplateType(productReview.getTemplateType());
        reviewResponse.setTemplateSubType(productReview.getTemplateSubType());
        reviewResponse.setTemplateName(productReview.getTemplateName());
        reviewResponse.setClientName(productReview.getClientName());
        reviewResponse.setClientType(productReview.getClientType());
        reviewResponse.setClientCategory(productReview.getClientCategory());
        reviewResponse.setClientCategorySubType(productReview.getClientSubCategory());
        reviewResponse.setPassengerName(productReview.getPassengerName());
        reviewResponse.setCompanyMarket(productReview.getCompanyMarket());
        reviewResponse.setBookingReferenceNumber(productReview.getReviewFormSubmitted().getBookingReferenceNumber());
        reviewResponse.setNotBookedWithUs(productReview.getReviewFormSubmitted().isNotBookedWithUs());
        ProductDetailsResponse productDetailsResponse = new ProductDetailsResponse();
        productDetailsResponse.setTravelDate(productReview.getTravelDate());
        productDetailsResponse.setProductCategory(productReview.getProductCategory());
        productDetailsResponse.setProductCategorySubType(productReview.getProductSubCategory());
        productDetailsResponse.setTitleOfReview(productReview.getReviewFormSubmitted().getTitleOfYourReview());
        productDetailsResponse.setYourReview(productReview.getReviewFormSubmitted().getYourReview());
        productDetailsResponse.setProductName(productReview.getProductName());
        overallRating.setQuestion(productReview.getReviewFormSubmitted().getOverallRating().getQuestion());
        overallRating.setAnswer(Double.valueOf(productReview.getReviewFormSubmitted().getOverallRating().getAnswer()));
        overallRating.setId(productReview.getReviewFormSubmitted().getOverallRating().getId());
        overallRating.setType(productReview.getReviewFormSubmitted().getOverallRating().getButtonType());
        overallRating.setValues(productReview.getReviewFormSubmitted().getOverallRating().getPossibleValues());
        productDetailsResponse.setOverallRating(overallRating);
        productDetailsResponse.setTitleOfReview(productReview.getReviewFormSubmitted().getTitleOfYourReview());
        productDetailsResponse.setYourReview(productReview.getReviewFormSubmitted().getYourReview());
        ratingQuestion.setQuestion(productReview.getReviewFormSubmitted().getRatingAnswer().getQuestion());

        SubQuestionResponse subQuestionResponse = new SubQuestionResponse();
        subQuestionResponse.setLabelName(productReview.getReviewFormSubmitted().getRatingAnswer().getLabelName());
        List<SubQuestionDetails> subQuestionDetailsList = new ArrayList<>();
        for (RatingAnswer ratingAnswer : productReview.getReviewFormSubmitted().getRatingAnswer().getSubQuestions()) {
            SubQuestionDetails questionDetails = new SubQuestionDetails();
            questionDetails.setId(ratingAnswer.getId());
            questionDetails.setQuestion(ratingAnswer.getQuestion());
            questionDetails.setAnswer(ratingAnswer.getAnswer());
            questionDetails.setType(ratingAnswer.getButtonType());
            questionDetails.setValues(ratingAnswer.getPossibleValues());
            subQuestionDetailsList.add(questionDetails);
        }
        subQuestionResponse.setSubQuestionDetails(subQuestionDetailsList);
        ratingQuestion.setQuestion(productReview.getReviewFormSubmitted().getRatingAnswer().getQuestion());
        ratingQuestion.setSubQuestionResponse(subQuestionResponse);
        productDetailsResponse.setRatingQuestion(ratingQuestion);
        SubQuestionResponse subQuestionResponse1 = new SubQuestionResponse();
        List<SubQuestionDetails> subQuestionDetailsList1 = new ArrayList<>();
        for (ProductRelatedQuestionAnswer productRelatedQuestionAnswer : productReview.getReviewFormSubmitted().getProductRelatedQuestionAnswerList()) {
            SubQuestionDetails subQuestionDetails1 = new SubQuestionDetails();
            subQuestionDetails1.setId(productRelatedQuestionAnswer.getId());
            subQuestionDetails1.setQuestion(productRelatedQuestionAnswer.getQuestionId());
            subQuestionDetails1.setAnswer(String.valueOf(productRelatedQuestionAnswer.getPossibleValues().indexOf(productRelatedQuestionAnswer.getAnswer())));
            subQuestionDetails1.setType(productRelatedQuestionAnswer.getButtonType());
            subQuestionDetails1.setValues(productRelatedQuestionAnswer.getPossibleValues());
            subQuestionDetailsList1.add(subQuestionDetails1);

        }
        subQuestionResponse1.setSubQuestionDetails(subQuestionDetailsList1);
        productTypeQuestions.setSubQuestionResponse(subQuestionResponse1);
        productDetailsResponse.setProductTypeQuestions(productTypeQuestions);
        SubQuestionResponse subQuestionResponse2 = new SubQuestionResponse();
        subQuestionResponse2.setLabelName(productReview.getReviewFormSubmitted().getStyleQuestion().getLabel());
        List<SubQuestionDetails> subQuestionDetailsList2 = new ArrayList<>();
        for (ProductStyleQuestionAnswer productStyleQuestionAnswer : productReview.getReviewFormSubmitted().getStyleQuestion().getProductStyleQuestionAnswerList()) {
            SubQuestionDetails subQuestionDetails2 = new SubQuestionDetails();
            subQuestionDetails2.setId(productStyleQuestionAnswer.getId());
            subQuestionDetails2.setQuestion(productStyleQuestionAnswer.getQuestionId());
            subQuestionDetails2.setAnswer(String.valueOf(productStyleQuestionAnswer.getPossibleValues().indexOf(productStyleQuestionAnswer.getAnswer())));
            subQuestionDetails2.setValues(productStyleQuestionAnswer.getPossibleValues());
            subQuestionDetails2.setType(productStyleQuestionAnswer.getButtonType());
            subQuestionDetailsList2.add(subQuestionDetails2);
        }
        subQuestionResponse2.setSubQuestionDetails(subQuestionDetailsList2);
        productStyleQuestions.setSubQuestionResponse(subQuestionResponse2);
        productDetailsResponse.setProductStyleQuestions(productStyleQuestions);
        reviewResponse.setProductDetailsResponse(productDetailsResponse);
        reviewResponse.setCustomerAcceptanceRequired(productReview.isCustomerAcceptanceRequired());
        reviewResponse.setCustomerResponseFlg(productReview.getCustomerResponseFlag());
        reviewResponse.setStatus(productReview.getProductReviewStatus());
        reviewResponse.setProductReviewUsers(productReview.getProductReviewUsers());
        for (ProductReviewUser productReviewUser : reviewResponse.getProductReviewUsers()) {
            productReviewUser.setProductReview(productReview);
        }
        return reviewResponse;
    }

    @Override
    public List<String> getCompanyByUser(String userID) {
        String url = userManagementUrl + "/user/" + userID;
        String response = null;
        try {
            response = mdmRestUtils.exchange(url, HttpMethod.GET, String.class);
        } catch (OperationException e) {
            e.printStackTrace();
            logger.error("error in fetching details of user");
        }

        List<String> company = JsonPath.parse(response).read("$.userDetails.companies[?(@.defaultCompany == true)].companyName");
        return company;
    }
/*

    private Map<String, String> submitByOps(ProductReviewUser user, ProductReview productReview) throws OperationException {
        String message = "";
        if (user.isComplaintCreated()) {
            //TODO: Call complaint Master to create a complaint.
            message = "Complaint successfully created by Ops User";
        } else {
            if (createToDoTask(OPS_PRODUCT_USER, productReview)) {
                message = "successfully created TODO task for the Product user";
                user.setDone(true);
            } else {
                message = "To do task was not created";
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }
*/
/*
    private Map<String, String> submitByProductUser(ProductReviewUser user, ProductReview productReview) throws OperationException {
        String message = "";
        Map<String, String> map = new HashMap<>();
        if (user.isComplaintCreated()) {
            //TODO: Call complaint Master to create a complaint.
            message = "Complaint successfully created by Product User";
        } else {
            if (createToDoTask(OPS_MARKETING_USER, productReview)) {
                message = "Successfully created toDo task for the Marketing user";
                user.setDone(true);
            } else {
                message = "To do task was not created";
            }
        }
        map.put("message", message);
        return map;
    }*/


    private String submitByMarketingUser(ProductReviewUser productReviewUser, ProductReview productReview) throws OperationException {
        OpsBooking opsBooking = opsBookingService.getBooking(productReview.getReviewFormSubmitted().getBookingReferenceNumber());
        String message = "";
        if (productReviewUser.getComment().isEmpty() && productReviewUser.getComment() != null && !productReviewUser.isComplaintCreated() && !productReview.isCustomerAcceptanceRequired()) {
            productReview.setProductReviewStatus(ProdReviewStatus.PUBLISHED);
            calculateAverageRating(productReview);
            message = "marketing user did not added any comments, review successfully published";
        } else if (productReviewUser.isComplaintCreated()) {
            //TODO: Call complaint Master to create a complaint.
            message = "Complaint successfully created by Marketing user";
        } else if (productReview.isCustomerAcceptanceRequired()) {
            String clientMail = "";
            String clientName = null;
            if (opsBooking.getClientType().equalsIgnoreCase("B2B")) {
                clientMail = clientMasterDataService.getClientEmailId(opsBooking.getClientID(), MDMClientType.fromString(opsBooking.getClientType()));
                clientName = clientMasterDataService.getB2BClientNames(Collections.singletonList(opsBooking.getClientID())).get(opsBooking.getClientID());
            } else if (opsBooking.getClientType().equalsIgnoreCase("B2C")) {
                clientMail = clientMasterDataService.getClientEmailId(opsBooking.getClientID(), MDMClientType.fromString(opsBooking.getClientID()));
                clientName = clientMasterDataService.getB2CClientNames(Collections.singletonList(opsBooking.getClientID())).get(opsBooking.getClientID());
            }
            if (!clientMail.isEmpty()) {
                Map<String, String> dynamicVariables = new HashMap<>();
                dynamicVariables.put("client_name", clientName);
                String approveUrl = operationsBaseUrl + "/productReviews/v1/customerResponse/" + productReview.getId() + "/" + CustomerResponseFlg.CONFIRMED;
                String rejectUrl = operationsBaseUrl + "/productReviews/v1/customerResponse/" + productReview.getId() + "/" + CustomerResponseFlg.REJECTED;
                dynamicVariables.put("approve_url", approveUrl);
                dynamicVariables.put("reject_url", rejectUrl);
                EmailResponse emailResponse = null;
                try {
                    emailResponse = emailUtils.buildClientMail(function, approvalScenario, clientMail, "We require your Approval", dynamicVariables, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("Error in sending mail to client");
                }
                if (emailResponse != null && emailResponse.getStatus().equalsIgnoreCase("SUCCESS")) {
                    productReview.setCustomerResponseFlag(CustomerResponseFlg.SENT);
                    message = emailResponse.getMesssage();
                } else {
                    message = "Error while sending mail to client";
                }
                productReview.setProductReviewStatus(ProdReviewStatus.UN_VERIFIED);
                productReview.setVersionNumber(productReview.getVersionNumber() + 0.1f);
            } else {
                logger.error("Not able to send mail. Client Email is empty clientId: " + opsBooking.getClientID());
            }

        } else {
            productReview.setProductReviewStatus(ProdReviewStatus.PUBLISHED);
            calculateAverageRating(productReview);
            message = "review successfully published";
        }
        productReviewRepository.saveOrUpdate(productReview);
        return message;
    }

    @Override
    public String submitReview(CommentResource commentResource) throws OperationException {
        String message = "";
        ProductReview productReview = productReviewRepository.getReviewByLatestVersion(commentResource.getId());
        List<ProductReviewUser> productReviewUsers = productReview.getProductReviewUsers();
        List<ProductReviewUser> updatedProductReviewUser = new ArrayList<>();
        for (ProductReviewUser productReviewUser : productReviewUsers) {
            if (productReviewUser.getUserType().equals(commentResource.getProductReviewUser().getUserType())) {
                CopyUtils.copy(commentResource.getProductReviewUser(), productReviewUser);
                productReviewUser.setProductReview(productReview);
                productReviewUser.setDone(true);
                updatedProductReviewUser.add(productReviewUser);
            } else {
                productReviewUser.setProductReview(productReview);
                updatedProductReviewUser.add(productReviewUser);
            }

        }
        productReview.setProductReviewUsers(updatedProductReviewUser);
        productReview.setCustomerAcceptanceRequired(commentResource.getCustomerAcceptance());
        productReview.setProductReviewStatus(commentResource.getProdReviewStatus());
        if (commentResource.getProductReviewUser().isComplaintCreated()) {
            logger.info("Complaint created successfully");
        }
        if (commentResource.getProductReviewUser().getUserType().equals(OPS_USER)) {
            message = "Review Submitted successfully and a ToDo task is created for Product User";
            createToDoTask(OPS_PRODUCT_USER, productReview, commentResource.getUserId());
        } else if (commentResource.getProductReviewUser().getUserType().equals(OPS_PRODUCT_USER)) {
            message = "Review Submitted successfully and a ToDo task is created for Marketing User";
            createToDoTask(OPS_MARKETING_USER, productReview, commentResource.getUserId());
        } else if (commentResource.getProductReviewUser().getUserType().equals(OPS_MARKETING_USER)) {
            message = submitByMarketingUser(commentResource.getProductReviewUser(), productReview);
        }
        if (commentResource.getCustomerAcceptance()) {
            message = sendToCustomerForAcceptance(productReview);
        }
        productReviewRepository.saveOrUpdate(productReview);
        return message;
    }


    private Boolean createToDoTask(UserType user, ProductReview productReviewResource, String userId) throws OperationException {
        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        switch (user) {
            case OPS_USER:
                toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
                toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
                break;
            case OPS_PRODUCT_USER:
                toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.PRODUCT_USER.getValue());
                toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.SUB.getValue());
                break;
            case OPS_MARKETING_USER:
                toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.SALES_MARKETING.getValue());
                toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.SUB.getValue());
                break;
        }
        ToDoCriteria toDoCriteria = new ToDoCriteria();
        toDoCriteria.setReferenceId(productReviewResource.getId());
        toDoCriteria.setTaskFunctionalAreaId(toDoTaskResource.getTaskFunctionalAreaId());
        ToDoResponse toDoResponse = toDoTaskService.getByCriteria(toDoCriteria);
        if (toDoResponse.getData().size() == 0) {
            toDoTaskResource.setTaskNameId(ToDoTaskNameValues.VERIFICATION.getValue());
            toDoTaskResource.setProductId(productReviewResource.getOrderID());
            toDoTaskResource.setReferenceId(productReviewResource.getId());
            toDoTaskResource.setCompanyMarketId(productReviewResource.getCompanyMarket());
            toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.PRODUCT_REVIEW.toString());
            toDoTaskResource.setClientId(productReviewResource.getClientName());
            toDoTaskResource.setClientTypeId(productReviewResource.getClientType());
            toDoTaskResource.setClientCategoryId(productReviewResource.getClientCategory());
            toDoTaskResource.setClientSubCategoryId(productReviewResource.getClientSubCategory());
            toDoTaskResource.setCompanyId(productReviewResource.getCompany());
            if (userId != null) {
                toDoTaskResource.setFileHandlerId(userId);
            }
            toDoTaskResource.setDueOnDate(ZonedDateTime.now().plusDays(5));
            toDoTaskResource.setBookingRefId(productReviewResource.getReviewFormSubmitted().getBookingReferenceNumber());
            toDoTaskResource.setSuggestedActions("Review the task and verify, If necessary create a complaint");
            toDoTaskResource.setBookingRefId(productReviewResource.getReviewFormSubmitted().getBookingReferenceNumber());
            toDoTaskResource.setCreatedByUserId("SYSTEM");
            try {
                if (toDoTaskResource.getTaskFunctionalAreaId().equals(ToDoFunctionalAreaValues.PRODUCT_USER.getValue())) {
                    toDoCriteria.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
                    String mainTaskId = toDoTaskService.getByCriteria(toDoCriteria).getData().get(0).getId();
                    toDoTaskResource.setMainTaskId(mainTaskId);
                    toDoTaskService.updateToDoTaskStatus(productReviewResource.getId(), ToDoTaskSubTypeValues.PRODUCT_REVIEW, ToDoTaskStatusValues.COMPLETED);
                } else if (toDoTaskResource.getTaskFunctionalAreaId().equals(ToDoFunctionalAreaValues.SALES_MARKETING.getValue())) {
                    toDoCriteria.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
                    String mainTaskId = toDoTaskService.getByCriteria(toDoCriteria).getData().get(0).getId();
                    toDoTaskResource.setMainTaskId(mainTaskId);
                    toDoTaskService.updateToDoTaskStatus(productReviewResource.getId(), ToDoTaskSubTypeValues.PRODUCT_REVIEW, ToDoTaskStatusValues.COMPLETED);
                }
                toDoTaskService.save(toDoTaskResource);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //Converting UI response back to Product Review object
    private ProductReview convert(ReviewResponse reviewResponse) throws OperationException {
        ProductReview productReview = getProductReviewById(reviewResponse.getId());
        ReviewFormSubmitted reviewFormSubmitted = productReview.getReviewFormSubmitted();
        reviewFormSubmitted.getOverallRating().setAnswer(String.valueOf(reviewResponse.getProductDetailsResponse().getOverallRating().getAnswer()));
        List<ProductRelatedQuestionAnswer> productRelatedQuestionAnswerList = reviewFormSubmitted.getProductRelatedQuestionAnswerList();
        QuestionRating questionRating = reviewFormSubmitted.getRatingAnswer();
        List<ProductStyleQuestionAnswer> productStyleQuestionAnswerList = reviewFormSubmitted.getStyleQuestion().getProductStyleQuestionAnswerList();
        if (questionRating.getQuestion().equalsIgnoreCase(reviewResponse.getProductDetailsResponse().getRatingQuestion().getQuestion())) {
            for (RatingAnswer ratingAnswer : reviewFormSubmitted.getRatingAnswer().getSubQuestions()) {
                for (SubQuestionDetails subQuestionDetails : reviewResponse.getProductDetailsResponse().getRatingQuestion().getSubQuestionResponse().getSubQuestionDetails()) {
                    if (subQuestionDetails.getId().equals(ratingAnswer.getId())) {
                        ratingAnswer.setAnswer(subQuestionDetails.getAnswer());
                    }
                }
            }
        }

        for (ProductRelatedQuestionAnswer productRelatedQuestionAnswer : productRelatedQuestionAnswerList) {
            for (SubQuestionDetails subQuestionDetails : reviewResponse.getProductDetailsResponse().getProductTypeQuestions().getSubQuestionResponse().getSubQuestionDetails()) {
                if (subQuestionDetails.getId().equals(productRelatedQuestionAnswer.getId())) {
                    productRelatedQuestionAnswer.setAnswer(String.valueOf(subQuestionDetails.getValues().get(Integer.parseInt(subQuestionDetails.getAnswer()))));
                }
            }
        }
        for (ProductStyleQuestionAnswer productStyleQuestionAnswer : productStyleQuestionAnswerList) {
            for (SubQuestionDetails subQuestionDetails : reviewResponse.getProductDetailsResponse().getProductStyleQuestions().getSubQuestionResponse().getSubQuestionDetails()) {
                if (productStyleQuestionAnswer.getId().equals(subQuestionDetails.getId())) {
                    productStyleQuestionAnswer.setAnswer(String.valueOf(productStyleQuestionAnswer.getPossibleValues().get(Integer.parseInt(subQuestionDetails.getAnswer()))));
                }
            }
        }
        reviewFormSubmitted.setTitleOfYourReview(reviewResponse.getProductDetailsResponse().getTitleOfReview());
        reviewFormSubmitted.setYourReview(reviewResponse.getProductDetailsResponse().getYourReview());


        productReview.setProductReviewStatus(reviewResponse.getStatus());
        productReview.setReviewFormSubmitted(reviewFormSubmitted);
        productReview.setCustomerAcceptanceRequired(reviewResponse.getCustomerAcceptanceRequired());
        productReview.setCustomerResponseFlag(reviewResponse.getCustomerResponseFlg());


        for (ProductReviewUser productReviewUser : productReview.getProductReviewUsers()) {
            for (ProductReviewUser productReviewUser1 : reviewResponse.getProductReviewUsers()) {
                if (productReviewUser.getId().equals(productReviewUser1.getId())) {
                    productReviewUser.setComment(productReviewUser1.getComment());
                    productReviewUser.setDone(productReviewUser1.getDone());
                    productReviewUser.setComplaintCreated(productReviewUser1.isComplaintCreated());

                }
            }
        }
        return productReview;
    }


    private String sendToCustomerForAcceptance(ProductReview productReview) throws OperationException {
        String message = "";
        OpsBooking opsBooking = opsBookingService.getBooking(productReview.getReviewFormSubmitted().getBookingReferenceNumber());
        String clientMail = "seshu.thota@coxandkings.com";
        String clientName = null;
        if (opsBooking.getClientType().equalsIgnoreCase("B2B")) {
            clientMail = clientMasterDataService.getClientEmailId(opsBooking.getClientID(), MDMClientType.fromString(opsBooking.getClientType()));
            clientName = clientMasterDataService.getB2BClientNames(Collections.singletonList(opsBooking.getClientID())).get(opsBooking.getClientID());
        } else if (opsBooking.getClientType().equalsIgnoreCase("B2C")) {
            clientMail = clientMasterDataService.getClientEmailId(opsBooking.getClientID(), MDMClientType.fromString(opsBooking.getClientID()));
            clientName = clientMasterDataService.getB2CClientNames(Collections.singletonList(opsBooking.getClientID())).get(opsBooking.getClientID());
        }
        Map<String, String> dynamicVariables = new HashMap<>();
        dynamicVariables.put("client_name", clientName);
        String approveUrl = operationsBaseUrl + "/productReviews/v1/customerResponse/" + productReview.getId() + "/" + CustomerResponseFlg.CONFIRMED;
        String rejectUrl = operationsBaseUrl + "/productReviews/v1/customerResponse/" + productReview.getId() + "/" + CustomerResponseFlg.REJECTED;
        dynamicVariables.put("approve_url", approveUrl);
        dynamicVariables.put("reject_url", rejectUrl);
        EmailResponse emailResponse = null;
        try {
            emailResponse = emailUtils.buildClientMail(function, approvalScenario, clientMail, "We require your Approval", dynamicVariables, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error in sending mail to client");
        }
        if (emailResponse != null && emailResponse.getStatus().equalsIgnoreCase("SUCCESS")) {
            productReview.setCustomerResponseFlag(CustomerResponseFlg.SENT);
            message = emailResponse.getMesssage();
        } else {
            message = "Error while sending mail to client";
        }
        return message;
    }

    @Override
    public ProductReviewResposneClient getProductReviewByUserId(ProductReviewByUser productReviewByUser) {
        return productReviewRepository.getProductReviewsByUserId(productReviewByUser);
        
    }
}