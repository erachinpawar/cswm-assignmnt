package com.cswm.assignment.model.dto;

import java.math.BigDecimal;

public class OrderBookValidInValidStatistics {

	private Long validOrderCount;
	private Long inValidOrderCount;
	private Long validDemand;
	private Long inValidDemand;
	private Long executionQty;
	private BigDecimal totalExecutionPrice;
	private OrderBookStatisticsDto orderBookStatisticsDto;
	
	public OrderBookValidInValidStatistics() {	}	

	public OrderBookValidInValidStatistics(OrderBookStatisticsDto orderBookStats) {
		this.orderBookStatisticsDto = orderBookStats;
	}

	public Long getValidOrderCount() {
		return validOrderCount;
	}

	public void setValidOrderCount(Long validOrderCount) {
		this.validOrderCount = validOrderCount;
	}

	public Long getInValidOrderCount() {
		return inValidOrderCount;
	}

	public void setInValidOrderCount(Long inValidOrderCount) {
		this.inValidOrderCount = inValidOrderCount;
	}

	public Long getValidDemand() {
		return validDemand;
	}

	public void setValidDemand(Long validDemand) {
		this.validDemand = validDemand;
	}

	public Long getInValidDemand() {
		return inValidDemand;
	}

	public void setInValidDemand(Long inValidDemand) {
		this.inValidDemand = inValidDemand;
	}

	public Long getExecutionQty() {
		return executionQty;
	}

	public void setExecutionQty(Long executionQty) {
		this.executionQty = executionQty;
	}

	public BigDecimal getTotalExecutionPrice() {
		return totalExecutionPrice;
	}

	public void setTotalExecutionPrice(BigDecimal totalExecutionPrice) {
		this.totalExecutionPrice = totalExecutionPrice;
	}

	public OrderBookStatisticsDto getOrderBookStatisticsDto() {
		return orderBookStatisticsDto;
	}

	public void setOrderBookStatisticsDto(OrderBookStatisticsDto orderBookStatisticsDto) {
		this.orderBookStatisticsDto = orderBookStatisticsDto;
	}

	@Override
	public String toString() {
		return "OrderBookValidInValidStatistics [validOrderCount=" + validOrderCount + ", inValidOrderCount="
				+ inValidOrderCount + ", validDemand=" + validDemand + ", inValidDemand=" + inValidDemand
				+ ", executionQty=" + executionQty + ", totalExecutionPrice=" + totalExecutionPrice
				+ ", orderBookStatisticsDto=" + orderBookStatisticsDto + "]";
	}

}
