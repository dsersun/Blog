package org.sersun.simpleblog.repository;

import org.sersun.simpleblog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByPostId(Long postId);

    List<Post> findAllByUserUserId(Long userId);

    List<Post> findAllByPublicationStatusName(String publicationStatus);
    List<Post> findAllByUserUserIdAndPublicationStatusName(Long userId, String publicationStatus);

    List<Post> findAllByTagListContains(String tag);

}
