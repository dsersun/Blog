package org.sersun.simpleblog.repository;

import org.sersun.simpleblog.model.Post;
import org.sersun.simpleblog.model.PublicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserUserId(Long userId);

    List<Post> findAllByTagListContainingIgnoreCase(String tag);

    List<Post> findAllByPublicationStatusAndUserUserId(PublicationStatus publicationStatus, Long userId);

    @Modifying
    @Query("UPDATE Post p SET p.publicationStatus = :status WHERE p.user.userId = :userId")
    int updatePublicationStatusForUser(@Param("userId") Long userId, @Param("status") PublicationStatus status);


}
