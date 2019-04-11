package org.LPIntegrator.hibernate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import org.LogisticsPartner.ShipmentStatus;
import org.ShopifyInegration.models.OrderType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="shipment_tracking")
@Getter
@Setter
public class ShipmentTrackingEntity {

	
	@Column(name="id")
	private int id;
	
	@Id
	@Column(name="tracking_number")
	public String trackingNumber;
	
	@Column(name="shipment_status")
	@Enumerated(EnumType.STRING)
	private ShipmentStatus shipmentStatus;
	
	@Column(name="shipment_location")
	private String shipmentLocation;
	
	@Column(name="pickup_date")
	private Date pickupDate;
	
	@Column(name="status_date")
	private Date statusDate;
	
	@Column(name="shipment_type")
	@Enumerated(EnumType.STRING)
	private OrderType shipmentType;	
	
	@Column(name="lp_id")
	private int lpId;
}
