package com.coxandkings.travel.operations.controller.amendcompanycommercial;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.companycommercial.AmendCompanyCommercial;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.amendentitycommercial.AmendCommercialMetaData;
import com.coxandkings.travel.operations.resource.amendentitycommercial.AmendCommercialResource;
import com.coxandkings.travel.operations.resource.amendentitycommercial.ChangeApprovalStatusResource;
import com.coxandkings.travel.operations.service.amendcompanycommercial.AmendCompanyCommercialMasterDataLoader;
import com.coxandkings.travel.operations.service.amendcompanycommercial.AmendCompanyCommercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companyCommercials")
@CrossOrigin(origins = "*")
public class AmendCompanyCommercialController {

	@Autowired
    private AmendCompanyCommercialMasterDataLoader dataLoaderService;
	@Autowired
	private AmendCompanyCommercialService amendCompanyCommercialService;


    @GetMapping("/v1/data")
    public ResponseEntity<AmendCommercialMetaData> getCompanyCommercials(@RequestParam(value = "bookingId") String bookingId,
                                                                         @RequestParam(value = "orderId") String orderId, @RequestParam(value = "uniqueId") String uniqueId) throws OperationException {

        AmendCommercialMetaData screenMetaData = dataLoaderService.getScreenMetaData(bookingId, orderId, uniqueId);
        return new ResponseEntity<AmendCommercialMetaData>(screenMetaData, HttpStatus.OK);
    }

    @PostMapping("/v1/apply")
    public ResponseEntity<AmendCommercialResource> applyCommercial(
            @RequestBody AmendCommercialResource amendCompanyCommercial) throws OperationException {
        AmendCommercialResource companyCommercial = amendCompanyCommercialService.apply(amendCompanyCommercial);
        return new ResponseEntity<>(companyCommercial, HttpStatus.OK);
    }

    @PostMapping("/v1/update")
    public ResponseEntity<MessageResource> updateBRMSCommercial(@RequestBody AmendCommercialResource amendCompanyCommercial
    ) throws OperationException {
        MessageResource message = new MessageResource();
        amendCompanyCommercialService.update(amendCompanyCommercial);
        message.setMessage("Company Commercial updated Successfully");
        return new ResponseEntity<>(message, HttpStatus.OK);

    }
    
    @PostMapping("/v1/save")
    public ResponseEntity<MessageResource> saveAmendedCommercial(@RequestBody AmendCommercialResource companyCommercial
    ) throws OperationException {
       MessageResource message=new MessageResource();
       amendCompanyCommercialService.save(companyCommercial);
       message.setMessage("Company Commercial Saved Successfully");
       return new ResponseEntity<>(message,HttpStatus.CREATED);       
    }
    
    @RequestMapping(value = "/v1/{id}", method = {RequestMethod.GET})
    public ResponseEntity<AmendCompanyCommercial> getCommercial(@PathVariable String id) throws OperationException {
    	AmendCompanyCommercial companyCommercial = amendCompanyCommercialService.getCommercial(id);
    	if(companyCommercial==null) {
    		return new ResponseEntity<AmendCompanyCommercial>(companyCommercial, HttpStatus.NO_CONTENT);
    	}
        return new ResponseEntity<AmendCompanyCommercial>(companyCommercial, HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "/v1/changeApprovalStatus", method = {RequestMethod.POST})
    public ResponseEntity<MessageResource> changeApprovalStatus(
            @RequestBody ChangeApprovalStatusResource approvalStatusResource) throws OperationException {
    	MessageResource message=new MessageResource();
    	message.setMessage(amendCompanyCommercialService.changeApprovalStatus(approvalStatusResource));
        return new ResponseEntity<>(message,HttpStatus.OK);
    }


}
