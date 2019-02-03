package com.cswm.assignment.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cswm.assignment.applicationUtils.ErrorMessageEnum;
import com.cswm.assignment.exceptions.ApplicationException;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.modelvos.OrderBookStatsVo;
import com.cswm.assignment.service.OrderBookService;
import com.cswm.assignment.service.OrderService;

@RestController
@RequestMapping("/orderbooks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderBookController {

	@Autowired
	OrderBookService orderBookService;

	@Autowired
	OrderService orderService;

	@Context
	private ResourceContext resourceContext;

	@GetMapping
	public List<OrderBook> getOrderBooks() {
		return orderBookService.getOrderBooks();
	}

	@GetMapping("/{id}")
	public OrderBook getOrderBook(@PathVariable Long id) {
		return orderBookService.getOrderBook(id);
	}

	@GetMapping("/{orderBookId}/orderBookstats")
	public OrderBookStatsVo getOrderBookStats(@PathVariable Long orderBookId) {
		return orderBookService.getOrderBookStats(orderBookId);
	}

	@GetMapping("/{bookId}/orders")
	public List<Order> getOrdersByOrderBook(@PathVariable Long bookId) {
		return orderService.getOrdersByOrderBook(bookId);
	}

	@GetMapping("/{bookId}/orders/{orderId}")
	public Order getOrderByOrderBook(@PathVariable Long bookId, @PathVariable Long orderId) {
		return orderService.getOrderByOrderBook(bookId, orderId);

	}

	@PostMapping
	public OrderBook newOrderBook(@RequestBody OrderBook orderBook) {
		return orderBookService.saveBook(orderBook);
	}

	@PostMapping("/{bookId}/orders")
	public OrderBook newOrder(@RequestBody Order order, @PathVariable Long bookId) {
		return orderBookService.addOrderInBook(order, bookId);
	}

	@PutMapping("/{bookId}/orders/{orderId}")
	public ApplicationContextException updateOrder(@RequestBody Order order, @PathVariable Long orderId) {
		throw new ApplicationException(ErrorMessageEnum.UPDATE_ORDER_NOT_ALLOWED);

	}

	@PutMapping("/{id}")
	public OrderBook updateOrderBook(@RequestBody OrderBook orderBook, @PathVariable Long id) {
		return orderBookService.updateBook(orderBook, id);
	}

	@PutMapping("/{id}/orderBookStatus/{orderBookStatus}")
	public OrderBook openCloseOrderBook(@PathVariable String orderBookStatus, @PathVariable Long id) {
		return orderBookService.openCloseOrderBook(orderBookStatus, id);
	}

	@PutMapping("/{orderBookId}/executions")
	public OrderBook addExecutionToBook(@PathVariable Long orderBookId, @RequestBody Execution execution) {
		return orderBookService.addExecutionToBook(orderBookId, execution);
	}

	@DeleteMapping("/{id}")
	public OrderBook deleteOrderBook(@PathVariable Long id) {
		throw new ApplicationException(ErrorMessageEnum.DELETE_BOOK_NOT_SUPPORTED);
	}

	@DeleteMapping("/{bookId}/orders/{orderId}")
	public void removeOrderFromBook(@PathVariable Long bookId, @PathVariable Long orderId) {
		orderService.removeOrderFromBook(bookId, orderId);

	}

}
