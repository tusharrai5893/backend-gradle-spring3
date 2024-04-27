package com.reddit.backend.controllers;

import com.reddit.backend.dto.CommentDto;
import com.reddit.backend.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@AllArgsConstructor
@RequestMapping("api/comment/")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add-comment")
    public ResponseEntity<String> createComment(@RequestBody CommentDto commentDto) {

        commentService.createComment(commentDto);
        return status(201).body("Comment posted");
    }

    @GetMapping("/fetchAll-comment")
    public ResponseEntity<List<CommentDto>> getAllComment() {

        List<CommentDto> allComment = commentService.getAllComment();

        return status(200).body((allComment));

    }

    @GetMapping("/fetchCommentByPost-comment/{postId}")
    public ResponseEntity<List<CommentDto>> fetchAllCommentByPost(@PathVariable Long postId) {
        List<CommentDto> allCommentsByPost = commentService.fetchAllCommentByPost(postId);

        return status(200).body(allCommentsByPost);

    }

    @GetMapping("/fetchAllCommentByUser-comment/{userName}")
    public ResponseEntity<List<CommentDto>> fetchAllCommentByUser(@PathVariable String userName) {
        List<CommentDto> allCommentsByUser = commentService.fetchAllCommentByUser(userName);

        return status(200).body(allCommentsByUser);

    }

}
