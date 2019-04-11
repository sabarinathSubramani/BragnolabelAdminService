package org.LPIntegrator.hibernate;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.ShopifyInegration.models.FinancialStatus;
import org.ShopifyInegration.models.FullFillMentStatus;
import org.ShopifyInegration.models.OrderType;
import org.joda.time.DateTime;

import lombok.Data;

@Entity
@Table(name="orders")
@Data
public class OrderEntity {
	
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Id
	@Column(name="orderid")
	private long orderid;
	
	@Column(name="client_id")
	private int clientId;
	
	@Column(name="fulfillment_status")
	@Enumerated(EnumType.STRING)
	private FullFillMentStatus fulfillmentStatus;
	
	@Column(name="financial_status")
	@Enumerated(EnumType.STRING)
	private FinancialStatus financialStatus;
	
	@Column(name="order_status")
	private String orderStatus;
	
	@Column(name="total_price")
	private double totalPrice;
	
	@Column(name="tax_type")
	private String taxType;
	
	@Column(name="tax_rate")
	private double taxRate;
	
	@Column(name="tracking_number")
	private double trackingNumber;
	
	@Column(name="tracking_url")
	private double trackingUrl;
	
	@Column(name="test_order")
	private int testOrder;
	
	@Column(name="shipping_fees")
	private double shippingFees;
	
	@Column(name="discount")
	private double discount;
	
	@Column(name="created_at")
	private DateTime createdAt;
	
	@Column(name="last_updated_at")
	private DateTime lastUpdatedAt;
	
	@Column(name="pushed_to_warehouse")
	private int pushedToWareHouse;
	
	@Column(name="order_type")
	@Enumerated(EnumType.STRING)
	private OrderType orderType;
	
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="shipping_address_id", unique=true)
	private ShippingAddressEntity addressEntity;
	
	/*@OneToOne(mappedBy="orderEntity", cascade=CascadeType.ALL)
	private ShippingAddressEntity addressEntity;*/
	
	@OneToMany(mappedBy="orderEntity",cascade=CascadeType.ALL)
	private List<OrderLineItemEntity> orderLineItems;

	
}
