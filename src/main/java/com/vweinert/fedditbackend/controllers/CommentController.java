package com.vweinert.fedditbackend.controllers;

import org.modelmapper.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpHeaders;

import com.vweinert.fedditbackend.models.Comment;
import com.vweinert.fedditbackend.security.jwt.JwtUtils;
import com.vweinert.fedditbackend.dto.CommentDto;
import com.vweinert.fedditbackend.request.comment.PostComment;
import com.vweinert.fedditbackend.request.comment.PutComment;
import com.vweinert.fedditbackend.service.inter.CommentService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final JwtUtils jwtUtils;
    private final CommentService commentService;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    public CommentController(JwtUtils jwtUtils, CommentService commentService, ModelMapper modelMapper) {
        this.jwtUtils = jwtUtils;
        this.commentService = commentService;
        this.modelMapper = modelMapper;
        logger.debug("comment controller initialized");
    }
    @PostMapping("/{strPostId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> postComment(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @Validated(PostComment.class) @RequestBody Comment commentRequest, @PathVariable String strPostId){
        long userId = jwtUtils.getUserIdFromJwtToken(authorization);

        try {
            long postId = getId(strPostId);
            logger.debug("user id {} comment on post id {}, comment {}",userId,strPostId,commentRequest);
            Comment comment = commentService.createComment(userId, postId, commentRequest);
            CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
            return ResponseEntity.ok().body(commentDto);
        } catch (Exception e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{strCommentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCommentById(@PathVariable String strCommentId){
        try {
            long commentId = getId(strCommentId);
            logger.debug("getting commentId {}",commentId);
            Comment comment = commentService.getComment(commentId);
            CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
            return ResponseEntity.ok().body(commentDto);
        } catch (Exception e ) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> putCommentById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @Validated(PutComment.class) @RequestBody Comment commentRequest) {
        long userId = jwtUtils.getUserIdFromJwtToken(authorization);
        try {
            logger.debug("userId {} put commend on comment id {} body {}",userId,commentRequest.getId(),commentRequest);
            Comment comment = commentService.updateComment(userId,commentRequest);
            CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
            return ResponseEntity.ok().body(commentDto);
        } catch (Exception e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{strCommentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteCommentById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String strCommentId) {
        long userId = jwtUtils.getUserIdFromJwtToken(authorization);
        try {
            long commentId = getId(strCommentId);
            logger.debug("userId {} delete comment of id {}",userId,commentId);
            Comment comment = commentService.deleteComment(userId,commentId);
            CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
            return ResponseEntity.ok().body(commentDto);
        } catch (Exception e ) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private long getId(String id) {
        return Long.parseLong(id);
    }
}
