package com.coxandkings.travel.operations.repository.email.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.email.Email;
import com.coxandkings.travel.operations.repository.email.EmailRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class EmailRepositoryImpl extends SimpleJpaRepository<Email, String> implements EmailRepository {

    private EntityManager entityManager;

    public EmailRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em)
    {
        super(Email.class, em);
        entityManager=em;
    }

    @Override
    @Transactional
    public Email saveOrUpdate(Email email) {
        return entityManager.merge(email);
    }

    @Override
    public Email getEmailById(String id) {
        return this.findOne(id);
    }

    @Override
    public List<Email> getEmailByMessageId(String messageId) throws OperationException {
        List<Email> emails= null;

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Email> criteriaQuery = criteriaBuilder.createQuery(Email.class);
        Root<Email> root = criteriaQuery.from(Email.class);
        criteriaQuery.select(root);

        Predicate predicate=criteriaBuilder.equal(root.get("messageId"),"<010b01d41a92$83e57f30$8bb07d90$@mahamuni@coxandkings.com>");
        criteriaQuery.where(predicate);

        emails= entityManager.createQuery(criteriaQuery).getResultList();
        return emails;
    }

    @Override
    public Email markAsRead(String id) {
        Email email = getEmailById(id);
        email.setRead(true);
        return this.saveAndFlush(email);
    }

    @Override
    public Email getRefMailId(String refMail) {
        List<Email> emails = (List<Email>) entityManager.createQuery("FROM Email e WHERE e.refMailId=:refMailId ").setParameter("refMailId", refMail).getResultList();
        if (emails != null && emails.size() > 0) {
            return emails.get(0);
        }
        return null;
    }
}
