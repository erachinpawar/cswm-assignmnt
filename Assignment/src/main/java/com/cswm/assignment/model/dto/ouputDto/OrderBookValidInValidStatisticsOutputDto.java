package com.cswm.assignment.model.dto.ouputDto;

import java.math.BigDecimal;

public class OrderBookValidInValidStatisticsOutputDto {

	private Long validOrderCount;
	private Long inValidOrderCount;
	private Long validDemand;
	private Long inValidDemand;
	private Long executionQty;
	private BigDecimal totalExecutionPrice;
	private OrderBookStatisticsOutputDto orderBookStatisticsDto;
	
	public OrderBookValidInValidStatisticsOutputDto() {	}	

	public OrderBookValidInValidStatisticsOutputDto(OrderBookStatisticsOutputDto orderBookStats) {
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

	public OrderBookStatisticsOutputDto getOrderBookStatisticsDto() {
		return orderBookStatisticsDto;
	}

	public void setOrderBookStatisticsDto(OrderBookStatisticsOutputDto orderBookStatisticsDto) {
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
