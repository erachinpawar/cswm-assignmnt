package com.cswm.assignment.service;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.dto.inputDto.AddOrderInputDto;
import com.cswm.assignment.model.dto.inputDto.ExecutionInputDto;
import com.cswm.assignment.model.dto.inputDto.OrderBookInputDto;
import com.cswm.assignment.model.dto.ouputDto.ClosedOrderBookOutputDto;
import com.cswm.assignment.model.dto.ouputDto.OrderBookOutputDto;
import com.cswm.assignment.model.dto.ouputDto.OrderBookStatisticsOutputDto;
import com.cswm.assignment.model.dto.ouputDto.OrderOutputDto;

@Service
public interface OrderBookService {


	/**
	 * Used to create the order book
	 * @param orderBookCreateInputDto
	 * @return updated order book object after creating
	 */
	OrderBookOutputDto createOrderBook(OrderBookInputDto orderBookCreateInputDto);

	/**
	 * Used to add order to specified order book
	 * @param orderBookId
	 * @param orderDto
	 * @return updated order book with order book details after adding to order book
	 */
	OrderOutputDto addOrderToOrderBook(Long orderBookId, AddOrderInputDto addOrderInputDto);

	/**
	 * Used to close the orderbook
	 * @param orderBookId
	 * @return updated order book object after closing
	 */
	ClosedOrderBookOutputDto closeOrderBook(Long orderBookId);

	/**
	 * add execution to the order.
	 * @param orderBookId
	 * @param executionInputDto
	 * @return updated order book after adding an execution.
	 */
	OrderBookOutputDto addExecutionToBook(Long orderBookId, ExecutionInputDto executionInputDto);

	
	/**
	 * Used to get the order book statistics 
	 * amount of orders in each book, demand, 
	 * the biggest order and the smallest order, the earliest order entry, the last order entry, 
	 * limit break down (a table with limit prices and demand per limit price). Demand = accumulated order quantity.
	 * @param orderBookId
	 * @return OrderBookStatisticsDto object containing order book statistics
	 */
	OrderBookStatisticsOutputDto getOrderBookStats(Long orderBookId);
}
