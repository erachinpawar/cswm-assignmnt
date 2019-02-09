package com.cswm.assignment.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.cswm.assignment.applicationutils.OrderTypesInStatistics;
import com.cswm.assignment.configuration.CustomModelMapper;
import com.cswm.assignment.exceptions.ApplicationException;
import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.model.OrderDetails;
import com.cswm.assignment.model.dto.ExecutionDto;
import com.cswm.assignment.model.dto.OrderBookDto;
import com.cswm.assignment.model.dto.OrderBookStatisticsDto;
import com.cswm.assignment.model.dto.OrderDetailsDto;
import com.cswm.assignment.model.dto.OrderDto;
import com.cswm.assignment.repository.ExecutionRepository;
import com.cswm.assignment.repository.InstrumentRepository;
import com.cswm.assignment.repository.OrderBookRepository;
import com.cswm.assignment.repository.OrderDetailsRepository;
import com.cswm.assignment.repository.OrderRepository;
import com.cswm.assignment.service.OrderBookService;
import com.cswm.assignment.service.OrderService;

@Service
@Transactional
public class OrderBookServiceImpl implements OrderBookService {

	@Autowired
	private OrderBookRepository orderBookRepository;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private InstrumentRepository instrumentRepository;

	@Autowired
	private ExecutionRepository executionRepository;

	@Autowired
	private OrderDetailsRepository orderDetailsRepository;

