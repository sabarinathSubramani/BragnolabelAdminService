package org.Delhivery.Transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import org.Delhivery.models.DelhiveryTrackingResponse;
import org.Delhivery.models.Shipment;
import org.Delhivery.models.ShipmentDatum;
import org.LogisticsPartner.OrderType;
import org.LogisticsPartner.ShipmentStatus;
import org.LogisticsPartner.ShipmentTrackingResponse;
import org.LogisticsPartner.Status;
import org.LogisticsPartner.Tracking;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TrackingResponseTransformer {

	public static Logger logger = Logger.getLogger(TrackingResponseTransformer.class);
	
	public static Function<DelhiveryTrackingResponse, ShipmentTrackingResponse> toTrackingResponse(){
		
		return dr -> {
			ShipmentTrackingResponse response = new ShipmentTrackingResponse();
			response.setError(new org.LogisticsPartner.Error());
			response.setTracking(new HashMap<String, Tracking>());
			for(ShipmentDatum shipment : dr.getShipmentData()) {
				Shipment data = shipment.getShipment();
				if(data == null)
					continue;
				Tracking tracking = new Tracking();
				tracking.setWaybillNumber(data.getAWB());
				tracking.setCodAmt(Double.valueOf(data.getCODAmount()));
				tracking.setDestination(data.getDestination());
				tracking.setInvoiceAmt(Double.valueOf(data.getInvoiceAmount()));
				if(data.getOrderType().equals("Pre-paid"))
					tracking.setOrderType(OrderType.PREPAID);
				else if(data.getOrderType().equals("COD"))
					tracking.setOrderType(OrderType.COD);
				else
					tracking.setOrderType(OrderType.INVALID);
				
				tracking.setOrigin(data.getOrigin());
				tracking.setPickupDate(toDateTime(data.getPickUpDate()));
				tracking.setReferenceNumber(data.getReferenceNo());
				
				Status status = new Status();
				status.setLocation(data.getStatus().getStatusLocation());
				status.setStatus(ShipmentStatus.getStatusFromCode(data.getStatus().getStatusType()));
				if(status.getStatus() == ShipmentStatus.UNKNOWN) {
					org.LogisticsPartner.Error error = response.getError();
					if(error == null)
						response.getError().setErrorMessages(new ArrayList<String>());
					error.setCode(204);
					error.getErrorMessages().add("\"unknown status type - \"+data.getStatus().getStatusType()+\" for tracking numebr - \"+data.getAWB()");
					logger.error("unknown status type - "+data.getStatus().getStatusType()+" for tracking numebr - "+data.getAWB());
				}
			
				status.setStatusDate(toDateTime(data.getStatus().getStatusDateTime()));
				tracking.setCurrentStatus(status);
				response.getTracking().put(data.getAWB(), tracking);
			}
			return response;
		};
	}
	
	private static DateTimeFormatter formatter = null;
	static{
		formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
	}

	public static DateTime toDateTime(String date){
		return formatter.parseDateTime(date);
	}

	public static String dateToString(DateTime date){
		return formatter.print(date);

	}
}
