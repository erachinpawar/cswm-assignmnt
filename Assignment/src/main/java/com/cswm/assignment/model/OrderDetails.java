package com.cswm.assignment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.cswm.assignment.applicationutils.OrderStatus;
import com.cswm.assignment.applicationutils.OrderType;

@Entity
@Table(name = "orders_details_inv")
public class OrderDetails {

	@Id
	@Column(name = "orderDetails_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_details_inv_seq")
	private Long orderDetailsId;

	@Column(name = "order_status")
	private OrderStatus orderStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_type", length = 20)
	private OrderType orderType;

	@Column(name = "execution_quantity")
	@ColumnDefault("0")
	private Long executionQuantity;

	public Long getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Long orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public Long getExecutionQuantity() {
		return executionQuantity;
	}

	public void setExecutionQuantity(Long executionQuantity) {
		this.executionQuantity = executionQuantity;
	}

	@Override
	public String toString() {
		return "OrderDetails [orderDetailsId=" + orderDetailsId + ", orderStatus=" + orderStatus + ", orderType="
				+ orderType + ", executionQuantity=" + executionQuantity + "]";
	}

}
