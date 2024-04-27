package com.reddit.backend.repository;

import com.reddit.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    default Optional<User> saveUser(User userObject) {
        User user = save(userObject);
        return Optional.of(user);
    }
}
