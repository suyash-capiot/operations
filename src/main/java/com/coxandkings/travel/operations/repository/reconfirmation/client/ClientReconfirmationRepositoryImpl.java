package com.coxandkings.travel.operations.repository.reconfirmation.client;

import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
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

@Service("clientReconfirmationRepository")
public class ClientReconfirmationRepositoryImpl extends SimpleJpaRepository<ClientReconfirmationDetails, String> implements ClientReconfirmationRepository {
    private EntityManager entityManager;


    public ClientReconfirmationRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(ClientReconfirmationDetails.class, em);
        entityManager = em;
    }

    @Transactional
    @Override
    public ClientReconfirmationDetails saveOrUpdateClientReconfirmation(ClientReconfirmationDetails clientReconfirmationDetails) {
        try {
            clientReconfirmationDetails.setTemplate("some template");
            clientReconfirmationDetails = this.saveAndFlush(clientReconfirmationDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientReconfirmationDetails;
    }

    @Override
    public ClientReconfirmationDetails findByBookRefAndOrderNo(String bookRefNo, String orderNo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaBuilder.createQuery(ClientReconfirmationDetails.class);
        CriteriaQuery<ClientReconfirmationDetails> clientReconfirmationDetailsQuery = null;
        Root<ClientReconfirmationDetails> root = null;
        TypedQuery<ClientReconfirmationDetails> query = null;
        try {
            if (entityManager != null) {
                criteriaBuilder = this.entityManager.getCriteriaBuilder();
                if (criteriaBuilder != null) {
                    clientReconfirmationDetailsQuery = criteriaBuilder.createQuery(ClientReconfirmationDetails.class);
                    root = clientReconfirmationDetailsQuery.from(ClientReconfirmationDetails.class);
                    Predicate bookRefNoP = criteriaBuilder.equal(root.get("bookRefNo"), bookRefNo);
                    Predicate orderNoP = criteriaBuilder.equal(root.get("orderID"), orderNo);
                    clientReconfirmationDetailsQuery.select(root).where(criteriaBuilder.and(bookRefNoP, orderNoP));
                }
            }
            query = this.entityManager.createQuery(clientReconfirmationDetailsQuery);
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
    public ClientReconfirmationDetails findByHash(String hash) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaBuilder.createQuery(ClientReconfirmationDetails.class);
        CriteriaQuery<ClientReconfirmationDetails> clientReconfirmationDetailsQuery = null;
        Root<ClientReconfirmationDetails> root = null;
        TypedQuery<ClientReconfirmationDetails> query = null;
        try {
            if (entityManager != null) {
                criteriaBuilder = this.entityManager.getCriteriaBuilder();
                if (criteriaBuilder != null) {
                    clientReconfirmationDetailsQuery = criteriaBuilder.createQuery(ClientReconfirmationDetails.class);
                    root = clientReconfirmationDetailsQuery.from(ClientReconfirmationDetails.class);
                    Predicate bookRefNoP = criteriaBuilder.equal(root.get("hash"), hash);
                    clientReconfirmationDetailsQuery.select(root).where(criteriaBuilder.and(bookRefNoP));
                }
            }
            query = this.entityManager.createQuery(clientReconfirmationDetailsQuery);
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
    public ClientReconfirmationDetails findByClientReconfirmationId(String reconfirmationID) {
        ClientReconfirmationDetails clientReconfirmationDetails = null;
        try {
            clientReconfirmationDetails = findOne(reconfirmationID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientReconfirmationDetails;
    }

    @Override
    public List<ClientReconfirmationDetails> getAllClientReconfirmation() {
        return this.findAll();
    }


    @Override
    public void deleteAllReconfirmation() {
        this.deleteAll();
    }

    @Override
    public void deleteReconfirmation(String id) {
        this.delete(id);

    }
}
