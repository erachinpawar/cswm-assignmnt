package com.cswm.assignment.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.Order;
import com.cswm.assignment.model.OrderBook;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findAllByOrderBook(OrderBook orderBook);

	Optional<Order> findFirstByOrderBookAndOrderId(OrderBook orderBook, Long orderId);

	void deleteByOrderBookAndOrderId(OrderBook orderBook, Long orderId);

	Optional<Order> findFirstByOrderId(Long orderId);

	Collection<Order> findAllByInstrument(Instrument instrument);

	@Modifying
	@Query(value = "update Order o set o.executionQuantity=:executionQuantity")
	void updateQtyForExecution(@Param("executionQuantity") double executionQuantity);

	Order findFirstByOrderBookOrderByOrderQuantityDesc(OrderBook orderBook);

	Order findFirstByOrderBookOrderByOrderQuantityAsc(OrderBook orderBook);

	Order findFirstByOrderBookOrderByCreatedOnAsc(OrderBook orderBook);

	Order findFirstByOrderBookOrderByCreatedOnDesc(OrderBook orderBook);

	@Query(value = "select * from orders_inv where order_book_id = :orderBookId", nativeQuery = true)
	Set<Order> getByOrderBookID(@Param("orderBookId") Long orderBookId);

}
