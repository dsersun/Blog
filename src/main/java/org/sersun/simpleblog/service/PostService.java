package org.sersun.simpleblog.service;

import org.sersun.simpleblog.model.PublicationStatus;
import org.sersun.simpleblog.model.Users;
import org.sersun.simpleblog.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.sersun.simpleblog.model.Post;
import org.sersun.simpleblog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Validated
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final AsyncEmailService asyncEmailService;
    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       AsyncEmailService asyncEmailService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.asyncEmailService = asyncEmailService;
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

    @Transactional
    public void updatePost(Long postId, Post post) {
        try {
            Post existingPost = postRepository.findById(postId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with id " + postId + " not found"));
            post.setPostId(postId);
            Post updatedPost = postRepository.save(post);
            log.info("Post updated, id: {}", updatedPost.getPostId());
        } catch (Exception e) {
            log.error("Error updating post", e);
            throw e;
        }
    }

    @Transactional
    public void deletePost(Long postId) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with id " + postId + " not found"));
            postRepository.delete(post);
            log.info("Post deleted, id: {}", postId);
        } catch (Exception e) {
            log.error("Error deleting post", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Post getPostById(Long postId) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with id " + postId + " not found"));
            log.info("Post found, id: {}", postId);
            return post;
        } catch (Exception e) {
            log.error("Error getting post", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByUserId(Long userId) {
        try {
            List<Post> posts = postRepository.findAllByUserUserId(userId);
            log.info("Posts found, user id: {}", userId);
            return posts;
        } catch (Exception e) {
            log.error("Error getting posts by user id", e);
            throw e;
        }
    }

    @Transactional
    public void publishPost(Long postId) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with id " + postId + " not found"));
            post.setPublicationStatus(PublicationStatus.PUBLISHED);
            postRepository.save(post);
            log.info("Post published, id: {}", postId);
        } catch (Exception e) {
            log.error("Error publishing post", e);
            throw e;
        }
    }

    @Transactional
    public void unPublishPost(Long postId) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with id " + postId + " not found"));
            post.setPublicationStatus(PublicationStatus.DRAFT);
            postRepository.save(post);
            log.info("Post unpublished, id: {}", postId);
        } catch (Exception e) {
            log.error("Error unpublishing post", e);
            throw e;
        }
    }

    @Transactional
    public void likePost(Long postId) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with id " + postId + " not found"));
            post.setLikeCounter(post.getLikeCounter() + 1);
            postRepository.save(post);
            log.info("Post liked, id: {}", postId, "likeCounter: {}", post.getLikeCounter());
        } catch (Exception e) {
            log.error("Error liking post", e);
            throw e;
        }
    }

    @Transactional
    public void unLikePost(Long postId) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with id " + postId + " not found"));
            post.setLikeCounter(post.getLikeCounter() - 1);
            postRepository.save(post);
            log.info("Post unliked, id: {}", postId, "likeCounter: {}", post.getLikeCounter());
        } catch (Exception e) {
            log.error("Error unliking post", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByTagNamesContaining(String tagName) {
        try {
            List<Post> posts = postRepository.findAllByTagListContainingIgnoreCase(tagName);
            if (posts.isEmpty()) {
                log.info("No posts found for tag name: {}", tagName);
            } else {
                log.info("Found {} posts for tag name: {}", posts.size(), tagName);
            }
            return posts;
        } catch (Exception e) {
            log.error("Error getting posts by tag name", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByUserIdAndPublicationStatus(Long userId, PublicationStatus publicationStatus) {
        try {
            List<Post> posts = postRepository.findAllByPublicationStatusAndUserUserId(publicationStatus, userId);
            if (posts.isEmpty()) {
                log.info("No posts found for user id: {} and publication status: {}", userId, publicationStatus);
            } else {
                log.info("Found {} posts for user id: {} and publication status: {}", posts.size(), userId, publicationStatus);
            }
            return posts;
        } catch (Exception e) {
            log.error("Error getting posts by user id and publication status", e);
            throw e;
        }
    }

    @Transactional
    public void blockUser(Long userId) {
        try {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found"));
            int updatedCount = postRepository.updatePublicationStatusForUser(userId, PublicationStatus.REJECTED);
            asyncEmailService.sendEmail(user.getEmail(),
                    "Your Account has been blocked",
                    "Hi, " + user.getFirstname() + " " + user.getLastname() +
                    "\nYour account has been blocked.\n You cannot publish new posts and republish old ones.");
            log.info("User blocked, id: {}. Total posts updated: {}", userId, updatedCount);
        } catch (Exception e) {
            log.error("Error blocking user", e);
            throw e;
        }
    }

    @Transactional
    public void unblockUser(Long userId) {
        try {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found"));
            int updatedCount = postRepository.updatePublicationStatusForUser(userId, PublicationStatus.DRAFT);
            asyncEmailService.sendEmail(user.getEmail(),
                    "Your Account has been blocked",
                    "Hi, " + user.getFirstname() + " " + user.getLastname() +
                            "\nYour account has been unblocked.\nYou can now publish new posts and republish old ones.");
            log.info("User unblocked, id: {}. Total posts updated: {}", userId, updatedCount);
        } catch (Exception e) {
            log.error("Error unblocking user", e);
            throw e;
        }
    }

    // TODO: add other methods as:
    //  hidePostByAdmin, unHidePostByAdmin


}
