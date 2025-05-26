package com.eminyagiz.creditmodule.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class User extends BaseEntity implements LoanAppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userIdGenerator")
    @SequenceGenerator(name = "userIdGenerator", sequenceName = "user_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "password")
    private String password;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "credit_limit")
    private BigDecimal creditLimit;
    @Column(name = "used_credit_limit")
    private BigDecimal usedCreditLimit;

    @OneToMany(mappedBy = "user")
    private List<Loan> loans;

    @Column(name = "role_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role roleName;

    public boolean hasEnoughLimit(BigDecimal newCreditAmount) {
        return usedCreditLimit.add(newCreditAmount).compareTo(creditLimit) <= 0;
    }

    public void addUserUsedCreditLimit(BigDecimal totalLoanAmount) {
        BigDecimal newUsedLimit = getUsedCreditLimit().add(totalLoanAmount);
        setUsedCreditLimit(newUsedLimit);
    }

    public void addUserCreditLimit(BigDecimal additionalCreditLimit) {
        BigDecimal newLimit = getCreditLimit().add(additionalCreditLimit);
        setCreditLimit(newLimit);
    }

    @Override
    public long getUserId() {
        return id;
    }

    @Override
    public String getUserName() {
        return username;
    }
}
