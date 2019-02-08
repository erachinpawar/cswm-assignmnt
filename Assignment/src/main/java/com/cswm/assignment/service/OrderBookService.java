package com.cswm.assignment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.dto.ExecutionDto;
import com.cswm.assignment.model.dto.OrderBookDto;
import com.cswm.assignment.model.dto.OrderBookStatisticsDto;
import com.cswm.assignment.model.dto.OrderDto;

@Service
public interface OrderBookService {

	List<OrderBookDto> getAllOrderBooks();

	OrderBookDto getOrderBook(Long orderBookId);

	OrderBookDto createOrderBook(OrderBookDto orderBookDto);

	OrderBookDto addOrderToOrderBook(Long orderBookId, OrderDto orderDto);

	OrderBookDto closeOrderBook(Long orderBookId);

	OrderBookDto addExecutionToBook(Long orderBookId, ExecutionDto executionDto);

	OrderBookStatisticsDto getOrderBookStats(Long orderBookId);

	OrderBookDto createDefaultOrderBook();

}
