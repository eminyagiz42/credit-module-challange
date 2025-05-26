package com.eminyagiz.creditmodule.model.dto;

import com.eminyagiz.creditmodule.common.validation.OneOf;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateCreditLoanRequest(
        @Positive Long customerId,
        @Positive BigDecimal amount,
        @OneOf(value = {6, 9, 12, 24}) int numberOfInstallments,
        @DecimalMin(value = "0.1")
        @DecimalMax(value = "0.5")
        @Digits(integer = 3, fraction = 2) double interestRate) {
}
