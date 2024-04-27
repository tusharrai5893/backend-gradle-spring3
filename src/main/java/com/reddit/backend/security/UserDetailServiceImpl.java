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
        Optional<User> singleUser = userRepo.findByUsername(userName);
        User user1 = singleUser.orElseThrow(() -> new UsernameNotFoundException("User Not found, Username :- " + userName));
        return new UserDetailsImpl(user1);
    }
}
