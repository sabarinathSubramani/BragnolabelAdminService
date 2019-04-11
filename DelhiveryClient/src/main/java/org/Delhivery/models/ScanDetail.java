package org.Delhivery.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScanDetail {

@JsonProperty("ScanDateTime")
private String scanDateTime;
@JsonProperty("ScanType")
private String scanType;
@JsonProperty("Scan")
private String scan;
@JsonProperty("StatusDateTime")
private String statusDateTime;
@JsonProperty("ScannedLocation")
private String scannedLocation;
@JsonProperty("StatusCode")
private String statusCode;
@JsonProperty("Instructions")
private String instructions;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("ScanDateTime")
public String getScanDateTime() {
return scanDateTime;
}

@JsonProperty("ScanDateTime")
public void setScanDateTime(String scanDateTime) {
this.scanDateTime = scanDateTime;
}

@JsonProperty("ScanType")
public String getScanType() {
return scanType;
}

@JsonProperty("ScanType")
public void setScanType(String scanType) {
this.scanType = scanType;
}

@JsonProperty("Scan")
public String getScan() {
return scan;
}

@JsonProperty("Scan")
public void setScan(String scan) {
this.scan = scan;
}

@JsonProperty("StatusDateTime")
public String getStatusDateTime() {
return statusDateTime;
}

@JsonProperty("StatusDateTime")
public void setStatusDateTime(String statusDateTime) {
this.statusDateTime = statusDateTime;
}

@JsonProperty("ScannedLocation")
public String getScannedLocation() {
return scannedLocation;
}

@JsonProperty("ScannedLocation")
public void setScannedLocation(String scannedLocation) {
this.scannedLocation = scannedLocation;
}

@JsonProperty("StatusCode")
public String getStatusCode() {
return statusCode;
}

@JsonProperty("StatusCode")
public void setStatusCode(String statusCode) {
this.statusCode = statusCode;
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