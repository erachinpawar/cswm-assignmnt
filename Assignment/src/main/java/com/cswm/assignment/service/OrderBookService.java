package com.cswm.assignment.service;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.dto.OrderBookDto;
import com.cswm.assignment.model.dto.OrderBookStatisticsDto;
import com.cswm.assignment.model.dto.OrderBookValidInValidStatistics;
import com.cswm.assignment.model.dto.OrderDto;
import com.cswm.assignment.model.dto.inputDto.AddOrderInputDto;
import com.cswm.assignment.model.dto.inputDto.ExecutionInputDto;
import com.cswm.assignment.model.dto.inputDto.OrderBookInputDto;

@Service
public interface OrderBookService {


	/**
	 * Used to create the order book
	 * @param orderBookCreateInputDto
	 * @return updated order book object after creating
	 */
	OrderBookDto createOrderBook(OrderBookInputDto orderBookCreateInputDto);

	/**
	 * Used to add order to specified order book
	 * @param orderBookId
	 * @param orderDto
	 * @return updated order book with order book details after adding to order book
	 */
	OrderDto addOrderToOrderBook(Long orderBookId, AddOrderInputDto addOrderInputDto);

	/**
	 * Used to close the orderbook
	 * @param orderBookId
	 * @return updated order book object after closing
	 */
	OrderBookDto closeOrderBook(Long orderBookId);

	/**
	 * add execution to the order.
	 * @param orderBookId
	 * @param executionInputDto
	 * @return updated order book after adding an execution.
	 */
	OrderBookDto addExecutionToBook(Long orderBookId, ExecutionInputDto executionInputDto);

	
	/**
	 * Used to get the order book statistics 
	 * amount of orders in each book, demand, 
	 * the biggest order and the smallest order, the earliest order entry, the last order entry, 
	 * limit break down (a table with limit prices and demand per limit price). Demand = accumulated order quantity.
	 * @param orderBookId
	 * @return OrderBookStatisticsDto object containing order book statistics
	 */
	OrderBookStatisticsDto getOrderBookStats(Long orderBookId);

	/**
	 * Used to get the detailed order book statistics at valid and invalid order level.
	 * amount of valid/invalid orders in each book, 
	 * valid/invalid demand, 
	 * the biggest order
	 * the smallest order, the earliest order entry, the last order entry, 
	 * limit break down (a table with limit prices and demand limit price)
	 * accumulated execution quantity and execution price.
	 * @param orderBookId
	 * @return OrderBookValidInValidStatistics object containing order book statistics at valid and invalid order level
	 */
	OrderBookValidInValidStatistics getOrderBookValidInvalidOrdersStats(Long orderBookId);


}
