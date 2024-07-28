package com.reddit.backend.controllers;

import com.reddit.backend.domain.VoteDto;
import com.reddit.backend.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vote")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/doVote")
    public ResponseEntity doVote(@RequestBody VoteDto voteDto) {
        voteService.doVote(voteDto);
        return ResponseEntity.status(200).body(String.format("you have %s'd for the post.", voteDto.getVoteType()));
    }
}
