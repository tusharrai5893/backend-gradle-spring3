package com.reddit.backend.service;


import com.reddit.backend.config.Application;
import com.reddit.backend.domain.CommentDto;
import com.reddit.backend.exceptions.RedditCustomException;
import com.reddit.backend.mailConfig.MailService;
import com.reddit.backend.mailConfig.NotificationEmail;
import com.reddit.backend.mapper.CommentMapper;
import com.reddit.backend.models.Comment;
import com.reddit.backend.models.Post;
import com.reddit.backend.models.User;
import com.reddit.backend.repository.CommentRepo;
import com.reddit.backend.repository.PostRepo;
import com.reddit.backend.repository.UserRepo;
import com.reddit.backend.utilities.Utility;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepo commentRepo;
    private final PostRepo postRepo;
    private final UserRepo userRepo;

    private final AuthService authService;
    private final MailService mailService;

    private final CommentMapper commentMapper;
    private final Application appConfig;


    public void createComment(CommentDto commentDto) {

        Post commentOnPost = postRepo.findById(commentDto.getPostId())
                .orElseThrow(() -> new RedditCustomException("In Create Comment ,No post found by ID= " + commentDto.getPostId().toString()));

        User currentUser = authService.getCurrentUser();
        Comment comment = commentMapper.mapDTOtoModel(commentDto, commentOnPost, currentUser);
        commentRepo.save(comment);

        NotificationEmail msg = new NotificationEmail();
        msg.setSubject(commentOnPost.getUser().getUsername().toUpperCase() + " commented on a post that you have posted");
        msg.setRecipient(commentOnPost.getUser().getEmail());
        msg.setBody(currentUser.getUsername().toUpperCase() + " posted a comment on your post !");
        msg.setMsg(commentOnPost.getPostName());
        msg.setLink(appConfig.getUrl() + "/api/post/" + commentDto.getPostId());

        mailService.sendMail(msg);
    }

    public List<CommentDto> fetchAllCommentByPost(Long postId) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new RedditCustomException("Unable to find post commented on post ID " + postId));
        Set<Comment> commentRepoAll = commentRepo.findByPost(post);
        Set<Comment> fillComments = Utility.removeRedundantComments(new HashSet<>(), commentRepoAll);
        return fillComments
                .stream()
                .map(commentMapper::mapModelToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> fetchAllCommentByUser(String userName) {
        User user = userRepo.findByUsername(userName).orElseThrow(() -> new RedditCustomException("Unable to find Comment by user " + userName));

        return commentRepo.findAllByUser(user)
                .stream()
                .map(commentMapper::mapModelToDto)
                .collect(Collectors.toList());
    }


    public List<CommentDto> getAllComment() {
        Set<Comment> commentRepoAll = commentRepo.findAllSet();
        Set<Comment> fillComments = Utility.removeRedundantComments(new HashSet<>(), commentRepoAll);
        return fillComments
                .stream()
                .map(commentMapper::mapModelToDto)
                .collect(Collectors.toList());

    }


}
