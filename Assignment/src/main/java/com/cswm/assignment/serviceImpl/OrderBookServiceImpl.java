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
import com.cswm.assignment.configuration.CustomModelMapper;
import com.cswm.assignment.exceptions.ApplicationException;
import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.model.OrderDetails;
import com.cswm.assignment.model.dto.ExecutionBo;
import com.cswm.assignment.model.dto.OrderBo;
import com.cswm.assignment.model.dto.OrderBookBo;
import com.cswm.assignment.model.dto.OrderDetailsBo;
import com.cswm.assignment.model.dto.inputDto.AddOrderInputDto;
import com.cswm.assignment.model.dto.inputDto.ExecutionInputDto;
import com.cswm.assignment.model.dto.inputDto.OrderBookInputDto;
import com.cswm.assignment.model.dto.ouputDto.ClosedOrderBookOutputDto;
import com.cswm.assignment.model.dto.ouputDto.OrderBookOutputDto;
import com.cswm.assignment.model.dto.ouputDto.OrderBookStatisticsOutputDto;
import com.cswm.assignment.model.dto.ouputDto.OrderOutputDto;
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
	public synchronized OrderBookOutputDto createOrderBook(OrderBookInputDto orderBookInputDto) {
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
		return new ModelMapper().map(orderBook, OrderBookOutputDto.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cswm.assignment.service.OrderBookService#addOrderToOrderBook(java.lang.
	 * Long, com.cswm.assignment.model.dto.OrderDto)
	 */
	@Override
	public synchronized OrderOutputDto addOrderToOrderBook(Long orderBookId, AddOrderInputDto addOrderInputDto) {
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
		OrderBo orderDto = new ModelMapper().map(addOrderInputDto, OrderBo.class);
		if (null == orderDto.getOrderDetails())
			orderDto.setOrderDetails(new OrderDetailsBo());
		if (null == orderDto.getOrderprice() || orderDto.getOrderprice() == BigDecimal.ZERO) {
			logger.info("createOrderBook() Method  :: No order Price is provided in order creation hence marking order as MARKET ORDER");
			orderDto.getOrderDetails().setOrderType(OrderType.MARKET_ORDER);
		} else {
			logger.info("createOrderBook() Method  :: Order Price is provided in order creation hence marking order as LIMIT_ORDER");
			orderDto.getOrderDetails().setOrderType(OrderType.LIMIT_ORDER);
		}
		orderDto.setOrderBook(new ModelMapper().map(orderBook, OrderBookBo.class));
		orderDto.getOrderDetails().setOrderStatus(OrderStatus.VALID);
		orderDto.setCreatedOn(LocalDateTime.now());
		orderDto.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderDto.getOrderDetails().setExecutionQuantity(0l);
		ModelMapper modelMapper = CustomModelMapper.getOrderModelMapper();
		Order order = modelMapper.map(orderDto, Order.class);
		order = orderRepository.save(order);
		logger.info("createOrderBook() Method returned value  :: " + order.toString());
		return modelMapper.map(order, OrderOutputDto.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cswm.assignment.service.OrderBookService#closeOrderBook(java.lang.Long)
	 */
	@Override
	public synchronized ClosedOrderBookOutputDto closeOrderBook(Long orderBookId) {
		logger.info("closeOrderBook() Method called with argument :: (" + orderBookId + ");");
		OrderBook orderBook = new ModelMapper().map(getOrderBook(orderBookId), OrderBook.class);
		if (!OrderBookStatus.OPEN.equals(orderBook.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.BOOK_STATUS_NOT_OPEN);
		orderBook.setOrderBookStatus(OrderBookStatus.CLOSED);
		orderBookRepository.updateOrderBookStatus(orderBookId, OrderBookStatus.CLOSED);
		logger.info("closeOrderBook() Method returned value  :: " + orderBook.toString());
		return new ModelMapper().map(orderBook, ClosedOrderBookOutputDto.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cswm.assignment.service.OrderBookService#addExecutionToBook(java.lang.
	 * Long, com.cswm.assignment.model.dto.ExecutionDto)
	 */
	@Override
	public synchronized OrderBookOutputDto addExecutionToBook(Long orderBookId, ExecutionInputDto executionInputDto) {
		logger.info("addExecutionToBook() Method called with argument :: (" + orderBookId + executionInputDto.toString() + ");");
		if (null == executionInputDto.getQuantity() || executionInputDto.getQuantity() == 0l)
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_QTY_INVALID);
		if (null == executionInputDto.getPrice() || executionInputDto.getPrice() == BigDecimal.ZERO)
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_PRICE_ZERO);
		OrderBook orderBook = getOrderBook(orderBookId);
		if (!OrderBookStatus.CLOSED.equals(orderBook.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_NOT_CLOSED);
		if (!CollectionUtils.isEmpty(orderBook.getExecutions()) && !(executionInputDto.getPrice().compareTo(orderBook.getExecutions().iterator().next().getPrice()) == 0))
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_PRICE_INVALID);
		ExecutionBo executionDto = new ModelMapper().map(executionInputDto, ExecutionBo.class);
		executionDto.setOrderBook(new ModelMapper().map(orderBook, OrderBookBo.class));
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

		Long effectiveQtyForCurrentExec = ((totalExecutions + executionDto.getQuantity()) >= totalDemand) ? (long) (totalDemand) : executionDto.getQuantity() + totalExecutions;
		logger.info("addExecutionToBook() Method :: effective Quantity for the execution = " + effectiveQtyForCurrentExec);
		orderService.addExecutionQuantityToOrders(validOrders, totalDemand, effectiveQtyForCurrentExec);
		Execution execution = new ModelMapper().map(executionDto, Execution.class);
		logger.info("addExecutionToBook() Method :: Final Execution getting saved as " + execution);
		orderBook.getExecutions().add(execution);
		executionRepository.save(execution);
		if ((totalExecutions + executionDto.getQuantity()) >= totalDemand) {
			logger.info("addExecutionToBook() Method :: execution executed partially as the effectice quantity for execution = " + (totalDemand - totalExecutions)
					+ " and original execution quantity = " + execution.getQuantity());
			executionDto.setQuantity(totalDemand - totalExecutions);
			orderBook.setOrderBookStatus(OrderBookStatus.EXECUTED);
		}
		orderBook = getOrderBook(orderBookId);
		logger.info("addExecutionToBook() Method returned value  :: " + orderBook.toString());
		return new ModelMapper().map(orderBook, OrderBookOutputDto.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cswm.assignment.service.OrderBookService#getOrderBookStats(java.lang.
	 * Long)
	 */
	@Override
	public synchronized OrderBookStatisticsOutputDto getOrderBookStats(Long orderBookId) {
		logger.info("getOrderBookStats() Method called with argument :: (" + orderBookId + ");");
		OrderBook orderBook = getOrderBook(orderBookId);
		Map<BigDecimal, Long> limitBreakDownForAllOrders = new HashMap<>();
		Map<BigDecimal, Long> limitBreakDownForValidOrders = new HashMap<>();
		Map<BigDecimal, Long> limitBreakDownForInvalidOrders = new HashMap<>();
		Long totalOrderDemand = 0l;
		Long validOrderCount = 0l;
		Long validDemand = 0l;
		Long accumulatedExecutionQuantity = 0l;
		LocalDateTime earliest = LocalDateTime.MAX;
		LocalDateTime latest = LocalDateTime.MIN;

		OrderBookStatisticsOutputDto orderBookStatsVo = new OrderBookStatisticsOutputDto();
		orderBookStatsVo.setTotalOrderCount((long) orderBook.getOrders().size());
		for (Order order : orderBook.getOrders()) {
			OrderOutputDto orderDto = CustomModelMapper.getOrderModelMapper().map(order, OrderOutputDto.class);
			totalOrderDemand = totalOrderDemand + order.getOrderQuantity();

			if (order.getOrderDetails().getOrderType().equals(OrderType.LIMIT_ORDER) && limitBreakDownForAllOrders.containsKey(order.getOrderprice())) {
				Long newDemand = limitBreakDownForAllOrders.get(order.getOrderprice()) + order.getOrderQuantity();
				limitBreakDownForAllOrders.put(order.getOrderprice(), newDemand);
				if (order.getOrderDetails().getOrderStatus().equals(OrderStatus.VALID)) {
					limitBreakDownForValidOrders.put(order.getOrderprice(), newDemand);
				} else {
					limitBreakDownForInvalidOrders.put(order.getOrderprice(), newDemand);
				}
			} else if (order.getOrderDetails().getOrderType().equals(OrderType.LIMIT_ORDER)) {
				limitBreakDownForAllOrders.put(order.getOrderprice(), order.getOrderQuantity());
				if (order.getOrderDetails().getOrderStatus().equals(OrderStatus.VALID)) {
					limitBreakDownForValidOrders.put(order.getOrderprice(), order.getOrderQuantity());
				} else {
					limitBreakDownForInvalidOrders.put(order.getOrderprice(), order.getOrderQuantity());
				}
			}

			if (order.getOrderDetails().getOrderStatus().equals(OrderStatus.VALID)) {
				validDemand = validDemand + order.getOrderQuantity();
				validOrderCount++;
				accumulatedExecutionQuantity = accumulatedExecutionQuantity + (order.getOrderDetails().getExecutionQuantity());
			}

			if (null == orderBookStatsVo.getSmallestOrder() || orderBookStatsVo.getSmallestOrder().getOrderQuantity() > order.getOrderQuantity()) {
				orderBookStatsVo.setSmallestOrder(orderDto);
			}
			if (null == orderBookStatsVo.getBiggestOrder() || orderBookStatsVo.getBiggestOrder().getOrderQuantity() < order.getOrderQuantity()) {
				orderBookStatsVo.setBiggestOrder(orderDto);
			}
			if (null == orderBookStatsVo.getEarliestOrder() || earliest.compareTo(order.getCreatedOn()) > 0) {
				earliest = order.getCreatedOn();
				orderBookStatsVo.setEarliestOrder(orderDto);
			}
			if (null == orderBookStatsVo.getLastOrder() || latest.compareTo(order.getCreatedOn()) < 0) {
				latest = order.getCreatedOn();
				orderBookStatsVo.setLastOrder(orderDto);
			}
		}
		orderBookStatsVo.setValidOrderCount(validOrderCount);
		orderBookStatsVo.setInValidOrderCount(orderBookStatsVo.getTotalOrderCount() - validOrderCount);

		orderBookStatsVo.setTotalDemand(totalOrderDemand);
		orderBookStatsVo.setValidDemand(validDemand);
		orderBookStatsVo.setInValidDemand(totalOrderDemand - validDemand);

		if (!CollectionUtils.isEmpty(orderBook.getExecutions())) {
			orderBookStatsVo.setExecutionPrice(orderBook.getExecutions().iterator().next().getPrice());
		}
		orderBookStatsVo.setAccumulatedExecutionQuantity(accumulatedExecutionQuantity);
		orderBookStatsVo.setLimitBreakDownForAllOrders(limitBreakDownForAllOrders);
		orderBookStatsVo.setLimitBreakDownForInvalidOrders(limitBreakDownForInvalidOrders);
		orderBookStatsVo.setLimitBreakDownForValidOrders(limitBreakDownForValidOrders);

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
			else if ((executionPrice.compareTo(null == order.getOrderprice() ? BigDecimal.ZERO : order.getOrderprice()) <= 0 && order.getOrderDetails().getOrderType() == OrderType.LIMIT_ORDER))
				validOrders.add(order);
			else {
				order.getOrderDetails().setOrderStatus(OrderStatus.INVALID);
				orderDetailsRepository.save(new ModelMapper().map(order.getOrderDetails(), OrderDetails.class));
			}
		}
		logger.info("getOrderBookStats() Method returned value :: (" + validOrders + ");");
		return validOrders;

	}
}
