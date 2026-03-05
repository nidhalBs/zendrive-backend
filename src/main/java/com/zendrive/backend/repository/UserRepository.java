package com.zendrive.backend.repository;

import com.zendrive.backend.entity.User;
import com.zendrive.backend.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByVerificationToken(String token);
    Optional<User> findByResetToken(String token);
    Page<User> findByRole(UserRole role, Pageable pageable);
    long countByRole(UserRole role);
    long countByEnabled(boolean enabled);
}
