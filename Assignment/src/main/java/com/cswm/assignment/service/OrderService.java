package com.cswm.assignment.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.dto.OrderDto;
import com.cswm.assignment.model.dto.OrderStatisticsDto;

@Service
public interface OrderService {

	
	/**
	 * This method accepts order Id as the input and returns order statistics of the order
	 * @param orderId
	 * @return OrderStatisticsDto
	 */
	OrderStatisticsDto getOrderStats(Long orderId);

	/**
	 * distribution of execution quantity among valid orders linearly
	 * @param validOrderdDtos
	 * @param accumltdOrders
	 * @param effectiveQuanty
	 * @return list of the orders after distribution of execution quantity among valid orders linearly
	 */
	List<OrderDto> addExecutionQuantityToOrders(Set<OrderDto> validOrderdDtos, Long accumltdOrders, Long effectiveQuanty);
}
