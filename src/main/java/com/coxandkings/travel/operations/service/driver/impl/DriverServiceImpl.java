package com.coxandkings.travel.operations.service.driver.impl;


import com.coxandkings.travel.operations.criteria.driver.DriverCriteria;
import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.enums.driver.DriverStatus;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsBookingStatus;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.driver.Driver;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2BClient;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2CClient;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.driver.DriverRepository;
import com.coxandkings.travel.operations.repository.driver.VehicleCategoryRepository;
import com.coxandkings.travel.operations.resource.driver.DriverResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.driver.DriverService;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.productbookedthrother.mdm.MdmClientService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.systemlogin.MDMDataSource;
import com.coxandkings.travel.operations.utils.*;
import com.coxandkings.travel.operations.utils.email.EmailResourceService;
import com.coxandkings.travel.operations.utils.supplier.SupplierDetailsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.*;

//import com.coxandkings.travel.operations.repository.driver.DriverStatusRepository;


@Service("driverService")
public class DriverServiceImpl implements DriverService {

    private static Logger log = LogManager.getLogger(DriverServiceImpl.class);

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private VehicleCategoryRepository vehicleCategoryRepository;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private ClientMasterDataService clientMasterDataService;

    @Autowired
    private EmailResourceService emailResourceService;

    @Value("${communication.email.api}")
    private String sendEmailWithTemplateUrl;

    @Value("${communication.email.process}")
    private String process;

    @Value("${add_driver_details.template_config.function}")
    private String function;

    //Client
    @Value("${add_driver_details.template_config.client.scenario}")
    private String clientScenario;

    @Value("${add_driver_details.template_config.client.subject}")
    private String clientSubject;

    @Value("${add_driver_details.dynamic_variables.client.first_name}")
    private String clientFirstName;

    @Value("${add_driver_details.dynamic_variables.client.trip}")
    private String clientTrip;

    @Value("${add_driver_details.dynamic_variables.client.booking_id}")
    private String clientBookId;


    //Supplier

    @Value("${add_driver_details.template_config.supplier.scenario}")
    private String supplierScenario;

    @Value("${add_driver_details.template_config.supplier.subject}")
    private String supplierSubject;

    @Value("${add_driver_details.dynamic_variables.supplier.product_code}")
    private String supplierProductCode;

    @Value("${add_driver_details.dynamic_variables.supplier.booking_ref_id}")
    private String supplierBookId;

    @Value("${add_driver_details.dynamic_variables.supplier.fromDestination}")
    private String supplierFromDestination;

    @Value("${add_driver_details.dynamic_variables.supplier.toDestination}")
    private String supplierToDestination;

    @Value("${add_driver_details.links.supplier}")
    private String supplierEmailLink;

    @Autowired
    private MdmClientService mdmClientService;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    @Qualifier(value = "mDMDataSource")
    private MDMDataSource mdmDataSource;

    @Autowired
    private SupplierDetailsService supplierDetailsService;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private EmailUtils emailUtils;


    @Autowired
    private UserService userService;

    @Autowired
    private ToDoTaskService toDoTaskService;




    @Override
    public Driver getDriverById(String id) {
        return driverRepository.getDriverById(id);
    }


    @Override
    public List<Driver> getDriverByCriteria(DriverCriteria criteria) throws OperationException {
        List<Driver> drivers = driverRepository.getDriverByCriteria(criteria);

        if (drivers == null)
            throw new OperationException(Constants.ER01);
        else
            return drivers;
    }

    @Override
    public List<String> getDriverStatus() {
        List<String> driverStatus = new ArrayList<>();
        for (DriverStatus status : DriverStatus.values()) {
            driverStatus.add(status.getvalue());
        }
        return driverStatus;
    }

    @Override
    public void updateProductForAction(OpsBooking opsBooking) {
        if (opsBooking.getStatus().equals(OpsBookingStatus.CNF.toString())) {
//            1.
//            OpsProductCategory =PRODUCT_CATEGORY_TRANSPORTATION( "transportation" ),
//            productCategorySubType =  car,
//            Category =  Transfer,
//            Transfer DiscrepancyType = private

//            2.
//            OpsProductCategory =PRODUCT_CATEGORY_TRANSPORTATION( "transportation" ),
//            productCategorySubType =  car,
//            category = rental,
//            with chauffer = yes

            //        product.getProductLevelActions( ).put( ProductActions.ADD_DRIVER_DETAILS.getActionType( ) , true );
        } else {
            //        product.getProductLevelActions( ).put( ProductActions.ADD_DRIVER_DETAILS.getActionType( ) , false );
        }

    }

