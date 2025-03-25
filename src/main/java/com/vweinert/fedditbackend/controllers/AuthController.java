package com.vweinert.fedditbackend.controllers;

import com.vweinert.fedditbackend.security.jwt.JwtUtils;
import org.modelmapper.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.vweinert.fedditbackend.models.User;
import com.vweinert.fedditbackend.request.auth.LoginRequest;
import com.vweinert.fedditbackend.request.auth.SignupRequest;
import com.vweinert.fedditbackend.dto.AuthDto;
import com.vweinert.fedditbackend.service.inter.UserService;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    public AuthController(JwtUtils jwtUtils,UserService userService, ModelMapper modelMapper) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.modelMapper = modelMapper;
        logger.debug("Auth controller initialized");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@Validated(LoginRequest.class) @RequestBody User loginRequest) {
        try {
            logger.debug("logging in user {},",loginRequest);
            User user = userService.signInUser(loginRequest);
            AuthDto authDto = modelMapper.map(user, AuthDto.class);
            return ResponseEntity.ok(authDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Validated(SignupRequest.class) @RequestBody User signUpRequest) {
        try {
            logger.debug("signing up user {},", signUpRequest);
            User user = userService.registerUser(signUpRequest);
            AuthDto authDto = modelMapper.map(user, AuthDto.class);
            return ResponseEntity.ok(authDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping ("/whoami")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> whoami(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        long userId = jwtUtils.getUserIdFromJwtToken(authorization);
        try {
            User user = userService.whoami(userId);
            AuthDto authDto = modelMapper.map(user, AuthDto.class);
            return ResponseEntity.ok(authDto);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/updateUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody User user){
        long userId = jwtUtils.getUserIdFromJwtToken(authorization);
        try {
            User returned = userService.updateUser(userId,user);
            AuthDto authDto = modelMapper.map(returned, AuthDto.class);
            return ResponseEntity.ok(authDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
