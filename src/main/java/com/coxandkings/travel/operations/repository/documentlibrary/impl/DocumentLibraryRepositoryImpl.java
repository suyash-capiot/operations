package com.coxandkings.travel.operations.repository.documentlibrary.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.coxandkings.travel.operations.model.documentlibrary.NewDocument;
import com.coxandkings.travel.operations.repository.documentlibrary.DocumentLibraryRepository;

@Transactional
@Repository
public class DocumentLibraryRepositoryImpl extends SimpleJpaRepository<NewDocument, String> implements DocumentLibraryRepository {

	private EntityManager entityManager;

    public DocumentLibraryRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(NewDocument.class, em);
        entityManager = em;
    }

	@Override
	public NewDocument getById(String id) {
		return this.findOne(id);
	}

	@Override
	public NewDocument saveOrUpdate(NewDocument document) {
		return this.saveAndFlush(document);
	}

	@Override
	public NewDocument update(NewDocument document) {
		return this.saveAndFlush(document);
	}

	@Override
	public NewDocument getByCriteria(NewDocument document) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NewDocument> criteriaQuery = criteriaBuilder.createQuery(NewDocument.class);
        Root<NewDocument> root = criteriaQuery.from(NewDocument.class);
        criteriaQuery.select(root);
        
        List<Predicate> predicateList = new ArrayList<>();
        
        if (!StringUtils.isEmpty(document.getBookId()))
            predicateList.add(criteriaBuilder.equal(root.get("bookId"), document.getBookId()));
        
        if (!StringUtils.isEmpty(document.getCategory()))
            predicateList.add(criteriaBuilder.equal(root.get("category"), document.getCategory()));
        
        if (!StringUtils.isEmpty(document.getClientId()))
            predicateList.add(criteriaBuilder.equal(root.get("clientId"), document.getClientId()));
        
        if (!StringUtils.isEmpty(document.getDescription()))
            predicateList.add(criteriaBuilder.equal(root.get("description"), document.getDescription()));
        
        if (!StringUtils.isEmpty(document.getExtension()))
            predicateList.add(criteriaBuilder.equal(root.get("extension"), document.getExtension()));
        
        if (!StringUtils.isEmpty(document.getName()))
            predicateList.add(criteriaBuilder.equal(root.get("name"), document.getName()));
        
        if (!StringUtils.isEmpty(document.getPath()))
            predicateList.add(criteriaBuilder.equal(root.get("path"), document.getPath()));
        
        if (!StringUtils.isEmpty(document.getSubCategory()))
            predicateList.add(criteriaBuilder.equal(root.get("subCategory"), document.getSubCategory()));
        
        if (!StringUtils.isEmpty(document.getType()))
            predicateList.add(criteriaBuilder.equal(root.get("type"), document.getType()));
        
        if (!StringUtils.isEmpty(document.getUrl()))
            predicateList.add(criteriaBuilder.equal(root.get("url"), document.getUrl()));
        
        if (!StringUtils.isEmpty(document.getVersion()))
            predicateList.add(criteriaBuilder.equal(root.get("version"), document.getVersion()));
        


        criteriaQuery.where(predicateList.toArray(new Predicate[0]));
        TypedQuery<NewDocument> query = entityManager.createQuery(criteriaQuery);

        NewDocument newDocument;
        try {
        	newDocument = query.getSingleResult();
        }catch(NoResultException|IllegalArgumentException e){
            return null;
        }
        return newDocument;
	}

}
