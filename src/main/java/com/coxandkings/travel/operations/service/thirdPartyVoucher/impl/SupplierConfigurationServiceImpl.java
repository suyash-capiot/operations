package com.coxandkings.travel.operations.service.thirdPartyVoucher.impl;

import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.ReportGenerationCriteria;
import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.VoucherCodeSearchCriteria;
import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.SupplierConfigSearchCriteria;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.FileType;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.UnitOfMeasurement;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeStatus;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeUsageType;
import com.coxandkings.travel.operations.enums.workflow.WorkflowOperation;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.BaseLock;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.RequestLockObject;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.SupplierVoucherCodes;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.VoucherCode;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.VoucherCodePK;
import com.coxandkings.travel.operations.model.workflow.Doc;
import com.coxandkings.travel.operations.repository.thirdPartyVoucher.SupplierConfigurationRepository;
import com.coxandkings.travel.operations.repository.thirdPartyVoucher.VoucherCodeRepository;
import com.coxandkings.travel.operations.resource.documentLibrary.DocumentResource;
import com.coxandkings.travel.operations.resource.documentLibrary.DocumentSearchResource;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.SupplierConfigurationResource;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.UpdateSupplierConfigResource;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.UploadVouchersResource;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.VoucherCodeResource;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.response.thirdpartyvouchers.*;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.documentLibrary.DocumentLibraryService;
import com.coxandkings.travel.operations.service.email.EmailService;
import com.coxandkings.travel.operations.service.productbookedthrother.mdm.MdmClientService;
import com.coxandkings.travel.operations.service.supplierbillpassing.MDMRequirements;
import com.coxandkings.travel.operations.service.thirdPartyVoucher.SupplierConfigurationService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.service.workflow.ReleaseLockService;
import com.coxandkings.travel.operations.service.workflow.WorkflowService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.supplier.SupplierDetailsService;
import com.coxandkings.travel.operations.utils.thirdpartyvouchers.GeneratePDF;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.json.JSONObject;
import org.modeshape.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.RepositoryException;
import javax.persistence.EntityExistsException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierConfigurationServiceImpl implements SupplierConfigurationService,ReleaseLockService {

    @Autowired
    private SupplierConfigurationRepository supplierConfigurationRepository;

    @Autowired
    private VoucherCodeRepository voucherCodeRepository;

    @Autowired
    private MDMRequirements mdmRequirements;

    @Autowired
    private DocumentLibraryService documentLibraryService;

    @Autowired
    private GeneratePDF generatePDF;

    @Autowired
    private ObjectMapper objectMapper;

    @Value(value = "${communication.email.from_address}")
    private String emailAddress;

    @Value(value = "${communication.email.api}")
    private String emailUrl;

    @Autowired
    @Qualifier(value = "mDMToken")
    private MDMToken mdmToken;

    @Value(value = "${third-party-vouchers.voucher-file}")
    private String voucherFilepath;


    @Value(value = "${booking_engine.assign_vouchers}")
    private String updateVouchersUrl;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private SupplierDetailsService supplierDetailsService;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private MdmClientService mdmClientService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private UserService userService;

    private static final String entityType = "Third Party Vouchers";

    private static Logger logger = Logger.getLogger(SupplierConfigurationServiceImpl.class);


    @Override
    public Map searchByCriteria(VoucherCodeSearchCriteria searchByCriteria) throws OperationException {
        logger.info("Entered SupplierConfigurationServiceImpl :: searchByCriteria() method");
        OpsUser loggedInUser = userService.getLoggedInUser();
        String companyId = loggedInUser.getCompanyId();
        SupplierConfigSearchCriteria criteria = new SupplierConfigSearchCriteria();
        criteria.setSupplierName(searchByCriteria.getFilter().getSupplierName());
        criteria.setProductCategoryName(searchByCriteria.getFilter().getProductCategoryName());
        criteria.setCompanyId(companyId);
        criteria.setProductName(searchByCriteria.getFilter().getProductName());
        criteria.setProductSubCategoryName(searchByCriteria.getFilter().getProductSubCategoryName());
        searchByCriteria.setFilter(criteria);
        List<SupplierVoucherConfigSearchResponse> searchResult = new ArrayList<>();
        Boolean workflow = searchByCriteria.getWorkflow();
        if (!StringUtils.isEmpty(workflow))
        {
            if (workflow)
            {
                Map unApprovedList = getUnApprovedList(searchByCriteria);
                Object res = unApprovedList.get("data");
                List<com.coxandkings.travel.operations.model.workflow.WorkFlow> workFlows = objectMapper.convertValue(res, List.class);
                List<com.coxandkings.travel.operations.model.workflow.WorkFlow> workFlowList = new ArrayList<>();
                if (workFlows != null && workFlows.size() > 0)
                {
                    for (com.coxandkings.travel.operations.model.workflow.WorkFlow workFlow : workFlows) {
                        Object newDoc = workFlow.getDoc().getNewDoc();
                        SupplierVoucherConfigSearchResponse response = new SupplierVoucherConfigSearchResponse();
                        SupplierConfigurationResource supplierConfigurationResource = objectMapper.convertValue(newDoc, SupplierConfigurationResource.class);
                        response.setId(supplierConfigurationResource.getId());
                        response.setSupplierId(supplierConfigurationResource.getSupplierId());
                        response.setSupplierName(supplierConfigurationResource.getSupplierName());
                        response.setProductName(supplierConfigurationResource.getProductName());
                        response.setProductCategoryName(supplierConfigurationResource.getProductCategoryName());
                        response.setProductSubCategoryName(supplierConfigurationResource.getProductSubCategoryName());
                        response.setVoucherToBeAppliedOn(supplierConfigurationResource.getVoucherToBeAppliedOn());
                        response.setVoucherCodeUsageType(supplierConfigurationResource.getVoucherCodeUsageType());
                        response.setPaymentStatusToReleaseVoucher(supplierConfigurationResource.getPaymentStatusToReleaseVoucher());
                        response.setUnitOfMeasurement(supplierConfigurationResource.getUnitOfMeasurement());
                        response.setNoOfDaysToReleaseVoucher(supplierConfigurationResource.getNoOfDaysToReleaseVoucher());
                        response.setNoOfDaysToSendAlarm(supplierConfigurationResource.getNoOfDaysToSendAlarm());
                        response.setMultiplier(supplierConfigurationResource.getMultiplier());
                        response.setCustomerTemplateId(supplierConfigurationResource.getCustomerTemplateId());
                        response.setSupplierTemplateId(supplierConfigurationResource.getSupplierTemplateId());
                        response.setCompanyId(supplierConfigurationResource.getCompanyId());
                        List<VoucherDetailsResponse> voucherDetailsResponseList = new ArrayList<>();
                        Set<VoucherCodeResource> voucherCodes = supplierConfigurationResource.getVoucherCodes();
                        if (voucherCodes!=null && voucherCodes.size()>0)
                        {
                            for (VoucherCodeResource codeResource : voucherCodes) {
                                VoucherDetailsResponse voucherDetailsResponse = new VoucherDetailsResponse();
                                VoucherCodeResponse voucherCodeResponse = new VoucherCodeResponse();
                                voucherCodeResponse.setVoucherCodeStatus(VoucherCodeStatus.UNASSIGNED);
                                Set<String> inputVoucherCodes = codeResource.getInputVoucherCodes();
                                inputVoucherCodes.stream().map(String::valueOf).collect(Collectors.toList());
                                String code  = String.join(",",inputVoucherCodes);
                                voucherCodeResponse.setVoucherCode(code);

                                voucherCodeResponse.setDateOfUpload(ZonedDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now())));
                                FileType fileType = codeResource.getFileType();
                                VoucherCodeUsageType voucherCodeUsageType = codeResource.getVoucherCodeUsageType();
                                voucherCodeResponse.setFileType(fileType);
                                voucherCodeResponse.setVoucherCodeUsageType(voucherCodeUsageType);
                                if (FileType.BROWSE_FILE.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType))
                                {
                                    voucherCodeResponse.setFlieId(codeResource.getFileId());
                                }
                                voucherCodeResponse.setSupplierConfigId(supplierConfigurationResource.getId());
                                voucherDetailsResponse.setVoucherCodeResponse(voucherCodeResponse);
                                voucherDetailsResponseList.add(voucherDetailsResponse);
                            }
                        }
                        else {

                        }
                        response.setVoucherDetailsResponseList(voucherDetailsResponseList);
                        Doc doc = new Doc();
                        doc.setNewDoc(response);
                        workFlow.setDoc(doc);
                        workFlowList.add(workFlow);
                    }
                }
                else {
                    throw new OperationException(Constants.ER01);
                }
                unApprovedList.remove("data");
                unApprovedList.put("data",workFlowList);
                return unApprovedList;
            }
        }
        else {
            Map map = supplierConfigurationRepository.searchByCriteria(searchByCriteria);
            List<SupplierVoucherCodes> supplierVoucherCodesList = (List<SupplierVoucherCodes>) map.get("data");
            if (supplierVoucherCodesList!=null && supplierVoucherCodesList.size()>0)
            {
                for (SupplierVoucherCodes svc : supplierVoucherCodesList) {
                    SupplierVoucherConfigSearchResponse supplierVoucherConfigSearchResponse = get(svc);
                    searchResult.add(supplierVoucherConfigSearchResponse);
                }
                map.remove("data");
                map.put("data", searchResult);

                return map;
            }
            else
            {
                throw new OperationException(Constants.ER01);
            }
        }
        return null;
    }


    @Override
    public Map<String,Object> save(SupplierConfigurationResource supplierConfigurationResource) throws OperationException
    {
        logger.info("Entered SupplierConfigurationServiceImpl::save() method ");
        SupplierVoucherCodes supplierVoucherCodes = new SupplierVoucherCodes();
        Map<String, Object> map = new HashMap();
        SupplierVoucherCodes result = new SupplierVoucherCodes();
        if (supplierConfigurationResource.getId() != null) {
            supplierVoucherCodes = supplierConfigurationRepository.get(supplierConfigurationResource.getId());
            if (supplierVoucherCodes != null) {
                throw new OperationException(Constants.ER02);
            }
        }
        Set<VoucherCodeResource> vouchersResources = new HashSet<>();
        if (supplierConfigurationResource.getVoucherCodes() != null) {
            for (VoucherCodeResource vcr : supplierConfigurationResource.getVoucherCodes()) {
                vouchersResources.add(vcr);
            }
        }
        OpsUser loggedInUser = userService.getLoggedInUser();
        supplierConfigurationResource.setCompanyId(loggedInUser.getCompanyId());
        supplierConfigurationResource.getVoucherCodes().removeAll(supplierConfigurationResource.getVoucherCodes());
        CopyUtils.copy(supplierConfigurationResource, supplierVoucherCodes);
        supplierConfigurationRepository.saveSupplierConfig(supplierVoucherCodes);

        if (vouchersResources != null) {
            for (VoucherCodeResource voucherCode : vouchersResources) {
                UploadVouchersResource uploadVouchersResource = new UploadVouchersResource();
                uploadVouchersResource.setFileType(voucherCode.getFileType());
                uploadVouchersResource.setSupplierConfigId(supplierVoucherCodes.getId());
                uploadVouchersResource.setVoucherCodeUsageType(voucherCode.getVoucherCodeUsageType());
                if (voucherCode.getFileType().equals(FileType.BROWSE_FILE)) {
                    uploadVouchersResource.setFileId(voucherCode.getFileId());
                    uploadVouchersResource.setFileName(voucherCode.getFileName());
                    uploadVouchersResource.setInputVoucherCodes(voucherCode.getInputVoucherCodes());
                } else if (voucherCode.getFileType().equals(FileType.INPUT_CODE)) {
                    uploadVouchersResource.setInputVoucherCodes(voucherCode.getInputVoucherCodes());
                }
                this.updateVouchers(uploadVouchersResource);
            }
            map.put("Success", "Save Successfull");
            map.put("data", get(supplierVoucherCodes.getId()));
        }
        return map;

    }

    @Override
    public SupplierVoucherConfigSearchResponse get(String id) throws OperationException {
        logger.info("Entered SupplierConfigurationServiceImpl :: get() method ");
        SupplierVoucherCodes supplierVoucherCodes = supplierConfigurationRepository.get(id);
        if (supplierVoucherCodes == null) {
            logger.debug("No record exist with the given Id");
            throw new OperationException(Constants.ER01);
        }
        SupplierVoucherConfigSearchResponse supplierVoucherConfigSearchResponse = get(supplierVoucherCodes);
        return supplierVoucherConfigSearchResponse;
    }

    @Override
    public SupplierVoucherConfigSearchResponse update(UpdateSupplierConfigResource updateSupplierConfigResource) throws OperationException {
        logger.info("Entered SuplierConfiguration :: update() method to update the supplierConfiguration");
        SupplierVoucherConfigSearchResponse supplierVoucherConfigSearchResponse = new SupplierVoucherConfigSearchResponse();
        String id = updateSupplierConfigResource.getId();
        SupplierVoucherCodes supplierVoucherCodes = supplierConfigurationRepository.get(id);
        if (supplierVoucherCodes == null) {
            logger.debug("No record exist with the given Id to update");
            throw new OperationException(Constants.ER01);
        }
        if (updateSupplierConfigResource.getNoOfDaysToSendAlarm() != null) {
            supplierVoucherCodes.setNoOfDaysToSendAlarm(updateSupplierConfigResource.getNoOfDaysToSendAlarm());
        }
        if (updateSupplierConfigResource.getNoOfDaysToReleaseVoucher() != null) {
            supplierVoucherCodes.setNoOfDaysToReleaseVoucher(updateSupplierConfigResource.getNoOfDaysToReleaseVoucher());
        }
        if (updateSupplierConfigResource.getPaymentStatusToReleaseVoucher() != null) {
            supplierVoucherCodes.setPaymentStatusToReleaseVoucher(updateSupplierConfigResource.getPaymentStatusToReleaseVoucher());
        }
        if (updateSupplierConfigResource.getSupplierTemplateId() != null) {
            supplierVoucherCodes.setSupplierTemplateId(updateSupplierConfigResource.getSupplierTemplateId());
        }
        if (updateSupplierConfigResource.getCustomerTemplateId() != null) {
            supplierVoucherCodes.setCustomerTemplateId(updateSupplierConfigResource.getCustomerTemplateId());
        }
        supplierVoucherCodes = supplierConfigurationRepository.add(supplierVoucherCodes);
        supplierVoucherConfigSearchResponse = get(supplierVoucherCodes);
        return supplierVoucherConfigSearchResponse;
    }

    @Override
    public SupplierVoucherConfigSearchResponse updateDb(SupplierConfigurationResource supplierConfigurationResource) throws OperationException
    {
        String id = supplierConfigurationResource.getId();
        SupplierVoucherConfigSearchResponse supplierVoucherConfigSearchResponse = new SupplierVoucherConfigSearchResponse();
        SupplierVoucherCodes supplierVoucherCodes = supplierConfigurationRepository.get(id);
        if (supplierVoucherCodes == null) {
            logger.debug("No record exist with the given Id to update");
            throw new OperationException(Constants.ER01);
        }
        CopyUtils.copy(supplierConfigurationResource,supplierVoucherCodes);
        supplierVoucherCodes = supplierConfigurationRepository.add(supplierVoucherCodes);
        supplierVoucherConfigSearchResponse = get(supplierVoucherCodes);
        return supplierVoucherConfigSearchResponse;
    }

    @Override
    public SupplierVoucherConfigSearchResponse activateOrDeactivate(String supplierConfigId, List<String> voucherCode, FileType fileType) throws OperationException {
        logger.info("Entered SupplierConfigurationServiceImpl :: activateorDeactivate() method");
        SupplierVoucherConfigSearchResponse supplierVoucherConfigSearchResponse = new SupplierVoucherConfigSearchResponse();
        SupplierVoucherCodes supplierVoucherCodes = supplierConfigurationRepository.get(supplierConfigId);
        if (supplierVoucherCodes == null) {
            logger.debug("No such record exist with given Id");
            throw new OperationException(Constants.ER01);
        }
        VoucherCode voucherCode1 = null;
        if (fileType.equals(FileType.INPUT_CODE)) {
            for (String s : voucherCode) {
                VoucherCodePK voucherCodePK = new VoucherCodePK(supplierConfigId, s);
                voucherCode1 = voucherCodeRepository.getVoucherCode(voucherCodePK);
                if (voucherCode1 == null) {
                    throw new OperationException(Constants.ER01);
                }
                voucherCode1 = activateorDeactivateaVoucher(voucherCode1);
                voucherCode1.setSupplierVoucherCodes(supplierVoucherCodes);
            }

        } else if (fileType.equals(FileType.BROWSE_FILE)) {
            Map map= voucherCodeRepository.getVouchers(supplierConfigId, voucherCode.get(0),1,null);
            List<VoucherCode> voucherCodeList= (List<VoucherCode>) map.get("voucherCodes");
            if (!voucherCodeList.isEmpty()) {
                for (VoucherCode voucherCode11 : voucherCodeList) {
                    voucherCode1 = activateorDeactivateaVoucher(voucherCode11);
                    voucherCode1.setSupplierVoucherCodes(supplierVoucherCodes);
                }
            } else {
                throw new OperationException("VoucherCode file is not valid");
            }
        }
        voucherCode1.setSupplierVoucherCodes(supplierVoucherCodes);
        System.out.println("supplierVoucherCodes: " + supplierVoucherCodes.toString());
        SupplierVoucherCodes supplierVoucherCodes1 = supplierConfigurationRepository.add(supplierVoucherCodes);
        supplierVoucherConfigSearchResponse = get(supplierVoucherCodes1);
        return supplierVoucherConfigSearchResponse;

    }

    public VoucherCode activateorDeactivateaVoucher(VoucherCode voucherCode) {

        if (voucherCode.getVoucherCodeStatus().equals(VoucherCodeStatus.INVALID)) {
            voucherCode.setVoucherCodeStatus(VoucherCodeStatus.UNASSIGNED);
            voucherCode.setLastModifiedDate(ZonedDateTime.now());
        } else if (voucherCode.getVoucherCodeStatus().equals(VoucherCodeStatus.UNASSIGNED)) {
            voucherCode.setVoucherCodeStatus(VoucherCodeStatus.INVALID);
            voucherCode.setLastModifiedDate(ZonedDateTime.now());
        }
        return voucherCode;
    }

    @Override
    public VoucherCode getVoucherCode(String id, String voucherCode) throws OperationException {
        logger.info("Entered SupplierConfigurationServiceImpl :: getVoucher() method");
        VoucherCodePK voucherCodePK = new VoucherCodePK(id, voucherCode);
        VoucherCode voucherCode1 = voucherCodeRepository.getVoucherCode(voucherCodePK);
        if (voucherCode1 == null) {
            logger.debug("No such record exist with given Id");
            throw new OperationException(Constants.ER01);
        }
        return voucherCode1;
    }

    @Override
    public Map<String,Object> reportGeneration(ReportGenerationCriteria reportGenerationCriteria) {
        logger.info("Entered SupplierConfigurationServiceImpl :: reportGeneration() method");
        List<VoucherCode> voucherResult = new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        List<ThirdPartyVouchersReportResponse> list = new ArrayList<>();
        List<SupplierVoucherCodes> result = new ArrayList<>();
        if (reportGenerationCriteria.getVoucherCodeStatus() != null || reportGenerationCriteria.getPaymentStatusToReleaseVoucher() != null
                || reportGenerationCriteria.getVoucherCodeUsageType() != null || reportGenerationCriteria.getPaymentStatusOfBooking() != null) {
            map= voucherCodeRepository.searchForReports(reportGenerationCriteria);
            voucherResult= (List<VoucherCode>) map.get("voucherCodes");
            for (VoucherCode voucherCode : voucherResult) {
                ThirdPartyVouchersReportResponse thirdPartyVouchersReportResponse = new ThirdPartyVouchersReportResponse();
                thirdPartyVouchersReportResponse.setSupplier(voucherCode.getSupplierVoucherCodes().getSupplierName());
                thirdPartyVouchersReportResponse.setProductName(voucherCode.getSupplierVoucherCodes().getProductName());
                thirdPartyVouchersReportResponse.setProductCategory(voucherCode.getSupplierVoucherCodes().getProductCategoryName());
                thirdPartyVouchersReportResponse.setProductCategorySubtype(voucherCode.getSupplierVoucherCodes().getProductSubCategoryName());
                thirdPartyVouchersReportResponse.setVoucherCodeUsageType(voucherCode.getVoucherCodeUsageType());
                thirdPartyVouchersReportResponse.setStatus(voucherCode.getVoucherCodeStatus());
                thirdPartyVouchersReportResponse.setVoucherToBeAppliedOn(voucherCode.getSupplierVoucherCodes().getPaymentStatusToReleaseVoucher());
                thirdPartyVouchersReportResponse.setCompanyId(voucherCode.getSupplierVoucherCodes().getCompanyId());
                if (voucherCode.getPaymentStatus() != null) {
                    thirdPartyVouchersReportResponse.setPaymentStatusOfBooking(voucherCode.getPaymentStatus());
                }
                list.add(thirdPartyVouchersReportResponse);
            }
            map.put("result",list);
            map.remove("voucherCodes");
        } else {
            map = supplierConfigurationRepository.reportGeneration(reportGenerationCriteria);
            result= (List<SupplierVoucherCodes>) map.get("voucherCodes");
            for (SupplierVoucherCodes voucherCode : result) {
                ThirdPartyVouchersReportResponse thirdPartyVouchersReportResponse = new ThirdPartyVouchersReportResponse();
                thirdPartyVouchersReportResponse.setSupplier(voucherCode.getSupplierName());
                thirdPartyVouchersReportResponse.setProductName(voucherCode.getProductName());
                thirdPartyVouchersReportResponse.setProductCategory(voucherCode.getProductCategoryName());
                thirdPartyVouchersReportResponse.setProductCategorySubtype(voucherCode.getProductSubCategoryName());
                thirdPartyVouchersReportResponse.setVoucherToBeAppliedOn(voucherCode.getPaymentStatusToReleaseVoucher());
                thirdPartyVouchersReportResponse.setCompanyId(voucherCode.getVoucherCodes().iterator().next().getSupplierVoucherCodes().getCompanyId());
                list.add(thirdPartyVouchersReportResponse);
            }
            map.put("result",list);
            map.remove("voucherCodes");
        }

        return map;
    }

    @Override
    public MultipartFile exportReport(List<ThirdPartyVouchersReportResponse> list,String exportType) throws OperationException {
        logger.info("Entered SupplierConfigurationServiceImpl :: exportReport() method");
        MultipartFile file = null;
        // String docId=null;
        try {
            if(exportType.equalsIgnoreCase("PDF")){
                file = generatePDF.generateFile(list);
            }
            else {
                file = generatePDF.generateExcel(list);
            }

        } catch (IOException e) {
            logger.debug("Cannot find the location of the File");
            throw new OperationException(Constants.ER805);
        } catch (DocumentException e) {
            logger.debug("Fail to write to pdf");
            throw new OperationException(Constants.ER806);
        }
        /*NewDocumentResource newDocumentResource=new NewDocumentResource();
        newDocumentResource.setExtension("pdf");
        newDocumentResource.setName("report");
        DocumentReferenceResource documentReferenceResource= null;
        try {
            documentReferenceResource = documentLibraryService.create(file,newDocumentResource,null);
            docId=documentReferenceResource.getId();
        } catch (RepositoryException e) {
            logger.debug("Fail to save document to doc library");
            throw new OperationException(Constants.ER804);
        }*/

        return file;
    }

    @Override
    public List<UnitOfMeasurementResponse> getMeasurementUnits() {
        UnitOfMeasurement[] list = UnitOfMeasurement.values();
        List<UnitOfMeasurementResponse> response = new ArrayList<>();
        for (UnitOfMeasurement unit : list) {
            UnitOfMeasurementResponse unitOfMeasurementResponse = new UnitOfMeasurementResponse();
            unitOfMeasurementResponse.setName(unit.name());
            unitOfMeasurementResponse.setValue(unit.getValue());
            response.add(unitOfMeasurementResponse);
        }
        return response;
    }

    @Override
    public Map updateVouchers(UploadVouchersResource uploadVouchersResource) throws OperationException {
        logger.info("Entered SupplierConfigurationServiceImpl :: updateVouchers() method");
        Map map = new HashMap();
        String supplierConfigId = uploadVouchersResource.getSupplierConfigId();
        SupplierVoucherCodes supplierVoucherCodes = new SupplierVoucherCodes();
        if (supplierConfigId != null && !(supplierConfigId.isEmpty())) {
            supplierVoucherCodes = supplierConfigurationRepository.get(uploadVouchersResource.getSupplierConfigId());
            if (supplierVoucherCodes == null) {
                throw new OperationException(Constants.ER01);
            }
            Set<VoucherCode> voucherCodes = supplierVoucherCodes.getVoucherCodes();
            if (uploadVouchersResource.getFileType().equals(FileType.INPUT_CODE) ||
                    uploadVouchersResource.getVoucherCodeUsageType().equals(VoucherCodeUsageType.MULTIPLE)) {
                for (String vc : uploadVouchersResource.getInputVoucherCodes()) {
                    if (!(StringUtils.isEmpty(vc))) {
                        VoucherCode voucherCode = new VoucherCode();
                        voucherCode.setId(uploadVouchersResource.getSupplierConfigId());
                        voucherCode.setDateOfUpload(ZonedDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now())));
                        voucherCode.setVoucherCodeStatus(VoucherCodeStatus.UNASSIGNED);
                        voucherCode.setVoucherCode(vc);
                        voucherCode.setFileType(uploadVouchersResource.getFileType());
                        voucherCode.setVoucherCodeUsageType(uploadVouchersResource.getVoucherCodeUsageType());
                        voucherCodes.add(voucherCode);
                    }
                }

                supplierVoucherCodes.setVoucherCodes(voucherCodes);
            }
            if (uploadVouchersResource.getFileType().equals(FileType.BROWSE_FILE))
            {
                DocumentSearchResource documentSearchResource = new DocumentSearchResource();
                documentSearchResource.setId(uploadVouchersResource.getFileId());
                List<DocumentSearchResource> list = new ArrayList<>();
                list.add(documentSearchResource);
                List<DocumentResource> documentResources = new ArrayList<>();
                try {
                    documentResources = documentLibraryService.getDocuments(list);
                    documentResources.get(0);
                } catch (Exception e) {
                    logger.debug("Voucher File not available for the given fileId");
                    throw new OperationException(Constants.ER807);
                }

                byte[] bfile = documentResources.get(0).getByteArray();
//                System.out.println(documentResources.get(0).getByteArray());
                FileOutputStream fileOuputStream = null;
                InputStream targetStream = new ByteArrayInputStream(bfile);
                String fileAddress = voucherFilepath;
                try {
//                    fileOuputStream = new FileOutputStream(fileAddress);
//                    fileOuputStream.write(bfile);
//                    Workbook workbook = WorkbookFactory.create(new File(fileAddress));
                    Workbook workbook = WorkbookFactory.create(targetStream);
                    Iterator<Sheet> sheetIterator = workbook.sheetIterator();

                    while (sheetIterator.hasNext()) {
                        Sheet sheet = sheetIterator.next();
                        Iterator<Row> rowIterator = sheet.rowIterator();
                        while (rowIterator.hasNext()) {
                            rowIterator.next();
                            break;
                        }
                        while (rowIterator.hasNext()) {
                            Row row = rowIterator.next();
                            DataFormatter dataFormatter = new DataFormatter();
                            Iterator<Cell> cellIterator = row.cellIterator();
                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();
                                String cellValue = dataFormatter.formatCellValue(cell);
                                if (!cellValue.contains(" ")) {
                                    VoucherCode voucherCode = new VoucherCode();
                                    voucherCode.setId(uploadVouchersResource.getSupplierConfigId());
                                    voucherCode.setDateOfUpload(ZonedDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now())));
                                    voucherCode.setVoucherCodeStatus(VoucherCodeStatus.UNASSIGNED);
                                    voucherCode.setVoucherCode(cellValue);
                                    voucherCode.setFileId(uploadVouchersResource.getFileId());
                                    voucherCode.setFileType(uploadVouchersResource.getFileType());
                                    voucherCode.setVoucherCodeUsageType(uploadVouchersResource.getVoucherCodeUsageType());
                                    voucherCode.setVoucherDetails(uploadVouchersResource.getFileName());
                                    voucherCodes.add(voucherCode);
                                }
                            }
                        }
                    }
                    workbook.close();

                } catch (FileNotFoundException e) {
                    logger.debug("Voucher File path is not available");
                    throw new OperationException(Constants.ER813);
                } catch (InvalidFormatException e) {
                    logger.debug("Fail to read the file");
                    throw new OperationException(Constants.ER808);
                } catch (IOException e) {
                    logger.debug("Upload only xlsx extension files");
                    throw new OperationException(Constants.ER809);
                } finally {
                    if (fileOuputStream != null) {
                        try {
                            fileOuputStream.close();
                        } catch (IOException e) {
                            logger.debug("Fail to close the FileOutput stream");
                            throw new OperationException(Constants.ER810);
                        }
                    }
                }
                supplierVoucherCodes.setVoucherCodes(voucherCodes);
            }

        }
        try {
            supplierConfigurationRepository.saveSupplierConfig(supplierVoucherCodes);
            map.put("Success", "Uploaded Successfully");
            return map;
        } catch (EntityExistsException e) {
            logger.debug("This Voucher Code is already available for this Supplier");
            throw new OperationException(Constants.ER811);
        }
    }

    @Override
    public List<String> getDocumentsList(String supplierConfigId) throws OperationException {
        logger.info("Entered SupplierConfigurationServiceImpl :: getDocumentsList() method");
        List<String> list = voucherCodeRepository.getDocumentsList(supplierConfigId);
        return list;
    }


    public SupplierVoucherConfigSearchResponse get(SupplierVoucherCodes supplierVoucherCodes) {
        SupplierVoucherConfigSearchResponse supplierVoucherConfigSearchResponse = new SupplierVoucherConfigSearchResponse();
        CopyUtils.copy(supplierVoucherCodes, supplierVoucherConfigSearchResponse);
        Set<VoucherCode> set = supplierVoucherCodes.getVoucherCodes();
        List<VoucherCode> voucherCodesForSupplier = new ArrayList<>(set);
        List<VoucherDetailsResponse> voucherDetails = new ArrayList<>();
        List<String> voucherCodeDetails = voucherCodeRepository.getDocumentsList(supplierVoucherCodes.getId());

        if (!voucherCodeDetails.isEmpty()) {
            //voucherCodes for file uploaded
            for (String s : voucherCodeDetails) {
                List<VoucherCode> voucherCodeList = new ArrayList<>();

                for (VoucherCode voucherCode : supplierVoucherCodes.getVoucherCodes()) {
                    if (voucherCode.getVoucherDetails() != null && voucherCode.getVoucherDetails().equalsIgnoreCase(s)) {
                        voucherCodeList.add(voucherCode);
                        voucherCodesForSupplier.remove(voucherCode);
                    }
                }
                VoucherDetailsResponse voucherDetailsResponse = new VoucherDetailsResponse();
                VoucherCodeResponse voucherCodeResponse = new VoucherCodeResponse();
                List<String> deactivatedCodes = new ArrayList<>();

                for (VoucherCode vc1 : voucherCodeList) {
                    if (vc1.getVoucherCodeStatus().equals(VoucherCodeStatus.INVALID)) {
                        deactivatedCodes.add(vc1.getVoucherCode());
                    } else {
                        voucherCodeResponse.setVoucherCodeStatus(vc1.getVoucherCodeStatus());
                    }
                    voucherCodeResponse.setDateOfUpload(ZonedDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(vc1.getDateOfUpload())));
                    voucherCodeResponse.setVoucherCode(vc1.getVoucherDetails());
                    voucherCodeResponse.setVoucherCodeUsageType(vc1.getVoucherCodeUsageType());
                    voucherCodeResponse.setFileType(vc1.getFileType());
                    voucherCodeResponse.setSupplierConfigId(supplierVoucherCodes.getId());
                    voucherCodeResponse.setFlieId((FileType.BROWSE_FILE.equals(vc1.getFileType()) && VoucherCodeUsageType.FIXED.equals(vc1.getVoucherCodeUsageType()))?vc1.getFileId():"");
                }
                if (StringUtils.isEmpty(voucherCodeResponse.getVoucherCodeStatus())) {
                    voucherCodeResponse.setVoucherCodeStatus(VoucherCodeStatus.INVALID);
                }
                voucherDetailsResponse.setVoucherCodeResponse(voucherCodeResponse);
                voucherDetailsResponse.setDeactivatedCodes(deactivatedCodes);
                voucherDetails.add(voucherDetailsResponse);
            }
        }

        if (!voucherCodesForSupplier.isEmpty()) {
            List<String> deactivatedCodes = new ArrayList<>();
            List<VoucherCode> voucherCodeForMultipletype = new ArrayList<>();
            for (VoucherCode vc : voucherCodesForSupplier) {
                if (vc.getVoucherCodeUsageType().equals(VoucherCodeUsageType.MULTIPLE)) {
                    voucherCodeForMultipletype.add(vc);
                }
            }
            //voucher code for multiple type
            if (!voucherCodeForMultipletype.isEmpty()) {

                VoucherCodeResponse voucherCodeResponse = new VoucherCodeResponse();
                List<String> deactivatedCode = new ArrayList<>();
                String voucherCode = new String();
                for (VoucherCode vc : voucherCodeForMultipletype) {

                    if (vc.getVoucherCodeStatus().equals(VoucherCodeStatus.INVALID)) {
                        deactivatedCode.add(vc.getVoucherCode());
                    } else {
                        voucherCode = voucherCode + vc.getVoucherCode() + ",";
                    }

                    voucherCodeResponse.setDateOfUpload(ZonedDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(vc.getDateOfUpload())));
                    voucherCodeResponse.setVoucherCodeStatus(vc.getVoucherCodeStatus());
                    voucherCodeResponse.setVoucherCodeUsageType(vc.getVoucherCodeUsageType());
                    voucherCodeResponse.setFileType(vc.getFileType());
                    voucherCodeResponse.setSupplierConfigId(supplierVoucherCodes.getId());
                }
                voucherCodeResponse.setVoucherCode(voucherCode);

                VoucherDetailsResponse voucherDetailsResponse = new VoucherDetailsResponse();
                voucherDetailsResponse.setDeactivatedCodes(deactivatedCode);
                voucherDetailsResponse.setVoucherCodeResponse(voucherCodeResponse);

                voucherDetails.add(voucherDetailsResponse);
                voucherCodesForSupplier.removeAll(voucherCodeForMultipletype);
            }
            //voucherCode for Input code type
            for (VoucherCode vc : voucherCodesForSupplier) {
                List<String> deactiv = new ArrayList<>();
                VoucherDetailsResponse voucherDetailsResponse = new VoucherDetailsResponse();
                if (vc.getVoucherCodeStatus().equals(VoucherCodeStatus.INVALID)) {
                    deactiv.add(vc.getVoucherCode());
                }
                VoucherCodeResponse voucherCodeResponse = new VoucherCodeResponse();
                voucherCodeResponse.setVoucherCodeUsageType(vc.getVoucherCodeUsageType());
                voucherCodeResponse.setVoucherCode(vc.getVoucherCode());
                voucherCodeResponse.setVoucherCodeStatus(vc.getVoucherCodeStatus());
                voucherCodeResponse.setDateOfUpload(ZonedDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(vc.getDateOfUpload())));
                voucherCodeResponse.setFileType(vc.getFileType());
                voucherCodeResponse.setSupplierConfigId(supplierVoucherCodes.getId());
                voucherDetailsResponse.setDeactivatedCodes(deactiv);
                voucherDetailsResponse.setVoucherCodeResponse(voucherCodeResponse);
                voucherDetails.add(voucherDetailsResponse);
            }
        }
        supplierVoucherConfigSearchResponse.setVoucherDetailsResponseList(voucherDetails);
        return supplierVoucherConfigSearchResponse;
    }

    @Override
    public List getProductCategorySubType(String supplierId, String productCategory) throws OperationException {
        try {
            return mdmRequirements.getProductCategorySubType(supplierId,productCategory);
        } catch (IOException e) {
            throw new OperationException(Constants.ER814);
        }
    }

    @Override
    public Map getVoucherCodeOfVoucherFile(String supplierConfigId, String fileName,Integer pageNo,Integer pageSize) throws OperationException {
        Map voucherCodeList=new HashMap();
        List<String> docFileNames=voucherCodeRepository.getDocumentsList(supplierConfigId);
        if (!docFileNames.isEmpty()) {
            for (String s:docFileNames){
                if (s.equalsIgnoreCase(fileName)){
                    voucherCodeList=voucherCodeRepository.getVouchers(supplierConfigId,fileName,pageNo,pageSize);
                }
            }
        }else {
            throw new OperationException("No such file exists for the given supplier");
        }
        return voucherCodeList;
    }

    @Override
    public Map<String, Object> addInWorkflow(SupplierConfigurationResource supplierConfigurationResource, boolean draft) throws OperationException
    {
        Map map = new HashMap();
        OpsUser loggedInUser = userService.getLoggedInUser();
        supplierConfigurationResource.setCompanyId(loggedInUser.getCompanyId());
        String workFlowId = supplierConfigurationResource.getId();
        if (draft)
        {
            if (StringUtils.isEmpty(workFlowId))
            {
                String workflow = workflowService.saveWorkflow(supplierConfigurationResource, WorkflowOperation.SAVE, entityType, loggedInUser.getUserID());
                JSONObject workflowResp = new JSONObject(workflow);
                String id = workflowResp.optString("_id");
                if (!StringUtils.isEmpty(id)) {
                    map.put("Success", "Record saved sucessfully with workflow id " + id);
                    map.put("Data",workflowResp);
                    return map;
                } else {
                    throw new OperationException("Data not saved");
                }
            }
            else
            {
                JSONObject docs = workflowService.getDocs(supplierConfigurationResource.getId());
                JSONObject jsonObject = new JSONObject(supplierConfigurationResource);
                boolean similar = docs.similar(jsonObject);
                if (similar)
                {
                    throw new OperationException("No changes made to the record");
                }
                else {
                    String workflow = workflowService.updateWorkflowDoc(supplierConfigurationResource, WorkflowOperation.SAVE, workFlowId);
                    JSONObject workflowResp = new JSONObject(workflow);
                    String id = workflowResp.optString("_id");
                    if (!StringUtils.isEmpty(id)) {
                        map.put("Success", "Record saved sucessfully with workflow id " + id);
                        map.put("Data", workflowResp);
                        return map;
                    } else {
                        throw new OperationException("Data not saved");
                    }
                }

            }
        }
        else
        {
            if (StringUtils.isEmpty(workFlowId))
            {
                String workflow = workflowService.saveWorkflow(supplierConfigurationResource, WorkflowOperation.SUBMIT, entityType, loggedInUser.getUserID());
                JSONObject workflowResp = new JSONObject(workflow);
                String id = workflowResp.optString("_id");
                if (!StringUtils.isEmpty(id))
                {
                    map.put("Success","Record submitted sucessfully with workflow id "+id);
                    map.put("Data",workflowResp);
                    return map;
                }
                else {
                    throw new OperationException("Data not saved");
                }
            }
            else {

                String workflow = workflowService.updateWorkflowDoc(supplierConfigurationResource, WorkflowOperation.SUBMIT, workFlowId);
                JSONObject workflowResp = new JSONObject(workflow);
                String id = workflowResp.optString("_id");
                if (!StringUtils.isEmpty(id))
                {
                    map.put("Success","Record submitted sucessfully with workflow id "+id);
                    map.put("Data",workflowResp);
                    return map;
                }
                else {
                    throw new OperationException("Data not saved");
                }
            }
        }
    }

    @Override
    public Map<String, Object> updateWorkflow(String workflowId, UpdateSupplierConfigResource updateSupplierConfigResource, boolean draft,boolean workFlow) throws OperationException, IOException, RepositoryException {
        /*String res = workflowService.getWorkFlowById(workflowId);
        SupplierVoucherConfigSearchResponse supplierVoucherConfigSearchResponse;
        SupplierVoucherCodes supplierVoucherCodes = null;
        try {
            supplierVoucherCodes =  objectMapper.readValue(new JSONObject(new JSONTokener(res)).toString(), SupplierVoucherCodes.class);
        } catch (IOException e) {
            //TODO: throw an exception
            e.printStackTrace();
        }
        if (supplierVoucherCodes == null) {
            logger.debug("No record exist with the given Id to update");
            throw new OperationException(Constants.ER01);
        }
        if (updateSupplierConfigResource.getNoOfDaysToSendAlarm() != null) {
            supplierVoucherCodes.setNoOfDaysToSendAlarm(updateSupplierConfigResource.getNoOfDaysToSendAlarm());
        }
        if (updateSupplierConfigResource.getNoOfDaysToReleaseVoucher() != null) {
            supplierVoucherCodes.setNoOfDaysToReleaseVoucher(updateSupplierConfigResource.getNoOfDaysToReleaseVoucher());
        }
        if (updateSupplierConfigResource.getPaymentStatusToReleaseVoucher() != null) {
            supplierVoucherCodes.setPaymentStatusToReleaseVoucher(updateSupplierConfigResource.getPaymentStatusToReleaseVoucher());
        }
        if (updateSupplierConfigResource.getSuppliersTemplateId() != null) {
            supplierVoucherCodes.setSupplierTemplateId(updateSupplierConfigResource.getSuppliersTemplateId());
        }
        if (updateSupplierConfigResource.getCustomersTemplateId() != null) {
            supplierVoucherCodes.setCustomerTemplateId(updateSupplierConfigResource.getCustomersTemplateId());
        }

        if(draft!=null && draft)
            workflowService.updateWorkflowDoc(supplierVoucherCodes, WorkflowOperation.SAVE, workflowId);
        else
            workflowService.updateWorkflowDoc(supplierVoucherCodes, WorkflowOperation.SUBMIT, workflowId);


        supplierVoucherConfigSearchResponse = get(supplierVoucherCodes);
        return supplierVoucherConfigSearchResponse;*/
        Map map = new HashMap();
        String workFlowById = null;
        OpsUser loggedInUser = userService.getLoggedInUser();
        SupplierVoucherCodes voucherCodes = new SupplierVoucherCodes();
        SupplierVoucherConfigSearchResponse response = null;

        if (draft && workFlow)
        {
            if (!StringUtils.isEmpty(workflowId))
            {
                workFlowById = workflowService.getWorkFlowById(workflowId);
                JSONObject object = new JSONObject(workFlowById);
                JSONObject workFlowJsonObj= (JSONObject) object.optJSONArray("data").get(0);
                Object docs = workFlowJsonObj.getJSONObject("doc").getJSONObject("newDoc");
                if (docs != null)
                {
                    SupplierConfigurationResource resource = objectMapper.convertValue(docs, SupplierConfigurationResource.class);
                    if (resource != null) {
                        boolean similar = checkSimilarities(resource, updateSupplierConfigResource);
                        if (similar) {
                            throw new OperationException("No changes made to the record");
                        }
                        else
                        {
                            resource.setNoOfDaysToReleaseVoucher(updateSupplierConfigResource.getNoOfDaysToReleaseVoucher());
                            resource.setNoOfDaysToSendAlarm(updateSupplierConfigResource.getNoOfDaysToSendAlarm());
                            resource.setPaymentStatusToReleaseVoucher(updateSupplierConfigResource.getPaymentStatusToReleaseVoucher());
                            resource.setCustomerTemplateId(updateSupplierConfigResource.getCustomerTemplateId());
                            resource.setSupplierTemplateId(updateSupplierConfigResource.getSupplierTemplateId());
                            String workflow = workflowService.updateWorkflowDoc(resource, WorkflowOperation.SAVE, workflowId);
                            JSONObject workFlowJsonObject = new JSONObject(workflow);
                            String id = workFlowJsonObject.optString("_id");
                            if (!StringUtils.isEmpty(id))
                            {
//                                JSONObject jsonObject = (JSONObject) object.optJSONArray("data").get(0);
                                Object newDocs = workFlowJsonObject.getJSONObject("doc").getJSONObject("newDoc");
                                response = new SupplierVoucherConfigSearchResponse();
                                SupplierConfigurationResource supplierConfigurationResource = objectMapper.convertValue(newDocs, SupplierConfigurationResource.class);
                                response.setId(supplierConfigurationResource.getId());
                                response.setSupplierId(supplierConfigurationResource.getSupplierId());
                                response.setSupplierName(supplierConfigurationResource.getSupplierName());
                                response.setProductName(supplierConfigurationResource.getProductName());
                                response.setProductCategoryName(supplierConfigurationResource.getProductCategoryName());
                                response.setProductSubCategoryName(supplierConfigurationResource.getProductSubCategoryName());
                                response.setVoucherToBeAppliedOn(supplierConfigurationResource.getVoucherToBeAppliedOn());
                                response.setVoucherCodeUsageType(supplierConfigurationResource.getVoucherCodeUsageType());
                                response.setPaymentStatusToReleaseVoucher(supplierConfigurationResource.getPaymentStatusToReleaseVoucher());
                                response.setUnitOfMeasurement(supplierConfigurationResource.getUnitOfMeasurement());
                                response.setNoOfDaysToReleaseVoucher(supplierConfigurationResource.getNoOfDaysToReleaseVoucher());
                                response.setNoOfDaysToSendAlarm(supplierConfigurationResource.getNoOfDaysToSendAlarm());
                                response.setMultiplier(supplierConfigurationResource.getMultiplier());
                                response.setCustomerTemplateId(supplierConfigurationResource.getCustomerTemplateId());
                                response.setSupplierTemplateId(supplierConfigurationResource.getSupplierTemplateId());
                                response.setCompanyId(supplierConfigurationResource.getCompanyId());
                                List<VoucherDetailsResponse> voucherDetailsResponseList = new ArrayList<>();
                                Set<VoucherCodeResource> codes = supplierConfigurationResource.getVoucherCodes();
                                if (codes!=null && codes.size()>0)
                                {
                                    for (VoucherCodeResource codeResource : codes) {
                                        VoucherDetailsResponse voucherDetailsResponse = new VoucherDetailsResponse();
                                        VoucherCodeResponse voucherCodeResponse = new VoucherCodeResponse();
                                        voucherCodeResponse.setVoucherCodeStatus(VoucherCodeStatus.UNASSIGNED);
                                        if (codeResource.getInputVoucherCodes()==null)
                                        {
                                            codeResource.setInputVoucherCodes(new HashSet<>());
                                        }
                                        Set<String> inputVoucherCodes = codeResource.getInputVoucherCodes();
                                        inputVoucherCodes.stream().map(String::valueOf).collect(Collectors.toList());
                                        String code  = String.join(",",inputVoucherCodes);
                                        voucherCodeResponse.setVoucherCode(code);
                                        voucherCodeResponse.setDateOfUpload(ZonedDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now())));
                                        FileType fileType = codeResource.getFileType();
                                        VoucherCodeUsageType voucherCodeUsageType = codeResource.getVoucherCodeUsageType();
                                        voucherCodeResponse.setFileType(fileType);
                                        voucherCodeResponse.setVoucherCodeUsageType(voucherCodeUsageType);
                                        if (FileType.BROWSE_FILE.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType))
                                        {
                                            voucherCodeResponse.setFlieId(codeResource.getFileId());
                                        }
                                        voucherCodeResponse.setSupplierConfigId(supplierConfigurationResource.getId());
                                        voucherDetailsResponse.setVoucherCodeResponse(voucherCodeResponse);
                                        voucherDetailsResponseList.add(voucherDetailsResponse);
                                    }
                                }
                                response.setVoucherDetailsResponseList(voucherDetailsResponseList);
                                workFlowJsonObject.getJSONObject("doc").put("newDoc",new JSONObject(response));
                                map.put("Success", "Record saved sucessfully with workflow id " + id);
                                map.put("data", workFlowJsonObject);
                                return map;
                            }
                            else
                            {
                                throw new OperationException("Data not saved");
                            }
                        }
                    }
                    else {
                        logger.error("There is issue in converting workflow resp into SupplierConfigurationResource");
                    }
                }
            }
        }
        else if (!draft && workFlow)
        {
            if (!StringUtils.isEmpty(workflowId)) {
                workFlowById = workflowService.getWorkFlowById(workflowId);
                JSONObject object = new JSONObject(workFlowById);
                JSONObject workFlowJsonObj= (JSONObject) object.optJSONArray("data").get(0);
                Object docs = workFlowJsonObj.getJSONObject("doc").getJSONObject("newDoc");
//                JSONObject docs = workflowService.getDocs(workflowId);
                if (docs != null) {
                    SupplierConfigurationResource resource = objectMapper.convertValue(docs, SupplierConfigurationResource.class);
                    if (resource != null)
                    {
                        resource.setNoOfDaysToReleaseVoucher(updateSupplierConfigResource.getNoOfDaysToReleaseVoucher());
                        resource.setNoOfDaysToSendAlarm(updateSupplierConfigResource.getNoOfDaysToSendAlarm());
                        resource.setPaymentStatusToReleaseVoucher(updateSupplierConfigResource.getPaymentStatusToReleaseVoucher());
                        resource.setCustomerTemplateId(updateSupplierConfigResource.getCustomerTemplateId());
                        resource.setSupplierTemplateId(updateSupplierConfigResource.getSupplierTemplateId());
                    }
                    String workflow = workflowService.updateWorkflowDoc(resource, WorkflowOperation.SUBMIT, workflowId);
                    JSONObject workflowResp = new JSONObject(workflow);
                    String id = workflowResp.optString("_id");
                    if (!StringUtils.isEmpty(id))
                    {
//                        JSONObject jsonObject = (JSONObject) workflowResp.optJSONArray("data").get(0);
                        Object newDocs = workflowResp.getJSONObject("doc").getJSONObject("newDoc");
                        response = new SupplierVoucherConfigSearchResponse();
                        SupplierConfigurationResource supplierConfigurationResource = objectMapper.convertValue(newDocs, SupplierConfigurationResource.class);
                        response.setId(supplierConfigurationResource.getId());
                        response.setSupplierId(supplierConfigurationResource.getSupplierId());
                        response.setSupplierName(supplierConfigurationResource.getSupplierName());
                        response.setProductName(supplierConfigurationResource.getProductName());
                        response.setProductCategoryName(supplierConfigurationResource.getProductCategoryName());
                        response.setProductSubCategoryName(supplierConfigurationResource.getProductSubCategoryName());
                        response.setVoucherToBeAppliedOn(supplierConfigurationResource.getVoucherToBeAppliedOn());
                        response.setVoucherCodeUsageType(supplierConfigurationResource.getVoucherCodeUsageType());
                        response.setPaymentStatusToReleaseVoucher(supplierConfigurationResource.getPaymentStatusToReleaseVoucher());
                        response.setUnitOfMeasurement(supplierConfigurationResource.getUnitOfMeasurement());
                        response.setNoOfDaysToReleaseVoucher(supplierConfigurationResource.getNoOfDaysToReleaseVoucher());
                        response.setNoOfDaysToSendAlarm(supplierConfigurationResource.getNoOfDaysToSendAlarm());
                        response.setMultiplier(supplierConfigurationResource.getMultiplier());
                        response.setCustomerTemplateId(supplierConfigurationResource.getCustomerTemplateId());
                        response.setSupplierTemplateId(supplierConfigurationResource.getSupplierTemplateId());
                        response.setCompanyId(supplierConfigurationResource.getCompanyId());
                        List<VoucherDetailsResponse> voucherDetailsResponseList = new ArrayList<>();
                        Set<VoucherCodeResource> codes = supplierConfigurationResource.getVoucherCodes();
                        if (codes!=null && codes.size()>0)
                        {
                            for (VoucherCodeResource codeResource : codes) {
                                VoucherDetailsResponse voucherDetailsResponse = new VoucherDetailsResponse();
                                VoucherCodeResponse voucherCodeResponse = new VoucherCodeResponse();
                                voucherCodeResponse.setVoucherCodeStatus(VoucherCodeStatus.UNASSIGNED);
                                if (codeResource.getInputVoucherCodes()==null)
                                {
                                    codeResource.setInputVoucherCodes(new HashSet<>());
                                }
                                Set<String> inputVoucherCodes = codeResource.getInputVoucherCodes();
                                inputVoucherCodes.stream().map(String::valueOf).collect(Collectors.toList());
                                String code  = String.join(",",inputVoucherCodes);
                                voucherCodeResponse.setVoucherCode(code);
                                voucherCodeResponse.setDateOfUpload(ZonedDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now())));
                                FileType fileType = codeResource.getFileType();
                                VoucherCodeUsageType voucherCodeUsageType = codeResource.getVoucherCodeUsageType();
                                voucherCodeResponse.setFileType(fileType);
                                voucherCodeResponse.setVoucherCodeUsageType(voucherCodeUsageType);
                                if (FileType.BROWSE_FILE.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType))
                                {
                                    voucherCodeResponse.setFlieId(codeResource.getFileId());
                                }
                                voucherCodeResponse.setSupplierConfigId(supplierConfigurationResource.getId());
                                voucherDetailsResponse.setVoucherCodeResponse(voucherCodeResponse);
                                voucherDetailsResponseList.add(voucherDetailsResponse);
                            }
                        }
                        response.setVoucherDetailsResponseList(voucherDetailsResponseList);
                        workflowResp.getJSONObject("doc").put("newDoc",new JSONObject(response));


                        map.put("Success", "Record submitted sucessfully with workflow id " + id);
                        map.put("data", workflowResp);
                        return map;
                    } else {
                        throw new OperationException("Data not saved");
                    }
                }
            }
        }
        else if (draft && !workFlow)
        {
            Set<VoucherCodeResource> voucherCodeResources = new HashSet<>();
            SupplierConfigurationResource resource = new SupplierConfigurationResource();
            SupplierVoucherCodes supplierVoucherCodes = supplierConfigurationRepository.get(workflowId);
            supplierVoucherCodes.setSupplierTemplateId(updateSupplierConfigResource.getSupplierTemplateId());
            supplierVoucherCodes.setCustomerTemplateId(updateSupplierConfigResource.getCustomerTemplateId());
            supplierVoucherCodes.setPaymentStatusToReleaseVoucher(updateSupplierConfigResource.getPaymentStatusToReleaseVoucher());
            supplierVoucherCodes.setNoOfDaysToReleaseVoucher(updateSupplierConfigResource.getNoOfDaysToReleaseVoucher());
            supplierVoucherCodes.setNoOfDaysToSendAlarm(updateSupplierConfigResource.getNoOfDaysToSendAlarm());
            CopyUtils.copy(supplierVoucherCodes,resource);
            Set<VoucherCode> codeSet = supplierVoucherCodes.getVoucherCodes();
            for (VoucherCode code : codeSet)
            {
                VoucherCodeResource voucherCodeResource =new VoucherCodeResource();
                voucherCodeResource.setSupplierConfigId(code.getId());
                VoucherCodeUsageType voucherCodeUsageType = code.getVoucherCodeUsageType();
                voucherCodeResource.setVoucherCodeUsageType(voucherCodeUsageType);
                FileType fileType = code.getFileType();
                voucherCodeResource.setFileType(fileType);
                if (voucherCodeUsageType.equals(VoucherCodeUsageType.FIXED) && FileType.BROWSE_FILE.equals(fileType))
                {
                    String fileId = code.getFileId();
                    voucherCodeResource.setFileId(fileId);
                    DocumentSearchResource documentSearchResource = new DocumentSearchResource();
                    documentSearchResource.setId(fileId);
                    List<DocumentResource> documents = documentLibraryService.getDocuments(Collections.singletonList(documentSearchResource));
                    voucherCodeResource.setFileName(documents.get(0).getFileName());
                }
                else if (voucherCodeUsageType.equals(VoucherCodeUsageType.FIXED) && FileType.INPUT_CODE.equals(fileType))
                {
                    voucherCodeResource.setInputVoucherCodes(Collections.singleton(code.getVoucherCode()));
                }
                else {
                    voucherCodeResource.setInputVoucherCodes(Collections.singleton(code.getVoucherCode()));
                }
                voucherCodeResources.add(voucherCodeResource);
            }

            resource.setVoucherCodes(voucherCodeResources);
            String workflow = workflowService.saveWorkflow(resource, WorkflowOperation.SAVE, entityType, loggedInUser.getUserID());
            JSONObject workflowResp = new JSONObject(workflow);
            String id = workflowResp.optString("_id");
            if (!StringUtils.isEmpty(id))
            {
//                        JSONObject jsonObject = (JSONObject) workflowResp.optJSONArray("data").get(0);
                Object newDocs = workflowResp.getJSONObject("doc").getJSONObject("newDoc");
                response = new SupplierVoucherConfigSearchResponse();
                SupplierConfigurationResource supplierConfigurationResource = objectMapper.convertValue(newDocs, SupplierConfigurationResource.class);
                response.setId(supplierConfigurationResource.getId());
                response.setSupplierId(supplierConfigurationResource.getSupplierId());
                response.setSupplierName(supplierConfigurationResource.getSupplierName());
                response.setProductName(supplierConfigurationResource.getProductName());
                response.setProductCategoryName(supplierConfigurationResource.getProductCategoryName());
                response.setProductSubCategoryName(supplierConfigurationResource.getProductSubCategoryName());
                response.setVoucherToBeAppliedOn(supplierConfigurationResource.getVoucherToBeAppliedOn());
                response.setVoucherCodeUsageType(supplierConfigurationResource.getVoucherCodeUsageType());
                response.setPaymentStatusToReleaseVoucher(supplierConfigurationResource.getPaymentStatusToReleaseVoucher());
                response.setUnitOfMeasurement(supplierConfigurationResource.getUnitOfMeasurement());
                response.setNoOfDaysToReleaseVoucher(supplierConfigurationResource.getNoOfDaysToReleaseVoucher());
                response.setNoOfDaysToSendAlarm(supplierConfigurationResource.getNoOfDaysToSendAlarm());
                response.setMultiplier(supplierConfigurationResource.getMultiplier());
                response.setCustomerTemplateId(supplierConfigurationResource.getCustomerTemplateId());
                response.setSupplierTemplateId(supplierConfigurationResource.getSupplierTemplateId());
                response.setCompanyId(supplierConfigurationResource.getCompanyId());
                List<VoucherDetailsResponse> voucherDetailsResponseList = new ArrayList<>();
                Set<VoucherCodeResource> codes = supplierConfigurationResource.getVoucherCodes();
                if (codes!=null && codes.size()>0)
                {
                    for (VoucherCodeResource codeResource : codes) {
                        VoucherDetailsResponse voucherDetailsResponse = new VoucherDetailsResponse();
                        VoucherCodeResponse voucherCodeResponse = new VoucherCodeResponse();
                        voucherCodeResponse.setVoucherCodeStatus(VoucherCodeStatus.UNASSIGNED);
                        if (codeResource.getInputVoucherCodes()==null)
                        {
                            codeResource.setInputVoucherCodes(new HashSet<>());
                        }
                        Set<String> inputVoucherCodes = codeResource.getInputVoucherCodes();
                        inputVoucherCodes.stream().map(String::valueOf).collect(Collectors.toList());
                        String code  = String.join(",",inputVoucherCodes);
                        voucherCodeResponse.setVoucherCode(code);
                        voucherCodeResponse.setDateOfUpload(ZonedDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now())));
                        FileType fileType = codeResource.getFileType();
                        VoucherCodeUsageType voucherCodeUsageType = codeResource.getVoucherCodeUsageType();
                        voucherCodeResponse.setFileType(fileType);
                        voucherCodeResponse.setVoucherCodeUsageType(voucherCodeUsageType);
                        if (FileType.BROWSE_FILE.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType))
                        {
                            voucherCodeResponse.setFlieId(codeResource.getFileId());
                        }
                        voucherCodeResponse.setSupplierConfigId(supplierConfigurationResource.getId());
                        voucherDetailsResponse.setVoucherCodeResponse(voucherCodeResponse);
                        voucherDetailsResponseList.add(voucherDetailsResponse);
                    }
                }
                response.setVoucherDetailsResponseList(voucherDetailsResponseList);
                workflowResp.getJSONObject("doc").put("newDoc",new JSONObject(response));


                map.put("Success", "Record submitted sucessfully with workflow id " + id);
                map.put("data", workflowResp);
                return map;
            } else {
                throw new OperationException("Data not saved");
            }
        }
        else
        {
            Set<VoucherCodeResource> voucherCodeResources = new HashSet<>();
            SupplierConfigurationResource resource = new SupplierConfigurationResource();
            SupplierVoucherCodes supplierVoucherCodes = supplierConfigurationRepository.get(workflowId);
            supplierVoucherCodes.setSupplierTemplateId(updateSupplierConfigResource.getSupplierTemplateId());
            supplierVoucherCodes.setCustomerTemplateId(updateSupplierConfigResource.getCustomerTemplateId());
            supplierVoucherCodes.setPaymentStatusToReleaseVoucher(updateSupplierConfigResource.getPaymentStatusToReleaseVoucher());
            supplierVoucherCodes.setNoOfDaysToReleaseVoucher(updateSupplierConfigResource.getNoOfDaysToReleaseVoucher());
            supplierVoucherCodes.setNoOfDaysToSendAlarm(updateSupplierConfigResource.getNoOfDaysToSendAlarm());
            CopyUtils.copy(supplierVoucherCodes,resource);
            Set<VoucherCode> codeSet = supplierVoucherCodes.getVoucherCodes();
            for (VoucherCode code : codeSet)
            {
                VoucherCodeResource voucherCodeResource =new VoucherCodeResource();
                voucherCodeResource.setSupplierConfigId(code.getId());
                VoucherCodeUsageType voucherCodeUsageType = code.getVoucherCodeUsageType();
                voucherCodeResource.setVoucherCodeUsageType(voucherCodeUsageType);
                FileType fileType = code.getFileType();
                voucherCodeResource.setFileType(fileType);
                if (voucherCodeUsageType.equals(VoucherCodeUsageType.FIXED) && FileType.BROWSE_FILE.equals(fileType))
                {
                    String fileId = code.getFileId();
                    voucherCodeResource.setFileId(fileId);
                    DocumentSearchResource documentSearchResource = new DocumentSearchResource();
                    documentSearchResource.setId(fileId);
                    List<DocumentResource> documents = documentLibraryService.getDocuments(Collections.singletonList(documentSearchResource));
                    voucherCodeResource.setFileName(documents.get(0).getFileName());
                }
                else if (voucherCodeUsageType.equals(VoucherCodeUsageType.FIXED) && FileType.INPUT_CODE.equals(fileType))
                {
                    voucherCodeResource.setInputVoucherCodes(Collections.singleton(code.getVoucherCode()));
                }
                else {
                    voucherCodeResource.setInputVoucherCodes(Collections.singleton(code.getVoucherCode()));
                }
                voucherCodeResources.add(voucherCodeResource);
            }

            resource.setVoucherCodes(voucherCodeResources);
            String workflow = workflowService.saveWorkflow(resource, WorkflowOperation.SUBMIT, entityType, loggedInUser.getUserID());
            JSONObject workflowResp = new JSONObject(workflow);
            String id = workflowResp.optString("_id");
            if (!StringUtils.isEmpty(id))
            {
//                        JSONObject jsonObject = (JSONObject) workflowResp.optJSONArray("data").get(0);
                Object newDocs = workflowResp.getJSONObject("doc").getJSONObject("newDoc");
                response = new SupplierVoucherConfigSearchResponse();
                SupplierConfigurationResource supplierConfigurationResource = objectMapper.convertValue(newDocs, SupplierConfigurationResource.class);
                response.setId(supplierConfigurationResource.getId());
                response.setSupplierId(supplierConfigurationResource.getSupplierId());
                response.setSupplierName(supplierConfigurationResource.getSupplierName());
                response.setProductName(supplierConfigurationResource.getProductName());
                response.setProductCategoryName(supplierConfigurationResource.getProductCategoryName());
                response.setProductSubCategoryName(supplierConfigurationResource.getProductSubCategoryName());
                response.setVoucherToBeAppliedOn(supplierConfigurationResource.getVoucherToBeAppliedOn());
                response.setVoucherCodeUsageType(supplierConfigurationResource.getVoucherCodeUsageType());
                response.setPaymentStatusToReleaseVoucher(supplierConfigurationResource.getPaymentStatusToReleaseVoucher());
                response.setUnitOfMeasurement(supplierConfigurationResource.getUnitOfMeasurement());
                response.setNoOfDaysToReleaseVoucher(supplierConfigurationResource.getNoOfDaysToReleaseVoucher());
                response.setNoOfDaysToSendAlarm(supplierConfigurationResource.getNoOfDaysToSendAlarm());
                response.setMultiplier(supplierConfigurationResource.getMultiplier());
                response.setCustomerTemplateId(supplierConfigurationResource.getCustomerTemplateId());
                response.setSupplierTemplateId(supplierConfigurationResource.getSupplierTemplateId());
                response.setCompanyId(supplierConfigurationResource.getCompanyId());
                List<VoucherDetailsResponse> voucherDetailsResponseList = new ArrayList<>();
                Set<VoucherCodeResource> codes = supplierConfigurationResource.getVoucherCodes();
                if (codes!=null && codes.size()>0)
                {
                    for (VoucherCodeResource codeResource : codes) {
                        VoucherDetailsResponse voucherDetailsResponse = new VoucherDetailsResponse();
                        VoucherCodeResponse voucherCodeResponse = new VoucherCodeResponse();
                        voucherCodeResponse.setVoucherCodeStatus(VoucherCodeStatus.UNASSIGNED);
                        if (codeResource.getInputVoucherCodes()==null)
                        {
                            codeResource.setInputVoucherCodes(new HashSet<>());
                        }
                        Set<String> inputVoucherCodes = codeResource.getInputVoucherCodes();
                        inputVoucherCodes.stream().map(String::valueOf).collect(Collectors.toList());
                        String code  = String.join(",",inputVoucherCodes);
                        voucherCodeResponse.setVoucherCode(code);
                        voucherCodeResponse.setDateOfUpload(ZonedDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now())));
                        FileType fileType = codeResource.getFileType();
                        VoucherCodeUsageType voucherCodeUsageType = codeResource.getVoucherCodeUsageType();
                        voucherCodeResponse.setFileType(fileType);
                        voucherCodeResponse.setVoucherCodeUsageType(voucherCodeUsageType);
                        if (FileType.BROWSE_FILE.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType))
                        {
                            voucherCodeResponse.setFlieId(codeResource.getFileId());
                        }
                        voucherCodeResponse.setSupplierConfigId(supplierConfigurationResource.getId());
                        voucherDetailsResponse.setVoucherCodeResponse(voucherCodeResponse);
                        voucherDetailsResponseList.add(voucherDetailsResponse);
                    }
                }
                response.setVoucherDetailsResponseList(voucherDetailsResponseList);
                workflowResp.getJSONObject("doc").put("newDoc",new JSONObject(response));


                map.put("Success", "Record submitted sucessfully with workflow id " + id);
                map.put("data", workflowResp);
                return map;
            } else {
                throw new OperationException("Data not saved");
            }

        }
        return null;
    }


    private boolean checkSimilarities(SupplierConfigurationResource workFlowResp , UpdateSupplierConfigResource updatedRequest)
    {
        boolean releaseVoucher = false;
        boolean sendAlram = false;
        boolean status = false;
        boolean customerId = false;
        boolean supplierId = false;
        int i = updatedRequest.getNoOfDaysToReleaseVoucher().compareTo(workFlowResp.getNoOfDaysToReleaseVoucher());
        if (i==0)
        {
            releaseVoucher = true;
        }

        int j =  updatedRequest.getNoOfDaysToSendAlarm().compareTo(workFlowResp.getNoOfDaysToSendAlarm());
        if (j==0)
        {
            sendAlram = true;
        }

        if (updatedRequest.getPaymentStatusToReleaseVoucher().equals(workFlowResp.getPaymentStatusToReleaseVoucher()))
        {
            status = true;
        }
        if (updatedRequest.getCustomerTemplateId().equalsIgnoreCase(workFlowResp.getCustomerTemplateId()))
        {
            customerId = true;
        }
        if (updatedRequest.getSupplierTemplateId().equalsIgnoreCase(workFlowResp.getSupplierTemplateId()))
        {
            supplierId = true;
        }

        if (releaseVoucher && sendAlram && status && customerId&& supplierId)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Map getUnApprovedList(VoucherCodeSearchCriteria voucherCodeSearchCriteria) throws OperationException {
        String userId = userService.getLoggedInUserId();
        JSONObject filterObject = new JSONObject();
        SupplierConfigSearchCriteria supplierConfigSearchCriteria = voucherCodeSearchCriteria.getFilter();
        if (!StringUtils.isEmpty(supplierConfigSearchCriteria.getSupplierName()))
        {
            filterObject.putOpt("doc.newDoc.supplierName",supplierConfigSearchCriteria.getSupplierName());
        }
        if (!StringUtils.isEmpty(supplierConfigSearchCriteria.getProductCategoryName()))
        {
            filterObject.putOpt("doc.newDoc.productCategoryName",supplierConfigSearchCriteria.getProductCategoryName());
        }
        if (!StringUtils.isEmpty(supplierConfigSearchCriteria.getProductSubCategoryName()))
        {
            filterObject.putOpt("doc.newDoc.productSubCategoryName",supplierConfigSearchCriteria.getProductSubCategoryName());
        }
        if (!StringUtils.isEmpty(supplierConfigSearchCriteria.getProductName()))
        {
            filterObject.putOpt("doc.newDoc.productName",supplierConfigSearchCriteria.getProductName());
        }
        if (!StringUtils.isEmpty(supplierConfigSearchCriteria.getCompanyId()))
        {
            filterObject.putOpt("doc.newDoc.companyId",supplierConfigSearchCriteria.getCompanyId());
        }
        Map<String, Object> workFlows = workflowService.getWorkFlows(entityType, userId, filterObject, null, voucherCodeSearchCriteria.getPage(), voucherCodeSearchCriteria.getCount());
        return workFlows;
    }

    @Override
    public SupplierVoucherConfigSearchResponse getVoucherCode(String id, boolean workflow) throws OperationException {
        logger.info("Entered SupplierConfigurationServiceImpl :: getVoucherCode() method ");
        SupplierVoucherConfigSearchResponse supplierVoucherConfigSearchResponse = null;
        if (workflow)
        {
            supplierVoucherConfigSearchResponse = new SupplierVoucherConfigSearchResponse();
            JSONObject docs = workflowService.getDocs(id);
            if (docs!=null && docs.length()>0) {
                SupplierConfigurationResource supplierConfigurationResource = objectMapper.convertValue(docs, SupplierConfigurationResource.class);
                supplierVoucherConfigSearchResponse.setId(supplierConfigurationResource.getId());
                supplierVoucherConfigSearchResponse.setSupplierId(supplierConfigurationResource.getSupplierId());
                supplierVoucherConfigSearchResponse.setSupplierName(supplierConfigurationResource.getSupplierName());
                supplierVoucherConfigSearchResponse.setProductName(supplierConfigurationResource.getProductName());
                supplierVoucherConfigSearchResponse.setProductCategoryName(supplierConfigurationResource.getProductCategoryName());
                supplierVoucherConfigSearchResponse.setProductSubCategoryName(supplierConfigurationResource.getProductSubCategoryName());
                supplierVoucherConfigSearchResponse.setVoucherToBeAppliedOn(supplierConfigurationResource.getVoucherToBeAppliedOn());
                supplierVoucherConfigSearchResponse.setVoucherCodeUsageType(supplierConfigurationResource.getVoucherCodeUsageType());
                supplierVoucherConfigSearchResponse.setPaymentStatusToReleaseVoucher(supplierConfigurationResource.getPaymentStatusToReleaseVoucher());
                supplierVoucherConfigSearchResponse.setUnitOfMeasurement(supplierConfigurationResource.getUnitOfMeasurement());
                supplierVoucherConfigSearchResponse.setNoOfDaysToReleaseVoucher(supplierConfigurationResource.getNoOfDaysToReleaseVoucher());
                supplierVoucherConfigSearchResponse.setNoOfDaysToSendAlarm(supplierConfigurationResource.getNoOfDaysToSendAlarm());
                supplierVoucherConfigSearchResponse.setMultiplier(supplierConfigurationResource.getMultiplier());
                supplierVoucherConfigSearchResponse.setSupplierTemplateId(supplierConfigurationResource.getSupplierTemplateId());
                supplierVoucherConfigSearchResponse.setCustomerTemplateId(supplierConfigurationResource.getCustomerTemplateId());

                List<VoucherDetailsResponse> voucherDetailsResponseList = new ArrayList<>();
                Set<VoucherCodeResource> voucherCodes = supplierConfigurationResource.getVoucherCodes();
                if (voucherCodes != null && voucherCodes.size() > 0) {
                    for (VoucherCodeResource resource : voucherCodes) {
                        VoucherDetailsResponse voucherDetailsResponse = new VoucherDetailsResponse();
                        VoucherCodeResponse response = new VoucherCodeResponse();
                        response.setVoucherCodeUsageType(resource.getVoucherCodeUsageType());
                        Set<String> inputVoucherCodes = resource.getInputVoucherCodes();
                        inputVoucherCodes.stream().map(String::valueOf).collect(Collectors.toList());
                        String code = String.join(",", inputVoucherCodes);
                        response.setVoucherCode(code);
                        response.setFileType(resource.getFileType());
                        response.setDateOfUpload(ZonedDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now())));
                        response.setVoucherCodeStatus(VoucherCodeStatus.UNASSIGNED);
                        voucherDetailsResponse.setVoucherCodeResponse(response);
                        voucherDetailsResponseList.add(voucherDetailsResponse);
                    }
                }
                supplierVoucherConfigSearchResponse.setVoucherDetailsResponseList(voucherDetailsResponseList);
                return supplierVoucherConfigSearchResponse;
            }
            else
            {
                logger.debug("No record exist with the given Id");
                throw new OperationException(Constants.ER01);
            }

        }
        else
        {
            SupplierVoucherCodes supplierVoucherCodes = supplierConfigurationRepository.get(id);
            if (supplierVoucherCodes == null) {
                logger.debug("No record exist with the given Id");
                throw new OperationException(Constants.ER01);
            }
            supplierVoucherConfigSearchResponse = get(supplierVoucherCodes);
            return supplierVoucherConfigSearchResponse;
        }
    }

    @Override
    public JSONObject edit(String id, boolean workflow, boolean lock) throws OperationException, IOException, RepositoryException
    {
        SupplierVoucherCodes supplierVoucherCodes = null;
        OpsUser loggedInUser = userService.getLoggedInUser();

        if (workflow)
        {
            if (lock)
            {
                String workFlowById = workflowService.getWorkFlowById(id);
                JSONObject object = new JSONObject(workFlowById);
                JSONObject workFlowJsonObj = (JSONObject) object.optJSONArray("data").get(0);
                Object newDoc = workFlowJsonObj.getJSONObject("doc").getJSONObject("newDoc");
                SupplierVoucherConfigSearchResponse response = new SupplierVoucherConfigSearchResponse();
                SupplierConfigurationResource supplierConfigurationResource = objectMapper.convertValue(newDoc, SupplierConfigurationResource.class);
                response.setId(supplierConfigurationResource.getId());
                response.setSupplierId(supplierConfigurationResource.getSupplierId());
                response.setSupplierName(supplierConfigurationResource.getSupplierName());
                response.setProductName(supplierConfigurationResource.getProductName());
                response.setProductCategoryName(supplierConfigurationResource.getProductCategoryName());
                response.setProductSubCategoryName(supplierConfigurationResource.getProductSubCategoryName());
                response.setVoucherToBeAppliedOn(supplierConfigurationResource.getVoucherToBeAppliedOn());
                response.setVoucherCodeUsageType(supplierConfigurationResource.getVoucherCodeUsageType());
                response.setPaymentStatusToReleaseVoucher(supplierConfigurationResource.getPaymentStatusToReleaseVoucher());
                response.setUnitOfMeasurement(supplierConfigurationResource.getUnitOfMeasurement());
                response.setNoOfDaysToReleaseVoucher(supplierConfigurationResource.getNoOfDaysToReleaseVoucher());
                response.setNoOfDaysToSendAlarm(supplierConfigurationResource.getNoOfDaysToSendAlarm());
                response.setMultiplier(supplierConfigurationResource.getMultiplier());
                response.setCustomerTemplateId(supplierConfigurationResource.getCustomerTemplateId());
                response.setSupplierTemplateId(supplierConfigurationResource.getSupplierTemplateId());
                response.setCompanyId(supplierConfigurationResource.getCompanyId());
                List<VoucherDetailsResponse> voucherDetailsResponseList = new ArrayList<>();
                Set<VoucherCodeResource> voucherCodes = supplierConfigurationResource.getVoucherCodes();
                if (voucherCodes != null && voucherCodes.size() > 0) {
                    for (VoucherCodeResource codeResource : voucherCodes) {
                        VoucherDetailsResponse voucherDetailsResponse = new VoucherDetailsResponse();
                        VoucherCodeResponse voucherCodeResponse = new VoucherCodeResponse();
                        voucherCodeResponse.setVoucherCodeStatus(VoucherCodeStatus.UNASSIGNED);
                        FileType fileType = codeResource.getFileType();
                        VoucherCodeUsageType voucherCodeUsageType = codeResource.getVoucherCodeUsageType();
                        if (FileType.INPUT_CODE.equals(fileType) && VoucherCodeUsageType.MULTIPLE.equals(voucherCodeUsageType) ||
                                FileType.INPUT_CODE.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType)) {
                            Set<String> inputVoucherCodes = codeResource.getInputVoucherCodes();
                            if (inputVoucherCodes!=null && inputVoucherCodes.size()>0) {
                                inputVoucherCodes.stream().map(String::valueOf).collect(Collectors.toList());
                                String code = String.join(",", inputVoucherCodes);
                                voucherCodeResponse.setVoucherCode(code);
                            }
                        } else if (FileType.BROWSE_FILE.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType)) {
                            voucherCodeResponse.setFlieId(codeResource.getFileId());
                            voucherCodeResponse.setVoucherCode(codeResource.getFileName());
                        } else if (FileType.SUPPLIER_URL.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType)) {

                        }
                        voucherCodeResponse.setVoucherCodeUsageType(voucherCodeUsageType);
                        voucherCodeResponse.setFileType(fileType);
                        voucherCodeResponse.setSupplierConfigId(supplierConfigurationResource.getId());
                        voucherCodeResponse.setDateOfUpload(ZonedDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now())));
                        voucherDetailsResponse.setVoucherCodeResponse(voucherCodeResponse);
                        voucherDetailsResponseList.add(voucherDetailsResponse);
                    }
                }
                response.setVoucherDetailsResponseList(voucherDetailsResponseList);
                workFlowJsonObj.getJSONObject("doc").put("newDoc", new JSONObject(response));
                return workFlowJsonObj;
            }
            else
                {
                String workFlowById = workflowService.getWorkFlowById(id);
                JSONObject object = new JSONObject(workFlowById);
                JSONObject workFlowJsonObj = (JSONObject) object.optJSONArray("data").get(0);
                Object newDoc = workFlowJsonObj.getJSONObject("doc").getJSONObject("newDoc");
                SupplierVoucherConfigSearchResponse response = new SupplierVoucherConfigSearchResponse();
                SupplierConfigurationResource supplierConfigurationResource = objectMapper.convertValue(newDoc, SupplierConfigurationResource.class);
                response.setId(supplierConfigurationResource.getId());
                response.setSupplierId(supplierConfigurationResource.getSupplierId());
                response.setSupplierName(supplierConfigurationResource.getSupplierName());
                response.setProductName(supplierConfigurationResource.getProductName());
                response.setProductCategoryName(supplierConfigurationResource.getProductCategoryName());
                response.setProductSubCategoryName(supplierConfigurationResource.getProductSubCategoryName());
                response.setVoucherToBeAppliedOn(supplierConfigurationResource.getVoucherToBeAppliedOn());
                response.setVoucherCodeUsageType(supplierConfigurationResource.getVoucherCodeUsageType());
                response.setPaymentStatusToReleaseVoucher(supplierConfigurationResource.getPaymentStatusToReleaseVoucher());
                response.setUnitOfMeasurement(supplierConfigurationResource.getUnitOfMeasurement());
                response.setNoOfDaysToReleaseVoucher(supplierConfigurationResource.getNoOfDaysToReleaseVoucher());
                response.setNoOfDaysToSendAlarm(supplierConfigurationResource.getNoOfDaysToSendAlarm());
                response.setMultiplier(supplierConfigurationResource.getMultiplier());
                response.setCustomerTemplateId(supplierConfigurationResource.getCustomerTemplateId());
                response.setSupplierTemplateId(supplierConfigurationResource.getSupplierTemplateId());
                response.setCompanyId(supplierConfigurationResource.getCompanyId());
                List<VoucherDetailsResponse> voucherDetailsResponseList = new ArrayList<>();
                Set<VoucherCodeResource> voucherCodes = supplierConfigurationResource.getVoucherCodes();
                if (voucherCodes != null && voucherCodes.size() > 0) {
                    for (VoucherCodeResource codeResource : voucherCodes) {
                        VoucherDetailsResponse voucherDetailsResponse = new VoucherDetailsResponse();
                        VoucherCodeResponse voucherCodeResponse = new VoucherCodeResponse();
                        voucherCodeResponse.setVoucherCodeStatus(VoucherCodeStatus.UNASSIGNED);
                        FileType fileType = codeResource.getFileType();
                        VoucherCodeUsageType voucherCodeUsageType = codeResource.getVoucherCodeUsageType();
                        if (FileType.INPUT_CODE.equals(fileType) && VoucherCodeUsageType.MULTIPLE.equals(voucherCodeUsageType) ||
                                FileType.INPUT_CODE.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType)) {
                            Set<String> inputVoucherCodes = codeResource.getInputVoucherCodes();
                            inputVoucherCodes.stream().map(String::valueOf).collect(Collectors.toList());
                            String code = String.join(",", inputVoucherCodes);
                            voucherCodeResponse.setVoucherCode(code);
                        } else if (FileType.BROWSE_FILE.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType)) {
                            voucherCodeResponse.setFlieId(codeResource.getFileId());
                            voucherCodeResponse.setVoucherCode(codeResource.getFileName());
                        } else if (FileType.SUPPLIER_URL.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType)) {

                        }
                        voucherCodeResponse.setVoucherCodeUsageType(voucherCodeUsageType);
                        voucherCodeResponse.setFileType(fileType);
                        voucherCodeResponse.setSupplierConfigId(supplierConfigurationResource.getId());
                        voucherCodeResponse.setDateOfUpload(ZonedDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now())));
                        voucherDetailsResponse.setVoucherCodeResponse(voucherCodeResponse);
                        voucherDetailsResponseList.add(voucherDetailsResponse);
                    }
                }
                response.setVoucherDetailsResponseList(voucherDetailsResponseList);
                workFlowJsonObj.getJSONObject("doc").put("newDoc", new JSONObject(response));
                return workFlowJsonObj;
            }
        }
        //2. id is master id when workflow is false---get MAster Data
        else if (!workflow && !lock)
        {
            SupplierVoucherCodes exisingSupplierVoucherCodes = supplierConfigurationRepository.get(id);
            if (exisingSupplierVoucherCodes==null)
            {
                logger.debug("No record exist with the given Id");
                throw new OperationException(Constants.ER01);
            }
            SupplierVoucherConfigSearchResponse supplierVoucherConfigSearchResponse = get(exisingSupplierVoucherCodes);
            RequestLockObject lockObject = new RequestLockObject();
            lockObject.setEnabled(exisingSupplierVoucherCodes.isEnabled());
            lockObject.setUser(exisingSupplierVoucherCodes.getUserId());
            lockObject.setUserId(exisingSupplierVoucherCodes.getUserId());
            lockObject.setWorkflowId(exisingSupplierVoucherCodes.getWorkflowId());
            supplierVoucherConfigSearchResponse.setLock(lockObject);
            return new JSONObject(supplierVoucherConfigSearchResponse);
        }
        //3. id is master id when workflow is false and lock is true
        else if (!workflow && lock)
        {
            SupplierVoucherCodes exisingSupplierVoucherCodes = supplierConfigurationRepository.get(id);
            if (exisingSupplierVoucherCodes==null)
            {
                logger.debug("No record exist with the given Id");
                throw new OperationException(Constants.ER01);
            }
            boolean enabled = exisingSupplierVoucherCodes.isEnabled();
            if (!enabled)
            {
                SupplierConfigurationResource resource = new SupplierConfigurationResource();
                Set<VoucherCodeResource> voucherCodeResources = new HashSet<>();
                exisingSupplierVoucherCodes.setEnabled(true);
                exisingSupplierVoucherCodes.setUserId(userService.getLoggedInUserId());
                CopyUtils.copy(exisingSupplierVoucherCodes,resource);
                Set<VoucherCode> voucherCodes = exisingSupplierVoucherCodes.getVoucherCodes();
                for (VoucherCode vc: voucherCodes)
                {
                    VoucherCodeResource codeResource =new VoucherCodeResource();
                    codeResource.setSupplierConfigId(vc.getId());
                    VoucherCodeUsageType voucherCodeUsageType = vc.getVoucherCodeUsageType();
                    codeResource.setVoucherCodeUsageType(voucherCodeUsageType);
                    FileType fileType = vc.getFileType();
                    codeResource.setFileType(fileType);
                    if (voucherCodeUsageType.equals(VoucherCodeUsageType.FIXED) && FileType.BROWSE_FILE.equals(fileType))
                    {
                        String fileId = vc.getFileId();
                        codeResource.setFileId(fileId);
                        DocumentSearchResource documentSearchResource = new DocumentSearchResource();
                        documentSearchResource.setId(fileId);
                        List<DocumentResource> documents = documentLibraryService.getDocuments(Collections.singletonList(documentSearchResource));
                        codeResource.setFileName(documents.get(0).getFileName());
                    }
                    else if (voucherCodeUsageType.equals(VoucherCodeUsageType.FIXED) && FileType.INPUT_CODE.equals(fileType))
                    {
                        codeResource.setInputVoucherCodes(Collections.singleton(vc.getVoucherCode()));
                    }
                    else {
                        codeResource.setInputVoucherCodes(Collections.singleton(vc.getVoucherCode()));
                    }
                    voucherCodeResources.add(codeResource);
                }
                resource.setVoucherCodes(voucherCodeResources);
                String s = workflowService.saveWorkflow(resource, WorkflowOperation.SAVE, entityType, userService.getLoggedInUserId());
                if (StringUtils.isEmpty(s))
                {
                    throw new OperationException("Unable to Save Workflow");
                }
                        JSONObject workFlowJsonObject = new JSONObject(s);
                        String workFlowId = workFlowJsonObject.optString("_id");
                        if (!StringUtils.isEmpty(workFlowId)) {
                            exisingSupplierVoucherCodes.setWorkflowId(workFlowId);
                        }

                        exisingSupplierVoucherCodes.setVoucherCodes(voucherCodes);
                    exisingSupplierVoucherCodes = supplierConfigurationRepository.add(exisingSupplierVoucherCodes);
                SupplierVoucherConfigSearchResponse supplierVoucherConfigSearchResponse = get(exisingSupplierVoucherCodes);
                RequestLockObject lockObject = new RequestLockObject();
                lockObject.setEnabled(exisingSupplierVoucherCodes.isEnabled());
                lockObject.setUser(exisingSupplierVoucherCodes.getUserId());
                lockObject.setUserId(exisingSupplierVoucherCodes.getUserId());
                lockObject.setWorkflowId(exisingSupplierVoucherCodes.getWorkflowId());
                supplierVoucherConfigSearchResponse.setLock(lockObject);
                return new JSONObject(supplierVoucherConfigSearchResponse);

            }
            else
            {
                String userId = exisingSupplierVoucherCodes.getUserId();
                String loggedInUserUserID = loggedInUser.getUserID();
                if (loggedInUserUserID.equalsIgnoreCase(userId) )
                {
                    if (StringUtils.isEmpty(exisingSupplierVoucherCodes.getWorkflowId()))
                    {
                        SupplierConfigurationResource resource = new SupplierConfigurationResource();
                        Set<VoucherCodeResource> voucherCodeResources = new HashSet<>();
                        exisingSupplierVoucherCodes.setEnabled(true);
                        exisingSupplierVoucherCodes.setUserId(userService.getLoggedInUserId());
                        CopyUtils.copy(exisingSupplierVoucherCodes, resource);
                        Set<VoucherCode> voucherCodes = exisingSupplierVoucherCodes.getVoucherCodes();
                        for (VoucherCode vc : voucherCodes) {
                            VoucherCodeResource codeResource = new VoucherCodeResource();
                            codeResource.setSupplierConfigId(vc.getId());
                            VoucherCodeUsageType voucherCodeUsageType = vc.getVoucherCodeUsageType();
                            codeResource.setVoucherCodeUsageType(voucherCodeUsageType);
                            FileType fileType = vc.getFileType();
                            codeResource.setFileType(fileType);
                            if (voucherCodeUsageType.equals(VoucherCodeUsageType.FIXED) && FileType.BROWSE_FILE.equals(fileType)) {
                                String fileId = vc.getFileId();
                                codeResource.setFileId(fileId);
                                DocumentSearchResource documentSearchResource = new DocumentSearchResource();
                                documentSearchResource.setId(fileId);
                        List<DocumentResource> documents = documentLibraryService.getDocuments(Collections.singletonList(documentSearchResource));
                        codeResource.setFileName(documents.get(0).getFileName());
                            } else if (voucherCodeUsageType.equals(VoucherCodeUsageType.FIXED) && FileType.INPUT_CODE.equals(fileType)) {
                                codeResource.setInputVoucherCodes(Collections.singleton(vc.getVoucherCode()));
                            } else {
                                codeResource.setInputVoucherCodes(Collections.singleton(vc.getVoucherCode()));
                            }
                            voucherCodeResources.add(codeResource);
                        }
                        resource.setVoucherCodes(voucherCodeResources);
                        String s = workflowService.saveWorkflow(resource, WorkflowOperation.SAVE, entityType, userService.getLoggedInUserId());
                        if (StringUtils.isEmpty(s)) {
                            throw new OperationException("Unable to Save Workflow");
                        }
                        JSONObject workFlowJsonObject = new JSONObject(s);
                        String workFlowId = workFlowJsonObject.optString("_id");
                        if (!StringUtils.isEmpty(workFlowId)) {
                            exisingSupplierVoucherCodes.setWorkflowId(workFlowId);
                        }

                        exisingSupplierVoucherCodes.setVoucherCodes(voucherCodes);
                        exisingSupplierVoucherCodes = supplierConfigurationRepository.add(exisingSupplierVoucherCodes);
                        SupplierVoucherConfigSearchResponse supplierVoucherConfigSearchResponse = get(exisingSupplierVoucherCodes);
                        RequestLockObject lockObject = new RequestLockObject();
                        lockObject.setEnabled(exisingSupplierVoucherCodes.isEnabled());
                        lockObject.setUser(exisingSupplierVoucherCodes.getUserId());
                        lockObject.setUserId(exisingSupplierVoucherCodes.getUserId());
                        lockObject.setWorkflowId(exisingSupplierVoucherCodes.getWorkflowId());
                        supplierVoucherConfigSearchResponse.setLock(lockObject);
                        return new JSONObject(supplierVoucherConfigSearchResponse);
                    } else {
                        exisingSupplierVoucherCodes = supplierConfigurationRepository.add(exisingSupplierVoucherCodes);
                        SupplierVoucherConfigSearchResponse supplierVoucherConfigSearchResponse = get(exisingSupplierVoucherCodes);
                        RequestLockObject lockObject = new RequestLockObject();
                        lockObject.setEnabled(exisingSupplierVoucherCodes.isEnabled());
                        lockObject.setUser(exisingSupplierVoucherCodes.getUserId());
                        lockObject.setUserId(exisingSupplierVoucherCodes.getUserId());
                        lockObject.setWorkflowId(exisingSupplierVoucherCodes.getWorkflowId());
                        supplierVoucherConfigSearchResponse.setLock(lockObject);
                        return new JSONObject(supplierVoucherConfigSearchResponse);
                    }
                }
                else {
                    throw new OperationException("Already Locked by User " + exisingSupplierVoucherCodes.getUserId());
                }
            }

        }
        else {
            String workFlowById = workflowService.getWorkFlowById(id);
            JSONObject object = new JSONObject(workFlowById);
            JSONObject workFlowJsonObj= (JSONObject) object.optJSONArray("data").get(0);
            Object newDoc = workFlowJsonObj.getJSONObject("doc").getJSONObject("newDoc");
            SupplierVoucherConfigSearchResponse response = new SupplierVoucherConfigSearchResponse();
            SupplierConfigurationResource supplierConfigurationResource = objectMapper.convertValue(newDoc, SupplierConfigurationResource.class);
            response.setId(supplierConfigurationResource.getId());
            response.setSupplierId(supplierConfigurationResource.getSupplierId());
            response.setSupplierName(supplierConfigurationResource.getSupplierName());
            response.setProductName(supplierConfigurationResource.getProductName());
            response.setProductCategoryName(supplierConfigurationResource.getProductCategoryName());
            response.setProductSubCategoryName(supplierConfigurationResource.getProductSubCategoryName());
            response.setVoucherToBeAppliedOn(supplierConfigurationResource.getVoucherToBeAppliedOn());
            response.setVoucherCodeUsageType(supplierConfigurationResource.getVoucherCodeUsageType());
            response.setPaymentStatusToReleaseVoucher(supplierConfigurationResource.getPaymentStatusToReleaseVoucher());
            response.setUnitOfMeasurement(supplierConfigurationResource.getUnitOfMeasurement());
            response.setNoOfDaysToReleaseVoucher(supplierConfigurationResource.getNoOfDaysToReleaseVoucher());
            response.setNoOfDaysToSendAlarm(supplierConfigurationResource.getNoOfDaysToSendAlarm());
            response.setMultiplier(supplierConfigurationResource.getMultiplier());
            response.setCustomerTemplateId(supplierConfigurationResource.getCustomerTemplateId());
            response.setSupplierTemplateId(supplierConfigurationResource.getSupplierTemplateId());
            response.setCompanyId(supplierConfigurationResource.getCompanyId());
            List<VoucherDetailsResponse> voucherDetailsResponseList = new ArrayList<>();
            Set<VoucherCodeResource> voucherCodes = supplierConfigurationResource.getVoucherCodes();
            if (voucherCodes!=null && voucherCodes.size()>0)
            {
                for (VoucherCodeResource codeResource : voucherCodes)
                {
                    VoucherDetailsResponse voucherDetailsResponse = new VoucherDetailsResponse();
                    VoucherCodeResponse voucherCodeResponse = new VoucherCodeResponse();
                    voucherCodeResponse.setVoucherCodeStatus(VoucherCodeStatus.UNASSIGNED);
                    FileType fileType = codeResource.getFileType();
                    VoucherCodeUsageType voucherCodeUsageType = codeResource.getVoucherCodeUsageType();
                    if (FileType.INPUT_CODE.equals(fileType) && VoucherCodeUsageType.MULTIPLE.equals(voucherCodeUsageType) ||
                            FileType.INPUT_CODE.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType))
                    {
                        Set<String> inputVoucherCodes = codeResource.getInputVoucherCodes();
                        inputVoucherCodes.stream().map(String::valueOf).collect(Collectors.toList());
                        String code  = String.join(",",inputVoucherCodes);
                        voucherCodeResponse.setVoucherCode(code);
                    }
                    else if (FileType.BROWSE_FILE.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType))
                    {
                        voucherCodeResponse.setFlieId(codeResource.getFileId());
                        voucherCodeResponse.setVoucherCode(codeResource.getFileName());
                    }
                    else if (FileType.SUPPLIER_URL.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType))
                    {

                    }
                    voucherCodeResponse.setVoucherCodeUsageType(voucherCodeUsageType);
                    voucherCodeResponse.setFileType(fileType);
                    voucherCodeResponse.setSupplierConfigId(supplierConfigurationResource.getId());
                    voucherCodeResponse.setDateOfUpload(ZonedDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now())));
                    voucherDetailsResponse.setVoucherCodeResponse(voucherCodeResponse);
                    voucherDetailsResponseList.add(voucherDetailsResponse);
                }
            }
            response.setVoucherDetailsResponseList(voucherDetailsResponseList);
            workFlowJsonObj.getJSONObject("doc").put("newDoc",new JSONObject(response));
            return workFlowJsonObj;
        }
    }



