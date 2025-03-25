package com.vweinert.fedditbackend.service.impl;

import com.vweinert.fedditbackend.models.Comment;
import com.vweinert.fedditbackend.models.Post;
import com.vweinert.fedditbackend.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class SanitizerUtils {
    public static User sanitizedUser(User user) {
        return User
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .deleted(user.getDeleted())
                .jwt(user.getJwt())
                .build();
    }
    public static Post sanitizePost(Post post) {
        List<Comment> comments = new ArrayList<>();
        for(Comment comment:post.getComments()) {
            User userComment = sanitizedUser(comment.getUser());
            Comment newComment = Comment
                    .builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .deleted(comment.getDeleted())
                    .user(userComment)
                    .build();
            comments.add(newComment);
        }
        User user = sanitizedUser(post.getUser());
        return Post
                .builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .deleted(post.getDeleted())
                .user(user)
                .comments(comments)
                .build();
    }
    public static Comment sanitizeComment(Comment comment) {
        return Comment.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .deleted(comment.getDeleted())
                .user(sanitizedUser(comment.getUser()))
                .build();
    }
}
