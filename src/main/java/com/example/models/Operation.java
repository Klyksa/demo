package com.example.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "operation_type", nullable = false)
    private Integer operationType;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
}