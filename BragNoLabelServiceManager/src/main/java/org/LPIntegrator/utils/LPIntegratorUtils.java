package org.LPIntegrator.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.shopifyApis.client.internals.ShopifyApiQueryGenerator;

public class LPIntegratorUtils {

	private static DateTimeFormatter formatter = null;

	static{
		formatter = DateTimeFormat.forPattern(ShopifyApiQueryGenerator.DATE_FORMAT);
	}

	public static DateTime getShopifyOrderDateTime(String date){
		return formatter.parseDateTime(date);
	}
	
	public static String getShopifyOrderAsString(DateTime date){
		return formatter.print(date);
		
	}

}
