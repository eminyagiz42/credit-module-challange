package com.eminyagiz.creditModule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateLoanRequest {

    private Long customerId;
    private BigDecimal loanAmount;
    private Integer numberOfInstallment;
    private BigDecimal interestRate;
    private LocalDate createDate;
    private Boolean isPaid;

}