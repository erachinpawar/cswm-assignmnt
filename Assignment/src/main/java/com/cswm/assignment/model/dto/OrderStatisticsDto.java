package com.cswm.assignment.model.dto;

import java.math.BigDecimal;

import com.cswm.assignment.applicationutils.OrderStatus;
import com.cswm.assignment.model.dto.OrderDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatisticsDto {

	private OrderDto order;
	private BigDecimal executionPrice;
	private OrderStatus orderStatus;

	public OrderDto getOrder() {
		return order;
	}

	public void setOrder(OrderDto order) {
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
		return "OrderStatsVo [order=" + order + ", executionPrice=" + executionPrice + ", orderStatus=" + orderStatus
				+ "]";
	}

}