	private OrderBookDto getOrderBook(Long orderBookId) {
		OrderBook orderBook = orderBookRepository.findById(orderBookId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.ORDER_BOOK_NOT_FOUND));
		return new ModelMapper().map(orderBook, OrderBookDto.class);
	}

	@Override
	public synchronized OrderBookDto createOrderBook(OrderBookDto orderBookDto) {
		if (null == orderBookDto.getInstrument() || null == orderBookDto.getInstrument().getInstrumentId())
			throw new ApplicationException(ErrorMessageEnum.BOOK_WITHOUT_INSTRUMENT);
		Optional<Instrument> instrument = instrumentRepository.findById(orderBookDto.getInstrument().getInstrumentId());
		if (instrument.isPresent() && orderBookRepository.findFirstByInstrumentAndOrderBookStatusNot(instrument.get(),
				OrderBookStatus.EXECUTED) != null)
			throw new ApplicationException(ErrorMessageEnum.NOT_EXECUTED_ORDER_BOOK_PRESENT);
		orderBookDto.getInstrument().setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBookDto.getInstrument().setCreatedOn(LocalDateTime.now());
		orderBookDto.setCreatedOn(LocalDateTime.now());
		orderBookDto.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBookDto.setOrderBookStatus(OrderBookStatus.OPEN);
		OrderBook orderBook = CustomModelMapper.getOrderBookModelMapper().map(orderBookDto, OrderBook.class);
		orderBook = orderBookRepository.save(orderBook);
		return new ModelMapper().map(orderBook, OrderBookDto.class);
	}

	@Override
	public synchronized OrderDto addOrderToOrderBook(Long orderBookId, OrderDto orderDto) {
		if (null == orderDto.getOrderQuantity() || orderDto.getOrderQuantity() <= 0l)
			throw new ApplicationException(ErrorMessageEnum.ORDER_QUANTITY_INVALID);
		if (null == orderDto.getInstrument() || StringUtils.isEmpty(orderDto.getInstrument().getInstrumentId()))
			throw new ApplicationException(ErrorMessageEnum.INSRTUMENT_NOT_PRESENT);
		OrderBookDto orderBookDto = getOrderBook(orderBookId);
		if (!OrderBookStatus.OPEN.equals(orderBookDto.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_NOT_OPEN);
		if (!orderBookDto.getInstrument().getInstrumentId().equals(orderDto.getInstrument().getInstrumentId()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_NOT_BELONG_TO_INSTRUMENT);
		if (null == orderDto.getOrderDetails())
			orderDto.setOrderDetails(new OrderDetailsDto());
		if (null == orderDto.getOrderprice() || orderDto.getOrderprice() == BigDecimal.ZERO)
			orderDto.getOrderDetails().setOrderType(OrderType.MARKET_ORDER);
		else
			orderDto.getOrderDetails().setOrderType(OrderType.LIMIT_ORDER);
		orderDto.setOrderBook(orderBookDto);
		orderDto.getOrderDetails().setOrderStatus(OrderStatus.VALID);
		orderDto.setCreatedOn(LocalDateTime.now());
		orderDto.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderDto.getOrderDetails().setExecutionQuantity(0l);
		ModelMapper modelMapper = CustomModelMapper.getOrderModelMapper();
		Order order = modelMapper.map(orderDto, Order.class);
		order = orderRepository.save(order);
		return modelMapper.map(order, OrderDto.class);
	}

	@Override
	public synchronized OrderBookDto closeOrderBook(Long orderBookId) {
		OrderBook orderBook = new ModelMapper().map(getOrderBook(orderBookId), OrderBook.class);
		if (!OrderBookStatus.OPEN.equals(orderBook.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.BOOK_STATUS_NOT_OPEN);
		orderBook.setOrderBookStatus(OrderBookStatus.CLOSED);
		orderBookRepository.updateOrderBookStatus(orderBookId, OrderBookStatus.CLOSED);
		return new ModelMapper().map(orderBook, OrderBookDto.class);
	}

	@Override
	public synchronized OrderBookDto addExecutionToBook(Long orderBookId, ExecutionDto executionDto) {
		if (null == executionDto.getQuantity() || executionDto.getQuantity() == 0l)
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_QTY_INVALID);
		if (null == executionDto.getPrice() || executionDto.getPrice() == BigDecimal.ZERO)
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_PRICE_ZERO);
		OrderBookDto orderBookDto = getOrderBook(orderBookId);
		/*
		 * TODO : need to ask which code to keep. Whether one condition fine as prev. or
		 * need reason for rejection more specific. if
		 * (!OrderBookStatus.CLOSED.equals(orderBookDto.getOrderBookStatus())) throw new
		 * ApplicationException(ErrorMessageEnum.ORDER_BOOK_NOT_CLOSED);
		 */

		if (OrderBookStatus.OPEN.equals(orderBookDto.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_STATUS_OPEN);
		if (OrderBookStatus.EXECUTED.equals(orderBookDto.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_STATUS_EXECUTED);
		if (!CollectionUtils.isEmpty(orderBookDto.getExecutions())
				&& !(executionDto.getPrice().compareTo(orderBookDto.getExecutions().iterator().next().getPrice()) == 0))
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_PRICE_INVALID);
		executionDto.setOrderBook(orderBookDto);
		Set<OrderDto> validOrderdDtos = new HashSet<OrderDto>();
		if (CollectionUtils.isEmpty(orderBookDto.getExecutions()))
			validOrderdDtos = getValidOrders(orderBookDto.getOrders(), executionDto.getPrice());
		else
			validOrderdDtos = orderBookDto.getOrders().stream()
					.filter(orderDto -> orderDto.getOrderDetails().getOrderStatus().equals(OrderStatus.VALID))
					.collect(Collectors.toSet());

		Long totalDemand = 0l;
		Long totalExecutions = 0l;
		for (OrderDto orderDto : validOrderdDtos) {
			totalDemand = totalDemand + orderDto.getOrderQuantity();
			totalExecutions += (null == orderDto.getOrderDetails().getExecutionQuantity() ? 0
					: orderDto.getOrderDetails().getExecutionQuantity());
		}
		if (totalDemand <= totalExecutions.doubleValue()) {
			orderBookDto.setOrderBookStatus(OrderBookStatus.EXECUTED);
			orderBookRepository.updateOrderBookStatus(orderBookId, OrderBookStatus.EXECUTED);

			return orderBookDto;
		}
		Long effectiveQtyForCurrentExec = ((totalExecutions + executionDto.getQuantity()) > totalDemand)
				? (long) (totalDemand - totalExecutions)
				: executionDto.getQuantity();
		orderService.addExecutionQuantityToOrders(validOrderdDtos, totalDemand, effectiveQtyForCurrentExec);
		Execution execution = new ModelMapper().map(executionDto, Execution.class);
		executionRepository.save(execution);
		boolean isDeltaExecution = effectiveQtyForCurrentExec != executionDto.getQuantity();
		if (isDeltaExecution) {
			executionDto.setQuantity(effectiveQtyForCurrentExec);
			orderBookDto.setOrderBookStatus(OrderBookStatus.EXECUTED);
			orderBookDto = getOrderBook(orderBookId);
			return orderBookDto;
		}
		orderBookDto = getOrderBook(orderBookId);
		return orderBookDto;
	}

	@Override
	public synchronized OrderBookStatisticsDto getOrderBookStats(Long orderBookId) {
		OrderBookDto orderBookDto = getOrderBook(orderBookId);
		Set<OrderDto> bookOrders = orderBookDto.getOrders();
		Map<OrderTypesInStatistics, OrderDto> OrderTypeClassification = new HashMap<>();
		BigDecimal executionPrice = (CollectionUtils.isEmpty(orderBookDto.getExecutions()) ? BigDecimal.ZERO
				: orderBookDto.getExecutions().iterator().next().getPrice());
		Set<OrderDto> validOrderDtos = bookOrders.stream()
				.filter(orderDto -> orderDto.getOrderDetails().getOrderStatus().equals(OrderStatus.VALID))
				.collect(Collectors.toSet());

		OrderBookStatisticsDto orderBookStatsVo = new OrderBookStatisticsDto();
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
				OrderTypeClassification.put(OrderTypesInStatistics.SMALLEST_ORDER, orderDto);
			}
			if (LargestOrderQuantity < orderDto.getOrderQuantity()) {
				LargestOrderQuantity = orderDto.getOrderQuantity();
				OrderTypeClassification.put(OrderTypesInStatistics.BIGGEST_ORDER, orderDto);
			}
			if (earliest.compareTo(orderDto.getCreatedOn()) > 1) {
				earliest = orderDto.getCreatedOn();
				OrderTypeClassification.put(OrderTypesInStatistics.EARLIEST_ORDER, orderDto);
			}
			if (Laetst.compareTo(orderDto.getCreatedOn()) < 1) {
				Laetst = orderDto.getCreatedOn();
				OrderTypeClassification.put(OrderTypesInStatistics.LATEST_ORDER, orderDto);
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
			else {
				orderDto.getOrderDetails().setOrderStatus(OrderStatus.INVALID);
				orderDetailsRepository.save(new ModelMapper().map(orderDto.getOrderDetails(), OrderDetails.class));
			}
		}
		return validOrderDtos;
	}

}
