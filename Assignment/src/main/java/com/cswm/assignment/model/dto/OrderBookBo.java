package com.cswm.assignment.model.dto;

import java.time.LocalDateTime;
import java.util.Set;


import com.cswm.assignment.applicationutils.OrderBookStatus;

public class OrderBookBo {

	private Long orderBookId;

	private InstrumentBo instrument;

	private OrderBookStatus orderBookStatus;

	private Set<ExecutionBo> executions;

	private Set<OrderBo> orders;

	private String createdBy;

	private LocalDateTime createdOn;

	public Long getOrderBookId() {
		return orderBookId;
	}

	public void setOrderBookId(Long orderBookId) {
		this.orderBookId = orderBookId;
	}

	public InstrumentBo getInstrument() {
		return instrument;
	}

	public void setInstrument(InstrumentBo instrument) {
		this.instrument = instrument;
	}

	public OrderBookStatus getOrderBookStatus() {
		return orderBookStatus;
	}

	public void setOrderBookStatus(OrderBookStatus orderBookStatus) {
		this.orderBookStatus = orderBookStatus;
	}

	public Set<ExecutionBo> getExecutions() {
		return executions;
	}

	public void setExecutions(Set<ExecutionBo> executions) {
		this.executions = executions;
	}

	public Set<OrderBo> getOrders() {
		return orders;
	}

	public void setOrders(Set<OrderBo> orders) {
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

}
