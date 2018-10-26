package com.coxandkings.travel.operations.repository.manageofflinebooking.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.manageofflinebooking.manualofflinebooking.OfflineProducts;
import com.coxandkings.travel.operations.model.manageofflinebooking.manualofflinebooking.OfflineSearch;
import com.coxandkings.travel.operations.repository.manageofflinebooking.ManualOfflineProductsRepository;
import com.coxandkings.travel.operations.repository.manageofflinebooking.ManualOfflineSearchRepository;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.manageOfflineBooking.MDMDataUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ManualOfflineSearchRepositoryImpl extends SimpleJpaRepository<OfflineSearch, String> implements ManualOfflineSearchRepository{

    private static Logger logger = LogManager.getLogger(ManualOfflineProductsRepositoryImpl.class);

    private EntityManager entityManager;

    @Autowired
    ManualOfflineProductsRepository productsRepository;

    public ManualOfflineSearchRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(OfflineSearch.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void saveCreateRequestDetails(OfflineSearch offlineBooking) throws OperationException {
        try {
            this.saveAndFlush(offlineBooking);
        } catch (Exception e) {
            logger.error("Exception :", e);
            throw new OperationException(Constants.TECHNICAL_ISSUE_REQUEST_SAVE);
        }
    }

    @Override
    public Map<String, Object>  searchOfflineBookings(JSONObject criteriaJson) throws OperationException {
        List<OfflineSearch> bookings =null;
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<OfflineSearch> criteriaQuery=criteriaBuilder.createQuery(OfflineSearch.class);
        Root<OfflineSearch> root=criteriaQuery.from(OfflineSearch.class);

        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if(MDMDataUtils.isStringNotNullAndNotEmpty(criteriaJson.optString("productCategory")))
            predicates.add(criteriaBuilder.equal(root.get("productCategory"), criteriaJson.getString("productCategory")));

        if(MDMDataUtils.isStringNotNullAndNotEmpty(criteriaJson.optString("productCategorySubType")))
            predicates.add(criteriaBuilder.equal(root.get("productSubCategory"), criteriaJson.getString("productCategorySubType")));

        if(MDMDataUtils.isStringNotNullAndNotEmpty(criteriaJson.optString("productCategory")))
            predicates.add(criteriaBuilder.equal(root.get("productCategory"), criteriaJson.getString("productCategory")));

        if(MDMDataUtils.isStringNotNullAndNotEmpty(criteriaJson.optString("productName")))
            predicates.add(criteriaBuilder.equal(root.get("productName"), criteriaJson.getString("productName")));

        if(MDMDataUtils.isStringNotNullAndNotEmpty(criteriaJson.optString("companyMarket")))
            predicates.add(criteriaBuilder.equal(root.get("companyMarket"), criteriaJson.getString("companyMarket")));

        if(MDMDataUtils.isStringNotNullAndNotEmpty(criteriaJson.optString("supplierName")))
            predicates.add(criteriaBuilder.equal(root.get("supplierName"), criteriaJson.getString("supplierName")));

        if(MDMDataUtils.isStringNotNullAndNotEmpty(criteriaJson.optString("clientType")))
            predicates.add(criteriaBuilder.equal(root.get("clienttype"), criteriaJson.getString("clientType")));

        if(MDMDataUtils.isStringNotNullAndNotEmpty(criteriaJson.optString("status")))
            predicates.add(criteriaBuilder.equal(root.get("bookingStatus"), criteriaJson.getString("status")));

        if(!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
            applySorting(criteriaJson, criteriaBuilder, criteriaQuery, root);

        }else{
            criteriaQuery.select(root);
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdTime")));
            applySorting(criteriaJson, criteriaBuilder, criteriaQuery, root);

        }

//        TypedQuery<OfflineSearch> query = entityManager.createQuery(criteriaQuery);
//
//        int startIndex = 0;
//
//        if (size != 0 && page != 0) {
//            startIndex = (page - 1) * size;
//            query.setFirstResult(startIndex);
//            query.setMaxResults(size);
//        }
        int size = criteriaJson.optInt("size");
        int page = Integer.parseInt(criteriaJson.optString("pageNumber"));
        bookings = entityManager.createQuery(criteriaQuery).getResultList();
        List<OfflineSearch> validBookings = validate(bookings);
        int actualSize = validBookings.size();
        return applyPagination(validBookings, size, page, actualSize);
    }

    private void applySorting(JSONObject criteriaJson, CriteriaBuilder criteriaBuilder, CriteriaQuery<OfflineSearch> criteriaQuery, Root<OfflineSearch> root) {
        if(MDMDataUtils.isStringNotNullAndNotEmpty(criteriaJson.optString("sortingField"))){
            // each time both order and sort field should be present..
            if(MDMDataUtils.isStringNotNullAndNotEmpty(criteriaJson.optString("sortingOrder"))){
                String sortingOrder = criteriaJson.getString("sortingOrder");
                switch (sortingOrder){
                    case "desc" : criteriaQuery.orderBy(criteriaBuilder.desc(root.get(criteriaJson.getString("sortingField"))));
                    break;
                    case "asc" :  criteriaQuery.orderBy(criteriaBuilder.asc(root.get(criteriaJson.getString("sortingField"))));
                    break;
                    default: criteriaQuery.orderBy(criteriaBuilder.desc(root.get(criteriaJson.getString("sortingField"))));
                }
            }else {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(criteriaJson.getString("sortingField"))));
            }

        }else if(MDMDataUtils.isStringNotNullAndNotEmpty(criteriaJson.optString("sortingOrder"))) {
            //when the search happens first time.. code come into this block.
            String sortingOrder = criteriaJson.getString("sortingOrder");
            switch (sortingOrder){
                case "desc" : criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdTime")));
                    break;
                case "asc" :  criteriaQuery.orderBy(criteriaBuilder.asc(root.get("createdTime")));
                    break;
                default: criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdTime")));
            }
        }else
        {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdTime")));
        }
    }

    @Override
    @Transactional
    public OfflineSearch getByProductId(String productId) throws OperationException {
        try{
            CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
            CriteriaQuery<OfflineSearch> criteriaQuery=criteriaBuilder.createQuery(OfflineSearch.class);
            Root<OfflineSearch> root=criteriaQuery.from(OfflineSearch.class);
            Predicate p1 = criteriaBuilder.and(criteriaBuilder.equal(root.get("productId"), productId));
            criteriaQuery.where(p1);
            return entityManager.createQuery( criteriaQuery ).getResultList().get(0);
        }catch (Exception e){
            return null;
        }
        
    }

    private List<OfflineSearch>validate(List<OfflineSearch> bookings)throws OperationException{
        List<OfflineSearch> validBookings = new ArrayList<>();
        OfflineProducts product = null;
        for(OfflineSearch booking:bookings){
            product = productsRepository.findById(booking.getProductId());
            if(product.getDeleted()!=null ){
                if(product.getDeleted()==false)
                    validBookings.add(booking);
            }
        }
        return validBookings;
    }

