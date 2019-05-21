package com.cswm.assignment.model.dto.ouputDto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class OrderOutputDto {

	private Long orderId;

	private InstrumentOutputDto instrument;

	@JsonBackReference
	private OrderBookOutputDto orderBook;

	private OrderDetailsOutputDto orderDetails;

	private Long orderQuantity;

	private BigDecimal orderprice;

	public OrderOutputDto() {
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public InstrumentOutputDto getInstrument() {
		return instrument;
	}

	public void setInstrument(InstrumentOutputDto instrument) {
		this.instrument = instrument;
	}

//	public OrderBookOutputDto getOrderBook() {
//		return orderBook;
//	}

	public void setOrderBook(OrderBookOutputDto orderBook) {
		this.orderBook = orderBook;
	}

	public OrderDetailsOutputDto getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(OrderDetailsOutputDto orderDetails) {
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

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", instrument=" + instrument + ",  orderDetails=" + orderDetails
				+ ", orderQuantity=" + orderQuantity + ", orderprice=" + orderprice + "]";
	}

}
