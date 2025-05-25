package com.eminyagiz.creditModule.model.dto;

import com.eminyagiz.creditModule.model.entity.Loan;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class InstallmentResponse {

    private Long id;
    private Integer installmentNumber;
    private BigDecimal amount;
    private BigDecimal paidAmount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private Boolean isPaid;
}