    @Override
    public DriverResource loadDriverDetails(String bookingRefId, String orderId) throws OperationException {
        DriverResource driverResource = new DriverResource();
        DriverCriteria driverCriteria = new DriverCriteria();
        driverCriteria.setBookingRefId(bookingRefId);
        driverCriteria.setProductId(orderId);
        List<Driver> driver = driverRepository.getDriverByCriteria(driverCriteria);
        if (driver != null && driver.size() > 0 && driver.size() <= 1) {
            Driver driver1 = driver.get(0);
            boolean detailsPresent = isDetailsPresent(driver1.getId());
            if (detailsPresent) {
                CopyUtils.copy(driver1, driverResource);
                String getStatusValue = driver1.getDriverStatusType().getvalue();
                driverResource.setDriverStatus(getStatusValue);
                driverResource.setStatus(getDriverStatus());

            } else {
                CopyUtils.copy(driver1, driverResource);
                driverResource.setStatus(getDriverStatus());
            }
        } else {
            driverResource.setStatus(getDriverStatus());
        }
        return driverResource;
    }

    @Override
    public Driver saveDriver(DriverResource driverResource) throws OperationException {

        Driver driver = new Driver();

        // checking duplicate
        DriverCriteria driverCriteria = new DriverCriteria();
        driverCriteria.setBookingRefId(driverResource.getBookingRefId());
        driverCriteria.setProductId(driverResource.getProductId());
        List<Driver> driverByCriteria = driverRepository.getDriverByCriteria(driverCriteria);

        if (driverByCriteria != null) {
            if (driverByCriteria.size() > 0) {
                //TODO: throw exception driver found based on given id
                throw new OperationException(Constants.ER02);
            }
        }

        if (driverResource.getDriverStatus() != null) {
            String status = driverResource.getDriverStatus();
            driver.setDriverStatusType(DriverStatus.fromStringToEnum(status));
        }
        CopyUtils.copy(driverResource, driver);
        driver = driverRepository.saveDriver(driver);
        EmailResponse emailResponse = sendDriverDetailsToCustomer(driverResource);
        if (!("SUCCESS".equalsIgnoreCase(emailResponse.getStatus()))) {
            log.error("Email is not sent to Customer for booking id : " + driverResource.getBookingRefId() + " and Order Id " + driverResource.getProductId());
        }
        return driver;
    }

    @Override
    public Driver updateDriver(DriverResource driverResource) throws OperationException {
        Driver driver = new Driver();
        if (!StringUtils.isEmpty(driverResource.getId())) {
            if (log.isDebugEnabled()) {
                log.debug("driver Id:" + driverResource.getId());
            }
            Driver existingDriver = driverRepository.getDriverById(driverResource.getId());
            if (log.isDebugEnabled()) {
                log.debug("Driver Details:" + existingDriver);
            }
            if (existingDriver == null) {
                //TODO: throw exception driver not found based on given id
                throw new OperationException(Constants.ER01);
            }

            if (driverResource.getDriverStatus() != null) {
                String status = driverResource.getDriverStatus();
                existingDriver.setDriverStatusType(DriverStatus.fromStringToEnum(status));
            }

            CopyUtils.copy(driverResource, existingDriver);
            driver = existingDriver;
            driver = driverRepository.updateDriver(driver);
//            String extranetTodoId = driver.getExtranetTodoId();
//            if (!StringUtils.isEmpty(extranetTodoId))
//            {
//
//            }

            EmailResponse emailResponse = sendDriverDetailsToCustomer(driverResource);
            if (!("SUCCESS".equalsIgnoreCase(emailResponse.getStatus()))) {
                log.error("Email is not sent to Customer for booking id : " + driverResource.getBookingRefId() + " and Order Id " + driverResource.getProductId());
            }
        }
        return driver;
    }

