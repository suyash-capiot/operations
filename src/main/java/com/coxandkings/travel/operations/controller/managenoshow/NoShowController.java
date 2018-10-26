package com.coxandkings.travel.operations.controller.managenoshow;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.managenoshow.NoShowResource;
import com.coxandkings.travel.operations.service.managenoshow.NoShowService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/noShow")
public class NoShowController {

    @Autowired
    private NoShowService noShowService;

    @PostMapping("/v1/update")
    public ResponseEntity<MessageResource> updateNoShow(@RequestBody NoShowResource noShowResource) throws OperationException {
        MessageResource messageResource = new MessageResource();
        messageResource.setMessage(noShowService.updateNoShow(noShowResource));
        return new ResponseEntity<>(messageResource, HttpStatus.OK);
    }

    @GetMapping("/v1/isAttributeAlreadyApplied")
    public boolean isAttributeAlreadyApplied(@RequestParam String bookId, @RequestParam String orderId) throws OperationException {
        try {
            return noShowService.isNoShowAttributeAlreadyApplied(bookId, orderId);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20701);
        }
    }
}
