package com.reddit.backend.repository;

import com.reddit.backend.models.Comment;
import com.reddit.backend.models.Post;
import com.reddit.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c left join fetch c.children where c.post=:post")
    Set<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);

    @Query("select c from Comment c left join fetch c.children")
    Set<Comment> findAllSet();
}
