package com.cswm.assignment.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cswm.assignment.applicationUtils.ErrorMessageEnum;
import com.cswm.assignment.applicationUtils.OrderStatus;
import com.cswm.assignment.applicationUtils.OrderType;
import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.model.dto.OrderBuilder;
import com.cswm.assignment.modelvos.OrderStatsVo;
import com.cswm.assignment.repository.OrderRepository;
import com.cswm.assignment.service.OrderBookService;
import com.cswm.assignment.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderBookService orderBookService;

	@Override
	public List<Order> getValidOrders(OrderBook orderBook, Execution execution) {
		BigDecimal executionPrice = CollectionUtils.isEmpty(orderBook.getExecutions())
				? (null != execution ? execution.getPrice() : BigDecimal.ZERO)
				: orderBook.getExecutions().iterator().next().getPrice();
		List<Order> validOrders = new ArrayList<Order>();
		orderBook.getOrders().forEach(order -> {
			if (((executionPrice
					.compareTo(null == order.getOrderprice() ? BigDecimal.ZERO : order.getOrderprice()) == -1
					&& order.getOrderDetails().getOrderType() == OrderType.LIMIT_ORDER)
					|| order.getOrderDetails().getOrderType() == OrderType.MARKET_ORDER)) {
				validOrders.add(order);
			}
		});

		return validOrders;
	}

	private List<Order> sortOrders(List<Order> validOrders) {
		validOrders.sort((a, b) -> Long.compare(
				(long) (null == a.getOrderDetails().getExecutionQuantity() ? 0.0
						: a.getOrderDetails().getExecutionQuantity()),
				(long) (null == b.getOrderDetails().getExecutionQuantity() ? 0.0
						: b.getOrderDetails().getExecutionQuantity())));
		return validOrders;
	}

	@Override
	public synchronized List<Order> addExecutionQuantityToOrders(List<Order> validOrders, Long accumltdOrders,
			Long effectiveQuanty) {
		List<Order> updatedValidOrders = new ArrayList<>();
		Long addedExecution = 0l;
		Long updatedAccforvalidOrder = updateAccforVaidOrder(accumltdOrders, validOrders);
		for (Order order : validOrders) {
			OrderBuilder orderBuilder = new OrderBuilder(order);
			Long proRataQtyForOrder = (long) (order.getOrderQuantity() * effectiveQuanty) / updatedAccforvalidOrder;
			Long updatedExecutiontQty = (null == order.getOrderDetails().getExecutionQuantity() ? 0
					: order.getOrderDetails().getExecutionQuantity()) + proRataQtyForOrder;
			if (updatedExecutiontQty >= orderBuilder.getOrderQuantity()) {
				addedExecution = addedExecution + orderBuilder.getOrderQuantity()
						- order.getOrderDetails().getExecutionQuantity();
				orderBuilder.getOrderDetails().setExecutionQuantity(orderBuilder.getOrderQuantity());
			} else {
				orderBuilder.getOrderDetails().setExecutionQuantity(updatedExecutiontQty);
				addedExecution = addedExecution + proRataQtyForOrder;
			}

			updatedValidOrders.add(new Order(orderBuilder));
		}
		System.out.println("Difference  in the excuted qty :::: " + (effectiveQuanty - addedExecution));
		sortOrders(updatedValidOrders);
		updatedValidOrders = completeDeltaQty(addedExecution, effectiveQuanty, updatedValidOrders);

		return updatedValidOrders;
	}

	private Long updateAccforVaidOrder(Long accumltdOrders, List<Order> validOrders) {
		for (Order order : validOrders) {
			if (order.getOrderDetails().getExecutionQuantity() >= order.getOrderQuantity())
				accumltdOrders = accumltdOrders - order.getOrderQuantity();
		}

		return accumltdOrders;
	}

	private List<Order> completeDeltaQty(Long addedExecution, Long effectiveQuanty, List<Order> updatedValidOrders) {

		List<Order> updatedList = new ArrayList<>();
		for (Order order : updatedValidOrders) {
			if (addedExecution.equals(effectiveQuanty) || ((!addedExecution.equals(effectiveQuanty)) && (order
					.getOrderQuantity().longValue() <= order.getOrderDetails().getExecutionQuantity().longValue()))) {
				updatedList.add(order);
			} else {
				OrderBuilder orderBuilder = new OrderBuilder(order);
				orderBuilder.getOrderDetails()
						.setExecutionQuantity(orderBuilder.getOrderDetails().getExecutionQuantity() + 1);
				updatedList.add(new Order(orderBuilder));
				addedExecution = addedExecution + 1;
			}
		}

		return updatedList;
	}

	@Override
	public OrderStatsVo getOrderStats(Long orderId) {
		OrderStatsVo orderStatsVo = new OrderStatsVo();
		Order order = getOrder(orderId);
		orderStatsVo.setOrder(order);
		if (order.getOrderprice().compareTo(order.getOrderBook().getExecutions().iterator().next().getPrice()) == -1
				&& order.getOrderDetails().getOrderType().equals(OrderType.LIMIT_ORDER)) {
			orderStatsVo.setOrderStatus(OrderStatus.INVALID);
		} else {
			orderStatsVo.setOrderStatus(OrderStatus.VALID);
		}
		if (null == order.getOrderBook() || order.getOrderBook().getExecutions().isEmpty()) {
			orderStatsVo.setExecutionPrice(BigDecimal.ZERO);
		} else {

			Execution execution = order.getOrderBook().getExecutions().iterator().next();
			orderStatsVo.setExecutionPrice((null == order.getOrderDetails().getExecutionQuantity() ? BigDecimal.ZERO
					: BigDecimal.valueOf(order.getOrderDetails().getExecutionQuantity()))
							.multiply(execution.getPrice()));
		}
		return orderStatsVo;
	}

	private Order getOrder(Long orderId) {
		return orderRepository.findFirstByOrderId(orderId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.ORDER_NOT_FOUND));
	}

}
