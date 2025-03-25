package com.vweinert.fedditbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vweinert.fedditbackend.models.ERole;
import com.vweinert.fedditbackend.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
	
	boolean existsByName(ERole name);
}
