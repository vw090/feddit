package com.vweinert.fedditbackend.models;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Transient;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.vweinert.fedditbackend.request.auth.LoginRequest;
import com.vweinert.fedditbackend.request.auth.SignupRequest;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false,unique = true)
    @NotBlank(groups = {SignupRequest.class}, message = "email is blank")
    @Email(groups = {SignupRequest.class}, message = "invalid email")
    private String email;
    @Column(nullable=false,updatable = false,unique = true)
    @NotBlank(groups = {LoginRequest.class, SignupRequest.class},message = "username is blank")
    @Size(min = 8, max = 32, groups = {LoginRequest.class, SignupRequest.class}, message = "username must be between 8 and 32 characters")
    private String username;
    @Column(nullable=false)
    @NotBlank(groups = {LoginRequest.class, SignupRequest.class},message = "Missing password")
    @Size(min = 8, max = 32, groups = {LoginRequest.class, SignupRequest.class}, message = "password must be between 8 and 32 characters")
    private String password;
    @Column(columnDefinition = "text")
    private String about;
    @Column(nullable=false,updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime passwordChangedAt;
    private LocalDateTime aboutChangedAt;
    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted = false;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
  
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private Set<Post> posts;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private Set<Comment> comments;
    @Transient
    private String jwt;
    @Transient
    @Size(min = 8, max = 32,message = "new password must be between 8 & 32 characters long")
    private String newPassword;
    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
