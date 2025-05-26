package com.eminyagiz.creditmodule.model.dto;

import com.eminyagiz.creditmodule.model.entity.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateUserRequest(
        @NotEmpty String name,
        @NotEmpty String surname,
        @NotEmpty String password,
        @NotEmpty String username,
        @NotNull BigDecimal creditLimit,
        @NotNull Role customerRole
        ) {
}
