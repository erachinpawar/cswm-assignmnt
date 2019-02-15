package com.cswm.assignment.model.dto.ouputDto;

import java.util.Set;

import com.cswm.assignment.applicationutils.OrderBookStatus;

public class OrderBookOutputDto {

	private Long orderBookId;

	private Long instrumentId;

	private OrderBookStatus orderBookStatus;

	private Set<ExecutionOutputDto> executions;

	private Set<OrderOutputDto> orders;
	
	public OrderBookOutputDto() {}

	public Long getOrderBookId() {
		return orderBookId;
	}

	public void setOrderBookId(Long orderBookId) {
		this.orderBookId = orderBookId;
	}

	public Long getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(Long instrumentId) {
		this.instrumentId = instrumentId;
	}

	public OrderBookStatus getOrderBookStatus() {
		return orderBookStatus;
	}

	public void setOrderBookStatus(OrderBookStatus orderBookStatus) {
		this.orderBookStatus = orderBookStatus;
	}

	public Set<ExecutionOutputDto> getExecutions() {
		return executions;
	}

	public void setExecutions(Set<ExecutionOutputDto> executions) {
		this.executions = executions;
	}

	public Set<OrderOutputDto> getOrders() {
		return orders;
	}

	public void setOrders(Set<OrderOutputDto> orders) {
		this.orders = orders;
	}

}
