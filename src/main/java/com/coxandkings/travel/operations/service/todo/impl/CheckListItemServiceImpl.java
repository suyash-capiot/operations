package com.coxandkings.travel.operations.service.todo.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.todo.ToDoCheckListItem;
import com.coxandkings.travel.operations.repository.todo.CheckListItemRepository;
import com.coxandkings.travel.operations.resource.todo.CheckListItemResource;
import com.coxandkings.travel.operations.service.todo.CheckListItemService;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.coxandkings.travel.operations.utils.Constants.ER01;

@Service
public class CheckListItemServiceImpl implements CheckListItemService {
    private static Logger log = LogManager.getLogger(CheckListItemService.class);
    @Autowired
    public CheckListItemRepository checkListItemRepository;

    @Override
    public ToDoCheckListItem save(CheckListItemResource resource) throws OperationException {
        String id = resource.getId();
        ToDoCheckListItem listItem = null;
        if(!StringUtils.isEmpty(id)) {
            ToDoCheckListItem existingListItem = checkListItemRepository.getById(id);
            if(existingListItem == null) {
                log.error("Check list item not found for id:"+id);
                throw new OperationException(ER01);
            }
            CopyUtils.copy(resource, existingListItem);
            listItem = existingListItem;
        } else {
            listItem = new ToDoCheckListItem();
            CopyUtils.copy(resource, listItem);

        }

        return checkListItemRepository.saveOrUpdate(listItem);
    }
}
