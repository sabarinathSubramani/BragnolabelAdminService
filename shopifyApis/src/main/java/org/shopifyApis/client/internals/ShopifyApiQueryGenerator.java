package org.shopifyApis.client.internals;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.shopifyApis.models.ShopifyOrdersQuery;

public class ShopifyApiQueryGenerator implements Function<ShopifyOrdersQuery, Map<String, Object>> {

	Logger logger = Logger.getLogger(ShopifyApiQueryGenerator.class);

	//2014-04-25T16:15:47-04:00
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZ";
	


	/**
	 * this method takes care of create query params for orders api call
	 * @param shopifyOrdersQuery
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */

	public Map<String, Object> apply(ShopifyOrdersQuery shopifyOrdersQuery) {
		Map<String, Object> queryParams = new HashMap<String,Object>();
		for (Field field : ShopifyOrdersQuery.class.getDeclaredFields()){
			field.setAccessible(true);
			Object object = null;
			try {
				object = field.get(shopifyOrdersQuery);
			} catch (Exception e) {
				logger.error("error processing field - "+field, e);
			}
			if(object!= null){
				if(object instanceof DateTime){
					DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);
					object = formatter.print((DateTime)object);
				}
				queryParams.put(field.getName(),object);
			}
		}
		return queryParams;
	}
}
