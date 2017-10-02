package com.shopify.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FulfillmentRequest {

	Fulfillment fulfillment = new Fulfillment();
}
