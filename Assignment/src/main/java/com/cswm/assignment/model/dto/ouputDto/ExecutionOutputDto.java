package com.cswm.assignment.model.dto.ouputDto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class ExecutionOutputDto {

	private Long executionId;

	private BigDecimal price;

	private Long quantity;

	@JsonBackReference
	private OrderBookOutputDto orderBook;

	public ExecutionOutputDto() {
	}

	public Long getExecutionId() {
		return executionId;
	}

	public void setExecutionId(Long executionId) {
		this.executionId = executionId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

//	public OrderBookOutputDto getOrderBook() {
//		return orderBook;
//	}

	public void setOrderBook(OrderBookOutputDto orderBook) {
		this.orderBook = orderBook;
	}


}
