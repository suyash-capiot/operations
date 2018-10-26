package com.coxandkings.travel.operations.repository.reconfirmation.supplier;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Service("sSReconfirmationRepository")
public class SupplierReconfirmationRepositoryImpl extends SimpleJpaRepository<SupplierReconfirmationDetails, String> implements SupplierReconfirmationRepository {
    private EntityManager entityManager;


    public SupplierReconfirmationRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(SupplierReconfirmationDetails.class, em);
        entityManager = em;
    }

    @Transactional
    @Override
    public SupplierReconfirmationDetails saveOrUpdateSupplierReconfirmation(SupplierReconfirmationDetails supplierReconfirmation) {
        SupplierReconfirmationDetails supplierReconfirmationDetails = null;
        try {
            supplierReconfirmationDetails = this.saveAndFlush(supplierReconfirmation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplierReconfirmationDetails;
    }

    @Override
    public SupplierReconfirmationDetails findByBookRefAndOrderNo(String bookRefNo, String orderNo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaBuilder.createQuery(SupplierReconfirmationDetails.class);
        CriteriaQuery<SupplierReconfirmationDetails> supplierReconfirmationDetailsQuery = null;
        Root<SupplierReconfirmationDetails> root = null;
        TypedQuery<SupplierReconfirmationDetails> query = null;
        try {
            if (entityManager != null) {
                criteriaBuilder = this.entityManager.getCriteriaBuilder();
                if (criteriaBuilder != null) {
                    supplierReconfirmationDetailsQuery = criteriaBuilder.createQuery(SupplierReconfirmationDetails.class);
                    root = supplierReconfirmationDetailsQuery.from(SupplierReconfirmationDetails.class);
                    supplierReconfirmationDetailsQuery.select(root);
                    Predicate bookRefNoP = criteriaBuilder.equal(root.get("bookRefNo"), bookRefNo);
                    Predicate orderNoP = criteriaBuilder.equal(root.get("orderID"), orderNo);
                    supplierReconfirmationDetailsQuery.where(criteriaBuilder.and(bookRefNoP, orderNoP));
                }
            }
            query = this.entityManager.createQuery(supplierReconfirmationDetailsQuery);
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
    public List<SupplierReconfirmationDetails> getAllSupplierReconfirmation() {
        return this.findAll();
    }

    @Override
    public SupplierReconfirmationDetails findBySupplierReconfirmationId(String reconfirmationID) throws OperationException {
        SupplierReconfirmationDetails supplierReconDetails = null;
        try {
            supplierReconDetails = findOne(reconfirmationID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (supplierReconDetails == null) {
            throw new OperationException(Constants.ER316);
        }
        return supplierReconDetails;
    }

    @Override
    public SupplierReconfirmationDetails findByHash(String hash) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaBuilder.createQuery(SupplierReconfirmationDetails.class);
        CriteriaQuery<SupplierReconfirmationDetails> supplierReconfirmationDetailsQuery = null;
        Root<SupplierReconfirmationDetails> root = null;
        TypedQuery<SupplierReconfirmationDetails> query = null;
        try {
            if (entityManager != null) {
                criteriaBuilder = this.entityManager.getCriteriaBuilder();
                if (criteriaBuilder != null) {
                    supplierReconfirmationDetailsQuery = criteriaBuilder.createQuery(SupplierReconfirmationDetails.class);
                    root = supplierReconfirmationDetailsQuery.from(SupplierReconfirmationDetails.class);
                    Predicate bookRefNoP = criteriaBuilder.equal(root.get("hash"), hash);
                    supplierReconfirmationDetailsQuery.select(root).where(bookRefNoP);
                }
            }
            query = this.entityManager.createQuery(supplierReconfirmationDetailsQuery);
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
    public void deleteAllReconfirmation() {
        this.deleteAll();
    }

    @Override
    public void deleteReconfirmation(String id){
        SupplierReconfirmationDetails one = this.findOne(id);
        this.delete(one);

    }

}
