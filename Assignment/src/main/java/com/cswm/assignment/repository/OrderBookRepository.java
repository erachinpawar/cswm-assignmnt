package com.cswm.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cswm.assignment.applicationutils.OrderBookStatus;
import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.OrderBook;

@Repository
public interface OrderBookRepository extends JpaRepository<OrderBook, Long> {

	OrderBook findFirstByInstrumentAndOrderBookStatusNot(Instrument instrument, OrderBookStatus executed);

	@Modifying
	@Query("update OrderBook ob set ob.orderBookStatus = :orderBookStatus where ob.orderBookId = :orderBookId")
	void updateOrderBookStatus(@Param("orderBookId") Long orderBookId,
			@Param("orderBookStatus") OrderBookStatus orderBookStatus);


}
