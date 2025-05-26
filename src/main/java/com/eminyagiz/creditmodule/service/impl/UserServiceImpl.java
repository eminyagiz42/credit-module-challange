package com.eminyagiz.creditmodule.service.impl;

import com.eminyagiz.creditmodule.model.dto.CreateUserRequest;
import com.eminyagiz.creditmodule.model.dto.UpdateCreditLimitRequest;
import com.eminyagiz.creditmodule.repository.UserRepository;
import com.eminyagiz.creditmodule.model.entity.User;
import com.eminyagiz.creditmodule.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long customerId) {
        return userRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No any customer found with customerId: %d", customerId)));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new EntityExistsException("The user already exists with username %s".formatted(request.username()));
        }
        User user = User.builder()
                .name(request.name())
                .surname(request.surname())
                .creditLimit(request.creditLimit())
                .username(request.username())
                .password(request.password())
                .usedCreditLimit(BigDecimal.ZERO)
                .roleName(request.customerRole())
                .loans(List.of())
                .build();
        return userRepository.save(user);
    }

    @Override
    public User updateCreditLimit(UpdateCreditLimitRequest updateCreditLimitRequest) {
        User user = getUserById(updateCreditLimitRequest.customerId());
        user.addUserCreditLimit(BigDecimal.valueOf(updateCreditLimitRequest.additionalCreditLimit()));
        return userRepository.save(user);
    }

    @Override
    public User addUsedCreditLimit(User user, BigDecimal totalLoanAmount) {
        user.addUserUsedCreditLimit(totalLoanAmount);
        return userRepository.save(user);
    }

    @Override
    public User getUserByName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No any customer found with username: %s", username)));
    }
}
