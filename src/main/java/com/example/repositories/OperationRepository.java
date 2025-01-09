package com.example.repositories;

import com.example.models.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findByUserIdAndCreatedAtBetween(Long userId, Timestamp startDate, Timestamp endDate);
}