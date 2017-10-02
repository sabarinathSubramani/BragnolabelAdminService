package org.LPIntegrator.service.models;

import org.LPIntegrator.utils.LPIntegratorUtils;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Data;

@Data
public class GetOrdersRequest {

	private String orderIds;
	private DateTime createdAtMin;
	private DateTime createdAtMax;
	private String financialStatus;
	private String fulfilmentStatus;
	
	@JsonCreator
	public void setCreatedAtMin(String date){
		createdAtMin = LPIntegratorUtils.getShopifyOrderDateTime(date);
	}
	
	@JsonCreator
	public void setcreatedAtMax(String date){
		createdAtMin = LPIntegratorUtils.getShopifyOrderDateTime(date);
	}
}
