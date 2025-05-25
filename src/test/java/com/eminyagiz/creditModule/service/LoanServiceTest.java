package com.eminyagiz.creditModule.service;

import com.eminyagiz.creditModule.model.dto.CreateLoanRequest;
import com.eminyagiz.creditModule.model.dto.LoanResponse;
import com.eminyagiz.creditModule.model.entity.Customer;
import com.eminyagiz.creditModule.model.entity.Loan;
import com.eminyagiz.creditModule.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {



    @Mock
    private LoanRepository loanRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testFindById_WhenLoanExists() {
        Long loanId = 1L;
        Loan mockLoan = Loan.builder().id(loanId).loanAmount(new BigDecimal(10000.0)).isPaid(false).interestRate(new BigDecimal(0.01)).build();
        when(loanRepository.findById(loanId)).thenReturn(Optional.of(mockLoan));

        Loan result = loanService.findById(loanId);

        assertNotNull(result);
        assertEquals(loanId, result.getId());
        verify(loanRepository, times(1)).findById(loanId);
    }

    @Test
    void testFindById_WhenLoanDoesNotExist() {
        // Arrange
        Long loanId = 1L;
        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        // Act
        Loan result = loanService.findById(loanId);

        // Assert
        assertNull(result);
        verify(loanRepository, times(1)).findById(loanId);
    }

    @Test
    void testSave_WhenLoanIsValid() {
        Loan loan = Loan.builder().loanAmount(new BigDecimal(5000)).isPaid(false).interestRate(new BigDecimal("0.05")).build();
        when(loanRepository.save(loan)).thenReturn(loan);

        Loan savedLoan = loanService.save(loan);

        assertNotNull(savedLoan);
        assertEquals(loan.getLoanAmount(), savedLoan.getLoanAmount());
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void testSave_WhenLoanIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            loanService.save(null);
        });

        assertEquals("Loan is null", exception.getMessage());
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void testSaveLoan_WhenValidRequest() {
        // Arrange
        CreateLoanRequest createLoanRequest = new CreateLoanRequest();
        createLoanRequest.setCustomerId(1L);
        createLoanRequest.setLoanAmount(new BigDecimal("10000"));
        createLoanRequest.setInterestRate(new BigDecimal("0.1"));
        createLoanRequest.setNumberOfInstallment(12);

        Customer mockCustomer = Customer.builder().id(1L).name("John").creditLimit(new BigDecimal("20000")).usedCreditLimit(BigDecimal.ZERO).build();
        Loan savedLoan = Loan.builder().id(1L).loanAmount(new BigDecimal("10000")).interestRate(new BigDecimal("0.1")).numberOfInstallment(12).customer(mockCustomer).build();

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(mockCustomer));
        when(customerService.hasEnoughLimit(mockCustomer.getName(), new BigDecimal("10000"))).thenReturn(true);
        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        // Act
        LoanResponse response = loanService.saveLoan(createLoanRequest);

        // Assert
        assertNotNull(response);
        assertEquals("John", response.getCustomerName());
        assertEquals(12, savedLoan.getInstallments().size());
        verify(customerService, times(1)).getCustomerById(1L);
        verify(loanRepository, times(2)).save(any(Loan.class));
    }

    @Test
    void testSaveLoan_GeneratesCorrectInstallments() {
        // Arrange
        CreateLoanRequest createLoanRequest = new CreateLoanRequest();
        createLoanRequest.setCustomerId(1L);
        createLoanRequest.setLoanAmount(new BigDecimal("12000"));
        createLoanRequest.setInterestRate(new BigDecimal("0.1"));
        createLoanRequest.setNumberOfInstallment(6);

        Customer mockCustomer = Customer.builder().id(1L).name("Jane").creditLimit(new BigDecimal("20000")).usedCreditLimit(BigDecimal.ZERO).build();
        Loan savedLoan = Loan.builder()
                .id(1L)
                .loanAmount(createLoanRequest.getLoanAmount())
                .interestRate(createLoanRequest.getInterestRate())
                .numberOfInstallment(createLoanRequest.getNumberOfInstallment())
                .customer(mockCustomer).build();

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(mockCustomer));
        when(customerService.hasEnoughLimit(mockCustomer.getName(), createLoanRequest.getLoanAmount())).thenReturn(true);
        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        // Act
        LoanResponse response = loanService.saveLoan(createLoanRequest);

        // Assert
        assertNotNull(response);
        assertEquals(6, savedLoan.getInstallments().size());
        savedLoan.getInstallments().forEach(installment ->
                assertEquals(new BigDecimal("2200.0").setScale(1), installment.getAmount()));
    }

    @Test
    void testSaveLoan_CorrectInstallmentDueDates() {
        // Arrange
        CreateLoanRequest createLoanRequest = new CreateLoanRequest();
        createLoanRequest.setCustomerId(1L);
        createLoanRequest.setLoanAmount(new BigDecimal("12000"));
        createLoanRequest.setInterestRate(new BigDecimal("0.1"));
        createLoanRequest.setNumberOfInstallment(6);

        Customer mockCustomer = Customer.builder().id(1L).name("Alice").creditLimit(new BigDecimal("20000")).usedCreditLimit(BigDecimal.ZERO).build();
        Loan savedLoan = Loan.builder()
                .id(1L)
                .loanAmount(createLoanRequest.getLoanAmount())
                .interestRate(createLoanRequest.getInterestRate())
                .numberOfInstallment(createLoanRequest.getNumberOfInstallment())
                .customer(mockCustomer).build();

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(mockCustomer));
        when(customerService.hasEnoughLimit(mockCustomer.getName(), createLoanRequest.getLoanAmount())).thenReturn(true);
        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        // Act
        LoanResponse response = loanService.saveLoan(createLoanRequest);

        // Assert
        assertNotNull(response);
        assertEquals(6, savedLoan.getInstallments().size());
        for (int i = 0; i < 6; i++) {
            assertEquals(LocalDate.now().plusMonths(i + 1).withDayOfMonth(1),
                    savedLoan.getInstallments().get(i).getDueDate());
        }
    }

    @Test
    void testSaveLoan_UpdatesCustomerLimitsCorrectly() {
        // Arrange
        CreateLoanRequest createLoanRequest = new CreateLoanRequest();
        createLoanRequest.setCustomerId(1L);
        createLoanRequest.setLoanAmount(new BigDecimal("15000"));
        createLoanRequest.setInterestRate(new BigDecimal("0.2"));
        createLoanRequest.setNumberOfInstallment(12);

        Customer mockCustomer = Customer.builder()
                .id(1L)
                .name("Bob")
                .creditLimit(new BigDecimal("50000"))
                .usedCreditLimit(BigDecimal.ZERO)
                .build();
        Loan savedLoan = Loan.builder()
                .id(1L)
                .loanAmount(createLoanRequest.getLoanAmount())
                .interestRate(createLoanRequest.getInterestRate())
                .numberOfInstallment(createLoanRequest.getNumberOfInstallment())
                .customer(mockCustomer).build();

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(mockCustomer));
        when(customerService.hasEnoughLimit(mockCustomer.getName(), createLoanRequest.getLoanAmount())).thenReturn(true);
        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        // Act
        loanService.saveLoan(createLoanRequest);

        // Assert
        assertEquals(new BigDecimal("15000"), mockCustomer.getUsedCreditLimit());
        assertEquals(new BigDecimal("35000"), mockCustomer.getCreditLimit());
        verify(customerService, times(1)).saveCustomer(mockCustomer);
    }

    @Test
    void testSaveLoan_UpdatesCustomerCreditLimit() {
        // Arrange
        CreateLoanRequest createLoanRequest = new CreateLoanRequest();
        createLoanRequest.setCustomerId(1L);
        createLoanRequest.setLoanAmount(new BigDecimal("10000"));
        createLoanRequest.setInterestRate(new BigDecimal("0.1"));
        createLoanRequest.setNumberOfInstallment(12);

        Customer mockCustomer = Customer.builder()
                .id(1L)
                .name("John")
                .creditLimit(new BigDecimal("20000"))
                .usedCreditLimit(BigDecimal.ZERO).build();
        Loan savedLoan = Loan.builder()
                .id(1L)
                .loanAmount(new BigDecimal("10000"))
                .interestRate(new BigDecimal("0.1"))
                .numberOfInstallment(12)
                .customer(mockCustomer).build();

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(mockCustomer));
        when(customerService.hasEnoughLimit(mockCustomer.getName(), new BigDecimal("10000"))).thenReturn(true);
        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        // Act
        loanService.saveLoan(createLoanRequest);

        // Assert
        assertEquals(new BigDecimal("10000"), mockCustomer.getUsedCreditLimit());
        assertEquals(new BigDecimal("10000"), mockCustomer.getCreditLimit());
        verify(customerService, times(1)).saveCustomer(mockCustomer);
    }

    @Test
    void testSaveLoan_WhenCustomerNotFound() {
        // Arrange
        CreateLoanRequest createLoanRequest = new CreateLoanRequest();
        createLoanRequest.setCustomerId(1L);

        when(customerService.getCustomerById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                loanService.saveLoan(createLoanRequest));

        assertEquals("Customer not found", exception.getMessage());
        verify(customerService, times(1)).getCustomerById(1L);
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void testSaveLoan_WhenCreditLimitIsInsufficient() {
        // Arrange
        CreateLoanRequest createLoanRequest = new CreateLoanRequest();
        createLoanRequest.setCustomerId(1L);
        createLoanRequest.setLoanAmount(new BigDecimal("10000"));

        Customer mockCustomer = Customer.builder().id(1L).name("John").creditLimit(new BigDecimal("5000")).usedCreditLimit(BigDecimal.ZERO).build();

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(mockCustomer));
        when(customerService.hasEnoughLimit(mockCustomer.getName(), new BigDecimal("10000"))).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                loanService.saveLoan(createLoanRequest));

        assertEquals("Customer does not have enough credit limit for this loan", exception.getMessage());
        verify(customerService, times(1)).getCustomerById(1L);
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void testSaveLoan_WhenInvalidInstallments() {
        // Arrange
        CreateLoanRequest createLoanRequest = new CreateLoanRequest();
        createLoanRequest.setCustomerId(1L);
        createLoanRequest.setNumberOfInstallment(5);
        createLoanRequest.setLoanAmount(new BigDecimal("10000"));
        createLoanRequest.setInterestRate(new BigDecimal("0.6"));

        Customer mockCustomer = Customer.builder().id(1L).name("John").creditLimit(new BigDecimal("20000")).usedCreditLimit(BigDecimal.ZERO).build();

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(mockCustomer));
        when(customerService.hasEnoughLimit(mockCustomer.getName(), new BigDecimal("10000"))).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                loanService.saveLoan(createLoanRequest));

        assertEquals("Invalid number of installments. Must be one of: [6, 9, 12, 24]", exception.getMessage());
        verify(customerService, times(1)).getCustomerById(1L);
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void testSaveLoan_WhenInvalidInterestRate() {
        // Arrange
        CreateLoanRequest createLoanRequest = new CreateLoanRequest();
        createLoanRequest.setCustomerId(1L);
        createLoanRequest.setLoanAmount(new BigDecimal("10000"));
        createLoanRequest.setInterestRate(new BigDecimal("0.6"));
        createLoanRequest.setNumberOfInstallment(6);


        Customer mockCustomer = Customer.builder().id(1L).name("John").creditLimit(new BigDecimal("20000")).usedCreditLimit(BigDecimal.ZERO).build();

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(mockCustomer));
        when(customerService.hasEnoughLimit(mockCustomer.getName(), new BigDecimal("10000"))).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                loanService.saveLoan(createLoanRequest));

        assertEquals("Interest rate must be between 0.1 and 0.5", exception.getMessage());
        verify(customerService, times(1)).getCustomerById(1L);
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void testGetLoansByFilters_WhenAllFiltersAreProvided() {
        // Arrange
        Long customerId = 1L;
        Integer numberOfInstallment = 12;
        Boolean isPaid = false;

        Customer mockCustomer = Customer.builder().id(customerId).name("John").surname("Doe").creditLimit(new BigDecimal("20000")).build();
        Loan mockLoan = Loan.builder().id(1L).loanAmount(new BigDecimal("10000")).interestRate(new BigDecimal("0.1")).numberOfInstallment(numberOfInstallment).isPaid(isPaid).customer(mockCustomer).build();

        when(customerService.getCustomerById(customerId)).thenReturn(Optional.of(mockCustomer));
        when(loanRepository.findByCustomerAndNumberOfInstallmentAndIsPaid(mockCustomer, numberOfInstallment, isPaid)).thenReturn(List.of(mockLoan));

        // Act
        LoanResponse response = loanService.getLoansByFilters(customerId, numberOfInstallment, isPaid);

        // Assert
        assertNotNull(response);
        assertEquals("John", response.getCustomerName());
        assertEquals(1, response.getLoanItems().size());
        verify(customerService, times(1)).getCustomerById(customerId);
        verify(loanRepository, times(1)).findByCustomerAndNumberOfInstallmentAndIsPaid(mockCustomer, numberOfInstallment, isPaid);
    }

    @Test
    void testGetLoansByFilters_WhenOnlyInstallmentNumberFilterIsProvided() {
        // Arrange
        Long customerId = 1L;
        Integer numberOfInstallment = 12;

        Customer mockCustomer = Customer.builder().id(customerId).name("Jane").surname("Doe").creditLimit(new BigDecimal("15000")).build();
        Loan mockLoan = Loan.builder().id(2L).loanAmount(new BigDecimal("8000")).interestRate(new BigDecimal("0.2")).numberOfInstallment(numberOfInstallment).isPaid(true).customer(mockCustomer).build();

        when(customerService.getCustomerById(customerId)).thenReturn(Optional.of(mockCustomer));
        when(loanRepository.findByCustomerAndNumberOfInstallment(mockCustomer, numberOfInstallment)).thenReturn(List.of(mockLoan));

        // Act
        LoanResponse response = loanService.getLoansByFilters(customerId, numberOfInstallment, null);

        // Assert
        assertNotNull(response);
        assertEquals("Jane", response.getCustomerName());
        assertEquals(1, response.getLoanItems().size());
        verify(customerService, times(1)).getCustomerById(customerId);
        verify(loanRepository, times(1)).findByCustomerAndNumberOfInstallment(mockCustomer, numberOfInstallment);
    }

    @Test
    void testGetLoansByFilters_WhenOnlyIsPaidFilterIsProvided() {
        // Arrange
        Long customerId = 2L;
        Boolean isPaid = true;

        Customer mockCustomer = Customer.builder().id(customerId).name("Emily").surname("Smith").creditLimit(new BigDecimal("10000")).build();
        Loan mockLoan = Loan.builder().id(3L).loanAmount(new BigDecimal("5000")).interestRate(new BigDecimal("0.3")).numberOfInstallment(6).isPaid(isPaid).customer(mockCustomer).build();

        when(customerService.getCustomerById(customerId)).thenReturn(Optional.of(mockCustomer));
        when(loanRepository.findByCustomerAndIsPaid(mockCustomer, isPaid)).thenReturn(List.of(mockLoan));

        // Act
        LoanResponse response = loanService.getLoansByFilters(customerId, null, isPaid);

        // Assert
        assertNotNull(response);
        assertEquals("Emily", response.getCustomerName());
        assertEquals(1, response.getLoanItems().size());
        verify(customerService, times(1)).getCustomerById(customerId);
        verify(loanRepository, times(1)).findByCustomerAndIsPaid(mockCustomer, isPaid);
    }

    @Test
    void testGetLoansByFilters_WhenNoFiltersAreProvided() {
        // Arrange
        Long customerId = 3L;

        Customer mockCustomer = Customer.builder().id(customerId).name("Michael").surname("Brown").creditLimit(new BigDecimal("30000")).build();
        Loan mockLoan1 = Loan.builder().id(4L).loanAmount(new BigDecimal("15000")).interestRate(new BigDecimal("0.25")).numberOfInstallment(6).isPaid(false).customer(mockCustomer).build();
        Loan mockLoan2 = Loan.builder().id(5L).loanAmount(new BigDecimal("10000")).interestRate(new BigDecimal("0.15")).numberOfInstallment(12).isPaid(true).customer(mockCustomer).build();

        when(customerService.getCustomerById(customerId)).thenReturn(Optional.of(mockCustomer));
        when(loanRepository.findByCustomer(mockCustomer)).thenReturn(List.of(mockLoan1, mockLoan2));

        // Act
        LoanResponse response = loanService.getLoansByFilters(customerId, null, null);

        // Assert
        assertNotNull(response);
        assertEquals("Michael", response.getCustomerName());
        assertEquals(2, response.getLoanItems().size());
        verify(customerService, times(1)).getCustomerById(customerId);
        verify(loanRepository, times(1)).findByCustomer(mockCustomer);
    }

    @Test
    void testGetLoansByFilters_WhenCustomerNotFound() {
        // Arrange
        Long customerId = 4L;

        when(customerService.getCustomerById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> loanService.getLoansByFilters(customerId, null, null));
        assertEquals("Customer not found", exception.getMessage());
        verify(customerService, times(1)).getCustomerById(customerId);
        verify(loanRepository, never()).findByCustomer(any(Customer.class));
    }
}
