package com.eminyagiz.creditmodule.common.mapper;

import com.eminyagiz.creditmodule.model.dto.LoanResponse;
import com.eminyagiz.creditmodule.model.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, LoanInstallmentMapper.class})
public interface LoanMapper {

    @Mapping(target = "loanId", source = "entity.id")
    @Mapping(target = "userId", source = "entity.user.id")
    LoanResponse toLoanResponse(Loan entity);

}
