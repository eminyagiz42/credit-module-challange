package com.eminyagiz.creditModule.service;

import com.eminyagiz.creditModule.model.dto.InstallmentResponse;
import com.eminyagiz.creditModule.model.dto.PayResponse;
import com.eminyagiz.creditModule.model.entity.Installment;
import com.eminyagiz.creditModule.model.entity.Loan;
import com.eminyagiz.creditModule.repository.InstallmentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstallmentServiceTest {

    @Mock
    private InstallmentRepository installmentRepository;

    @Mock
    private LoanService loanService;

    @InjectMocks
    private InstallmentService installmentService;

    public InstallmentServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetInstallmentsByLoanId_Success() {
        Long loanId = 1L;

        Loan loanMock = new Loan();
        loanMock.setId(loanId);

        Installment installment1 = new Installment();
        installment1.setId(1L);
        installment1.setAmount(BigDecimal.valueOf(1000));
        installment1.setDueDate(LocalDate.now().plusDays(30));
        installment1.setIsPaid(false);

        Installment installment2 = new Installment();
        installment2.setId(2L);
        installment2.setAmount(BigDecimal.valueOf(2000));
        installment2.setDueDate(LocalDate.now().plusDays(60));
        installment2.setIsPaid(false);

        when(loanService.findById(loanId)).thenReturn(loanMock);
        when(installmentRepository.findByLoanId(loanId)).thenReturn(List.of(installment1, installment2));

        List<InstallmentResponse> result = installmentService.getInstallmentsByLoanId(loanId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(installment1.getAmount(), result.get(0).getAmount());
        assertEquals(installment2.getAmount(), result.get(1).getAmount());

        verify(loanService, times(1)).findById(loanId);
        verify(installmentRepository, times(1)).findByLoanId(loanId);
    }

    @Test
    void testGetInstallmentsByLoanId_LoanNotFound() {
        Long loanId = 2L;

        when(loanService.findById(loanId)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            installmentService.getInstallmentsByLoanId(loanId);
        });

        assertEquals("loan not found", exception.getMessage());
        verify(loanService, times(1)).findById(loanId);
        verify(installmentRepository, never()).findByLoanId(anyLong());
    }

    @Test
    void testGetInstallmentsByLoanId_NoInstallmentsFound() {
        Long loanId = 3L;

        Loan loanMock = new Loan();
        loanMock.setId(loanId);

        when(loanService.findById(loanId)).thenReturn(loanMock);
        when(installmentRepository.findByLoanId(loanId)).thenReturn(List.of());

        List<InstallmentResponse> result = installmentService.getInstallmentsByLoanId(loanId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(loanService, times(1)).findById(loanId);
        verify(installmentRepository, times(1)).findByLoanId(loanId);
    }


    @Test
    void testPayInstallmentsByLoan_Success() {
        Long loanId = 1L;
        BigDecimal paymentAmount = BigDecimal.valueOf(4000);

        Loan loan = new Loan();
        loan.setId(loanId);
        loan.setLoanAmount(BigDecimal.valueOf(6000));
        loan.setIsPaid(false);

        Installment installment1 = new Installment();
        installment1.setId(1L);
        installment1.setIsPaid(false);
        installment1.setDueDate(LocalDate.now().plusMonths(0));
        installment1.setAmount(BigDecimal.valueOf(2000));

        Installment installment2 = new Installment();
        installment2.setId(2L);
        installment2.setIsPaid(false);
        installment2.setDueDate(LocalDate.now().plusMonths(1));
        installment2.setAmount(BigDecimal.valueOf(2000));

        Installment installment3 = new Installment();
        installment3.setId(3L);
        installment3.setIsPaid(false);
        installment3.setDueDate(LocalDate.now().plusMonths(2));
        installment3.setAmount(BigDecimal.valueOf(2000));

        loan.setInstallments(List.of(installment1, installment2, installment3));

        when(loanService.findById(loanId)).thenReturn(loan);
        when(installmentRepository.findByLoanId(loanId)).thenReturn(List.of(installment1, installment2, installment3));
        when(installmentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(loanService.save(any())).thenReturn(loan);

        PayResponse response = installmentService.payInstallmentsByLoan(loanId, paymentAmount);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(4000), response.getTotalAmount());
        assertEquals(BigDecimal.valueOf(4000), response.getPaidAmount());
        assertEquals(BigDecimal.valueOf(0), response.getUnpaidAmount());
        assertEquals(2, response.getPayCount());
        assertFalse(response.getPayAll());

        verify(loanService, times(1)).findById(loanId);
        verify(installmentRepository, times(2)).save(any());
    }

    @Test
    void testPayInstallmentsByLoan_LoanNotFound() {
        Long loanId = 2L;
        BigDecimal paymentAmount = BigDecimal.valueOf(1000);

        when(loanService.findById(loanId)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            installmentService.payInstallmentsByLoan(loanId, paymentAmount);
        });

        assertEquals("loan not found", exception.getMessage());

        verify(loanService, times(1)).findById(loanId);
        verify(installmentRepository, never()).save(any());
    }

    @Test
    void testPayInstallmentsByLoan_InsufficientPaymentAmount() {
        Long loanId = 3L;
        BigDecimal paymentAmount = BigDecimal.valueOf(500);

        Loan loan = new Loan();
        loan.setId(loanId);
        loan.setLoanAmount(BigDecimal.valueOf(5000));
        loan.setIsPaid(false);

        Installment installment = new Installment();
        installment.setId(1L);
        installment.setIsPaid(false);
        installment.setAmount(BigDecimal.valueOf(1000));

        loan.setInstallments(List.of(installment));

        when(loanService.findById(loanId)).thenReturn(loan);
        when(installmentRepository.findByLoanId(loanId)).thenReturn(List.of(installment));

        PayResponse response = installmentService.payInstallmentsByLoan(loanId, paymentAmount);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(0), response.getPaidAmount());
        assertEquals(BigDecimal.valueOf(500), response.getUnpaidAmount());
        assertEquals(0, response.getPayCount());
        assertFalse(response.getPayAll());

        verify(loanService, times(1)).findById(loanId);
        verify(installmentRepository, never()).save(any());
    }

}