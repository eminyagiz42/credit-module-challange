package com.eminyagiz.creditModule.controller;

import com.eminyagiz.creditModule.model.dto.CreateLoanRequest;
import com.eminyagiz.creditModule.model.dto.LoanResponse;
import com.eminyagiz.creditModule.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@RequestBody CreateLoanRequest request) {
        try {
            LoanResponse loanResponse = loanService.saveLoan(request);
            return new ResponseEntity<>(loanResponse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found: " + e.getMessage());

        }
    }

    @GetMapping
    public ResponseEntity<LoanResponse> listLoans(
            @RequestParam Long customerId,
            @RequestParam(required = false) Integer numberOfInstallment,
            @RequestParam(required = false) Boolean isPaid) {

        return ResponseEntity.ok(
            loanService.getLoansByFilters(customerId, numberOfInstallment, isPaid)
        );
    }


}
