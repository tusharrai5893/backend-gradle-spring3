package com.reddit.backend.repository;

import com.reddit.backend.models.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubredditRepo extends JpaRepository<Subreddit, Long> {

    @Query("select s from Subreddit s where s.subredditName in (:subredditName)")
    Optional<Subreddit> findBySubredditName(String subredditName);
}