@Override
public String releaseLock(String id) throws OperationException {
        String loggedInUserId = userService.getLoggedInUserId();
        SupplierVoucherCodes codes = supplierConfigurationRepository.get(id);
        if (codes.isEnabled())
        {
        if (codes.getUserId().equalsIgnoreCase(loggedInUserId))
        {
        String workFlowResp = workflowService.releaseWorkflow(codes.getWorkflowId());
        if (!StringUtils.isEmpty(workFlowResp))
        {
        codes.setEnabled(false);
        codes.setUserId(null);
        codes.setWorkflowId(null);
        SupplierVoucherCodes add = supplierConfigurationRepository.add(codes);
        return workFlowResp;
        }
        }
        }
        else {
        throw new OperationException("Record with id "+id+" is not locked by any other User");
        }
        return null;
        }

@Override
public JSONObject deleteVoucherCode(VoucherDetailsResponse voucherDetailsResponse) throws OperationException {
        JSONObject resp = new JSONObject();
        FileType fileType = voucherDetailsResponse.getVoucherCodeResponse().getFileType();
        VoucherCodeUsageType voucherCodeUsageType = voucherDetailsResponse.getVoucherCodeResponse().getVoucherCodeUsageType();
        try
        {
        switch (fileType)
        {
        case INPUT_CODE:
        switch (voucherCodeUsageType)
        {
        case MULTIPLE:
        String supplierConfigId = voucherDetailsResponse.getVoucherCodeResponse().getSupplierConfigId();
        List<String> asList = Arrays.asList(voucherDetailsResponse.getVoucherCodeResponse().getVoucherCode());
        for (String code: asList)
        {
        VoucherCode voucherCode1 = getVoucherCode(supplierConfigId, code);
        supplierConfigurationRepository.deleteVoucherCode(supplierConfigId,voucherCode1);
        }
        resp.put("message","Record deleted Sucessfully");
        break;

        case FIXED:
        String configId = voucherDetailsResponse.getVoucherCodeResponse().getSupplierConfigId();
        String voucherCode = voucherDetailsResponse.getVoucherCodeResponse().getVoucherCode();
        VoucherCode voucherCode1 = getVoucherCode(configId, voucherCode);
        supplierConfigurationRepository.deleteVoucherCode(configId,voucherCode1);
        resp.put("message","Record deleted Sucessfully");
        break;
        }
        break;
        case BROWSE_FILE:
        switch (voucherCodeUsageType)
        {
        case MULTIPLE:
        break;

        case FIXED:
        String fileId = voucherDetailsResponse.getVoucherCodeResponse().getFlieId();
        List<VoucherCode> voucherCode = voucherCodeRepository.getVoucherCode(fileId);
        if (voucherCode!=null && voucherCode.size()>0)
        {
        for (VoucherCode code : voucherCode)
        {
        supplierConfigurationRepository.deleteVoucherCode(code.getId(),code);
        resp.put("message","Record deleted Sucessfully");

        }
        }
        else{
        throw new OperationException("No Voucher Code found with file Id "+fileId);
        }

        break;
        }
        break;
        case SUPPLIER_URL:
        break;

        }

        }
        catch (OperationException e)
        {
        new OperationException("There is some technical issue while deleting data");
        }
        return resp;
        }

