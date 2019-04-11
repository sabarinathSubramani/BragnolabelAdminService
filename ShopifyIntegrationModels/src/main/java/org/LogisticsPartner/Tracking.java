package org.LogisticsPartner;

import org.joda.time.DateTime;
import lombok.Data;

@Data
public class Tracking {
	
	private String waybillNumber;
	private String referenceNumber;
	private String origin;
	private String destination;
	private DateTime pickupDate;
	private OrderType orderType;
	private Double invoiceAmt;
	private Double codAmt;
	private Status currentStatus;

}
