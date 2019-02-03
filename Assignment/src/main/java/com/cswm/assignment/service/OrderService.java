package com.cswm.assignment.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.modelvos.OrderStatsVo;

@Service
public interface OrderService {

	List<Order> getOrdersByOrderBook(Long orderBookId);

	Order getOrderByOrderBook(Long orderBookId, Long orderId);

	void removeOrderFromBook(Long orderBookId, Long orderId);

	List<Order> getAllOrders();

	Order getOrder(Long orderId);

	Order deleteOrder(Long orderId);

	Collection<Order> getOrdersByInstruments(Instrument instrument);

	List<Order> getValidOrders(OrderBook orderBook);

	Long getAccOrdersFromValidOrders(List<Order> validOrders);

	double getTotExecQtyValidOrders(List<Order> validOrders);

	List<Order> addExecutionQuantityToOrders(List<Order> validOrders, Double perOrderExec);

	Order getBiggestOrderForBook(OrderBook orderBook);

	Order getSmallestOrderForBook(OrderBook orderBook);

	Order getEarliestOrderInBook(OrderBook orderBook);

	Order getLatestOrderInBook(OrderBook orderBook);

	OrderStatsVo getOrderStats(Long orderId);

	OrderBook addOrderInBook(Order order, OrderBook orderBook);

	Set<Order> getOrdersFromDBforBook(Long orderBookId);


	
}
