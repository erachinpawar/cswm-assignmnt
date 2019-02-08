package com.cswm.assignment.model.dto;

import java.time.LocalDateTime;
import java.util.Set;


import com.cswm.assignment.applicationutils.OrderBookStatus;

public class OrderBookDto {

	private Long orderBookId;

	private InstrumentDto instrument;

	private OrderBookStatus orderBookStatus;

	private Set<ExecutionDto> executions;

	private Set<OrderDto> orders;

	private String createdBy;

	private LocalDateTime createdOn;

	public Long getOrderBookId() {
		return orderBookId;
	}

	public void setOrderBookId(Long orderBookId) {
		this.orderBookId = orderBookId;
	}

	public InstrumentDto getInstrument() {
		return instrument;
	}

	public void setInstrument(InstrumentDto instrument) {
		this.instrument = instrument;
	}

	public OrderBookStatus getOrderBookStatus() {
		return orderBookStatus;
	}

	public void setOrderBookStatus(OrderBookStatus orderBookStatus) {
		this.orderBookStatus = orderBookStatus;
	}

	public Set<ExecutionDto> getExecutions() {
		return executions;
	}

	public void setExecutions(Set<ExecutionDto> executions) {
		this.executions = executions;
	}

	public Set<OrderDto> getOrders() {
		return orders;
	}

	public void setOrders(Set<OrderDto> orders) {
		this.orders = orders;
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
		return "OrderBookDto [orderBookId=" + orderBookId + ", instrument=" + instrument + ", orderBookStatus="
				+ orderBookStatus + ", executions=" + executions + ", orders=" + orders + ", createdBy=" + createdBy
				+ ", createdOn=" + createdOn + "]";
	}

	
	

}
