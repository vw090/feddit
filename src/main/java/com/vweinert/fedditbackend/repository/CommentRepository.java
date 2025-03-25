package com.vweinert.fedditbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vweinert.fedditbackend.models.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Optional<Comment> getCommentById(Long id);
}