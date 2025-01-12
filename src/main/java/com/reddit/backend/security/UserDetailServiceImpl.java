package com.reddit.backend.security;

import com.reddit.backend.models.User;
import com.reddit.backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return new UserDetailsImpl(userRepo
                .findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("No Customer found, Username :- " + userName)));
    }
}
