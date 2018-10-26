package com.coxandkings.travel.operations.controller.refund;

import com.coxandkings.travel.operations.enums.refund.ReasonForRequest;
import com.coxandkings.travel.operations.enums.refund.RefundStatus;
import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.refund.ChangeType;
import com.coxandkings.travel.operations.model.refund.Refund;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ClientDetail;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ClientType;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.refund.*;
import com.coxandkings.travel.operations.service.refund.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/refunds")
@CrossOrigin(value = "*")
public class RefundController {

    @Autowired
    private RefundService refundService;

    @PostMapping("/v1/save")
    public ResponseEntity<Refund> saveRefund(@RequestBody RefundResource refundResource) throws OperationException {

        Refund refund = null;

        refund = refundService.add(refundResource);

        return new ResponseEntity<Refund>(refund, HttpStatus.CREATED);

    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<Refund> getRefund(@PathVariable String id) throws OperationException {

        return new ResponseEntity<>(refundService.get(id), HttpStatus.OK);

    }

    @PutMapping("/v1/update")
    public ResponseEntity<Refund> updateRefund(@RequestBody RefundResource refundResource) throws OperationException {

        return new ResponseEntity<>(refundService.update(refundResource), HttpStatus.OK);

    }


    @PostMapping(value = "/v1/changeRefundType")
    public ResponseEntity changeRefundType(@RequestBody ChangeRefundTypeResponse changeRefundTypeResponse) throws OperationException {


        Map map = refundService.changeRefundType(changeRefundTypeResponse);
        return new ResponseEntity<Map>(map, HttpStatus.OK);

    }


    @GetMapping("/v1/getRefundTypes")
    public ResponseEntity<List<RefundTypesResource>> getRefundTypes() throws OperationException {

        List<RefundTypesResource> refundTypesResourceList = null;
        refundTypesResourceList = Arrays.asList(RefundTypes.values()).stream()
                .map(refundType -> new RefundTypesResource(refundType, refundType.getStatus()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(refundTypesResourceList, HttpStatus.OK);

    }

    @GetMapping(value = "/v1/getReasonForRequest")
    public ResponseEntity<List<ReasonForRequestResource>> getReasonForRequest() throws OperationException {

        List<ReasonForRequestResource> reasonForRequestResourceList = null;
        reasonForRequestResourceList = Arrays.asList(ReasonForRequest.values()).stream()
                .map(reasonForRequest -> new ReasonForRequestResource(reasonForRequest, reasonForRequest.getReason()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(reasonForRequestResourceList, HttpStatus.OK);

    }


    @GetMapping(value = "/v1/getRefundStatus")
    public ResponseEntity<List<RefundStatusResource>> getRefundStatus() throws OperationException {

        List<RefundStatusResource> refundStatusResourceList = null;
        refundStatusResourceList = Arrays.asList(RefundStatus.values()).stream().
                map(refundStatus -> new RefundStatusResource(refundStatus, refundStatus.getStatus()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(refundStatusResourceList, HttpStatus.OK);


    }

    @GetMapping(value = "/v1/getRefundsByBookingId")
    public ResponseEntity<List<Refund>> getRefundClaimsByBookingRefId(@RequestParam("bookingRefId") String bookingReference) throws OperationException {

        return new ResponseEntity<>(refundService.refundClaimsByBookingReferenceNo(bookingReference), HttpStatus.OK);

    }

    @PostMapping(value = "/v1/approve")
    public ResponseEntity<Map> approveChangeRefundType(@RequestBody ApprovalResource approvalResource) throws OperationException {

        return new ResponseEntity(refundService.approval(approvalResource), HttpStatus.OK);

    }

    @GetMapping(value = "/v1/getCreditNote/{creditNoteNo}")
    public ResponseEntity<String> getCrediNoteByNo(@PathVariable("creditNoteNo") String creditNoteNO) throws OperationException {


        return new ResponseEntity<String>(refundService.getCreditNote(creditNoteNO), HttpStatus.OK);

    }

    @GetMapping("/v1/client/{clientId}/{clientType}")
    public ResponseEntity<ClientDetail> getClientById(@PathVariable("clientId") String clientId, @PathVariable("clientType") String clientType) throws OperationException {


        return new ResponseEntity<ClientDetail>(refundService.getClientById(clientId, clientType), HttpStatus.OK);

    }


    @PostMapping("/v1/sendMailToClient")
    public ResponseEntity<MessageResource> sendMailToClient(@RequestBody SendMailResource sendMailResource) throws OperationException {


        return new ResponseEntity<MessageResource>(refundService.sendMailToClient(sendMailResource), HttpStatus.OK);

    }

    @GetMapping("/v1/getDefaultRefundType/{clientId}/{clientType}")
    public ResponseEntity<RefundTypes> getDefaultRefundType(@PathVariable("clientId") String clientId, @PathVariable("clientType") ClientType clientType) throws OperationException {

        return new ResponseEntity<RefundTypes>(refundService.getDefaultRefundType(clientId, clientType), HttpStatus.OK);

    }

    @GetMapping("/v1/getRefundChangeRequest/{refundClaimNo}")
    public ResponseEntity<ChangeType> getRefundChangeRequest(@PathVariable("refundClaimNo") String refundClaim) {
        return new ResponseEntity<>(refundService.getDefaultRefundType(refundClaim),HttpStatus.OK);
    }


}