package com.example.budget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BudgetApplication {

	@RequestMapping("/")
	public String index() {
		return "Hello Docker";
	}

	public static void main(String[] args) {
		SpringApplication.run(BudgetApplication.class, args);
	}

}
