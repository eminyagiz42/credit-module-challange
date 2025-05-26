package com.eminyagiz.creditmodule.common.mapper;

import com.eminyagiz.creditmodule.model.dto.LoanInstallmentResponse;
import com.eminyagiz.creditmodule.model.entity.Installment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, LoanMapper.class})
public interface LoanInstallmentMapper {

    @Mapping(target = "loanId", source = "installment.loan.id")
    @Mapping(target = "nextPaymentDate", source = "installment.dueDate")
    LoanInstallmentResponse fromInstallment(Installment installment);
}
