package com.cswm.assignment.serviceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cswm.assignment.ApplicationConstants;
import com.cswm.assignment.applicationUtils.ErrorMessageEnum;
import com.cswm.assignment.exceptions.ApplicationException;
import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.Order.OrderStatus;
import com.cswm.assignment.model.Order.OrderType;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.modelvos.OrderStatsVo;
import com.cswm.assignment.repository.OrderRepository;
import com.cswm.assignment.service.OrderBookService;
import com.cswm.assignment.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderBookService orderBookService;

	@Override
	public List<Order> getAllOrders() {
		return (List<Order>) orderRepository.findAll();

	}

	@Override
	public List<Order> getOrdersByOrderBook(Long orderBookId) {
		return (List<Order>) orderRepository.findAllByOrderBook(orderBookService.getOrderBook(orderBookId));

	}

	@Override
	public Order getOrderByOrderBook(Long orderBookId, Long orderId) {

		return orderRepository.findFirstByOrderBookAndOrderId(orderBookService.getOrderBook(orderBookId), orderId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.ORDER_NOT_FOUND_FOR_BOOK));
	}

	@Override
	public OrderBook addOrderInBook(Order order, OrderBook orderBook) {

		switch (orderBook.getOrderBookStatus()) {
		case CLOSED:
			throw new ApplicationException(ErrorMessageEnum.ADD_ORDER_ORDERBOOK_CLOSED);
		case OPEN:
			return performAddOfOrderToBook(orderBook, order);
		default:
			throw new ApplicationException(ErrorMessageEnum.ADD_ORDER_ORDERBOOK_STATUS_UNKNOWN);
		}
	}

	private OrderBook performAddOfOrderToBook(OrderBook orderBook, Order order) {
		validateOrder(order, orderBook);
		Order newOrder = new Order(orderBook, ApplicationConstants.DEFAULT_USER, new Date(), order);
		Set<Order> orders = CollectionUtils.isEmpty(orderBook.getOrders()) ? new HashSet<>() : orderBook.getOrders();
		orders.add(newOrder);
		return orderBook;
	}

	private void validateOrder(Order order, OrderBook orderBook) {

		if (null == order.getOrderName() || order.getOrderName().isEmpty())
			throw new ApplicationException(ErrorMessageEnum.ORDER_NAME_INVALID);
		if (!orderBook.getInstrument().getInstrumentId().equals(order.getInstrument().getInstrumentId()))
			throw new ApplicationException(ErrorMessageEnum.ORDER_NOT_BELONG_TO_INSTRUMENT);
		if (null == order.getOrderQuantity() || order.getOrderQuantity() <= 0l)
			throw new ApplicationException(ErrorMessageEnum.ORDER_QUANTITY_INVALID);
		if (null == order.getOrderprice() || order.getOrderprice() <= 0d)
			throw new ApplicationException(ErrorMessageEnum.ORDER_PRICE_INVALID);
		if (null == order.getOrderType())
			throw new ApplicationException(ErrorMessageEnum.ORDER_TYPE_INVALID);

	}

	@Override
	public void removeOrderFromBook(Long orderBookId, Long orderId) {
		throw new ApplicationException(ErrorMessageEnum.REMOVE_ORDER_ORDERBOOK);
	}

	@Override
	public Order getOrder(Long orderId) {
		return orderRepository.findFirstByOrderId(orderId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.ORDER_NOT_FOUND));
	}

	@Override
	public Order deleteOrder(Long orderId) {
		Order order = getOrder(orderId);
		if (order != null) {
			if (null != order.getOrderBook())
				throw new ApplicationException(ErrorMessageEnum.REMOVE_ORDER_ORDERBOOK_CLOSED);
			orderRepository.delete(order);
		}
		return order;
	}

	@Override
	public Collection<Order> getOrdersByInstruments(Instrument instrument) {
		return orderRepository.findAllByInstrument(instrument);
	}

	@Override
	public List<Order> getValidOrders(OrderBook orderBook) {
		List<Order> allOrdersForBook = orderRepository.findAllByOrderBook(orderBook);
		Double executionPrice = orderBook.getExecutions().iterator().next().getPrice();
		// if first execution execution price from them
		List<Order> validOrders = new ArrayList<Order>();
		allOrdersForBook.forEach(order -> {
			if (((null == order.getOrderprice() ? 0.0d : order.getOrderprice()) < executionPrice
					&& order.getOrderType() == OrderType.LIMIT_ORDER)
					|| order.getOrderType() == OrderType.MARKET_ORDER) {
				validOrders.add(order);
			}
		});

		return validOrders;
	}

	@Override
	public Long getAccOrdersFromValidOrders(List<Order> validOrders) {
		Long accOrderQuant = 0l;
		for (Order order : validOrders) {
			accOrderQuant = accOrderQuant + order.getOrderQuantity();
		}

		return accOrderQuant;
		// validOrders.stream().mapToInt(o -> o.getOrderQuantity()).sum();
	}

	@Override
	public double getTotExecQtyValidOrders(List<Order> validOrders) {

		double totExecQtyValidOrder = 0;
		for (Order order : validOrders) {
			totExecQtyValidOrder += (null == order.getExecutionQuantity() ? 0 : order.getExecutionQuantity());
		}

		return totExecQtyValidOrder;
		// validOrders.stream().mapToDouble(o -> o.getExecutionQuantity()).sum();
	}

	@Override
	public List<Order> addExecutionQuantityToOrders(List<Order> validOrders, Double perOrderExec) {

		List<Order> updatedValidOrders = new ArrayList<>();
		for (Order order : validOrders) {
			double updatedExecutiontQty = (null == order.getExecutionQuantity() ? 0 : order.getExecutionQuantity())
					+ (order.getOrderQuantity() * perOrderExec);
			updatedValidOrders.add(new Order(order, updatedExecutiontQty));
		}
		return updatedValidOrders;
	}

	@Override
	public Order getBiggestOrderForBook(OrderBook orderBook) {

		return orderRepository.findFirstByOrderBookOrderByOrderQuantityDesc(orderBook);
	}

	@Override
	public Order getSmallestOrderForBook(OrderBook orderBook) {
		return orderRepository.findFirstByOrderBookOrderByOrderQuantityAsc(orderBook);
	}

	@Override
	public Order getEarliestOrderInBook(OrderBook orderBook) {
		return orderRepository.findFirstByOrderBookOrderByCreatedOnAsc(orderBook);
	}

	@Override
	public Order getLatestOrderInBook(OrderBook orderBook) {
		return orderRepository.findFirstByOrderBookOrderByCreatedOnDesc(orderBook);
	}

	@Override
	public OrderStatsVo getOrderStats(Long orderId) {
		OrderStatsVo orderStatsVo = new OrderStatsVo();
		orderStatsVo.setOrder(getOrder(orderId));
		orderStatsVo.setOrderStatus(getOrderStatus(orderStatsVo.getOrder()));
		orderStatsVo.setExecutionPrice(getTotalExecutionPrice(orderStatsVo.getOrder()));
		return orderStatsVo;
	}

	private double getTotalExecutionPrice(Order order) {
		if (null == order.getOrderBook() || order.getOrderBook().getExecutions().isEmpty()) {
			return 0;
		} else {
			Execution execution = order.getOrderBook().getExecutions().iterator().next();
			return (null == order.getExecutionQuantity() ? 0 : order.getExecutionQuantity()) * execution.getPrice();
		}
	}

	private OrderStatus getOrderStatus(Order order) {
		if (null == order.getOrderBook() || order.getOrderBook().getExecutions().isEmpty()) {
			return OrderStatus.NO_EXECUTION_ON_BOOK_YET;
		} else if (OrderType.MARKET_ORDER == order.getOrderType()) {
			return OrderStatus.VALID;
		} else {
			Execution execution = order.getOrderBook().getExecutions().iterator().next();
			if (order.getOrderprice() < execution.getPrice()) {
				return OrderStatus.INVALID;
			}
		}
		return OrderStatus.VALID;
	}

	@Override
	public Set<Order> getOrdersFromDBforBook(Long orderBookId) {

		return orderRepository.getByOrderBookID(orderBookId);
	}
}
