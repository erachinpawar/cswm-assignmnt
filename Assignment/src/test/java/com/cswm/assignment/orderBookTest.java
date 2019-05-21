package com.cswm.assignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;

import javax.ws.rs.core.MediaType;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cswm.assignment.applicationutils.ErrorMessageEnum;
import com.cswm.assignment.applicationutils.OrderBookStatus;
import com.cswm.assignment.applicationutils.OrderStatus;
import com.cswm.assignment.applicationutils.OrderType;
import com.cswm.assignment.model.Message;
import com.cswm.assignment.model.dto.inputDto.AddOrderInputDto;
import com.cswm.assignment.model.dto.inputDto.ExecutionInputDto;
import com.cswm.assignment.model.dto.inputDto.OrderBookInputDto;
import com.cswm.assignment.model.dto.ouputDto.ClosedOrderBookOutputDto;
import com.cswm.assignment.model.dto.ouputDto.OrderBookOutputDto;
import com.cswm.assignment.model.dto.ouputDto.OrderBookStatisticsOutputDto;
import com.cswm.assignment.model.dto.ouputDto.OrderOutputDto;
import com.cswm.assignment.model.dto.ouputDto.OrderStatisticsOutputDto;

@FixMethodOrder(MethodSorters.JVM)
public class orderBookTest extends AbstractTest {

