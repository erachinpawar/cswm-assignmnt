package com.cswm.assignment.service;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.dto.ExecutionDto;
import com.cswm.assignment.model.dto.OrderBookDto;
import com.cswm.assignment.model.dto.OrderBookStatisticsDto;
import com.cswm.assignment.model.dto.OrderDto;

@Service
public interface OrderBookService {


	OrderBookDto createOrderBook(OrderBookDto orderBookDto);

	OrderDto addOrderToOrderBook(Long orderBookId, OrderDto orderDto);

	OrderBookDto closeOrderBook(Long orderBookId);

	OrderBookDto addExecutionToBook(Long orderBookId, ExecutionDto executionDto);

	OrderBookStatisticsDto getOrderBookStats(Long orderBookId);


}
