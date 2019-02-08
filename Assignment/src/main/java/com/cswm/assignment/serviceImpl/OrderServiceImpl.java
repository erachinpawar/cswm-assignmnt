package com.cswm.assignment.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cswm.assignment.applicationutils.ErrorMessageEnum;
import com.cswm.assignment.applicationutils.OrderStatus;
import com.cswm.assignment.applicationutils.OrderType;
import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBuilder;
import com.cswm.assignment.model.dto.ExecutionDto;
import com.cswm.assignment.model.dto.OrderBookDto;
import com.cswm.assignment.model.dto.OrderBuilderDto;
import com.cswm.assignment.model.dto.OrderDto;
import com.cswm.assignment.model.dto.OrderStatisticsDto;
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
	public List<OrderDto> getValidOrders(OrderBookDto orderBookDto, ExecutionDto executionDto) {
		BigDecimal executionPrice = CollectionUtils.isEmpty(orderBookDto.getExecutions())
				? (null != executionDto ? executionDto.getPrice() : BigDecimal.ZERO)
				: orderBookDto.getExecutions().iterator().next().getPrice();
		List<OrderDto> validOrdersDtos = new ArrayList<OrderDto>();

		for (OrderDto orderDto : orderBookDto.getOrders()) {
			if (orderDto.getOrderDetails().getOrderType().equals(OrderType.MARKET_ORDER))
				validOrdersDtos.add(orderDto);
			else if ((executionPrice
					.compareTo(null == orderDto.getOrderprice() ? BigDecimal.ZERO : orderDto.getOrderprice()) == -1
					&& orderDto.getOrderDetails().getOrderType() == OrderType.LIMIT_ORDER))
				validOrdersDtos.add(orderDto);

		}

		return validOrdersDtos;
	}

	private List<OrderDto> sortOrders(List<OrderDto> validOrders) {
		validOrders.sort((a, b) -> Long.compare(
				(long) (null == a.getOrderDetails().getExecutionQuantity() ? 0.0
						: a.getOrderDetails().getExecutionQuantity()),
				(long) (null == b.getOrderDetails().getExecutionQuantity() ? 0.0
						: b.getOrderDetails().getExecutionQuantity())));
		return validOrders;
	}

	@Override
	public synchronized List<OrderDto> addExecutionQuantityToOrders(List<OrderDto> validOrdersDtos, Long accumltdOrders,
			Long effectiveQuanty) {
		List<OrderDto> updatedValidOrderDtos = new ArrayList<>();
		Long addedExecution = 0l;
		Long updatedToavalidOrder = updateTotalValidOrders(accumltdOrders, validOrdersDtos);
		for (OrderDto orderDto : validOrdersDtos) {
			OrderBuilderDto orderBuilderDto = new OrderBuilderDto(orderDto);
			Long proRataQtyForOrder = (long) (orderDto.getOrderQuantity() * effectiveQuanty) / updatedToavalidOrder;
			Long updatedExecutiontQty = (null == orderDto.getOrderDetails().getExecutionQuantity() ? 0
					: orderDto.getOrderDetails().getExecutionQuantity()) + proRataQtyForOrder;
			if (updatedExecutiontQty >= orderBuilderDto.getOrderQuantity()) {
				addedExecution = addedExecution + orderBuilderDto.getOrderQuantity()
						- orderDto.getOrderDetails().getExecutionQuantity();
				orderBuilderDto.getOrderDetails().setExecutionQuantity(orderBuilderDto.getOrderQuantity());
			} else {
				orderBuilderDto.getOrderDetails().setExecutionQuantity(updatedExecutiontQty);
				addedExecution = addedExecution + proRataQtyForOrder;
			}

			updatedValidOrderDtos.add(new OrderDto(orderBuilderDto));
		}
		System.out.println("Difference  in the excuted qty :::: " + (effectiveQuanty - addedExecution));
		sortOrders(updatedValidOrderDtos);
		updatedValidOrderDtos = completeDeltaQty(addedExecution, effectiveQuanty, updatedValidOrderDtos);
		for (OrderDto orderDto : updatedValidOrderDtos) {
			OrderBuilder orderBuilder = new ModelMapper().map(new OrderBuilderDto(orderDto), OrderBuilder.class);
			Order order = new Order(orderBuilder);
			orderRepository.save(order);
		}
		return updatedValidOrderDtos;
	}

	private Long updateTotalValidOrders(Long accumltdOrders, List<OrderDto> validOrdersDtos) {
		for (OrderDto orderDto : validOrdersDtos) {
			if (orderDto.getOrderDetails().getExecutionQuantity() >= orderDto.getOrderQuantity())
				accumltdOrders = accumltdOrders - orderDto.getOrderQuantity();
		}
		return accumltdOrders;
	}

	private List<OrderDto> completeDeltaQty(Long addedExecution, Long effectiveQuanty, List<OrderDto> updatedValidOrderDtos) {

		List<OrderDto> updatedValidOrderList = new ArrayList<>();
		for (OrderDto orderDto : updatedValidOrderDtos) {
			if (addedExecution.equals(effectiveQuanty) || ((!addedExecution.equals(effectiveQuanty)) && (orderDto
					.getOrderQuantity().longValue() <= orderDto.getOrderDetails().getExecutionQuantity().longValue()))) {
				updatedValidOrderList.add(orderDto);
			} else {
				OrderBuilderDto orderBuilderDto = new OrderBuilderDto(orderDto);
				orderBuilderDto.getOrderDetails()
						.setExecutionQuantity(orderBuilderDto.getOrderDetails().getExecutionQuantity() + 1);
				updatedValidOrderList.add(new OrderDto(orderBuilderDto));
				addedExecution = addedExecution + 1;
			}
		}

		return updatedValidOrderList;
	}

	@Override
	public OrderStatisticsDto getOrderStats(Long orderId) {
		OrderStatisticsDto orderStatsVo = new OrderStatisticsDto();
		Order order = orderRepository.findFirstByOrderId(orderId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.ORDER_NOT_FOUND));
		orderStatsVo.setOrder(new ModelMapper().map(order, com.cswm.assignment.model.dto.OrderDto.class));
		if (CollectionUtils.isEmpty(order.getOrderBook().getExecutions())) {
			orderStatsVo.setOrderStatus(OrderStatus.ORDER_PLACED);
			orderStatsVo.setExecutionPrice(BigDecimal.ZERO);
		} else if (order.getOrderDetails().getOrderType().equals(OrderType.MARKET_ORDER)) {
			orderStatsVo.setOrderStatus(OrderStatus.VALID);
		} else if ((null == order.getOrderprice() ? BigDecimal.ZERO : order.getOrderprice())
				.compareTo(order.getOrderBook().getExecutions().iterator().next().getPrice()) == -1
				&& order.getOrderDetails().getOrderType().equals(OrderType.LIMIT_ORDER))
			orderStatsVo.setOrderStatus(OrderStatus.INVALID);
		else {
			orderStatsVo.setOrderStatus(OrderStatus.VALID);
		}
		if (!CollectionUtils.isEmpty(order.getOrderBook().getExecutions())) {
			Execution execution = order.getOrderBook().getExecutions().iterator().next();
			orderStatsVo.setExecutionPrice((null == order.getOrderDetails().getExecutionQuantity() ? BigDecimal.ZERO
					: BigDecimal.valueOf(order.getOrderDetails().getExecutionQuantity()))
							.multiply(execution.getPrice()));
		}
		return orderStatsVo;
	}

}
