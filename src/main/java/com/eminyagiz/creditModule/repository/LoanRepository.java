package com.eminyagiz.creditModule.repository;

import com.eminyagiz.creditModule.model.entity.Customer;
import com.eminyagiz.creditModule.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByCustomer(Customer customer);
    List<Loan> findByCustomerAndNumberOfInstallment(Customer customer, Integer numberOfInstallment);
    List<Loan> findByCustomerAndIsPaid(Customer customer, Boolean isPaid);
    List<Loan> findByCustomerAndNumberOfInstallmentAndIsPaid(Customer customer, Integer numberOfInstallment, Boolean isPaid);
}