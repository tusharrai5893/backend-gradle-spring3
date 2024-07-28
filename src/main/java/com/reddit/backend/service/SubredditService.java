package com.reddit.backend.service;

import com.reddit.backend.domain.SubredditDto;
import com.reddit.backend.exceptions.RedditCustomException;
import com.reddit.backend.mapper.SubredditMapper;
import com.reddit.backend.models.Subreddit;
import com.reddit.backend.models.User;
import com.reddit.backend.repository.SubredditRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubredditService {

    private final SubredditRepo subredditRepo;

    private final SubredditMapper subredditMapper;
    private final AuthService authService;

    @Transactional
    public SubredditDto persistSubreddit(SubredditDto subredditDto) {

        User currentUser = authService.getCurrentUser();
        Subreddit persistedSubReddit = subredditRepo.save(subredditMapper.mapDTOToModel(subredditDto, currentUser));
        subredditDto.setSubredditId(persistedSubReddit.getSubredditId());

        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> fetchAllSubreddit() {
        String currentUser = authService.getCurrentUser().getUsername();
        return subredditRepo.findAll()
                .stream()
                .map(e -> {
                    return subredditMapper.mapModelToDTO(e, currentUser);
                })
                .collect(Collectors.toList());

    }

    public SubredditDto getSubredditById(Long id) {
        String currentUser = authService.getCurrentUser().getUsername();
        Subreddit subreddit = subredditRepo.findById(id)
                .orElseThrow(() -> new RedditCustomException("No Subreddit found with ID = " + id));

        return subredditMapper.mapModelToDTO(subreddit, currentUser);
    }
}
