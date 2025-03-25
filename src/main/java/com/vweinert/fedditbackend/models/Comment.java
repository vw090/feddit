package com.vweinert.fedditbackend.models;

import java.time.LocalDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.vweinert.fedditbackend.request.comment.PostComment;
import com.vweinert.fedditbackend.request.comment.PutComment;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull(groups = {PutComment.class})
    private Long id;
    @Column(nullable=false,columnDefinition = "text")
    @NotEmpty(groups = {PutComment.class, PostComment.class})
    private String content;
    @Column(nullable=false,updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="post_id", referencedColumnName = "id")
    private Post post;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
    @Column(nullable=false)
    @Builder.Default
    private Boolean deleted = false;
}
