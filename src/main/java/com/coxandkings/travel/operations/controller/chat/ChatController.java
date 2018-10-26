package com.coxandkings.travel.operations.controller.chat;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.chat.Chat;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.resource.chat.ChatResource;
import com.coxandkings.travel.operations.resource.communication.UpdateCommunicationTagsResource;
import com.coxandkings.travel.operations.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chats")
@CrossOrigin(origins = "*")
public class ChatController {
    @Autowired
    ChatService chatService;


    @PostMapping(value="/v1/add")
    public ResponseEntity<Chat> createChat(@RequestBody(required = false) ChatResource chatResouce)  {
        Chat chat=chatService.save(chatResouce);
        return new ResponseEntity<>(chat, HttpStatus.CREATED);
    }


    @GetMapping(value="/v1/{id}")
    public ResponseEntity<Chat> getChatById(@PathVariable("id") String id) throws OperationException {
        Chat chat=chatService.getChatById(id);
        return new ResponseEntity<>(chat,HttpStatus.OK);
    }

    @PutMapping(path="/v1/markasread")
    public ResponseEntity<Chat> markAsRead(@RequestParam String id) throws OperationException {
        Chat chat = chatService.markAsRead(id);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @GetMapping(value="/v1/communicationtags/{id}")
    public ResponseEntity<CommunicationTags> getCommunicationTags(@PathVariable("id") String id) throws OperationException {
        CommunicationTags communicationTags = chatService.getAssociatedTags(id);
        return new ResponseEntity<>(communicationTags,HttpStatus.OK);
    }

    @PutMapping(value = "/v1/updatecommunicationtags")
    public ResponseEntity<Chat> getCommunicationTags(@RequestBody UpdateCommunicationTagsResource updateCommunicationTagsResource) throws OperationException {
        Chat chat =  chatService.updateCommunicationTags(updateCommunicationTagsResource.getId(),updateCommunicationTagsResource.getCommunicationTags());
        return new ResponseEntity<>(chat,HttpStatus.OK);
    }

}
