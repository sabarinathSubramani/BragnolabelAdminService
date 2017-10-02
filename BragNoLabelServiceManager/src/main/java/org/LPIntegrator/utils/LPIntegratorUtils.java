package org.LPIntegrator.utils;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.shopifyApis.client.internals.ShopifyApiQueryGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LPIntegratorUtils {

	static Logger logger = Logger.getLogger(LPIntegratorUtils.class);
	private static DateTimeFormatter formatter = null;
	private static ObjectMapper o = new ObjectMapper();
	static{
		formatter = DateTimeFormat.forPattern(ShopifyApiQueryGenerator.DATE_FORMAT);
	}

	public static DateTime getShopifyOrderDateTime(String date){
		return formatter.parseDateTime(date);
	}

	public static String getShopifyOrderAsString(DateTime date){
		return formatter.print(date);

	}


	public static <T> T jsonConverter(String json, Class<T> clzz){
		try {
			return o.readValue(json.getBytes(), clzz);
		} catch (Exception e) {
			logger.error("unable to convert to object, e");
			return null;
		}
	}

	public static String toJsonConverter(Object obj){
		try {
			return o.writeValueAsString(obj);
		} catch (Exception e) {
			logger.error("unable to convert to String", e);
			return null;
		}
	}

}
