package org.LogisticsPartner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

public enum ShipmentStatus {
	
	CREATED, PICKED, IN_TRANSIT, DELIVERED, UNKNOWN;
	

	public static Map<ShipmentStatus, List<String>> statusMap = new HashMap<>();
	static {
		statusMap.put(CREATED, Lists.newArrayList("CRT"));
		statusMap.put(PICKED, Lists.newArrayList("PAK"));
		statusMap.put(IN_TRANSIT, Lists.newArrayList("UD"));
		statusMap.put(DELIVERED, Lists.newArrayList("DL"));
	}
	
	public static ShipmentStatus getStatusFromCode(String code) {
		for(Entry<ShipmentStatus, List<String>> es : ShipmentStatus.statusMap.entrySet()) {
			if(es.getValue().contains(code)) {
				return es.getKey();
			}
		}
		return ShipmentStatus.UNKNOWN;
	}
}
