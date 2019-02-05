package com.cswm.assignment.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import com.cswm.assignment.model.Order.OrderBuilder;
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
	public OrderBook addOrderInBook(Order order, OrderBook orderBook) {

		switch (orderBook.getOrderBookStatus()) {
		case CLOSE:
			throw new ApplicationException(ErrorMessageEnum.ADD_ORDER_ORDERBOOK_CLOSED);
		case OPEN:
			return performAddOfOrderToBook(orderBook, order);
		default:
			throw new ApplicationException(ErrorMessageEnum.ADD_ORDER_ORDERBOOK_STATUS_UNKNOWN);
		}
	}

	private synchronized OrderBook performAddOfOrderToBook(OrderBook orderBook, Order order) {
		validateOrder(order, orderBook);
		Order newOrder = setOrderBookInOrder(order, orderBook);
		Set<Order> orders = CollectionUtils.isEmpty(orderBook.getOrders()) ? new HashSet<>() : orderBook.getOrders();
		orders.add(newOrder);
		orderBook.setOrders(orders);
		return orderBook;
	}

	private Order setOrderBookInOrder(Order order, OrderBook orderBook) {
		OrderBuilder orderBuilder = new OrderBuilder(order);
		orderBuilder.setOrderBook(orderBook);
		orderBuilder.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		if (null == orderBuilder.getExecutionQuantity())
			orderBuilder.setExecutionQuantity(0l);
		orderBuilder.setCreatedOn(LocalDateTime.now());
		orderBuilder.setInstrument(orderBook.getInstrument());
		return new Order(orderBuilder);
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
	public List<Order> getValidOrders(OrderBook orderBook, Execution execution) {
		Double executionPrice = CollectionUtils.isEmpty(orderBook.getExecutions())
				? (null != execution ? execution.getPrice() : 0d)
				: orderBook.getExecutions().iterator().next().getPrice();
		List<Order> validOrders = new ArrayList<Order>();
		orderBook.getOrders().forEach(order -> {
			if ((((null == order.getOrderprice() ? 0.0d : order.getOrderprice()) >= executionPrice
					&& order.getOrderType() == OrderType.LIMIT_ORDER)
					|| order.getOrderType() == OrderType.MARKET_ORDER)) {
				validOrders.add(order);
			}
		});
		if (null != execution)
			sortOrders(validOrders);
		return validOrders;
	}

	private List<Order> sortOrders(List<Order> validOrders) {
		validOrders
				.sort((a, b) -> Long.compare((long) (null == a.getExecutionQuantity() ? 0.0 : a.getExecutionQuantity()),
						(long) (null == b.getExecutionQuantity() ? 0.0 : b.getExecutionQuantity())));
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
	public Long getTotExecQtyValidOrders(List<Order> validOrders) {
		Long totExecQtyValidOrder = 0l;
		for (Order order : validOrders) {
			totExecQtyValidOrder += (null == order.getExecutionQuantity() ? 0 : order.getExecutionQuantity());
		}
		return totExecQtyValidOrder;
		// validOrders.stream().mapToDouble(o -> o.getExecutionQuantity()).sum();
	}

	@Override
	public synchronized List<Order> addExecutionQuantityToOrders(List<Order> validOrders, Long accumltdOrders,
			Long effectiveQuanty) {
		List<Order> updatedValidOrders = new ArrayList<>();
		Long addedExecution = 0l;
		Long updatedAccforvalidOrder = updateAccforVaidOrder(accumltdOrders, validOrders);
		for (Order order : validOrders) {
			OrderBuilder orderBuilder = new OrderBuilder(order);
			Long proRataQtyForOrder = StatsUtil.getProRataExecution(order.getOrderQuantity(), effectiveQuanty,
					updatedAccforvalidOrder);
			Long updatedExecutiontQty = (null == order.getExecutionQuantity() ? 0 : order.getExecutionQuantity())
					+ proRataQtyForOrder;
			if (updatedExecutiontQty >= orderBuilder.getOrderQuantity()) {
				addedExecution = addedExecution + orderBuilder.getOrderQuantity() - order.getExecutionQuantity();
				orderBuilder.setExecutionQuantity(orderBuilder.getOrderQuantity());
			} else {
				orderBuilder.setExecutionQuantity(updatedExecutiontQty);
				addedExecution = addedExecution + proRataQtyForOrder;
			}

			updatedValidOrders.add(new Order(orderBuilder));
		}
		System.out.println("Difference  in the excuted qty :::: " + (effectiveQuanty - addedExecution));

		updatedValidOrders = completeDeltaQty(addedExecution, effectiveQuanty, updatedValidOrders);

		return updatedValidOrders;
	}

	private Long updateAccforVaidOrder(Long accumltdOrders, List<Order> validOrders) {
		for (Order order : validOrders) {
			if (order.getExecutionQuantity() >= order.getOrderQuantity())
				accumltdOrders = accumltdOrders - order.getOrderQuantity();
		}

		return accumltdOrders;
	}

	private List<Order> completeDeltaQty(Long addedExecution, Long effectiveQuanty, List<Order> updatedValidOrders) {

		List<Order> updatedList = new ArrayList<>();
		for (Order order : updatedValidOrders) {
			if (addedExecution.equals(effectiveQuanty) || ((!addedExecution.equals(effectiveQuanty))
					&& (order.getOrderQuantity().longValue() <= order.getExecutionQuantity().longValue()))) {
				updatedList.add(order);
			} else {
				OrderBuilder orderBuilder = new OrderBuilder(order);
				orderBuilder.setExecutionQuantity(orderBuilder.getExecutionQuantity() + 1);
				updatedList.add(new Order(orderBuilder));
				addedExecution = addedExecution + 1;
			}
		}

		return updatedList;
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

	private Order getOrder(Long orderId) {
		return orderRepository.findFirstByOrderId(orderId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.ORDER_NOT_FOUND));
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

	private double getTotalExecutionPrice(Order order) {
		if (null == order.getOrderBook() || order.getOrderBook().getExecutions().isEmpty()) {
			return 0;
		} else {
			Execution execution = order.getOrderBook().getExecutions().iterator().next();
			return (null == order.getExecutionQuantity() ? 0 : order.getExecutionQuantity()) * execution.getPrice();
		}
	}
}
