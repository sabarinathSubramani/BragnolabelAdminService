package org.LogisticsPartner;

import org.joda.time.DateTime;
import lombok.Data;

@Data
public class Status {
	private ShipmentStatus status;
	private String location;
	private DateTime statusDate;
}
