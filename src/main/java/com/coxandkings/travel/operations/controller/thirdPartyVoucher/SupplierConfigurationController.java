package com.coxandkings.travel.operations.controller.thirdPartyVoucher;

import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.ReportGenerationCriteria;
import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.VoucherCodeSearchCriteria;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.FileType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.VoucherCode;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.SupplierConfigurationResource;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.UpdateSupplierConfigResource;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.UploadVouchersResource;
import com.coxandkings.travel.operations.response.thirdpartyvouchers.SupplierVoucherConfigSearchResponse;
import com.coxandkings.travel.operations.response.thirdpartyvouchers.ThirdPartyVouchersReportResponse;
import com.coxandkings.travel.operations.response.thirdpartyvouchers.UnitOfMeasurementResponse;
import com.coxandkings.travel.operations.response.thirdpartyvouchers.VoucherDetailsResponse;
import com.coxandkings.travel.operations.service.documentLibrary.DocumentLibraryService;
import com.coxandkings.travel.operations.service.thirdPartyVoucher.SupplierConfigurationService;
import net.minidev.json.parser.ParseException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/supplierConfigurations")
@CrossOrigin(value = "*")
public class SupplierConfigurationController {

    @Autowired
    private SupplierConfigurationService supplierConfigurationService;

    @Autowired
    private DocumentLibraryService documentLibraryService;

    //***  This method responsible for searching data from MDM-Workflow as well master db based on VoucherCodeSearchCriteria ***
    @PostMapping(value = "/v1/searchByCriteria")
    public ResponseEntity<Map<String, Object>> searchByCriteria(@RequestBody VoucherCodeSearchCriteria supplierConfigVoucherCodeSearchCriteria) throws OperationException, ParseException {
        return new ResponseEntity<Map<String, Object>>(supplierConfigurationService.searchByCriteria(supplierConfigVoucherCodeSearchCriteria), HttpStatus.OK);
    }

    //This method responsible to get data from master db based on id
    @GetMapping(value = "/v1/{id}")
    public ResponseEntity<SupplierVoucherConfigSearchResponse> get(@PathVariable(value = "id") String id) throws OperationException {
        return new ResponseEntity<SupplierVoucherConfigSearchResponse>(supplierConfigurationService.get(id), HttpStatus.OK);
    }

    //This method responsible for to get data from master db as well from MDM-workflow based on id
    @GetMapping(value = "/v2/{id}")
    public ResponseEntity<SupplierVoucherConfigSearchResponse> getVoucherCode(@PathVariable(value = "id") String id,
                                                                              @RequestParam(value = "workflow",required = false) boolean workflow) throws OperationException {
        return new ResponseEntity<SupplierVoucherConfigSearchResponse>(supplierConfigurationService.getVoucherCode(id,workflow), HttpStatus.OK);
    }


    //This method is reponsible to save data into Ops-DB
    @PostMapping(value = "/v1/save")
    public ResponseEntity<Map<String, Object>> add(@RequestBody SupplierConfigurationResource supplierConfigurationResource) throws OperationException {
        return new ResponseEntity<Map<String, Object>>(supplierConfigurationService.save(supplierConfigurationResource), HttpStatus.OK);
    }

    //*** This method is responsible for add-save,add-submit functionality. ***
    @PostMapping(value = "/v2/save")
    public ResponseEntity<Map<String, Object>> addV2(@RequestParam(value = "draft", required = false) boolean draft,
                                                     @RequestParam(value = "workflow", required = false) boolean workflow,
                                                     @RequestBody SupplierConfigurationResource supplierConfigurationResource) throws OperationException {
        return new ResponseEntity<>(supplierConfigurationService.addInWorkflow(supplierConfigurationResource, draft), HttpStatus.OK);
    }

