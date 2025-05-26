package com.eminyagiz.creditmodule.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Installment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loanInstallmentIdGenerator")
    @SequenceGenerator(name = "loanInstallmentIdGenerator", sequenceName = "loan_installment_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    private Loan loan;

    private BigDecimal amount;

    private BigDecimal paidAmount;

    private Date dueDate;

    private Date paymentDate;

    private Boolean isPaid;
}
