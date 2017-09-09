package org.LPIntegrator.hibernate;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="order_line_items")
@Getter
@Setter
public class OrderLineItemEntity {
	

	
	@Id
	@Column(name="order_item_id")
	private long orderItemId;
	
	@Column(name="product_title")
	private String productTitle;
	
	@Column(name="quantity")
	private int quantity;
	
	@Column(name="total_price")
	private double totalPrice;
	
	@Column(name="weight")
	private double weight;
	
	@Column(name="sku")
	private String sku;
	
	@Column(name="tax_type")
	private String taxType;
	
	@Column(name="tax_rate")
	private double taxRate;
	
	@Column(name="created_at")
	private Date createdAt;
	
	@Column(name="last_updated_at")
	private Date lastUpdatedAt;
	
	@ManyToOne
	@JoinColumn(name="orderid",nullable=false)
	private OrderEntity orderEntity;
	
}
