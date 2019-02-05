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

import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.modelvos.OrderBookStatsVo;
import com.cswm.assignment.modelvos.OrderStatsVo;
import com.cswm.assignment.service.OrderBookService;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Controller
@RequestMapping("/orderbooks")
public class OrderBookController {

	@Autowired
	OrderBookService orderBookService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public @ResponseBody List<OrderBook> getOrderBooks() {
		return orderBookService.getOrderBooks();
	}

	@RequestMapping(value = "/{orderBookId}", method = RequestMethod.GET)
	public @ResponseBody OrderBook getOrderBook(@PathVariable Long orderBookId) {
		return orderBookService.getOrderBook(orderBookId);
	}
	
	@RequestMapping(value="/{orderBookId}/orderBookstats", method = RequestMethod.GET)
	public @ResponseBody OrderBookStatsVo getOrderBookStats(@PathVariable Long orderBookId) {
		return orderBookService.getOrderBookStats(orderBookId);
	}

	@RequestMapping(value = "/{orderBookId}/orders/{orderId}/orderStats", method = RequestMethod.GET)
	public @ResponseBody OrderStatsVo getOrderStats(@PathVariable Long orderBookId, @PathVariable Long orderId) {
		return orderBookService.getOrderStats(orderBookId, orderId);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public @ResponseBody OrderBook createOrderBook(@RequestBody OrderBook orderBook) {
		return orderBookService.saveBook(orderBook);
	}

	@RequestMapping(value="/{orderBookId}/orders", method=RequestMethod.PUT)
	public @ResponseBody OrderBook addOrderToOrderBook(@RequestBody Order order, @PathVariable Long orderBookId) {
		return orderBookService.addOrderInBook(orderBookId, order);
	}

	@RequestMapping(value="/{orderBookId}/orderBookStatus/{orderBookStatus}", method=RequestMethod.PUT)
	public @ResponseBody OrderBook openCloseOrderBook(@PathVariable Long orderBookId, @PathVariable String orderBookStatus) {
		return orderBookService.openCloseOrderBook(orderBookStatus, orderBookId);
	}

	@RequestMapping(value="/{orderBookId}/executions", method=RequestMethod.PUT)
	public @ResponseBody OrderBook addExecutionToBook(@PathVariable Long orderBookId, @RequestBody Execution execution) {
		return orderBookService.addExecutionToBook(orderBookId, execution);
	}
	
	@RequestMapping(value="/{orderBookId}", method=RequestMethod.PUT)
	public OrderBook updateOrderBook(@RequestBody OrderBook orderBook, @PathVariable Long orderBookId) {
		return orderBookService.updateBook(orderBook, orderBookId);
	}
	
}
