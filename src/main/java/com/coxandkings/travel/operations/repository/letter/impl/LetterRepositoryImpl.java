package com.coxandkings.travel.operations.repository.letter.impl;

import com.coxandkings.travel.operations.criteria.communication.CommunicationCriteria;
import com.coxandkings.travel.operations.model.letter.Letter;
import com.coxandkings.travel.operations.repository.letter.LetterRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
public class LetterRepositoryImpl extends SimpleJpaRepository<Letter, String> implements LetterRepository {
    private EntityManager entityManager;

    public LetterRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(Letter.class, em);
        entityManager = em;
    }


    @Override
    public Letter saveLetter(Letter letter) {
        return this.saveAndFlush(letter);
    }

    @Override
    public Letter getLetterById(String id) {
        Letter letter = this.findOne(id);
        return letter;
    }

    @Override
    public List<Letter> getByCriteria(CommunicationCriteria communicationType) {

        return findAll();
    }
}
