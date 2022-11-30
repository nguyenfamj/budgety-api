package com.example.budget;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.budget.features.account.AccountController;
import com.example.budget.features.auth.AuthController;
import com.example.budget.features.budget.BudgetController;
import com.example.budget.features.category.CategoryController;
import com.example.budget.features.transaction.TransactionController;
import com.example.budget.features.transaction.payee.PayeeController;
import com.example.budget.features.user.UserAccountController;

@SpringBootTest
class BudgetApplicationTests {
	@Autowired
	AuthController authController;

	@Autowired
	AccountController accountController;

	@Autowired
	CategoryController categoryController;

	@Autowired
	TransactionController transactionController;

	@Autowired
	PayeeController payeeController;

	@Autowired
	UserAccountController userAccountController;

	@Autowired
	BudgetController budgetController;

	@Test
	void contextLoads() {
		assertThat(authController).isNotNull();
		assertThat(accountController).isNotNull();
		assertThat(categoryController).isNotNull();
		assertThat(transactionController).isNotNull();
		assertThat(payeeController).isNotNull();
		assertThat(userAccountController).isNotNull();
		assertThat(budgetController).isNotNull();
	}

}
