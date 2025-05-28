package com.eminyagiz.creditmodule.controller;

import com.eminyagiz.creditmodule.model.dto.*;
import com.eminyagiz.creditmodule.service.InstallmentService;
import com.eminyagiz.creditmodule.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan")
public class LoanController {

    private final LoanService loanService;
    private final InstallmentService installmentService;

    @PostMapping
    @PreAuthorize("#createCreditLoanRequest.customerId() == authentication.principal.id or hasAuthority('ADMIN')")
    public CreateLoanResponse createLoan(@Valid @RequestBody CreateCreditLoanRequest createCreditLoanRequest) {
        return loanService.createLoan(createCreditLoanRequest);
    }

    @PutMapping
    @PreAuthorize("#payLoanRequest.customerId() == authentication.principal.id or hasAuthority('ADMIN')")
    public PayLoanResponse payLoan(@Valid @RequestBody PayLoanRequest payLoanRequest) {
        return loanService.payLoan(payLoanRequest);
    }

    @GetMapping()
    @PreAuthorize("#customerId == authentication.principal.id or hasAuthority('ADMIN')")
    public List<LoanResponse> getLoansByCustomerId(@RequestParam Long customerId) {
        return loanService.getLoansByCustomerId(new GetAllCustomerLoanRequest(customerId, 0, false));
    }

    @GetMapping("/{loanId}")
    @PostAuthorize("returnObject.userId() == authentication.principal.id or hasAuthority('ADMIN')")
    public LoanResponse getCustomerLoans(@PathVariable Long loanId) {
        return loanService.getLoanById(loanId);
    }

    @GetMapping("/{loanId}/installment")
    public List<LoanInstallmentResponse> getLoanInstallments(@PathVariable Long loanId) {
        return installmentService.getInstallmentsByLoanId(loanId);
    }

}
