package org.sersun.simpleblog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "post")
public class Post {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @NotEmpty
    @Column(name = "title", nullable = false)
    private String title;

    @NotEmpty
    @Column(name = "subtitle", nullable = false)
    private String subtitle;

    @NotEmpty
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "post_image")
    private byte[] postImage;

    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    private List<String> tagList;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @PrePersist
    protected void onCreate(){
        createdDate = LocalDateTime.now();
    }

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Column(name = "publication_status", columnDefinition = "publication_status DEFAULT 'Draft'")
    @Enumerated(EnumType.STRING)
    private PublicationStatus publicationStatus;

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();

        if (publicationStatus == PublicationStatus.PUBLISHED && publishedDate == null) {
            publishedDate = LocalDateTime.now();
        }
    }

    @Column(name = "like_counter", columnDefinition = "INT DEFAULT 0")
    private int likeCounter;



    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", content='" + content + '\'' +
                ", tagList=" + tagList +
                ", user=" + user +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", publishedDate=" + publishedDate +
                ", publicationStatus=" + publicationStatus +
                ", likeCounter=" + likeCounter +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        //return postId != null ? postId.equals(post.postId) : post.postId == null;
        return Objects.equals(postId, post.postId);
    }

    @Override
    public int hashCode() {
        return postId != null ? postId.hashCode() : 0;
    }
}
