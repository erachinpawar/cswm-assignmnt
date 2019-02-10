package com.cswm.assignment.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;

import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.model.OrderDetails;
import com.cswm.assignment.model.dto.OrderBookDto;
import com.cswm.assignment.model.dto.OrderDto;

public class CustomModelMapper {

	public static ModelMapper getOrderModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.createTypeMap(OrderDto.class, Order.class).setProvider(new Provider<Order>() {
			public Order get(ProvisionRequest<Order> request) {
				OrderDto s = OrderDto.class.cast(request.getSource());
				return new Order(s.getOrderId(), new ModelMapper().map(s.getInstrument(), Instrument.class),
						new ModelMapper().map(s.getOrderBook(), OrderBook.class),
						new ModelMapper().map(s.getOrderDetails(), OrderDetails.class), s.getOrderQuantity(),
						s.getOrderprice(), s.getCreatedBy(), s.getCreatedOn());
			}
		});
		return modelMapper;
	}
	
	public static ModelMapper getOrderBookModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.createTypeMap(OrderBookDto.class, OrderBook.class).setProvider(new Provider<OrderBook>() {
			public OrderBook get(ProvisionRequest<OrderBook> request) {
				OrderBookDto s = OrderBookDto.class.cast(request.getSource());
				OrderBook orderBook = new OrderBook();
				orderBook.setCreatedBy(s.getCreatedBy());
				orderBook.setCreatedOn(s.getCreatedOn());
				orderBook.setInstrument(new ModelMapper().map(s.getInstrument(), Instrument.class));
				orderBook.setOrderBookId(s.getOrderBookId());
				orderBook.setOrderBookStatus(s.getOrderBookStatus());
				return orderBook;
			}
		});
		return modelMapper;
	}

}
