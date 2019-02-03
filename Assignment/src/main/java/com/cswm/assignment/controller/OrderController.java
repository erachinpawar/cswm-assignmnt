package com.cswm.assignment.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cswm.assignment.model.Order;
import com.cswm.assignment.modelvos.OrderStatsVo;
import com.cswm.assignment.repository.OrderRepository;
import com.cswm.assignment.service.OrderService;

@RestController
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderService orderService;

	@GetMapping
	public List<Order> getOrders() {
		return orderService.getAllOrders();

	}

	@GetMapping("/{orderId}")
	public Order getOrder(@PathVariable Long orderId) {
		return orderService.getOrder(orderId);

	}

	@DeleteMapping("/{orderId}")
	public Order deleteOrder(@PathVariable Long orderId) {
		return orderService.deleteOrder(orderId);

	}

	@GetMapping("/{orderId}/orderStats")
	public OrderStatsVo getOrderStats(@PathVariable Long orderId) {
		return orderService.getOrderStats(orderId);
	}

}
