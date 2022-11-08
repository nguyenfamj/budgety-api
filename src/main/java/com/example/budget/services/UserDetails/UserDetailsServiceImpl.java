package com.example.budget.services.UserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.budget.features.user.UserAccount;
import com.example.budget.features.user.UserAccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserDetailsServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount foundUser = userAccountRepository.findByUserName(username);

        UserDetails user = new User(username, foundUser.getHashedPassword(),
                AuthorityUtils.createAuthorityList(foundUser.getRole()));
        return user;
    }
}
