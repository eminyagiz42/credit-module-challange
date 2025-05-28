package com.eminyagiz.creditmodule.service.impl;

import com.eminyagiz.creditmodule.model.dto.*;
import com.eminyagiz.creditmodule.model.entity.Installment;
import com.eminyagiz.creditmodule.service.LoanService;
import com.eminyagiz.creditmodule.common.exception.CustomerNotEnoughLimitForLoanException;
import com.eminyagiz.creditmodule.common.mapper.LoanMapper;
import com.eminyagiz.creditmodule.repository.InstallmentRepository;
import com.eminyagiz.creditmodule.repository.LoanRepository;
import com.eminyagiz.creditmodule.model.entity.Loan;
import com.eminyagiz.creditmodule.model.entity.User;
import com.eminyagiz.creditmodule.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final UserService userService;
    private final LoanRepository loanRepository;
    private final InstallmentRepository installmentRepository;
    private final LoanMapper loanMapper;

    @Override
    @Transactional
    public CreateLoanResponse createLoan(CreateCreditLoanRequest createCreditLoanRequest) {
        User user = getUserToCreateLoan(createCreditLoanRequest);

        BigDecimal totalLoanAmount = calculateTotalLoanAmount(createCreditLoanRequest);

        Loan loan = Loan.builder()
                .user(user)
                .loanAmount(totalLoanAmount)
                .isPaid(false)
                .build();
        List<Installment> installments = createInstallments(loan, totalLoanAmount, createCreditLoanRequest);
        loan.setInstallments(installments);
        loan.setNumberOfInstallment(installments.size());
        Loan savedLoan = saveLoan(loan);

        userService.addUsedCreditLimit(user, totalLoanAmount);

        BigDecimal monthlyInstallmentAmount = installments.stream().findAny().map(Installment::getAmount).orElse(BigDecimal.ZERO);

        return new CreateLoanResponse(savedLoan.getId(), monthlyInstallmentAmount, totalLoanAmount, totalLoanAmount.subtract(createCreditLoanRequest.amount()));
    }

    private User getUserToCreateLoan(CreateCreditLoanRequest createCreditLoanRequest) {
        User user = userService.getUserById(createCreditLoanRequest.customerId());
        if (!user.hasEnoughLimit(createCreditLoanRequest.amount())) {
            throw new CustomerNotEnoughLimitForLoanException("Cannot create loan since user does not has enough limit");
        }
        return user;
    }

    private static BigDecimal calculateMonthlyPaymentAmount(CreateCreditLoanRequest createCreditLoanRequest, BigDecimal totalLoanAmount) {
        return totalLoanAmount.divide(BigDecimal.valueOf(createCreditLoanRequest.numberOfInstallments()), 2, RoundingMode.FLOOR);
    }

    private List<Installment> createInstallments(Loan loan, BigDecimal totalLoanAmount, CreateCreditLoanRequest createCreditLoanRequest) {
        List<Installment> installments = new ArrayList<>();
        BigDecimal installmentAmountPerMonth = calculateMonthlyPaymentAmount(createCreditLoanRequest, totalLoanAmount);
        for (int installmentIndex = 0; installmentIndex < createCreditLoanRequest.numberOfInstallments(); installmentIndex++) {
            Installment installment = Installment.builder()
                    .loan(loan)
                    .amount(installmentAmountPerMonth)
                    .paidAmount(BigDecimal.ZERO)
                    .isPaid(false)
                    .dueDate(calculateInstallmentDueDate(installmentIndex + 1))
                    .build();
            installments.add(installment);
        }
        return installments;
    }

    private Date calculateInstallmentDueDate(int installmentIndex) {
        Instant instant = YearMonth.now(ZoneOffset.UTC)
                .plusMonths(installmentIndex)
                .atDay(1)
                .atTime(17, 0, 0)
                .toInstant(ZoneOffset.UTC);
        return Date.from(instant);
    }

    @Override
    public List<LoanResponse> getLoansByCustomerId(GetAllCustomerLoanRequest allCustomerLoanRequest) {
        Long customerId = allCustomerLoanRequest.customerId();
        return loanRepository.getLoansByUserId(customerId).stream().map(loanMapper::toLoanResponse).toList();
    }

    @Override
    public LoanResponse getLoanById(Long loanId) {
        Loan loan = getById(loanId);
        return Optional.of(loan)
                .map(loanMapper::toLoanResponse)
                .orElseThrow(EntityNotFoundException::new);
    }

    private Loan getById(Long loanId) {
        return loanRepository.findById(loanId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public PayLoanResponse payLoan(PayLoanRequest payLoanRequest) {
        User user = userService.getUserById(payLoanRequest.customerId());
        Loan loan = getById(payLoanRequest.loanId());

        validateLoanPayment(loan, user);

        BigDecimal monthlyInstallmentAmount = loan.getInstallments().stream().findAny().map(Installment::getAmount).orElse(BigDecimal.ZERO);
        int validateLoanFactor = validateLoanAmount(payLoanRequest, monthlyInstallmentAmount);

        PayInstallmentDTO payInstallmentDTO = payInstallments(loan, validateLoanFactor);

        if (isAllLoanInstallmentsArePaid(payInstallmentDTO.installments())) {
            loan.setIsPaid(Boolean.TRUE);
            saveLoan(loan);
        }
        return new PayLoanResponse(validateLoanFactor, payInstallmentDTO.paidAmount, loan.getIsPaid());
    }

    private Loan saveLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    private PayInstallmentDTO payInstallments(Loan loan, int validateLoanFactor) {
        Date currentDate = Date.from(Instant.now());
        BigDecimal paidAmount = BigDecimal.ZERO;

        List<Installment> installments = loan.getInstallments().stream()
                .filter(loanInstallment -> !loanInstallment.getIsPaid()).toList();
        for (int installmentIndex = 0; installmentIndex < validateLoanFactor; installmentIndex++) {
            if (installmentIndex < installments.size()) {
                Installment installment = installments.get(installmentIndex);
                installment.setIsPaid(Boolean.TRUE);
                installment.setPaymentDate(currentDate);

                if (DateUtils.isSameDay(installment.getDueDate(), currentDate)) {
                    paidAmount = paidAmount.add(installment.getAmount());
                    installment.setPaidAmount(installment.getAmount());
                } else if (currentDate.before(installment.getDueDate())) {
                    BigDecimal rewardedLoan = installment.getAmount().subtract(installment.getAmount().divide(BigDecimal.valueOf(1000), 2, RoundingMode.FLOOR));
                    paidAmount = paidAmount.add(rewardedLoan);
                    installment.setPaidAmount(rewardedLoan);
                } else {
                    BigDecimal rewardedLoan = installment.getAmount().add(installment.getAmount().divide(BigDecimal.valueOf(1000), 2, RoundingMode.FLOOR));
                    paidAmount = paidAmount.add(rewardedLoan);
                    installment.setPaidAmount(installment.getAmount());
                }
                installmentRepository.save(installment);
            }
        }
        return new PayInstallmentDTO(installments, paidAmount);
    }

    private void validateLoanPayment(Loan loan, User user) {
        if (Boolean.TRUE.equals(loan.getIsPaid())) {
            throw new IllegalStateException(String.format("The Loan is already paid. Loan Id: %d", loan.getId()));
        }
        if (!loan.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException(String.format("Cannot pay loan. Loan owner id: %s is not matched with requested user id: %s", loan.getUser().getId(), user.getId()));
        }
    }

    private boolean isAllLoanInstallmentsArePaid(List<Installment> installments) {
        return installments.stream().filter(loanInstallment -> !loanInstallment.getIsPaid()).findAny().isEmpty();
    }

    private int validateLoanAmount(PayLoanRequest payLoanRequest, BigDecimal monthlyInstallmentAmount) {
        BigDecimal amountBigDecimal = BigDecimal.valueOf(payLoanRequest.amount());
        int amountFactor = amountBigDecimal.divide(monthlyInstallmentAmount, 2, RoundingMode.FLOOR).intValue();
        if (amountFactor <= 0 || amountFactor > 3) {
            throw new IllegalArgumentException("Loan pay amount is less than a single installment amount or more than 3 times of a single installment amount");
        }
        return amountFactor;
    }

    private BigDecimal calculateTotalLoanAmount(CreateCreditLoanRequest createCreditLoanRequest) {
        return createCreditLoanRequest.amount().multiply(BigDecimal.valueOf(1 + createCreditLoanRequest.interestRate()));
    }

    private record PayInstallmentDTO(List<Installment> installments, BigDecimal paidAmount) {

    }
}
