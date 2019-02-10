package com.cswm.assignment.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class OrderDto {

	private Long orderId;

	private InstrumentDto instrument;

	@JsonBackReference
	private OrderBookDto orderBook;

	private OrderDetailsDto orderDetails;

	private Long orderQuantity;

	private BigDecimal orderprice;

	private String createdBy;

	private LocalDateTime createdOn;

	public OrderDto() {
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public InstrumentDto getInstrument() {
		return instrument;
	}

	public void setInstrument(InstrumentDto instrument) {
		this.instrument = instrument;
	}

	public OrderBookDto getOrderBook() {
		return orderBook;
	}

	public void setOrderBook(OrderBookDto orderBook) {
		this.orderBook = orderBook;
	}

	public OrderDetailsDto getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(OrderDetailsDto orderDetails) {
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

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", instrument=" + instrument + ",  orderDetails=" + orderDetails
				+ ", orderQuantity=" + orderQuantity + ", orderprice=" + orderprice + ", createdBy=" + createdBy
				+ ", createdOn=" + createdOn + "]";
	}

}
