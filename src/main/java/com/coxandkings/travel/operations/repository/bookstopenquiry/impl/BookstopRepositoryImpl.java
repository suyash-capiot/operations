package com.coxandkings.travel.operations.repository.bookstopenquiry.impl;


import com.coxandkings.travel.operations.criteria.bookstopenquiry.BookstopEnquiryCriteria;
import com.coxandkings.travel.operations.model.bookstopenquiry.BookstopEnquiry;
import com.coxandkings.travel.operations.repository.bookstopenquiry.BookstopEnquiryRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookstopRepositoryImpl extends SimpleJpaRepository<BookstopEnquiry, String> implements BookstopEnquiryRepository {

    private EntityManager entityManager;

    public BookstopRepositoryImpl(@Qualifier("opsEntityManager")EntityManager em) {
        super(BookstopEnquiry.class, em);
        this.entityManager = em;
    }

    @Override
    public BookstopEnquiry saveBookstopEnquiry(BookstopEnquiry bookstopEnquiry) {
        return this.saveAndFlush(bookstopEnquiry);
    }

    @Override
    public BookstopEnquiry updateBookstopEnquiry(BookstopEnquiry bookstopEnquiry) {
        return this.saveAndFlush(bookstopEnquiry);
    }

    @Override
    public List<BookstopEnquiry> getBookstopEnquiryByCriteria(BookstopEnquiryCriteria criteria) {
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<BookstopEnquiry> criteriaQuery=criteriaBuilder.createQuery(BookstopEnquiry.class);
        Root<BookstopEnquiry> root=criteriaQuery.from(BookstopEnquiry.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();


        if (!StringUtils.isEmpty(criteria.getClient_customer_id())) {
            predicates.add(criteriaBuilder.equal(root.get("client_customer_id"),criteria.getClient_customer_id()));
        }if (!StringUtils.isEmpty(criteria.getClient_customer_name())) {
            predicates.add(criteriaBuilder.equal(root.get("client_customer_name"),criteria.getClient_customer_name()));
        }if (!StringUtils.isEmpty(criteria.getEmailAddress())) {
            predicates.add(criteriaBuilder.equal(root.get("emailAddress"),criteria.getEmailAddress()));
        }if (!StringUtils.isEmpty(criteria.getPhoneNumber())) {
            predicates.add(criteriaBuilder.equal(root.get("phoneNumber"),criteria.getPhoneNumber()));
        }if (!StringUtils.isEmpty(criteria.getAddress1())) {
            predicates.add(criteriaBuilder.equal(root.get("address1"),criteria.getAddress1()));
        } if (!StringUtils.isEmpty(criteria.getAddress2())) {
            predicates.add(criteriaBuilder.equal(root.get("address2"),criteria.getAddress2()));
        }if (!StringUtils.isEmpty(criteria.getZipcode())) {
            predicates.add(criteriaBuilder.equal(root.get("zipcode"),criteria.getZipcode()));
        }if (!StringUtils.isEmpty(criteria.getReason())) {
            predicates.add(criteriaBuilder.equal(root.get("reason"),criteria.getReason()));
        }if (!StringUtils.isEmpty(criteria.getComments())) {
            predicates.add(criteriaBuilder.equal(root.get("comments"),criteria.getComments()));
        }if (!StringUtils.isEmpty(criteria.getStatus())) {
            predicates.add(criteriaBuilder.equal(root.get("status"),criteria.getStatus()));
        }

        if(!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public BookstopEnquiry getBookstopEnquiryById(String id) {
        return this.findOne(id);
    }

    @Override
    public BookstopEnquiry updateBookstopEnquiryStatusToClosed(String id) {
        BookstopEnquiry bookstopEnquiry = this.findOne(id);
        bookstopEnquiry.setStatus("CLOSED");
        return this.saveAndFlush(bookstopEnquiry);
    }
}
