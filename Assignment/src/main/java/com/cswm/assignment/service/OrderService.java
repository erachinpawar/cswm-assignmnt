package com.cswm.assignment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.dto.ExecutionDto;
import com.cswm.assignment.model.dto.OrderBookDto;
import com.cswm.assignment.model.dto.OrderDto;
import com.cswm.assignment.model.dto.OrderStatisticsDto;

@Service
public interface OrderService {

	OrderStatisticsDto getOrderStats(Long orderId);

	List<OrderDto> getValidOrders(OrderBookDto orderBookDto, ExecutionDto executionDto);


	List<OrderDto> addExecutionQuantityToOrders(List<OrderDto> validOrders, Long accumltdOrders, Long effectiveQuanty);
}
