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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cswm.assignment.ApplicationConstants;
import com.cswm.assignment.applicationUtils.ErrorMessageEnum;
import com.cswm.assignment.applicationUtils.OrderBookStatus;
import com.cswm.assignment.applicationUtils.OrderStatus;
import com.cswm.assignment.applicationUtils.OrderType;
import com.cswm.assignment.exceptions.ApplicationException;
import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.model.dto.OrderBuilder;
import com.cswm.assignment.modelvos.OrderBookStatsVo;
import com.cswm.assignment.modelvos.OrderBookStatsVo.OrderTypesInStats;
import com.cswm.assignment.repository.OrderBookRepository;
import com.cswm.assignment.repository.OrderRepository;
import com.cswm.assignment.service.InstrumentService;
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
	InstrumentService instrumentService;

	@Override
	public List<OrderBook> getAllOrderBooks() {
		return orderBookRepository.findAll();
	}

	@Override
	public OrderBook getOrderBook(Long orderBookId) {
		return orderBookRepository.findById(orderBookId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.ORDER_BOOK_NOT_FOUND));
	}

	@Override
	public synchronized OrderBook createOrderBook(OrderBook orderBook) {
		if (null == orderBook.getInstrument())
			throw new ApplicationException(ErrorMessageEnum.BOOK_WITHOUT_INSTRUMENT);
		if (orderBookRepository.findFirstByInstrumentAndOrderBookStatusNot(orderBook.getInstrument(),
				OrderBookStatus.EXECUTED) != null)
			throw new ApplicationException(ErrorMessageEnum.NOT_EXECUTED_ORDER_BOOK_PRESENT);
		orderBook.setCreatedOn(LocalDateTime.now());
		orderBook.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBook.setOrderBookStatus(OrderBookStatus.OPEN);
		orderBook = orderBookRepository.save(orderBook);
		return orderBook;
	}

	@Override
	public synchronized OrderBook addOrderToOrderBook(Long orderBookId, Order order) {
		if (null == order.getOrderQuantity() || order.getOrderQuantity() <= 0l)
			throw new ApplicationException(ErrorMessageEnum.ORDER_QUANTITY_INVALID);
		if (null == order.getInstrument())
			throw new ApplicationException(ErrorMessageEnum.INSRTUMENT_NOT_PRESENT);
		OrderBook orderBook = getOrderBook(orderBookId);
		if (!OrderBookStatus.OPEN.equals(orderBook.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_NOT_OPEN);
		if (!orderBook.getInstrument().getInstrumentId().equals(order.getInstrument().getInstrumentId()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_NOT_BELONG_TO_INSTRUMENT);
		OrderBuilder orderBuilder = new OrderBuilder(order);
		if (null == order.getOrderprice() || order.getOrderprice() == BigDecimal.ZERO)
			orderBuilder.getOrderDetails().setOrderType(OrderType.MARKET_ORDER);
		order.getOrderDetails().setOrderStatus(OrderStatus.ORDER_PLACED);
		orderBuilder.setOrderBook(orderBook);
		orderBuilder.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		if (null == orderBuilder.getOrderDetails().getExecutionQuantity())
			orderBuilder.getOrderDetails().setExecutionQuantity(0l);
		orderBuilder.setCreatedOn(LocalDateTime.now());
		Set<Order> orders = CollectionUtils.isEmpty(orderBook.getOrders()) ? new HashSet<>() : orderBook.getOrders();
		orders.add(new Order(orderBuilder));
		orderBook.setOrders(orders);
		return orderBookRepository.save(orderBook);
	}

	@Override
	public synchronized OrderBook closeOrderBook(Long orderBookId) {
		OrderBook orderBook = getOrderBook(orderBookId);
		if (!OrderBookStatus.OPEN.equals(orderBook.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.BOOK_STATUS_NOT_OPEN);
		orderBook.setOrderBookStatus(OrderBookStatus.CLOSED);
		return orderBookRepository.save(orderBook);
	}

	@Override
	public synchronized OrderBook addExecutionToBook(Long orderBookId, Execution execution) {
		if (null == execution.getQuantity() || execution.getQuantity() == 0l)
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_QTY_INVALID);
		OrderBook orderBook = getOrderBook(orderBookId);
		if (!OrderBookStatus.CLOSED.equals(orderBook.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_NOT_CLOSED);
		execution.setOrderBook(orderBook);
		if (!CollectionUtils.isEmpty(orderBook.getExecutions())
				&& !execution.getPrice().equals(orderBook.getExecutions().iterator().next().getPrice()))
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_PRICE_INVALID);
		List<Order> validOrders = orderService.getValidOrders(orderBook, execution);
		Long totalDemand = 0l;
		Long totalExecutions = 0l;
		for (Order order : validOrders) {
			totalDemand = totalDemand + order.getOrderQuantity();
			totalExecutions += (null == order.getOrderDetails().getExecutionQuantity() ? 0
					: order.getOrderDetails().getExecutionQuantity());
		}
		if (totalDemand <= totalExecutions.doubleValue()) {
			orderBook.setOrderBookStatus(OrderBookStatus.EXECUTED);
			orderBookRepository.save(orderBook);
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_EXECUTED);
		}
		Long effectiveQuanty = ((totalExecutions + execution.getQuantity()) > totalDemand)
				? (long) (totalDemand - totalExecutions)
				: execution.getQuantity();
		orderBook.getOrders().removeAll(validOrders);
		validOrders=orderService.addExecutionQuantityToOrders(validOrders, totalDemand, effectiveQuanty);
		orderBook.getOrders().addAll(validOrders);
		boolean isDeltaExecution = effectiveQuanty != execution.getQuantity();
		Set<Execution> orderBookExecutions = CollectionUtils.isEmpty(orderBook.getExecutions()) ? new HashSet<>()
				: orderBook.getExecutions();
		orderBookExecutions.add(execution);
		orderBook.setExecutions(orderBookExecutions);
		if (isDeltaExecution) {
			execution.setQuantity(effectiveQuanty);
			orderBookRepository.save(orderBook);
			throw new ApplicationException(ErrorMessageEnum.PARTIALLY_EXECUTED);
		}
		return orderBookRepository.save(orderBook);
	}

	@Override
	public OrderBookStatsVo getOrderBookStats(Long orderBookId) {
		OrderBook orderBook = getOrderBook(orderBookId);
		List<Order> validOrders = orderService.getValidOrders(orderBook, null);
		List<Order> bookOrders = new ArrayList<>(orderBook.getOrders());
		List<Order> invalidOrders = new ArrayList<>(bookOrders);
		invalidOrders.removeAll(validOrders);
		OrderBookStatsVo orderBookStatsVo = new OrderBookStatsVo().setOrderBook(orderBook)
				.setTotalNoOfOrders((long) bookOrders.size())
				.setTotalNoofAccuOrders(bookOrders.stream().mapToLong(o -> o.getOrderQuantity()).sum())
				.setOrderStats(getStatsMapforOrderBook(orderBook)).setValidOrderCount((long) validOrders.size())
				.setInValidOrderCount((long) (bookOrders.size() - validOrders.size()))
				.setValidDemand(validOrders.stream().mapToLong(o -> o.getOrderQuantity()).sum()).setExecutionQty(
						validOrders.stream().mapToLong(o -> (o.getOrderDetails().getExecutionQuantity() == null ? 0l
								: o.getOrderDetails().getExecutionQuantity())).sum());

		orderBookStatsVo
				.setInvalidDemand(orderBookStatsVo.getTotalNoofAccuOrders() - orderBookStatsVo.getValidDemand());
		orderBookStatsVo.setExecutionPrice(BigDecimal
				.valueOf(orderBookStatsVo.getExecutionQty() * (CollectionUtils.isEmpty(orderBook.getExecutions()) ? 0d
						: orderBook.getExecutions().iterator().next().getPrice().longValue())));

		return orderBookStatsVo;
	}

	private Map<OrderTypesInStats, Order> getStatsMapforOrderBook(OrderBook orderBook) {
		Map<OrderTypesInStats, Order> orderStats = new HashMap<>();
		orderStats.put(OrderTypesInStats.BIGGEST_ORDER,
				orderRepository.findFirstByOrderBookOrderByOrderQuantityDesc(orderBook));
		orderStats.put(OrderTypesInStats.SMALLEST_ORDER,
				orderRepository.findFirstByOrderBookOrderByOrderQuantityAsc(orderBook));
		orderStats.put(OrderTypesInStats.EARLIEST_ORDER,
				orderRepository.findFirstByOrderBookOrderByCreatedOnAsc(orderBook));
		orderStats.put(OrderTypesInStats.LATEST_ORDER,
				orderRepository.findFirstByOrderBookOrderByCreatedOnDesc(orderBook));
		return orderStats;
	}
	@Override
	public OrderBook createDefaultOrderBook() {
		OrderBook orderBook = new OrderBook();
		orderBook.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBook.setCreatedOn(LocalDateTime.now());
		orderBook.setOrderBookStatus(OrderBookStatus.OPEN);
		return orderBook;
	}

}
