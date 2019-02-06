package com.cswm.assignment.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.model.OrderDetails;

public class OrderBuilder {

	private Long orderId;
	private Instrument instrument;
	private Long orderQuantity;
	private BigDecimal orderprice;
	private String createdBy;
	private LocalDateTime createdOn;
	private OrderBook orderBook;
	private OrderDetails OrderDetails;

	public OrderBuilder(Order order) {
		this.orderId = order.getOrderId();
		this.instrument = order.getInstrument();
		this.orderQuantity = order.getOrderQuantity();
		this.orderprice = order.getOrderprice();
		this.orderBook=order.getOrderBook();
		this.createdBy = order.getCreatedBy();
		this.createdOn = order.getCreatedOn();
		this.OrderDetails=order.getOrderDetails();
	}

	public Order build() {
		return new Order(this);
	}

	public OrderBuilder() {
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}

	public Long getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(Long orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public BigDecimal getOrderprice() {
		return orderprice;
	}

	public void setOrderprice(BigDecimal orderprice) {
		this.orderprice = orderprice;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	
	public OrderBook getOrderBook() {
		return orderBook;
	}

	public void setOrderBook(OrderBook orderBook) {
		this.orderBook = orderBook;
	}
	
	

	public OrderDetails getOrderDetails() {
		return OrderDetails;
	}

	public void setOrderDetails(OrderDetails orderDetails) {
		OrderDetails = orderDetails;
	}

	@Override
	public String toString() {
		return "OrderBuilder [orderId=" + orderId + ", instrument=" + instrument + ", orderQuantity=" + orderQuantity
				+ ", orderprice=" + orderprice + ", createdBy=" + createdBy + ", createdOn=" + createdOn
				+ ", orderBook=" + orderBook + "]";
	}

	
	
}
