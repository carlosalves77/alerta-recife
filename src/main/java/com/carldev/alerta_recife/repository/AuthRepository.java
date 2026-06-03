package com.carldev.alerta_recife.repository;

import com.carldev.alerta_recife.entity.UserAuth;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<UserAuth, UUID> {

    Optional<UserAuth> findUserByEmail(String username);

    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE UserAuth u SET u.lastLoginAt = :lastLogin WHERE u.id = :id")
    void updateLastLoginById(@Param("id") UUID id,
                            @Param("lastLogin") Instant lastLogin);

}
