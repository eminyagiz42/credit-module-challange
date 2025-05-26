package com.eminyagiz.creditmodule.controller;

import com.eminyagiz.creditmodule.common.mapper.UserMapper;
import com.eminyagiz.creditmodule.model.dto.CreateUserRequest;
import com.eminyagiz.creditmodule.model.dto.UpdateCreditLimitRequest;
import com.eminyagiz.creditmodule.model.dto.UserResponse;
import com.eminyagiz.creditmodule.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{customerId}")
    @PreAuthorize("#customerId == authentication.principal.id or hasAuthority('ADMIN')")
    public UserResponse getCustomerById(@PathVariable Long customerId) {
        return userMapper.toUserResponse(userService.getUserById(customerId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        return userMapper.toUserResponse(userService.createUser(request));
    }

    @PutMapping("/credit-limit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponse updateUserCreditLimit(@Valid @RequestBody UpdateCreditLimitRequest updateCreditLimitRequest) {
        return userMapper.toUserResponse(userService.updateCreditLimit(updateCreditLimitRequest));
    }

}
