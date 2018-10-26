package com.coxandkings.travel.operations.controller.amendsuppliercommercial;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.amendsuppliercommercial.AmendSupplierCommercial;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.AmendSupplierCommercialApprovalResource;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.AmendSupplierCommercialMetaDataResource;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.AmendSupplierCommercialResource;
import com.coxandkings.travel.operations.service.amendsuppliercommercial.AmendSupplierCommercialMasterDataLoaderService;
import com.coxandkings.travel.operations.service.amendsuppliercommercial.AmendSupplierCommercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/amendSupplierCommercials")
@CrossOrigin("*")
public class AmendSupplierCommercialController {
    @Autowired
    private AmendSupplierCommercialService amendSupplierCommercialService;
    @Autowired
    private AmendSupplierCommercialMasterDataLoaderService dataLoaderService;

    @GetMapping("/v1/fetchScreenMetaData")
    public HttpEntity<AmendSupplierCommercialMetaDataResource> findSupplierCommercialHeads(
            @RequestParam(value = "bookingId") String bookingId,
            @RequestParam(value = "orderId") String orderId,
            @RequestParam(value = "roomId", required = false) String roomId,
            @RequestParam(value = "paxType", required = false) String paxType

    ) throws OperationException {
        AmendSupplierCommercialMetaDataResource screenMetaData = dataLoaderService.getScreenMetaData(
                bookingId, orderId, roomId, paxType);
        return new ResponseEntity<>(screenMetaData, HttpStatus.OK);
    }

    @PostMapping("/v1/save")
    public HttpEntity<MessageResource> create(@RequestBody AmendSupplierCommercialResource amendSupplierCommercialResource
    ) throws OperationException {
        amendSupplierCommercialService.save(amendSupplierCommercialResource);
        MessageResource message=new MessageResource();
		message.setMessage("Supplier Commercial Saved Successfully");
		return new ResponseEntity<MessageResource>(message,HttpStatus.CREATED);
		
    }

    @PostMapping("/v1/update")
    public HttpEntity<MessageResource> update(@RequestBody AmendSupplierCommercialResource supplierCommercial) throws OperationException {
        MessageResource messageResource = new MessageResource();
        amendSupplierCommercialService.update(supplierCommercial);
        messageResource.setMessage("Supplier Commercial updated Successfully");
        return new ResponseEntity<>(messageResource, HttpStatus.OK);
    }


    @GetMapping("/v1/{id}")
    public HttpEntity<AmendSupplierCommercial> getAmendSupplierCommercial(@PathVariable("id") String id
    ) throws OperationException {
        AmendSupplierCommercial amendSupplierCommercial = amendSupplierCommercialService.getAmendSupplierCommercial(id);
        return new ResponseEntity<>(amendSupplierCommercial, HttpStatus.OK);
    }

    //ToDo need API from Booking2 engine to calculate the new commercial

    @PostMapping("/v1/acco/calculateSupplierCommercial")
    public HttpEntity<AmendSupplierCommercial> calculateSupplierCommercialForAcco(
            @RequestBody AmendSupplierCommercialResource supplierCommercial
    ) throws OperationException, ParseException {
        AmendSupplierCommercial opsRoomSuppCommercial = amendSupplierCommercialService.
                calculateSupplierCommercialForAcco(supplierCommercial);


        return new ResponseEntity<>(opsRoomSuppCommercial, HttpStatus.OK);
    }

    //ToDo need API from Booking2 engine to calculate the new commercial

    @PostMapping("/v1/air/calculateSupplierCommercial")
    public HttpEntity<AmendSupplierCommercial> calculateSupplierCommercialForAir(
            @RequestBody AmendSupplierCommercialResource supplierCommercial
    ) throws OperationException, ParseException {
        AmendSupplierCommercial opsOrderSupplierCommercial =
                amendSupplierCommercialService.calculateSupplierCommercialForAir(supplierCommercial);
        return new ResponseEntity<>(opsOrderSupplierCommercial, HttpStatus.OK);
    }

    @PostMapping("/v1/approveOrReject")
    public HttpEntity<MessageResource> approveOrReject(
            @RequestBody AmendSupplierCommercialApprovalResource resource) throws OperationException {
        MessageResource messageResource = new MessageResource();
        messageResource.setMessage(amendSupplierCommercialService.approveOrReject(resource));
        return new ResponseEntity<>(messageResource, HttpStatus.ACCEPTED);
    }

  /*  @PostMapping("/v1/calculateMargin")
    public HttpEntity<SupplierCommercialPricingDetailResource> calculateMargin(
            @RequestBody OpsOrderDetails orderDetails) throws OperationException {
        return new ResponseEntity<>(amendSupplierCommercialService.calculateMargin(orderDetails), HttpStatus.OK);
    }*/

}
