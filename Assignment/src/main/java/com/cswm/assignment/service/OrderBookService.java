package com.cswm.assignment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.modelvos.OrderBookStatsVo;
import com.cswm.assignment.modelvos.OrderStatsVo;

@Service
public interface OrderBookService {

	List<OrderBook> getOrderBooks();

	OrderBook getOrderBook(Long orderBookId);

	OrderBook saveBook(OrderBook orderBook);

	OrderBook addOrderInBook(Long orderBookId, Order order);

	OrderBook openCloseOrderBook(String orderBookStatus, Long orderBookId);

	OrderBook addExecutionToBook(Long orderBookId, Execution execution);

	OrderBookStatsVo getOrderBookStats(Long orderBookId);

	OrderStatsVo getOrderStats(Long orderBookId, Long orderId);

	OrderBook createDefaultOrderBook();

	OrderBook updateBook(OrderBook orderBook, Long orderBookId);

}
