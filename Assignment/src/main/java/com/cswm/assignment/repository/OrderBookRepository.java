package com.cswm.assignment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.OrderBook;

@Repository
public interface OrderBookRepository extends JpaRepository<OrderBook, Long> {


	List<OrderBook> findAllByInstrument(Instrument instrument);

}
