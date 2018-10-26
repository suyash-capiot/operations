package com.coxandkings.travel.operations.controller.changesuppliername;

import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskSubTypeValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.changesuppliername.DiscountOnSupplierPrice;
import com.coxandkings.travel.operations.model.changesuppliername.SupplementOnSupplierPrice;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.changesuppliername.ChangeSupplierPriceApprovalResource;
import com.coxandkings.travel.operations.resource.changesuppliername.ChangedSupplierPriceResource;
import com.coxandkings.travel.operations.resource.changesuppliername.SupplierResource;
import com.coxandkings.travel.operations.service.changesuppliername.ChangeSupplierNameService;
import com.coxandkings.travel.operations.service.changesuppliername.ChangeSupplierPriceService;
import com.coxandkings.travel.operations.utils.Constants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/changeSupplierName")
@CrossOrigin("*")
public class ChangeSupplierNameController {
	@Autowired
	private ChangeSupplierNameService changeSupplierNameService;
	@Autowired
	@Qualifier("changeSupplierPriceServiceImpl")
	private ChangeSupplierPriceService changeSupplierPriceService;

	@GetMapping("/v1/getSuppliers")
	public HttpEntity<List<SupplierResource>> getAllSupplier(@RequestParam("productCategory") String productCategory,
			@RequestParam("productSubCategory") String productSubCategory, @RequestParam("clientId") String clientId,
			@RequestParam("clientType") String clientType) throws OperationException {
		try {
			return new ResponseEntity<>(
					changeSupplierNameService.getSuppliers(productCategory, productSubCategory, clientId, clientType),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new OperationException(Constants.OPS_ERR_10900);
		}
	}

	@PostMapping("/v1/applyForAir")
	public HttpEntity<Object> applyRatesForAir(@RequestBody JSONObject supplierChangeRequest)
			throws ParseException, OperationException, IOException {
		try {
			Object supplierRates = changeSupplierNameService.applyRatesForAir(supplierChangeRequest);
			return new ResponseEntity<>(supplierRates, HttpStatus.OK);
		} catch (Exception e) {
			throw new OperationException(Constants.OPS_ERR_10901);
		}
	}

	@PostMapping("/v1/applyForAcco")
	public HttpEntity<Object> applyRatesForAcco(@RequestBody JSONObject supplierChangeRequest)
			throws ParseException, OperationException, IOException {
		try {
			Object supplierRates = changeSupplierNameService.applyRatesForAcco(supplierChangeRequest);
			return new ResponseEntity<>(supplierRates, HttpStatus.OK);
		} catch (Exception e) {
			throw new OperationException(Constants.OPS_ERR_10902);
		}
	}

	@GetMapping("/v1/getDefinedRates")
	public HttpEntity<JSONObject> getSupplierRates(@RequestParam("productCategory") OpsProductCategory productCategory,
			@RequestParam("productSubCategory") OpsProductSubCategory productSubCategory,
			@RequestParam("supplierId") String supplierId) throws OperationException {
		try {
			JSONObject supplierRates = changeSupplierNameService.getSupplierRates(productCategory, productSubCategory,
					supplierId);
			return new ResponseEntity<JSONObject>(supplierRates, HttpStatus.OK);
		} catch (Exception e) {
			throw new OperationException(Constants.OPS_ERR_10903);
		}
	}

	@PostMapping("/v1/getDiscount")
	public HttpEntity<ChangedSupplierPriceResource> getDiscountOnSupplierPrice(
			@RequestBody JSONObject discountOnSupplierPrice) throws OperationException {

		discountOnSupplierPrice.put("operation", ToDoTaskSubTypeValues.DISCOUNT_ON_SUPPLIER_PRICE.toString());
		ChangedSupplierPriceResource changedSupplierpriceResource = changeSupplierNameService
				.getDiscountOrAddSupplementOnSupplierPrice(discountOnSupplierPrice);
		return new ResponseEntity<>(changedSupplierpriceResource, HttpStatus.OK);

	}

	@PostMapping("/v1/addSupplement")
	public HttpEntity<ChangedSupplierPriceResource> addSupplementOnSupplierPrice(
			@RequestBody JSONObject supplementOnSupplierPrice) throws OperationException {

		supplementOnSupplierPrice.put("operation", ToDoTaskSubTypeValues.SUPPLEMENT_ON_SUPPLIER_PRICE.toString());
		ChangedSupplierPriceResource revisedPricingDetails = changeSupplierNameService
				.getDiscountOrAddSupplementOnSupplierPrice(supplementOnSupplierPrice);
		return new ResponseEntity<>(revisedPricingDetails, HttpStatus.OK);

	}

	@PostMapping("/v1/getMetaData")
	public HttpEntity<JSONObject> getScreenMetaData(@RequestBody JSONObject jsonResource) throws OperationException {

		JSONObject screenMetaData = changeSupplierNameService.getScreenMetaData(jsonResource);
		return new ResponseEntity<>(screenMetaData, HttpStatus.OK);

	}

	@PostMapping("/v1/saveDiscountOnSupplierPriceDetail")
	public HttpEntity<MessageResource> saveDiscountOnSupplierPriceDetail(
			@RequestBody DiscountOnSupplierPrice discountOnSupplierPriceJson) throws OperationException {
		MessageResource message = new MessageResource();
		message.setMessage("Discount On Supplier Price Saved Successfully");
		changeSupplierNameService.saveDiscountOnSupplierPriceDetail(discountOnSupplierPriceJson);
		return new ResponseEntity<MessageResource>(message, HttpStatus.CREATED);

	}

	@PostMapping("/v1/saveSupplementOnSupplierPriceDetail")
	public HttpEntity<MessageResource> saveSupplementOnSupplierPriceDetail(
			@RequestBody SupplementOnSupplierPrice supplementOnSupplierPrice) throws OperationException {

		MessageResource message = new MessageResource();
		message.setMessage("Supplement On Supplier Price Saved Successfully");
		changeSupplierNameService.saveSupplementOnSupplierPriceDetail(supplementOnSupplierPrice);
		return new ResponseEntity<MessageResource>(message, HttpStatus.CREATED);

	}

	@PostMapping("/v1/approveOrReject")
	public HttpEntity<JSONObject> acceptOrReject(@RequestBody ChangeSupplierPriceApprovalResource approvalResource)
			throws OperationException {
		JSONObject responseResource = null;

		responseResource = changeSupplierPriceService.approveOrReject(approvalResource);
		return new ResponseEntity<>(responseResource, HttpStatus.OK);

	}

	@GetMapping("/v1/client/accept")
	public HttpEntity<JSONObject> approveClientRequest(@RequestParam(name = "id", required = true) String identifier)
			throws OperationException {

		JSONObject responseResource = null;
		responseResource = changeSupplierPriceService.approveClientRequest(identifier);
		return new ResponseEntity<>(responseResource, HttpStatus.OK);

	}

	@GetMapping("/v1/client/reject")
	public HttpEntity<JSONObject> rejectClientRequest(@RequestParam(name = "id", required = true) String identifier)
			throws OperationException {

		JSONObject responseResource = null;
		responseResource = changeSupplierPriceService.rejectClientRequest(identifier);
		return new ResponseEntity<>(responseResource, HttpStatus.OK);

	}

	@GetMapping("/v1/supplements/{id}")
	public HttpEntity<SupplementOnSupplierPrice> getSupplement(
			@PathVariable(name = "id", required = true) String identifier) throws OperationException {

		return new ResponseEntity<>(changeSupplierPriceService.getSupplement(identifier), HttpStatus.OK);

	}

	@GetMapping("/v1/discounts/{id}")
	public HttpEntity<DiscountOnSupplierPrice> getDiscount(
			@PathVariable(name = "id", required = true) String identifier) throws OperationException {

		return new ResponseEntity<>(changeSupplierPriceService.getDiscount(identifier), HttpStatus.OK);

	}

}
