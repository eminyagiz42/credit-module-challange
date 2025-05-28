package com.eminyagiz.creditmodule.repository;

import com.eminyagiz.creditmodule.model.entity.Installment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstallmentRepository extends JpaRepository<Installment, Long> {

    List<Installment> findAllById(Long id);

}
