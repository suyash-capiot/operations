package com.coxandkings.travel.operations.service.email.impl;

import com.coxandkings.travel.operations.enums.communication.CommunicationType;
import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.model.email.Email;
import com.coxandkings.travel.operations.model.email.FileAttachment;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.producer.service.WorkUnitDispatcher;
import com.coxandkings.travel.operations.repository.communication.CommunicationTagRepository;
import com.coxandkings.travel.operations.repository.email.EmailRepository;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;
import com.coxandkings.travel.operations.resource.documentLibrary.DocumentReferenceResource;
import com.coxandkings.travel.operations.resource.documentLibrary.DocumentResource;
import com.coxandkings.travel.operations.resource.documentLibrary.DocumentSearchResource;
import com.coxandkings.travel.operations.resource.documentLibrary.NewDocumentResource;
import com.coxandkings.travel.operations.resource.email.*;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.documentLibrary.DocumentLibraryService;
import com.coxandkings.travel.operations.service.email.EmailService;
import com.coxandkings.travel.operations.service.template.TemplateLoaderService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.SendMailCnK;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.mail.pop3.POP3Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    private static Logger logger = LogManager.getLogger(EmailServiceImpl.class);

    @Value(value = "${email.inbound.host}")
    private String INBOUND_HOST;
    @Value(value = "${email.inbound.port}")
    private String INBOUND_PORT;
    @Value(value = "${email.inbound.protocol}")
    private String INBOUND_PROTOCOL;
    @Value(value = "${email.inbound.folder}")
    private String INBOUND_FOLDER;
    @Value(value = "${email.inbound.client-inbox}")
    private String CLIENT_USERNAME;
    @Value(value = "${email.inbound.client-password}")
    private String CLIENT_PASSWORD;
    @Value(value = "${email.inbound.customer-inbox}")
    private String CUSTOMER_USERNAME;
    @Value(value = "${email.inbound.customer-password}")
    private String CUSTOMER_PASSWORD;
    @Value(value = "${email.inbound.supplier-inbox}")
    private String SUPPLIER_USERNAME;
    @Value(value = "${email.inbound.supplier-password}")
    private String SUPPLIER_PASSWORD;
    @Value("${email.inbound.scheduler-config}")
    private String inbodundSchedulerCronExpression;
    @Value("${email.temp-folder}")
    private String UPLOADED_FOLDER;
    @Value(value = "${email.run-inbound}")
    private Boolean FLAG;

    @Autowired
    private SendMailCnK sendMailCnK;

    @Autowired
    private WorkUnitDispatcher workUnitDispatcher;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private CommunicationTagRepository communicationTagRepository;

    @Autowired
    private TemplateLoaderService templateLoaderService;

    @Autowired
    private DocumentLibraryService documentLibraryService;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private UserService userService;

    @Value("${document_library.upload}")
    private String docsUrl;
    @Value("${kafka.producer.email.topic}")
    private String emailTopic;

    private static void deleteFileFromTheTemporaryFolder(String file_path) throws OperationException {
        try {
            Path filePath = Paths.get(file_path);
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                logger.error("deleteFileFromTheTemporaryFolder() - Error while deleting the file \n" + e);
                throw new OperationException(Constants.OPS_ERR_20006);
            }
        } catch (Exception e) {
            logger.error("Error in deleting temporary file", e);
            throw new OperationException(Constants.OPS_ERR_20007);
        }
    }

    private static void writeBytesToFileNio(byte[] bFile, String fileDest) throws OperationException {
        try {
            try {
                Path path = Paths.get(fileDest);
                Files.write(path, bFile);
            } catch (IOException e) {
                throw new OperationException(Constants.OPS_ERR_20008);
            }
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20009);
        }

    }

    @Override
    public EmailResponse sendEmail(EmailWithBodyResource resource) throws OperationException {
        try {
            if (resource.getPriority().toString().toUpperCase().equalsIgnoreCase("NORMAL")) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(resource);
                Map<String, String> map = (Map<String, String>) mapper.readValue(json, Map.class);
                map.put("emailResourceType", "type1");
                json = mapper.writeValueAsString(map);
                System.out.println(json);
                this.workUnitDispatcher.dispatch(emailTopic, json);
                EmailResponse response = new EmailResponse();
                response.setStatus("Success");
                return response;

            } else if (resource.getPriority().toString().toUpperCase().equalsIgnoreCase("HIGH")) {
                Email email = new Email();

                List<FileAttachment> fileAttachments = new ArrayList<>();
                CopyUtils.copy(resource, email);

                email.setRecipientList(resource.getToMail());
                email.setSender(resource.getFromMail());
                email.setIs_outbound(true);
                email.setCommunicationType(String.valueOf(CommunicationType.EMAIL));
                email.setCcRecipientList(resource.getCcMail());
                email.setBccRecipientList(resource.getBccMail());
                email.setBookId(resource.getBookId());
                email.setUserId(resource.getUserId());
                email.setSupplier(resource.getSupplier());
                email.setProcess(resource.getProcess());
                email.setScenario(resource.getScenario());
                email.setFunction(resource.getFunction());
                email.setProductSubCategory(resource.getProductSubCategory());

                if (resource.getCommunicationTagResource() != null) {
                    CommunicationTags communicationTags = new CommunicationTags();
                    communicationTags.setBaseCommunication(email);
                    CopyUtils.copy(resource.getCommunicationTagResource(), communicationTags);
                    email.setCommunicationTags(communicationTags);
                }

                if (resource.getFileAttachments() != null && resource.getFileAttachments().size() > 0) {
                    for (FileAttachmentResource fileAttachmentResource : resource.getFileAttachments()) {
                        FileAttachment fileAttachment = new FileAttachment();
                        Blob blob = null;
                        try {
                            blob = new javax.sql.rowset.serial.SerialBlob(fileAttachmentResource.getContent());
                        } catch (Exception e) {
                            throw new OperationException(Constants.OPS_ERR_20003);
                        }
                        fileAttachment.setContent(blob);
                        fileAttachment.setEmail(email);
                        fileAttachment.setFilename(fileAttachmentResource.getFilename());
                        fileAttachments.add(fileAttachment);
                    }
                    email.setFileAttachments(fileAttachments);

                    for (FileAttachmentResource fileAttachmentResource : resource.getFileAttachments()) {
                        String fileName = UPLOADED_FOLDER + File.separator + fileAttachmentResource.getFilename();
                        writeBytesToFileNio(fileAttachmentResource.getContent(), fileName);
                    }
                }

                String to = null, cc = null, bcc = null;
                to = createToString(email.getRecipientList());
                if (email.getCcRecipientList() != null && email.getCcRecipientList().size() > 0)
                    cc = createToString(email.getCcRecipientList());
                if (email.getBccRecipientList() != null && email.getBccRecipientList().size() > 0)
                    bcc = createToString(email.getBccRecipientList());
                EmailResponse response;
                if (resource.getFileAttachments() != null && resource.getFileAttachments().size() > 0) {
                    try {
                        response = sendMailCnK.send(email.getSender(), to, cc, bcc, email.getSubject(), email.getBody(), resource.getFileAttachments());
                    } catch (Exception e) {
                        logger.error("Technical problem occured - Webmail server issues.");
                        throw new OperationException(Constants.OPS_ERR_20002);
                    }
                    for (FileAttachmentResource fileAttachmentResource : resource.getFileAttachments()) {
                        String fileName = UPLOADED_FOLDER + File.separator + fileAttachmentResource.getFilename();
                        deleteFileFromTheTemporaryFolder(fileName);
                    }
                } else
                    try {
                        response = sendMailCnK.send(email.getSender(), to, cc, bcc, email.getSubject(), email.getBody());
                    } catch (Exception e) {
                        logger.error("Technical problem occured - Webmail server issues.");
                        throw new OperationException(Constants.OPS_ERR_20002);
                    }
                //Saving it into the DB
                email.setMessageId(response.getMessageID());
                Email emailDB = emailRepository.saveOrUpdate(email);
                response.setEmailID(emailDB.getId());
                return response;
            } else {

                throw new OperationException(Constants.OPS_ERR_20001);
            }
        } catch (Exception e) {
            logger.error("Some error while sending email", e);
            throw new OperationException(Constants.OPS_ERR_20005);
        }
    }

    @Override
    public EmailResponse sendEmail(EmailUsingTemplateResource resource) throws OperationException, JSONException {
        try {
            if (resource.getPriority().toString().toUpperCase().equalsIgnoreCase("NORMAL")) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(resource);
                Map<String, String> map = (Map<String, String>) mapper.readValue(json, Map.class);
                map.put("emailResourceType", "type2");
                json = mapper.writeValueAsString(map);
                System.out.println(json);
                this.workUnitDispatcher.dispatch(emailTopic, json);
                EmailResponse response = new EmailResponse();
                response.setStatus("Success");
                return response;

            } else if (resource.getPriority().toString().toUpperCase().equalsIgnoreCase("HIGH")) {
                List<DynamicVariables> dynamicVariables = new ArrayList<>();
                if (resource.getDynamicVariables() != null) {
                    DynamicVariables dynamicVariable = null;
                    for (Map.Entry<String, String> entry : resource.getDynamicVariables().entrySet()) {
                        dynamicVariable = new DynamicVariables();
                        dynamicVariable.setName(entry.getKey());
                        dynamicVariable.setValue(entry.getValue());
                        dynamicVariables.add(dynamicVariable);
                    }
                }

                List<FileAttachment> fileAttachments = new ArrayList<>();
                String body = templateLoaderService.getEmailContent(resource.getTemplateInfo(), dynamicVariables);

                Email email = new Email();
                CopyUtils.copy(resource, email);
                email.setBody(body);
                email.setRecipientList(resource.getToMail());
                email.setSender(resource.getFromMail());
                email.setIs_outbound(true);
                email.setCommunicationType(String.valueOf(CommunicationType.EMAIL));
                email.setCcRecipientList(resource.getCcMail());
                email.setBccRecipientList(resource.getBccMail());
                email.setBookId(resource.getBookId());
                email.setUserId(resource.getUserId());
                email.setSupplier(resource.getSupplier());
                email.setProcess(resource.getProcess());
                email.setScenario(resource.getScenario());
                email.setFunction(resource.getFunction());
                email.setProductSubCategory(resource.getProductSubCategory());

                if (resource.getCommunicationTagResource() != null) {
                    CommunicationTags communicationTags = new CommunicationTags();
                    communicationTags.setBaseCommunication(email);
                    CopyUtils.copy(resource.getCommunicationTagResource(), communicationTags);
                    email.setCommunicationTags(communicationTags);
                }
                if (resource.getFileAttachments() != null && resource.getFileAttachments().size() > 0) {
                    for (FileAttachmentResource fileAttachmentResource : resource.getFileAttachments()) {
                        FileAttachment fileAttachment = new FileAttachment();
                        Blob blob = null;
                        try {
                            blob = new javax.sql.rowset.serial.SerialBlob(fileAttachmentResource.getContent());
                        } catch (SQLException e) {
                            throw new OperationException(Constants.OPS_ERR_20003);
                        }
                        fileAttachment.setContent(blob);
                        fileAttachment.setEmail(email);
                        fileAttachment.setFilename(fileAttachmentResource.getFilename());
                        fileAttachments.add(fileAttachment);
                    }
                    email.setFileAttachments(fileAttachments);
                    for (FileAttachmentResource fileAttachmentResource : resource.getFileAttachments()) {
                        String fileName = UPLOADED_FOLDER + File.separator + fileAttachmentResource.getFilename();
                        writeBytesToFileNio(fileAttachmentResource.getContent(), fileName);
                    }
                }
                //sending email
                String to = null, cc = null, bcc = null;
                to = createToString(email.getRecipientList());
                if (email.getCcRecipientList() != null && email.getCcRecipientList().size() > 0)
                    cc = createToString(email.getCcRecipientList());
                if (email.getBccRecipientList() != null && email.getBccRecipientList().size() > 0)
                    bcc = createToString(email.getBccRecipientList());
                EmailResponse response;
                if (resource.getFileAttachments() != null && resource.getFileAttachments().size() > 0) {
                    try {
                        response = sendMailCnK.send(email.getSender(), to, cc, bcc, email.getSubject(), email.getBody(), resource.getFileAttachments());
                    } catch (Exception e) {
                        logger.error("Technical problem occured - Webmail server issues.", e);
                        throw new OperationException(Constants.OPS_ERR_20002);
                    }
                    for (FileAttachmentResource fileAttachmentResource : resource.getFileAttachments()) {
                        String fileName = UPLOADED_FOLDER + File.separator + fileAttachmentResource.getFilename();
                        deleteFileFromTheTemporaryFolder(fileName);
                    }
                } else
                    try {
                        response = sendMailCnK.send(email.getSender(), to, cc, bcc, email.getSubject(), email.getBody());
                    } catch (Exception e) {
                        logger.error("Technical problem occured - Webmail server issues.", e);
                        throw new OperationException(Constants.OPS_ERR_20002);
                    }

                //Saving it into the DB
                email.setMessageId(response.getMessageID());
                Email emailDB = emailRepository.saveOrUpdate(email);
                response.setEmailID(emailDB.getId());
                return response;
            } else {
                throw new OperationException(Constants.OPS_ERR_20001);
            }
        } catch (Exception e) {
            logger.error("Some error while sending email", e);
            throw new OperationException(Constants.OPS_ERR_20005);
        }

    }

    @Override
    public EmailResponse sendEmail(EmailUsingTemplateAndDocumentsResource resource) throws OperationException, JSONException {
        try {
            if (resource.getPriority().toString().toUpperCase().equalsIgnoreCase("NORMAL")) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(resource);
                Map<String, String> map = (Map<String, String>) mapper.readValue(json, Map.class);
                map.put("emailResourceType", "type3");
                json = mapper.writeValueAsString(map);
                System.out.println(json);
                this.workUnitDispatcher.dispatch(emailTopic, json);
                EmailResponse response = new EmailResponse();
                response.setStatus("Success");
                return response;

            } else if (resource.getPriority().toString().toUpperCase().equalsIgnoreCase("HIGH")) {
                List<DynamicVariables> dynamicVariables = new ArrayList<>();
                DynamicVariables dynamicVariable = null;
                if (resource.getDynamicVariables() != null) {
                    for (Map.Entry<String, String> entry : resource.getDynamicVariables().entrySet()) {
                        dynamicVariable = new DynamicVariables();
                        dynamicVariable.setName(entry.getKey());
                        dynamicVariable.setValue(entry.getValue());
                        dynamicVariables.add(dynamicVariable);
                    }
                }

                List<FileAttachment> fileAttachments = new ArrayList<>();
                String body = templateLoaderService.getEmailContent(resource.getTemplateInfo(), dynamicVariables);
                Email email = new Email();
                CopyUtils.copy(resource, email);
                email.setBody(body);
                email.setRecipientList(resource.getToMail());
                email.setSender(resource.getFromMail());
                email.setIs_outbound(true);
                email.setCommunicationType(String.valueOf(CommunicationType.EMAIL));
                email.setCcRecipientList(resource.getCcMail());
                email.setBccRecipientList(resource.getBccMail());
                email.setBookId(resource.getBookId());
                email.setUserId(resource.getUserId());
                email.setSupplier(resource.getSupplier());
                email.setProcess(resource.getProcess());
                email.setScenario(resource.getScenario());
                email.setFunction(resource.getFunction());
                email.setProductSubCategory(resource.getProductSubCategory());

                if (resource.getCommunicationTagResource() != null) {
                    CommunicationTags communicationTags = new CommunicationTags();
                    communicationTags.setBaseCommunication(email);
                    CopyUtils.copy(resource.getCommunicationTagResource(), communicationTags);
                    email.setCommunicationTags(communicationTags);
                }

                List<FileAttachmentResource> fileAttachmentResources = new ArrayList<>();

                if (resource.getFileAttachments() != null && resource.getFileAttachments().size() > 0) {
                    for (FileAttachmentResource fileAttachmentResource : resource.getFileAttachments()) {
                        String fileName = UPLOADED_FOLDER + File.separator + fileAttachmentResource.getFilename();
                        writeBytesToFileNio(fileAttachmentResource.getContent(), fileName);
                        fileAttachmentResources.add(fileAttachmentResource);

                        FileAttachment fileAttachment = new FileAttachment();
                        Blob blob = null;
                        try {
                            blob = new javax.sql.rowset.serial.SerialBlob(fileAttachmentResource.getContent());
                        } catch (SQLException e) {
                            throw new OperationException(Constants.OPS_ERR_20003);
                        }
                        fileAttachment.setContent(blob);
                        fileAttachment.setEmail(email);
                        fileAttachment.setFilename(fileAttachmentResource.getFilename());
                        fileAttachments.add(fileAttachment);

                    }
                }

                List<DocumentSearchResource> documentSearchResources = new ArrayList<>();
                List<DocumentResource> documentResources = new ArrayList<>();
                if (resource.getDocumentReferenceIDs() != null && resource.getDocumentReferenceIDs().size() > 0) {

                    for (String docRefID : resource.getDocumentReferenceIDs()) {
                        DocumentSearchResource documentSearchResource = new DocumentSearchResource();
                        documentSearchResource.setId(docRefID);
                        documentSearchResources.add(documentSearchResource);
                    }
                    //Adding all the Files from document Management Library and files added in the request to one object
                    documentResources = documentLibraryService.getDocumentsV2(documentSearchResources);
                    for (DocumentResource documentResource : documentResources) {

                        //storing all the files in a temporary location
                        String fileName = UPLOADED_FOLDER + File.separator + documentResource.getFileName();
                        writeBytesToFileNio(documentResource.getByteArray(), fileName);

                        FileAttachmentResource fileAttachmentResource = new FileAttachmentResource();
                        fileAttachmentResource.setFilename(documentResource.getFileName());
                        fileAttachmentResource.setContent(documentResource.getByteArray());
                        fileAttachmentResources.add(fileAttachmentResource);

                        FileAttachment fileAttachment = new FileAttachment();
                        Blob blob = null;
                        try {
                            blob = new javax.sql.rowset.serial.SerialBlob(fileAttachmentResource.getContent());
                        } catch (SQLException e) {
                            throw new OperationException(Constants.OPS_ERR_20003);
                        }
                        fileAttachment.setContent(blob);
                        fileAttachment.setEmail(email);
                        fileAttachment.setFilename(fileAttachmentResource.getFilename());
                        fileAttachments.add(fileAttachment);
                    }
                }
                email.setFileAttachments(fileAttachments);

                //sending email
                String to = null, cc = null, bcc = null;
                to = createToString(email.getRecipientList());
                if (email.getCcRecipientList() != null && email.getCcRecipientList().size() > 0)
                    cc = createToString(email.getCcRecipientList());
                if (email.getBccRecipientList() != null && email.getBccRecipientList().size() > 0)
                    bcc = createToString(email.getBccRecipientList());
                EmailResponse response;
                if (fileAttachmentResources != null && fileAttachmentResources.size() > 0) {
                    try {
                        response = sendMailCnK.send(email.getSender(), to, cc, bcc, email.getSubject(), email.getBody(), fileAttachmentResources);
                    } catch (Exception e) {
                        logger.error("Technical problem occured - Webmail server issues.", e);
                        throw new OperationException(Constants.OPS_ERR_20002);
                    }
                } else
                    try {
                        response = sendMailCnK.send(email.getSender(), to, cc, bcc, email.getSubject(), email.getBody());
                    } catch (Exception e) {
                        logger.error("Technical problem occured - Webmail server issues.");
                        throw new OperationException(Constants.OPS_ERR_20002);
                    }

                //deleting all the files
                if (resource.getFileAttachments() != null && resource.getDocumentReferenceIDs().size() > 0) {
                    for (FileAttachmentResource fileAttachmentResource : resource.getFileAttachments()) {
                        String fileName = UPLOADED_FOLDER + File.separator + fileAttachmentResource.getFilename();
                        deleteFileFromTheTemporaryFolder(fileName);
                    }
                }

                if (resource.getDocumentReferenceIDs() != null && resource.getDocumentReferenceIDs().size() > 0) {
                    for (DocumentResource documentResource : documentResources) {
                        //storing all the files in a temporary location
                        String fileName = UPLOADED_FOLDER + File.separator + documentResource.getFileName();
                        deleteFileFromTheTemporaryFolder(fileName);
                    }
                }
                //Saving it into the DB
                email.setMessageId(response.getMessageID());
                Email emailDB = emailRepository.saveOrUpdate(email);
                response.setEmailID(emailDB.getId());
                return response;
            } else {
                throw new OperationException(Constants.OPS_ERR_20001);
            }
        } catch (Exception e) {
            logger.error("Some error while sending email", e);
            throw new OperationException(Constants.OPS_ERR_20005);
        }
    }

    @Override
    public EmailResponse sendEmail(EmailUsingBodyAndDocumentsResource resource) throws OperationException {
        try {
            if (null == resource.getPriority())
                resource.setPriority(EmailPriority.HIGH);

            if (resource.getPriority().toString().toUpperCase().equalsIgnoreCase("NORMAL")) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(resource);
                Map<String, String> map = (Map<String, String>) mapper.readValue(json, Map.class);
                map.put("emailResourceType", "type4");
                json = mapper.writeValueAsString(map);
                System.out.println(json);
                this.workUnitDispatcher.dispatch(emailTopic, json);
                EmailResponse response = new EmailResponse();
                response.setStatus("Success");
                return response;

            } else if (resource.getPriority().toString().toUpperCase().equalsIgnoreCase("HIGH")) {

                Email email = new Email();
                List<FileAttachment> fileAttachments = new ArrayList<>();
                CopyUtils.copy(resource, email);
                email.setRecipientList(resource.getToMail());
                email.setSender(resource.getFromMail());
                email.setIs_outbound(true);
                email.setCommunicationType(String.valueOf(CommunicationType.EMAIL));
                email.setCcRecipientList(resource.getCcMail());
                email.setBccRecipientList(resource.getBccMail());
                email.setBookId(resource.getBookId());
                email.setUserId(resource.getUserId());
                email.setSupplier(resource.getSupplier());
                email.setProcess(resource.getProcess());
                email.setScenario(resource.getScenario());
                email.setFunction(resource.getFunction());
                email.setProductSubCategory(resource.getProductSubCategory());

                if (resource.getCommunicationTagResource() != null) {
                    CommunicationTags communicationTags = new CommunicationTags();
                    communicationTags.setBaseCommunication(email);
                    CopyUtils.copy(resource.getCommunicationTagResource(), communicationTags);
                    email.setCommunicationTags(communicationTags);
                }

                List<FileAttachmentResource> fileAttachmentResources = new ArrayList<>();

                if (resource.getFileAttachments() != null && resource.getFileAttachments().size() > 0) {
                    for (FileAttachmentResource fileAttachmentResource : resource.getFileAttachments()) {
                        String fileName = UPLOADED_FOLDER + File.separator + fileAttachmentResource.getFilename();
                        writeBytesToFileNio(fileAttachmentResource.getContent(), fileName);
                        fileAttachmentResources.add(fileAttachmentResource);

                        FileAttachment fileAttachment = new FileAttachment();
                        Blob blob = null;
                        try {
                            blob = new javax.sql.rowset.serial.SerialBlob(fileAttachmentResource.getContent());
                        } catch (SQLException e) {
                            throw new OperationException(Constants.OPS_ERR_20003);
                        }

                        fileAttachment.setContent(blob);
                        fileAttachment.setEmail(email);
                        fileAttachment.setFilename(fileAttachmentResource.getFilename());
                        fileAttachments.add(fileAttachment);
                    }
                }

                List<DocumentSearchResource> documentSearchResources = new ArrayList<>();
                List<DocumentResource> documentResources = new ArrayList<>();
                if (resource.getDocumentReferenceIDs() != null && resource.getDocumentReferenceIDs().size() > 0) {

                    for (String docRefID : resource.getDocumentReferenceIDs()) {
                        DocumentSearchResource documentSearchResource = new DocumentSearchResource();
                        documentSearchResource.setId(docRefID);
                        documentSearchResources.add(documentSearchResource);
                    }
                    //Adding all the Files from document Management Library and files added in the request to one object
                    documentResources = documentLibraryService.getDocumentsV2(documentSearchResources);
                    for (DocumentResource documentResource : documentResources) {

                        //storing all the files in a temporary location
                        String fileName = UPLOADED_FOLDER + File.separator + documentResource.getFileName();
                        writeBytesToFileNio(documentResource.getByteArray(), fileName);

                        FileAttachmentResource fileAttachmentResource = new FileAttachmentResource();
                        fileAttachmentResource.setFilename(documentResource.getFileName());
                        fileAttachmentResource.setContent(documentResource.getByteArray());
                        fileAttachmentResources.add(fileAttachmentResource);

                        FileAttachment fileAttachment = new FileAttachment();
                        Blob blob = null;
                        try {
                            blob = new javax.sql.rowset.serial.SerialBlob(fileAttachmentResource.getContent());
                        } catch (Exception e) {
                            throw new OperationException(Constants.OPS_ERR_20003);
                        }

                        fileAttachment.setContent(blob);
                        fileAttachment.setDocId(documentResource.getId());
                        fileAttachment.setEmail(email);
                        fileAttachment.setFilename(fileAttachmentResource.getFilename());
                        fileAttachments.add(fileAttachment);
                    }
                }
                email.setFileAttachments(fileAttachments);

                //sending email
                String to = null, cc = null, bcc = null;
                to = createToString(email.getRecipientList());
                if (email.getCcRecipientList() != null && email.getCcRecipientList().size() > 0)
                    cc = createToString(email.getCcRecipientList());
                if (email.getBccRecipientList() != null && email.getBccRecipientList().size() > 0)
                    bcc = createToString(email.getBccRecipientList());
                EmailResponse response;
                if (fileAttachmentResources != null && fileAttachmentResources.size() > 0)
                    try {
                        response = sendMailCnK.send(email.getSender(), to, cc, bcc, email.getSubject(), email.getBody(), fileAttachmentResources);
                    } catch (Exception e) {
                        logger.error("Technical problem occured - Webmail server issues.", e);
                        throw new OperationException(Constants.OPS_ERR_20002);
                    }
                else
                    try {
                        response = sendMailCnK.send(email.getSender(), to, cc, bcc, email.getSubject(), email.getBody());
                    } catch (Exception e) {
                        logger.error("Technical problem occured - Webmail server issues.", e);
                        throw new OperationException(Constants.OPS_ERR_20002);
                    }

                if (resource.getFileAttachments() != null && resource.getFileAttachments().size() > 0) {
                    for (FileAttachmentResource fileAttachmentResource : resource.getFileAttachments()) {
                        String fileName = UPLOADED_FOLDER + File.separator + fileAttachmentResource.getFilename();
                        deleteFileFromTheTemporaryFolder(fileName);
                    }
                }

                if (resource.getDocumentReferenceIDs() != null && resource.getDocumentReferenceIDs().size() > 0) {
                    for (DocumentResource documentResource : documentResources) {
                        //storing all the files in a temporary location
                        String fileName = UPLOADED_FOLDER + File.separator + documentResource.getFileName();
                        deleteFileFromTheTemporaryFolder(fileName);
                    }
                }

                //Saving it into the DB
                email.setMessageId(response.getMessageID());
                Email emailDB = emailRepository.saveOrUpdate(email);
                response.setEmailID(emailDB.getId());
                return response;
            } else {
                throw new OperationException(Constants.OPS_ERR_20001);
            }
        } catch (Exception e) {
            logger.error("Some error while sending email", e);
            throw new OperationException(Constants.OPS_ERR_20005);
        }
    }

    @PostConstruct
    @Override
    public void receiveCustomerInboundEmails() {
        if (FLAG)
            receiveEmails("customer");
    }

    @PostConstruct
    @Override
    public void receiveClientInboundEmails() {
        if (FLAG)
            receiveEmails("client");
    }

    @PostConstruct
    @Override
    public void receiveSupplierInboundEmails() {
        if (FLAG)
            receiveEmails("supplier");
    }

    @Override
    public void receiveAutoInBoundEmail() {
        if (FLAG)
            receiveEmails("auto");
    }

    @Override
    public Email getEmailById(String emailId) {
        return emailRepository.getEmailById(emailId);
    }

    @Override
    public List<Email> getEmailByMessageId(String messageId) throws OperationException {
        return emailRepository.getEmailByMessageId(messageId);
    }

    @Override
    public Email getEmailByRefId(String messageId) throws OperationException {
        return emailRepository.getRefMailId(messageId);
    }

    @Override
    public Email markAsRead(String id) {
        Email email = emailRepository.markAsRead(id);
        return email;
    }

    @javax.transaction.Transactional
    private void receiveEmails(String entity) {
        Folder fldr = null;
        Store store = null;
        try {
            String username = null;
            String password = null;
            switch (entity) {
                case "client":
                    username = CLIENT_USERNAME;
                    password = CLIENT_PASSWORD;
                    break;
                case "customer":
                    username = CUSTOMER_USERNAME;
                    password = CUSTOMER_PASSWORD;
                    break;
                case "supplier":
                    username = SUPPLIER_USERNAME;
                    password = SUPPLIER_PASSWORD;
                    break;
                case "auto":
                    username = SUPPLIER_USERNAME;
                    password = SUPPLIER_PASSWORD;
            }
            Session session = Session.getInstance(new Properties());
            store = session.getStore(INBOUND_PROTOCOL);
            store.connect(INBOUND_HOST, username, password);

            fldr = store.getFolder(INBOUND_FOLDER);
            fldr.open(Folder.READ_WRITE);
            int count = fldr.getMessageCount();
            logger.info("Email receiver - fetched " + count + " messages");



            for (int i = 1; i <= count; i++) {

                POP3Message m = (POP3Message) fldr.getMessage(i);

                Email email = null;
                email = new Email();
                email.setId(m.getMessageID());
                email.setMessageId(m.getMessageID());
                email.setSubject(m.getSubject());
                email.setIs_outbound(false);
                m.setFlag(Flags.Flag.SEEN, false);
                email.setBody(m.getDescription());
                String refMail = null;
                String result = getTextFromMessage(m);
                email.setBody(result);
                email.setCommunicationType(CommunicationType.EMAIL.toString());
                Address[] froms = m.getFrom();
                String from = froms == null ? null : ((InternetAddress) froms[0]).getAddress();
                email.setFromMail(from);
                email.setRecipientList(Collections.singletonList(Arrays.toString(m.getRecipients(Message.RecipientType.TO))));
                if (null != Message.RecipientType.CC && m.getRecipients(Message.RecipientType.CC) != null)
                    email.setCcRecipientList(Collections.singletonList(Arrays.toString(m.getRecipients(Message.RecipientType.CC))));
                if (null != Message.RecipientType.BCC && m.getRecipients(Message.RecipientType.BCC) != null)
                    email.setBccRecipientList(Collections.singletonList(Arrays.toString(m.getRecipients(Message.RecipientType.BCC))));
                if (null != m.getHeader("References")
                        && null != m.getHeader("References")[0]
                        && null != m.getHeader("References")[0].split("\r\n")[0]) {
                    refMail = m.getHeader("References")[0].split("\r\n")[0];
                    email.setRefMailId(refMail);
                }

                email = getFilesFromMail(m, email);

                if (null != refMail) {
                   List< Email> emailByMessageIds = emailRepository.getEmailByMessageId(refMail);
                   Email emailByMessageId=null;
                    if(emailByMessageIds!=null && emailByMessageIds.size()>0){
                        emailByMessageId=emailByMessageIds.get(0);
                    if (emailByMessageId != null) {
                        email.setCommunicationTags(emailByMessageId.getCommunicationTags());
                        email.setModule(emailByMessageId.getModule());
                        email.setUserId(emailByMessageId.getUserId());
                        email.setPriority(emailByMessageId.getPriority());
                        email.setBookId(emailByMessageId.getBookId());
                        email.setFunction(emailByMessageId.getFunction());
                        email.setScenario(emailByMessageId.getScenario());
                        email.setPriority(emailByMessageId.getPriority());
                        email.setProcess(emailByMessageId.getProcess());
                        email.setProductSubCategory(emailByMessageId.getProductSubCategory());
                        email.setStatus(emailByMessageId.getStatus());
                    }
                    } else {
                        emailRepository.saveOrUpdate(email);
                        createToDoTaskForInboundCommunication(email);
                        return;
                    }
                }

                emailRepository.saveOrUpdate(email);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fldr != null) {
                    fldr.close(true);
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            try {
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                //          result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

    public String createToString(List<String> to) {

        String toString = "";
        for (String recvEmail : to) {
            toString += recvEmail + ",";
        }
        return toString;
    }

    private Email getFilesFromMail(Message message, Email email) throws OperationException {
        try {
            List<FileAttachment> fileAttachments = new ArrayList<>();
            List<DocumentSearchResource> documentSearchResources = new ArrayList<>();
            try {
                if (message.isMimeType("multipart/*")) {
                    String attachFiles = "";
                    Multipart multiPart = null;
                    try {
                        multiPart = (Multipart) message.getContent();
                    } catch (IOException e) {
                        logger.error("getFilesFromMail() - Error while fetching email content " + e);
                        throw new OperationException(Constants.OPS_ERR_20014);
                    }
                    int numberOfParts = multiPart.getCount();
                    List<DocumentResource> documentResources;
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            // this part is attachment
                            String fileName = part.getFileName();
                            attachFiles += fileName + ", ";
                            try {
                                part.saveFile(UPLOADED_FOLDER + File.separator + fileName);
                            } catch (IOException e) {
                                logger.error("getFilesFromMail() - Error while saving file to the directory " + e);
                                throw new OperationException(Constants.OPS_ERR_20013);
                            }
                            DocumentReferenceResource documentReferenceResource = null;
                            try {
                                documentReferenceResource = saveToDocumentLibrary(UPLOADED_FOLDER + File.separator + fileName, fileName);
                            } catch (RepositoryException e) {
                                logger.error("getFilesFromMail() - Error while saving file to the document Library" + e);
                                throw new OperationException(Constants.OPS_ERR_20011);
                            } catch (FileNotFoundException e) {
                                logger.error("getFilesFromMail() - File Not Found while saving file to the document Library" + e);
                                throw new OperationException(Constants.OPS_ERR_20012);
                            }

                           /* DocumentSearchResource documentSearchResource = new DocumentSearchResource();
                            documentSearchResource.setId(documentReferenceResource.getId());
                            documentSearchResources.add(documentSearchResource);
                            documentResources = documentLibraryService.getDocuments(documentSearchResources);*/

                            FileAttachment fileAttachment = new FileAttachment();
                           /* Blob blob = null;
                            try {
                                blob = new javax.sql.rowset.serial.SerialBlob(documentResources.get(0).getByteArray());
                            } catch (SQLException e) {
                                logger.error("getFilesFromMail() - SQLException - Error while parsing the file");
                                throw new OperationException(Constants.OPS_ERR_20003);
                            }
                            fileAttachment.setContent(blob);*/
                            fileAttachment.setEmail(email);
                            fileAttachment.setDocId(documentReferenceResource.getId());
                            fileAttachment.setFilename(fileName);
                            fileAttachments.add(fileAttachment);

                            deleteFileFromTheTemporaryFolder(UPLOADED_FOLDER + File.separator + fileName);
                        }
                    }
                    email.setFileAttachments(fileAttachments);
                }
            } catch (MessagingException e) {
                logger.error("getFilesFromMail() : Error reading the body of Incoming email to fetch files " + e);
                throw new OperationException(Constants.OPS_ERR_20004);
            }
            return email;
        } catch (Exception e) {
            logger.error("getFilesFromMail() - Error while reading files from the mail ", e);
            throw new OperationException(Constants.OPS_ERR_20010);
        }
    }

    private DocumentReferenceResource saveToDocumentLibrary(String filePath, String fileName) throws FileNotFoundException, OperationException, RepositoryException {
        File initialFile = new File(filePath);
        InputStream is = new FileInputStream(initialFile);
        NewDocumentResource newDocumentResource = new DocumentSearchResource();
        newDocumentResource.setType("emailAttachment");
        newDocumentResource.setName(fileName);
        newDocumentResource.setCategory("emailAttachment");
        DocumentReferenceResource documentReferenceResource = documentLibraryService.createV2(null, newDocumentResource, is);

        return documentReferenceResource;
    }

    private void createToDoTaskForInboundCommunication(Email email) throws OperationException {
        ToDoTask toDoDetails = null;
        try {
            ToDoTaskResource toDo = new ToDoTaskResource();
            toDo.setBookingRefId(email.getBookId());
            try {
                String userId = userService.getSystemUserIdFromMDMToken();
                toDo.setCreatedByUserId(userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            toDo.setReferenceId(email.getMessageId());
            //toDo.setClientTypeId(opsBooking.getClientType());
            toDo.setCompanyId("Company");
            toDo.setClientId(email.getUserId());
            toDo.setCompanyMarketId("CompanyMarket");
            //  toDo.setDueOn( ZonedDateTime.now( ).toInstant( ).toEpochMilli( ) );
            toDo.setTaskNameId(ToDoTaskNameValues.EMAILTAG.getValue());
            toDo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
            toDo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
            toDo.setTaskSubTypeId(ToDoTaskSubTypeValues.EMAILTAG.toString());
            toDo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
            toDo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
            toDoTaskService.save(toDo);
        } catch (Exception e) {
            logger.error("Error while creating a todo task for tagging an incoming email");
            throw new OperationException(Constants.OPS_ERR_20015);
        }
    }

    @Override
    public CommunicationTags getAssociatedTags(String id) {
        Email email = emailRepository.getEmailById(id);
        return email.getCommunicationTags();
    }

    @Override
    public Email updateCommunicationTags(String id, CommunicationTagResource communicationTagResource) {
        Email email = getEmailById(id);
        CommunicationTags communicationTags = new CommunicationTags();
        CopyUtils.copy(communicationTagResource, communicationTags);
        email.setCommunicationTags(communicationTags);
        return emailRepository.saveOrUpdate(email);
    }

    private void createSearchEmailCondition(){
        SearchTerm searchCondition = new SearchTerm() {
            @Override
            public boolean match(Message message) {
                try {

                    if (message.getSubject().contains("")) {
                        return true;
                    }
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        };
    }
}