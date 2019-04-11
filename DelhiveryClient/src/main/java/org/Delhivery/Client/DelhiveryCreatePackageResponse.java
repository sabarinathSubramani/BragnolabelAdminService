package org.Delhivery.Client;

import java.util.List;

import org.LogisticsPartner.LPClientResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DelhiveryCreatePackageResponse extends LPClientResponse {
	
	@JsonProperty("cash_pickups_count")
	private Integer cashPickupsCount;
	@JsonProperty("package_count")
	private Integer packageCount;
	@JsonProperty("upload_wbn")
	private String uploadWbn;
	@JsonProperty("replacement_count")
	private Integer replacementCount;
	@JsonProperty("pickups_count")
	private Integer pickupsCount;
	@JsonProperty("packages")
	private List<Package> packages = null;
	@JsonProperty("cash_pickups")
	private Integer cashPickups;
	@JsonProperty("cod_count")
	private Integer codCount;
	@JsonProperty("success")
	private Boolean success;
	@JsonProperty("prepaid_count")
	private Integer prepaidCount;
	@JsonProperty("cod_amount")
	private Integer codAmount;
	@JsonProperty("rmk")
	private String remarks;
	
}
