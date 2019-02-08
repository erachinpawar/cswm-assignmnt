package com.cswm.assignment.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cswm.assignment.ApplicationConstants;
import com.cswm.assignment.applicationutils.ErrorMessageEnum;
import com.cswm.assignment.applicationutils.OrderBookStatus;
import com.cswm.assignment.applicationutils.OrderStatus;
import com.cswm.assignment.applicationutils.OrderType;
import com.cswm.assignment.exceptions.ApplicationException;
import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.model.OrderBuilder;
import com.cswm.assignment.model.dto.ExecutionDto;
import com.cswm.assignment.model.dto.OrderBookDto;
import com.cswm.assignment.model.dto.OrderBookStatisticsDto;
import com.cswm.assignment.model.dto.OrderBookStatisticsDto.OrderTypesInStats;
import com.cswm.assignment.model.dto.OrderBuilderDto;
import com.cswm.assignment.model.dto.OrderDto;
import com.cswm.assignment.repository.ExecutionRepository;
import com.cswm.assignment.repository.InstrumentRepository;
import com.cswm.assignment.repository.OrderBookRepository;
import com.cswm.assignment.repository.OrderRepository;
import com.cswm.assignment.service.OrderBookService;
import com.cswm.assignment.service.OrderService;

@Service
@Transactional
public class OrderBookServiceImpl implements OrderBookService {

	@Autowired
	OrderBookRepository orderBookRepository;

	@Autowired
	OrderService orderService;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	InstrumentRepository instrumentRepository;

	@Autowired
	ExecutionRepository executionRepository;

	@Override
	public List<OrderBookDto> getAllOrderBooks() {
		List<OrderBookDto> orderBookDtos = new ArrayList<>();
		orderBookRepository.findAll()
				.forEach(orderBook -> orderBookDtos.add(new ModelMapper().map(orderBook, OrderBookDto.class)));

		return orderBookDtos;
	}