    //*** This method is responsible to get data required for edit ***
    @GetMapping(value = "/v2/edit/{id}")
    public ResponseEntity<JSONObject> edit(@PathVariable(value = "id") String id,
                                           @RequestParam(value = "workflow",  required = false) boolean workflow,
                                           @RequestParam(value = "lock", required = false) boolean lock) throws OperationException, IOException, RepositoryException {
        return new ResponseEntity<JSONObject>(supplierConfigurationService.edit(id,workflow,lock), HttpStatus.OK);
    }

    //This method is responsible for edit-save, edit-submit functionality
    @PutMapping(value = "/v2/save/{id}")
    public ResponseEntity<Map<String, Object>> save(@PathVariable String id,
                                                    @RequestParam(value = "draft", required = false) boolean draft,
                                                    @RequestParam(value = "workflow", required = false) boolean workflow,
                                                    @RequestBody UpdateSupplierConfigResource updateSupplierConfigResource) throws OperationException, IOException, RepositoryException {

        //Updating the Workflow of Already existing master record
        return new ResponseEntity<>(supplierConfigurationService.updateWorkflow(id, updateSupplierConfigResource, draft,workflow), HttpStatus.OK);
    }

//    //This method is responsible for updateDataInto DB
//    @PutMapping(value = "/v2/save/{id}")
//    public ResponseEntity<Map<String, Object>> updateDataIntoDb(@RequestBody UpdateSupplierConfigResource updateSupplierConfigResource) throws OperationException {
//
//        //Updating the Workflow of Already existing master record
//        return new ResponseEntity<>(supplierConfigurationService.updateWorkflow(id, updateSupplierConfigResource, draft), HttpStatus.OK);
//    }
//
    @GetMapping(value = "/v2/releaseLock/{id}")
    public ResponseEntity<String> releaseLock(@PathVariable("id") String id) throws OperationException {
        return new ResponseEntity<>(supplierConfigurationService.releaseLock(id),HttpStatus.OK);
    }


    @PutMapping(value = "/v1/update")
    public ResponseEntity<SupplierVoucherConfigSearchResponse> update(@RequestBody UpdateSupplierConfigResource updateSupplierConfigResource) throws OperationException {
        return new ResponseEntity<SupplierVoucherConfigSearchResponse>(supplierConfigurationService.update(updateSupplierConfigResource), HttpStatus.OK);
    }


    @PutMapping(value = "/v2/update")
    public ResponseEntity<SupplierVoucherConfigSearchResponse> update(@RequestBody SupplierConfigurationResource supplierConfigurationResource) throws OperationException {
        return new ResponseEntity<SupplierVoucherConfigSearchResponse>(supplierConfigurationService.updateDb(supplierConfigurationResource), HttpStatus.OK);
    }


    @PutMapping(value = "/v1/upload")
    public ResponseEntity<Map<String, String>> upload(@RequestBody UploadVouchersResource uploadVouchersResource) throws OperationException {
        return new ResponseEntity<Map<String, String>>(supplierConfigurationService.updateVouchers(uploadVouchersResource), HttpStatus.OK);
    }

    @PutMapping(value = "/v2/upload")
    public ResponseEntity<Map<String, Object>> uploadVoucherCode(@RequestBody UploadVouchersResource uploadVouchersResource) throws OperationException {
        return new ResponseEntity<Map<String, Object>>(supplierConfigurationService.updateVoucherCode(uploadVouchersResource), HttpStatus.OK);
    }

    @PutMapping(value = "/v1/activateOrDeactivate")
    public ResponseEntity<SupplierVoucherConfigSearchResponse> activateOrDeactivate(@RequestParam(value = "supplierConfigId", required = true) String supplierConfigId,
                                                                                    @RequestParam(value = "voucherCode", required = true) List<String> voucherCode,
                                                                                    @RequestParam(value = "fileType", required = true) FileType fileType) throws OperationException {
        return new ResponseEntity<SupplierVoucherConfigSearchResponse>(supplierConfigurationService.activateOrDeactivate(supplierConfigId, voucherCode, fileType), HttpStatus.OK);

    }

