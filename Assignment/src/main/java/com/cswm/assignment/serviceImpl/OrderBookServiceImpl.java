package com.cswm.assignment.serviceImpl;

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
import com.cswm.assignment.applicationUtils.StatsUtil;
import com.cswm.assignment.exceptions.ApplicationException;
import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.model.OrderBook.ExecutionStatus;
import com.cswm.assignment.model.OrderBook.OrderBookStatus;
import com.cswm.assignment.modelvos.OrderBookStatsVo;
import com.cswm.assignment.modelvos.OrderBookStatsVo.OrderTypesInStats;
import com.cswm.assignment.modelvos.OrderStatsVo;
import com.cswm.assignment.repository.OrderBookRepository;
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
	InstrumentService instrumentService;

	@Override
	public List<OrderBook> getOrderBooks() {
		return orderBookRepository.findAll();
	}

	@Override
	public OrderBook getOrderBook(Long orderBookId) {
		return orderBookRepository.findById(orderBookId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.ORDER_BOOK_NOT_FOUND));
	}

	@Override
	public synchronized OrderBook saveBook(OrderBook orderBook) {
		validate(orderBook);
		orderBook = orderBookRepository.save(orderBook);
		return orderBook;
	}

	private void validate(OrderBook orderBook) {
		if (!CollectionUtils.isEmpty(orderBook.getExecutions()))
			throw new ApplicationException(ErrorMessageEnum.CAN_NOT_ADD_EXECUTION_IN_CREATION);
		if (ExecutionStatus.EXECUTED == orderBook.getExecutionStatus())
			throw new ApplicationException(ErrorMessageEnum.BOOK_EXECUTED_IN_CREATION);
		if (null == orderBook.getOrderBookName() || orderBook.getOrderBookName().isEmpty())
			throw new ApplicationException(ErrorMessageEnum.BOOK_NAME_BLANK);
		if (null == orderBook.getInstrument())
			throw new ApplicationException(ErrorMessageEnum.BOOK_WITHOUT_INSTRUMENT);
		if (OrderBookStatus.CLOSE.equals(orderBook.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_CAN_NOT_BECLOSED);
	}

	@Override
	public synchronized OrderBook addOrderInBook(Long orderBookId, Order order) {
		OrderBook orderBook = getOrderBook(orderBookId);
		orderBook = orderService.addOrderInBook(order, orderBook);
		return orderBookRepository.save(orderBook);
	}

	@Override
	public synchronized OrderBook openCloseOrderBook(String orderBookStatus, Long orderBookId) {
		OrderBook orderBook = getOrderBook(orderBookId);
		if (OrderBookStatus.CLOSE.toString().equalsIgnoreCase(orderBookStatus))
			orderBook.setOrderBookStatus(OrderBookStatus.CLOSE);
		else if (OrderBookStatus.OPEN.toString().equalsIgnoreCase(orderBookStatus))
			orderBook.setOrderBookStatus(OrderBookStatus.OPEN);
		else
			throw new ApplicationException(ErrorMessageEnum.BOOK_STATUS_OPEN_CLOSE);
		return orderBookRepository.save(orderBook);
	}

	@Override
	public synchronized OrderBook addExecutionToBook(Long orderBookId, Execution execution) {
		validate(execution);
		OrderBook orderBook = getOrderBook(orderBookId);
		execution.setOrderBook(orderBook);

		if (!isOrderBookClosed(orderBook)) {
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_CAN_NOT_BE_ADDED);
		} else if (orderBookExecuted(orderBook)) {
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_EXECUTED);
		} else if (!CollectionUtils.isEmpty(orderBook.getExecutions())
				&& !execution.getPrice().equals(orderBook.getExecutions().iterator().next().getPrice())) {
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_PRICE_INVALID);
		} else if (isOrderBookClosed(orderBook) && !orderBookExecuted(orderBook)) {
			orderBook = addExecutionToClosedBook(orderBook, execution);
		}
		return orderBookRepository.save(orderBook);
	}

	private void validate(Execution execution) {
		if (null == execution.getExecutionName() || execution.getExecutionName().isEmpty())
			throw new ApplicationException(ErrorMessageEnum.INVALID_EXECUTION_NAME);
		if (null == execution.getQuantity() || execution.getQuantity() == 0l)
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_QTY_INVALID);
	}

	private boolean orderBookExecuted(OrderBook orderBook) {
		if (ExecutionStatus.EXECUTED == orderBook.getExecutionStatus())
			return true;
		return false;
	}

	private boolean isOrderBookClosed(OrderBook orderBook) {
		if (OrderBookStatus.CLOSE == orderBook.getOrderBookStatus())
			return true;
		return false;
	}

	private synchronized OrderBook addExecutionToClosedBook(OrderBook orderBook, Execution execution) {
		List<Order> validOrders = orderService.getValidOrders(orderBook, execution);
		Long accumltdOrders = orderService.getAccOrdersFromValidOrders(validOrders);
		Long accExecQty = orderService.getTotExecQtyValidOrders(validOrders);

		if (accumltdOrders <= accExecQty.doubleValue()) {
			if (null==orderBook.getExecutionStatus()||ExecutionStatus.NOT_EXECUTED.equals(orderBook.getExecutionStatus())) {
				orderBook.setExecutionStatus(ExecutionStatus.EXECUTED);
				orderBookRepository.save(orderBook);
				throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_EXECUTED);
			}
		}

		else {
			Long effectiveQuanty = StatsUtil.getEffectiveQtyForExec(accumltdOrders, accExecQty,
					execution.getQuantity());
			orderBook.getOrders().removeAll(validOrders);
			validOrders = orderService.addExecutionQuantityToOrders(validOrders, accumltdOrders, effectiveQuanty);
			orderBook.getOrders().addAll(validOrders);
			if (effectiveQuanty != execution.getQuantity()) {
				updateEffQtyAndSave(effectiveQuanty, orderBook, execution);
			}
			Set<Execution> orderBookExecutions = CollectionUtils.isEmpty(orderBook.getExecutions()) ? new HashSet<>()
					: orderBook.getExecutions();
			orderBookExecutions.add(execution);
			orderBook.setExecutions(orderBookExecutions);
		}
		return orderBook;
	}

	private synchronized void updateEffQtyAndSave(Long effectiveQuanty, OrderBook orderBook, Execution execution) {

		execution.setQuantity(effectiveQuanty);
		Set<Execution> orderBookExecutions = CollectionUtils.isEmpty(orderBook.getExecutions()) ? new HashSet<>()
				: orderBook.getExecutions();
		orderBookExecutions.add(execution);
		orderBook.setExecutions(orderBookExecutions);
		orderBookRepository.save(orderBook);
		throw new ApplicationException(ErrorMessageEnum.PARTIALLY_EXECUTED);

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
				.setTotalNoofAccuOrders(orderService.getAccOrdersFromValidOrders(bookOrders))
				.setOrderStats(getStatsMapforOrderBook(orderBook)).setValidOrderCount((long) validOrders.size())
				.setInValidOrderCount((long) (bookOrders.size() - validOrders.size()))
				.setValidDemand(orderService.getAccOrdersFromValidOrders(validOrders))
				.setInvalidDemand(orderService.getAccOrdersFromValidOrders(invalidOrders))
				.setExecutionQty(orderService.getTotExecQtyValidOrders(validOrders));
		orderBookStatsVo.setExecutionPrice(
				orderBookStatsVo.getExecutionQty() * (CollectionUtils.isEmpty(orderBook.getExecutions()) ? 0d
						: orderBook.getExecutions().iterator().next().getPrice()));
		return orderBookStatsVo;
	}

	private Map<OrderTypesInStats, Order> getStatsMapforOrderBook(OrderBook orderBook) {
		Map<OrderTypesInStats, Order> orderStats = new HashMap<>();
		orderStats.put(OrderTypesInStats.BIGGEST_ORDER, orderService.getBiggestOrderForBook(orderBook));
		orderStats.put(OrderTypesInStats.SMALLEST_ORDER, orderService.getSmallestOrderForBook(orderBook));
		orderStats.put(OrderTypesInStats.EARLIEST_ORDER, orderService.getEarliestOrderInBook(orderBook));
		orderStats.put(OrderTypesInStats.LATEST_ORDER, orderService.getLatestOrderInBook(orderBook));
		return orderStats;
	}

	@Override
	public OrderStatsVo getOrderStats(Long orderBookId, Long orderId) {
		return orderService.getOrderStats(orderId);
	}
	
	@Override
	public OrderBook updateBook(OrderBook orderBook, Long bookId) {
		orderBook.setOrderBookId(bookId);
		orderBook.setInstrument(instrumentService.getInstrument(orderBook.getInstrument().getInstrumentName()));
		return orderBookRepository.save(orderBook);
	}

	@Override
	public OrderBook createDefaultOrderBook() {
		OrderBook orderBook = new OrderBook();
		orderBook.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		orderBook.setCreatedOn(LocalDateTime.now());
		orderBook.setExecutionStatus(ExecutionStatus.NOT_EXECUTED);
		orderBook.setOrderBookStatus(OrderBookStatus.OPEN);
		return orderBook;
	}

}