	/// Create order Book Test Cases /////
	@Test
	public void newOrderBookTest() throws Exception {
		String uri = "/orderbooks/create";
		OrderBookInputDto bookInputDto = new OrderBookInputDto();
		bookInputDto.setInstrumentId(10l);

		String inputJson = super.mapToJson(bookInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBookOutputDto createdOrderBook = mapFromJson(content, OrderBookOutputDto.class);
		assertEquals(200, status);
		assertNotEquals(null, createdOrderBook.getOrderBookId());
		assertNotEquals(null, createdOrderBook.getInstrument().getInstrumentId());
	}

	@Test
	public void newOrderBookNoInstrumentTest() throws Exception {
		String uri = "/orderbooks/create";
		OrderBookInputDto bookInputDto = new OrderBookInputDto();
		String inputJson = super.mapToJson(bookInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.BOOK_WITHOUT_INSTRUMENT.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderBooWithOtherOpenBook() throws Exception {
		String uri = "/orderbooks/create";
		OrderBookInputDto bookInputDto = new OrderBookInputDto();
		bookInputDto.setInstrumentId(1l);
		String inputJson = super.mapToJson(bookInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.NOT_EXECUTED_ORDER_BOOK_PRESENT.getMessage(), message.getMessage());
	}

	//// Add order order to order book
	@Test
	public void addLimitOrderToOrderBook() throws Exception {
		/// add order to order book
		String uri = "/orderbooks/1001/orders";
		AddOrderInputDto addOrderInputDto = new AddOrderInputDto();
		addOrderInputDto.setInstrumentId(2l);
		addOrderInputDto.setOrderprice(BigDecimal.valueOf(2000l));
		addOrderInputDto.setOrderQuantity(100l);
		String inputJson = super.mapToJson(addOrderInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderOutputDto addedOrderDto = mapFromJson(content, OrderOutputDto.class);
		assertEquals(200, status);
		assertNotEquals(null, addedOrderDto.getInstrument());
		assertEquals(2l, addedOrderDto.getInstrument().getInstrumentId().longValue());
		assertEquals(OrderType.LIMIT_ORDER, addedOrderDto.getOrderDetails().getOrderType());
		assertEquals(OrderStatus.VALID, addedOrderDto.getOrderDetails().getOrderStatus());
	}

	@Test
	public void addMarketOrderToOrderBook() throws Exception {
		/// add order to order book
		String uri = "/orderbooks/1001/orders";
		AddOrderInputDto addOrderInputDto = new AddOrderInputDto();
		addOrderInputDto.setInstrumentId(2l);
		addOrderInputDto.setOrderQuantity(100l);
		String inputJson = super.mapToJson(addOrderInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderOutputDto addedOrderDto = mapFromJson(content, OrderOutputDto.class);
		assertEquals(200, status);
		assertNotEquals(null, addedOrderDto.getInstrument());
		assertEquals(2l, addedOrderDto.getInstrument().getInstrumentId().longValue());
		assertEquals(OrderType.MARKET_ORDER, addedOrderDto.getOrderDetails().getOrderType());
		assertEquals(OrderStatus.VALID, addedOrderDto.getOrderDetails().getOrderStatus());
	}

	@Test
	public void addOrderToOrderBookWithDifferntInstrument() throws Exception {
		/// add order to order book
		String uri = "/orderbooks/1001/orders";
		AddOrderInputDto addOrderInputDto = new AddOrderInputDto();
		addOrderInputDto.setInstrumentId(3l);
		addOrderInputDto.setOrderprice(BigDecimal.valueOf(2000l));
		addOrderInputDto.setOrderQuantity(100l);
		String inputJson = super.mapToJson(addOrderInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_NOT_BELONG_TO_INSTRUMENT.getMessage(), message.getMessage());
	}

	@Test
	public void addOrderToOrderBookWithBookNotOpen() throws Exception {
		/// add order to order book
		String closeUri = "/orderbooks/1001/close";
		String uri = "/orderbooks/1001/orders";
		AddOrderInputDto addOrderInputDto = new AddOrderInputDto();
		addOrderInputDto.setInstrumentId(2l);
		addOrderInputDto.setOrderprice(BigDecimal.valueOf(2000l));
		addOrderInputDto.setOrderQuantity(100l);
		String inputJson = super.mapToJson(addOrderInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(closeUri).contentType(MediaType.APPLICATION_JSON)).andReturn();
		mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_BOOK_NOT_OPEN.getMessage(), message.getMessage());
	}

	@Test
	public void addOrderToOrderBookWithNoInstrument() throws Exception {
		/// add order to order book

		String uri = "/orderbooks/1001/orders";
		AddOrderInputDto addOrderInputDto = new AddOrderInputDto();
		addOrderInputDto.setOrderprice(BigDecimal.valueOf(2000l));
		addOrderInputDto.setOrderQuantity(100l);
		String inputJson = super.mapToJson(addOrderInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.INSRTUMENT_NOT_PRESENT.getMessage(), message.getMessage());
	}

	@Test
	public void addOrderToOrderBookWithNoQty() throws Exception {
		/// add order to order book
		String uri = "/orderbooks/1001/orders";
		AddOrderInputDto addOrderInputDto = new AddOrderInputDto();
		addOrderInputDto.setInstrumentId(2l);
		addOrderInputDto.setOrderprice(BigDecimal.valueOf(2000l));
		String inputJson = super.mapToJson(addOrderInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_QUANTITY_INVALID.getMessage(), message.getMessage());
	}

	// close order book
	@Test
	public void closeOrderBook() throws Exception {
		/// add order to order book
		String uri = "/orderbooks/1002/close";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		ClosedOrderBookOutputDto orderBookDto = mapFromJson(content, ClosedOrderBookOutputDto.class);
		assertEquals(200, status);
		assertEquals(OrderBookStatus.CLOSED, orderBookDto.getOrderBookStatus());
	}

	@Test
	public void closeOrderBookStatusNotInOpen() throws Exception {
		/// add order to order book
		String uri = "/orderbooks/1002/close";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.BOOK_STATUS_NOT_OPEN.getMessage(), message.getMessage());
	}

	// Add Executions to order Book
	@Test
	public void addExecutionToOrderBook() throws Exception {
		String uri = "/orderbooks/1003/execution";
		ExecutionInputDto executionInputDto = new ExecutionInputDto();
		executionInputDto.setPrice(BigDecimal.valueOf(40l));
		executionInputDto.setQuantity(50l);
		String inputJson = super.mapToJson(executionInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBookOutputDto orderBookDto = mapFromJson(content, OrderBookOutputDto.class);
		orderBookDto.getOrders().forEach(order -> {
			if (order.getOrderId().longValue() == 2006l) {
				assertEquals(OrderStatus.VALID, order.getOrderDetails().getOrderStatus());
				assertEquals(21l, order.getOrderDetails().getExecutionQuantity().longValue());
			}
			if (order.getOrderId().longValue() == 2005l) {
				assertEquals(OrderStatus.VALID, order.getOrderDetails().getOrderStatus());
				assertEquals(11l, order.getOrderDetails().getExecutionQuantity().longValue());
			}
			if (order.getOrderId().longValue() == 2007l) {
				assertEquals(OrderStatus.VALID, order.getOrderDetails().getOrderStatus());
				assertEquals(7l, order.getOrderDetails().getExecutionQuantity().longValue());
			}
			if (order.getOrderId().longValue() == 2004l) {
				assertEquals(OrderStatus.INVALID, order.getOrderDetails().getOrderStatus());
				assertEquals(0l, order.getOrderDetails().getExecutionQuantity().longValue());
			}
		});
		assertEquals(200, status);
	}

	@Test
	public void addExecutionToOrderBookInvalidQty() throws Exception {
		String uri = "/orderbooks/1003/execution";
		ExecutionInputDto executionInputDto = new ExecutionInputDto();
		executionInputDto.setPrice(BigDecimal.valueOf(40l));
		executionInputDto.setQuantity(0l);
		String inputJson = super.mapToJson(executionInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.EXECUTION_QTY_INVALID.getMessage(), message.getMessage());
	}

	@Test
	public void addExecutionToOrderBookWithDiffExecutionPrice() throws Exception {
		String uri = "/orderbooks/1003/execution";
		ExecutionInputDto executionInputDto = new ExecutionInputDto();
		executionInputDto.setPrice(BigDecimal.valueOf(50l));
		executionInputDto.setQuantity(50l);
		String inputJson = super.mapToJson(executionInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.EXECUTION_PRICE_INVALID.getMessage(), message.getMessage());
	}

	@Test
	public void addExecutionToOrderBookHasOpenStatus() throws Exception {
		String uri = "/orderbooks/1007/execution";
		ExecutionInputDto executionInputDto = new ExecutionInputDto();
		executionInputDto.setPrice(BigDecimal.valueOf(40l));
		executionInputDto.setQuantity(50l);
		String inputJson = super.mapToJson(executionInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_BOOK_NOT_CLOSED.getMessage(), message.getMessage());
	}

	@Test
	public void addExecutionToOrderBookZeroPrice() throws Exception {
		String uri = "/orderbooks/1003/execution";
		ExecutionInputDto executionInputDto = new ExecutionInputDto();
		executionInputDto.setPrice(BigDecimal.valueOf(0l));
		executionInputDto.setQuantity(50l);
		String inputJson = super.mapToJson(executionInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.EXECUTION_PRICE_ZERO.getMessage(), message.getMessage());
	}

	@Test
	public void addExecutionToOrderPartialExecution() throws Exception {
		String uri = "/orderbooks/1003/execution";
		ExecutionInputDto executionInputDto = new ExecutionInputDto();
		executionInputDto.setPrice(BigDecimal.valueOf(40l));
		executionInputDto.setQuantity(200l);
		String inputJson = super.mapToJson(executionInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBookOutputDto orderBookDto = mapFromJson(content, OrderBookOutputDto.class);
		orderBookDto.getOrders().forEach(order -> {
			if (order.getOrderId().longValue() == 2004l) {
				assertEquals(OrderStatus.INVALID, order.getOrderDetails().getOrderStatus());
				assertEquals(0l, order.getOrderDetails().getExecutionQuantity().longValue());
			}
			if (order.getOrderId().longValue() == 2005l) {
				assertEquals(OrderStatus.VALID, order.getOrderDetails().getOrderStatus());
				assertEquals(50l, order.getOrderDetails().getExecutionQuantity().longValue());
			}
			if (order.getOrderId().longValue() == 2006l) {
				assertEquals(OrderStatus.VALID, order.getOrderDetails().getOrderStatus());
				assertEquals(100l, order.getOrderDetails().getExecutionQuantity().longValue());
			}
			if (order.getOrderId().longValue() == 2007l) {
				assertEquals(OrderStatus.VALID, order.getOrderDetails().getOrderStatus());
				assertEquals(30l, order.getOrderDetails().getExecutionQuantity().longValue());
			}

		});
		assertEquals(200, status);
	}

	@Test
	public void addExecutionToOrderBookWithExeccutedStatus() throws Exception {
		String uri = "/orderbooks/1008/execution";
		ExecutionInputDto executionInputDto = new ExecutionInputDto();
		executionInputDto.setPrice(BigDecimal.valueOf(40l));
		executionInputDto.setQuantity(50l);
		String inputJson = super.mapToJson(executionInputDto);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_BOOK_NOT_CLOSED.getMessage(), message.getMessage());
	}

	//// get Order Book Stats
	@Test
	public void getOrderBookStatistics() throws Exception {
		String uri = "/orderbooks/1003/stastitics";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBookStatisticsOutputDto orderBookStatisticsDto = mapFromJson(content, OrderBookStatisticsOutputDto.class);
		assertEquals(200, status);
		assertEquals(6l, orderBookStatisticsDto.getTotalOrderCount().longValue());
		assertEquals(4l, orderBookStatisticsDto.getValidOrderCount().longValue());
		assertEquals(2l, orderBookStatisticsDto.getInValidOrderCount().longValue());
		assertEquals(260l, orderBookStatisticsDto.getTotalDemand().longValue());
		assertEquals(30l, orderBookStatisticsDto.getInValidDemand().longValue());
		assertEquals(230l, orderBookStatisticsDto.getValidDemand().longValue());
		assertEquals(2007l, orderBookStatisticsDto.getLastOrder().getOrderId().longValue());
		assertEquals(2006l, orderBookStatisticsDto.getBiggestOrder().getOrderId().longValue());
		assertEquals(2008l, orderBookStatisticsDto.getSmallestOrder().getOrderId().longValue());
		assertEquals(2004l, orderBookStatisticsDto.getEarliestOrder().getOrderId().longValue());
		assertEquals(230l, orderBookStatisticsDto.getAccumulatedExecutionQuantity().longValue());
		assertEquals(0, BigDecimal.valueOf(40l).compareTo(orderBookStatisticsDto.getExecutionPrice()));
		assertEquals(2, orderBookStatisticsDto.getLimitBreakDownForAllOrders().size());
		assertEquals(1, orderBookStatisticsDto.getLimitBreakDownForInvalidOrders().size());
		assertEquals(1, orderBookStatisticsDto.getLimitBreakDownForValidOrders().size());

	}

	@Test
	public void getOrderBookStatsNtFoundTest() throws Exception {
		String uri = "/orderbooks/2004/stastitics";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(404, status);
		assertEquals(ErrorMessageEnum.ORDER_BOOK_NOT_FOUND.getMessage(), message.getMessage());
	}

	@Test
	public void getOrderStats() throws Exception {
		String uri = "/orderbooks/1003/orderStatistics/2005";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderStatisticsOutputDto orderStatisticsDto = mapFromJson(content, OrderStatisticsOutputDto.class);
		assertEquals(200, status);
		assertEquals(40l, orderStatisticsDto.getExecutionPrice().longValue());

	}
	

	
}
