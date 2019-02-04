package com.cswm.assignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cswm.assignment.applicationUtils.ErrorMessageEnum;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.Message;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.Order.OrderStatus;
import com.cswm.assignment.model.Order.OrderType;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.model.OrderBook.ExecutionStatus;
import com.cswm.assignment.model.OrderBook.OrderBookStatus;
import com.cswm.assignment.modelvos.OrderBookStatsVo;
import com.cswm.assignment.service.ExecutionService;
import com.cswm.assignment.service.InstrumentService;
import com.cswm.assignment.serviceImpl.OrderBookServiceImpl.OrderTypesInStats;

public class orderBookTest extends AbstractTest {

	@Autowired
	InstrumentService instrumentService;
	@Autowired
	ExecutionService executionService;

	@Test
	public void getOrderBooksTest() throws Exception {
		String uri = "/orderbooks";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		OrderBook[] orderBooks = mapFromJson(content, OrderBook[].class);
		assertTrue(orderBooks.length == 2);
	}

	@Test
	public void getOrderBookTest() throws Exception {
		String uri = "/orderbooks/1001";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBook orderBook = mapFromJson(content, OrderBook.class);
		assertEquals(200, status);
		assertEquals("Book-1", orderBook.getOrderBookName());
		assertEquals(1, orderBook.getOrders().size());
		assertEquals("Instrument-1", orderBook.getInstrument().getInstrumentName());
		assertEquals("Execution-1", orderBook.getExecutions().iterator().next().getExecutionName());
	}

	@Test
	public void getOrderBookNotFound() throws Exception {
		String uri = "/orderbooks/2004";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(404, status);
		System.out.println(ErrorMessageEnum.ORDER_BOOK_NOT_FOUND.getMessage());
		System.out.println(message.getMessage());
		assertEquals(ErrorMessageEnum.ORDER_BOOK_NOT_FOUND.getMessage(), message.getMessage());
	}

	@Test
	public void getOrdersByOrderBkNotFoundTest() throws Exception {
		String uri = "/orderbooks/2004/orders";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(404, status);
		System.out.println(ErrorMessageEnum.ORDER_BOOK_NOT_FOUND.getMessage());
		System.out.println(message.getMessage());
		assertEquals(ErrorMessageEnum.ORDER_BOOK_NOT_FOUND.getMessage(), message.getMessage());
	}

	@Test
	public void getOrdersByOrderBookTest() throws Exception {
		String uri = "/orderbooks/1001/orders";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Order[] ordersForBook = mapFromJson(content, Order[].class);
		assertEquals(200, status);
		assertEquals("Order-1", ordersForBook[0].getOrderName());
		assertEquals("Instrument-1", ordersForBook[0].getInstrument().getInstrumentName());
		assertEquals(OrderType.MARKET_ORDER, ordersForBook[0].getOrderType());
	}

	@Test
	public void getOrderByOrderBookTest() throws Exception {
		String uri = "/orderbooks/1001/orders/1001";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Order ordersForBook = mapFromJson(content, Order.class);
		assertEquals(200, status);
		assertEquals("Order-1", ordersForBook.getOrderName());
		assertEquals("Instrument-1", ordersForBook.getInstrument().getInstrumentName());
		assertEquals(OrderType.MARKET_ORDER, ordersForBook.getOrderType());
	}

	@Test
	public void getOrderBookStatsTest() throws Exception {
		String uri = "/orderbooks/1001/orderBookstats";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBookStatsVo orderBookStats = mapFromJson(content, OrderBookStatsVo.class);
		assertEquals(200, status);
		assertTrue(1l == orderBookStats.getTotalNoOfOrders());
		assertTrue(1l == orderBookStats.getValidOrderCount());
		assertTrue(200l == orderBookStats.getTotalNoofAccuOrders());
		assertTrue(200l == orderBookStats.getValidDemand());
		assertTrue(0 == orderBookStats.getInValidOrderCount());
		assertTrue(0 == orderBookStats.getExecutionQty());
		assertEquals("Order-1", orderBookStats.getOrderStats().get(OrderTypesInStats.BIGGEST_ORDER).getOrderName());
		assertEquals("Order-1", orderBookStats.getOrderStats().get(OrderTypesInStats.LATEST_ORDER).getOrderName());
		assertEquals("Order-1", orderBookStats.getOrderStats().get(OrderTypesInStats.EARLIEST_ORDER).getOrderName());
		assertEquals("Order-1", orderBookStats.getOrderStats().get(OrderTypesInStats.SMALLEST_ORDER).getOrderName());
	}

