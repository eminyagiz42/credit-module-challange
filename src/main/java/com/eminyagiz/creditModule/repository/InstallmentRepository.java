package com.eminyagiz.creditModule.repository;

import com.eminyagiz.creditModule.model.entity.Installment;
import com.eminyagiz.creditModule.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long> {

    List<Installment> findByLoanId(Long id);

    List<Installment> findByLoanOrderByInstallmentNumber(Loan loan);
}