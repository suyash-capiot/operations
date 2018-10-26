package com.coxandkings.travel.operations.repository.remarks.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.remarks.BookingRemarks;
import com.coxandkings.travel.operations.repository.remarks.BookingRemarksRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BookingRemarksRepositoryImpl extends SimpleJpaRepository<BookingRemarks, String> implements BookingRemarksRepository {

    private EntityManager entityManager;

    public BookingRemarksRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(BookingRemarks.class, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public BookingRemarks saveOrUpdateBookingRemarks(BookingRemarks bookingRemarks) {
        return this.saveAndFlush(bookingRemarks);
    }

    @Override
    public List<BookingRemarks> getAllBookingRemarks() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookingRemarks> bookingRemarksCriteriaQuery = criteriaBuilder.createQuery(BookingRemarks.class);
        Root<BookingRemarks> root = bookingRemarksCriteriaQuery.from(BookingRemarks.class);
        return entityManager.createQuery(bookingRemarksCriteriaQuery).getResultList();
    }

    @Override
    public BookingRemarks getBookingRemarksById(String remarkId) {
        return this.findOne(remarkId);
    }

    @Override
    public void deleteByRemarkId(String remarkId) {
        delete(remarkId);
        flush();
    }

    @Override
    public BookingRemarks findBookingRemarkByBookId(BookingRemarks bookingRemark) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaBuilder.createQuery(BookingRemarks.class);
        CriteriaQuery<BookingRemarks> bookingRemarks = null;
        Root<BookingRemarks> root = null;
        TypedQuery<BookingRemarks> query = null;
        try {
            if (entityManager != null) {
                criteriaBuilder = this.entityManager.getCriteriaBuilder();
                if (criteriaBuilder != null) {
                    bookingRemarks = criteriaBuilder.createQuery(BookingRemarks.class);
                    root = bookingRemarks.from(BookingRemarks.class);
                    Predicate bookRefNoP = criteriaBuilder.equal(root.get("bookId"), bookingRemark.getBookId());
                    bookingRemarks.select(root).where(bookRefNoP);
                }
            }
            query = this.entityManager.createQuery(bookingRemarks);
            if (query != null) {
                if (query.getSingleResult() != null) {
                    return query.getSingleResult();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (NonUniqueResultException e) {
            e.printStackTrace();
            return query.getResultList().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public List<BookingRemarks> getRemarksByBookId(String bookId) throws OperationException {
        try{
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<BookingRemarks> criteria = builder.createQuery(BookingRemarks.class);
            Root<BookingRemarks> root = criteria.from(BookingRemarks.class);
            Predicate p1 = builder.and(builder.equal(root.get("bookId"), bookId));
            criteria.where(p1);
            return entityManager.createQuery( criteria ).getResultList();
        }catch (Exception e){
            return null;
        }
    }
}
