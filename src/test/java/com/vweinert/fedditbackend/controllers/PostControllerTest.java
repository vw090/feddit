package com.vweinert.fedditbackend.controllers;

import com.vweinert.fedditbackend.dto.AuthDto;
import com.vweinert.fedditbackend.dto.PostDto;
import com.vweinert.fedditbackend.models.Post;
import com.vweinert.fedditbackend.models.User;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Rollback
public class PostControllerTest {
    @Autowired
    private AuthController authController;
    @Autowired
    private PostController postController;

    private final String testEmail1 = "emailyabc@gmail.com";
    private final String testUsername1 =  "usernamabc   e";
    private final String testPassword = "password123";
    private String jwt;
    private User user;


//    @WithUserDetails(value = testUsername1)
    @Test
    public void testUserSignupAndPost() {
        user = User.builder()
                .email(testEmail1)
                .username(testUsername1)
                .password(testPassword)
                .build();
        ResponseEntity<AuthDto> goodResponse =  (ResponseEntity<AuthDto>) authController.registerUser(user);
        assertThat(goodResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        AuthDto body = goodResponse.getBody();
        assert body != null;
        assertThat(body.getUsername()).isEqualTo(testUsername1);
        assertThat(body.getId()).isNotNull();
        assertThat(body.getJwt()).isNotEmpty();
        assertThat(body.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        this.jwt = body.getJwt();

        String jwt = this.jwt;
        System.out.println(jwt);
        Post post = Post.builder()
                .title("hello world")
                .content("this is some post's content")
                .build();
        ResponseEntity<PostDto> postDtoResponseEntity = (ResponseEntity<PostDto>) postController.postPost(jwt,post);
        assertThat(postDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        PostDto postBody = postDtoResponseEntity.getBody();
        assert postBody != null;

        assert !postBody.isDeleted();
        assert Objects.equals(postBody.getUser().getId(), goodResponse.getBody().getId());

    }

}
