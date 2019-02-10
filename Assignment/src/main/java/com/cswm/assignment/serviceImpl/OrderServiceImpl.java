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
import com.cswm.assignment.applicationutils.OrderStatus;
import com.cswm.assignment.applicationutils.OrderType;
import com.cswm.assignment.configuration.CustomModelMapper;
import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.dto.OrderDto;
import com.cswm.assignment.model.dto.OrderStatisticsDto;
import com.cswm.assignment.repository.OrderRepository;
import com.cswm.assignment.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	///
	private List<OrderDto> sortOrders(List<OrderDto> validOrders) {
		logger.info("sortOrders() Method called with argument :: (" + validOrders.toString() + ");");
		validOrders.sort((a, b) -> Long.compare(
				(long) (null == a.getOrderDetails().getExecutionQuantity() ? 0.0
						: a.getOrderDetails().getExecutionQuantity()),
				(long) (null == b.getOrderDetails().getExecutionQuantity() ? 0.0
						: b.getOrderDetails().getExecutionQuantity())));
		logger.info("sortOrders() Method returned value :: (" + validOrders.toString() + ");");
		return validOrders;
	}

	@Override
	public synchronized List<OrderDto> addExecutionQuantityToOrders(Set<OrderDto> validOrdersDtos, Long accumltdOrders,
			Long effectiveQuanty) {
		logger.info("addExecutionQuantityToOrders() Method called with argument :: (" + validOrdersDtos.toString() + ","
				+ accumltdOrders + "," + effectiveQuanty + ");");
		List<OrderDto> updatedValidOrderDtos = new ArrayList<>();
		Long addedExecution = 0l;
		Long updatedToavalidOrder = updateTotalValidOrders(accumltdOrders, validOrdersDtos);
		for (OrderDto orderDto : validOrdersDtos) {
			Long proRataQtyForOrder = (long) (orderDto.getOrderQuantity() * effectiveQuanty) / updatedToavalidOrder;
			Long updatedExecutiontQty = (null == orderDto.getOrderDetails().getExecutionQuantity() ? 0
					: orderDto.getOrderDetails().getExecutionQuantity()) + proRataQtyForOrder;
			if (updatedExecutiontQty >= orderDto.getOrderQuantity()) {
				addedExecution = addedExecution + orderDto.getOrderQuantity()
						- orderDto.getOrderDetails().getExecutionQuantity();
				orderDto.getOrderDetails().setExecutionQuantity(orderDto.getOrderQuantity());
			} else {
				orderDto.getOrderDetails().setExecutionQuantity(updatedExecutiontQty);
				addedExecution = addedExecution + proRataQtyForOrder;
			}

			updatedValidOrderDtos.add(orderDto);
		}
		sortOrders(updatedValidOrderDtos);
		updatedValidOrderDtos = completeDeltaQty(addedExecution, effectiveQuanty, updatedValidOrderDtos);
		for (OrderDto orderDto : updatedValidOrderDtos) {
			Order order = CustomModelMapper.getOrderModelMapper().map(orderDto, Order.class);
			orderRepository.save(order);
		}
		logger.info(
				"addExecutionQuantityToOrders() Method returned value :: (" + updatedValidOrderDtos.toString() + ");");
		return updatedValidOrderDtos;
	}

	private Long updateTotalValidOrders(Long accumltdOrders, Set<OrderDto> validOrdersDtos) {
		logger.info("updateTotalValidOrders() Method called with argument :: (" + accumltdOrders + "," + validOrdersDtos
				+ ");");
		for (OrderDto orderDto : validOrdersDtos) {
			if (orderDto.getOrderDetails().getExecutionQuantity() >= orderDto.getOrderQuantity())
				accumltdOrders = accumltdOrders - orderDto.getOrderQuantity();
		}
		logger.info("updateTotalValidOrders() Method returned value :: (" + accumltdOrders + ");");
		return accumltdOrders;
	}

	private List<OrderDto> completeDeltaQty(Long addedExecution, Long effectiveQuanty,
			List<OrderDto> updatedValidOrderDtos) {
		logger.info("completeDeltaQty() Method called with argument :: (" + addedExecution + "," + effectiveQuanty + ","
				+ updatedValidOrderDtos.toString() + ");");
		List<OrderDto> updatedValidOrderList = new ArrayList<>();
		for (OrderDto orderDto : updatedValidOrderDtos) {
			if (addedExecution.equals(effectiveQuanty)
					|| ((!addedExecution.equals(effectiveQuanty)) && (orderDto.getOrderQuantity()
							.longValue() <= orderDto.getOrderDetails().getExecutionQuantity().longValue()))) {
				updatedValidOrderList.add(orderDto);
			} else {
				orderDto.getOrderDetails().setExecutionQuantity(orderDto.getOrderDetails().getExecutionQuantity() + 1);
				updatedValidOrderList.add(orderDto);
				addedExecution = addedExecution + 1;
			}
		}
		logger.info("updateTotalValidOrders() Method returned value :: (" + updatedValidOrderList + ");");
		return updatedValidOrderList;
	}

	@Override
	public OrderStatisticsDto getOrderStats(Long orderId) {
		logger.info("getOrderStats() Method called with argument :: (" + orderId +");");
		OrderStatisticsDto orderStatsVo = new OrderStatisticsDto();
		Order order = orderRepository.findFirstByOrderId(orderId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.ORDER_NOT_FOUND));
		orderStatsVo.setOrder(new ModelMapper().map(order, com.cswm.assignment.model.dto.OrderDto.class));
		if (!CollectionUtils.isEmpty(order.getOrderBook().getExecutions())) {
			Execution execution = order.getOrderBook().getExecutions().iterator().next();
			orderStatsVo.setExecutionPrice((null == order.getOrderDetails().getExecutionQuantity() ? BigDecimal.ZERO
					: BigDecimal.valueOf(order.getOrderDetails().getExecutionQuantity()))
							.multiply(execution.getPrice()));
		}
		logger.info("getOrderStats() Method returned value :: (" + orderStatsVo + ");");
		return orderStatsVo;
	}

}
