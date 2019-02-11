package com.cswm.assignment.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.dto.ouputDto.OrderStatisticsOutputDto;

@Service
public interface OrderService {

	
	/**
	 * This method accepts order Id as the input and returns order statistics of the order
	 * @param orderId
	 * @return OrderStatisticsDto
	 */
	OrderStatisticsOutputDto getOrderStats(Long orderId);

	/**
	 * distribution of execution quantity among valid orders linearly
	 * @param validOrders
	 * @param accumltdOrders
	 * @param effectiveQuanty
	 * @return list of the orders after distribution of execution quantity among valid orders linearly
	 */
	List<Order> addExecutionQuantityToOrders(Set<Order> validOrders, Long accumltdOrders, Long effectiveQuanty);
}
