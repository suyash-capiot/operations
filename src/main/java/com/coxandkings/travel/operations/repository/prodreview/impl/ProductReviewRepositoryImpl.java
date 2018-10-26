package com.coxandkings.travel.operations.repository.prodreview.impl;

import com.coxandkings.travel.operations.criteria.prodreview.ProductReviewCriteria;
import com.coxandkings.travel.operations.model.prodreview.ProductReview;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewByUser;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewVersion;
import com.coxandkings.travel.operations.model.prodreview.ReviewFormSubmitted;
import com.coxandkings.travel.operations.repository.prodreview.ProductReviewRepository;
import com.coxandkings.travel.operations.resource.prodreview.SearchProductReviewResponse;
import com.coxandkings.travel.operations.response.prodreview.ProductReviewResposneClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository("productReviewRepository")
@Transactional
public class ProductReviewRepositoryImpl extends SimpleJpaRepository<ProductReview, ProductReviewVersion> implements ProductReviewRepository {

    private static final Logger logger = LogManager.getLogger(ProductReviewRepository.class);
    private final EntityManager entityManager;

    public ProductReviewRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(ProductReview.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public ProductReview saveOrUpdate(ProductReview productReview) {
        return this.saveAndFlush(productReview);
    }


    public ProductReview getReviewByLatestVersion(String id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductReview> criteriaQuery = criteriaBuilder.createQuery(ProductReview.class);
        Root<ProductReview> root = criteriaQuery.from(ProductReview.class);

        Subquery<Integer> subQuery = criteriaQuery.subquery(Integer.class);
        Root<ProductReview> subRoot = subQuery.from(ProductReview.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("id"), id));

        List<Predicate> subPredicates = new ArrayList<>();

        Expression expression = criteriaBuilder.max(subRoot.get("versionNumber"));
        subQuery.select(expression);
        subQuery.groupBy(subRoot.get("id"));
        subPredicates.add(criteriaBuilder.equal(root.get("id"), subRoot.get("id")));
        subQuery.where(subPredicates.toArray(new Predicate[0]));
        predicates.add(criteriaBuilder.equal(root.get("versionNumber"), subQuery));

        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        ProductReview productReview = null;
        try {
            productReview = entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            logger.warn("No productReview Found for id" + id);
        }
        return productReview;
    }

    @Override
    public SearchProductReviewResponse getManageProductCriteria(ProductReviewCriteria productReviewCriteria) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductReview> criteriaQuery = criteriaBuilder.createQuery(ProductReview.class);
        Root<ProductReview> root = criteriaQuery.from(ProductReview.class);

        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<ProductReview> subRoot = subquery.from(ProductReview.class);

        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(productReviewCriteria.getTemplateType())) {
            predicates.add(criteriaBuilder.equal(root.get("templateType"), productReviewCriteria.getTemplateType()));
        }

        if (!StringUtils.isEmpty(productReviewCriteria.getTemplateSubType())) {
            predicates.add(criteriaBuilder.equal(root.get("templateSubType"), productReviewCriteria.getTemplateSubType()));
        }


        if (!StringUtils.isEmpty(productReviewCriteria.getClientGroupId())) {
            predicates.add(criteriaBuilder.equal(root.get("clientGroup"), productReviewCriteria.getClientGroupId()));
        }
        if (!StringUtils.isEmpty(productReviewCriteria.getClientId())) {
            predicates.add(criteriaBuilder.equal(root.get("clientId"), productReviewCriteria.getClientId()));
        }

        if (!StringUtils.isEmpty(productReviewCriteria.getClientNameId())) {
            predicates.add(criteriaBuilder.equal(root.get("clientName"), productReviewCriteria.getClientNameId()));

        }

        if (!StringUtils.isEmpty(productReviewCriteria.getClientType())) {
            predicates.add(criteriaBuilder.equal(root.get("clientType"), productReviewCriteria.getClientType()));

        }

        if (!StringUtils.isEmpty(productReviewCriteria.getCompanyMarketId())) {
            predicates.add(criteriaBuilder.equal(root.get("companyMarket"), productReviewCriteria.getCompanyMarketId()));

        }

        if (!StringUtils.isEmpty(productReviewCriteria.getFromDate()) && !StringUtils.isEmpty(productReviewCriteria.getToDate())) {
/*            //predicates.add(criteriaBuilder.equal(root.get("createdTime"), fromDate));
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("submitDate"), productReviewCriteria.getFromDate()));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("submitDate"), productReviewCriteria.getToDate()));*/
            predicates.add(criteriaBuilder.between(root.get("submitDate"), ZonedDateTime.parse(productReviewCriteria.getFromDate()), ZonedDateTime.parse(productReviewCriteria.getToDate())));

        } else if (productReviewCriteria.getFromDate() != null && productReviewCriteria.getToDate() == null) {
            predicates.add(criteriaBuilder.equal(root.get("submitDate"), productReviewCriteria.getFromDate()));
        }

        if (!StringUtils.isEmpty(productReviewCriteria.getProductCategory())) {
            predicates.add(criteriaBuilder.equal(root.get("productCategory"), productReviewCriteria.getProductCategory()));

        }
        if (!StringUtils.isEmpty(productReviewCriteria.getProductName())) {
            predicates.add(criteriaBuilder.equal(root.get("productName"), productReviewCriteria.getProductName()));

        }
        if (!StringUtils.isEmpty(productReviewCriteria.getProductSubCategory())) {
            predicates.add(criteriaBuilder.equal(root.get("productSubCategory"), productReviewCriteria.getProductSubCategory()));

        }
        predicates.add(criteriaBuilder.equal(root.get("deleted"), false));

        List<Predicate> subPredicates = new ArrayList<>();

        Expression expression = criteriaBuilder.max(subRoot.get("versionNumber"));
        subquery.select(expression);
        subquery.groupBy(subRoot.get("id"));
        subPredicates.add(criteriaBuilder.equal(root.get("id"), subRoot.get("id")));
        subquery.where(subPredicates.toArray(new Predicate[0]));

        predicates.add(criteriaBuilder.equal(root.get("versionNumber"), subquery));


        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        //criteriaQuery.distinct(true);
        if (productReviewCriteria.getOrder() != null && !StringUtils.isEmpty(productReviewCriteria.getSortingField())) {
            if (productReviewCriteria.getOrder().equalsIgnoreCase("desc")) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(productReviewCriteria.getSortingField())));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(productReviewCriteria.getSortingField())));
            }
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
        }

        JSONObject searchResponse = new JSONObject();

        Double numOfPages = 0.0;
        List<ProductReview> productReviews;
        SearchProductReviewResponse searchProductReviewResponse = new SearchProductReviewResponse();
        productReviews = entityManager.createQuery(criteriaQuery).getResultList();
        if (productReviews.size() != 0) {
            numOfPages = (Math.ceil(productReviews.size() / productReviewCriteria.getPageSize()) + 1);
        }
        final TypedQuery<ProductReview> query = entityManager
                .createQuery(criteriaQuery);

        productReviews = pagination(productReviewCriteria.getPageNumber(), productReviewCriteria.getPageSize(), query).getResultList();
        searchProductReviewResponse.setNumOfPages(numOfPages.intValue());
        searchProductReviewResponse.setProductReviews(productReviews);
        return searchProductReviewResponse;
    }

    private Join<ProductReview, ReviewFormSubmitted> getReviewSubmittedJoin(Root<ProductReview> root, Join<ProductReview, ReviewFormSubmitted> currentJoin) {
        if (currentJoin == null) {
            Join<ProductReview, ReviewFormSubmitted> join = root.join("reviewFormSubmitted");
            return join;
        } else {
            return currentJoin;
        }
    }

    @Override
    public ProductReview getReviewId(ProductReviewVersion productReviewVersion) {
        return this.findOne(productReviewVersion);
    }

    /*@Override
    public Boolean getExists(String id) {
        try {
            this.getOne(id);
        } catch (Exception e) {
            logger.error("could not find any review with the reference number");
            return false;
        }
        return true;
    }*/

