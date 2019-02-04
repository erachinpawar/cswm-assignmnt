package com.cswm.assignment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.modelvos.OrderBookStatsVo;

@Service
public interface OrderBookService  {

	List<OrderBook>  getOrderBooks();

	OrderBook getOrderBook(Long bookId);

	OrderBook saveBook(OrderBook orderBook);

	OrderBook updateBook(OrderBook orderBook,Long bookId);

	List<OrderBook> getOrderBooksByInstruments(Instrument instrument);

	OrderBook openCloseOrderBook(String orderBookStatus, Long orderBookId);

	OrderBook addExecutionToBook(Long orderBookId, Execution execution);

	OrderBookStatsVo getOrderBookStats(Long orderBookId);

	OrderBook addOrderInBook(Order order, Long bookId);

	OrderBook createDefaultOrderBook();


}
