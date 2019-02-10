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
import com.cswm.assignment.applicationutils.OrderTypesInStatistics;
import com.cswm.assignment.model.Message;
import com.cswm.assignment.model.dto.ExecutionDto;
import com.cswm.assignment.model.dto.InstrumentDto;
import com.cswm.assignment.model.dto.OrderBookDto;
import com.cswm.assignment.model.dto.OrderBookStatisticsDto;
import com.cswm.assignment.model.dto.OrderBookValidInValidStatistics;
import com.cswm.assignment.model.dto.OrderDto;
import com.cswm.assignment.model.dto.OrderStatisticsDto;

@FixMethodOrder(MethodSorters.JVM)
public class orderBookTest extends AbstractTest {

	/// Create order Book Test Cases /////
	@Test
	public void newOrderBookTest() throws Exception {
		String uri = "/orderbooks/create";
		OrderBookDto orderBookDto = new OrderBookDto();
		InstrumentDto instrumentDto = new InstrumentDto();
		instrumentDto.setInstrumentId(1l);
		orderBookDto.setInstrument(instrumentDto);
		String inputJson = super.mapToJson(orderBookDto);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBookDto createdOrderBook = mapFromJson(content, OrderBookDto.class);
		assertEquals(200, status);
		assertNotEquals(null, createdOrderBook.getOrderBookId());
		assertNotEquals(null, createdOrderBook.getInstrument().getInstrumentId());
	}

