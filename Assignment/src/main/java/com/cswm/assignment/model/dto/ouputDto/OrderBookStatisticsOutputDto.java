package com.cswm.assignment.model.dto.ouputDto;

import java.math.BigDecimal;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderBookStatisticsOutputDto {

	private Long totalOrderCount;
	private Long validOrderCount;
	private Long inValidOrderCount;
	private Long totalDemand;
	private Long validDemand;
	private Long inValidDemand;
	private Long accumulatedExecutionQuantity;
	private BigDecimal executionPrice;
	private OrderOutputDto smallestOrder;
	private OrderOutputDto biggestOrder;
	private OrderOutputDto earliestOrder;
	private OrderOutputDto lastOrder;
	Map<BigDecimal, Long> limitBreakDownForAllOrders;
	Map<BigDecimal, Long> limitBreakDownForValidOrders;
	Map<BigDecimal, Long> limitBreakDownForInvalidOrders;

	public Long getTotalOrderCount() {
		return totalOrderCount;
	}

	public void setTotalOrderCount(Long totalOrderCount) {
		this.totalOrderCount = totalOrderCount;
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

	public Long getTotalDemand() {
		return totalDemand;
	}

	public void setTotalDemand(Long totalDemand) {
		this.totalDemand = totalDemand;
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

	public Long getAccumulatedExecutionQuantity() {
		return accumulatedExecutionQuantity;
	}

	public void setAccumulatedExecutionQuantity(Long accumulatedExecutionQuantity) {
		this.accumulatedExecutionQuantity = accumulatedExecutionQuantity;
	}

	public BigDecimal getExecutionPrice() {
		return executionPrice;
	}

	public void setExecutionPrice(BigDecimal executionPrice) {
		this.executionPrice = executionPrice;
	}

	public OrderOutputDto getSmallestOrder() {
		return smallestOrder;
	}

	public void setSmallestOrder(OrderOutputDto smallestOrder) {
		this.smallestOrder = smallestOrder;
	}

	public OrderOutputDto getBiggestOrder() {
		return biggestOrder;
	}

	public void setBiggestOrder(OrderOutputDto biggestOrder) {
		this.biggestOrder = biggestOrder;
	}

	public OrderOutputDto getEarliestOrder() {
		return earliestOrder;
	}

	public void setEarliestOrder(OrderOutputDto earliestOrder) {
		this.earliestOrder = earliestOrder;
	}

	public OrderOutputDto getLastOrder() {
		return lastOrder;
	}

	public void setLastOrder(OrderOutputDto lastOrder) {
		this.lastOrder = lastOrder;
	}

	public Map<BigDecimal, Long> getLimitBreakDownForAllOrders() {
		return limitBreakDownForAllOrders;
	}

	public void setLimitBreakDownForAllOrders(Map<BigDecimal, Long> limitBreakDownForAllOrders) {
		this.limitBreakDownForAllOrders = limitBreakDownForAllOrders;
	}

	public Map<BigDecimal, Long> getLimitBreakDownForValidOrders() {
		return limitBreakDownForValidOrders;
	}

	public void setLimitBreakDownForValidOrders(Map<BigDecimal, Long> limitBreakDownForValidOrders) {
		this.limitBreakDownForValidOrders = limitBreakDownForValidOrders;
	}

	public Map<BigDecimal, Long> getLimitBreakDownForInvalidOrders() {
		return limitBreakDownForInvalidOrders;
	}

	public void setLimitBreakDownForInvalidOrders(Map<BigDecimal, Long> limitBreakDownForInvalidOrders) {
		this.limitBreakDownForInvalidOrders = limitBreakDownForInvalidOrders;
	}

	@Override
	public String toString() {
		return "OrderBookStatisticsOutputDto [totalOrderCount=" + totalOrderCount + ", validOrderCount=" + validOrderCount + ", inValidOrderCount=" + inValidOrderCount + ", totalDemand=" + totalDemand
				+ ", validDemand=" + validDemand + ", inValidDemand=" + inValidDemand + ", accumulatedExecutionQuantity=" + accumulatedExecutionQuantity + ", executionPrice=" + executionPrice
				+ ", smallestOrder=" + smallestOrder + ", biggestOrder=" + biggestOrder + ", earliestOrder=" + earliestOrder + ", lastOrder=" + lastOrder + ", limitBreakDownForAllOrders="
				+ limitBreakDownForAllOrders + ", limitBreakDownForValidOrders=" + limitBreakDownForValidOrders + ", limitBreakDownForInvalidOrders=" + limitBreakDownForInvalidOrders + "]";
	}

}
