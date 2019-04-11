package org.LogisticsPartner;

import java.util.Map;

import lombok.Data;

@Data
public class ShipmentTrackingResponse extends LPClientResponse{
	
	private Map<String,Tracking> tracking;
	
	public static ShipmentTrackingResponse getResponseWithError(int code, String message) {
		ShipmentTrackingResponse resp = new ShipmentTrackingResponse();
		org.LogisticsPartner.Error err = new org.LogisticsPartner.Error();
		err.setMessage("unable to get tracking numbers for tracking.");
		err.setCode(500);
		resp.setError(err);
		return resp;
	}

}
