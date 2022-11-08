package com.example.budget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.example.budget.config.RsaKeyProperties;
import com.example.budget.features.user.UserAccount;
import com.example.budget.features.user.UserAccountRepository;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class BudgetApplication {

	private static final Logger log = LoggerFactory.getLogger(BudgetApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BudgetApplication.class, args);

	}

	@Bean
	public CommandLineRunner userDemo(UserAccountRepository userAccountRepository) {

		return (args) -> {
			userAccountRepository.save(new UserAccount("nguyenfamj1", "Nguyen", "Pham",
					"$2a$10$02DLbwIF4og5GP1EKS94MO7aqj4wiIe7yRaK6WpK/KIDrttvTVOHa",
					"USER"));

			log.info("new user created");

		};
	}
}
