package com.eminyagiz.creditModule.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    private Integer installmentNumber;
    private BigDecimal amount;
    private BigDecimal paidAmount;

    private LocalDate dueDate;
    private LocalDate paymentDate;

    private Boolean isPaid;
}