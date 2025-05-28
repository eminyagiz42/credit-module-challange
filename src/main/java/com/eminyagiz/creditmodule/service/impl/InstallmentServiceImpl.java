package com.eminyagiz.creditmodule.service.impl;

import com.eminyagiz.creditmodule.common.mapper.LoanInstallmentMapper;
import com.eminyagiz.creditmodule.model.dto.LoanInstallmentResponse;
import com.eminyagiz.creditmodule.repository.InstallmentRepository;
import com.eminyagiz.creditmodule.model.entity.Installment;
import com.eminyagiz.creditmodule.service.InstallmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstallmentServiceImpl implements InstallmentService {
    private final InstallmentRepository installmentRepository;
    private final LoanInstallmentMapper loanInstallmentMapper;

    @Override
    public List<LoanInstallmentResponse> getInstallmentsByLoanId(Long loanId) {
        List<Installment> installments = installmentRepository.findAllById(loanId);
        if (installments.isEmpty()) {
            throw new EntityNotFoundException(String.format("There is no any installments found for loanId: %s", loanId));
        }
        return installments.stream()
                .map(loanInstallmentMapper::fromInstallment)
                .toList();
    }
}
