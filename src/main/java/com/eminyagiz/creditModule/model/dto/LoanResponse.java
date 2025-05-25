package com.eminyagiz.creditModule.model.dto;

import com.eminyagiz.creditModule.model.entity.Customer;
import com.eminyagiz.creditModule.model.entity.Installment;
import com.eminyagiz.creditModule.model.entity.Loan;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class LoanResponse {

    private String customerName;
    private String customerSurname;
    private BigDecimal customerLimit;
    private List<LoanItemResponse> loanItems;

}
