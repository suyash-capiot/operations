package com.coxandkings.travel.operations.service.letter.impl;

import com.coxandkings.travel.operations.enums.communication.CommunicationType;
import com.coxandkings.travel.operations.enums.communication.Direction;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.model.letter.Letter;
import com.coxandkings.travel.operations.repository.letter.LetterRepository;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;
import com.coxandkings.travel.operations.resource.letter.LetterResource;
import com.coxandkings.travel.operations.service.letter.LetterService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class LetterServiceImpl implements LetterService {

    @Autowired
    private LetterRepository letterRepository;

    @Override
    public Letter save(LetterResource letterResource) throws OperationException {
        Letter letter = new Letter();
        if (letterResource != null) {

            letter.setAddress(letterResource.getAddress());
            letter.setSender(letterResource.getSender());
            letter.setRecipientList(Collections.singletonList(letterResource.getReceiver()));
            letter.setSubject(letterResource.getSubject());
            letter.setBody(letterResource.getDescription());
            letter.setCommunicationType(String.valueOf(CommunicationType.LETTER));
            letter.setBookId(letterResource.getBookId());
            letter.setUserId(letterResource.getUserId());
            letter.setFunction(letterResource.getFunction());
            letter.setProcess(letterResource.getProcess());
            letter.setScenario(letterResource.getScenario());
            letter.setProductSubCategory(letterResource.getProductSubCategory());
            letter.setSupplier(letterResource.getSupplier());
            letter.setDocumentRefIDs(letterResource.getDocumentRefIDs());

            if (letterResource.getDirection().equalsIgnoreCase(String.valueOf(Direction.INCOMING)))
                letter.setIs_outbound(false);
            else if(letterResource.getDirection().equalsIgnoreCase(String.valueOf(Direction.OUTGOING)))
                letter.setIs_outbound(true);

        }

        if (letterResource != null && letterResource.getCommunicationTagResource() != null) {
            CommunicationTags communicationTags = new CommunicationTags();
            communicationTags.setBaseCommunication(letter);
            CopyUtils.copy(letterResource.getCommunicationTagResource(), communicationTags);
            letter.setCommunicationTags(communicationTags);
        }

        return letterRepository.saveLetter(letter);
    }

    @Override
    public Letter getLetterById(String id) throws OperationException {
        Letter letter = null;
        if (id != null) {
            letter = letterRepository.getLetterById(id);
            if (letter == null) {
                throw new OperationException(Constants.ER01);
            }
        } else {
            throw new OperationException("Invalid input");
        }
        return letter;
    }

    @Override
    public Letter markAsRead(String id) throws OperationException {
        Letter letter = getLetterById(id);
        letter.setRead(true);
        letterRepository.saveLetter(letter);
        return letter;
    }

    @Override
    public CommunicationTags getAssociatedTags(String id) throws OperationException {
        Letter letter = getLetterById(id);
        return letter.getCommunicationTags();
    }

    @Override
    public Letter updateCommunicationTags(String id, CommunicationTagResource communicationTagResource) throws OperationException {
        Letter letter = getLetterById(id);
        CommunicationTags communicationTags = new CommunicationTags();
        CopyUtils.copy(communicationTagResource, communicationTags);
        letter.setCommunicationTags(communicationTags);
        return letterRepository.saveLetter(letter);
    }

}
