package com.coxandkings.travel.operations.controller.letter;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.model.letter.Letter;
import com.coxandkings.travel.operations.resource.communication.UpdateCommunicationTagsResource;
import com.coxandkings.travel.operations.resource.letter.LetterResource;
import com.coxandkings.travel.operations.service.letter.LetterService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/letters")
@CrossOrigin(origins = "*")
public class LetterController {
    @Autowired
    LetterService letterService;


    @PostMapping(value="/v1/add")
    public ResponseEntity<Letter> createLetter(@RequestBody (required = false) LetterResource letterResource) throws OperationException {

            Letter letter = letterService.save(letterResource);
            return new ResponseEntity<>(letter, HttpStatus.CREATED);

    }


    @GetMapping(value="/v1/{id}")
    public ResponseEntity<Letter> gettById(@PathVariable("id") String id) throws OperationException {
        try {
            Letter letter = letterService.getLetterById(id);
            return new ResponseEntity<>(letter, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_20401);
        }
    }

    @PutMapping(path="/v1/markasread")
    public ResponseEntity<Letter> markAsRead(@RequestParam String id) throws OperationException {
        try {
            Letter letter = letterService.markAsRead(id);
            return new ResponseEntity<>(letter, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_20402);
        }
    }

    @GetMapping(value="/v1/communicationtags/{id}")
    public ResponseEntity<CommunicationTags> getCommunicationTags(@PathVariable("id") String id) throws OperationException {
        try {
            CommunicationTags communicationTags = letterService.getAssociatedTags(id);
            return new ResponseEntity<>(communicationTags, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_20403);
        }
    }

    @PutMapping(value = "/v1/updatecommunicationtags")
    public ResponseEntity<Letter> getCommunicationTags(@RequestBody UpdateCommunicationTagsResource updateCommunicationTagsResource) throws OperationException {
        try {
            Letter letter = letterService.updateCommunicationTags(updateCommunicationTagsResource.getId(), updateCommunicationTagsResource.getCommunicationTags());
            return new ResponseEntity<>(letter, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_20404);
        }
    }
}
