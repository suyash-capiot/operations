package com.coxandkings.travel.operations.model.documentlibrary;

import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.coxandkings.travel.operations.model.amendsuppliercommercial.JSONUserType;

@Entity
@TypeDefs({@TypeDef(name = "StringJsonObject", typeClass = JSONUserType.class)})
public class NewDocument {

		/*@Id
		@GeneratedValue(generator = "UUID")
    	@GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")*/
		@Column
		@Id
    	private String id;
		
		@Column
		private Integer version;
		
		@Column
		private String docName;
		
		@Column
	    private String path;
	    
	    @Column
	    private String url;
		@Column
	 	private String type;
		@Column
	    private String name;
	    @Column
	    private String category;
	    @Column
	    private String subCategory;
	    @Column
	    private String description;
	    @Column
	    private String clientId;
	    @Column
	    private String extension;
	    @Column
	    private String bookId;
	    @Column
	    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "java.util.HashMap")})
	    private Map<String, String> additionalAttributes;
	    
	    
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public Integer getVersion() {
			return version;
		}
		public void setVersion(Integer version) {
			this.version = version;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
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
		public String getBookId() {
			return bookId;
		}
		public void setBookId(String bookId) {
			this.bookId = bookId;
		}
		public Map<String, String> getAdditionalAttributes() {
			return additionalAttributes;
		}
		public void setAdditionalAttributes(Map<String, String> additionalAttributes) {
			this.additionalAttributes = additionalAttributes;
		}
		public String getDocName() {
			return docName;
		}
		public void setDocName(String docName) {
			this.docName = docName;
		}
		
	   
	    
}
