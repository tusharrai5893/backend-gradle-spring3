package com.reddit.backend.service;

import com.reddit.backend.domain.VoteDto;
import com.reddit.backend.exceptions.RedditCustomException;
import com.reddit.backend.mapper.VoteMapper;
import com.reddit.backend.models.Post;
import com.reddit.backend.models.Vote;
import com.reddit.backend.repository.PostRepo;
import com.reddit.backend.repository.VoteRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.reddit.backend.models.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepo voteRepo;
    private final PostRepo postRepo;

    private final AuthService authService;

    private final VoteMapper voteMapper;

    @Transactional
    public void doVote(VoteDto voteDto) {

        Post post = postRepo.findById(voteDto.getPostId()).orElseThrow(() -> new RedditCustomException("Post not found for the vote"));

        Optional<Vote> topByPostAndUser = voteRepo.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if (topByPostAndUser.isPresent() && topByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new RedditCustomException(
                    String.format("You have already %s'd for the post.", voteDto.getVoteType())
            );
        }

        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepo.save(voteMapper.mapDtoToModel(voteDto, post, authService.getCurrentUser()));
        postRepo.save(post);
    }
}
