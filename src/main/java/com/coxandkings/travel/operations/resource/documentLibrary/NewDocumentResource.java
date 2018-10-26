package com.coxandkings.travel.operations.resource.documentLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewDocumentResource {
    private String type;
    private String name;
    private String category;
    private String subCategory;
    private String description;
    private String clientId;
    private String extension;
    private String bookId;
    private String docName;
    private Map<String, String> additionalAttributes;
    private List<DocumentReferenceResource> documentReferenceResources;
    private String URL;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Map<String, String> getAdditionalAttributes() {
        if (additionalAttributes == null) {
            additionalAttributes = new HashMap<String, String>();
        }
        return additionalAttributes;
    }

    public void setAdditionalAttributes(Map<String, String> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public List<DocumentReferenceResource> getDocumentReferenceResources() {
        if (documentReferenceResources == null)
            documentReferenceResources = new ArrayList<>();
        return documentReferenceResources;
    }

    public void setDocumentReferenceResources(List<DocumentReferenceResource> documentReferenceResources) {
        this.documentReferenceResources = documentReferenceResources;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "NewDocumentResource{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", description='" + description + '\'' +
                ", clientId='" + clientId + '\'' +
                ", extension='" + extension + '\'' +
                ", bookId='" + bookId + '\'' +
                ", additionalAttributes=" + additionalAttributes +
                ", documentReferenceResources=" + documentReferenceResources +
                '}';
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}
}
