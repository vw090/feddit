package com.vweinert.fedditbackend.service.inter;
import java.util.List;
import java.util.Optional;

import com.vweinert.fedditbackend.models.Post;

public interface PostService {
    Optional<Post> getMostRecentPost();
    List<Post> getTenMostRecentPosts();
    Post createPost(long userId, Post post) throws Exception;
    Post updatePost(long userId, long postId, Post post) throws Exception;
    Post deletePost(long userId, long postId) throws Exception;
	Post getPostById(long postId) throws Exception;
    boolean isPostDeleted(Post post) throws Exception;
}
