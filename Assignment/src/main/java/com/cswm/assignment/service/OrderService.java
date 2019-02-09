package com.cswm.assignment.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.dto.OrderDto;
import com.cswm.assignment.model.dto.OrderStatisticsDto;

@Service
public interface OrderService {

	OrderStatisticsDto getOrderStats(Long orderId);

	List<OrderDto> addExecutionQuantityToOrders(Set<OrderDto> validOrderdDtos, Long accumltdOrders, Long effectiveQuanty);
}
