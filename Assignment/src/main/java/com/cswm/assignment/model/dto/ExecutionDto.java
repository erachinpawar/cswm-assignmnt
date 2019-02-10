package com.cswm.assignment.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class ExecutionDto {

	private Long executionId;

	private BigDecimal price;

	private Long quantity;

	@JsonBackReference
	private OrderBookDto orderBook;

	private String createdBy;

	private LocalDateTime createdOn;

	public ExecutionDto() {
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

	public OrderBookDto getOrderBook() {
		return orderBook;
	}

	public void setOrderBook(OrderBookDto orderBook) {
		this.orderBook = orderBook;
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
		return "ExecutionDto [executionId=" + executionId + ", price=" + price + ", quantity=" + quantity
				+ ", createdBy=" + createdBy + ", createdOn=" + createdOn + "]";
	}

}