	@Override
	public OrderBookDto getOrderBook(Long orderBookId) {
		OrderBook orderBook = orderBookRepository.findById(orderBookId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.ORDER_BOOK_NOT_FOUND));
		return new ModelMapper().map(orderBook, OrderBookDto.class);
	}

	@Override
	public synchronized OrderBookDto createOrderBook(OrderBookDto orderBookDto) {
		if (null == orderBookDto.getInstrument())
			throw new ApplicationException(ErrorMessageEnum.BOOK_WITHOUT_INSTRUMENT);
		Instrument instrument = instrumentRepository
				.findFirstByInstrumentName(orderBookDto.getInstrument().getInstrumentName());
		if (orderBookRepository.findFirstByInstrumentAndOrderBookStatusNot(instrument,
				OrderBookStatus.EXECUTED) != null)
			throw new ApplicationException(ErrorMessageEnum.NOT_EXECUTED_ORDER_BOOK_PRESENT);
		OrderBook orderBook = new ModelMapper().map(orderBookDto, OrderBook.class);
		orderBook.getInstrument().setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBook.getInstrument().setCreatedOn(LocalDateTime.now());
		orderBook.setCreatedOn(LocalDateTime.now());
		orderBook.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBook.setOrderBookStatus(OrderBookStatus.OPEN);
		orderBook = orderBookRepository.save(orderBook);
		new ModelMapper().map(orderBook, OrderBookDto.class);
		return new ModelMapper().map(orderBook, OrderBookDto.class);
	}

	@Override
	public synchronized OrderBookDto addOrderToOrderBook(Long orderBookId, OrderDto orderDto) {
		if (null == orderDto.getOrderQuantity() || orderDto.getOrderQuantity() <= 0l)
			throw new ApplicationException(ErrorMessageEnum.ORDER_QUANTITY_INVALID);
		if (null == orderDto.getInstrument() || StringUtils.isEmpty(orderDto.getInstrument().getInstrumentName()))
			throw new ApplicationException(ErrorMessageEnum.INSRTUMENT_NOT_PRESENT);
		OrderBook orderBook = new ModelMapper().map(getOrderBook(orderBookId), OrderBook.class);
		if (!OrderBookStatus.OPEN.equals(orderBook.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_NOT_OPEN);
		if (!orderBook.getInstrument().getInstrumentId().equals(orderDto.getInstrument().getInstrumentId()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_NOT_BELONG_TO_INSTRUMENT);
		OrderBuilder orderBuilder = new ModelMapper().map(new OrderBuilderDto(orderDto), OrderBuilder.class);
		Order order = new Order(orderBuilder);
		if (null == order.getOrderprice() || order.getOrderprice() == BigDecimal.ZERO)
			orderBuilder.getOrderDetails().setOrderType(OrderType.MARKET_ORDER);
		order.getOrderDetails().setOrderStatus(OrderStatus.ORDER_PLACED);
		// orderBuilder.setOrderBook(orderBook);
		orderBuilder.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		if (null == orderBuilder.getOrderDetails().getExecutionQuantity())
			orderBuilder.getOrderDetails().setExecutionQuantity(0l);
		orderBuilder.setCreatedOn(LocalDateTime.now());
		orderRepository.save(order);
		return new ModelMapper().map(orderBook, OrderBookDto.class);
	}

	@Override
	public synchronized OrderBookDto closeOrderBook(Long orderBookId) {
		OrderBook orderBook = new ModelMapper().map(getOrderBook(orderBookId), OrderBook.class);
		if (!OrderBookStatus.OPEN.equals(orderBook.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.BOOK_STATUS_NOT_OPEN);
		orderBook.setOrderBookStatus(OrderBookStatus.CLOSED);
		return new ModelMapper().map(orderBookRepository.save(orderBook), OrderBookDto.class);
	}

	@Override
	public synchronized OrderBookDto addExecutionToBook(Long orderBookId, ExecutionDto executionDto) {
		if (null == executionDto.getQuantity() || executionDto.getQuantity() == 0l)
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_QTY_INVALID);
		OrderBookDto orderBookDto = getOrderBook(orderBookId);
		if (!OrderBookStatus.CLOSED.equals(orderBookDto.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_NOT_CLOSED);
		if (!CollectionUtils.isEmpty(orderBookDto.getExecutions())
				&& !executionDto.getPrice().equals(orderBookDto.getExecutions().iterator().next().getPrice()))
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_PRICE_INVALID);
		executionDto.setOrderBook(orderBookDto);
		List<OrderDto> validOrderdDtos = orderService.getValidOrders(orderBookDto, executionDto);
		Long totalDemand = 0l;
		Long totalExecutions = 0l;
		for (OrderDto orderDto : validOrderdDtos) {
			totalDemand = totalDemand + orderDto.getOrderQuantity();
			totalExecutions += (null == orderDto.getOrderDetails().getExecutionQuantity() ? 0
					: orderDto.getOrderDetails().getExecutionQuantity());
		}
		if (totalDemand <= totalExecutions.doubleValue()) {
			orderBookDto.setOrderBookStatus(OrderBookStatus.EXECUTED);
			OrderBook orderBook = orderBookRepository.save(new ModelMapper().map(orderBookDto, OrderBook.class));
			orderBookDto = new ModelMapper().map(orderBook, OrderBookDto.class);
			return orderBookDto;
		}
		Long effectiveQtyForCurrentExec = ((totalExecutions + executionDto.getQuantity()) > totalDemand)
				? (long) (totalDemand - totalExecutions)
				: executionDto.getQuantity();
		orderBookDto.getOrders().removeAll(validOrderdDtos);
		orderService.addExecutionQuantityToOrders(validOrderdDtos, totalDemand, effectiveQtyForCurrentExec);
		Execution execution = new ModelMapper().map(executionDto, Execution.class);
		executionRepository.save(execution);
		boolean isDeltaExecution = effectiveQtyForCurrentExec != executionDto.getQuantity();
		if (isDeltaExecution) {
			executionDto.setQuantity(effectiveQtyForCurrentExec);
			orderBookDto.setOrderBookStatus(OrderBookStatus.EXECUTED);
			orderBookDto.setCreatedBy(ApplicationConstants.PARTIAL_EXECUTION);
			return orderBookDto;
		}
		return orderBookDto;
	}

	@Override
	public OrderBookStatisticsDto getOrderBookStats(Long orderBookId) {
		OrderBookDto orderBookDto = getOrderBook(orderBookId);
		Set<OrderDto> bookOrders = orderBookDto.getOrders();
		Map<OrderTypesInStats, OrderDto> OrderTypeClassification = new HashMap<>();
		BigDecimal executionPrice = (CollectionUtils.isEmpty(orderBookDto.getExecutions()) ? BigDecimal.ZERO
				: orderBookDto.getExecutions().iterator().next().getPrice());
		Set<OrderDto> validOrderDtos = getValidOrders(bookOrders, executionPrice);

		OrderBookStatisticsDto orderBookStatsVo = new OrderBookStatisticsDto();
		orderBookStatsVo.setOrderBookDto(orderBookDto);
		orderBookStatsVo.setTotalNoOfOrders((long) bookOrders.size());
		orderBookStatsVo.setValidOrderCount((long) validOrderDtos.size());
		orderBookStatsVo.setInValidOrderCount((long) (bookOrders.size() - validOrderDtos.size()));

		Long validDemand = 0l;
		Long totalExecutionQuqntity = 0l;
		Long totalOrderDemand = 0l;
		for (OrderDto orderDto : validOrderDtos) {
			validDemand = validDemand + orderDto.getOrderQuantity();
			totalExecutionQuqntity = totalExecutionQuqntity + (orderDto.getOrderDetails().getExecutionQuantity());
		}
		orderBookStatsVo.setValidDemand(validDemand);
		orderBookStatsVo.setExecutionQty(totalExecutionQuqntity);

		Long smallestOrderQuantity = Long.MAX_VALUE;
		Long LargestOrderQuantity = Long.MIN_VALUE;
		LocalDateTime earliest = LocalDateTime.MAX;
		LocalDateTime Laetst = LocalDateTime.MIN;
		for (OrderDto orderDto : bookOrders) {
			totalOrderDemand = totalOrderDemand + orderDto.getOrderQuantity();
			if (smallestOrderQuantity > orderDto.getOrderQuantity()) {
				smallestOrderQuantity = orderDto.getOrderQuantity();
				OrderTypeClassification.put(OrderTypesInStats.SMALLEST_ORDER, orderDto);
			}
			if (LargestOrderQuantity < orderDto.getOrderQuantity()) {
				LargestOrderQuantity = orderDto.getOrderQuantity();
				OrderTypeClassification.put(OrderTypesInStats.BIGGEST_ORDER, orderDto);
			}
			if (earliest.compareTo(orderBookDto.getCreatedOn()) > 1) {
				earliest = orderBookDto.getCreatedOn();
				OrderTypeClassification.put(OrderTypesInStats.EARLIEST_ORDER, orderDto);
			}
			if (Laetst.compareTo(orderDto.getCreatedOn()) < 1) {
				Laetst = orderBookDto.getCreatedOn();
				OrderTypeClassification.put(OrderTypesInStats.LATEST_ORDER, orderDto);
			}
		}

		orderBookStatsVo.setOrderStats(OrderTypeClassification);
		orderBookStatsVo.setTotalNoofAccuOrders(totalOrderDemand);
		orderBookStatsVo.setInvalidDemand(totalOrderDemand - validDemand);
		orderBookStatsVo.setExecutionPrice(executionPrice.multiply(executionPrice));
		return orderBookStatsVo;
	}

	private Set<OrderDto> getValidOrders(Set<OrderDto> bookOrderDtos, BigDecimal executionPrice) {
		Set<OrderDto> validOrderDtos = new HashSet<>();
		for (OrderDto orderDto : bookOrderDtos) {
			if (orderDto.getOrderDetails().getOrderType().equals(OrderType.MARKET_ORDER))
				validOrderDtos.add(orderDto);
			else if ((executionPrice
					.compareTo(null == orderDto.getOrderprice() ? BigDecimal.ZERO : orderDto.getOrderprice()) == -1
					&& orderDto.getOrderDetails().getOrderType() == OrderType.LIMIT_ORDER))
				validOrderDtos.add(orderDto);
		}
		return validOrderDtos;
	}

	@Override
	public OrderBookDto createDefaultOrderBook() {
		OrderBookDto orderBookDto = new OrderBookDto();
		orderBookDto.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBookDto.setCreatedOn(LocalDateTime.now());
		orderBookDto.setOrderBookStatus(OrderBookStatus.OPEN);
		return orderBookDto;
	}

}