	@Test
	public void getOrderBookStatsNtFoundTest() throws Exception {
		String uri = "/orderbooks/2004/orderBookstats";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(404, status);
		System.out.println(ErrorMessageEnum.ORDER_BOOK_NOT_FOUND.getMessage());
		System.out.println(message.getMessage());
		assertEquals(ErrorMessageEnum.ORDER_BOOK_NOT_FOUND.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderBookTest() throws Exception {
		String uri = "/orderbooks";
		OrderBook orderBook = new OrderBook();
		orderBook.setOrderBookId(2001l);
		orderBook.setOrderBookName("Demo OrderBok");
		orderBook.setExecutions(null);
		orderBook.setExecutionStatus(ExecutionStatus.NOT_EXECUTED);
		
		Instrument instrument = new Instrument();
		instrument.setInstrumentId(1001l);
		instrument.setCreatedBy("Jnit Test");
		instrument.setCreatedOn(new Date());
		instrument.setInstrumentName("New Instrument");
		orderBook.setInstrument(instrument);
		orderBook.setOrderBookStatus(OrderBookStatus.OPEN);
		String inputJson = super.mapToJson(orderBook);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBook createdOrderBook = mapFromJson(content, OrderBook.class);
		assertEquals(200, status);
		assertNotEquals(null, createdOrderBook.getOrderBookId());
		assertNotEquals(null, createdOrderBook.getInstrument().getInstrumentId());
	}

	@Test
	public void closeBook() throws Exception {
		String uri = "/orderbooks/1001/orderBookStatus/CLOSE";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBook createdOrderBook = mapFromJson(content, OrderBook.class);
		assertEquals(200, status);
		assertEquals(OrderBookStatus.CLOSED, createdOrderBook.getOrderBookStatus());
	}

	@Test
	public void openBook() throws Exception {
		String uri = "/orderbooks/1001/orderBookStatus/OPEN";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBook createdOrderBook = mapFromJson(content, OrderBook.class);
		assertEquals(200, status);
		assertEquals(OrderBookStatus.OPEN, createdOrderBook.getOrderBookStatus());
	}

	@Test
	public void newOrderBookClosedTest() throws Exception {
		String uri = "/orderbooks";
		OrderBook orderBook = new OrderBook();
		orderBook.setOrderBookId(2001l);
		orderBook.setOrderBookName("Demo OrderBok");
		orderBook.setExecutions(null);
		orderBook.setExecutionStatus(ExecutionStatus.NOT_EXECUTED);
		Instrument instrument = new Instrument();
		instrument.setCreatedBy("Jnit Test");
		instrument.setCreatedOn(new Date());
		instrument.setInstrumentName("New Instrument");
		orderBook.setInstrument(instrument);
		orderBook.setOrderBookStatus(OrderBookStatus.CLOSED);
		String inputJson = super.mapToJson(orderBook);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_BOOK_CAN_NOT_BECLOSED.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderBookNoInstrumentTest() throws Exception {
		String uri = "/orderbooks";
		OrderBook orderBook = new OrderBook();
		orderBook.setOrderBookId(2001l);
		orderBook.setOrderBookName("Demo OrderBok");
		orderBook.setExecutions(null);
		orderBook.setExecutionStatus(ExecutionStatus.NOT_EXECUTED);
		orderBook.setOrderBookStatus(OrderBookStatus.OPEN);
		String inputJson = super.mapToJson(orderBook);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.BOOK_WITHOUT_INSTRUMENT.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderBookExecutionTest() throws Exception {
		String uri = "/orderbooks";
		OrderBook orderBook = new OrderBook();
		orderBook.setOrderBookId(2001l);
		orderBook.setOrderBookName("Demo OrderBok");
		Execution execution = new Execution();
		execution.setCreatedBy("Unit test");
		execution.setCreatedOn(new Date());
		execution.setExecutionName("Unit test Execution");
		execution.setPrice(2000d);
		execution.setQuantity(200l);
		Set<Execution> executions = new HashSet<Execution>();
		executions.add(execution);
		orderBook.setExecutions(executions);
		orderBook.setExecutionStatus(ExecutionStatus.NOT_EXECUTED);
		Instrument instrument = new Instrument();
		instrument.setCreatedBy("Jnit Test");
		instrument.setCreatedOn(new Date());
		instrument.setInstrumentName("New Instrument");
		orderBook.setInstrument(instrument);
		orderBook.setOrderBookStatus(OrderBookStatus.OPEN);
		String inputJson = super.mapToJson(orderBook);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.CAN_NOT_ADD_EXECUTION_IN_CREATION.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderBookExecutedTest() throws Exception {
		String uri = "/orderbooks";
		OrderBook orderBook = new OrderBook();
		orderBook.setOrderBookId(2001l);
		orderBook.setOrderBookName("Demo OrderBok");
		orderBook.setExecutions(null);
		orderBook.setCreatedBy("Unit test");
		orderBook.setCreatedOn(new Date());
		orderBook.setExecutionStatus(ExecutionStatus.EXECUTED);
		Instrument instrument = new Instrument();
		instrument.setCreatedBy("Jnit Test");
		instrument.setCreatedOn(new Date());
		instrument.setInstrumentName("New Instrument");
		orderBook.setInstrument(instrument);
		orderBook.setOrderBookStatus(OrderBookStatus.OPEN);
		String inputJson = super.mapToJson(orderBook);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.BOOK_EXECUTED_IN_CREATION.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderBookNtNameTest() throws Exception {
		String uri = "/orderbooks";
		OrderBook orderBook = new OrderBook();
		orderBook.setOrderBookId(2001l);
		orderBook.setCreatedBy("Unit test");
		orderBook.setCreatedOn(new Date());
		orderBook.setExecutions(null);
		orderBook.setExecutionStatus(ExecutionStatus.NOT_EXECUTED);
		Instrument instrument = new Instrument();
		instrument.setCreatedBy("Jnit Test");
		instrument.setCreatedOn(new Date());
		instrument.setInstrumentName("New Instrument");
		instrument.setCreatedOn(new Date());
		instrument.setInstrumentName("New Instrument");
		orderBook.setInstrument(instrument);
		orderBook.setOrderBookStatus(OrderBookStatus.OPEN);
		String inputJson = super.mapToJson(orderBook);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.BOOK_NAME_BLANK.getMessage(), message.getMessage());
	}

	@Test
	public void deleteOrderBookTest() throws Exception {
		String uri = "/orderbooks/1001";
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.delete(uri).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.DELETE_BOOK_NOT_SUPPORTED.getMessage(), message.getMessage());
	}

	@Test
	public void removeOrderFromBookTest() throws Exception {
		String uri = "/orderbooks/1001/orders/1001";
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.delete(uri).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.REMOVE_ORDER_ORDERBOOK.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderTest() throws Exception {
		Instrument instrument = instrumentService.getInstrument(1001l);
		openBook();
		String uri = "/orderbooks/1001/orders";
		Order order = new Order("Unit Test Order", instrument, 200l, 400d, OrderStatus.VALID, OrderType.LIMIT_ORDER,
				null, 2d, "Unit Test Crated", new Date());
		String inputJson = mapToJson(order);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBook orderBook = mapFromJson(content, OrderBook.class);
		assertEquals(200, status);
		assertEquals(2, orderBook.getOrders().size());
	}

	@Test
	public void newOrderClosedBookTest() throws Exception {
		Instrument instrument = instrumentService.getInstrument(1001l);
		closeBook();
		String uri = "/orderbooks/1001/orders";
		Order order = new Order("Unit Test Order", instrument, 200l, 400d, OrderStatus.VALID, OrderType.LIMIT_ORDER,
				null, 2d, "Unit Test Crated", new Date());
		String inputJson = mapToJson(order);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ADD_ORDER_ORDERBOOK_CLOSED.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderNoNameTest() throws Exception {
		Instrument instrument = instrumentService.getInstrument(1001l);
		openBook();
		String uri = "/orderbooks/1001/orders";
		Order order = new Order("", instrument, 200l, 400d, OrderStatus.VALID, OrderType.LIMIT_ORDER, null, 2d,
				"Unit Test Crated", new Date());
		String inputJson = mapToJson(order);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_NAME_INVALID.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderNoQtyTest() throws Exception {
		Instrument instrument = instrumentService.getInstrument(1001l);
		openBook();
		String uri = "/orderbooks/1001/orders";
		Order order = new Order("Unit Test Crated", instrument, null, 400d, OrderStatus.VALID, OrderType.LIMIT_ORDER,
				null, 2d, "Unit Test Crated", new Date());
		String inputJson = mapToJson(order);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_QUANTITY_INVALID.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderNoPriceTest() throws Exception {
		Instrument instrument = instrumentService.getInstrument(1001l);
		openBook();
		String uri = "/orderbooks/1001/orders";
		Order order = new Order("Unit Test Crated", instrument, 200l, null, OrderStatus.VALID, OrderType.MARKET_ORDER,
				null, 2d, "Unit Test Crated", new Date());
		String inputJson = mapToJson(order);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_PRICE_INVALID.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderNoTypeTest() throws Exception {
		Instrument instrument = instrumentService.getInstrument(1001l);
		openBook();
		String uri = "/orderbooks/1001/orders";
		Order order = new Order("Unit Test Crated", instrument, 200l, 400d, OrderStatus.VALID, null, null, 2d,
				"Unit Test Crated", new Date());
		String inputJson = mapToJson(order);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_TYPE_INVALID.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderDiffInstrumentTest() throws Exception {
		Instrument instrument = instrumentService.getInstrument(1003l);
		String uri = "/orderbooks/1001/orders";
		Order order = new Order("Unit Test Order", instrument, 200l, 400d, OrderStatus.VALID, OrderType.LIMIT_ORDER,
				null, 2d, "Unit Test Crated", new Date());
		String inputJson = mapToJson(order);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_NOT_BELONG_TO_INSTRUMENT.getMessage(), message.getMessage());
	}

	@Test
	public void addExecutionToBookTest() throws Exception {
		Execution execution = executionService.getExecution(1001l);
		execution.setExecutionId(1010l);
		execution.setQuantity(0l);
		OrderBook orderBook = orderBookService.getOrderBook(1001l);
		orderBook.setExecutionStatus(ExecutionStatus.NOT_EXECUTED);
		orderBookService.updateBook(orderBook, orderBook.getOrderBookId());
		closeBook();
		String uri = "/orderbooks/1001/executions";
		String inputJson = mapToJson(execution);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		orderBook = mapFromJson(content, OrderBook.class);
		assertEquals(200, status);
		assertEquals(2, orderBook.getExecutions().size());
	}

	@Test
	public void addExecutionMoreThanOrderTest() throws Exception {
		Execution execution = executionService.getExecution(1001l);
		execution.setExecutionId(1010l);
		execution.setQuantity(250l);
		closeBook();
		OrderBook orderBook = orderBookService.getOrderBook(1001l);
		orderBook.setExecutionStatus(ExecutionStatus.NOT_EXECUTED);
		orderBookService.updateBook(orderBook, orderBook.getOrderBookId());
		String uri = "/orderbooks/1001/executions";
		String inputJson = mapToJson(execution);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.PARTIALLY_EXECUTED.getMessage(), message.getMessage());
	}

	@Test
	public void addExecutionToOpenBookTest() throws Exception {
		Execution execution = executionService.getExecution(1001l);
		execution.setExecutionId(1010l);
		openBook();
		String uri = "/orderbooks/1001/executions";
		String inputJson = mapToJson(execution);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.EXECUTION_CAN_NOT_BE_ADDED.getMessage(), message.getMessage());
	}

	@Test
	public void addExecutionToExecutedBookTest() throws Exception {
		Execution execution = executionService.getExecution(1001l);
		execution.setExecutionId(1010l);
		closeBook();
		OrderBook orderBook = orderBookService.getOrderBook(1001l);
		orderBook.setExecutionStatus(ExecutionStatus.EXECUTED);
		orderBookService.updateBook(orderBook, orderBook.getOrderBookId());
		String uri = "/orderbooks/1001/executions";
		String inputJson = mapToJson(execution);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_BOOK_EXECUTED.getMessage(), message.getMessage());
	}

}
