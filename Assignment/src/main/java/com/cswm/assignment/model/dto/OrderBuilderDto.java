package com.cswm.assignment.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cswm.assignment.applicationutils.OrderType;

public class OrderBuilderDto {

	private Long orderId;
	private InstrumentDto instrument;
	private Long orderQuantity;
	private BigDecimal orderprice;
	private String createdBy;
	private LocalDateTime createdOn;
	private OrderBookDto orderBook;
	private OrderDetailsDto orderDetails;

	public OrderBuilderDto(OrderDto orderDto) {

		this.orderId = orderDto.getOrderId();
		this.instrument = orderDto.getInstrument();
		this.orderQuantity = orderDto.getOrderQuantity();
		this.orderprice = orderDto.getOrderprice();
		this.orderBook = orderDto.getOrderBook();
		this.createdBy = orderDto.getCreatedBy();
		this.createdOn = orderDto.getCreatedOn();
		this.orderDetails = orderDto.getOrderDetails();
		if (null == orderDto.getOrderprice() || orderDto.getOrderprice().equals(BigDecimal.ZERO))
			this.getOrderDetails().setOrderType(OrderType.MARKET_ORDER);
		else
			this.getOrderDetails().setOrderType(OrderType.LIMIT_ORDER);

	}

	public OrderDto build() {
		return new OrderDto(this);
	}

	public OrderBuilderDto() {
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

	@Override
	public String toString() {
		return "OrderBuilderDto [orderId=" + orderId + ", instrument=" + instrument + ", orderQuantity=" + orderQuantity
				+ ", orderprice=" + orderprice + ", createdBy=" + createdBy + ", createdOn=" + createdOn
				+ ", orderBook=" + orderBook + ", orderDetails=" + orderDetails + "]";
	}

}
