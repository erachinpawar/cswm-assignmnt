package com.cswm.assignment.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cswm.assignment.UrlConstants;
import com.cswm.assignment.model.dto.ExecutionDto;
import com.cswm.assignment.model.dto.OrderBookDto;
import com.cswm.assignment.model.dto.OrderBookStatisticsDto;
import com.cswm.assignment.model.dto.OrderDto;
import com.cswm.assignment.model.dto.OrderStatisticsDto;
import com.cswm.assignment.service.OrderBookService;
import com.cswm.assignment.service.OrderService;

@Controller
public class OrderBookController {

	@Autowired
	OrderBookService orderBookService;
	@Autowired
	OrderService orderService;

	/*
	 * Used to get statistics of the order book
	 * URI : /orderbooks/{orderBookId}/stastitics
	 */
	@RequestMapping(value = UrlConstants.URL_GET_ORDER_BOOK_STATISTICS, method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderBookStatisticsDto getOrderBookStats(@PathVariable Long orderBookId) {
		return orderBookService.getOrderBookStats(orderBookId);
	}

	/*
	 * Used to get statistics of the order in a order book
	 * URI : /orderbooks/{orderBookId}/orderStatistics/{orderId}
	 */
	@RequestMapping(value = UrlConstants.URL_GET_ORDER_STATISTICS, method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderStatisticsDto getOrderStats(@PathVariable Long orderBookId, @PathVariable Long orderId) {
		return orderService.getOrderStats(orderId);
	}

	/*
	 * Used to Create a new order book
	 * URI : /orderbooks/create
	 */
	@RequestMapping(value = UrlConstants.URL_CREATE_ORDER_BOOK, method = RequestMethod.POST)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderBookDto createOrderBook(@RequestBody OrderBookDto orderBookDto) {
		return orderBookService.createOrderBook(orderBookDto);
	}

	/*
	 * Used to add an order in a orderbook
	 * URI : /orderbooks/{orderBookId}/orders
	 */
	@RequestMapping(value = UrlConstants.URL_ADD_ORDER_BOOK, method = RequestMethod.PUT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderDto addOrderToOrderBook(@RequestBody OrderDto orderDto, @PathVariable Long orderBookId) {
		return orderBookService.addOrderToOrderBook(orderBookId, orderDto);
	}

	/*
	 * Used to Close a orderbook
	 * URI : /orderbooks/{orderBookId}/close
	 */
	@RequestMapping(value = UrlConstants.URL_CLOSE_ORDER_BOOK, method = RequestMethod.PUT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderBookDto closeOrderBook(@PathVariable Long orderBookId) {
		return orderBookService.closeOrderBook(orderBookId);
	}
	/*
	 * Used to execute Order book
	 * URI : /orderbooks/{orderBookId}/execute
	 */
	@RequestMapping(value = UrlConstants.URL_EXECUTE_ORDER_BOOK, method = RequestMethod.PUT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderBookDto addExecutionToBook(@PathVariable Long orderBookId,
			@RequestBody ExecutionDto executionDto) {
		 OrderBookDto orderBookDto = orderBookService.addExecutionToBook(orderBookId, executionDto);
		 return orderBookDto;
			 
	}

}
