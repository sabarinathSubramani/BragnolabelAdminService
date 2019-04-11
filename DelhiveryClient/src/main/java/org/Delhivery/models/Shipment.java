package org.Delhivery.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Shipment {

@JsonProperty("Origin")
private String origin;
@JsonProperty("Status")
private Status status;
@JsonProperty("PickUpDate")
private String pickUpDate;
@JsonProperty("InvoiceAmount")
private String invoiceAmount;
@JsonProperty("OrderType")
private String orderType;
@JsonProperty("Destination")
private String destination;
@JsonProperty("Consignee")
private Consignee consignee;
@JsonProperty("ReferenceNo")
private String referenceNo;
@JsonProperty("ReturnedDate")
private String returnedDate;
@JsonProperty("DestRecieveDate")
private String destRecieveDate;
@JsonProperty("ReverseInTransit")
private String reverseInTransit;
@JsonProperty("OutDestinationDate")
private String outDestinationDate;
@JsonProperty("CODAmount")
private String cODAmount;
@JsonProperty("ChargedWeight")
private String chargedWeight;
@JsonProperty("OriginRecieveDate")
private String originRecieveDate;
@JsonProperty("Scans")
private List<Scan> scans = null;
@JsonProperty("SenderName")
private String senderName;
@JsonProperty("AWB")
private String aWB;
@JsonProperty("DispatchCount")
private String dispatchCount;
@JsonProperty("FirstAttemptDate")
private String firstAttemptDate;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("Origin")
public String getOrigin() {
return origin;
}

@JsonProperty("Origin")
public void setOrigin(String origin) {
this.origin = origin;
}

@JsonProperty("Status")
public Status getStatus() {
return status;
}

@JsonProperty("Status")
public void setStatus(Status status) {
this.status = status;
}

@JsonProperty("PickUpDate")
public String getPickUpDate() {
return pickUpDate;
}

@JsonProperty("PickUpDate")
public void setPickUpDate(String pickUpDate) {
this.pickUpDate = pickUpDate;
}

@JsonProperty("InvoiceAmount")
public String getInvoiceAmount() {
return invoiceAmount;
}

@JsonProperty("InvoiceAmount")
public void setInvoiceAmount(String invoiceAmount) {
this.invoiceAmount = invoiceAmount;
}

@JsonProperty("OrderType")
public String getOrderType() {
return orderType;
}

@JsonProperty("OrderType")
public void setOrderType(String orderType) {
this.orderType = orderType;
}

@JsonProperty("Destination")
public String getDestination() {
return destination;
}

@JsonProperty("Destination")
public void setDestination(String destination) {
this.destination = destination;
}

@JsonProperty("Consignee")
public Consignee getConsignee() {
return consignee;
}

@JsonProperty("Consignee")
public void setConsignee(Consignee consignee) {
this.consignee = consignee;
}

@JsonProperty("ReferenceNo")
public String getReferenceNo() {
return referenceNo;
}

@JsonProperty("ReferenceNo")
public void setReferenceNo(String referenceNo) {
this.referenceNo = referenceNo;
}

@JsonProperty("ReturnedDate")
public String getReturnedDate() {
return returnedDate;
}

@JsonProperty("ReturnedDate")
public void setReturnedDate(String returnedDate) {
this.returnedDate = returnedDate;
}

@JsonProperty("DestRecieveDate")
public String getDestRecieveDate() {
return destRecieveDate;
}

@JsonProperty("DestRecieveDate")
public void setDestRecieveDate(String destRecieveDate) {
this.destRecieveDate = destRecieveDate;
}

@JsonProperty("ReverseInTransit")
public String getReverseInTransit() {
return reverseInTransit;
}

@JsonProperty("ReverseInTransit")
public void setReverseInTransit(String reverseInTransit) {
this.reverseInTransit = reverseInTransit;
}

@JsonProperty("OutDestinationDate")
public String getOutDestinationDate() {
return outDestinationDate;
}

@JsonProperty("OutDestinationDate")
public void setOutDestinationDate(String outDestinationDate) {
this.outDestinationDate = outDestinationDate;
}

@JsonProperty("CODAmount")
public String getCODAmount() {
return cODAmount;
}

@JsonProperty("CODAmount")
public void setCODAmount(String cODAmount) {
this.cODAmount = cODAmount;
}

@JsonProperty("ChargedWeight")
public String getChargedWeight() {
return chargedWeight;
}

@JsonProperty("ChargedWeight")
public void setChargedWeight(String chargedWeight) {
this.chargedWeight = chargedWeight;
}

@JsonProperty("OriginRecieveDate")
public String getOriginRecieveDate() {
return originRecieveDate;
}

@JsonProperty("OriginRecieveDate")
public void setOriginRecieveDate(String originRecieveDate) {
this.originRecieveDate = originRecieveDate;
}

@JsonProperty("Scans")
public List<Scan> getScans() {
return scans;
}

@JsonProperty("Scans")
public void setScans(List<Scan> scans) {
this.scans = scans;
}

@JsonProperty("SenderName")
public String getSenderName() {
return senderName;
}

@JsonProperty("SenderName")
public void setSenderName(String senderName) {
this.senderName = senderName;
}

@JsonProperty("AWB")
public String getAWB() {
return aWB;
}

@JsonProperty("AWB")
public void setAWB(String aWB) {
this.aWB = aWB;
}

@JsonProperty("DispatchCount")
public String getDispatchCount() {
return dispatchCount;
}

@JsonProperty("DispatchCount")
public void setDispatchCount(String dispatchCount) {
this.dispatchCount = dispatchCount;
}

@JsonProperty("FirstAttemptDate")
public String getFirstAttemptDate() {
return firstAttemptDate;
}

@JsonProperty("FirstAttemptDate")
public void setFirstAttemptDate(String firstAttemptDate) {
this.firstAttemptDate = firstAttemptDate;
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