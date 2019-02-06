package com.cswm.assignment.modelvos;

import java.math.BigDecimal;

import com.cswm.assignment.applicationUtils.OrderStatus;
import com.cswm.assignment.model.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatsVo {


	private Order order;
	private BigDecimal executionPrice;
	private OrderStatus orderStatus;
	
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
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
