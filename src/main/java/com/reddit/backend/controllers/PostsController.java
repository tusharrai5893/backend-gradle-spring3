package com.reddit.backend.controllers;

import com.reddit.backend.dto.PostReqDto;
import com.reddit.backend.dto.PostResDto;
import com.reddit.backend.mapper.PostMapper;
import com.reddit.backend.models.Post;
import com.reddit.backend.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostsController {

    private final PostService postService;
    private final PostMapper postMapper;

    @PostMapping(value = "/add-post")
    public ResponseEntity<PostResDto> createPost(@RequestBody @Valid PostReqDto postReqDto) {
        Post post = postService.persistPost(postReqDto);
        return ResponseEntity.status(201).body(postMapper.mapModelToDto(post));
    }


    @GetMapping(value = "/fetchAll-post")
    public ResponseEntity fetchAllPost() {
        List<PostResDto> postResDtos = postService.fetchAllPost();

        //postResDtos.stream().forEach(e-> System.err.print(e));
        return ResponseEntity.status(200).body(postResDtos);
    }

    @GetMapping(value = "/fetchOne-post/{postId}")
    public ResponseEntity fetchOnePost(@PathVariable Long postId) {
        PostResDto post = postService.fetchOnePost(postId);
        return ResponseEntity.status(200).body(post);
    }

    @GetMapping(value = "/fetchPostBySubreddit-post/{subredditId}")
    public ResponseEntity fetchPostBySubreddit(@PathVariable Long subredditId) {
        List<PostResDto> post = postService.fetchPostBySubreddit(subredditId);
        return ResponseEntity.status(200).body(post);
    }

    @GetMapping(value = "/fetchPostByUsername-post/{username}")
    public ResponseEntity fetchPostByUsername(@PathVariable String username) {
        List<PostResDto> posts = postService.fetchPostByUsername(username);
        return ResponseEntity.status(200).body(posts);
    }


}
