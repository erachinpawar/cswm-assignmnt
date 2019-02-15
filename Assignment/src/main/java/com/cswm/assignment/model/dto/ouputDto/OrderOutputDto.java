package com.cswm.assignment.model.dto.ouputDto;

import java.math.BigDecimal;

import com.cswm.assignment.applicationutils.OrderStatus;
import com.cswm.assignment.applicationutils.OrderType;
import com.fasterxml.jackson.annotation.JsonBackReference;

public class OrderOutputDto {

	private Long orderId;

	private Long instrumentId;

	@JsonBackReference
	private OrderBookOutputDto orderBook;

	private Long orderQuantity;

	private BigDecimal orderprice;

	private OrderStatus orderStatus;

	private OrderType orderType;

	private Long executionQuantity;

	public OrderOutputDto() {
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(Long instrumentId) {
		this.instrumentId = instrumentId;
	}

	public OrderBookOutputDto getOrderBook() {
		return orderBook;
	}

	public void setOrderBook(OrderBookOutputDto orderBook) {
		this.orderBook = orderBook;
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

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public Long getExecutionQuantity() {
		return executionQuantity;
	}

	public void setExecutionQuantity(Long executionQuantity) {
		this.executionQuantity = executionQuantity;
	}

}
