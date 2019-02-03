package com.cswm.assignment.modelvos;

import java.util.Map;

import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.serviceImpl.OrderBookServiceImpl.OrderTypesInStats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderBookStatsVo {
	
	private Long totalNoOfOrders;
	private Long totalNoofAccuOrders;
	private Map<OrderTypesInStats, Order> orderStats;
	private Long validOrderCount;
	private Long inValidOrderCount;
	private Long validDemand;
	private Long invalidDemand;
	private Double executionQty;
	private Double executionPrice;
	
	private OrderBook orderBook;
	
	public Long getTotalNoOfOrders() {
		return totalNoOfOrders;
	}
	public OrderBookStatsVo setTotalNoOfOrders(Long totalNoOfOrders) {
		this.totalNoOfOrders = totalNoOfOrders;
		return this;
	}
	public Long getTotalNoofAccuOrders() {
		return totalNoofAccuOrders;
	}
	public OrderBookStatsVo setTotalNoofAccuOrders(Long totalNoofAccuOrders) {
		this.totalNoofAccuOrders = totalNoofAccuOrders;
		return this;
	}
	public Long getValidOrderCount() {
		return validOrderCount;
	}
	public OrderBookStatsVo setValidOrderCount(Long validOrderCount) {
		this.validOrderCount = validOrderCount;
		return this;
	}
	public Long getInValidOrderCount() {
		return inValidOrderCount;
	}
	public OrderBookStatsVo setInValidOrderCount(Long inValidOrderCount) {
		this.inValidOrderCount = inValidOrderCount;
		return this;
	}
	public Long getValidDemand() {
		return validDemand;
	}
	public OrderBookStatsVo setValidDemand(Long validDemand) {
		this.validDemand = validDemand;
		return this;
	}
	public Long getInvalidDemand() {
		return invalidDemand;
	}
	public OrderBookStatsVo setInvalidDemand(Long invalidDemand) {
		this.invalidDemand = invalidDemand;
		return this;
	}
	public Double getExecutionQty() {
		return executionQty;
	}
	public OrderBookStatsVo setExecutionQty(Double executionQty) {
		this.executionQty = executionQty;
		return this;
	}
	public Double getExecutionPrice() {
		return executionPrice;
	}
	public OrderBookStatsVo setExecutionPrice(Double executionPrice) {
		this.executionPrice = executionPrice;
		return this;
	}
	
	
	public OrderBook getOrderBook() {
		return orderBook;
	}
	public OrderBookStatsVo setOrderBook(OrderBook orderBook) {
		this.orderBook = orderBook;
		return this;
	}
	
	
	public Map<OrderTypesInStats, Order> getOrderStats() {
		return orderStats;
	}
	public OrderBookStatsVo setOrderStats(Map<OrderTypesInStats, Order> orderStats) {
		this.orderStats = orderStats;
		return this;
	}
	@Override
	public String toString() {
		return "OrderBookStatsVo [totalNoOfOrders=" + totalNoOfOrders + ", totalNoofAccuOrders=" + totalNoofAccuOrders
				+ ", orderStats=" + orderStats + ", validOrderCount=" + validOrderCount + ", inValidOrderCount="
				+ inValidOrderCount + ", validDemand=" + validDemand + ", invalidDemand=" + invalidDemand
				+ ", executionQty=" + executionQty + ", executionPrice=" + executionPrice + ", orderBook=" + orderBook
				+ "]";
	}
	
	
	
	
	
}
