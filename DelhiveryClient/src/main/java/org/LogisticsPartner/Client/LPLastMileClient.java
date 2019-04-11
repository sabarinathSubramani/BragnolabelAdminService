package org.LogisticsPartner.Client;

import org.LogisticsPartner.CreatePackageRequest;
import org.LogisticsPartner.LPClientResponse;
import org.LogisticsPartner.ShipmentTrackingRequest;
import org.LogisticsPartner.ShipmentTrackingResponse;

public interface LPLastMileClient extends LPClient{

	public ShipmentTrackingResponse trackPackage(ShipmentTrackingRequest shipmentTrackingRequest);
	
	public LPClientResponse createPackage(CreatePackageRequest createPackageRequest);
	
}