    @GetMapping(value = "/v1/getVoucherCode")
    public ResponseEntity<VoucherCode> getVoucherCode(@RequestParam("supplierConfigId") String supplierConfigurationId,
                                                      @RequestParam("voucherCode") String voucherCode) throws OperationException {
        return new ResponseEntity<VoucherCode>(supplierConfigurationService.getVoucherCode(supplierConfigurationId, voucherCode), HttpStatus.OK);
    }

    @PostMapping(value = "/v1/reportGeneration")
    public ResponseEntity<Map<String,Object>> reportGeneration(@RequestBody ReportGenerationCriteria reportGenerationCriteria) throws OperationException {
        return new ResponseEntity<Map<String,Object>>(supplierConfigurationService.reportGeneration(reportGenerationCriteria), HttpStatus.OK);
    }

    @PostMapping(value = "/v1/export/type/{exportType}")
    public ResponseEntity<byte[]> exportReport(@PathVariable("exportType") String exportType, @RequestBody List<ThirdPartyVouchersReportResponse> list) throws OperationException, IOException, RepositoryException {
		MultipartFile file = supplierConfigurationService.exportReport(list, exportType);
		byte[] response = file.getBytes();
		

		HttpHeaders headers = new HttpHeaders();
		//headers.setAccessControlExposeHeaders(Arrays.asList("Content-Disposition","Content-Type"));
		headers.set("Content-Disposition", "attachment; filename=\"" + file.getName()+"\"");
		
		if (exportType.equalsIgnoreCase("pdf")) {
			headers.set("Content-Type", "application/pdf");
		} else {
			headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		}									

		return new ResponseEntity<>(response, headers, HttpStatus.OK);
	}   

    @GetMapping(value = "/v1/getMeasurementUnits")
    public ResponseEntity<List<UnitOfMeasurementResponse>> getMeasurementUnits() {
        return new ResponseEntity<List<UnitOfMeasurementResponse>>(supplierConfigurationService.getMeasurementUnits(), HttpStatus.OK);
    }

    @GetMapping(value = "/v1/getDocumentList")
    public ResponseEntity<List<String>> getDocumentList(@RequestParam("supplierConfigId") String supplierConfigId) throws OperationException {
        return new ResponseEntity<List<String>>(supplierConfigurationService.getDocumentsList(supplierConfigId), HttpStatus.OK);
    }

    @GetMapping(value = "/v1/getProductCategorySubType")
    public ResponseEntity<List> getProductCategorySubType(@RequestParam("supplierId") String supplierId,@RequestParam("productCategory") String productCategory) throws OperationException {
        return new ResponseEntity<List>(supplierConfigurationService.getProductCategorySubType(supplierId,productCategory),HttpStatus.OK);
    }

    @GetMapping(value = "/v1/getVoucherCodesOfVoucherFile")
    public ResponseEntity<Map<String,Object>> getVoucherCodeOfVoucherFile(@RequestParam("supplierConfigId") String supplierConfigId,
                                                                         @RequestParam("voucherFileName") String voucherFileName,
                                                                         @RequestParam("pageNo") Integer pageNo,
                                                                         @RequestParam("pageSize") Integer pageSize) throws OperationException{
        return new ResponseEntity<Map<String,Object>>(supplierConfigurationService.getVoucherCodeOfVoucherFile(supplierConfigId,voucherFileName,pageNo,pageSize),HttpStatus.OK);
    }


    @PutMapping(value = "/v2/deleteVoucherCode")
    public ResponseEntity<JSONObject> deleteVoucherCode(@RequestBody VoucherDetailsResponse voucherDetailsResponse) throws OperationException {
        return new ResponseEntity<>(supplierConfigurationService.deleteVoucherCode(voucherDetailsResponse),HttpStatus.OK);
    }
}
