package com.example.budget;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

import com.example.budget.features.account.Account;
import com.example.budget.features.account.AccountRepository;
import com.example.budget.features.role.Role;
import com.example.budget.features.role.RoleEnum;
import com.example.budget.features.user.UserAccount;
import com.example.budget.features.user.UserAccountRepository;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class BudgetApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgetApplication.class, args);

	}

	@Bean
	public CommandLineRunner userDemo(UserAccountRepository userAccountRepository,
			AccountRepository accountRepository) {

		return (args) -> {
			// New user
			UserAccount user1 = new UserAccount("user1", "Nguyen", "Pham",
					"$2a$10$02DLbwIF4og5GP1EKS94MO7aqj4wiIe7yRaK6WpK/KIDrttvTVOHa");

			UserAccount admin1 = new UserAccount("admin1", "Admin", "Budgety",
					"$2a$10$02DLbwIF4og5GP1EKS94MO7aqj4wiIe7yRaK6WpK/KIDrttvTVOHa");

			// Set role
			Set<Role> userRoles = new HashSet<>();
			userRoles.add(new Role(RoleEnum.ROLE_USER));
			user1.setRoles(userRoles);

			Set<Role> adminRoles = new HashSet<>();
			adminRoles.add(new Role(RoleEnum.ROLE_ADMIN));
			admin1.setRoles(adminRoles);

			// Save user in the database
			userAccountRepository.save(user1);
			userAccountRepository.save(admin1);

			// New account for user1
			Account account_user1 = new Account("babi", new BigDecimal(20000.00), user1);

			// Save accounts
			accountRepository.save(account_user1);

			userAccountRepository.findAll().stream().forEach((user) -> {
				log.info("Log: {}", user.getUserName());
			});

		};
	}
}
