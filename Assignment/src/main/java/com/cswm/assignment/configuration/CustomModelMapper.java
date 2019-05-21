package com.cswm.assignment.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;

import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.model.OrderDetails;
import com.cswm.assignment.model.dto.OrderBo;

public class CustomModelMapper {

	public static ModelMapper getOrderModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.createTypeMap(OrderBo.class, Order.class).setProvider(new Provider<Order>() {
			public Order get(ProvisionRequest<Order> request) {
				OrderBo s = OrderBo.class.cast(request.getSource());
				return new Order(s.getOrderId(), new ModelMapper().map(s.getInstrument(), Instrument.class),
						new ModelMapper().map(s.getOrderBook(), OrderBook.class),
						new ModelMapper().map(s.getOrderDetails(), OrderDetails.class), s.getOrderQuantity(),
						s.getOrderprice(), s.getCreatedBy(), s.getCreatedOn());
			}
		});
		return modelMapper;
	}
	
}
