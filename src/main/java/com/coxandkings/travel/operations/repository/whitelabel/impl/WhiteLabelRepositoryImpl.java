package com.coxandkings.travel.operations.repository.whitelabel.impl;

import com.coxandkings.travel.operations.criteria.whitelabel.WhiteLabelCriteria;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.repository.whitelabel.WhiteLabelRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("whiteLabelRepository")
public class WhiteLabelRepositoryImpl extends SimpleJpaRepository<WhiteLabel, String> implements WhiteLabelRepository{

    private EntityManager entityManager;

    public WhiteLabelRepositoryImpl(@Qualifier("opsEntityManager")EntityManager em) {
        super(WhiteLabel.class, em);
        this.entityManager = em;
    }


    @Override
    public WhiteLabel saveOrUpdate(WhiteLabel whiteLabel) {
        return this.saveAndFlush(whiteLabel);
    }

    @Override
    public WhiteLabel getWhiteLabelById(String id) {
        return this.findOne(id);
    }

    @Override
    public List<WhiteLabel> getAllWhiteLabels() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<WhiteLabel> whiteLabelCriteriaQuery=criteriaBuilder.createQuery(WhiteLabel.class);
        Root<WhiteLabel> root = whiteLabelCriteriaQuery.from(WhiteLabel.class);

        return entityManager.createQuery(whiteLabelCriteriaQuery).getResultList();
    }

    @Override
    public List<WhiteLabel> getWhiteLabelsByCriteria(WhiteLabelCriteria criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<WhiteLabel> whiteLabelCriteriaQuery=criteriaBuilder.createQuery(WhiteLabel.class);
        Root<WhiteLabel> root = whiteLabelCriteriaQuery.from(WhiteLabel.class);


        //As per wireframe document only sorting functionality added
        String sortCriteria = criteria.getSortCriteria();
        if(!StringUtils.isEmpty(sortCriteria)) {
            Boolean isDescending = criteria.getDescending();
            if(criteria.getDescending() == null){
                isDescending = false;
            }
            if(isDescending) {
                whiteLabelCriteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortCriteria)));
            }
            else{
                whiteLabelCriteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortCriteria)));
            }

        }
        return entityManager.createQuery(whiteLabelCriteriaQuery).getResultList();
    }

    @Override
    public void deleteById(String id) {
        delete(id);
        flush();
    }
}
