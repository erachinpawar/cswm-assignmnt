package com.cswm.assignment.model.dto.ouputDto;

import java.math.BigDecimal;

import com.cswm.assignment.applicationutils.OrderStatus;

public class OrderStatisticsOutputDto {

	private OrderBookOutputDto order;
	private BigDecimal executionPrice;
	private OrderStatus orderStatus;

	public OrderBookOutputDto getOrder() {
		return order;
	}

	public void setOrder(OrderBookOutputDto order) {
		this.order = order;
	}

	public BigDecimal getExecutionPrice() {
		return executionPrice;
	}

	public void setExecutionPrice(BigDecimal executionPrice) {
		this.executionPrice = executionPrice;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public String toString() {
		return "OrderStatsVo [order=" + order + ", executionPrice=" + executionPrice + ", orderStatus=" + orderStatus + "]";
	}

}
