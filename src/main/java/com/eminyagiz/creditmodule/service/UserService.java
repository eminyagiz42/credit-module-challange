package com.eminyagiz.creditmodule.service;

import com.eminyagiz.creditmodule.model.dto.CreateUserRequest;
import com.eminyagiz.creditmodule.model.dto.UpdateCreditLimitRequest;
import com.eminyagiz.creditmodule.model.entity.User;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public interface UserService {

    User getUserById(Long customerId);

    User getUserByName(@NotNull String username);

    User createUser(CreateUserRequest request);

    User updateCreditLimit(UpdateCreditLimitRequest updateCreditLimitRequest);

    User addUsedCreditLimit(User user, BigDecimal totalLoanAmount);
}
