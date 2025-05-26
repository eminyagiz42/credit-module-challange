package com.eminyagiz.creditmodule.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loanIdGenerator")
    @SequenceGenerator(name = "loanIdGenerator", sequenceName = "loan_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    @Cascade(CascadeType.ALL)
    private List<Installment> installments;

    private BigDecimal loanAmount;

    private int numberOfInstallment;

    private Boolean isPaid;
}
