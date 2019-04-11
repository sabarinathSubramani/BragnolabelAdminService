package org.LPIntegrator.service.models;

import org.LogisticsPartner.LP;
import lombok.Data;

@Data
public class UpdateCODPaymentStatusRequest {

	private String[] orderIds;
	private LP lp;
}
