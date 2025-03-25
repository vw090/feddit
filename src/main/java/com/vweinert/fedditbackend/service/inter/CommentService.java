package com.vweinert.fedditbackend.service.inter;

import com.vweinert.fedditbackend.models.Comment;

public interface CommentService {
    public Comment createComment(long userId, long postId, Comment commentRequest)throws Exception;
    public Comment getComment(long commentId)throws Exception;
    public Comment updateComment(long userId,Comment commentRequest)throws Exception;
    public Comment deleteComment(long userId, long commentId)throws Exception;
}
