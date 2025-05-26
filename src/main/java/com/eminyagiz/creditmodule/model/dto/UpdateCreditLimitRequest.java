package com.eminyagiz.creditmodule.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCreditLimitRequest(
        @NotNull Long customerId,
        @Positive long additionalCreditLimit
) {
}