    @Override
    public EmailResponse sendDriverDetailsToCustomer(DriverResource driverResource) throws OperationException {
        String clientEmailId = null;
        String clientId = null;
        String clientType = null;
        Map<String, String> dynamicVariables = new HashMap<>();
        EmailResponse emailResponse = null;

        String bookingId = driverResource.getBookingRefId();
        String orderId = driverResource.getProductId();
        OpsBooking opsBooking = opsBookingService.getBooking(bookingId);
        clientId = opsBooking.getClientID();
        if (clientId != null || !clientId.isEmpty()) {
            clientType = opsBooking.getClientType();
            clientEmailId = clientMasterDataService.getClientEmailId(clientId, MDMClientType.fromString(clientType));
            if (MDMClientType.B2B.equals(MDMClientType.fromString(clientType))) {
                Map<String, String> b2BClientNames = clientMasterDataService.getB2BClientNames(Collections.singletonList(clientId));
                if (b2BClientNames != null && b2BClientNames.size() > 0) {
                    for (Map.Entry<String, String> entry : b2BClientNames.entrySet()) {
                        dynamicVariables.put(clientFirstName, entry.getValue());
                    }
                }
            } else if (MDMClientType.B2C.equals(MDMClientType.fromString(clientType))) {
                Map<String, String> b2CClientNames = clientMasterDataService.getB2CClientNames(Collections.singletonList(clientId));
                if (b2CClientNames != null && b2CClientNames.size() > 0) {
                    for (Map.Entry<String, String> entry : b2CClientNames.entrySet()) {
                        dynamicVariables.put(clientFirstName, entry.getValue());

                    }
                }
            } else {
                log.info("No client name found ");
            }

        } else {
            log.info("No client name found ");
        }
        dynamicVariables.put(clientTrip, "Trip Mumbai to Pune");//TODO: need to be change in future
        dynamicVariables.put(clientBookId, getHtml(driverResource));

        emailResponse = emailUtils.buildClientMail(function, clientScenario, clientEmailId, clientSubject, dynamicVariables, null, null);
        return emailResponse;
    }

