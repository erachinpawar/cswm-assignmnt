package com.cswm.assignment.model.dto.ouputDto;

import java.math.BigDecimal;
import java.util.Map;

import com.cswm.assignment.applicationutils.OrderTypesInStatistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderBookStatisticsOutputDto {

	private Long totalNoOfOrders;
	private Long totalNoofAccuOrders;
	private Map<OrderTypesInStatistics, OrderOutputDto> orderTypesInStats;
	Map<BigDecimal, Long> limitPriceVsDemandTable;

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

	public Map<OrderTypesInStatistics, OrderOutputDto> getOrderTypesInStats() {
		return orderTypesInStats;
	}

	public void setOrderTypesInStats(Map<OrderTypesInStatistics, OrderOutputDto> orderTypesInStats) {
		this.orderTypesInStats = orderTypesInStats;
	}

	public Map<BigDecimal, Long> getLimitPriceVsDemandTable() {
		return limitPriceVsDemandTable;
	}

	public void setLimitPriceVsDemandTable(Map<BigDecimal, Long> limitPriceVsDemandTable) {
		this.limitPriceVsDemandTable = limitPriceVsDemandTable;
	}

	@Override
	public String toString() {
		return "OrderBookStatisticsDto [totalNoOfOrders=" + totalNoOfOrders + ", totalNoofAccuOrders="
				+ totalNoofAccuOrders + ", orderTypesInStats=" + orderTypesInStats + ", limitPriceVsDemandTable="
				+ limitPriceVsDemandTable + "]";
	}

}
