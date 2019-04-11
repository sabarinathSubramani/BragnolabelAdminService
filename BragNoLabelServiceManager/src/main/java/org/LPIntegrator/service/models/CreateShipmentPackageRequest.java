package org.LPIntegrator.service.models;

import org.LogisticsPartner.LP;
import lombok.Data;

@Data
public class CreateShipmentPackageRequest {

	private String[] orderIds;
	private LP lp;
}
