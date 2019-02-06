package com.cswm.assignment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.modelvos.OrderBookStatsVo;

@Service
public interface OrderBookService {

	List<OrderBook> getAllOrderBooks();

	OrderBook getOrderBook(Long orderBookId);

	OrderBook createOrderBook(OrderBook orderBook);

	OrderBook addOrderToOrderBook(Long orderBookId, Order order);

	OrderBook closeOrderBook(Long orderBookId);

	OrderBook addExecutionToBook(Long orderBookId, Execution execution);

	OrderBookStatsVo getOrderBookStats(Long orderBookId);

	OrderBook createDefaultOrderBook();
}
