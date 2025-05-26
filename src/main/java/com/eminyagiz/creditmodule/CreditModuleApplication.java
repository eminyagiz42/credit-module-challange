package com.eminyagiz.creditmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class })
public class CreditModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditModuleApplication.class, args);
	}

}
