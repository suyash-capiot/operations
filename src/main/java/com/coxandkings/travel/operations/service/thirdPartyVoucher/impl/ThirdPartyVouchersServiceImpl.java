package com.coxandkings.travel.operations.service.thirdPartyVoucher.impl;

import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.SupplierConfigSearchCriteria;
import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsOrderStatus;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2BClient;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2CClient;
import com.coxandkings.travel.operations.model.template.request.TemplateInfo;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.SupplierVoucherCodes;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.VoucherCode;
import com.coxandkings.travel.operations.repository.thirdPartyVoucher.SupplierConfigurationRepository;
import com.coxandkings.travel.operations.repository.thirdPartyVoucher.VoucherCodeRepository;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.email.EmailUsingTemplateResource;
import com.coxandkings.travel.operations.resource.email.FileAttachmentResource;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.AssignVouchersToBooking;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.SearchVouchersToSend;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.documentLibrary.DocumentLibraryService;
import com.coxandkings.travel.operations.service.email.EmailService;
import com.coxandkings.travel.operations.service.productbookedthrother.mdm.MdmClientService;
import com.coxandkings.travel.operations.service.template.TemplateLoaderService;
import com.coxandkings.travel.operations.service.thirdPartyVoucher.SupplierConfigurationService;
import com.coxandkings.travel.operations.service.thirdPartyVoucher.ThirdPartyVouchersService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.supplier.CommunicationType;
import com.coxandkings.travel.operations.utils.supplier.SupplierDetailsService;
import com.coxandkings.travel.operations.utils.thirdpartyvouchers.SendVoucherToClient;
import com.itextpdf.text.DocumentException;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ThirdPartyVouchersServiceImpl implements ThirdPartyVouchersService {

    @Autowired
    private SendVoucherToClient sendVoucherToClient;

    @Autowired
    private VoucherCodeRepository voucherCodeRepository;

    @Autowired
    private SupplierConfigurationRepository supplierConfigurationRepository;

    @Value(value = "${third-party-vouchers.voucher-file}")
    private String voucherFilepath;

    @Value(value = "${booking_engine.assign_vouchers}")
    private String updateVouchersUrl;


    @Value(value = "${communication.email.from_address}")
    private String emailAddress;

    @Value(value = "${communication.email.api}")
    private String emailUrl;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private SupplierDetailsService supplierDetailsService;

    @Autowired
    private MdmClientService mdmClientService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DocumentLibraryService documentLibraryService;

    @Autowired
    private SupplierConfigurationService supplierConfigurationService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private TemplateLoaderService templateLoaderService;
    @Autowired
    private UserService userService;

    private static Logger logger = Logger.getLogger(SupplierConfigurationServiceImpl.class);

    @Override
    public String assignVouchersToBooking(SupplierVoucherCodes supplierVoucherCodes, OpsProduct opsProduct, List<String> voucherIds) throws OperationException {
        logger.info("Entered ThirdPartyVouchersServiceImpl :: assignVouchersToBooking() method");
        supplierConfigurationRepository.add(supplierVoucherCodes);
        List<String> vouchersAvailable = opsProduct.getVouchers();
        vouchersAvailable.addAll(voucherIds);
        AssignVouchersToBooking assignVouchersToBooking = new AssignVouchersToBooking();
        assignVouchersToBooking.setOrderID(opsProduct.getOrderID());
        assignVouchersToBooking.setProductSubCategory(opsProduct.getProductSubCategory());
        assignVouchersToBooking.setUserID(userService.getSystemUserIdFromMDMToken());
        assignVouchersToBooking.setVoucherIDs(vouchersAvailable);
        HttpEntity httpEntity = new HttpEntity(assignVouchersToBooking);
        ResponseEntity<String> vouchersAssigned = null;
        try {
            vouchersAssigned = restUtils.exchange(updateVouchersUrl, HttpMethod.PUT, httpEntity, String.class);
        } catch (Exception e) {
            logger.info("Error in updating booking with vouchers ");
            throw new OperationException("Cannot update booking with vouchers");
        }
        return vouchersAssigned.getBody();
    }

    @Override
    public List<SupplierVoucherCodes> searchToAssignVouchers(OpsProduct opsProduct) {
        logger.info("Entered ThirdPartyVoucherServiceImpl :: searchToAssignVouchers() method");
        SupplierConfigSearchCriteria supplierConfigSearchCriteria = new SupplierConfigSearchCriteria();
        supplierConfigSearchCriteria.setSupplierName(opsProduct.getSupplierID());
        supplierConfigSearchCriteria.setProductName(opsProduct.getProductName());
        if (opsProduct.getOpsProductSubCategory() != null)
            supplierConfigSearchCriteria.setProductSubCategoryName(opsProduct.getOpsProductSubCategory().getSubCategory());
        return supplierConfigurationRepository.searchToAssignVouchers(supplierConfigSearchCriteria);

    }

    @Override
    public List<VoucherCode> vouchersAvailable(String supplierConfigId) {
        logger.info("Entered ThirdPartyVoucherServiceImpl :: voucherAvailable() method");
        return voucherCodeRepository.unassignedVouchers(supplierConfigId);
    }

    @Override
    public EmailResponse sendEmailToClient(OpsBooking opsBooking, String orderId, FileAttachmentResource fileAttachmentResource, String templateId, String productName) throws OperationException {
        logger.info("Entered ThirdPartyVoucherServiceImpl :: sendEmailToClient() method");
        EmailUsingTemplateResource emailUsingTemplateResource = sendEmailUsingTemplateId(templateId);
        List<FileAttachmentResource> fileAttachmentResourceList = new ArrayList<>();
        fileAttachmentResourceList.add(fileAttachmentResource);
        emailUsingTemplateResource.setFileAttachments(fileAttachmentResourceList);
        Map<String, String> dynamicVariables = new HashMap<>();
        if (opsBooking.getClientType().equalsIgnoreCase("B2B")) {
            B2BClient b2BClient = null;
            try {
                b2BClient = mdmClientService.getB2bClient(opsBooking.getClientID());

            } catch (OperationException e) {
                e.printStackTrace();
            }
            emailUsingTemplateResource.setToMail((b2BClient.getEmail()));
            emailUsingTemplateResource.setBookId(opsBooking.getBookID());
            dynamicVariables.put("First Name", b2BClient.getFirstName());
            dynamicVariables.put("Booking_Ref_Id", opsBooking.getBookID());
            dynamicVariables.put("Order_Id", orderId);
            dynamicVariables.put("Product Name", productName);
            emailUsingTemplateResource.setDynamicVariables(dynamicVariables);
        } else if (opsBooking.getClientType().equalsIgnoreCase("B2C")) {
            B2CClient b2CClient = new B2CClient();
            try {
                b2CClient = mdmClientService.getB2cClient(opsBooking.getClientID());
            } catch (OperationException e) {
                logger.debug("Error to get details of client");
                e.printStackTrace();
            }
            emailUsingTemplateResource.setToMail(b2CClient.getEmail());
            emailUsingTemplateResource.setBookId(opsBooking.getBookID());
            dynamicVariables.put("First Name", b2CClient.getFirstName());
            dynamicVariables.put("Booking_Ref_Id", opsBooking.getBookID());
            dynamicVariables.put("Order_Id", orderId);
            dynamicVariables.put("Product Name", productName);
            emailUsingTemplateResource.setDynamicVariables(dynamicVariables);
        }
        EmailResponse emailResponse = null;
        try {
            emailResponse = emailService.sendEmail(emailUsingTemplateResource);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OperationException e) {
            logger.debug("Error in sending email");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emailResponse;
    }

    @Override
    public EmailResponse sendEmailToSupplier(String bookId, String orderId, String productName, String templateId, String noOfVouchers) throws OperationException {
        logger.info("Entered ThirdPartyVoucherServiceImpl :: sendEmailToSupplier() method");
        EmailResponse emailResponse = new EmailResponse();
        EmailUsingTemplateResource emailUsingTemplateResource = null;
        Map<String, String> dynamicVariables = new HashMap<>();
        try {
            emailUsingTemplateResource = sendEmailUsingTemplateId(templateId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        emailUsingTemplateResource.setFromMail(emailAddress);

        OpsProduct opsProduct = opsBookingService.getProduct(bookId, orderId);
        String supplierId = opsProduct.getSupplierID();
        String supplierName = null;
        CommunicationType communicationType = supplierDetailsService.getSupplierCommunicationTypeBySupplierId(supplierId);
        if (communicationType == null) {
            throw new OperationException(Constants.ER01);
        } else {
            switch (communicationType) {
                case EMAIL:
                    String jsonSupplier = supplierDetailsService.getSupplierDetails(supplierId);
                    String emailId = String.valueOf(jsonObjectProvider.getChildObject(jsonSupplier, "$.contactInfo.contactDetails.email", String.class));
                    supplierName = String.valueOf(jsonObjectProvider.getChildObject(jsonSupplier, "$.supplier.name", String.class));
                    emailUsingTemplateResource.setToMail(Collections.singletonList(emailId));
                    break;

                case FAX:
                    throw new OperationException(Constants.ER45 + opsProduct.getEnamblerSupplierName());

                case PHONE:
                    throw new OperationException(Constants.ER45 + opsProduct.getEnamblerSupplierName());
            }
        }

        emailUsingTemplateResource.setPriority(EmailPriority.HIGH);
        dynamicVariables.put("First Name", supplierName);
        dynamicVariables.put("Product Name", productName);
        dynamicVariables.put("NumberOfAvailableVouchers", noOfVouchers);
        emailUsingTemplateResource.setDynamicVariables(dynamicVariables);
        try {
            emailResponse = emailService.sendEmail(emailUsingTemplateResource);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OperationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return emailResponse;
    }


    public EmailUsingTemplateResource sendEmailUsingTemplateId(String templateId) throws OperationException {
        logger.info("Entered ThirdPartyVoucherServiceImpl :: sendEmailUsingTemplateId() method");
        Map map = templateLoaderService.getTemplateInfoById(templateId);
        TemplateInfo templateInfo = (TemplateInfo) map.get("templateInfo");

        templateInfo.setIsActive(true);
        templateInfo.setGroupOfCompanies("");
        templateInfo.setGroupCompany("");
        templateInfo.setCompanyName("");
        templateInfo.setBusinessUnit("");
        templateInfo.setSubBusinessUnit("");
        templateInfo.setMarket("");
        templateInfo.setOffice("");

        templateInfo.setSource("");
        templateInfo.setProductCategory("");
        templateInfo.setProductCategorySubType("");
        templateInfo.setRule1("");
        templateInfo.setRule2("");
        templateInfo.setRule3("");
        templateInfo.setCommunicationType("");
        templateInfo.setCommunicateTo("");
        templateInfo.setIncomingCommunicationType("");
        templateInfo.setDestination("");
        templateInfo.setBrochure("");
        templateInfo.setTour("");
        EmailUsingTemplateResource emailUsingTemplateResource = new EmailUsingTemplateResource();
        emailUsingTemplateResource.setFromMail(emailAddress);
        emailUsingTemplateResource.setPriority(EmailPriority.HIGH);
        emailUsingTemplateResource.setTemplateInfo(templateInfo);
        emailUsingTemplateResource.setSubject((String) map.get("subject"));
        emailUsingTemplateResource.setFunction(templateInfo.getFunction());
        emailUsingTemplateResource.setScenario(templateInfo.getScenario());
        emailUsingTemplateResource.setProcess(templateInfo.getProcess());

        return emailUsingTemplateResource;

    }

    @Override
    public void getThirdPartyVoucher() {
        System.out.println("third party vouchers batch job sending mails");
        logger.info("Entered ThirdpartyVoucherServiceImpl :: getThirdPartyVoucher() method");
        ZonedDateTime date = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS);
        List<VoucherCode> getResultsforReleaseDate = voucherCodeRepository.getReleaseDates(date);
        for (VoucherCode voucherCode : getResultsforReleaseDate) {
            if (!voucherCode.getVoucherCodeStatus().getValue().equalsIgnoreCase("Sent")) {

                SearchVouchersToSend searchVouchersToSend = new SearchVouchersToSend();
                searchVouchersToSend.setBookId(voucherCode.getBookId());
                searchVouchersToSend.setOrderId(voucherCode.getOrderId());
                searchVouchersToSend.setSupplierConfigId(voucherCode.getSupplierVoucherCodes().getId());
                searchVouchersToSend.setVoucherCodeStatus(VoucherCodeStatus.ASSIGNED);
                List<VoucherCode> searchToSendVouchers = voucherCodeRepository.searchToSendVouchers(searchVouchersToSend);
                String concatedVoucher = new String();
                if (!searchToSendVouchers.isEmpty()) {
                    logger.info("Entered ThirdpartyVoucherServiceImpl :: vouchers avaiable");
                    for (VoucherCode v : searchToSendVouchers) {
                        concatedVoucher = concatedVoucher + v.getVoucherCode();
                    }
                    FileAttachmentResource fileAttachmentResource = new FileAttachmentResource();
                    try {
                        fileAttachmentResource = sendVoucherToClient.generateBarCodes(concatedVoucher);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                    System.out.println("conct: " + concatedVoucher);

                    //todo call barcode functionality
                    try {
                        OpsBooking opsBooking = opsBookingService.getBooking(voucherCode.getBookId());
                        String supplierConfigId = voucherCode.getSupplierVoucherCodes().getId();
                        SupplierVoucherCodes supplierVoucherCodes = supplierConfigurationRepository.get(supplierConfigId);
                        if (opsBooking.getProducts().stream()
                                .filter(opsProduct -> opsProduct.getOrderID().equalsIgnoreCase(voucherCode.getOrderId()))
                                .findFirst().get().getOrderDetails().getOpsOrderStatus().getProductStatus().equals(OpsOrderStatus.valueOf("OK").getProductStatus())) {
                            EmailResponse emailResponse = this.sendEmailToClient(opsBooking, voucherCode.getOrderId(), fileAttachmentResource
                                    , supplierVoucherCodes.getCustomerTemplateId(), supplierVoucherCodes.getProductSubCategoryName());
                            if (emailResponse != null && emailResponse.getStatus().equalsIgnoreCase("SUCCESS")) {
                                for (VoucherCode v : searchToSendVouchers) {
                                    v.setVoucherCodeStatus(VoucherCodeStatus.SENT);
                                    v.setLastModifiedDate(ZonedDateTime.now());
                                }
                                voucherCodeRepository.update(searchToSendVouchers);
                            } else {
                                logger.debug("Error in Sending Mail ");
                                throw new OperationException("Error in Sending Mail ");
                            }
                        }
                    } catch (OperationException e) {
                        logger.info("Error to get booking using getBookId ");
                    }
                }
            }
        }
    }

    @Override
    public void updateVouchersWithReleaseDate(List<VoucherCode> voucherCodes) {
        logger.info("Entered ThirdPartyVoucherServiceImpl :: updateVouchersWithReleaseDate() method");
        voucherCodeRepository.update(voucherCodes);
    }
}