    @Override
    public EmailResponse sendDriverDetailsToSupplier(String bookingRefId, String orderId, boolean automaticTrigger) throws OperationException {
        EmailResponse emailResponse = null;
        OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBookingService.getBooking(bookingRefId), orderId);
        ToDoTask toDoTaskForSupplierExtranet = null;
        ToDoTask toDoTaskForOpsUser = null;
        Driver driver = null;
        String supplierID = opsProduct.getSupplierID();
        if (!StringUtils.isEmpty(supplierID)) {
            String jsonSupplier = supplierDetailsService.getSupplierDetails(supplierID);
            String extranetAccess = jsonObjectProvider.getAttributeValue(jsonSupplier, "$.extranetAccess", String.class);
            if (automaticTrigger) {
                if ("true".equalsIgnoreCase(extranetAccess)) {
                    DriverCriteria criteria = new DriverCriteria();
                    criteria.setBookingRefId(bookingRefId);
                    criteria.setProductId(orderId);
                    List<Driver> driverByCriteria = driverRepository.getDriverByCriteria(criteria);
                    if (driverByCriteria != null && driverByCriteria.size() > 0) {
                        Driver existingDriver = driverByCriteria.get(0);
                        if (isDetailsPresent(existingDriver.getId())) {
                            throw new OperationException("Driver detais already present");
                        } else {
                            driver = new Driver();
                            CopyUtils.copy(existingDriver, driver);
                            try {
                                toDoTaskForSupplierExtranet = toDoTaskService.save(getTodoForSupplierExtranet(driver));
                                toDoTaskForOpsUser = toDoTaskService.save(getTodoForUser(driver));
                                driver.setExtranetTodoId(toDoTaskForSupplierExtranet.getId());
                                driver.setUserTodoId(toDoTaskForOpsUser.getId());
                                driver = driverRepository.saveDriver(driver);
                                log.info("todo task id " + toDoTaskForSupplierExtranet.getId());
                            } catch (OperationException | IllegalAccessException | ParseException | IOException | InvocationTargetException e) {
                                log.debug("Error occured in saving todo task");
                            }

                        }
                    } else {
                        driver = new Driver();
                        driver.setBookingRefId(bookingRefId);
                        driver.setProductId(orderId);
                        driver = driverRepository.saveDriver(driver);
                        try {
                            toDoTaskForSupplierExtranet = toDoTaskService.save(getTodoForSupplierExtranet(driver));
                            toDoTaskForOpsUser = toDoTaskService.save(getTodoForUser(driver));
                            driver.setExtranetTodoId(toDoTaskForSupplierExtranet.getId());
                            driver.setUserTodoId(toDoTaskForOpsUser.getId());
                            driver = driverRepository.saveDriver(driver);
                            log.info("todo task id " + toDoTaskForSupplierExtranet.getId());
                        } catch (OperationException | IllegalAccessException | ParseException | IOException | InvocationTargetException e) {
                            log.debug("Error occured in saving todo task");
                        }


                    }
                } else {
                    emailResponse = sentEmail(bookingRefId, orderId, jsonSupplier);
                }
            } else {
                emailResponse = sentEmail(bookingRefId, orderId, jsonSupplier);
            }
        }
        return emailResponse;
    }

    @Override
    public boolean isDetailsPresent(String id) {
        boolean flag = false;
        Driver driver = driverRepository.getDriverById(id);
        if (driver != null) {
            if (!StringUtils.isEmpty(driver.getVehicleCategoryId()) && !StringUtils.isEmpty(driver.getVehicleName()) && !StringUtils.isEmpty(driver.getVehicleNumber()) &&
                    !StringUtils.isEmpty(driver.getDriverName()) && !StringUtils.isEmpty(driver.getDriverStatusType()) && !StringUtils.isEmpty(driver.getMobileNumber())) {
                flag = true;
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    @Override
    public Driver updateDriverBySupplier(DriverResource driverResource) throws OperationException {
        Driver driver = new Driver();
        if (!StringUtils.isEmpty(driverResource.getId())) {
            if (log.isDebugEnabled()) {
                log.debug("driver Id:" + driverResource.getId());
            }
            Driver existingDriver = driverRepository.getDriverById(driverResource.getId());
            if (log.isDebugEnabled()) {
                log.debug("Driver Details:" + existingDriver);
            }
            if (existingDriver == null) {
                //TODO: throw exception driver not found based on given id
                throw new OperationException(Constants.ER01);
            }

            if (driverResource.getDriverStatus() != null) {
                String status = driverResource.getDriverStatus();
                existingDriver.setDriverStatusType(DriverStatus.fromStringToEnum(status));
            }
            String bookingRefId = existingDriver.getBookingRefId();
            String productId = existingDriver.getProductId();
            CopyUtils.copy(driverResource, existingDriver);
            existingDriver.setBookingRefId(bookingRefId);
            existingDriver.setProductId(productId);
            driver = existingDriver;
            driver = driverRepository.updateDriver(driver);
            String extranetTodoId = driver.getExtranetTodoId();
            if (!StringUtils.isEmpty(extranetTodoId)) {
                try {
                    ToDoTask existingToDoTask = toDoTaskService.getById(extranetTodoId);
                    if (existingToDoTask != null) {
                        toDoTaskService.updateToDoTaskStatus(existingDriver.getId(), ToDoTaskSubTypeValues.ADD_DRIVER_DETAILS, ToDoTaskStatusValues.CLOSED);
                    }
                } catch (IOException e) {
                    log.error("Error while getting TODO task for Extranet Supplier. ToDo Task is : " + extranetTodoId);
                }
            }
            String userTodo = driver.getUserTodoId();
            if (!StringUtils.isEmpty(userTodo)) {
                try {
                    ToDoTask userToDoTask = toDoTaskService.getById(userTodo);
                    if (userToDoTask != null) {
                        toDoTaskService.updateToDoTaskStatus(existingDriver.getId(), ToDoTaskSubTypeValues.ADD_DRIVER_DETAILS, ToDoTaskStatusValues.CLOSED);
                    }
                } catch (IOException e) {
                    log.error("Error while getting TODO task for Extranet Supplier. ToDo Task is : " + userTodo);
                }
            }

            driverResource.setBookingRefId(driver.getBookingRefId());
            driverResource.setProductId(driver.getProductId());
            EmailResponse emailResponse = sendDriverDetailsToCustomer(driverResource);
            if (!("SUCCESS".equalsIgnoreCase(emailResponse.getStatus()))) {
                log.error("Email is not sent to Customer for booking id : " + driverResource.getBookingRefId() + " and Order Id " + driverResource.getProductId());
            }

        }
        return driver;
    }

    private String getHtml(DriverResource driverResource) {
        String header = new String();
        header = "<table style= \"border: 1px solid black;border-collapse: collapse;\">\n" +
                "<tr>\n" +
                "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Driver Name</th>\n" +
                "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + driverResource.getDriverName() + "</td>\n" +
                "</tr>\n" +
                "\n" +
                "<tr>\n" +
                "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Contact Number</th>\n" +
                "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + driverResource.getMobileNumber() + "</td>\n" +
                "</tr>\n" +
                "\n" +
                "<tr>\n" +
                "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Vehicle Number</th>\n" +
                "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + driverResource.getVehicleNumber() + "</td>\n" +
                "</tr>\n" +
                "\n" +
                "<tr>\n" +
                "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Vehicle Name</th>\n" +
                "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + driverResource.getVehicleName() + "</td>\n" +
                "</tr>\n" +
                "\n" +
                "</table>";
        return header;
    }

    private EmailResponse sentEmail(String bookingRefId, String orderId, String jsonSupplier) throws OperationException {
        Map<String, String> dynamicVariables = new HashMap<>();
        EmailResponse emailResponse = null;
        Driver driver = null;
        DriverResource driverResource = null;

        DriverCriteria driverCriteria = new DriverCriteria();
        driverCriteria.setBookingRefId(bookingRefId);
        driverCriteria.setProductId(orderId);
        List<Driver> driverByCriteria = driverRepository.getDriverByCriteria(driverCriteria);

        if (driverByCriteria != null && driverByCriteria.size() > 0) {
            Driver existingDriver = driverByCriteria.get(0);
            boolean detailsPresent = isDetailsPresent(existingDriver.getId());
            if (detailsPresent) {
                throw new OperationException("Already Details of Driver is present for Booking Id " + bookingRefId + " and Order Id " + orderId);
            } else {
                driver = new Driver();
                CopyUtils.copy(existingDriver, driver);
                String emailId = String.valueOf((jsonObjectProvider.getChildObject(jsonSupplier, "$.contactInfo.contactDetails.email", String.class)));
                dynamicVariables.put(supplierBookId, bookingRefId);
                dynamicVariables.put(supplierProductCode, supplierEmailLink + "/" + driver.getId());
                dynamicVariables.put(supplierFromDestination, "Mumbai"); //TODO: take this from Booking
                dynamicVariables.put(supplierToDestination, "Pune"); //TODO: take this from Booking
                emailResponse = emailUtils.buildClientMail(function, supplierScenario, emailId, supplierSubject, dynamicVariables, null, null);
                if ("success".equalsIgnoreCase(emailResponse.getStatus())) {
                    driver.setLinkSent(true);
                } else {
                    driver.setLinkSent(false);
                }
                driver = driverRepository.updateDriver(driver);
            }
        } else {
            driver = new Driver();
            driver.setBookingRefId(bookingRefId);
            driver.setProductId(orderId);
            driver = driverRepository.saveDriver(driver);
            String emailId = String.valueOf((jsonObjectProvider.getChildObject(jsonSupplier, "$.contactInfo.contactDetails.email", String.class)));
            dynamicVariables.put(supplierBookId, bookingRefId);
            dynamicVariables.put(supplierProductCode, supplierEmailLink + "/" + driver.getId());
            dynamicVariables.put(supplierFromDestination, "Mumbai"); //TODO: take this from Booking
            dynamicVariables.put(supplierToDestination, "Pune"); //TODO: take this from Booking
            emailResponse = emailUtils.buildClientMail(function, supplierScenario, emailId, supplierSubject, dynamicVariables, null, null);
            if ("success".equalsIgnoreCase(emailResponse.getStatus())) {
                driver.setLinkSent(true);
            } else {
                driver.setLinkSent(false);
            }
            driver = driverRepository.updateDriver(driver);
//            driverResource = new DriverResource();
//            CopyUtils.copy(driver, driverResource);
        }
        return emailResponse;
    }

    public ToDoTaskResource getTodoForSupplierExtranet(Driver driver) throws OperationException {
        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        B2BClient b2BClient = new B2BClient();
        B2CClient b2CClient = new B2CClient();
        String bookingRefId = driver.getBookingRefId();
        String productId = driver.getProductId();
        String userID = userService.getLoggedInUserId();
        OpsBooking opsBooking = opsBookingService.getBooking(bookingRefId);
        OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking, productId);

        toDoTaskResource.setReferenceId(driver.getId()); //OPS DB ClientCommercialClaimNO
        toDoTaskResource.setBookingRefId(bookingRefId);
        toDoTaskResource.setCreatedByUserId(userID);
        toDoTaskResource.setProductId(opsProduct.getProductName());
        toDoTaskResource.setClientId(opsBooking.getClientID());
        toDoTaskResource.setCompanyId(opsBooking.getCompanyId());
        toDoTaskResource.setClientTypeId(opsBooking.getClientType());

        if (opsBooking.getClientType().equalsIgnoreCase("B2B")) {
            b2BClient = mdmClientService.getB2bClient(opsBooking.getClientID());
            toDoTaskResource.setClientCategoryId(b2BClient.getClientCategory());
            toDoTaskResource.setCompanyMarketId(b2BClient.getCompanyId());//TODo: need to add in B2Bclient class
            toDoTaskResource.setClientSubCategoryId(b2BClient.getClientSubCategory());
        }
        if (opsBooking.getClientType().equalsIgnoreCase("B2C")) {
            b2CClient = mdmClientService.getB2cClient(opsBooking.getClientID());
            toDoTaskResource.setClientCategoryId(b2CClient.getClientCategory());
            toDoTaskResource.setCompanyMarketId(b2CClient.getCompanyId());//TODo: need to add in B2Bclient class
            toDoTaskResource.setClientSubCategoryId(b2CClient.getClientSubCategory());
        }

        toDoTaskResource.setDueOnDate(ZonedDateTime.now());//TODO: take this as travel date - 24 hrs
        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.UPDATE.getValue());
        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.ADD_DRIVER_DETAILS.toString());
        toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue()); // OPERATIONS or FINANCE
        toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.ASSIGNED.getValue()); //ASSIGN as per my assumption
        toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
        return toDoTaskResource;
    }

    public ToDoTaskResource getTodoForUser(Driver driver) throws OperationException {
        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        B2BClient b2BClient = new B2BClient();
        B2CClient b2CClient = new B2CClient();
        String bookingRefId = driver.getBookingRefId();
        String productId = driver.getProductId();
        String userID = userService.getLoggedInUserId();
        OpsBooking opsBooking = opsBookingService.getBooking(bookingRefId);
        OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking, productId);

        toDoTaskResource.setReferenceId(driver.getId()); //OPS DB ClientCommercialClaimNO
        toDoTaskResource.setBookingRefId(bookingRefId);
        toDoTaskResource.setCreatedByUserId(userID);
        toDoTaskResource.setProductId(opsProduct.getProductName());
        toDoTaskResource.setClientId(opsBooking.getClientID());
        toDoTaskResource.setCompanyId(opsBooking.getCompanyId());
        toDoTaskResource.setClientTypeId(opsBooking.getClientType());

        if (opsBooking.getClientType().equalsIgnoreCase("B2B")) {
            b2BClient = mdmClientService.getB2bClient(opsBooking.getClientID());
            toDoTaskResource.setClientCategoryId(b2BClient.getClientCategory());
            toDoTaskResource.setCompanyMarketId(b2BClient.getCompanyId());//TODo: need to add in B2Bclient class
            toDoTaskResource.setClientSubCategoryId(b2BClient.getClientSubCategory());
        }
        if (opsBooking.getClientType().equalsIgnoreCase("B2C")) {
            b2CClient = mdmClientService.getB2cClient(opsBooking.getClientID());
            toDoTaskResource.setClientCategoryId(b2CClient.getClientCategory());
            toDoTaskResource.setCompanyMarketId(b2CClient.getCompanyId());//TODo: need to add in B2Bclient class
            toDoTaskResource.setClientSubCategoryId(b2CClient.getClientSubCategory());
        }

        toDoTaskResource.setDueOnDate(ZonedDateTime.now());//TODO: take this as travel date - 24 hrs
        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.UPDATE.getValue());
        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.ADD_DRIVER_DETAILS.toString());
        toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue()); // OPERATIONS or FINANCE
        toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.ASSIGNED.getValue()); //ASSIGN as per my assumption
        toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
        return toDoTaskResource;
    }


//    private boolean isSupplierExtranet(String supplierId) throws OperationException {
//        boolean extranet = false;
//        String jsonSupplier = supplierDetailsService.getSupplierDetails(supplierId);
//        String extranetAccess = jsonObjectProvider.getAttributeValue(jsonSupplier, "$.extranetAccess", String.class);
//        if (!StringUtils.isEmpty(extranetAccess))
//        {
//            if ("True".equalsIgnoreCase(extranetAccess))
//                extranet = true;
//            else
//                extranet = false;
//        }
//        return extranet;
//    }

}
