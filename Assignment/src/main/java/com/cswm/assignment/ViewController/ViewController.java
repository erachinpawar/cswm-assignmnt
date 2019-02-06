package com.cswm.assignment.ViewController;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cswm.assignment.ApplicationConstants;
import com.cswm.assignment.controller.OrderBookController;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.model.dto.OrderBuilder;
import com.cswm.assignment.modelvos.OrderBookStatsVo;
import com.cswm.assignment.modelvos.OrderStatsVo;
import com.cswm.assignment.service.ExecutionService;
import com.cswm.assignment.service.InstrumentService;
import com.cswm.assignment.service.OrderBookService;
import com.cswm.assignment.service.OrderService;

@Controller
public class ViewController {

	@Autowired
	OrderBookController orderBookController;

	@Autowired
	OrderService orderController;

	@Autowired
	InstrumentService instrumentService;

	@Autowired
	OrderBookService orderBookService;
	
	@Autowired
	ExecutionService executionService;

	@Autowired
	OrderService orderService;

	@RequestMapping(value = "/admin/home", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("userName", "Welcome " + ApplicationConstants.DEFAULT_USER);
		modelAndView.addObject("adminMessage", "Wel-Come to Orders Management System");
		modelAndView.setViewName("admin/home");
		return modelAndView;
	}

	@RequestMapping(value = { "admin/myorderbook" }, method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/myorderbook");
		modelAndView.addObject("userName", "Welcome " + ApplicationConstants.DEFAULT_USER);
		List<OrderBook> orderBooks = orderBookController.getAllOrderBooks();
		modelAndView.addObject("orderBooks", orderBooks);
		modelAndView.addObject("adminMessage", "Orders Inventory");
		return modelAndView;
	}

	@RequestMapping(value = "/createOrderBook", method = RequestMethod.GET)
	public ModelAndView getCreateOrderBook() {
		ModelAndView modelAndView = new ModelAndView();
		OrderBook orderBook = orderBookService.createDefaultOrderBook();
		modelAndView.addObject("orderBook", orderBook);
		modelAndView.addObject("adminMessage", "Create - Order Book");
		modelAndView.addObject("userName", "Welcome : " + ApplicationConstants.DEFAULT_USER);
		modelAndView.setViewName("admin/editorderbook");
		return modelAndView;
	}

	@RequestMapping(value = "/orderBookEdit/{orderBookId}", method = RequestMethod.GET)
	public ModelAndView getOrderBook(@PathVariable long orderBookId) {
		ModelAndView modelAndView = new ModelAndView();
		OrderBook orderBook = orderBookController.getOrderBook(orderBookId);
		modelAndView.addObject("orderBook", orderBook);
		modelAndView.addObject("adminMessage", "Modify  - Order Book");
		modelAndView.addObject("userName", "Welcome : " + ApplicationConstants.DEFAULT_USER);
		modelAndView.setViewName("admin/editorderbook");
		return modelAndView;
	}

	@RequestMapping(value = "/orderBookClose/{orderBookId}", method = RequestMethod.GET)
	public String closeOrderBook(@PathVariable long orderBookId) {
		orderBookController.closeOrderBook(orderBookId);
		return "redirect:/admin/myorderbook";
	}


	@RequestMapping(value = "/saveOrderBook", method = RequestMethod.POST)
	public String saveOrderBook(OrderBook orderBook) {
		orderBook.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBook.setCreatedOn(LocalDateTime.now());
		if (null == orderBook.getOrderBookId())
			orderBookController.createOrderBook(orderBook);
		return "redirect:/admin/myorderbook";
	}

	@RequestMapping(value = "/addOrderToOrderBook/{orderBookId}", method = RequestMethod.GET)
	public ModelAndView addOrderToOrderBook(@PathVariable long orderBookId) {
		ModelAndView modelAndView = new ModelAndView();
		OrderBuilder orderBuilder = new OrderBuilder();
		OrderBook orderBook=orderBookService.getOrderBook(orderBookId);
		orderBuilder.setOrderBook(orderBook);
		orderBuilder.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBuilder.setCreatedOn(LocalDateTime.now());
		orderBuilder.setInstrument(orderBook.getInstrument());
		modelAndView.addObject("orderBuilder", orderBuilder);
		modelAndView.addObject("adminMessage", "Modify - Order Details");
		modelAndView.addObject("userName", "Welcome : " + ApplicationConstants.DEFAULT_USER);
		modelAndView.setViewName("admin/editorder");
		return modelAndView;
	}

	@RequestMapping(value = "/saveOrder", method = RequestMethod.POST)
	public String saveOrderBook(OrderBuilder orderBuilder) {
		orderBookController.addOrderToOrderBook(new Order(orderBuilder), orderBuilder.getOrderBook().getOrderBookId());
		return "redirect:/orderBookEdit/" + orderBuilder.getOrderBook().getOrderBookId();
	}
	
	@RequestMapping(value = "/addExecutionToOrderBook/{orderBookId}", method = RequestMethod.GET)
	public ModelAndView addExecution(@PathVariable long orderBookId) {
		ModelAndView modelAndView = new ModelAndView();
		OrderBook orderBook = orderBookController.getOrderBook(orderBookId);
		modelAndView.addObject("orderBook", orderBook);
		Execution execution=executionService.getTempExecutionForOrder(orderBook);
		modelAndView.addObject("execution", execution);
		modelAndView.addObject("adminMessage", "Order Book - Add Execution");
		modelAndView.addObject("userName", "Welcome : " + ApplicationConstants.DEFAULT_USER);
		modelAndView.setViewName("admin/addExecution");
		return modelAndView;
	}
	
	@RequestMapping(value = "/addExecution", method = RequestMethod.POST)
	public String addExecution(Execution execution) {
		orderBookController.addExecutionToBook(execution.getOrderBook().getOrderBookId(), execution);
		return "redirect:/orderBookEdit/" + execution.getOrderBook().getOrderBookId();
	}

	@RequestMapping(value = "getBookStats/{orderBookId}", method = RequestMethod.GET)
	public ModelAndView getOrderBookStats(@PathVariable long orderBookId) {
		ModelAndView modelAndView = new ModelAndView();
		OrderBookStatsVo orderBookStatsVo = orderBookController.getOrderBookStats(orderBookId);
		modelAndView.addObject("orderBookStatsVo", orderBookStatsVo);
		modelAndView.addObject("orderBook", orderBookStatsVo.getOrderBook());
		modelAndView.addObject("adminMessage", "Order Book - Statistics");
		modelAndView.addObject("userName", "Welcome : " + ApplicationConstants.DEFAULT_USER);
		modelAndView.setViewName("admin/orderBookStats");
		return modelAndView;
	}
	
	@RequestMapping(value = "/getOrderStats/{orderId}", method = RequestMethod.GET)
	public ModelAndView getOrderStats(@PathVariable long orderId) {
		ModelAndView modelAndView = new ModelAndView();
		OrderStatsVo orderStatsVo=orderController.getOrderStats(orderId);
		Order order = orderStatsVo.getOrder();
		modelAndView.addObject("orderStatsVo", orderStatsVo);
		modelAndView.addObject("adminMessage", "Order Statistics");
		modelAndView.addObject("order", order);
		modelAndView.addObject("userName", "Welcome : " + ApplicationConstants.DEFAULT_USER);
		modelAndView.setViewName("admin/orderStats");
		return modelAndView;
	}
	
}
