package com.cswm.assignment.model.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.cswm.assignment.model.dto.OrderBookDto;
import com.cswm.assignment.model.dto.OrderDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderBookStatisticsDto {

	public enum OrderTypesInStats {
		BIGGEST_ORDER, SMALLEST_ORDER, EARLIEST_ORDER, LATEST_ORDER
	}

	private Long totalNoOfOrders;
	private Long totalNoofAccuOrders;
	private Map<OrderTypesInStats, OrderDto> orderStats;
	private Long validOrderCount;
	private Long inValidOrderCount;
	private Long validDemand;
	private Long invalidDemand;
	private Long executionQty;
	private BigDecimal executionPrice;
	private OrderBookDto orderBookDto;

	public Long getTotalNoOfOrders() {
		return totalNoOfOrders;
	}


	public void setTotalNoOfOrders(Long totalNoOfOrders) {
		this.totalNoOfOrders = totalNoOfOrders;
	}


	public Long getTotalNoofAccuOrders() {
		return totalNoofAccuOrders;
	}


	public void setTotalNoofAccuOrders(Long totalNoofAccuOrders) {
		this.totalNoofAccuOrders = totalNoofAccuOrders;
	}


	public Map<OrderTypesInStats, OrderDto> getOrderStats() {
		return orderStats;
	}


	public void setOrderStats(Map<OrderTypesInStats, OrderDto> orderStats) {
		this.orderStats = orderStats;
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


	public Long getInvalidDemand() {
		return invalidDemand;
	}


	public void setInvalidDemand(Long invalidDemand) {
		this.invalidDemand = invalidDemand;
	}


	public Long getExecutionQty() {
		return executionQty;
	}


	public void setExecutionQty(Long executionQty) {
		this.executionQty = executionQty;
	}


	public BigDecimal getExecutionPrice() {
		return executionPrice;
	}


	public void setExecutionPrice(BigDecimal executionPrice) {
		this.executionPrice = executionPrice;
	}


	public OrderBookDto getOrderBookDto() {
		return orderBookDto;
	}


	public void setOrderBookDto(OrderBookDto orderBookDto) {
		this.orderBookDto = orderBookDto;
	}


	@Override
	public String toString() {
		return "OrderBookStatsVo [totalNoOfOrders=" + totalNoOfOrders + ", totalNoofAccuOrders=" + totalNoofAccuOrders
				+ ", orderStats=" + orderStats + ", validOrderCount=" + validOrderCount + ", inValidOrderCount="
				+ inValidOrderCount + ", validDemand=" + validDemand + ", invalidDemand=" + invalidDemand
				+ ", executionQty=" + executionQty + ", executionPrice=" + executionPrice + ", orderBookDto="
				+ orderBookDto + "]";
	}

}
