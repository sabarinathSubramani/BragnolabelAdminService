package org.LogisticsPartner;

import java.util.Set;
import lombok.Data;

@Data
public class ShipmentTrackingRequest extends LPClientRequest{
	
	private Set<String> trackingNumbers;
	private LP lp;
	
}
