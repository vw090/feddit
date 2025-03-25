package com.vweinert.fedditbackend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.vweinert.fedditbackend.models.Post;
import com.vweinert.fedditbackend.models.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private User user;
    private Boolean deleted;

    private Post post;
}
