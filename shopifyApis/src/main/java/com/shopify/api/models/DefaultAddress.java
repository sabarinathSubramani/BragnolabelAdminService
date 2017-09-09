package com.shopify.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class DefaultAddress
{
	private String zip;

	private String phone;

	private String province_code;

	private String address1;

	private String address2;

	private String country_code;

	private String country_name;

	private String city;

	private String country;

	private String id;

	private String first_name;

	private String name;

	private String company;

	private String last_name;

	private String province;

	private String customer_id;
	
	private boolean defaultValue;

	public String getZip ()
	{
		return zip;
	}

	public void setZip (String zip)
	{
		this.zip = zip;
	}

	public String getPhone ()
	{
		return phone;
	}

	public void setPhone (String phone)
	{
		this.phone = phone;
	}

	public String getProvince_code ()
	{
		return province_code;
	}

	public void setProvince_code (String province_code)
	{
		this.province_code = province_code;
	}

	public String getAddress1 ()
	{
		return address1;
	}

	public void setAddress1 (String address1)
	{
		this.address1 = address1;
	}

	public String getAddress2 ()
	{
		return address2;
	}

	public void setAddress2 (String address2)
	{
		this.address2 = address2;
	}

	public String getCountry_code ()
	{
		return country_code;
	}

	public void setCountry_code (String country_code)
	{
		this.country_code = country_code;
	}

	public String getCountry_name ()
	{
		return country_name;
	}

	public void setCountry_name (String country_name)
	{
		this.country_name = country_name;
	}

	public String getCity ()
	{
		return city;
	}

	public void setCity (String city)
	{
		this.city = city;
	}

	public String getCountry ()
	{
		return country;
	}

	public void setCountry (String country)
	{
		this.country = country;
	}

	public String getId ()
	{
		return id;
	}

	public void setId (String id)
	{
		this.id = id;
	}

	public String getFirst_name ()
	{
		return first_name;
	}

	public void setFirst_name (String first_name)
	{
		this.first_name = first_name;
	}

	@JsonProperty("default")
	public boolean getDefault(){
		return defaultValue;
	}
	
	@JsonProperty("default")
	public void setDefault (boolean defaultValue){
		this.defaultValue = defaultValue;
	}

	public String getName()
	{
		return name;
	}

	public void setName (String name)
	{
		this.name = name;
	}

	public String getCompany ()
	{
		return company;
	}

	public void setCompany (String company)
	{
		this.company = company;
	}

	public String getLast_name ()
	{
		return last_name;
	}

	public void setLast_name (String last_name)
	{
		this.last_name = last_name;
	}

	public String getProvince ()
	{
		return province;
	}

	public void setProvince (String province)
	{
		this.province = province;
	}

	public String getCustomer_id ()
	{
		return customer_id;
	}

	public void setCustomer_id (String customer_id)
	{
		this.customer_id = customer_id;
	}

	@Override
	public String toString(){
		return "[zip = "+zip+", phone = "+phone+", province_code = "+province_code+", address1 = "+address1+", address2 = "+address2+", country_code = "+country_code+", country_name = "+country_name+", city = "+city+", country = "+country+", id = "+id+", first_name = "+first_name+", default = "+defaultValue+", name = "+name+", company = "+company+", last_name = "+last_name+", province = "+province+", customer_id = "+customer_id+"]";
	}
}