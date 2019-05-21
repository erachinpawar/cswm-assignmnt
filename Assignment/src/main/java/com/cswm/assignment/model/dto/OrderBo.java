package com.cswm.assignment.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class OrderBo {

	private Long orderId;

	private InstrumentBo instrument;

	@JsonBackReference
	private OrderBookBo orderBook;

	private OrderDetailsBo orderDetails;

	private Long orderQuantity;

	private BigDecimal orderprice;

	private String createdBy;

	private LocalDateTime createdOn;

	public OrderBo() {
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public InstrumentBo getInstrument() {
		return instrument;
	}

	public void setInstrument(InstrumentBo instrument) {
		this.instrument = instrument;
	}

	public OrderBookBo getOrderBook() {
		return orderBook;
	}

	public void setOrderBook(OrderBookBo orderBook) {
		this.orderBook = orderBook;
	}

	public OrderDetailsBo getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(OrderDetailsBo orderDetails) {
		this.orderDetails = orderDetails;
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


}
