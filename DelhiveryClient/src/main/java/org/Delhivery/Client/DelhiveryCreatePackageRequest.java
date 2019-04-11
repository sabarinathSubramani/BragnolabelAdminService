package org.Delhivery.Client;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DelhiveryCreatePackageRequest {
	
	@JsonProperty("pickup_location")
	private PickupLocation pickupLocation;
	@JsonProperty("shipments")
	private List<Shipment> shipments = new ArrayList<Shipment>();

}