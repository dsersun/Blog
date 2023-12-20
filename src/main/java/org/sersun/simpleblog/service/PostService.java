package org.sersun.simpleblog.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.sersun.simpleblog.model.Post;
import org.sersun.simpleblog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public Post createPost(Post post) {
        try {
            Post savedPost = postRepository.save(post);
            log.info("Post created, id: {}", savedPost.getPostId());
            return savedPost;
        } catch (Exception e) {
            log.error("Error creating post", e);
            throw e;
        }
    }
}
