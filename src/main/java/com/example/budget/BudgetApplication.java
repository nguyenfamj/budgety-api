package com.example.budget;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

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
	public CommandLineRunner userDemo(UserAccountRepository userAccountRepository) {

		return (args) -> {
			UserAccount user1 = new UserAccount("user1", "Nguyen", "Pham",
					"$2a$10$02DLbwIF4og5GP1EKS94MO7aqj4wiIe7yRaK6WpK/KIDrttvTVOHa");
			Set<Role> roles = new HashSet<>();
			roles.add(new Role(RoleEnum.ROLE_USER));

			user1.setRoles(roles);

			userAccountRepository.save(user1);

			userAccountRepository.findAll().stream().forEach((user) -> {
				log.info("Log: ", user);
			});

		};
	}
}
