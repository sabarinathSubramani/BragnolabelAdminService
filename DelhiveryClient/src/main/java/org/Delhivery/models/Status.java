package org.Delhivery.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Status {

@JsonProperty("Status")
private String status;
@JsonProperty("StatusLocation")
private String statusLocation;
@JsonProperty("StatusDateTime")
private String statusDateTime;
@JsonProperty("RecievedBy")
private String recievedBy;
@JsonProperty("StatusCode")
private String statusCode;
@JsonProperty("StatusType")
private String statusType;
@JsonProperty("Instructions")
private String instructions;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("Status")
public String getStatus() {
return status;
}

@JsonProperty("Status")
public void setStatus(String status) {
this.status = status;
}

@JsonProperty("StatusLocation")
public String getStatusLocation() {
return statusLocation;
}

@JsonProperty("StatusLocation")
public void setStatusLocation(String statusLocation) {
this.statusLocation = statusLocation;
}

@JsonProperty("StatusDateTime")
public String getStatusDateTime() {
return statusDateTime;
}

@JsonProperty("StatusDateTime")
public void setStatusDateTime(String statusDateTime) {
this.statusDateTime = statusDateTime;
}

@JsonProperty("RecievedBy")
public String getRecievedBy() {
return recievedBy;
}

@JsonProperty("RecievedBy")
public void setRecievedBy(String recievedBy) {
this.recievedBy = recievedBy;
}

@JsonProperty("StatusCode")
public String getStatusCode() {
return statusCode;
}

@JsonProperty("StatusCode")
public void setStatusCode(String statusCode) {
this.statusCode = statusCode;
}

@JsonProperty("StatusType")
public String getStatusType() {
return statusType;
}

@JsonProperty("StatusType")
public void setStatusType(String statusType) {
this.statusType = statusType;
}

@JsonProperty("Instructions")
public String getInstructions() {
return instructions;
}

@JsonProperty("Instructions")
public void setInstructions(String instructions) {
this.instructions = instructions;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}