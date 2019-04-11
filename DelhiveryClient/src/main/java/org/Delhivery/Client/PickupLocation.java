package org.Delhivery.Client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PickupLocation {

		@JsonProperty("pin")
		private String pin;
		@JsonProperty("add")
		private String add;
		@JsonProperty("phone")
		private String phone;
		@JsonProperty("state")
		private String state;
		@JsonProperty("city")
		private String city;
		@JsonProperty("country")
		private String country;
		@JsonProperty("name")
		private String name;

}
