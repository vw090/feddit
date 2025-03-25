package com.vweinert.fedditbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vweinert.fedditbackend.models.Post;

public interface PostRepository extends JpaRepository<Post,Long> {
    @Query(value="select * from posts WHERE deleted = false order by created_at desc limit 1",nativeQuery = true)
    Optional<Post> findMostRecent();
    
    @Query(value="select * from posts WHERE deleted = false order by created_at desc limit 10", nativeQuery = true)
    List<Post> findTenMostRecent();
}
