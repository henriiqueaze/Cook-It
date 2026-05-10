package com.p5Project.cookIt.repository;

import com.p5Project.cookIt.entities.PasswordResetToken;
import com.p5Project.cookIt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findTopByUserOrderByCreatedAtDesc(User user);

    void deleteAllByUser(User user);
}