package com.eminyagiz.creditmodule.model.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record LoanResponse(Long loanId,
                           Long userId,
                           List<LoanInstallmentResponse> installments,
                           BigDecimal loanAmount,
                           int numberOfInstallment,
                           Date createdDate,
                           Boolean isPaid) {
}
