package org.Delhivery.Client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Shipment {

@JsonProperty("return_name")
private String returnName;
@JsonProperty("return_add")
private String returnAdd;
@JsonProperty("return_city")
private String returnCity;
@JsonProperty("return_state")
private String returnState;
@JsonProperty("return_pin")
private String returnPin;
@JsonProperty("return_country")
private String returnCountry;
@JsonProperty("return_phone")
private String returnPhone;
@JsonProperty("order")
private String order;
@JsonProperty("phone")
private String phone;
@JsonProperty("products_desc")
private String productsDesc;
@JsonProperty("cod_amount")
private String codAmount;
@JsonProperty("name")
private String name;
@JsonProperty("country")
private String country;
@JsonProperty("seller_inv_date")
private String sellerInvDate;
@JsonProperty("order_date")
private String orderDate;
@JsonProperty("total_amount")
private String totalAmount;
@JsonProperty("seller_add")
private String sellerAdd;
@JsonProperty("seller_cst")
private String sellerCst;
@JsonProperty("add")
private String add;
@JsonProperty("seller_name")
private String sellerName;
@JsonProperty("seller_inv")
private String sellerInv;
@JsonProperty("seller_tin")
private String sellerTin;
@JsonProperty("pin")
private String pin;
@JsonProperty("quantity")
private String quantity;
@JsonProperty("payment_mode")
private String paymentMode;
@JsonProperty("state")
private String state;
@JsonProperty("city")
private String city;
}