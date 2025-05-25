package com.eminyagiz.creditModule.controller;

import com.eminyagiz.creditModule.model.dto.InstallmentResponse;
import com.eminyagiz.creditModule.model.dto.PayResponse;
import com.eminyagiz.creditModule.service.InstallmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/installments")
@AllArgsConstructor
public class InstallmentController {

    private final InstallmentService installmentService;


    @GetMapping("/loan/{loanId}")
    public ResponseEntity<List<InstallmentResponse>> getInstallmentsByLoanId(@PathVariable Long loanId) {
        try {
            return ResponseEntity.ok(installmentService.getInstallmentsByLoanId(loanId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: " + e.getMessage());
        }
    }

    @PostMapping("/loan/{loanId}")
    public ResponseEntity<PayResponse> payInstallments(
            @PathVariable Long loanId,
            @RequestParam BigDecimal amount) {
        try {
            PayResponse response = installmentService.payInstallmentsByLoan(loanId, amount);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: " + e.getMessage());
        }
    }

}