package com.eminyagiz.creditmodule.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PayLoanRequest(@NotNull @Positive Long loanId,
                             @NotNull @Positive Long customerId,
                             @NotNull @Positive Double amount) {

}
