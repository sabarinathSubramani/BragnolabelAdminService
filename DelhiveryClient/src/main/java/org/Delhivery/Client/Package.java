package org.Delhivery.Client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Package {

@JsonProperty("status")
private String status;
@JsonProperty("client")
private String client;
@JsonProperty("sort_code")
private Object sortCode;
@JsonProperty("remarks")
private List<String> remarks = null;
@JsonProperty("waybill")
private String waybill;
@JsonProperty("cod_amount")
private Integer codAmount;
@JsonProperty("payment")
private String payment;
@JsonProperty("serviceable")
private Boolean serviceable;
@JsonProperty("refnum")
private String refnum;

}