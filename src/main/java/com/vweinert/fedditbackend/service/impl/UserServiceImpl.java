package com.vweinert.fedditbackend.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vweinert.fedditbackend.models.ERole;
import com.vweinert.fedditbackend.models.Role;
import com.vweinert.fedditbackend.models.User;
import com.vweinert.fedditbackend.exception.custom.ResourceNotFoundException;
import com.vweinert.fedditbackend.repository.RoleRepository;
import com.vweinert.fedditbackend.repository.UserRepository;
import com.vweinert.fedditbackend.security.jwt.JwtUtils;
import com.vweinert.fedditbackend.service.inter.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final Set<Role> userRoles;
    private final String admin = "admin";
    private final String adminEmail = "admin@admin.com";

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils,Set<Role> userRoles, @Value("${feddit.app.admin_password}") String adminPassword) throws ResourceNotFoundException {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.userRoles = new HashSet<>();
        for(ERole role: ERole.values()) {
            if (!roleRepository.existsByName(role)){
                logger.warn("adding role {} to role table in DB",role);
                roleRepository.save(new Role(role));
            }
        }
        Optional<Role> userRole = this.roleRepository.findByName(ERole.ROLE_USER);
        if (userRole.isPresent()) {
            this.userRoles.add(userRole.get());
        } else {
            throw new ResourceNotFoundException();
        }
        if (!userRepository.existsByUsername(admin)) {
            logger.warn("adding admin user to app based upon ${FEDDIT_ADMIN_PASSWORD} env variable");
            Set<Role> adminRole = new HashSet<>();
            if (!roleRepository.existsByName(ERole.ROLE_ADMIN)) {
                throw new ResourceNotFoundException();
            }
            adminRole.add(roleRepository.findByName(ERole.ROLE_ADMIN).get());
            User adminUser = User.builder()
                    .username(admin)
                    .email(adminEmail)
                    .password(encoder.encode(adminPassword))
                    .roles(adminRole)
                    .build();
            userRepository.save(adminUser);
        }
        logger.debug("user service initialized");
    }
    public boolean isUserDeleted(User user) throws Exception {
        if(user.getDeleted()){
            return true;
        }
        Optional<User> userFromRepo = userRepository.findById(user.getId());
        if (userFromRepo.isPresent()) {
            return userFromRepo.get().getDeleted();
        } else {
            throw new ResourceNotFoundException("user not found");
        }
    }
    public User registerUser(User user) throws Exception {
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.info("someone tried to create user on username that already exists");
            throw new Exception("username already in use");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            logger.info("someone tried to create user on email that already exists");
            throw new Exception("email already in use");
        }

        User saving = User
                .builder()
                .about(user.getAbout())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(encoder.encode(user.getPassword()))
                .roles(userRoles)
                .build();
        User saved = userRepository.saveAndFlush(saving);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        saved.setJwt(jwt);
        logger.info("registering username {}, email {}",saved.getUsername(),saved.getEmail());
        return saved;
    }


    public User signInUser(User user) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        Optional<User> optionalFromRepo = userRepository.findByUsername(user.getUsername());
        if (optionalFromRepo.isPresent()) {
            User fromRepo = optionalFromRepo.get();
            logger.info("signing in  user {}",fromRepo.getUsername());
            fromRepo.setJwt(jwt);
            return fromRepo;
        } else {
            throw new Exception("an error occured");
        }
    }

    @Override
    public User updateUser(long id, User user) throws Exception {
        Optional<User> optionalFromRepo = userRepository.findById(id);
        if (optionalFromRepo.isPresent()) {
            User userFromRepo = optionalFromRepo.get();
            if (userFromRepo.getDeleted()) {
                throw new ResourceNotFoundException("user is deleted");
            } else {
                LocalDateTime now = LocalDateTime.now();
                if (user.getAbout() != null) {
                    userFromRepo.setAbout(user.getAbout());
                    userFromRepo.setAboutChangedAt(now);
                }
                if (user.getNewPassword() != null) {
                    if (user.getPassword() == null) {
                        throw new IllegalStateException("need old password when setting new password");
                    } else {
                        Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(userFromRepo.getUsername(), user.getPassword()));
                        userFromRepo.setPassword(encoder.encode(user.getNewPassword()));
                        userFromRepo.setPasswordChangedAt(now);
                        User saved = userRepository.save(userFromRepo);
                        Authentication authenticationAfterSave = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(userFromRepo.getUsername(), user.getNewPassword()));
                        saved.setJwt(jwtUtils.generateJwtToken(authenticationAfterSave));
                        return saved;
                    }
                }
                return SanitizerUtils.sanitizedUser(userRepository.saveAndFlush(userFromRepo));

            }
        } else {
            throw new ResourceNotFoundException("user has JWT but no user found w/ that id");
        }
    }

    @Override
    public User whoami(long id) throws Exception {
        Optional<User> optionalFromRepo = userRepository.findById(id);
        if (optionalFromRepo.isPresent()) {
            return SanitizerUtils.sanitizedUser(optionalFromRepo.get());
        } else {
            throw new ResourceNotFoundException("user has JWT but no user found w/ that id");
        }

    }
}
