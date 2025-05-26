package com.eminyagiz.creditmodule.controller;

import com.eminyagiz.creditmodule.model.dto.LoginRequest;
import com.eminyagiz.creditmodule.model.dto.LoginResponse;
import com.eminyagiz.creditmodule.model.dto.RegistrationRequest;
import com.eminyagiz.creditmodule.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }

    @PostMapping("/register")
    public LoginResponse register(@Valid @RequestBody RegistrationRequest request) {
        return authenticationService.register(request);
    }
}
