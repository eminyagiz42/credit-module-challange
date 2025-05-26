package com.eminyagiz.creditmodule.model.dto;

import java.math.BigDecimal;

public record CreateLoanResponse(long loanId,
                                 BigDecimal monthlyInstallmentAmount,
                                 BigDecimal totalPayment,
                                 BigDecimal totalInterest) {
}
