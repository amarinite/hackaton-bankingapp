package com.hackathon.bankingapp.Services.Impl;

import com.hackathon.bankingapp.Entities.User;
import com.hackathon.bankingapp.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            user = userRepository.findByAccountNumber(username);
        }
        return user.map(u -> (UserDetails) u)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    }
}
