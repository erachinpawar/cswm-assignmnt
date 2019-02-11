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
import com.cswm.assignment.model.dto.OrderBookDto;
import com.cswm.assignment.model.dto.OrderBookStatisticsDto;
import com.cswm.assignment.model.dto.OrderBookValidInValidStatistics;
import com.cswm.assignment.model.dto.OrderDto;
import com.cswm.assignment.model.dto.OrderStatisticsDto;
import com.cswm.assignment.model.dto.inputDto.AddOrderInputDto;
import com.cswm.assignment.model.dto.inputDto.ExecutionInputDto;
import com.cswm.assignment.model.dto.inputDto.OrderBookInputDto;
import com.cswm.assignment.service.OrderBookService;
import com.cswm.assignment.service.OrderService;

import io.swagger.annotations.ApiOperation;

@Controller
public class OrderBookController {

	@Autowired
	OrderBookService orderBookService;
	@Autowired
	OrderService orderService;

	/**
	 * Used to get statistics of the order book URI :
	 * /orderbooks/{orderBookId}/stastitics
	 * 
	 * @param orderBookId
	 * @return
	 */
	@RequestMapping(value = UrlConstants.URL_GET_ORDER_BOOK_STATISTICS, method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get order book statistics for a order book id", response = OrderBookStatisticsDto.class)
	public @ResponseBody OrderBookStatisticsDto getOrderBookStats(@PathVariable Long orderBookId) {
		return orderBookService.getOrderBookStats(orderBookId);
	}

	/**
	 * Used to get valid invalid statistics of the order book URI
	 * :/orderbooks/{orderBookId}/detailedstastitics
	 * 
	 * @param orderBookId
	 * @return
	 */
	@ApiOperation(value = "Get order book detailed statistics at valid and invalid order level for a order book id", response = OrderBookValidInValidStatistics.class)
	@RequestMapping(value = UrlConstants.URL_GET_ORDER_BOOK_VALID_INVALID_STATISTICS, method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderBookValidInValidStatistics getOrderBookValidInvalidOrdersStats(@PathVariable Long orderBookId) {
		return orderBookService.getOrderBookValidInvalidOrdersStats(orderBookId);
	}

	/**
	 * Used to get statistics of the order in a order book URI
	 * :/orderbooks/{orderBookId}/orderStatistics/{orderId}
	 * 
	 * @param orderBookId
	 * @param orderId
	 * @return
	 */
	@ApiOperation(value = "Get order statistics for a order id ", response = OrderStatisticsDto.class)
	@RequestMapping(value = UrlConstants.URL_GET_ORDER_STATISTICS, method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderStatisticsDto getOrderStats(@PathVariable Long orderBookId, @PathVariable Long orderId) {
		return orderService.getOrderStats(orderId);
	}

	/**
	 * Used to Create a new order book URI : /orderbooks/create
	 * 
	 * @param orderBookDto
	 * @return
	 */
	@ApiOperation(value = "Create order book ", response = OrderBookDto.class)
	@RequestMapping(value = UrlConstants.URL_CREATE_ORDER_BOOK, method = RequestMethod.POST)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderBookDto createOrderBook(@RequestBody OrderBookInputDto orderBookCreateInputDto) {
		return orderBookService.createOrderBook(orderBookCreateInputDto);
	}

	/**
	 * Used to add an order in a orderbook URI : /orderbooks/{orderBookId}/orders
	 * 
	 * @param orderDto
	 * @param orderBookId
	 * @return
	 */
	@ApiOperation(value = "add order to order book ", response = OrderDto.class)
	@RequestMapping(value = UrlConstants.URL_ADD_ORDER_BOOK, method = RequestMethod.PUT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderDto addOrderToOrderBook(@RequestBody AddOrderInputDto addOrderInputDto, @PathVariable Long orderBookId) {
		return orderBookService.addOrderToOrderBook(orderBookId, addOrderInputDto);
	}

	/**
	 * Used to Close a orderbook URI : /orderbooks/{orderBookId}/close
	 * 
	 * @param orderBookId
	 * @return
	 */
	@ApiOperation(value = "Close order book so that addition of more orders is not allowed and executions can be added", response = OrderBookDto.class)
	@RequestMapping(value = UrlConstants.URL_CLOSE_ORDER_BOOK, method = RequestMethod.PUT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderBookDto closeOrderBook(@PathVariable Long orderBookId) {
		return orderBookService.closeOrderBook(orderBookId);
	}

	/**
	 * Used to execute Order book URI : /orderbooks/{orderBookId}/execute
	 * 
	 * @param orderBookId
	 * @param executionDto
	 * @return
	 */
	@ApiOperation(value = "Used to add executions to order book", response = OrderBookDto.class)
	@RequestMapping(value = UrlConstants.URL_EXECUTE_ORDER_BOOK, method = RequestMethod.PUT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderBookDto addExecutionToBook(@PathVariable Long orderBookId, @RequestBody ExecutionInputDto executionInputDto ) {
		OrderBookDto orderBookDto = orderBookService.addExecutionToBook(orderBookId, executionInputDto);
		return orderBookDto;

	}

}