public Set<VoucherCode> getVoucherCode(Set<VoucherCodeResource> resources)
        {
        Set<VoucherCode> voucherCodes = new HashSet<>();
        for (VoucherCodeResource codeResource : resources)
        {
        VoucherCode code = new VoucherCode();

        }
        return null;
        }

@Override
public Map updateVoucherCode(UploadVouchersResource uploadVouchersResource) throws OperationException {
        logger.info("Entered SupplierConfigurationServiceImpl :: updateVouchers() method");
        OpsUser loggedInUser = userService.getLoggedInUser();
        Map map = new HashMap();
        String supplierConfigId = uploadVouchersResource.getSupplierConfigId();
        SupplierVoucherCodes supplierVoucherCodes = supplierConfigurationRepository.get(supplierConfigId);

        if (!supplierVoucherCodes.isEnabled() && StringUtils.isEmpty(supplierVoucherCodes.getUserId()))
        {
        SupplierConfigurationResource resource = new SupplierConfigurationResource();
        CopyUtils.copy(supplierVoucherCodes, resource);
        Set<VoucherCodeResource> voucherCodes = new HashSet<>();
        Set<VoucherCode> voucherCodeSet = supplierVoucherCodes.getVoucherCodes();
        for (VoucherCode vc: voucherCodeSet)
        {
        VoucherCodeResource voucherCodeResource = new VoucherCodeResource();
        if (FileType.BROWSE_FILE.equals(vc.getFileType()) && VoucherCodeUsageType.MULTIPLE.equals(vc.getVoucherCodeUsageType()))
        {
        voucherCodeResource.setFileId(vc.getFileId());
        voucherCodeResource.setFileName("");
        }
        voucherCodeResource.setFileType(vc.getFileType());
        voucherCodeResource.setVoucherCodeUsageType(vc.getVoucherCodeUsageType());
        voucherCodeResource.setInputVoucherCodes(Collections.singleton(vc.getVoucherCode()));
        voucherCodeResource.setSupplierConfigId(supplierConfigId);
        voucherCodes.add(voucherCodeResource);
        }

        VoucherCodeResource voucherCodeResource = new VoucherCodeResource();
        CopyUtils.copy(uploadVouchersResource, voucherCodeResource);
        voucherCodes.add(voucherCodeResource);
        resource.setVoucherCodes(voucherCodes);
        String workflow = workflowService.saveWorkflow(resource, WorkflowOperation.SUBMIT, entityType, loggedInUser.getUserID());
        JSONObject workflowResp = new JSONObject(workflow);
        String id = workflowResp.optString("_id");
        Object newDoc = workflowResp.getJSONObject("doc").getJSONObject("newDoc");
        SupplierVoucherConfigSearchResponse response = new SupplierVoucherConfigSearchResponse();
        SupplierConfigurationResource supplierConfigurationResource = objectMapper.convertValue(newDoc, SupplierConfigurationResource.class);
        response.setId(supplierConfigurationResource.getId());
        response.setSupplierId(supplierConfigurationResource.getSupplierId());
        response.setSupplierName(supplierConfigurationResource.getSupplierName());
        response.setProductName(supplierConfigurationResource.getProductName());
        response.setProductCategoryName(supplierConfigurationResource.getProductCategoryName());
        response.setProductSubCategoryName(supplierConfigurationResource.getProductSubCategoryName());
        response.setVoucherToBeAppliedOn(supplierConfigurationResource.getVoucherToBeAppliedOn());
        response.setVoucherCodeUsageType(supplierConfigurationResource.getVoucherCodeUsageType());
        response.setPaymentStatusToReleaseVoucher(supplierConfigurationResource.getPaymentStatusToReleaseVoucher());
        response.setUnitOfMeasurement(supplierConfigurationResource.getUnitOfMeasurement());
        response.setNoOfDaysToReleaseVoucher(supplierConfigurationResource.getNoOfDaysToReleaseVoucher());
        response.setNoOfDaysToSendAlarm(supplierConfigurationResource.getNoOfDaysToSendAlarm());
        response.setMultiplier(supplierConfigurationResource.getMultiplier());
        response.setCustomerTemplateId(supplierConfigurationResource.getCustomerTemplateId());
        response.setSupplierTemplateId(supplierConfigurationResource.getSupplierTemplateId());
        response.setCompanyId(supplierConfigurationResource.getCompanyId());
        List<VoucherDetailsResponse> voucherDetailsResponseList = new ArrayList<>();
        Set<VoucherCodeResource> voucherCodeResourceSet = supplierConfigurationResource.getVoucherCodes();
        if (voucherCodes!=null && voucherCodes.size()>0)
        {
        for (VoucherCodeResource codeResource : voucherCodes)
        {
        VoucherDetailsResponse voucherDetailsResponse = new VoucherDetailsResponse();
        VoucherCodeResponse voucherCodeResponse = new VoucherCodeResponse();
        voucherCodeResponse.setVoucherCodeStatus(VoucherCodeStatus.UNASSIGNED);
        FileType fileType = codeResource.getFileType();
        VoucherCodeUsageType voucherCodeUsageType = codeResource.getVoucherCodeUsageType();
        if (FileType.INPUT_CODE.equals(fileType) && VoucherCodeUsageType.MULTIPLE.equals(voucherCodeUsageType) ||
        FileType.INPUT_CODE.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType))
        {
        Set<String> inputVoucherCodes = codeResource.getInputVoucherCodes();
        inputVoucherCodes.stream().map(String::valueOf).collect(Collectors.toList());
        String code  = String.join(",",inputVoucherCodes);
        voucherCodeResponse.setVoucherCode(code);
        }
        else if (FileType.BROWSE_FILE.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType))
        {
        voucherCodeResponse.setFlieId(codeResource.getFileId());
        voucherCodeResponse.setVoucherCode(codeResource.getFileName());
        }
        else if (FileType.SUPPLIER_URL.equals(fileType) && VoucherCodeUsageType.FIXED.equals(voucherCodeUsageType))
        {

        }
        voucherCodeResponse.setVoucherCodeUsageType(voucherCodeUsageType);
        voucherCodeResponse.setFileType(fileType);
        voucherCodeResponse.setSupplierConfigId(supplierConfigurationResource.getId());
        voucherCodeResponse.setDateOfUpload(ZonedDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now())));
        voucherDetailsResponse.setVoucherCodeResponse(voucherCodeResponse);
        voucherDetailsResponseList.add(voucherDetailsResponse);
        }
        }
        response.setVoucherDetailsResponseList(voucherDetailsResponseList);
        workflowResp.getJSONObject("doc").put("newDoc",new JSONObject(response));
        if (!StringUtils.isEmpty(id)) {
        map.put("Success", "Record submitted sucessfully with workflow id " + id);
        map.put("Data", workflowResp);
        return map;
        } else {
        throw new OperationException("Data not saved");
        }
        } else {
        throw new OperationException("Already locked by user " + supplierVoucherCodes.getUserId());
        }
        }


@Override
public boolean isResponsibleFor(String type) throws OperationException
        {
        return type.equalsIgnoreCase(entityType);
        }
        }
