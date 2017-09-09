package com.shopify.api.models;

import lombok.Data;

@Data
public class Customer {
	 
		private String tags;
	
	    private DefaultAddress default_address;
	
	    private String phone;
	
	    private String last_order_name;
	
	    private String total_spent;
	
	    private String state;
	
	    private String multipass_identifier;
	
	    private String tax_exempt;
	
	    private String orders_count;
	
	    private String id;
	
	    private String first_name;
	
	    private String last_order_id;
	
	    private String updated_at;
	
	    private String email;
	
	    private String last_name;
	
	    private String created_at;
	
	    private String verified_email;
	
	    private String accepts_marketing;
	
	    private String note;
	
	    
	
	    @Override
	    public String toString()
	    {
	        return "ClassPojo [tags = "+tags+", default_address = "+default_address+", phone = "+phone+", last_order_name = "+last_order_name+", total_spent = "+total_spent+", state = "+state+", multipass_identifier = "+multipass_identifier+", tax_exempt = "+tax_exempt+", orders_count = "+orders_count+", id = "+id+", first_name = "+first_name+", last_order_id = "+last_order_id+", updated_at = "+updated_at+", email = "+email+", last_name = "+last_name+", created_at = "+created_at+", verified_email = "+verified_email+", accepts_marketing = "+accepts_marketing+", note = "+note+"]";
	    }
}

