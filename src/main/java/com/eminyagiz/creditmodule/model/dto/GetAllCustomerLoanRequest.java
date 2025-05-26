package com.eminyagiz.creditmodule.model.dto;

import jakarta.validation.constraints.NotNull;

public record GetAllCustomerLoanRequest(
        @NotNull Long customerId,
        int numberOfInstallments,
        boolean isPaid) {

}
