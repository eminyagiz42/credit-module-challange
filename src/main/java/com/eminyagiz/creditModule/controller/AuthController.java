package com.eminyagiz.creditModule.controller;

import com.eminyagiz.creditModule.model.dto.AuthRequest;
import com.eminyagiz.creditModule.model.dto.AuthResponse;
import com.eminyagiz.creditModule.model.entity.User;
import com.eminyagiz.creditModule.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        User user = userRepository.findByUsername(authRequest.getUsername()).orElseThrow();

        return ResponseEntity.ok(new AuthResponse(null, user.getUsername(), user.getRole().name()));
    }
}
