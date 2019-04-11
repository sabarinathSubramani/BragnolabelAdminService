package org.Delhivery.models;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DelhiveryTrackingResponse {

@JsonProperty("ShipmentData")
private List<ShipmentDatum> shipmentData = null;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("ShipmentData")
public List<ShipmentDatum> getShipmentData() {
return shipmentData;
}

@JsonProperty("ShipmentData")
public void setShipmentData(List<ShipmentDatum> shipmentData) {
this.shipmentData = shipmentData;
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