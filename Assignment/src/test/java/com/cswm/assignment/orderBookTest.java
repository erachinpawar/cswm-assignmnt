package com.cswm.assignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cswm.assignment.applicationUtils.ErrorMessageEnum;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.Message;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.Order.OrderBuilder;
import com.cswm.assignment.model.Order.OrderStatus;
import com.cswm.assignment.model.Order.OrderType;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.model.OrderBook.ExecutionStatus;
import com.cswm.assignment.model.OrderBook.OrderBookStatus;
import com.cswm.assignment.modelvos.OrderBookStatsVo;
import com.cswm.assignment.modelvos.OrderBookStatsVo.OrderTypesInStats;
import com.cswm.assignment.service.ExecutionService;
import com.cswm.assignment.service.InstrumentService;


@FixMethodOrder(MethodSorters.JVM)
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
		assertEquals(2,orderBooks.length);
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
	public void getOrderBookStatsTest() throws Exception {
		String uri = "/orderbooks/1001/orderBookstats";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBookStatsVo orderBookStats = mapFromJson(content, OrderBookStatsVo.class);
		assertEquals(200, status);
		assertEquals(1l, orderBookStats.getTotalNoOfOrders().longValue());
		assertEquals(1l,orderBookStats.getValidOrderCount().longValue());
		assertEquals(200l ,orderBookStats.getTotalNoofAccuOrders().longValue());
		assertEquals(200l, orderBookStats.getValidDemand().longValue());
		assertEquals(0 ,orderBookStats.getInValidOrderCount().longValue());
		assertEquals(0 ,orderBookStats.getExecutionQty().longValue());
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
		instrument.setCreatedOn(LocalDateTime.now());
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
		assertEquals(OrderBookStatus.CLOSE, createdOrderBook.getOrderBookStatus());
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
		instrument.setCreatedOn(LocalDateTime.now());
		instrument.setInstrumentName("New Instrument");
		orderBook.setInstrument(instrument);
		orderBook.setOrderBookStatus(OrderBookStatus.CLOSE);
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
		execution.setCreatedOn(LocalDateTime.now());
		execution.setExecutionName("Unit test Execution");
		execution.setPrice(2000d);
		execution.setQuantity(200l);
		Set<Execution> executions = new HashSet<Execution>();
		executions.add(execution);
		orderBook.setExecutions(executions);
		orderBook.setExecutionStatus(ExecutionStatus.NOT_EXECUTED);
		Instrument instrument = new Instrument();
		instrument.setCreatedBy("Jnit Test");
		instrument.setCreatedOn(LocalDateTime.now());
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
		orderBook.setCreatedOn(LocalDateTime.now());
		orderBook.setExecutionStatus(ExecutionStatus.EXECUTED);
		Instrument instrument = new Instrument();
		instrument.setCreatedBy("Jnit Test");
		instrument.setCreatedOn(LocalDateTime.now());
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
		orderBook.setCreatedOn(LocalDateTime.now());
		orderBook.setExecutions(null);
		orderBook.setExecutionStatus(ExecutionStatus.NOT_EXECUTED);
		Instrument instrument = new Instrument();
		instrument.setCreatedBy("Jnit Test");
		instrument.setCreatedOn(LocalDateTime.now());
		instrument.setInstrumentName("New Instrument");
		instrument.setCreatedOn(LocalDateTime.now());
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
	public void newOrderTest() throws Exception {
		Instrument instrument = instrumentService.getInstrumentById(1001l);
		openBook();
		String uri = "/orderbooks/1001/orders";
		OrderBuilder orderBuilder = new OrderBuilder();
		orderBuilder.setOrderName("Unit Test Order");
		orderBuilder.setInstrument(instrument);
		orderBuilder.setOrderQuantity(200l);
		orderBuilder.setOrderprice(400d);
		orderBuilder.setOrderStatus(OrderStatus.VALID);
		orderBuilder.setOrderType(OrderType.LIMIT_ORDER);
		orderBuilder.setOrderBook(null);
		orderBuilder.setExecutionQuantity(2l);
		orderBuilder.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBuilder.setCreatedOn(LocalDateTime.now());
		Order order = new Order(orderBuilder);
		String inputJson = mapToJson(order );
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBook orderBook = mapFromJson(content, OrderBook.class);
		assertEquals(200, status);
		assertEquals(2, orderBook.getOrders().size());
	}

	@Test
	public void newOrderClosedBookTest() throws Exception {
		Instrument instrument = instrumentService.getInstrumentById(1001l);
		closeBook();
		String uri = "/orderbooks/1001/orders";
		OrderBuilder orderBuilder = new OrderBuilder();
		orderBuilder.setOrderName("Unit Test Order");
		orderBuilder.setInstrument(instrument);
		orderBuilder.setOrderQuantity(200l);
		orderBuilder.setOrderprice(400d);
		orderBuilder.setOrderStatus(OrderStatus.VALID);
		orderBuilder.setOrderType(OrderType.LIMIT_ORDER);
		orderBuilder.setOrderBook(null);
		orderBuilder.setExecutionQuantity(2l);
		orderBuilder.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBuilder.setCreatedOn(LocalDateTime.now());
		Order order = new Order(orderBuilder);
		String inputJson = mapToJson(order);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ADD_ORDER_ORDERBOOK_CLOSED.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderNoNameTest() throws Exception {
		Instrument instrument = instrumentService.getInstrumentById(1001l);
		openBook();
		String uri = "/orderbooks/1001/orders";OrderBuilder orderBuilder = new OrderBuilder();
		orderBuilder.setInstrument(instrument);
		orderBuilder.setOrderQuantity(200l);
		orderBuilder.setOrderprice(400d);
		orderBuilder.setOrderStatus(OrderStatus.VALID);
		orderBuilder.setOrderType(OrderType.LIMIT_ORDER);
		orderBuilder.setOrderBook(null);
		orderBuilder.setExecutionQuantity(2l);
		orderBuilder.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBuilder.setCreatedOn(LocalDateTime.now());
		Order order = new Order(orderBuilder);
		String inputJson = mapToJson(order);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_NAME_INVALID.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderNoQtyTest() throws Exception {
		Instrument instrument = instrumentService.getInstrumentById(1001l);
		openBook();
		String uri = "/orderbooks/1001/orders";
		OrderBuilder orderBuilder = new OrderBuilder();
		orderBuilder.setOrderName("Unit Test Order");
		orderBuilder.setInstrument(instrument);
		orderBuilder.setOrderprice(400d);
		orderBuilder.setOrderStatus(OrderStatus.VALID);
		orderBuilder.setOrderType(OrderType.LIMIT_ORDER);
		orderBuilder.setOrderBook(null);
		orderBuilder.setExecutionQuantity(2l);
		orderBuilder.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBuilder.setCreatedOn(LocalDateTime.now());
		Order order = new Order(orderBuilder);
		String inputJson = mapToJson(order);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_QUANTITY_INVALID.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderNoPriceTest() throws Exception {
		Instrument instrument = instrumentService.getInstrumentById(1001l);
		openBook();
		String uri = "/orderbooks/1001/orders";
		OrderBuilder orderBuilder = new OrderBuilder();
		orderBuilder.setOrderName("Unit Test Order");
		orderBuilder.setInstrument(instrument);
		orderBuilder.setOrderQuantity(200l);
		orderBuilder.setOrderStatus(OrderStatus.VALID);
		orderBuilder.setOrderType(OrderType.MARKET_ORDER);
		orderBuilder.setOrderBook(null);
		orderBuilder.setExecutionQuantity(2l);
		orderBuilder.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBuilder.setCreatedOn(LocalDateTime.now());
		Order order = new Order(orderBuilder);
		String inputJson = mapToJson(order);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_PRICE_INVALID.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderNoTypeTest() throws Exception {
		Instrument instrument = instrumentService.getInstrumentById(1001l);
		openBook();
		String uri = "/orderbooks/1001/orders";
		OrderBuilder orderBuilder = new OrderBuilder();
		orderBuilder.setOrderName("Unit Test Order");
		orderBuilder.setInstrument(instrument);
		orderBuilder.setOrderQuantity(200l);
		orderBuilder.setOrderprice(400d);
		orderBuilder.setOrderBook(null);
		orderBuilder.setExecutionQuantity(2l);
		orderBuilder.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBuilder.setCreatedOn(LocalDateTime.now());
		Order order = new Order(orderBuilder);
		String inputJson = mapToJson(order);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_TYPE_INVALID.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderDiffInstrumentTest() throws Exception {
		Instrument instrument = instrumentService.getInstrument("Instrument-3");
		String uri = "/orderbooks/1001/orders";
		OrderBuilder orderBuilder = new OrderBuilder();
		orderBuilder.setOrderName("Unit Test Order");
		orderBuilder.setInstrument(instrument);
		orderBuilder.setOrderQuantity(200l);
		orderBuilder.setOrderprice(400d);
		orderBuilder.setOrderStatus(OrderStatus.VALID);
		orderBuilder.setOrderType(OrderType.LIMIT_ORDER);
		orderBuilder.setOrderBook(null);
		orderBuilder.setExecutionQuantity(2l);
		orderBuilder.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBuilder.setCreatedOn(LocalDateTime.now());
		Order order = new Order(orderBuilder);
		String inputJson = mapToJson(order);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
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
		execution.setQuantity(100l);
		OrderBook orderBook = orderBookService.getOrderBook(1001l);
		orderBook.setExecutionStatus(ExecutionStatus.NOT_EXECUTED);
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
		OrderBook orderBook = orderBookService.getOrderBook(1001l);
		orderBook.setExecutionStatus(ExecutionStatus.NOT_EXECUTED);
		orderBook.setExecutions(null);
		orderBook.setOrderBookStatus(OrderBookStatus.OPEN);
		orderBookService.saveBook(orderBook);
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
		assertEquals(3, orderBook.getExecutions().size());
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
		execution.setQuantity(100l);
		OrderBook orderBook = orderBookService.getOrderBook(1001l);
		orderBook.setExecutionStatus(ExecutionStatus.NOT_EXECUTED);
		orderBook.setExecutions(null);
		orderBook.setOrderBookStatus(OrderBookStatus.OPEN);
		orderBookService.saveBook(orderBook);
		closeBook();
		String uri = "/orderbooks/1001/executions";
		String inputJson = mapToJson(execution);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.PARTIALLY_EXECUTED.getMessage(), message.getMessage());
	}

}
