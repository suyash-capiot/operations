package com.coxandkings.travel.operations.repository.booking.impl;//package com.coxandkings.travel.operations.repository.booking.batchjob;
//
//import com.coxandkings.travel.operations.criteria.booking.BookingSearchCriteria;
//import com.coxandkings.travel.operations.model.booking.NewSellingPriceRecord;
//import com.coxandkings.travel.operations.repository.booking.BookingRepository;
//import org.apache.commons.collections.CollectionUtils;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
//import org.springframework.stereotype.Repository;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//import java.util.ArrayList;
//import java.util.List;
//
//@Repository
//public class BookingRepositoryImpl extends SimpleJpaRepository<Booking2, String> implements BookingRepository {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    public BookingRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
//        super(Booking2.class, entityManager);
//        this.entityManager=entityManager;
//    }
//
//    @Override
//    public Booking2 saveBooking(Booking2 booking) {
//       return this.saveAndFlush(booking);
//
//    }
//
//    @Override
//    public List<Booking2> getBookingsByCriteria(BookingSearchCriteria criteria) {
//        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
//        CriteriaQuery<Booking2> criteriaQuery=criteriaBuilder.createQuery(Booking2.class);
//        Root<Booking2> root=criteriaQuery.from(Booking2.class);
//        criteriaQuery.select(root);
//        List<Predicate> predicates = new ArrayList<>();
//        String[] ids = criteria.getIds();
//        if(ids !=null && ids.length > 0) {
//            Predicate id = root.get("id").in(ids);
//            predicates.add(id);
//        }
//
//        String[] excludeIds = criteria.getExcludeIds();
//        if(excludeIds !=null && excludeIds.length > 0) {
//            Predicate id = criteriaBuilder.not(root.get("id").in(ids));
//            predicates.add(id);
//        }
//
//        String[] bookingRefIds = criteria.getBookingRefId();
//        if(bookingRefIds != null && bookingRefIds.length > 0 ) {
//            predicates.add(root.get("bookingRefID").in(bookingRefIds));
//        }
//
//        if(!CollectionUtils.isEmpty(predicates)) {
//            criteriaQuery.where(predicates.toArray(new Predicate[0]));
//        }
//
//        return entityManager.createQuery(criteriaQuery).getResultList();
//    }
//
//
//}
