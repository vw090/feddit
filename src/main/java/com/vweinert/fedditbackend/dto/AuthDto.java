package com.vweinert.fedditbackend.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.vweinert.fedditbackend.models.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
    private Long id;
    private String email;
    private String username;
    private String about;
    private LocalDateTime createdAt;
    private LocalDateTime passwordChangedAt;
    private LocalDateTime aboutChangedAt;
    private String jwt;
    private Set<Role> roles;
    private Boolean deleted;
}
