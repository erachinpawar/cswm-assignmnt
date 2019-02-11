package com.cswm.assignment.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cswm.assignment.applicationutils.ErrorMessageEnum;
import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.dto.OrderStatisticsDto;
import com.cswm.assignment.repository.OrderRepository;
import com.cswm.assignment.service.OrderService;

/**
 * @author sachinpawar
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	
	/**
	 * Sorts the list of the valid orders based on the order quantity. 
	 * This is used for the distribution of the remaining partial executions to the lowest quantity order first.
	 * @param updatedValidOrders
	 * @return Sorted list of valid orders 
	 */
	private List<Order> sortOrders(List<Order> updatedValidOrders) {
		logger.info("sortOrders() Method called with argument :: (" + updatedValidOrders.toString() + ");");
		updatedValidOrders.sort((a, b) -> Long.compare((long) (null == a.getOrderDetails().getExecutionQuantity() ? 0.0 : a.getOrderDetails().getExecutionQuantity()),
				(long) (null == b.getOrderDetails().getExecutionQuantity() ? 0.0 : b.getOrderDetails().getExecutionQuantity())));
		logger.info("sortOrders() Method returned value :: (" + updatedValidOrders.toString() + ");");
		return updatedValidOrders;
	}

	
	
	/* (non-Javadoc)
	 * @see com.cswm.assignment.service.OrderService#addExecutionQuantityToOrders(java.util.Set, java.lang.Long, java.lang.Long)
	 */
	@Override
	public synchronized List<Order> addExecutionQuantityToOrders(Set<Order> validOrders, Long accumltdOrders, Long effectiveQuanty) {
		logger.info("addExecutionQuantityToOrders() Method called with argument :: (" + validOrders.toString() + "," + accumltdOrders + "," + effectiveQuanty + ");");
		List<Order> updatedValidOrders = new ArrayList<>();
		Long addedExecution = 0l;
		Long updatedToavalidOrder = updateTotalValidOrders(accumltdOrders, validOrders);
		for (Order order : validOrders) {
			Long proRataQtyForOrder = (long) (order.getOrderQuantity() * effectiveQuanty) / updatedToavalidOrder;
			Long updatedExecutiontQty = (null == order.getOrderDetails().getExecutionQuantity() ? 0 : order.getOrderDetails().getExecutionQuantity()) + proRataQtyForOrder;
			if (updatedExecutiontQty >= order.getOrderQuantity()) {
				addedExecution = addedExecution + order.getOrderQuantity() - order.getOrderDetails().getExecutionQuantity();
				order.getOrderDetails().setExecutionQuantity(order.getOrderQuantity());
			} else {
				order.getOrderDetails().setExecutionQuantity(updatedExecutiontQty);
				addedExecution = addedExecution + proRataQtyForOrder;
			}

			updatedValidOrders.add(order);
		}
		sortOrders(updatedValidOrders);
		updatedValidOrders = completeDeltaQty(addedExecution, effectiveQuanty, updatedValidOrders);
		for (Order order : updatedValidOrders) {
			orderRepository.save(order);
		}
		logger.info("addExecutionQuantityToOrders() Method returned value :: (" + updatedValidOrders.toString() + ");");
		return updatedValidOrders;
	}

	/**
	 * Method to sum up all the valid order quantity for executing execution
	 * @param accumltdOrders
	 * @param validOrders
	 * @return accumulated valid order quantity.
	 */
	private Long updateTotalValidOrders(Long accumltdOrders, Set<Order> validOrders) {
		logger.info("updateTotalValidOrders() Method called with argument :: (" + accumltdOrders + "," + validOrders + ");");
		for (Order order : validOrders) {
			if (order.getOrderDetails().getExecutionQuantity() >= order.getOrderQuantity())
				accumltdOrders = accumltdOrders - order.getOrderQuantity();
		}
		logger.info("updateTotalValidOrders() Method returned value :: (" + accumltdOrders + ");");
		return accumltdOrders;
	}

	/**
	 * in case if there is still remaining execution quantity after equal distribution this method is used to distribute reamining executions
	 * @param addedExecution
	 * @param effectiveQuanty
	 * @param updatedValidOrders
	 * @return List of the orders with updated execution quantity.
	 */
	private List<Order> completeDeltaQty(Long addedExecution, Long effectiveQuanty, List<Order> updatedValidOrders) {
		logger.info("completeDeltaQty() Method called with argument :: (" + addedExecution + "," + effectiveQuanty + "," + updatedValidOrders.toString() + ");");
		List<Order> updatedValidOrderList = new ArrayList<>();
		for (Order order : updatedValidOrders) {
			if (addedExecution.equals(effectiveQuanty)
					|| ((!addedExecution.equals(effectiveQuanty)) && (order.getOrderQuantity().longValue() <= order.getOrderDetails().getExecutionQuantity().longValue()))) {
				updatedValidOrderList.add(order);
			} else {
				order.getOrderDetails().setExecutionQuantity(order.getOrderDetails().getExecutionQuantity() + 1);
				updatedValidOrderList.add(order);
				addedExecution = addedExecution + 1;
			}
		}
		logger.info("updateTotalValidOrders() Method returned value :: (" + updatedValidOrderList + ");");
		return updatedValidOrderList;
	}

	/* (non-Javadoc)
	 * @see com.cswm.assignment.service.OrderService#getOrderStats(java.lang.Long)
	 */
	@Override
	public synchronized OrderStatisticsDto getOrderStats(Long orderId) {
		logger.info("getOrderStats() Method called with argument :: (" + orderId + ");");
		OrderStatisticsDto orderStatsVo = new OrderStatisticsDto();
		Order order = orderRepository.findFirstByOrderId(orderId).orElseThrow(() -> new NotFoundException(ErrorMessageEnum.ORDER_NOT_FOUND));
		orderStatsVo.setOrder(new ModelMapper().map(order, com.cswm.assignment.model.dto.OrderDto.class));
		if (!CollectionUtils.isEmpty(order.getOrderBook().getExecutions())) {
			Execution execution = order.getOrderBook().getExecutions().iterator().next();
			orderStatsVo.setExecutionPrice(
					(null == order.getOrderDetails().getExecutionQuantity() ? BigDecimal.ZERO : BigDecimal.valueOf(order.getOrderDetails().getExecutionQuantity())).multiply(execution.getPrice()));
		}
		logger.info("getOrderStats() Method returned value :: (" + orderStatsVo + ");");
		return orderStatsVo;
	}

}
