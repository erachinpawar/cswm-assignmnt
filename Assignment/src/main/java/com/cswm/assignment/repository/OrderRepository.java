package com.cswm.assignment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	Order findFirstByOrderBookOrderByOrderQuantityDesc(OrderBook orderBook);

	Order findFirstByOrderBookOrderByOrderQuantityAsc(OrderBook orderBook);

	Order findFirstByOrderBookOrderByCreatedOnAsc(OrderBook orderBook);

	Order findFirstByOrderBookOrderByCreatedOnDesc(OrderBook orderBook);

	Optional<Order> findFirstByOrderId(Long orderId);
}
