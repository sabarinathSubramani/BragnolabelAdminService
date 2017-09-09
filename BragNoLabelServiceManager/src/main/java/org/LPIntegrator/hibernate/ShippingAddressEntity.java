package org.LPIntegrator.hibernate;

import java.io.Serializable;
import java.security.MessageDigest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name="shipping_address")
@Entity
public class ShippingAddressEntity {
	
	public ShippingAddressEntity(long orderId){
		id =new HashCodeBuilder().append(orderId).build();
	}
	
	@Id
	@Setter(value=AccessLevel.PRIVATE)
	private long id;
	
	@Column(name="full_name")
	private String fullName;
	
	@Column(name="address1")
	private String address1;
	
	@Column(name="address2")
	private String address2;
	
	@Column(name="city")
	private String city;
	
	@Column(name="state")
	private String state;
	
	@Column(name="pincode")
	private int pincode;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="email")
	private String email;
	
	@Column(name="country")
	private String country;
	
	@Column(name="gstin")
	private String gstin;
	
	@OneToOne
	@JoinColumn(name="orderid",nullable=false)
	private OrderEntity orderEntity;
	
	
}
