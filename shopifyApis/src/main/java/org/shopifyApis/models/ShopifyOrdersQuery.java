package org.shopifyApis.models;

import org.joda.time.DateTime;

import lombok.Data;

@Data
public class ShopifyOrdersQuery {

	//A comma-separated list of order ids	
	private String ids;
	//Amount of results (default: 50) (maximum: 250)"	
	private Integer limit;
	
	/*Page to show
	(default: 1)*/	
	private Integer page;
	
	//Restrict results to after the specified ID	
	private String since_id;
	
	//Show orders created after date (format: 2014-04-25T16:15:47-04:00)	
	private String created_at_min;
	
	//Show orders created before date (format: 2014-04-25T16:15:47-04:00)	
	private DateTime created_at_max;
	
	
	//Show orders last updated after date (format: 2014-04-25T16:15:47-04:00)	
	private String updated_at_min;
	
	//Show orders last updated before date (format: 2014-04-25T16:15:47-04:00)	
	private String updated_at_max;
	
	//Show orders imported after date (format: 2014-04-25T16:15:47-04:00)	
	private String processed_at_min;
	
	//Show orders imported before date (format: 2014-04-25T16:15:47-04:00)	
	private String processed_at_max;
	
	/*
	Show orders attributed to a specific app. 
	Valid values are the app ID to filter on (eg. 123) or a value of "current" to only show orders for the app currently consuming the API.
	*/	
	private String attribution_app_id;
	
	/*open - All open orders (default) 
	 * closed - Show only closed orders 
	 * cancelled - Show only cancelled orders 
	 * any - Any order status	
	 */
	private Status status;
	
	/*authorized - Show only authorized orders 
	 * pending - Show only pending orders 
	 * paid - Show only paid orders 
	 * partially_paid - Show only partially paid orders 
	 * refunded - Show only refunded orders 
	 * voided - Show only voided orders 
	 * partially_refunded - Show only partially_refunded orders 
	 * any - Show all authorized, pending, and paid orders (default). This is a filter, not a value. 
	 * unpaid - Show all authorized, or partially_paid orders. This is a filter, not a value.	
	*/
	private FinancialStatus financial_status;
	/*
	 * shipped - Show orders that have been shipped 
	 * partial - Show partially shipped orders 
	 * unshipped - Show orders that have not yet been shipped 
	 * any - Show orders with any fulfillment_status. (default)	
	*/
	private FullFillMentStatus fulfillment_status;
	
	//comma-separated list of fields to include in the response	
	private String fields;
	
	public static enum Status{
		open,
		closed,
		cancelled,
		any;
	}
	
	public static enum FinancialStatus{
		authorized,
		pending,
		paid,
		partially_paid,
		refunded,
		voided,
		partially_refunded,
		any,
		unpaid;
	}
	
	public  static enum FullFillMentStatus{
		shipped,
		partial,
		unshipped,
		any;
	}
}
