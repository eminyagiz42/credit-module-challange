package com.eminyagiz.creditmodule.service.impl;

import com.eminyagiz.creditmodule.common.exception.AuthenticationException;
import com.eminyagiz.creditmodule.model.dto.*;
import com.eminyagiz.creditmodule.model.dto.CreateUserRequest;
import com.eminyagiz.creditmodule.model.dto.LoginRequest;
import com.eminyagiz.creditmodule.model.dto.LoginResponse;
import com.eminyagiz.creditmodule.model.dto.RegistrationRequest;
import com.eminyagiz.creditmodule.model.entity.User;
import com.eminyagiz.creditmodule.security.JWTUtil;
import com.eminyagiz.creditmodule.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.eminyagiz.creditmodule.service.UserService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @Override
    public LoginResponse register(RegistrationRequest registrationRequest) {
        CreateUserRequest createUserRequest = new CreateUserRequest(
                registrationRequest.name(),
                registrationRequest.surname(),
                registrationRequest.password(),
                registrationRequest.username(),
                BigDecimal.ZERO,
                registrationRequest.customerRole());
        User user = userService.createUser(createUserRequest);
        return new LoginResponse(jwtUtil.generateToken(user.getRoleName(), user), user.getId());
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        final User user = userService.getUserByName(loginRequest.username());
        if (!user.getPassword().equals(loginRequest.password())) {
            throw new AuthenticationException("Requested login parameters are incorrect!");
        }
        return new LoginResponse(jwtUtil.generateToken(user.getRoleName(), user), user.getId());
    }
}
