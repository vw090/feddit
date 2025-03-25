package com.vweinert.fedditbackend.models;

import java.time.LocalDateTime;
import java.util.List;

import com.vweinert.fedditbackend.request.post.PostPost;
import com.vweinert.fedditbackend.request.post.PutPost;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="posts", indexes = {@Index(name="on_created_at", columnList = "createdAt DESC")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false,updatable = false)
    @NotBlank(groups = {PostPost.class},message = "Missing password")
    private String title;
    @Column(nullable=false,columnDefinition = "text")
    @NotBlank(groups = {PutPost.class, PostPost.class},message = "Missing password")
    private String content;
    @Column(nullable=false,updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
    @Column(nullable=false)
    @Builder.Default
    private Boolean deleted = false;
}
