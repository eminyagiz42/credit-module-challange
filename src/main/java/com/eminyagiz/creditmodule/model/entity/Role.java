package com.eminyagiz.creditmodule.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public enum Role implements GrantedAuthority {
    CUSTOMER("CUSTOMER"),
    ADMIN("ADMIN");

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}
