package com.eminyagiz.creditmodule.service;

import com.eminyagiz.creditmodule.model.dto.LoginRequest;
import com.eminyagiz.creditmodule.model.dto.LoginResponse;
import com.eminyagiz.creditmodule.model.dto.RegistrationRequest;

public interface AuthenticationService {
    LoginResponse register(RegistrationRequest registrationRequest);

    LoginResponse login(LoginRequest loginRequest);
}
