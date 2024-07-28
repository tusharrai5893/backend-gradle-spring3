package com.reddit.backend.service;

import com.reddit.backend.domain.PostReqDto;
import com.reddit.backend.domain.PostResDto;
import com.reddit.backend.exceptions.RedditCustomException;
import com.reddit.backend.mapper.PostMapper;
import com.reddit.backend.models.Post;
import com.reddit.backend.models.Subreddit;
import com.reddit.backend.models.User;
import com.reddit.backend.repository.PostRepo;
import com.reddit.backend.repository.SubredditRepo;
import com.reddit.backend.repository.UserRepo;
import com.reddit.backend.utilities.Utility;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepo postRepo;
    private final SubredditRepo subredditRepo;
    private final UserRepo userRepo;

    private final AuthService authService;

    private final PostMapper postMapper;

    @Transactional(rollbackFor = Exception.class)
    public Post persistPost(PostReqDto postReqDto) {
        Subreddit subredditName = subredditRepo
                .findBySubredditName(postReqDto.getSubredditName())
                .orElseThrow(() -> new RedditCustomException("No subreddit named " + postReqDto.getSubredditName()));

        return postRepo.save(postMapper.mapDTOtoModel(postReqDto, subredditName, authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public List<PostResDto> fetchAllPost() {
        List<Post> postRepo1 = postRepo.findAll();
        return postRepo1
                .stream()
                .sorted((o1, o2) -> {
                    int compare = o2.getCreatedDate().compareTo(o1.getCreatedDate());
                    if (compare == 0)
                        return o1.getPostId().compareTo(o2.getPostId());
                    return compare;
                })
                .map(post -> {
                            Utility.removeRedundantComments(new HashSet<>(), post.getComments());
                            return postMapper.mapModelToDto(post);

                        }
                )
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public PostResDto fetchOnePost(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RedditCustomException(new StringBuilder().append("No post found for the id=").append(postId).toString()));
        Utility.removeRedundantComments(new HashSet<>(), post.getComments());
        return postMapper.mapModelToDto(post);
    }

    public List<PostResDto> fetchPostBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepo.findById(subredditId)
                .orElseThrow(() -> new RedditCustomException("Unable to fetch Subreddit by subreddit Id= " + subredditId));

        List<Post> subredditByPost = postRepo.findAllBySubreddit(subreddit);

        return subredditByPost.stream()
                .sorted((o1, o2) -> {
                    int compare = o2.getCreatedDate().compareTo(o1.getCreatedDate());
                    if (compare == 0)
                        return o1.getPostId().compareTo(o2.getPostId());
                    return compare;
                })
                .map(postMapper::mapModelToDto)
                .collect(toList());


    }

    public List<PostResDto> fetchPostByUsername(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RedditCustomException("No user found for username =" + username));

        return postRepo.findAllByUser(user)
                .stream().map(postMapper::mapModelToDto)
                .collect(toList());

    }
}

