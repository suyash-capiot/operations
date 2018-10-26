package com.coxandkings.travel.operations.controller.supplierbillpassing;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.creditDebitNote.CreditDebitNote;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CreditDebitNoteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/creditDebitNote")
public class CreditDebitNoteController {

    @Autowired
    private CreditDebitNoteUtils creditDebitNoteUtils;

    @GetMapping("/v1/get")
    public ResponseEntity<CreditDebitNote> get(@RequestParam String id) throws OperationException {
        try {
            return creditDebitNoteUtils.get(id);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30801);
        }
    }
}
