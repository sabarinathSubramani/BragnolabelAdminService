package org.LPIntegrator.service.models;

import org.LogisticsPartner.LP;
import lombok.Data;

@Data
public class UpdateTrackingRequest {
	
	private LP lp;
	private String[] trackingNumbers;
}
