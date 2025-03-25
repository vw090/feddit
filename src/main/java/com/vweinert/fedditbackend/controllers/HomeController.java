package com.vweinert.fedditbackend.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vweinert.fedditbackend.dto.PostDto;
import com.vweinert.fedditbackend.models.Post;
import com.vweinert.fedditbackend.service.inter.PostService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/home")
public class HomeController {
    String noPostsInDb = "no posts in db";

    private final PostService postService;
	private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    public HomeController(PostService postService, ModelMapper modelMapper) {
        this.postService = postService;
        this.modelMapper = modelMapper;
        logger.debug("home controller initialized");
    }
    @GetMapping("/mostRecent")
    public ResponseEntity<?> getMostRecent() {
        logger.debug("get most recent post");
        Optional<Post> post = postService.getMostRecentPost();
        if (post.isPresent()){
            PostDto postDto = modelMapper.map(post.get(), PostDto.class);
            return ResponseEntity.ok().body(postDto);
        } else {
            logger.warn("no posts in the database");
            return ResponseEntity.badRequest().body(noPostsInDb);
        }
        
    }
    @GetMapping("/TenMostRecent")
    public ResponseEntity<?> getTenMostRecent() {
        logger.debug("get ten most recent posts");
        List<Post> posts = postService.getTenMostRecentPosts();
        if (!posts.isEmpty()){
            List<PostDto> postsDtos = new ArrayList<>();
            for(Post post: posts) {
                postsDtos.add(modelMapper.map(post,PostDto.class));
            }
            return ResponseEntity.ok().body(postsDtos);
        } else {
            logger.warn("no posts in the database");
            return ResponseEntity.badRequest().body(noPostsInDb);
        }
    }
}
