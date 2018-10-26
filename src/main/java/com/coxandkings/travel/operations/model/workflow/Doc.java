package com.coxandkings.travel.operations.model.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

})
public class Doc implements Serializable {

	private Object newDoc;

	private Object oldDoc;

	public Object getNewDoc() {
		return newDoc;
	}

	public void setNewDoc(Object newDoc) {
		this.newDoc = newDoc;
	}

	public Object getOldDoc() {
		return oldDoc;
	}

	public void setOldDoc(Object oldDoc) {
		this.oldDoc = oldDoc;
	}

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
