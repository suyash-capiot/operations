package com.coxandkings.travel.operations.service.thirdPartyVoucher;

import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.ReportGenerationCriteria;
import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.VoucherCodeSearchCriteria;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.FileType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.VoucherCode;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.SupplierConfigurationResource;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.UpdateSupplierConfigResource;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.UploadVouchersResource;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.VoucherCodeResource;
import com.coxandkings.travel.operations.response.thirdpartyvouchers.SupplierVoucherConfigSearchResponse;
import com.coxandkings.travel.operations.response.thirdpartyvouchers.ThirdPartyVouchersReportResponse;
import com.coxandkings.travel.operations.response.thirdpartyvouchers.UnitOfMeasurementResponse;
import com.coxandkings.travel.operations.response.thirdpartyvouchers.VoucherDetailsResponse;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SupplierConfigurationService {

    public Map save(SupplierConfigurationResource supplierConfigurationResource) throws OperationException;

    public SupplierVoucherConfigSearchResponse get(String id) throws OperationException;

    public Map searchByCriteria(VoucherCodeSearchCriteria voucherCodeSearchCriteria) throws OperationException;

    public SupplierVoucherConfigSearchResponse update(UpdateSupplierConfigResource updateSupplierConfigResource) throws OperationException;

    public SupplierVoucherConfigSearchResponse updateDb(SupplierConfigurationResource supplierConfigurationResource) throws OperationException;

    public SupplierVoucherConfigSearchResponse activateOrDeactivate(String supplierConfigId, List<String> voucherId,FileType fileType) throws OperationException;

    public VoucherCode getVoucherCode(String id, String voucherCodeId) throws OperationException;

    public Map<String,Object> reportGeneration(ReportGenerationCriteria reportGenerationCriteria) throws OperationException;

    public MultipartFile exportReport(List<ThirdPartyVouchersReportResponse> list,String exportType) throws OperationException;

    public List<UnitOfMeasurementResponse> getMeasurementUnits();

    public List<String> getDocumentsList(String supplierConfigId) throws OperationException;

    public Map updateVouchers(UploadVouchersResource uploadVouchersResource) throws OperationException;

    public List getProductCategorySubType(String supplierId, String productCategory) throws OperationException;

    public Map getVoucherCodeOfVoucherFile(String supplierConfigId,String fileName,Integer pageNo,Integer pageSize) throws OperationException;

    Map<String, Object> addInWorkflow(SupplierConfigurationResource supplierConfigurationResource, boolean draft) throws OperationException;

    Map<String, Object> updateWorkflow(String workflowId, UpdateSupplierConfigResource updateSupplierConfigResource, boolean draft,boolean workflow) throws OperationException, IOException, RepositoryException;


     SupplierVoucherConfigSearchResponse getVoucherCode(String id,boolean workflow) throws OperationException;

     JSONObject edit(String id,boolean workflow,boolean lock) throws OperationException, IOException, RepositoryException;

    String releaseLock(String id) throws OperationException;

    JSONObject deleteVoucherCode(VoucherDetailsResponse voucherDetailsResponse) throws OperationException;

    Set<VoucherCode> getVoucherCode(Set<VoucherCodeResource> resource);

    Map updateVoucherCode(UploadVouchersResource uploadVouchersResource) throws OperationException;


}
