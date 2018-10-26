package com.coxandkings.travel.operations.service.documentLibrary.impl;

import com.coxandkings.travel.operations.controller.coreBE.RetrieveBookingDetailsController;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.documentlibrary.NewDocument;
import com.coxandkings.travel.operations.repository.documentlibrary.DocumentLibraryRepository;
import com.coxandkings.travel.operations.resource.documentLibrary.*;
import com.coxandkings.travel.operations.service.documentLibrary.DocumentLibraryService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.systemlogin.MDMDataSource;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.modeshape.common.collection.Problems;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.RepositoryConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DocumentLibraryServiceImpl implements DocumentLibraryService {
	
	private static Logger logger = LogManager.getLogger(RetrieveBookingDetailsController.class);
	
	private static final String PATH = "/IMAGES";
	private Repository repository;
	private Workspace workspace;
	private Session session;
	private ModeShapeEngine engine;
	private QueryManager queryManager;
	private RepositoryConfiguration config;
	@Value("${document_library.update_booking_docs}")
	private String updateDocURL;
	@Value("${document_library.get_booking_docs}")
	private String getDocURL;
	@Value("${document_library.modeShapeConfigFileName}")
	private String repoName;

	@Value("${document-library.upload-asset}")
	private String uploadAssetURL;

	@Value("${document-library.get-asset}")
	private String getAssetURL;

	@Autowired
	private UserService userService;

	@Autowired
	private DocumentLibraryRepository documentLibraryRepository;

	@Autowired
	private MDMRestUtils mdmRestUtils;

	@Autowired
	@Qualifier(value = "mDMDataSource")
	private MDMDataSource mdmDataSource;

	@Autowired
	private RestUtils restUtils;

	@PostConstruct
	public void init() {
		try {
			this.engine = new ModeShapeEngine();
			this.engine.start();
			String repositoryName = null;

			System.out.println("ModeShape repository Name :  " + this.repoName);
			this.config = RepositoryConfiguration
					.read(DocumentLibraryServiceImpl.class.getClassLoader().getResource(repoName));
			Problems problems = config.validate();
			if (problems.hasErrors()) {
				System.err.println("Problems starting the engine.");
				System.err.println(problems);
			}
			repository = engine.deploy(config);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	public void destroy() {
		try {
			this.engine.shutdown().get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public DocumentReferenceResource create(MultipartFile file, NewDocumentResource newDocumentResource,
			InputStream inputStream) throws RepositoryException, OperationException {
		this.session = this.repository.login("otherWorkspace");
		Node rootNode = session.getRootNode();
		DocumentReferenceResource documentReferenceResource = new DocumentReferenceResource();
		Node nameNode = null;
		nameNode = rootNode.addNode(UUID.randomUUID().toString());
		if (newDocumentResource != null) {
			nameNode.setProperty("1", 1);
			nameNode.setProperty("category", newDocumentResource.getCategory());
			nameNode.setProperty("clientId", newDocumentResource.getClientId());
			nameNode.setProperty("name", newDocumentResource.getName());
			nameNode.setProperty("subCategory", newDocumentResource.getSubCategory());
			nameNode.setProperty("extension", newDocumentResource.getExtension());
			nameNode.setProperty("description", newDocumentResource.getDescription());
			nameNode.setProperty("type", newDocumentResource.getType());
			nameNode.setProperty("bookId", newDocumentResource.getBookId());
			nameNode.setProperty("URL", newDocumentResource.getURL() + "/");
			if (file != null)
				nameNode.setProperty("docName", file.getOriginalFilename());
			if (newDocumentResource.getAdditionalAttributes() != null) {
				for (Map.Entry<String, String> additionalAttribute : newDocumentResource.getAdditionalAttributes()
						.entrySet()) {
					nameNode.setProperty(additionalAttribute.getKey(), additionalAttribute.getValue());
				}
			}
		}

		Node content = nameNode.addNode("jcr:content", "nt:resource");
		Binary binary = null;
		try {
			if (file == null) {

				binary = session.getValueFactory().createBinary(inputStream);
			} else {
				binary = session.getValueFactory().createBinary(file.getInputStream());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			content.setProperty("jcr:data", binary);
			session.save();
			documentReferenceResource.setId(nameNode.getIdentifier());
			documentReferenceResource.setPath(nameNode.getPath());
			documentReferenceResource.setUrl(newDocumentResource.getURL() + "/");
		} finally {
			if (session != null) {
				session.logout();
			}
		}

		return documentReferenceResource;
	}

	@Override
	public DownloadableDocumentResource get(String docId, Integer docVersion)
			throws RepositoryException, OperationException {
		DownloadableDocumentResource downloadableDocument = null;
		try {
			this.session = this.repository.login("otherWorkspace");
			// Node rootNode = session.getRootNode();
			Node node = session.getNodeByIdentifier(docId);

			Node jcrNode = null;
			Integer c = 0;
			if (null != node) {
				NodeIterator it = node.getNodes();
				while (it.hasNext()) {
					jcrNode = it.nextNode();
					c++;
					if (docVersion != null && c == docVersion) {
						break;
					}

				}
			}
			if (docVersion != null && c != docVersion) {
				throw new OperationException("Requested version of document is not available");
			}
			downloadableDocument = new DownloadableDocumentResource();
			if (node.hasProperty("docName"))
				downloadableDocument.setFileName(node.getProperty("docName").getValue().getString());
			else {
				downloadableDocument.setFileName(node.getProperty("name").getValue().getString() + "."
						+ node.getProperty("extension").getValue().toString());
			}
			if (jcrNode == null) {
				jcrNode = node;
			}
			Binary content = jcrNode.getProperty("jcr:data").getBinary();
			downloadableDocument.setInputStream(content.getStream());

		} finally {
			if (session != null) {
				session.logout();
			}
		}
		return downloadableDocument;
	}

	private Integer getVersion(Node node) throws RepositoryException {
		Integer i = 1;
		if (null != node) {
			NodeIterator it = node.getNodes();
			while (it.hasNext()) {
				it.nextNode();
				i++;
			}
		}
		return i;
	}

	@Override
	public List<NewDocumentResource> search(DocumentSearchResource searchRequest) throws RepositoryException {
		// ToDo make generic search
		// String queryString = "SELECT * FROM [nt:unstructured] WHERE [category]
		// ='handover'";
		// SELECT * FROM [nt:unstructured] WHERE someProp = 'white dog'
		// AND someProp = 'black dog'
		/*
		 * String queryString = "SELECT * FROM [nt:unstructured] WHERE category='" +
		 * searchRequest.getCategory() + "' AND clientId='" +
		 * searchRequest.getClientId() + "'";
		 */
		String queryString = "";

		queryString = createQueryString(searchRequest);
		// queryString = "SELECT * FROM [nt:unstructured] WHERE 1=1";
		Query query = null;
		List<NewDocumentResource> docs = new ArrayList<>();
		try {
			session = repository.login("otherWorkspace");
			queryManager = session.getWorkspace().getQueryManager();
			query = queryManager.createQuery(queryString, Query.JCR_SQL2);
			QueryResult queryResult = query.execute();
			NodeIterator nodeIterator = queryResult.getNodes();
			Node doc = null;

			while (nodeIterator.hasNext()) {
				NewDocumentResource documentResource = new NewDocumentResource();
				doc = nodeIterator.nextNode();
				setAllProperties(doc, documentResource);
				if (doc != null) {
					Integer i = 0;
					NodeIterator it = doc.getNodes();
					while (it.hasNext()) {
						Node fileNode = it.nextNode();
						DocumentReferenceResource dr = new DocumentReferenceResource();
						dr.setId(fileNode.getIdentifier());
						dr.setVersion(++i);
						documentResource.getDocumentReferenceResources().add(dr);
					}
				}

				docs.add(documentResource);

			}
		} finally {
			if (session != null)
				session.logout();
		}

		return docs;
	}

	@Override
	public List<DocumentResource> getDocuments(List<DocumentSearchResource> searchResources)
			throws RepositoryException, IOException {
		List<DocumentResource> documentResources = new ArrayList<>();
		try {
			this.session = this.repository.login("otherWorkspace");
			// Node rootNode = session.getRootNode();
			Node node = null;
			Node jcrNode = null;
			DocumentResource documentResource = null;
			for (DocumentSearchResource ds : searchResources) {
				node = session.getNodeByIdentifier(ds.getId());
				jcrNode = null;
				if (null != node) {
					NodeIterator it = node.getNodes();
					while (it.hasNext()) {
						jcrNode = it.nextNode();

					}
				}
				if (jcrNode == null) {
					jcrNode = node;
				}
				documentResource = new DocumentResource();
				// node.getParent().getName();
				documentResource.setId(ds.getId());
				documentResource.setFileName(node.getProperty("docName").getValue().getString());
				Binary content = jcrNode.getProperty("jcr:data").getBinary();
				documentResource.setByteArray(getByteArrayFromInputStream(content.getStream()));
				documentResources.add(documentResource);
			}
		} finally {
			if (session != null) {
				session.logout();
			}
		}
		return documentResources;
	}

	@Override
	public List<JSONObject> getMetaData(List<DocumentSearchResource> searchResources) throws RepositoryException {
		List<JSONObject> resources = new ArrayList<>();

		try {
			this.session = this.repository.login("otherWorkspace");
			for (DocumentSearchResource doc : searchResources) {
				try {
					Node node = session.getNodeByIdentifier(doc.getId());
					JSONObject newDocumentResource = new JSONObject();
					Node jcrNode = null;
					int count = 0;

					PropertyIterator properties = node.getProperties();
					newDocumentResource.put("docId", doc.getId());
					if (properties != null) {
						while (properties.hasNext()) {
							Property property = properties.nextProperty();
							newDocumentResource.put(property.getName(), property.getValue().toString());

						}
					}
					resources.add(newDocumentResource);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return resources;
		} finally {
			if (session != null) {
				session.logout();
			}
		}

	}

	private String createQueryString(DocumentSearchResource searchRequest) {
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT * FROM [nt:unstructured] WHERE 1=1");
		if (!StringUtils.isEmpty(searchRequest.getName())) {
			appendFilter(sb, "name", searchRequest.getName());
		}
		if (!StringUtils.isEmpty(searchRequest.getCategory())) {
			appendFilter(sb, "category", searchRequest.getCategory());
		}
		if (!StringUtils.isEmpty(searchRequest.getSubCategory())) {
			appendFilter(sb, "subCategory", searchRequest.getSubCategory());
		}
		if (!StringUtils.isEmpty(searchRequest.getDescription())) {
			appendFilter(sb, "description", searchRequest.getDescription());
		}
		if (!StringUtils.isEmpty(searchRequest.getClientId())) {
			appendFilter(sb, "clientId", searchRequest.getClientId());
		}
		if (!StringUtils.isEmpty(searchRequest.getExtension())) {
			appendFilter(sb, "extension", searchRequest.getExtension());
		}
		if (!StringUtils.isEmpty(searchRequest.getType())) {
			appendFilter(sb, "type", searchRequest.getType());
		}
		return sb.toString();
	}

	private void appendFilter(StringBuilder sb, String fieldName, String fieldValue) {
		if (fieldValue != null && !fieldValue.trim().equalsIgnoreCase("")) {
			sb.append(" AND " + fieldName + "='" + fieldValue + "'");
		}
	}

	private void setAllProperties(Node doc, NewDocumentResource documentResource) throws RepositoryException {

		if (doc != null) {

			if (doc.hasProperty("category")) {
				Property categoryProp = doc.getProperty("category");
				documentResource.setCategory(categoryProp.getValue().getString());
			}

			if (doc.hasProperty("subCategory")) {
				Property subCategoryProp = doc.getProperty("subCategory");
				documentResource.setSubCategory(subCategoryProp.getValue().getString());
			}

			if (doc.hasProperty("description")) {
				Property descriptionProp = doc.getProperty("description");
				documentResource.setDescription(descriptionProp.getValue().getString());
			}

			if (doc.hasProperty("type")) {
				Property typeProp = doc.getProperty("type");
				documentResource.setType(typeProp.getValue().getString());
			}

			if (doc.hasProperty("clientId")) {
				Property clientIdProp = doc.getProperty("clientId");
				documentResource.setClientId(clientIdProp.getValue().getString());
			}
			if (doc.hasProperty("extension")) {
				Property extensionProp = doc.getProperty("extension");
				documentResource.setExtension(extensionProp.getValue().getString());
			}
			if (doc.hasProperty("name")) {
				Property nameProp = doc.getProperty("name");
				documentResource.setName(nameProp.getValue().getString());
			}
		}
	}

	private byte[] getByteArrayFromInputStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		while (true) {
			int r = inputStream.read(buffer);
			if (r == -1)
				break;
			out.write(buffer, 0, r);
		}

		byte[] ret = out.toByteArray();
		return ret;
	}

	@Override
	public DocumentReferenceResource createV2(MultipartFile file, NewDocumentResource newDocumentResource,
			InputStream inputStream) throws OperationException {

		DocumentReferenceResource documentReferenceResource = new DocumentReferenceResource();
		
		if(file==null && inputStream!=null) {
			try {
				file = new MockMultipartFile(newDocumentResource.getName(), inputStream);
			} catch (Exception e) {
				logger.error("Error reading file from Input Stream",e.getMessage());
				e.printStackTrace();
				throw new OperationException("Error reading file from Input Stream");
			}
			finally {
				if(inputStream!=null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						logger.warn("Could not close Input Stream");
						e.printStackTrace();
					}
				}
			}
		}
		JSONObject response = uploadFile(file);

		NewDocument doc = new NewDocument();
		String id= response.getString("_id");
		String extention = response.getString("extension");
		
		String url = newDocumentResource.getURL();
		url= url.substring(0,url.lastIndexOf("/"));
		url=url+"/get/"+id;
		
		doc.setId(id);

		// doc.setVersion(version);
		// doc.setPath(path);
		if (newDocumentResource != null) {
			doc.setCategory(newDocumentResource.getCategory());
			doc.setClientId(newDocumentResource.getClientId());
			doc.setName(newDocumentResource.getName());
			doc.setSubCategory(newDocumentResource.getSubCategory());
			doc.setExtension(extention);
			doc.setDescription(newDocumentResource.getDescription());
			doc.setType(newDocumentResource.getType());
			doc.setBookId(newDocumentResource.getBookId());
			doc.setUrl(url);
			doc.setDocName(file.getOriginalFilename());
			doc.setAdditionalAttributes(newDocumentResource.getAdditionalAttributes());
		}

		documentLibraryRepository.saveOrUpdate(doc);
		documentReferenceResource.setId(id);
		// documentReferenceResource.setPath(nameNode.getPath());
		documentReferenceResource.setUrl(url);
		return documentReferenceResource;
	}

	private JSONObject uploadFile(MultipartFile file) throws OperationException {

		MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();

		File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
				+ file.getOriginalFilename());
		try {
			file.transferTo(tmpFile);
		} catch (Exception e) {
			logger.error("Could not write to Temporary File",e.getMessage());
			e.printStackTrace();
			throw new OperationException("Could not write to Temporary File");
		}

		multipartRequest.add("file", new FileSystemResource(tmpFile));

		URI builder = UriComponentsBuilder.fromUriString(uploadAssetURL).build().encode().toUri();
		JSONObject response = null;
		try {
		/*	HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
			String token = mdmDataSource.getToken().getToken();
			httpHeaders.add("Authorization", token);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, httpHeaders);

			RestTemplate restTemplate = RestUtils.getTemplate();
*/
			/*
			 * FormHttpMessageConverter formHttpMessageConverter = new
			 * FormHttpMessageConverter(); formHttpMessageConverter.addPartConverter(new
			 * MappingJackson2HttpMessageConverter());
			 * formHttpMessageConverter.addPartConverter(new
			 * ResourceHttpMessageConverter());
			 * formHttpMessageConverter.addPartConverter(new
			 * ByteArrayHttpMessageConverter());
			 * formHttpMessageConverter.addPartConverter(new StringHttpMessageConverter());
			 * restTemplate.getMessageConverters().add(formHttpMessageConverter);
			 */

			//ResponseEntity<String> responseEntity = restTemplate.exchange(builder, HttpMethod.POST, requestEntity, String.class);
			ResponseEntity<String> responseEntity = mdmRestUtils.exchange(builder, HttpMethod.POST, multipartRequest,String.class,MediaType.MULTIPART_FORM_DATA);
			response = new JSONObject(responseEntity.getBody());
			
		} catch (Exception e) {
			logger.error("Error Uploading File to MDM",e.getMessage());
			e.printStackTrace();
			throw new OperationException("Error Uploading File to MDM");
		}
		finally {
			tmpFile.delete();
		}

		return response;
	}

	@Override
	public DownloadableDocumentResource getV2(String docId, Integer docVersion) throws OperationException {
		
		NewDocument doc=documentLibraryRepository.getById(docId);
		if(doc==null) {
			throw new OperationException("Document not found for Id: "+docId);
		}
		
		/*HttpHeaders httpHeaders = new HttpHeaders();
		
		String token = mdmDataSource.getToken().getToken();
		httpHeaders.add("Authorization", token);
		httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

	    HttpEntity<String> requestEntity = new HttpEntity<String>(httpHeaders);

		

		RestTemplate restTemplate = RestUtils.getTemplate();

		
		 FormHttpMessageConverter formHttpMessageConverter = new
		  FormHttpMessageConverter(); formHttpMessageConverter.addPartConverter(new
		  MappingJackson2HttpMessageConverter());
		  formHttpMessageConverter.addPartConverter(new
		  ResourceHttpMessageConverter());
		  formHttpMessageConverter.addPartConverter(new ByteArrayHttpMessageConverter());
		 formHttpMessageConverter.addPartConverter(new StringHttpMessageConverter());
		  restTemplate.getMessageConverters().add(formHttpMessageConverter);
		  restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
		  URI builder = UriComponentsBuilder.fromUriString(getAssetURL).path(docId).build().encode().toUri();
		  MultipartFile file=null;
		  InputStream is= null;
		  try {
			  ResponseEntity<byte[]> responseEntity = restTemplate.exchange(builder, HttpMethod.GET, requestEntity, byte[].class);
			  if (responseEntity.getStatusCode() == HttpStatus.OK) {
			      file = new MockMultipartFile(doc.getDocName(),responseEntity.getBody());
			      is=file.getInputStream();
				 // Files.write(Paths.get("D://google.png"), responseEntity.getBody());
			    }
		  }
		catch(Exception e) {
			e.printStackTrace();
		}
		*/
		
		InputStream is = getDocumentFromMetadata(doc);
		
		DownloadableDocumentResource downloadableDocument = new DownloadableDocumentResource();
		downloadableDocument.setFileName(doc.getDocName());
		downloadableDocument.setExtension(doc.getExtension());
		downloadableDocument.setInputStream(is);
		return downloadableDocument;
	}

	@Override
	public List<NewDocumentResource> searchV2(DocumentSearchResource searchRequest) throws OperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentResource> getDocumentsV2(List<DocumentSearchResource> searchResources)
			throws OperationException {
		List<DocumentResource> documentResources = new ArrayList<>();
	    DocumentResource documentResource = null;
			for (DocumentSearchResource ds : searchResources) {
				String docId=ds.getId();
				
				NewDocument docMetadata= documentLibraryRepository.getById(docId);
				if(docMetadata==null) {
					logger.error("Error fetching Document with Id: "+docId);
					continue;
				}
				documentResource = new DocumentResource();
				documentResource.setId(docId);
				documentResource.setFileName(docMetadata.getDocName());
				
				InputStream is = getDocumentFromMetadata(docMetadata);
				
				try {
					documentResource.setByteArray(getByteArrayFromInputStream(is));
				} catch (IOException e) {
					logger.error("Error fetching Document with Id: "+docId);
					e.printStackTrace();
					continue;
				}
				finally {
					if(is!=null) {
						try {
							is.close();
						} catch (IOException e) {
							logger.warn("Could not close Input Stream");
							e.printStackTrace();
						}
					}
				}
				documentResources.add(documentResource);
			}
			return documentResources;
		} 
		
	@Override
	public List<JSONObject> getMetaDataV2(List<DocumentSearchResource> searchResources) throws OperationException {
		List<JSONObject> resources = new ArrayList<>();

		for (DocumentSearchResource doc : searchResources) {
			
			NewDocument docMetadata= documentLibraryRepository.getById(doc.getId());
			if(docMetadata==null) {
				//log exception
				continue;
			}
					JSONObject newDocumentResource = new JSONObject();
				
					newDocumentResource.put("docId", doc.getId());
					  
			        if (!StringUtils.isEmpty(docMetadata.getBookId()))
			        	newDocumentResource.put("bookId",docMetadata.getBookId());
			        
			        if (!StringUtils.isEmpty(docMetadata.getCategory()))
			        	newDocumentResource.put("category",docMetadata.getCategory());
			        
			        if (!StringUtils.isEmpty(docMetadata.getClientId()))
			        	newDocumentResource.put("clientId",docMetadata.getClientId());
			        
			        if (!StringUtils.isEmpty(docMetadata.getDescription()))
			        	newDocumentResource.put("description",docMetadata.getDescription());
			        
			        if (!StringUtils.isEmpty(docMetadata.getExtension()))
			        	newDocumentResource.put("extension",docMetadata.getExtension());
			        
			        if (!StringUtils.isEmpty(docMetadata.getName()))
			        	newDocumentResource.put("name",docMetadata.getName());
			        
			        if (!StringUtils.isEmpty(docMetadata.getPath()))
			        	newDocumentResource.put("path",docMetadata.getPath());
			        
			        if (!StringUtils.isEmpty(docMetadata.getSubCategory()))
			        	newDocumentResource.put("subCategory",docMetadata.getSubCategory());
			        
			        if (!StringUtils.isEmpty(docMetadata.getType()))
			        	newDocumentResource.put("type",docMetadata.getType());
			        
			        if (!StringUtils.isEmpty(docMetadata.getUrl()))
			        	newDocumentResource.put("url",docMetadata.getUrl());
			        
			        if (!StringUtils.isEmpty(docMetadata.getVersion()))
			        	newDocumentResource.put("version",docMetadata.getVersion());
			        
			        resources.add(newDocumentResource);
		}
							
			return resources;
		} 
	
	
	private InputStream getDocumentFromMetadata(NewDocument docMetadata) throws OperationException {
		
		
		String docId=docMetadata.getId();
		
		HttpHeaders httpHeaders = new HttpHeaders();
		
		String token = mdmDataSource.getToken().getToken();
		httpHeaders.add("Authorization", token);
		httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

	    HttpEntity<String> requestEntity = new HttpEntity<String>(httpHeaders);

		

		RestTemplate restTemplate = RestUtils.getTemplate();

		
		 FormHttpMessageConverter formHttpMessageConverter = new
		  FormHttpMessageConverter(); formHttpMessageConverter.addPartConverter(new
		  MappingJackson2HttpMessageConverter());
		  formHttpMessageConverter.addPartConverter(new
		  ResourceHttpMessageConverter());
		  formHttpMessageConverter.addPartConverter(new ByteArrayHttpMessageConverter());
		 formHttpMessageConverter.addPartConverter(new StringHttpMessageConverter());
		  restTemplate.getMessageConverters().add(formHttpMessageConverter);
		  restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
		  URI builder = UriComponentsBuilder.fromUriString(getAssetURL).path(docId).build().encode().toUri();
		  MultipartFile file=null;
		  InputStream is= null;
		  try {
			  ResponseEntity<byte[]> responseEntity = restTemplate.exchange(builder, HttpMethod.GET, requestEntity, byte[].class);
			  if (responseEntity.getStatusCode() == HttpStatus.OK) {
			      file = new MockMultipartFile(docMetadata.getDocName(),responseEntity.getBody());
			      is=file.getInputStream();
				 // Files.write(Paths.get("D://google.png"), responseEntity.getBody());
			    }
		  }
		catch(Exception e) {
			logger.error("Could not get Document from MDM for Id:"+docId,e.getMessage());
			throw new OperationException("Could not get Document from MDM for Id:"+docId);
		}
		  return is;
	}
	
	

}
