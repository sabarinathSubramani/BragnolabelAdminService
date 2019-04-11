package org.Delhivery.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Scan {

@JsonProperty("ScanDetail")
private ScanDetail scanDetail;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("ScanDetail")
public ScanDetail getScanDetail() {
return scanDetail;
}

@JsonProperty("ScanDetail")
public void setScanDetail(ScanDetail scanDetail) {
this.scanDetail = scanDetail;
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