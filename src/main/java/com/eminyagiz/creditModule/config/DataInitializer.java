package com.eminyagiz.creditModule.config;

import com.eminyagiz.creditModule.model.entity.Customer;
import com.eminyagiz.creditModule.model.entity.Role;
import com.eminyagiz.creditModule.model.entity.User;
import com.eminyagiz.creditModule.repository.CustomerRepository;
import com.eminyagiz.creditModule.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            Customer customer = Customer.builder().name("Adam").surname("Johnathon")
                    .creditLimit(new BigDecimal("200000.00")).usedCreditLimit(new BigDecimal("00.00")).build();

            Customer customer1 = Customer.builder().name("Michael").surname("Smith")
                    .creditLimit(new BigDecimal("700000.00")).usedCreditLimit(new BigDecimal("00.00")).build();

            customer = customerRepository.save(customer);
            customer1 = customerRepository.save(customer1);

            User customerUser = User.builder()
                    .username("customer1")
                    .password(passwordEncoder.encode("password123"))
                    .role(Role.CUSTOMER)
                    .customer(customer1)
                    .build();

            User adminUser = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("password123"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(customerUser);
            userRepository.save(adminUser);

            System.out.println("Sample data initialized!");
        };
    }
}
