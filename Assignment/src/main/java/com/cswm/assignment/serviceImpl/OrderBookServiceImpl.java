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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.cswm.assignment.model.dto.OrderBookValidInValidStatistics;
import com.cswm.assignment.model.dto.OrderDetailsDto;
import com.cswm.assignment.model.dto.OrderDto;
import com.cswm.assignment.model.dto.inputDto.AddOrderInputDto;
import com.cswm.assignment.model.dto.inputDto.ExecutionInputDto;
import com.cswm.assignment.model.dto.inputDto.OrderBookInputDto;
import com.cswm.assignment.repository.ExecutionRepository;
import com.cswm.assignment.repository.InstrumentRepository;
import com.cswm.assignment.repository.OrderBookRepository;
import com.cswm.assignment.repository.OrderDetailsRepository;
import com.cswm.assignment.repository.OrderRepository;
import com.cswm.assignment.service.OrderBookService;
import com.cswm.assignment.service.OrderService;

/**
 * @author sachinpawar
 *
 */
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
	private final Logger logger = LoggerFactory.getLogger(OrderBookServiceImpl.class);

	/**
	 * get the order book details of particular order book id
	 * 
	 * @param orderBookId
	 * @return OrderBookDto
	 */
	private OrderBook getOrderBook(Long orderBookId) {
		logger.info("getOrderBook() Method called with argument :: " + orderBookId);
		OrderBook orderBook = orderBookRepository.findById(orderBookId).orElseThrow(() -> new NotFoundException(ErrorMessageEnum.ORDER_BOOK_NOT_FOUND));
		logger.info("getOrderBook() Method returned value  :: " + orderBook.toString());
		return orderBook;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cswm.assignment.service.OrderBookService#createOrderBook(com.cswm.
	 * assignment.model.dto.OrderBookDto)
	 */
	@Override
	public synchronized OrderBookDto createOrderBook(OrderBookInputDto orderBookInputDto) {
		logger.info("createOrderBook() Method called with argument :: " + orderBookInputDto.toString());
		if (null == orderBookInputDto.getInstrumentId())
			throw new ApplicationException(ErrorMessageEnum.BOOK_WITHOUT_INSTRUMENT);
		Optional<Instrument> instrument = instrumentRepository.findById(orderBookInputDto.getInstrumentId());
		if (instrument.isPresent() && orderBookRepository.findFirstByInstrumentAndOrderBookStatusNot(instrument.get(), OrderBookStatus.EXECUTED) != null)
			throw new ApplicationException(ErrorMessageEnum.NOT_EXECUTED_ORDER_BOOK_PRESENT);
		OrderBook orderBook = new ModelMapper().map(orderBookInputDto, OrderBook.class);
		orderBook.getInstrument().setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBook.getInstrument().setCreatedOn(LocalDateTime.now());
		orderBook.setCreatedOn(LocalDateTime.now());
		orderBook.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBook.setOrderBookStatus(OrderBookStatus.OPEN);
		orderBook = orderBookRepository.save(orderBook);
		logger.info("createOrderBook() Method returned value  :: " + orderBook.toString());
		return new ModelMapper().map(orderBook, OrderBookDto.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cswm.assignment.service.OrderBookService#addOrderToOrderBook(java.lang.
	 * Long, com.cswm.assignment.model.dto.OrderDto)
	 */
	@Override
	public synchronized OrderDto addOrderToOrderBook(Long orderBookId, AddOrderInputDto addOrderInputDto) {
		logger.info("addOrderToOrderBook() Method called with argument :: (" + orderBookId + addOrderInputDto.toString() + ");");
		if (null == addOrderInputDto.getOrderQuantity() || addOrderInputDto.getOrderQuantity() <= 0l)
			throw new ApplicationException(ErrorMessageEnum.ORDER_QUANTITY_INVALID);
		if (null == addOrderInputDto.getInstrumentId())
			throw new ApplicationException(ErrorMessageEnum.INSRTUMENT_NOT_PRESENT);
		OrderBook orderBook = getOrderBook(orderBookId);
		if (!OrderBookStatus.OPEN.equals(orderBook.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_NOT_OPEN);
		if (!orderBook.getInstrument().getInstrumentId().equals(addOrderInputDto.getInstrumentId()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_NOT_BELONG_TO_INSTRUMENT);
		OrderDto orderDto = new ModelMapper().map(addOrderInputDto, OrderDto.class);
		if (null == orderDto.getOrderDetails())
			orderDto.setOrderDetails(new OrderDetailsDto());
		if (null == orderDto.getOrderprice() || orderDto.getOrderprice() == BigDecimal.ZERO) {
			logger.info("createOrderBook() Method  :: No order Price is provided in order creation hence marking order as MARKET ORDER");
			orderDto.getOrderDetails().setOrderType(OrderType.MARKET_ORDER);
		} else {
			logger.info("createOrderBook() Method  :: Order Price is provided in order creation hence marking order as LIMIT_ORDER");
			orderDto.getOrderDetails().setOrderType(OrderType.LIMIT_ORDER);
		}
		orderDto.setOrderBook(new ModelMapper().map(orderBook, OrderBookDto.class));
		orderDto.getOrderDetails().setOrderStatus(OrderStatus.VALID);
		orderDto.setCreatedOn(LocalDateTime.now());
		orderDto.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderDto.getOrderDetails().setExecutionQuantity(0l);
		ModelMapper modelMapper = CustomModelMapper.getOrderModelMapper();
		Order order = modelMapper.map(orderDto, Order.class);
		order = orderRepository.save(order);
		logger.info("createOrderBook() Method returned value  :: " + order.toString());
		return modelMapper.map(order, OrderDto.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cswm.assignment.service.OrderBookService#closeOrderBook(java.lang.Long)
	 */
	@Override
	public synchronized OrderBookDto closeOrderBook(Long orderBookId) {
		logger.info("closeOrderBook() Method called with argument :: (" + orderBookId + ");");
		OrderBook orderBook = new ModelMapper().map(getOrderBook(orderBookId), OrderBook.class);
		if (!OrderBookStatus.OPEN.equals(orderBook.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.BOOK_STATUS_NOT_OPEN);
		orderBook.setOrderBookStatus(OrderBookStatus.CLOSED);
		orderBookRepository.updateOrderBookStatus(orderBookId, OrderBookStatus.CLOSED);
		logger.info("closeOrderBook() Method returned value  :: " + orderBook.toString());
		return new ModelMapper().map(orderBook, OrderBookDto.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cswm.assignment.service.OrderBookService#addExecutionToBook(java.lang.
	 * Long, com.cswm.assignment.model.dto.ExecutionDto)
	 */
	@Override
	public synchronized OrderBookDto addExecutionToBook(Long orderBookId, ExecutionInputDto executionInputDto) {
		logger.info("addExecutionToBook() Method called with argument :: (" + orderBookId + executionInputDto.toString() + ");");
		if (null == executionInputDto.getQuantity() || executionInputDto.getQuantity() == 0l)
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_QTY_INVALID);
		if (null == executionInputDto.getPrice() || executionInputDto.getPrice() == BigDecimal.ZERO)
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_PRICE_ZERO);
		OrderBook orderBook = getOrderBook(orderBookId);
		/*
		 * TODO : need to ask which code to keep. Whether one condition fine as prev. or
		 * need reason for rejection more specific. if
		 * (!OrderBookStatus.CLOSED.equals(orderBookDto.getOrderBookStatus())) throw new
		 * ApplicationException(ErrorMessageEnum.ORDER_BOOK_NOT_CLOSED);
		 */

		if (OrderBookStatus.OPEN.equals(orderBook.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_STATUS_OPEN);
		if (OrderBookStatus.EXECUTED.equals(orderBook.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_STATUS_EXECUTED);
		if (!CollectionUtils.isEmpty(orderBook.getExecutions()) && !(executionInputDto.getPrice().compareTo(orderBook.getExecutions().iterator().next().getPrice()) == 0))
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_PRICE_INVALID);
		ExecutionDto executionDto = new ModelMapper().map(executionInputDto, ExecutionDto.class);
		executionDto.setOrderBook(new ModelMapper().map(orderBook, OrderBookDto.class));
		Set<Order> validOrders = new HashSet<Order>();
		if (CollectionUtils.isEmpty(orderBook.getExecutions())) {
			logger.info("addExecutionToBook() Method :: Adding the first execution to the OrderBook So Marking the orders as Valid/Invalid");
			validOrders = getValidOrders(orderBook.getOrders(), executionDto.getPrice());
		} else {
			validOrders = orderBook.getOrders().stream().filter(orderDto -> orderDto.getOrderDetails().getOrderStatus().equals(OrderStatus.VALID)).collect(Collectors.toSet());
			logger.info("addExecutionToBook() Method :: valid Orders in the Order Book are : ");
			validOrders.forEach(orderDto -> logger.info("addExecutionToBook() Method :: " + orderDto.toString()));
		}

		Long totalDemand = 0l;
		Long totalExecutions = 0l;
		for (Order order : validOrders) {
			totalDemand = totalDemand + order.getOrderQuantity();
			totalExecutions += (null == order.getOrderDetails().getExecutionQuantity() ? 0 : order.getOrderDetails().getExecutionQuantity());
		}
		logger.info("addExecutionToBook() Method :: validDemands in the order Book = " + totalDemand);
		logger.info("addExecutionToBook() Method :: totalExecutions in the order Book = " + totalExecutions);
		if (totalDemand <= totalExecutions.doubleValue()) {
			logger.info("addExecutionToBook() Method :: validDemands is less than total execution marking the order book as executed");
			orderBook.setOrderBookStatus(OrderBookStatus.EXECUTED);
			orderBookRepository.updateOrderBookStatus(orderBookId, OrderBookStatus.EXECUTED);
			return CustomModelMapper.getOrderBookModelMapper().map(orderBook, OrderBookDto.class);
		}

		Long effectiveQtyForCurrentExec = ((totalExecutions + executionDto.getQuantity()) > totalDemand) ? (long) (totalDemand - totalExecutions) : executionDto.getQuantity();
		logger.info("addExecutionToBook() Method :: effective Quantity for the execution = " + effectiveQtyForCurrentExec);
		orderService.addExecutionQuantityToOrders(validOrders, totalDemand, effectiveQtyForCurrentExec);
		Execution execution = new ModelMapper().map(executionDto, Execution.class);
		logger.info("addExecutionToBook() Method :: Final Execution getting saved as " + execution);
		executionRepository.save(execution);
		if (effectiveQtyForCurrentExec != executionDto.getQuantity()) {
			logger.info("addExecutionToBook() Method :: execution executed partially as the effectice quantity for execution = " + effectiveQtyForCurrentExec + " and original execution quantity = "
					+ execution.getQuantity());
			executionDto.setQuantity(effectiveQtyForCurrentExec);
			orderBook.setOrderBookStatus(OrderBookStatus.EXECUTED);
		}
		orderBook = getOrderBook(orderBookId);
		logger.info("addExecutionToBook() Method returned value  :: " + orderBook.toString());
		return CustomModelMapper.getOrderBookModelMapper().map(orderBook, OrderBookDto.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cswm.assignment.service.OrderBookService#getOrderBookStats(java.lang.
	 * Long)
	 */
	@Override
	public synchronized OrderBookStatisticsDto getOrderBookStats(Long orderBookId) {
		logger.info("getOrderBookStats() Method called with argument :: (" + orderBookId + ");");
		OrderBook orderBook = getOrderBook(orderBookId);
		Set<Order> bookOrders = orderBook.getOrders();
		Map<OrderTypesInStatistics, OrderDto> OrderTypeClassification = new HashMap<>();
		Map<BigDecimal, Long> limitPriceVsDemandTable = new HashMap<>();

		OrderBookStatisticsDto orderBookStatsVo = new OrderBookStatisticsDto();
		orderBookStatsVo.setTotalNoOfOrders((long) bookOrders.size());

		Long totalOrderDemand = 0l;
		Long smallestOrderQuantity = Long.MAX_VALUE;
		Long LargestOrderQuantity = Long.MIN_VALUE;
		LocalDateTime earliest = LocalDateTime.MAX;
		LocalDateTime Laetst = LocalDateTime.MIN;
		for (Order order : bookOrders) {
			totalOrderDemand = totalOrderDemand + order.getOrderQuantity();
			if (order.getOrderDetails().getOrderType().equals(OrderType.LIMIT_ORDER) && limitPriceVsDemandTable.containsKey(order.getOrderprice())) {
				Long newDemand = limitPriceVsDemandTable.get(order.getOrderprice()) + order.getOrderQuantity();
				limitPriceVsDemandTable.put(order.getOrderprice(), newDemand);
			} else if (order.getOrderDetails().getOrderType().equals(OrderType.LIMIT_ORDER)) {
				limitPriceVsDemandTable.put(order.getOrderprice(), order.getOrderQuantity());
			}
			OrderDto orderDto = CustomModelMapper.getOrderModelMapper().map(order, OrderDto.class);
			if (smallestOrderQuantity > order.getOrderQuantity()) {
				smallestOrderQuantity = order.getOrderQuantity();
				OrderTypeClassification.put(OrderTypesInStatistics.SMALLEST_ORDER, orderDto);
			}
			if (LargestOrderQuantity < order.getOrderQuantity()) {
				LargestOrderQuantity = order.getOrderQuantity();
				OrderTypeClassification.put(OrderTypesInStatistics.BIGGEST_ORDER, orderDto);
			}
			if (earliest.compareTo(order.getCreatedOn()) > 1) {
				earliest = order.getCreatedOn();
				OrderTypeClassification.put(OrderTypesInStatistics.EARLIEST_ORDER, orderDto);
			}
			if (Laetst.compareTo(order.getCreatedOn()) < 1) {
				Laetst = order.getCreatedOn();
				OrderTypeClassification.put(OrderTypesInStatistics.LATEST_ORDER, orderDto);
			}
		}
		orderBookStatsVo.setLimitPriceVsDemandTable(limitPriceVsDemandTable);
		orderBookStatsVo.setOrderTypesInStats(OrderTypeClassification);
		orderBookStatsVo.setTotalNoofAccuOrders(totalOrderDemand);
		logger.info("getOrderBookStats() Method returned value :: (" + orderBookStatsVo + ");");
		return orderBookStatsVo;
	}

	/**
	 * Used to mark the order list under the specific order book as valid or invalid
	 * 
	 * @param orders
	 * @param executionPrice
	 * @return set of valid orders
	 */
	private Set<Order> getValidOrders(Set<Order> orders, BigDecimal executionPrice) {
		logger.info("getValidOrders() Method called with argument :: (" + orders + "," + executionPrice + ");");
		Set<Order> validOrders = new HashSet<>();
		for (Order order : orders) {
			if (order.getOrderDetails().getOrderType().equals(OrderType.MARKET_ORDER))
				validOrders.add(order);
			else if ((executionPrice.compareTo(null == order.getOrderprice() ? BigDecimal.ZERO : order.getOrderprice()) == -1 && order.getOrderDetails().getOrderType() == OrderType.LIMIT_ORDER))
				validOrders.add(order);
			else {
				order.getOrderDetails().setOrderStatus(OrderStatus.INVALID);
				orderDetailsRepository.save(new ModelMapper().map(order.getOrderDetails(), OrderDetails.class));
			}
		}
		logger.info("getOrderBookStats() Method returned value :: (" + validOrders + ");");
		return validOrders;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cswm.assignment.service.OrderBookService#
	 * getOrderBookValidInvalidOrdersStats(java.lang.Long)
	 */
	@Override
	public OrderBookValidInValidStatistics getOrderBookValidInvalidOrdersStats(Long orderBookId) {
		logger.info("getValidOrders() Method called with argument :: (" + orderBookId + ");");
		OrderBookValidInValidStatistics bookValidInValidStatistics = new OrderBookValidInValidStatistics(getOrderBookStats(orderBookId));

		OrderBook orderBook = getOrderBook(orderBookId);
		Set<Order> bookOrders = orderBook.getOrders();
		BigDecimal executionPrice = (CollectionUtils.isEmpty(orderBook.getExecutions()) ? BigDecimal.ZERO : orderBook.getExecutions().iterator().next().getPrice());
		Set<Order> validOrders = bookOrders.stream().filter(orderDto -> orderDto.getOrderDetails().getOrderStatus().equals(OrderStatus.VALID)).collect(Collectors.toSet());

		bookValidInValidStatistics.setValidOrderCount((long) validOrders.size());
		bookValidInValidStatistics.setInValidOrderCount((long) (bookOrders.size() - validOrders.size()));

		Long validDemand = 0l;
		Long totalExecutionQuqntity = 0l;
		for (Order order : validOrders) {
			validDemand = validDemand + order.getOrderQuantity();
			totalExecutionQuqntity = totalExecutionQuqntity + (order.getOrderDetails().getExecutionQuantity());
		}
		bookValidInValidStatistics.setValidDemand(validDemand);
		bookValidInValidStatistics.setExecutionQty(totalExecutionQuqntity);

		bookValidInValidStatistics.setInValidDemand(bookValidInValidStatistics.getOrderBookStatisticsDto().getTotalNoofAccuOrders() - validDemand);
		bookValidInValidStatistics.setTotalExecutionPrice(executionPrice.multiply(executionPrice));
		logger.info("getOrderBookValidInvalidOrdersStats() Method returned value :: (" + bookValidInValidStatistics + ");");
		return bookValidInValidStatistics;
	}

}
