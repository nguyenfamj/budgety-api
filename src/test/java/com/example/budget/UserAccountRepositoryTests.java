package com.example.budget;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.budget.features.user.UserAccount;
import com.example.budget.features.user.UserAccountRepository;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class UserAccountRepositoryTests {
    @Autowired
    UserAccountRepository userAccountRepository;

    @Test
    public void findByUserNameShouldReturnFirstName() {
        UserAccount user1 = userAccountRepository.findByUserName("user1");

        assertThat(user1.getFirstName()).isEqualTo("Nguyen");
    }

    @Test
    public void createNewUserAccount() {
        UserAccount user2 = new UserAccount("user2", "John", "Doe",
                "$2a$10$02DLbwIF4og5GP1EKS94MO7aqj4wiIe7yRaK6WpK/KIDrttvTVOHa");

        UserAccount savedUser = userAccountRepository.save(user2);

        assertThat(savedUser.getUserName()).isEqualTo(user2.getUserName());
    }
}
