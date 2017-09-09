package org.LPIntegrator.models;

import lombok.Data;

@Data
public class User {

	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String state;
	private Address defaultAddress;
}