/*    public void deleteProductReview(String reviewId) throws OperationException {

        ProductReview productReview = getReviewId(reviewId);
        productReview.setDeleted(true);
        this.saveOrUpdate(productReview);
        super.delete(reviewId);
    }*/

    private TypedQuery pagination(Integer pageNumber, Integer pageSize, TypedQuery<ProductReview> query) {
        if (pageNumber == null || pageNumber == 0) {
            pageNumber = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 10;
        }

        Integer firstResult = ((pageNumber - 1) * pageSize);
        query.setFirstResult(firstResult);
        query.setMaxResults(pageSize);
        return query;
    }

    @Override
    public ProductReviewResposneClient getProductReviewsByUserId(ProductReviewByUser productReviewByUser) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductReview> criteriaQuery = criteriaBuilder.createQuery(ProductReview.class);
        Root<ProductReview> root = criteriaQuery.from(ProductReview.class);

        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<ProductReview> subRoot = subquery.from(ProductReview.class);

        List<Predicate> predicates = new ArrayList<>();
        if (!StringUtils.isEmpty(productReviewByUser.getProductCategory())) {
            predicates.add(criteriaBuilder.equal(root.get("productCategory"), productReviewByUser.getProductCategory()));

        }
        if (!StringUtils.isEmpty(productReviewByUser.getUserId())) {
            predicates.add(criteriaBuilder.equal(root.get("clientId"), productReviewByUser.getUserId()));
        }
        predicates.add(criteriaBuilder.equal(root.get("deleted"), false));

        List<Predicate> subPredicates = new ArrayList<>();

        Expression expression = criteriaBuilder.max(subRoot.get("versionNumber"));
        subquery.select(expression);
        subquery.groupBy(subRoot.get("id"));
        subPredicates.add(criteriaBuilder.equal(root.get("id"), subRoot.get("id")));
        subquery.where(subPredicates.toArray(new Predicate[0]));

        //predicates.add(criteriaBuilder.equal(root.get("versionNumber"), subquery));
        

        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        List<Order> orderList = new ArrayList();
        orderList.add(criteriaBuilder.asc(root.get("productCategory")));
        orderList.add(criteriaBuilder.asc(root.get("submitDate")));
        criteriaQuery.orderBy(orderList);

        //criteriaQuery.distinct(true);


        Double numOfPages = 0.0;
        List<ProductReview> productReviews;

        productReviews = entityManager.createQuery(criteriaQuery).getResultList();
        if (productReviews.size() != 0) {
            numOfPages = (Math.ceil(productReviews.size() / productReviewByUser.getPageSize()) + 1);
        }
        final TypedQuery<ProductReview> query = entityManager
                .createQuery(criteriaQuery);

        productReviews = pagination(productReviewByUser.getPageNumber(), productReviewByUser.getPageSize(), query).getResultList();
        ProductReviewResposneClient productReviewResposneClient = new ProductReviewResposneClient();
        productReviewResposneClient.setNumOfPages(numOfPages.intValue());
        productReviewResposneClient.setProductReviewList(productReviews);
        return productReviewResposneClient;
    }
}
