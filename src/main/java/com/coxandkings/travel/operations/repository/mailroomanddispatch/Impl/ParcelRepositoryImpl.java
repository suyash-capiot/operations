package com.coxandkings.travel.operations.repository.mailroomanddispatch.Impl;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.SearchParcelCriteria;
import com.coxandkings.travel.operations.model.mailroomanddispatch.Parcel;
import com.coxandkings.travel.operations.repository.mailroomanddispatch.ParcelRepository;
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

@Transactional(readOnly = false)
@Repository
public class ParcelRepositoryImpl extends SimpleJpaRepository<Parcel, String> implements ParcelRepository {

    private EntityManager entityManager;

    public ParcelRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(Parcel.class, em);
        entityManager = em;
    }

    public Parcel saveOrUpdate(Parcel createParcel) {

        return saveAndFlush(createParcel);
    }

    public Parcel getParcelId(String parcelId) {

        return this.findOne(parcelId);
    }

    public List<Parcel> getParcels(){
        return this.findAll();
    }

    @Override
    public Parcel searchByCriteria(SearchParcelCriteria searchParcelCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Parcel> parcelCriteriaQuery = criteriaBuilder.createQuery(Parcel.class);
        Root<Parcel> root = parcelCriteriaQuery.from(Parcel.class);
        List<Predicate> predicates = new ArrayList<>();
        if (searchParcelCriteria.getParcelId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("parcelId"), searchParcelCriteria.getParcelId()));
        }
        if (!predicates.isEmpty()) {
            parcelCriteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        Parcel result = null;
        try {
            TypedQuery<Parcel> query = entityManager.createQuery(parcelCriteriaQuery);
            if (query != null) {
                result = query.getSingleResult();
            }
        } catch (NoResultException e) {
          //  e.printStackTrace();
            return result;
        }

        return result;

    }
}

