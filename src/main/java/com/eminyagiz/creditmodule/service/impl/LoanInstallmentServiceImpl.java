package com.eminyagiz.creditmodule.service.impl;

import com.eminyagiz.creditmodule.common.mapper.LoanInstallmentMapper;
import com.eminyagiz.creditmodule.model.dto.LoanInstallmentResponse;
import com.eminyagiz.creditmodule.repository.LoanInstallmentRepository;
import com.eminyagiz.creditmodule.model.entity.Installment;
import com.eminyagiz.creditmodule.service.LoanInstallmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoanInstallmentServiceImpl implements LoanInstallmentService {
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final LoanInstallmentMapper loanInstallmentMapper;

    @Override
    public List<LoanInstallmentResponse> getByLoanId(Long loanId) {
        List<Installment> installments = loanInstallmentRepository.findAllById(loanId);
        if (installments.isEmpty()) {
            throw new EntityNotFoundException(String.format("There is no any installments found for loanId: %s", loanId));
        }
        return installments.stream()
                .map(loanInstallmentMapper::fromInstallment)
                .toList();
    }
}
