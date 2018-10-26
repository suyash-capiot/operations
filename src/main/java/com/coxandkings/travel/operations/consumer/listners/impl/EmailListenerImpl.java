package com.coxandkings.travel.operations.consumer.listners.impl;

import com.coxandkings.travel.operations.consumer.listners.EmailListener;
import com.coxandkings.travel.operations.enums.communication.CommunicationType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.model.email.Email;
import com.coxandkings.travel.operations.model.email.FileAttachment;
import com.coxandkings.travel.operations.repository.email.EmailRepository;
import com.coxandkings.travel.operations.resource.documentLibrary.DocumentResource;
import com.coxandkings.travel.operations.resource.documentLibrary.DocumentSearchResource;
import com.coxandkings.travel.operations.resource.email.*;
import com.coxandkings.travel.operations.service.documentLibrary.DocumentLibraryService;
import com.coxandkings.travel.operations.service.template.TemplateLoaderService;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.SendMailCnK;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;

import javax.jcr.RepositoryException;
import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmailListenerImpl implements EmailListener{


    @Autowired
    private SendMailCnK sendMailCnK ;

    //temporary folder
    @Value("${email.temp-folder}")
    private String UPLOADED_FOLDER ;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private TemplateLoaderService templateLoaderService;

    @Autowired
    DocumentLibraryService documentLibraryService ;
    @Async
    @Override
    public void processEmail(EmailWithBodyResource resource) throws SQLException, OperationException, MessagingException {

        if( resource.getPriority().toString().toUpperCase().equalsIgnoreCase("NORMAL")){
            Email email = new Email();

            List<FileAttachment> fileAttachments=new ArrayList<>();
            CopyUtils.copy(resource, email);

            if(resource.getCommunicationTagResource() != null) {
                CommunicationTags communicationTags = new CommunicationTags();
                communicationTags.setBaseCommunication(email);
                CopyUtils.copy(resource.getCommunicationTagResource(), communicationTags);
                email.setCommunicationTags(communicationTags);
            }

            if(resource.getFileAttachments() != null && resource.getFileAttachments().size() > 0) {
                for (FileAttachmentResource fileAttachmentResource : resource.getFileAttachments()) {
                    FileAttachment fileAttachment = new FileAttachment();
                    Blob blob = new javax.sql.rowset.serial.SerialBlob(fileAttachmentResource.getContent());
                    fileAttachment.setContent(blob);
                    fileAttachment.setEmail(email);
                    fileAttachment.setFilename(fileAttachmentResource.getFilename());
                    fileAttachments.add(fileAttachment);
                }
                email.setFileAttachments(fileAttachments);
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

                for(FileAttachmentResource fileAttachmentResource: resource.getFileAttachments()){
                    String fileName = UPLOADED_FOLDER+ "\\"+fileAttachmentResource.getFilename();
                    writeBytesToFileNio(fileAttachmentResource.getContent(),fileName);
                }
            }

            String to= null, cc = null, bcc = null;
            to = createToString(email.getRecipientList());
            if(email.getCcRecipientList() != null && email.getCcRecipientList().size() > 0)
                cc = createToString(email.getCcRecipientList());
            if(email.getBccRecipientList() != null && email.getBccRecipientList().size() > 0)
                bcc = createToString(email.getBccRecipientList());
            EmailResponse response;
            if(resource.getFileAttachments() != null && resource.getFileAttachments().size() > 0) {
                response = sendMailCnK.send(email.getSender(),to,cc,bcc ,email.getSubject(), email.getBody(), resource.getFileAttachments());
                for(FileAttachmentResource fileAttachmentResource: resource.getFileAttachments()){
                    String fileName = UPLOADED_FOLDER+ "\\"+fileAttachmentResource.getFilename();
                    deleteFileFromTheTemporaryFolder(fileName);
                }
            }
            else
                response = sendMailCnK.send(email.getSender(),to,cc,bcc,email.getSubject(),email.getBody());

            //Saving it into the DB
            Email emailDB =  emailRepository.saveOrUpdate(email);
            response.setEmailID(emailDB.getId());
        }
        else{
            EmailResponse response = new EmailResponse();
            response.setStatus("Error");
            response.setMesssage("The Priority should be either NORMAL or HIGH");

        }
    }

    @Async
    @Override
    public void processEmail(EmailUsingTemplateResource resource) throws SQLException, OperationException, IOException, JSONException, MessagingException {
        if( resource.getPriority().toString().toUpperCase().equalsIgnoreCase("NORMAL")){
            List<DynamicVariables> dynamicVariables = new ArrayList<>();
            DynamicVariables dynamicVariable = null;
            for (Map.Entry<String, String> entry : resource.getDynamicVariables().entrySet()) {
                dynamicVariable = new DynamicVariables();
                dynamicVariable.setName(entry.getKey());
                dynamicVariable.setValue(entry.getValue());
                dynamicVariables.add(dynamicVariable);
            }

            List<FileAttachment> fileAttachments=new ArrayList<>();
            String body = templateLoaderService.getEmailContent(resource.getTemplateInfo(), dynamicVariables);

            Email email = new Email();
            CopyUtils.copy(resource, email);
            email.setBody(body);
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

            if(resource.getCommunicationTagResource() != null) {
                CommunicationTags communicationTags = new CommunicationTags();
                communicationTags.setBaseCommunication(email);
                CopyUtils.copy(resource.getCommunicationTagResource(), communicationTags);
                email.setCommunicationTags(communicationTags);
            }
            if(resource.getFileAttachments() != null && resource.getFileAttachments().size() > 0) {
                for (FileAttachmentResource fileAttachmentResource : resource.getFileAttachments()) {
                    FileAttachment fileAttachment = new FileAttachment();
                    Blob blob = new javax.sql.rowset.serial.SerialBlob(fileAttachmentResource.getContent());
                    fileAttachment.setContent(blob);
                    fileAttachment.setEmail(email);
                    fileAttachment.setFilename(fileAttachmentResource.getFilename());
                    fileAttachments.add(fileAttachment);
                }
                email.setFileAttachments(fileAttachments);
                for (FileAttachmentResource fileAttachmentResource : resource.getFileAttachments()) {
                    String fileName = UPLOADED_FOLDER + "\\" + fileAttachmentResource.getFilename();
                    writeBytesToFileNio(fileAttachmentResource.getContent(), fileName);
                }
            }
            //sending email
            String to= null, cc = null, bcc = null;
            to = createToString(email.getRecipientList());
            if(email.getCcRecipientList() != null && email.getCcRecipientList().size() > 0)
                cc = createToString(email.getCcRecipientList());
            if(email.getBccRecipientList() != null && email.getBccRecipientList().size() > 0)
                bcc = createToString(email.getBccRecipientList());
            EmailResponse response;
            if(resource.getFileAttachments() != null && resource.getFileAttachments().size() > 0) {
                response = sendMailCnK.send(email.getSender(),to, cc,bcc,email.getSubject(), email.getBody(), resource.getFileAttachments());
                for(FileAttachmentResource fileAttachmentResource: resource.getFileAttachments()){
                    String fileName = UPLOADED_FOLDER+ "\\"+fileAttachmentResource.getFilename();
                    deleteFileFromTheTemporaryFolder(fileName);
                }
            }
            else
                response = sendMailCnK.send(email.getSender(),to,cc,bcc,email.getSubject(),email.getBody());

            //Saving it into the DB
            Email emailDB  = emailRepository.saveOrUpdate(email);
            response.setEmailID(emailDB.getId());
        }
    }

    @Async
    @Override
    public void processEmail(EmailUsingTemplateAndDocumentsResource resource) throws SQLException, OperationException, IOException, JSONException, MessagingException, RepositoryException {
        if( resource.getPriority().toString().toUpperCase().equalsIgnoreCase("NORMAL")) {
            List<DynamicVariables> dynamicVariables = new ArrayList<>();
            DynamicVariables dynamicVariable = null;
            for (Map.Entry<String, String> entry : resource.getDynamicVariables().entrySet()) {
                dynamicVariable = new DynamicVariables();
                dynamicVariable.setName(entry.getKey());
                dynamicVariable.setValue(entry.getValue());
                dynamicVariables.add(dynamicVariable);
            }

            List<FileAttachment> fileAttachments=new ArrayList<>();
            String body = templateLoaderService.getEmailContent(resource.getTemplateInfo(), dynamicVariables);
            Email email = new Email();
            CopyUtils.copy(resource, email);
            email.setBody(body);
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

            if(resource.getCommunicationTagResource() != null) {
                CommunicationTags communicationTags = new CommunicationTags();
                communicationTags.setBaseCommunication(email);
                CopyUtils.copy(resource.getCommunicationTagResource(), communicationTags);
                email.setCommunicationTags(communicationTags);
            }

            List<FileAttachmentResource> fileAttachmentResources = new ArrayList<>();

            if(resource.getFileAttachments() != null && resource.getFileAttachments().size() > 0) {
                for (FileAttachmentResource fileAttachmentResource : resource.getFileAttachments()) {
                    String fileName = UPLOADED_FOLDER + "\\" + fileAttachmentResource.getFilename();
                    writeBytesToFileNio(fileAttachmentResource.getContent(), fileName);
                    fileAttachmentResources.add(fileAttachmentResource);

                    FileAttachment fileAttachment = new FileAttachment();
                    Blob blob = new javax.sql.rowset.serial.SerialBlob(fileAttachmentResource.getContent());
                    fileAttachment.setContent(blob);
                    fileAttachment.setEmail(email);
                    fileAttachment.setFilename(fileAttachmentResource.getFilename());
                    fileAttachments.add(fileAttachment);

                }
            }

            List<DocumentSearchResource> documentSearchResources = new ArrayList<>();
            List<DocumentResource> documentResources = new ArrayList<>();
            if(resource.getDocumentReferenceIDs() != null && resource.getDocumentReferenceIDs().size() > 0) {

                for (String docRefID : resource.getDocumentReferenceIDs()) {
                    DocumentSearchResource documentSearchResource = new DocumentSearchResource();
                    documentSearchResource.setId(docRefID);
                    documentSearchResources.add(documentSearchResource);
                }
                //Adding all the Files from document Management Library and files added in the request to one object
                documentResources = documentLibraryService.getDocuments(documentSearchResources);
                for (DocumentResource documentResource : documentResources) {

                    //storing all the files in a temporary location
                    String fileName = UPLOADED_FOLDER + "\\" + documentResource.getFileName();
                    writeBytesToFileNio(documentResource.getByteArray(), fileName);

                    FileAttachmentResource fileAttachmentResource = new FileAttachmentResource();
                    fileAttachmentResource.setFilename(documentResource.getFileName());
                    fileAttachmentResource.setContent(documentResource.getByteArray());
                    fileAttachmentResources.add(fileAttachmentResource);

                    FileAttachment fileAttachment = new FileAttachment();
                    Blob blob = new javax.sql.rowset.serial.SerialBlob(fileAttachmentResource.getContent());
                    fileAttachment.setContent(blob);
                    fileAttachment.setEmail(email);
                    fileAttachment.setFilename(fileAttachmentResource.getFilename());
                    fileAttachments.add(fileAttachment);
                }
            }
            email.setFileAttachments(fileAttachments);

            //sending email
            String to= null, cc = null, bcc = null;
            to = createToString(email.getRecipientList());
            if(email.getCcRecipientList() != null && email.getCcRecipientList().size() > 0)
                cc = createToString(email.getCcRecipientList());
            if(email.getBccRecipientList() != null && email.getBccRecipientList().size() > 0)
                bcc = createToString(email.getBccRecipientList());
            EmailResponse response;
            if(fileAttachmentResources != null && fileAttachmentResources.size() > 0) {
                response = sendMailCnK.send(email.getSender(),to,cc,bcc, email.getSubject(), email.getBody(), fileAttachmentResources);
            }
            else
                response = sendMailCnK.send(email.getSender(),to,cc,bcc,email.getSubject(),email.getBody());

            //deleting all the files
            if(resource.getFileAttachments() != null && resource.getDocumentReferenceIDs().size() > 0){
                for(FileAttachmentResource fileAttachmentResource: resource.getFileAttachments()){
                    String fileName = UPLOADED_FOLDER+ "\\"+fileAttachmentResource.getFilename();
                    deleteFileFromTheTemporaryFolder(fileName);
                }
            }

            if(resource.getDocumentReferenceIDs() != null && resource.getDocumentReferenceIDs().size() > 0){
                for(DocumentResource documentResource : documentResources){
                    //storing all the files in a temporary location
                    String fileName = UPLOADED_FOLDER + "\\" + documentResource.getFileName();
                    deleteFileFromTheTemporaryFolder(fileName);
                }
            }
            //Saving it into the DB
            Email emailDB = emailRepository.saveOrUpdate(email);
            response.setEmailID(emailDB.getId());
        }
        else{
            EmailResponse response = new EmailResponse();
            response.setStatus("Error");
            response.setMesssage("The Priority should be either NORMAL or HIGH");
        }
    }

    @Async
    @Override
    public void processEmail(EmailUsingBodyAndDocumentsResource resource) throws SQLException, OperationException, IOException, MessagingException, RepositoryException {
        if( resource.getPriority().toString().toUpperCase().equalsIgnoreCase("NORMAL")){
            Email email = new Email();
            List<FileAttachment> fileAttachments=new ArrayList<>();
            CopyUtils.copy(resource, email);
            if(resource.getCommunicationTagResource() != null) {
                CommunicationTags communicationTags = new CommunicationTags();
                communicationTags.setBaseCommunication(email);
                CopyUtils.copy(resource.getCommunicationTagResource(), communicationTags);
                email.setCommunicationTags(communicationTags);
            }

            List<FileAttachmentResource> fileAttachmentResources = new ArrayList<>();

            if(resource.getFileAttachments() != null && resource.getFileAttachments().size() > 0 ) {
                for (FileAttachmentResource fileAttachmentResource : resource.getFileAttachments()) {
                    String fileName = UPLOADED_FOLDER + "\\" + fileAttachmentResource.getFilename();
                    writeBytesToFileNio(fileAttachmentResource.getContent(), fileName);
                    fileAttachmentResources.add(fileAttachmentResource);

                    FileAttachment fileAttachment = new FileAttachment();
                    Blob blob = new javax.sql.rowset.serial.SerialBlob(fileAttachmentResource.getContent());
                    fileAttachment.setContent(blob);
                    fileAttachment.setEmail(email);
                    fileAttachment.setFilename(fileAttachmentResource.getFilename());
                    fileAttachments.add(fileAttachment);
                }
            }

            List<DocumentSearchResource> documentSearchResources = new ArrayList<>();
            List<DocumentResource> documentResources = new ArrayList<>();
            if(resource.getDocumentReferenceIDs() != null && resource.getDocumentReferenceIDs().size() > 0 ) {

                for (String docRefID : resource.getDocumentReferenceIDs()) {
                    DocumentSearchResource documentSearchResource = new DocumentSearchResource();
                    documentSearchResource.setId(docRefID);
                    documentSearchResources.add(documentSearchResource);
                }
                //Adding all the Files from document Management Library and files added in the request to one object
                documentResources = documentLibraryService.getDocuments(documentSearchResources);
                for (DocumentResource documentResource : documentResources) {

                    //storing all the files in a temporary location
                    String fileName = UPLOADED_FOLDER + "\\" + documentResource.getFileName();
                    writeBytesToFileNio(documentResource.getByteArray(), fileName);

                    FileAttachmentResource fileAttachmentResource = new FileAttachmentResource();
                    fileAttachmentResource.setFilename(documentResource.getFileName());
                    fileAttachmentResource.setContent(documentResource.getByteArray());
                    fileAttachmentResources.add(fileAttachmentResource);

                    FileAttachment fileAttachment = new FileAttachment();
                    Blob blob = new javax.sql.rowset.serial.SerialBlob(fileAttachmentResource.getContent());
                    fileAttachment.setContent(blob);
                    fileAttachment.setEmail(email);
                    fileAttachment.setFilename(fileAttachmentResource.getFilename());
                    fileAttachments.add(fileAttachment);
                }
            }
            email.setFileAttachments(fileAttachments);

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

            //sending email
            String to= null, cc = null, bcc = null;
            to = createToString(email.getRecipientList());
            if(email.getCcRecipientList() != null && email.getCcRecipientList().size() > 0)
                cc = createToString(email.getCcRecipientList());
            if(email.getBccRecipientList() != null && email.getBccRecipientList().size() > 0)
                bcc = createToString(email.getBccRecipientList());
            EmailResponse response ;
            if(fileAttachmentResources != null && fileAttachmentResources.size() > 0)
                response = sendMailCnK.send(email.getSender(),to,cc,bcc, email.getSubject(), email.getBody(), fileAttachmentResources);
            else
                response = sendMailCnK.send(email.getSender(),to ,cc,bcc, email.getSubject() , email.getBody());

            if(resource.getFileAttachments() != null && resource.getFileAttachments().size() > 0){
                for(FileAttachmentResource fileAttachmentResource: resource.getFileAttachments()){
                    String fileName = UPLOADED_FOLDER+ "\\"+fileAttachmentResource.getFilename();
                    deleteFileFromTheTemporaryFolder(fileName);
                }
            }

            if(resource.getDocumentReferenceIDs() != null && resource.getDocumentReferenceIDs().size() > 0){
                for(DocumentResource documentResource : documentResources){
                    //storing all the files in a temporary location
                    String fileName = UPLOADED_FOLDER + "\\" + documentResource.getFileName();
                    deleteFileFromTheTemporaryFolder(fileName);
                }
            }

            //Saving it into the DB
            Email emailDB = emailRepository.saveOrUpdate(email);
            response.setEmailID(emailDB.getId());

        }
        else {
            EmailResponse response = new EmailResponse();
            response.setStatus("Error");
            response.setMesssage("The Priority should be either NORMAL or HIGH");

        }
    }


    private static void deleteFileFromTheTemporaryFolder(String file_path) {
        Path filePath = Paths.get(file_path);
        try {
            Files.delete(filePath);
        } catch(IOException ioException) {
            ioException.printStackTrace();
            //some error in deleting the file
        }
    }


    public String createToString (List<String> to){

        String toString = "" ;
        for(String recvEmail : to){
            toString+=recvEmail+",";
        }
        return toString;
    }

    public List<Path> getPaths(String location){
        Path path= Paths.get(location);
        final List<Path> files=new ArrayList<>();
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if(!attrs.isDirectory()){
                        files.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    private static void writeBytesToFileNio(byte[] bFile, String fileDest) {

        try {
            Path path = Paths.get(fileDest);
            Files.write(path, bFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
