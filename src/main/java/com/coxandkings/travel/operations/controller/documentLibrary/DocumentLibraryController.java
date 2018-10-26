package com.coxandkings.travel.operations.controller.documentLibrary;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.documentLibrary.*;
import com.coxandkings.travel.operations.service.documentLibrary.DocumentLibraryService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.HtmlToPdfUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping("/documentLibrary")
@CrossOrigin("*")
public class DocumentLibraryController {

    @Autowired
    private DocumentLibraryService documentLibraryService;

    private static Logger logger = LogManager.getLogger(DocumentLibraryController.class);
    @Autowired
    private HtmlToPdfUtil htmlToPdfUtil;

    @PostMapping("/v1/create")
    public HttpEntity<DocumentReferenceResource> create(@RequestParam(value = "file", required = true) MultipartFile file,
                                                        @RequestParam(value = "type", required = true) String type,
                                                        @RequestParam(value = "name", required = true) String name,
                                                        @RequestParam(value = "category", required = true) String category,
                                                        @RequestParam(value = "subCategory", required = false) String subCategory,
                                                        @RequestParam(value = "description", required = false) String description,
                                                        @RequestParam(value = "clientId", required = false) String clientId,
                                                        @RequestParam(value = "bookId", required = false) String bookId,

                                                        HttpServletRequest request) throws RepositoryException, OperationException {

        NewDocumentResource newDocumentResource = new DocumentSearchResource();
        newDocumentResource.setType(type);
        newDocumentResource.setName(name);
        newDocumentResource.setCategory(category);
        newDocumentResource.setSubCategory(subCategory);
        newDocumentResource.setClientId(clientId);
        newDocumentResource.setDescription(description);
        newDocumentResource.setBookId(bookId);
        newDocumentResource.setExtension(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
        newDocumentResource.setURL(request.getRequestURL().toString());
        Enumeration<String> att = request.getParameterNames();
        while (att.hasMoreElements()) {
            String attribute = att.nextElement();
            switch (attribute) {
                case "file":
                case "type":
                case "name":
                case "category":
                case "subCategory":
                case "description":
                case "clientId":
                case "bookId":
                    break;
                default:
                    newDocumentResource.getAdditionalAttributes().put(attribute, request.getParameter(attribute));
            }


        }
        return new ResponseEntity<>(documentLibraryService.create(file, newDocumentResource, null), HttpStatus.CREATED);
    }
    
    @PostMapping("/v2/create")
    public HttpEntity<DocumentReferenceResource> createV2(@RequestParam(value = "file", required = true) MultipartFile file,
                                                        @RequestParam(value = "type", required = true) String type,
                                                        @RequestParam(value = "name", required = true) String name,
                                                        @RequestParam(value = "category", required = true) String category,
                                                        @RequestParam(value = "subCategory", required = false) String subCategory,
                                                        @RequestParam(value = "description", required = false) String description,
                                                        @RequestParam(value = "clientId", required = false) String clientId,
                                                        @RequestParam(value = "bookId", required = false) String bookId,

                                                        HttpServletRequest request) throws RepositoryException, OperationException {

        NewDocumentResource newDocumentResource = new DocumentSearchResource();
        newDocumentResource.setType(type);
        newDocumentResource.setName(name);
        newDocumentResource.setCategory(category);
        newDocumentResource.setSubCategory(subCategory);
        newDocumentResource.setClientId(clientId);
        newDocumentResource.setDescription(description);
        newDocumentResource.setBookId(bookId);
        newDocumentResource.setURL(request.getRequestURL().toString());
        Enumeration<String> att = request.getParameterNames();
        while (att.hasMoreElements()) {
            String attribute = att.nextElement();
            switch (attribute) {
                case "file":
                case "type":
                case "name":
                case "category":
                case "subCategory":
                case "description":
                case "clientId":
                case "bookId":
                    break;
                default:
                    newDocumentResource.getAdditionalAttributes().put(attribute, request.getParameter(attribute));
            }


        }
        return new ResponseEntity<>(documentLibraryService.createV2(file, newDocumentResource, null), HttpStatus.CREATED);
    }

    @GetMapping("/v1/get")
    public void get(@RequestParam(value = "id") String docId, @RequestParam(value = "version", required = false) Integer docVersion, HttpServletResponse response) throws RepositoryException, IOException, OperationException {

        DownloadableDocumentResource dd = documentLibraryService.get(docId, docVersion);
        String extension = dd.getFileName().substring(dd.getFileName().lastIndexOf(".") + 1);


        try {

            if (null != extension && extension.equalsIgnoreCase("txt")) {
                response.setContentType("application/text");
            } else if (null != extension && extension.equalsIgnoreCase("pdf")) {
                response.setContentType("application/pdf");
            } else if (null != extension && extension.equalsIgnoreCase("csv")) {
                response.setContentType("text/csv");
            } else if (null != extension && (extension.equalsIgnoreCase("xls")
            )) {
                response.setContentType("application/vnd.ms-excel");
            } else if (null != extension && (
                    extension.equalsIgnoreCase("xlsx"))) {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            }
            //response.setContentLengthLong(fileSize);
            response.setHeader("Content-Disposition", "attachment;filename=" + dd.getFileName());
            getOutPutStreamFromInputStream(dd.getInputStream(), response.getOutputStream());

        } catch (Exception ex) {
            logger.error("Error occurred while fetching document: " + docId, ex);
            throw new OperationException(Constants.OPS_ERR_41301, docId);
        } finally {

            if (response.getOutputStream() != null) {
                response.getOutputStream().close();
            }
        }
        response.flushBuffer();
    }
    
    
    @GetMapping("/v2/get")
    public void getV2(@RequestParam(value = "id") String docId, @RequestParam(value = "version", required = false) Integer docVersion, HttpServletResponse response) throws RepositoryException, IOException, OperationException {

        DownloadableDocumentResource dd = documentLibraryService.getV2(docId, docVersion);
        String extension = dd.getExtension();


        try {

            if (null != extension && extension.equalsIgnoreCase("txt")) {
                response.setContentType("application/text");
            } else if (null != extension && extension.equalsIgnoreCase("pdf")) {
                response.setContentType("application/pdf");
            } else if (null != extension && extension.equalsIgnoreCase("csv")) {
                response.setContentType("text/csv");
            } else if (null != extension && (extension.equalsIgnoreCase("xls")
            )) {
                response.setContentType("application/vnd.ms-excel");
            } else if (null != extension && (
                    extension.equalsIgnoreCase("xlsx"))) {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            }
            //response.setContentLengthLong(fileSize);
            response.setHeader("Content-Disposition", "attachment;filename=" + dd.getFileName());
            getOutPutStreamFromInputStream(dd.getInputStream(), response.getOutputStream());

        } catch (Exception ex) {
            logger.error("Error occurred while fetching document: " + docId, ex);
            throw new OperationException(Constants.OPS_ERR_41301, docId);
        } finally {

            if (response.getOutputStream() != null) {
                response.getOutputStream().close();
            }
            if(dd.getInputStream()!=null) {
            	try {
            		dd.getInputStream().close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}            	
            }
        }
        response.flushBuffer();
    }

    @PostMapping("/v1/search")
    public HttpEntity<List<NewDocumentResource>> search(@RequestBody DocumentSearchResource searchRequest) throws OperationException {
        try {
            return new ResponseEntity<>(documentLibraryService.search(searchRequest), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred while searching documents", ex);
            throw new OperationException(Constants.OPS_ERR_41303);
        }
    }

    @PostMapping("/v1/documents")
    public HttpEntity<List<DocumentResource>> search(@RequestBody List<DocumentSearchResource> searchResources) throws IOException, OperationException {
        try {
            return new ResponseEntity<>(documentLibraryService.getDocuments(searchResources), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred while searching documents", ex);
            throw new OperationException(Constants.OPS_ERR_41303);
        }
    }
    
    @PostMapping("/v2/documents")
    public HttpEntity<List<DocumentResource>> getDocumentsV2(@RequestBody List<DocumentSearchResource> searchResources) throws IOException, OperationException {
        try {
            return new ResponseEntity<>(documentLibraryService.getDocumentsV2(searchResources), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred while searching documents", ex);
            throw new OperationException(Constants.OPS_ERR_41303);
        }
    }

    @PostMapping("/v1/metaData")
    public HttpEntity<List<JSONObject>> getMetaData(@RequestBody List<DocumentSearchResource> searchResources) throws IOException, OperationException {
        try {
            return new ResponseEntity<>(documentLibraryService.getMetaData(searchResources), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred while fetching doc meta-data", ex);
            throw new OperationException(Constants.OPS_ERR_41302);
        }
    }
    
    @PostMapping("/v2/metaData")
    public HttpEntity<List<JSONObject>> getMetaDataV2(@RequestBody List<DocumentSearchResource> searchResources) throws IOException, OperationException {
        try {
            return new ResponseEntity<>(documentLibraryService.getMetaDataV2(searchResources), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred while fetching doc meta-data", ex);
            throw new OperationException(Constants.OPS_ERR_41302);
        }
    }

    @PostMapping("/v1/htmlToPDF")
    public HttpEntity<String> convertHtmlToPDF(@RequestParam String data, @RequestParam("fileName") String fileName) throws OperationException {
        File file = htmlToPdfUtil.convertHtmlToPdf(data, fileName);
        NewDocumentResource newDocumentResource = new NewDocumentResource();
        newDocumentResource.setDocName(fileName);
        newDocumentResource.setExtension("pdf");
        InputStream input = null;
        DocumentReferenceResource documentReferenceResource = null;
        try {
            input = new FileInputStream(file);
            try {
                documentReferenceResource = documentLibraryService.create(null, newDocumentResource, input);
            } catch (RepositoryException e) {
                throw new OperationException("Error while Uploading File");
            }
        } catch (FileNotFoundException e) {
            throw new OperationException("Error while Uploading File");
        }

        file.delete();


        return new ResponseEntity<>(documentReferenceResource.getId(), HttpStatus.OK);
    }

    private long getOutPutStreamFromInputStream(InputStream is, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        long count = 0;
        int n = 0;
        while (-1 != (n = is.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }


}