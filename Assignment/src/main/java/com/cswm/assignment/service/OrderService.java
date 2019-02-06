package com.cswm.assignment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.modelvos.OrderStatsVo;

@Service
public interface OrderService {

	OrderStatsVo getOrderStats(Long orderId);

	List<Order> getValidOrders(OrderBook orderBook, Execution execution);

	List<Order> addExecutionQuantityToOrders(List<Order> validOrders, Long accumltdOrders, Long effectiveQuanty);
}
