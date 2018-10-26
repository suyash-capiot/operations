package com.coxandkings.travel.operations.controller.amendclientcommercial;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.clientcommercial.ClientCommercial;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.amendentitycommercial.AmendCommercialMetaData;
import com.coxandkings.travel.operations.resource.amendentitycommercial.AmendCommercialResource;
import com.coxandkings.travel.operations.resource.amendentitycommercial.ChangeApprovalStatusResource;
import com.coxandkings.travel.operations.service.amendclientcommercial.AmendClientCommercialService;
import com.coxandkings.travel.operations.service.amendclientcommercial.AmendClientCommercialsMasterDataLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientCommercials")
@CrossOrigin(origins = "*")
public class AmendClientCommercialController {

	@Autowired
	private AmendClientCommercialsMasterDataLoaderService dataLoaderService;
	@Autowired
	private AmendClientCommercialService amendClientCommercialService;

	@RequestMapping(value = "/v1/data", method = { RequestMethod.GET })
    public ResponseEntity<AmendCommercialMetaData> getClientCommercials(
            @RequestParam(value = "bookingId") String bookingId, @RequestParam(value = "orderId") String orderId,
            @RequestParam(value = "uniqueId") String uniqueId) throws OperationException {
        AmendCommercialMetaData clientCommercials = dataLoaderService.getScreenMetaData(bookingId, orderId,
				uniqueId);
        return new ResponseEntity<AmendCommercialMetaData>(clientCommercials, HttpStatus.OK);
	}

	@PostMapping("/v1/apply")
    public ResponseEntity<AmendCommercialResource> apply(
            @RequestBody AmendCommercialResource amendClientCommercialResource)
            throws OperationException {
        AmendCommercialResource clientCommercial = amendClientCommercialService
				.apply(amendClientCommercialResource);
		return new ResponseEntity<>(clientCommercial, HttpStatus.OK);
	}

    @PostMapping("/v1/update")
    public ResponseEntity<MessageResource> updateBRMSCommercial(@RequestBody AmendCommercialResource amendCompanyCommercial
    ) throws OperationException {
        MessageResource message = new MessageResource();
        amendClientCommercialService.update(amendCompanyCommercial);
        message.setMessage("Client Commercial updated Successfully");
        return new ResponseEntity<>(message, HttpStatus.OK);

    }


	@PostMapping("/v1/save")
    public ResponseEntity<MessageResource> saveAmendedCommercial(
            @RequestBody AmendCommercialResource amendClientCommercialResource)
            throws OperationException {
		MessageResource message=new MessageResource();
		amendClientCommercialService.save(amendClientCommercialResource);
		message.setMessage("Client Commercial Saved Successfully");
		return new ResponseEntity<MessageResource>(message,HttpStatus.CREATED);
	}

	@RequestMapping(value = "/v1/{id}", method = { RequestMethod.GET })
	public ResponseEntity<ClientCommercial> getCommercial(@PathVariable String id) throws OperationException {
		ClientCommercial clientCommercial = amendClientCommercialService.getCommercial(id);
		if (clientCommercial == null) {
			return new ResponseEntity<ClientCommercial>(clientCommercial, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<ClientCommercial>(clientCommercial, HttpStatus.OK);
	}

	@RequestMapping(value = "/v1/changeApprovalStatus", method = { RequestMethod.POST })
    public ResponseEntity<MessageResource> changeApprovalStatus(
			@RequestBody ChangeApprovalStatusResource approvalStatusResource)
            throws OperationException {
		MessageResource message = new MessageResource();
		message.setMessage(amendClientCommercialService.changeApprovalStatus(approvalStatusResource));
		return new ResponseEntity<>(message, HttpStatus.OK);
	}


}