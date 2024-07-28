package com.reddit.backend.controllers;

import com.reddit.backend.domain.SubredditDto;
import com.reddit.backend.service.SubredditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("api/subreddit")
@RequiredArgsConstructor
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping("/add-subreddit")
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {

        return status(HttpStatus.CREATED)
                .body(subredditService.persistSubreddit(subredditDto));
    }

    @GetMapping("fetchAll-subreddit")
    public ResponseEntity<List<SubredditDto>> fetchAll() {
        return status(HttpStatus.OK)
                .body(subredditService.fetchAllSubreddit());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubredditById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subredditService.getSubredditById(id));
    }

}
