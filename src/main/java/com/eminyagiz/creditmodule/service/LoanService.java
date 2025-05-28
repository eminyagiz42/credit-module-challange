package com.eminyagiz.creditmodule.service;

import com.eminyagiz.creditmodule.model.dto.*;
import jakarta.validation.Valid;

import java.util.List;

public interface LoanService {
    CreateLoanResponse createLoan(CreateCreditLoanRequest createCreditLoanRequest);

    List<LoanResponse> getLoansByCustomerId(GetAllCustomerLoanRequest allCustomerLoanRequest);

    LoanResponse getLoanById(Long loanId);

    PayLoanResponse payLoan(@Valid PayLoanRequest payLoanRequest);
}
