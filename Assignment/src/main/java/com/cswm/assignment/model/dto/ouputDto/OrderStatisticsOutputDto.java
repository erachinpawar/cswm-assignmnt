package com.cswm.assignment.model.dto.ouputDto;

import java.math.BigDecimal;

import com.cswm.assignment.applicationutils.OrderStatus;

public class OrderStatisticsOutputDto {

	private Long orderId;
	private BigDecimal executionPrice;
	private OrderStatus orderStatus;
	private Long executionQuantity;

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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getExecutionQuantity() {
		return executionQuantity;
	}

	public void setExecutionQuantity(Long executionQuantity) {
		this.executionQuantity = executionQuantity;
	}

	@Override
	public String toString() {
		return "OrderStatisticsOutputDto [orderId=" + orderId + ", executionPrice=" + executionPrice + ", orderStatus=" + orderStatus + ", executionQuantity=" + executionQuantity + "]";
	}

}
