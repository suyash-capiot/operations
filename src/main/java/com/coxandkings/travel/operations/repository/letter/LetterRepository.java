package com.coxandkings.travel.operations.repository.letter;

import com.coxandkings.travel.operations.criteria.communication.CommunicationCriteria;
import com.coxandkings.travel.operations.model.letter.Letter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LetterRepository {
    Letter saveLetter(Letter letter);
    Letter getLetterById(String id);

    List<Letter> getByCriteria(CommunicationCriteria communicationType);
}