//    private Map<String, Object> applyPagination(List<OfflineSearch> bookings, Integer size, Integer page, Integer actualSize) {
//        Map<String, Object> entity = new HashMap<>();
//        entity.put("results", bookings);
//
//        if (bookings.isEmpty())
//            return entity;
//
//        entity.put("size", size);
//        entity.put("page", page);
//
//        Integer noOfPages = 0;
//        actualSize = (null == actualSize) ? 0 : actualSize;
//        size = (null == size) ? actualSize : size;
//        if (actualSize % size == 0)
//            noOfPages = actualSize / size;
//        else noOfPages = actualSize / size + 1;
//
//        entity.put("size", size);
//        entity.put("page", page);
//        entity.put("noOfPages", noOfPages);
//        return entity;
//    }

    private Map<String, Object> applyPagination(List<OfflineSearch> bookings, int size, int page, int actualSize) {
//        entity.put("results", bookings);

        if (bookings.isEmpty())
            return null;

//        entity.put("size", size);
//        entity.put("page", page);

        Integer noOfPages = 0;

        if (actualSize % size == 0)
            noOfPages = actualSize / size;
        else noOfPages = actualSize / size + 1;

        Map<String,Object> paginationResults =null;
        Map<Integer, Map<String,Object>> pageWiseBookings = new HashMap<>();
        for(int i=0;i<noOfPages;i++){
            paginationResults = new HashMap<>();
            paginationResults.put("size",size);
            paginationResults.put("page", page);
            paginationResults.put("noOfPages", noOfPages);
            int fromSize = (size*i);
            paginationResults.put("results",arrangeBookingsAsPerPage(bookings,fromSize,size));
            pageWiseBookings.put(new Integer(i),paginationResults);
        }

        return pageWiseBookings.get(new Integer(page-1));

    }

    private List<OfflineSearch> arrangeBookingsAsPerPage(List<OfflineSearch> bookings,int fromSize,int size){
        List<OfflineSearch> bookingsPerPage = new ArrayList<>();
        for(int i=0;i<size;i++){
            if(fromSize==bookings.size())
                return bookingsPerPage;
            else{
                bookingsPerPage.add(bookings.get(fromSize));
                fromSize++;
            }

        }

        return bookingsPerPage;
    }
}