	@Test
	public void newOrderBookNoInstrumentTest() throws Exception {
		String uri = "/orderbooks/create";
		OrderBookDto orderBookDto = new OrderBookDto();
		orderBookDto.setOrderBookId(2001l);
		String inputJson = super.mapToJson(orderBookDto);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.BOOK_WITHOUT_INSTRUMENT.getMessage(), message.getMessage());
	}

	@Test
	public void newOrderBooWithOtherOpenBook() throws Exception {
		String uri = "/orderbooks/create";
		OrderBookDto orderBookDto = new OrderBookDto();
		InstrumentDto instrumentDto = new InstrumentDto();
		instrumentDto.setInstrumentId(1l);
		orderBookDto.setInstrument(instrumentDto);
		String inputJson = super.mapToJson(orderBookDto);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
		mvcResult = mvc
				.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
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
		OrderDto orderDto = new OrderDto();
		InstrumentDto instrumentDto = new InstrumentDto();
		instrumentDto.setInstrumentId(2l);
		orderDto.setInstrument(instrumentDto);
		orderDto.setOrderprice(BigDecimal.valueOf(2000l));
		orderDto.setOrderQuantity(100l);
		String inputJson = super.mapToJson(orderDto);
		inputJson = super.mapToJson(orderDto);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderDto addedOrderDto = mapFromJson(content, OrderDto.class);
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
		OrderDto orderDto = new OrderDto();
		InstrumentDto instrumentDto = new InstrumentDto();
		instrumentDto.setInstrumentId(2l);
		orderDto.setInstrument(instrumentDto);
		orderDto.setOrderQuantity(100l);
		String inputJson = super.mapToJson(orderDto);
		inputJson = super.mapToJson(orderDto);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderDto addedOrderDto = mapFromJson(content, OrderDto.class);
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
		OrderDto orderDto = new OrderDto();
		InstrumentDto instrumentDto = new InstrumentDto();
		instrumentDto.setInstrumentId(3l);
		orderDto.setInstrument(instrumentDto);
		orderDto.setOrderprice(BigDecimal.valueOf(2000l));
		orderDto.setOrderQuantity(100l);
		String inputJson = super.mapToJson(orderDto);
		inputJson = super.mapToJson(orderDto);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_NOT_BELONG_TO_INSTRUMENT.getMessage(), message.getMessage());
	}

	@Test
	public void addOrderToOrderBookWithBookNotOpen() throws Exception {
		/// add order to order book
		String uri = "/orderbooks/1001/orders";
		String closeUri = "/orderbooks/1001/close";
		OrderDto orderDto = new OrderDto();
		InstrumentDto instrumentDto = new InstrumentDto();
		instrumentDto.setInstrumentId(3l);
		orderDto.setInstrument(instrumentDto);
		orderDto.setOrderprice(BigDecimal.valueOf(2000l));
		orderDto.setOrderQuantity(100l);
		String inputJson = super.mapToJson(orderDto);
		inputJson = super.mapToJson(orderDto);
		MvcResult mvcResult = mvc
				.perform(
						MockMvcRequestBuilders.put(closeUri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
		mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
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
		OrderDto orderDto = new OrderDto();
		orderDto.setOrderprice(BigDecimal.valueOf(2000l));
		orderDto.setOrderQuantity(100l);
		String inputJson = super.mapToJson(orderDto);
		inputJson = super.mapToJson(orderDto);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
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
		OrderDto orderDto = new OrderDto();
		InstrumentDto instrumentDto = new InstrumentDto();
		instrumentDto.setInstrumentId(3l);
		orderDto.setInstrument(instrumentDto);
		orderDto.setOrderprice(BigDecimal.valueOf(2000l));
		String inputJson = super.mapToJson(orderDto);
		inputJson = super.mapToJson(orderDto);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
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
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBookDto orderBookDto = mapFromJson(content, OrderBookDto.class);
		assertEquals(200, status);
		assertEquals(OrderBookStatus.CLOSED, orderBookDto.getOrderBookStatus());
	}

	@Test
	public void closeOrderBookStatusNotInOpen() throws Exception {
		/// add order to order book
		String uri = "/orderbooks/1002/close";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON))
				.andReturn();
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
		ExecutionDto executionDto = new ExecutionDto();
		executionDto.setPrice(BigDecimal.valueOf(40l));
		executionDto.setQuantity(50l);
		String inputJson = super.mapToJson(executionDto);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBookDto orderBookDto = mapFromJson(content, OrderBookDto.class);
		orderBookDto.getOrders().forEach(order -> {
			if (order.getOrderId().longValue() == 6l) {
				assertEquals(OrderStatus.VALID, order.getOrderDetails().getOrderStatus());
				assertEquals(27l, order.getOrderDetails().getExecutionQuantity().longValue());
			}
			if (order.getOrderId().longValue() == 5l) {
				assertEquals(OrderStatus.VALID, order.getOrderDetails().getOrderStatus());
				assertEquals(14l, order.getOrderDetails().getExecutionQuantity().longValue());
			}
			if (order.getOrderId().longValue() == 7l) {
				assertEquals(OrderStatus.VALID, order.getOrderDetails().getOrderStatus());
				assertEquals(9l, order.getOrderDetails().getExecutionQuantity().longValue());
			}
			if (order.getOrderId().longValue() == 4l) {
				assertEquals(OrderStatus.INVALID, order.getOrderDetails().getOrderStatus());
				assertEquals(0l, order.getOrderDetails().getExecutionQuantity().longValue());
			}
		});
		assertEquals(200, status);
	}

	@Test
	public void addExecutionToOrderBookInvalidQty() throws Exception {
		String uri = "/orderbooks/1003/execution";
		ExecutionDto executionDto = new ExecutionDto();
		executionDto.setPrice(BigDecimal.valueOf(40l));
		executionDto.setQuantity(0l);
		String inputJson = super.mapToJson(executionDto);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.EXECUTION_QTY_INVALID.getMessage(), message.getMessage());
	}

	@Test
	public void addExecutionToOrderBookWithDiffExecutionPrice() throws Exception {
		String uri = "/orderbooks/1003/execution";
		ExecutionDto executionDto = new ExecutionDto();
		executionDto.setPrice(BigDecimal.valueOf(50l));
		executionDto.setQuantity(50l);
		String inputJson = super.mapToJson(executionDto);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.EXECUTION_PRICE_INVALID.getMessage(), message.getMessage());
	}

	@Test
	public void addExecutionToOrderBookHasOpenStatus() throws Exception {
		String uri = "/orderbooks/1007/execution";
		ExecutionDto executionDto = new ExecutionDto();
		executionDto.setPrice(BigDecimal.valueOf(40l));
		executionDto.setQuantity(50l);
		String inputJson = super.mapToJson(executionDto);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_BOOK_STATUS_OPEN.getMessage(), message.getMessage());
	}

	@Test
	public void addExecutionToOrderBookZeroPrice() throws Exception {
		String uri = "/orderbooks/1003/execution";
		ExecutionDto executionDto = new ExecutionDto();
		executionDto.setPrice(BigDecimal.valueOf(0l));
		executionDto.setQuantity(50l);
		String inputJson = super.mapToJson(executionDto);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.EXECUTION_PRICE_ZERO.getMessage(), message.getMessage());
	}

	@Test
	public void addExecutionToOrderPartialExecution() throws Exception {
		String uri = "/orderbooks/1003/execution";
		ExecutionDto executionDto = new ExecutionDto();
		executionDto.setPrice(BigDecimal.valueOf(40l));
		executionDto.setQuantity(200l);
		String inputJson = super.mapToJson(executionDto);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBookDto orderBookDto = mapFromJson(content, OrderBookDto.class);
		orderBookDto.getOrders().forEach(order -> {
			if (order.getOrderId().longValue() == 4l) {
				assertEquals(OrderStatus.INVALID, order.getOrderDetails().getOrderStatus());
				assertEquals(0l, order.getOrderDetails().getExecutionQuantity().longValue());
			}
			if (order.getOrderId().longValue() == 5l) {
				assertEquals(OrderStatus.VALID, order.getOrderDetails().getOrderStatus());
				assertEquals(50l, order.getOrderDetails().getExecutionQuantity().longValue());
			}
			if (order.getOrderId().longValue() == 6l) {
				assertEquals(OrderStatus.VALID, order.getOrderDetails().getOrderStatus());
				assertEquals(100l, order.getOrderDetails().getExecutionQuantity().longValue());
			}
			if (order.getOrderId().longValue() == 7l) {
				assertEquals(OrderStatus.VALID, order.getOrderDetails().getOrderStatus());
				assertEquals(30l, order.getOrderDetails().getExecutionQuantity().longValue());
			}

		});
		assertEquals(200, status);
	}

	@Test
	public void addExecutionToOrderBookWithExeccutedStatus() throws Exception {
		String uri = "/orderbooks/1008/execution";
		ExecutionDto executionDto = new ExecutionDto();
		executionDto.setPrice(BigDecimal.valueOf(40l));
		executionDto.setQuantity(50l);
		String inputJson = super.mapToJson(executionDto);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.ORDER_BOOK_STATUS_EXECUTED.getMessage(), message.getMessage());
	}

	//// get Order Book Stats
	@Test
	public void getOrderBookStatistics() throws Exception {
		String uri = "/orderbooks/1003/stastitics";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBookStatisticsDto orderBookStatisticsDto = mapFromJson(content, OrderBookStatisticsDto.class);
		assertEquals(200, status);
		assertEquals(4l, orderBookStatisticsDto.getTotalNoOfOrders().longValue());
		assertEquals(200l, orderBookStatisticsDto.getTotalNoofAccuOrders().longValue());
		assertEquals(7l, orderBookStatisticsDto.getOrderTypesInStats().get(OrderTypesInStatistics.LATEST_ORDER)
				.getOrderId().longValue());
		assertEquals(6l, orderBookStatisticsDto.getOrderTypesInStats().get(OrderTypesInStatistics.BIGGEST_ORDER)
				.getOrderId().longValue());
		assertEquals(4l, orderBookStatisticsDto.getOrderTypesInStats().get(OrderTypesInStatistics.SMALLEST_ORDER)
				.getOrderId().longValue());

	}

	@Test
	public void getOrderBookValidInValidStatistics() throws Exception {
		String uri = "/orderbooks/1003/detailedstastitics";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderBookValidInValidStatistics bookValidInValidStatistics = mapFromJson(content,
				OrderBookValidInValidStatistics.class);
		assertEquals(200, status);
		assertEquals(4l, bookValidInValidStatistics.getOrderBookStatisticsDto().getTotalNoOfOrders().longValue());
		assertEquals(200l, bookValidInValidStatistics.getOrderBookStatisticsDto().getTotalNoofAccuOrders().longValue());
		assertEquals(3l, bookValidInValidStatistics.getValidOrderCount().longValue());
		assertEquals(1l, bookValidInValidStatistics.getInValidOrderCount().longValue());
		assertEquals(20l, bookValidInValidStatistics.getInValidDemand().longValue());
		assertEquals(180l, bookValidInValidStatistics.getValidDemand().longValue());
		assertEquals(180l, bookValidInValidStatistics.getExecutionQty().longValue());
		assertEquals(0, BigDecimal.valueOf(1600l).compareTo(bookValidInValidStatistics.getTotalExecutionPrice()));
		assertEquals(7l, bookValidInValidStatistics.getOrderBookStatisticsDto().getOrderTypesInStats()
				.get(OrderTypesInStatistics.LATEST_ORDER).getOrderId().longValue());
		assertEquals(6l, bookValidInValidStatistics.getOrderBookStatisticsDto().getOrderTypesInStats()
				.get(OrderTypesInStatistics.BIGGEST_ORDER).getOrderId().longValue());
		assertEquals(4l, bookValidInValidStatistics.getOrderBookStatisticsDto().getOrderTypesInStats()
				.get(OrderTypesInStatistics.SMALLEST_ORDER).getOrderId().longValue());

	}

	@Test
	public void getOrderBookStatsNtFoundTest() throws Exception {
		String uri = "/orderbooks/2004/stastitics";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(404, status);
		assertEquals(ErrorMessageEnum.ORDER_BOOK_NOT_FOUND.getMessage(), message.getMessage());
	}

	@Test
	public void getOrderStats() throws Exception {
		String uri = "/orderbooks/1003/orderStatistics/5";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		OrderStatisticsDto orderStatisticsDto = mapFromJson(content, OrderStatisticsDto.class);
		assertEquals(200, status);
		assertEquals(2000l, orderStatisticsDto.getExecutionPrice().longValue());

	}
}
