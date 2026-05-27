package com.seoanalyzer.seo_tool.repository;

import com.seoanalyzer.seo_tool.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // SELECT * FROM users WHERE email = ? — used at login
    Optional<User> findByEmail(String email);

    // SELECT EXISTS WHERE email = ? — used at registration
    boolean existsByEmail(String email);
}