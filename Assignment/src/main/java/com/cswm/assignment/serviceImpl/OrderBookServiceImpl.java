package com.cswm.assignment.serviceImpl;

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

import com.cswm.assignment.applicationUtils.ErrorMessageEnum;
import com.cswm.assignment.applicationUtils.StatsUtil;
import com.cswm.assignment.exceptions.ApplicationException;
import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.model.OrderBook.ExecutionStatus;
import com.cswm.assignment.model.OrderBook.OrderBookStatus;
import com.cswm.assignment.modelvos.OrderBookStatsVo;
import com.cswm.assignment.repository.OrderBookRepository;
import com.cswm.assignment.service.OrderBookService;
import com.cswm.assignment.service.OrderService;

@Service
@Transactional
public class OrderBookServiceImpl implements OrderBookService {

	@Autowired
	OrderBookRepository orderBookRepository;

	@Autowired
	OrderService orderService;

	public final String CLOSED_STATUS = "close";

	public enum OrderTypesInStats {
		BIGGEST_ORDER, SMALLEST_ORDER, EARLIEST_ORDER, LATEST_ORDER
	}

	@Override
	public List<OrderBook> getOrderBooks() {
		return (List<OrderBook>) orderBookRepository.findAll();
	}

	@Override
	public OrderBook getOrderBook(Long bookId) {
		return orderBookRepository.findById(bookId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.ORDER_BOOK_NOT_FOUND));
	}

	@Override
	public OrderBook saveBook(OrderBook orderBook) {

		validate(orderBook);
		if (!CollectionUtils.isEmpty(orderBook.getOrders())) {
			orderBook.getOrders().forEach(order -> orderService.deleteOrder(order.getOrderId()));
		}
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
		if (OrderBookStatus.CLOSED.equals(orderBook.getOrderBookStatus()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_CAN_NOT_BECLOSED);
	}

	@Override
	public OrderBook updateBook(OrderBook orderBook, Long bookId) {
		orderBook.setOrderBookId(bookId);
		return orderBookRepository.save(orderBook);
	}

	@Override
	public List<OrderBook> getOrderBooksByInstruments(Instrument instrument) {
		return orderBookRepository.findAllByInstrument(instrument);
	}

	@Override
	public OrderBook openCloseOrderBook(String orderBookStatus, Long orderBookId) {

		OrderBook orderBook = getOrderBook(orderBookId);
		if (CLOSED_STATUS.equalsIgnoreCase(orderBookStatus))
			orderBook.setOrderBookStatus(OrderBookStatus.CLOSED);
		else
			orderBook.setOrderBookStatus(OrderBookStatus.OPEN);
		return orderBookRepository.save(orderBook);
	}

	@Override
	public OrderBook addExecutionToBook(Long orderBookId, Execution execution) {
		OrderBook orderBook = getOrderBook(orderBookId);
		execution.setOrderBook(orderBook);
		if (isOrderBookClosed(orderBook) && !orderBookExecuted(orderBook)) {
			orderBook = addExecutionToClosedBook(orderBook, execution);
		} else if (!isOrderBookClosed(orderBook)) {
			throw new ApplicationException(ErrorMessageEnum.EXECUTION_CAN_NOT_BE_ADDED);
		} else if (orderBookExecuted(orderBook)) {
			throw new ApplicationException(ErrorMessageEnum.ORDER_BOOK_EXECUTED);
		}
		return orderBookRepository.save(orderBook);
	}

	private boolean orderBookExecuted(OrderBook orderBook) {
		if (ExecutionStatus.EXECUTED == orderBook.getExecutionStatus())
			return true;
		return false;
	}

	private boolean isOrderBookClosed(OrderBook orderBook) {
		if (OrderBookStatus.CLOSED == orderBook.getOrderBookStatus())
			return true;
		return false;
	}

	private OrderBook addExecutionToClosedBook(OrderBook orderBook, Execution execution) {
// Need to add execution here for valid order calculator
		List<Order> validOrders = orderService.getValidOrders(orderBook);
		Long accumltdOrders = orderService.getAccOrdersFromValidOrders(validOrders);
		Double accExecQty = orderService.getTotExecQtyValidOrders(validOrders);

		if (accumltdOrders <= accExecQty.doubleValue()) {
			orderBook.setExecutionStatus(ExecutionStatus.EXECUTED);
			updateBook(orderBook, orderBook.getOrderBookId());
		}

		else {
			Long effectiveQuanty = StatsUtil.getEffectiveQtyForExec(accumltdOrders, accExecQty,
					execution.getQuantity());
			Double perOrderExec = StatsUtil.getlinearExecutionPerOrder(accumltdOrders, effectiveQuanty);
			orderBook.getOrders().removeAll(validOrders);
			validOrders = orderService.addExecutionQuantityToOrders(validOrders, perOrderExec);
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


	private void updateEffQtyAndSave(Long effectiveQuanty, OrderBook orderBook, Execution execution) {

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
		List<Order> validOrders = orderService.getValidOrders(orderBook);
		List<Order> bookOrders = orderService.getOrdersByOrderBook(orderBookId);
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
				orderBookStatsVo.getExecutionQty() * orderBook.getExecutions().iterator().next().getPrice());
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
	public OrderBook addOrderInBook(Order order, Long bookId) {
		OrderBook orderBook = getOrderBook(bookId);
		orderBook = orderService.addOrderInBook(order, orderBook);
		return orderBookRepository.save(orderBook);
	}

}
