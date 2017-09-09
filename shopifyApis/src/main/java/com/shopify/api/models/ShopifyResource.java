package com.shopify.api.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class ShopifyResource {
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	private HashSet<String> dirtyKeys = new HashSet<String>();

	@JsonProperty("id")
	public long getId() {
		Integer value = (Integer)getAttribute("id");
		return value != null ? value : 0;
	}
	

	@JsonProperty("id")
	public void setId(long id) {
		setAttribute("id", id);
	}
	
	@JsonProperty("test")
	public boolean isTest() {
		boolean value = (Boolean)getAttribute("test");
		return value;
	}
	
	@JsonProperty("test")
	public void setTest(boolean test) {
		setAttribute("test", test);
	}

	@JsonProperty("created_at")
	public String getCreatedAt() {
		return (String)getAttribute("created_at");
	}

	@JsonProperty("created_at")
	public void setCreatedAt(String createdAt) {
		setAttribute("created_at", createdAt);
	}

	@JsonProperty("updated_at")
	public String getUpdatedAt() {
		return (String) getAttribute("updated_at");
	}

	@JsonProperty("updated_at")
	public void setUpdatedAt(String updatedAt) {
		setAttribute("updated_at", updatedAt);
	}

	@JsonIgnore
	public String getFieldName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.createObjectNode();
		for (String key : determineKeys()) {
			((ObjectNode)root).put(key, mapper.valueToTree(attributes.get(key)));
		}
		return root.toString();
	}

	protected Set<String> determineKeys(){
		return isDirty() ? dirtyKeys : attributes.keySet();
	}

	protected void setAttribute(String attributeName, Object attributeValue) {
		Object currentValue = attributes.get(attributeName);
		if(currentValue == null || !currentValue.equals(attributeValue)) {
			dirtyKeys.add(attributeName);
		}
		attributes.put(attributeName, attributeValue);
	}

	protected Object getAttribute(String attributeName) {
		return attributes.get(attributeName);
	}

	@JsonIgnore
	public boolean isDirty() {
		return !dirtyKeys.isEmpty();
	}

	@JsonIgnore
	public void makeDirty() {
		dirtyKeys.addAll(attributes.keySet());
	}

	@JsonIgnore
	public void makeDirty(String attribute) {
		if(attributes.keySet().contains(attribute)) {
			dirtyKeys.add(attribute);
		}
	}

	public void clean() {
		dirtyKeys.clear();
	}
}
