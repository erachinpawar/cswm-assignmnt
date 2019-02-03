package com.cswm.assignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cswm.assignment.applicationUtils.ErrorMessageEnum;
import com.cswm.assignment.model.Message;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.Order.OrderStatus;
import com.cswm.assignment.modelvos.OrderStatsVo;

public class OrderTest extends AbstractTest {

	
	@Test
	public void getOrderTest() throws Exception {
		String uri = "/orders/1001";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Order order = mapFromJson(content, Order.class);
		assertEquals("Order-1", order.getOrderName());
	}
	
	@Test
	public void getOrderNotFoundTest() throws Exception {
		String uri = "/orders/2001";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(404, status);
		assertEquals(ErrorMessageEnum.ORDER_NOT_FOUND.getMessage(), message.getMessage());
	}
	
	
	@Test
	public void deleteOrderTest() throws Exception {
		String uri = "/orders/1002";
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.delete(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Order order = mapFromJson(content, Order.class);
		assertEquals(200, status);
		assertEquals(null, order.getOrderBook());
	}
	
	@Test
	public void deleteOrderInBookTest() throws Exception {
		String uri = "/orders/1001";
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.delete(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.REMOVE_ORDER_ORDERBOOK_CLOSED.getMessage(), message.getMessage());
	}
	
	@Test
	public void getOrderStatsTest() throws Exception {
		String uri = "/orders/1001/orderStats";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderStatsVo orderStatsVo = mapFromJson(content, OrderStatsVo.class);
		assertEquals(200, status);
		assertTrue(OrderStatus.VALID.equals(orderStatsVo.getOrderStatus()));
		assertEquals("Order-1", orderStatsVo.getOrder().getOrderName());
		assertEquals(200, orderStatsVo.getOrder().getOrderprice().intValue());
		assertEquals("0.0", orderStatsVo.getExecutionPrice().toString());
	}
	
	@Test
	public void getOrdersTest() throws Exception {
		String uri = "/orders";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Order[] orders = mapFromJson(content, Order[].class);
		assertEquals(2, orders.length);
	}

}
