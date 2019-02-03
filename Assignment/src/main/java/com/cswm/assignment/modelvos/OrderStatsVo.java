package com.cswm.assignment.modelvos;

import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.Order.OrderStatus;

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
	private Double executionPrice;
	private OrderStatus orderStatus;
	
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public Double getExecutionPrice() {
		return executionPrice;
	}
	public void setExecutionPrice(Double executionPrice) {
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
