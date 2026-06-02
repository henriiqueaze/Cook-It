package com.p5Project.cookIt.repository;

import com.p5Project.cookIt.entities.EmailVerificationToken;
import com.p5Project.cookIt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, String> {

    Optional<EmailVerificationToken> findByToken(String token);

    Optional<EmailVerificationToken> findTopByUserOrderByCreatedAtDesc(User user);

    void deleteAllByUser(User user);
}
