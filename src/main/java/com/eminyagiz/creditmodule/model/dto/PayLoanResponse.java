package com.eminyagiz.creditmodule.model.dto;

import java.math.BigDecimal;

public record PayLoanResponse(int installmentsPaid,
                              BigDecimal amountPaid,
                              boolean isLoanPaid) {
}
