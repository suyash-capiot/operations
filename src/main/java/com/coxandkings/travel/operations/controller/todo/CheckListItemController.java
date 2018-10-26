package com.coxandkings.travel.operations.controller.todo;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.todo.ToDoCheckListItem;
import com.coxandkings.travel.operations.resource.todo.CheckListItemResource;
import com.coxandkings.travel.operations.service.todo.CheckListItemService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todo/checkList")
public class CheckListItemController {

    @Autowired
    CheckListItemService checkListItemService;

    private static Logger logger = Logger.getLogger( CheckListItemController.class );

    @PostMapping("/")
    public ResponseEntity<ToDoCheckListItem> createCheckListItem(@RequestBody CheckListItemResource checkListItemRequest) throws OperationException {
        try {
            ToDoCheckListItem toDoCheckListItem = checkListItemService.save(checkListItemRequest);
            return new ResponseEntity<ToDoCheckListItem>(toDoCheckListItem, HttpStatus.OK);
        } catch (Exception e) {
            logger.error( "Error code - OPS_ERR_31100; Error occurred in /todo/checkList", e );
            throw new OperationException(Constants.OPS_ERR_31100);
        }
    }
}
