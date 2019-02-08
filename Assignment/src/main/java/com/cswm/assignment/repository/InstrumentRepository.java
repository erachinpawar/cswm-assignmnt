package com.cswm.assignment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cswm.assignment.model.Instrument;


@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {

	Optional<Instrument> findFirstByInstrumentId(Long instrumentId);

	Instrument findFirstByInstrumentName(String instrumentName);

}
