package com.vweinert.fedditbackend.controllers;
import static org.assertj.core.api.Assertions.assertThat;

import com.vweinert.fedditbackend.dto.AuthDto;
import com.vweinert.fedditbackend.models.User;
import com.vweinert.fedditbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;


@SpringBootTest
@Rollback
public class AuthControllerTest {
    @Autowired
    private AuthController authController;
    private final String testEmail1 = "emaily@gmail.com";
    private final String testEmail2 = "emailys@gmail.com";
    private final String testEmail3 = "emailyss@gmail.com";
    private final String testEmail4 = "emailysss@gmail.com";
    private final String testEmail5 = "emailyssss@gmail.com";
    private final String testUsername1=  "username";
    private final String testUsername2 = "username1";
    private final String testUsername3 = "username2";
    private final String testUsername4 = "username3";
    private final String testUsername5 = "username4";
    private final String testPassword = "password123";

    @Test
    public void testNewUserSignup() {

        User user = User.builder()
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
    }
    @Test
    public void testNewUserSignIn() {
        User user = User.builder()
                .email(testEmail2)
                .username(testUsername2)
                .password(testPassword)
                .build();
        ResponseEntity<AuthDto> goodResponse =  (ResponseEntity<AuthDto>) authController.registerUser(user);
        assertThat(goodResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        AuthDto body = goodResponse.getBody();
        assert body != null;
        assertThat(body.getUsername()).isEqualTo(testUsername2);
        assertThat(body.getId()).isNotNull();
        assertThat(body.getJwt()).isNotEmpty();
        assertThat(body.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        ResponseEntity<AuthDto> goodResponseSignIn = (ResponseEntity<AuthDto>) authController.loginUser(user);
        AuthDto bodySignIn = goodResponseSignIn.getBody();
        assert bodySignIn != null;
        assertThat(bodySignIn.getUsername()).isEqualTo(testUsername2);
        assertThat(bodySignIn.getId()).isNotNull();
        assertThat(bodySignIn.getJwt()).isNotEmpty();
        assertThat(bodySignIn.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());

    }
    @Test
    public void testDuplicateUsernameSignup(){
        User user = User.builder()
                .email(testEmail3)
                .username(testUsername3)
                .password(testPassword)
                .build();
        ResponseEntity<AuthDto> goodResponse =  (ResponseEntity<AuthDto>) authController.registerUser(user);
        assertThat(goodResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        AuthDto body = goodResponse.getBody();
        assert body != null;
        assertThat(body.getUsername()).isEqualTo(testUsername3);
        assertThat(body.getId()).isNotNull();
        assertThat(body.getJwt()).isNotEmpty();
        assertThat(body.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        user.setEmail(testEmail4);
        ResponseEntity<String> badResponse = (ResponseEntity<String>) authController.registerUser(user);
        assertThat(badResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(badResponse.getBody()).contains("username already in use");
    }
    @Test
    public void testDuplicateEmailSignup() {
        User user = User.builder()
                .email(testEmail4)
                .username(testUsername4)
                .password(testPassword)
                .build();
        ResponseEntity<AuthDto> goodResponse = (ResponseEntity<AuthDto>) authController.registerUser(user);
        assertThat(goodResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        AuthDto body = goodResponse.getBody();
        assert body != null;
        assertThat(body.getUsername()).isEqualTo(testUsername4);
        assertThat(body.getId()).isNotNull();
        assertThat(body.getJwt()).isNotEmpty();
        assertThat(body.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        user.setUsername(testEmail5);
        ResponseEntity<String> badResponse = (ResponseEntity<String>) authController.registerUser(user);
        assertThat(badResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(badResponse.getBody()).contains("email already in use");
    }
}
