package com.eminyagiz.creditmodule.model.dto;

import com.eminyagiz.creditmodule.model.entity.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotNull @Size(min = 2, max = 64) String name,
        @NotNull @Size(min = 2, max = 64) String surname,
        @NotNull @Size(min = 4, max = 24) String password,
        @NotNull @Size(min = 4, max = 24) String username,
        @NotNull Role customerRole
) {
}
