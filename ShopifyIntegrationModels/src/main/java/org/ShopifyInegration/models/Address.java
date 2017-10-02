package org.ShopifyInegration.models;

import lombok.Data;

@Data
public class Address {

	private String firstName;
	private String lastName;
	private String fullName;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private int zip;
	private String state;
	private String country;
	private String phone;
	private String email;
}
