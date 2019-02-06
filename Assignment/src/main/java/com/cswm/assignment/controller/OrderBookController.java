package com.cswm.assignment.controller;

import java.util.List;

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
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.modelvos.OrderBookStatsVo;
import com.cswm.assignment.modelvos.OrderStatsVo;
import com.cswm.assignment.service.OrderBookService;
import com.cswm.assignment.service.OrderService;

@Controller
@RequestMapping("/orderbooks")
public class OrderBookController {

	@Autowired
	OrderBookService orderBookService;
	@Autowired
	private OrderService orderService;

	@RequestMapping(value = UrlConstants.URL_ALL_ORDER_BOOKS, method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody List<OrderBook> getAllOrderBooks() {
		return orderBookService.getAllOrderBooks();
	}

	@RequestMapping(value = UrlConstants.URL_GET_ORDER_BOOK, method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderBook getOrderBook(@PathVariable Long orderBookId) {
		return orderBookService.getOrderBook(orderBookId);
	}

	@RequestMapping(value = UrlConstants.URL_GET_ORDER_BOOK_STATISTICS, method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderBookStatsVo getOrderBookStats(@PathVariable Long orderBookId) {
		return orderBookService.getOrderBookStats(orderBookId);
	}

	@RequestMapping(value = UrlConstants.URL_GET_ORDER_STATISTICS, method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderStatsVo getOrderStats(@PathVariable Long orderBookId, @PathVariable Long orderId) {
		return orderService.getOrderStats(orderId);
	}

	@RequestMapping(value = UrlConstants.URL_CREATE_ORDER_BOOK, method = RequestMethod.POST)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderBook createOrderBook(@RequestBody OrderBook orderBook) {
		return orderBookService.createOrderBook(orderBook);
	}

	@RequestMapping(value = UrlConstants.URL_ADD_ORDER_BOOK, method = RequestMethod.PUT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderBook addOrderToOrderBook(@RequestBody Order order, @PathVariable Long orderBookId) {
		return orderBookService.addOrderToOrderBook(orderBookId, order);
	}

	@RequestMapping(value = UrlConstants.URL_CLOSE_ORDER_BOOK, method = RequestMethod.PUT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderBook closeOrderBook(@PathVariable Long orderBookId) {
		return orderBookService.closeOrderBook(orderBookId);
	}

	@RequestMapping(value = UrlConstants.URL_EXECUTE_ORDER_BOOK, method = RequestMethod.PUT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody OrderBook addExecutionToBook(@PathVariable Long orderBookId,
			@RequestBody Execution execution) {
		return orderBookService.addExecutionToBook(orderBookId, execution);
	}

}
