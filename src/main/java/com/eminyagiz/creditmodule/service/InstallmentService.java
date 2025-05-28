package com.eminyagiz.creditmodule.service;

import com.eminyagiz.creditmodule.model.dto.LoanInstallmentResponse;

import java.util.List;

public interface InstallmentService {

    List<LoanInstallmentResponse> getInstallmentsByLoanId(Long loanId);
}
