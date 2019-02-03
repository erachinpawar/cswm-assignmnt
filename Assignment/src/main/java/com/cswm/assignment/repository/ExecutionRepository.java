package com.cswm.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cswm.assignment.model.Execution;


@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long> {

}
