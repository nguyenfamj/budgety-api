package com.example.budget.services.UserDetails;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.budget.features.user.UserAccount;
import com.example.budget.features.user.UserAccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserAccountRepository userAccountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserAccount foundUser = userAccountRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this user name: " + userName));

        return UserDetailsImpl.build(foundUser);
    }

}
