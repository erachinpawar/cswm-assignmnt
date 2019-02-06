package com.cswm.assignment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.cswm.assignment.model.dto.OrderBuilder;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "orders_inv")
public class Order {


	@Id
	@Column(name = "order_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_inv_seq") 
	private Long orderId;

	@OneToOne
	@JoinColumn(name = "instrument_id") 
	private Instrument instrument;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "order_book_id", nullable = true)
	@JsonBackReference
	private OrderBook orderBook;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="orderDetails_id")
	private OrderDetails OrderDetails;

	@Column(name = "order_quantity") 
	@ColumnDefault("0")
	private Long orderQuantity;

	@Column(name = "order_price") 
	@ColumnDefault("0")
	private BigDecimal orderprice;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	public Order() {
	}
	
	public Order(OrderBuilder orderBuilder) {
		super();
		this.orderId = orderBuilder.getOrderId();
		this.instrument = orderBuilder.getInstrument();
		this.orderBook=orderBuilder.getOrderBook();
		this.OrderDetails=orderBuilder.getOrderDetails();
		this.orderQuantity = orderBuilder.getOrderQuantity();
		this.orderprice = orderBuilder.getOrderprice();
		this.createdBy = orderBuilder.getCreatedBy();
		this.createdOn = orderBuilder.getCreatedOn();
	}

	public Long getOrderId() {
		return orderId;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public Long getOrderQuantity() {
		return orderQuantity;
	}

	public BigDecimal getOrderprice() {
		return orderprice;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public OrderBook getOrderBook() {
		return orderBook;
	}

	public OrderDetails getOrderDetails() {
		return OrderDetails;
	}
	

}
