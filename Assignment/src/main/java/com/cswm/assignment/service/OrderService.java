package com.cswm.assignment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.modelvos.OrderStatsVo;

@Service
public interface OrderService {

	OrderBook addOrderInBook(Order order, OrderBook orderBook);

	List<Order> getValidOrders(OrderBook orderBook, Execution execution);

	Long getAccOrdersFromValidOrders(List<Order> validOrders);

	Long getTotExecQtyValidOrders(List<Order> validOrders);

	
	Order getBiggestOrderForBook(OrderBook orderBook);

	Order getSmallestOrderForBook(OrderBook orderBook);

	Order getEarliestOrderInBook(OrderBook orderBook);

	Order getLatestOrderInBook(OrderBook orderBook);

	OrderStatsVo getOrderStats(Long orderId);

	List<Order> addExecutionQuantityToOrders(List<Order> validOrders, Long accumltdOrders, Long effectiveQuanty);
}
