package com.coxandkings.travel.operations.repository.BookingHistory.impl;

import com.coxandkings.travel.operations.model.bookingHistory.BookingHistory;
import com.coxandkings.travel.operations.repository.BookingHistory.BookingHistoryRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class BookingHistoryRepositoryImpl  extends SimpleJpaRepository<BookingHistory, String> implements BookingHistoryRepository{

    private EntityManager entityManager;

    public BookingHistoryRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(BookingHistory.class, em);
        entityManager = em;
    }

    @Override
    @Transactional
    public BookingHistory saveOrUpdate(BookingHistory bookingHistory) {
        return this.saveAndFlush(bookingHistory);
    }

    @Override
    public   List<BookingHistory> getBookingHistory(String bookId) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookingHistory> criteriaQuery = criteriaBuilder.createQuery(BookingHistory.class);
        Root<BookingHistory> root = criteriaQuery.from(BookingHistory.class);
        criteriaQuery.select(root);

        Predicate predicate = criteriaBuilder.equal(root.get("bookId"), bookId);
        criteriaQuery.where(predicate);
        TypedQuery<BookingHistory> query = entityManager.createQuery(criteriaQuery);

        List<BookingHistory> bookingHistories = new ArrayList<>();
        try {
            bookingHistories = query.getResultList();
        }catch(NoResultException |IllegalArgumentException e){
            return bookingHistories;
        }
        return bookingHistories;

    }


}
