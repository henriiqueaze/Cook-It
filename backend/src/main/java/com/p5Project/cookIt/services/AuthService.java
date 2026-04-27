package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.requests.LoginRequest;
import com.p5Project.cookIt.dtos.requests.RegisterRequest;
import com.p5Project.cookIt.dtos.responses.AuthResponse;
import com.p5Project.cookIt.entities.EmailVerificationToken;
import com.p5Project.cookIt.entities.PasswordResetToken;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.mappers.UserMapper;
import com.p5Project.cookIt.repository.EmailVerificationTokenRepository;
import com.p5Project.cookIt.repository.PasswordResetTokenRepository;
import com.p5Project.cookIt.repository.UserRepository;
import com.p5Project.cookIt.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final EmailService emailService;

    @Value("${app.email-verification-base-url:http://localhost:8080/api/auth/confirm-email?token=}")
    private String emailVerificationBaseUrl;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = createUser(request.getName(), request.getEmail(), request.getPassword());
        user.setEmailVerified(false);
        userRepository.save(user);

        createAndSendVerificationToken(user);
        return buildAuthResponse(user, false);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email not verified");
        }

        validatePassword(request.getPassword(), user.getPassword());
        return buildAuthResponse(user, true);
    }

    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        String token = UUID.randomUUID().toString();
        passwordResetTokenRepository.save(buildResetToken(user, token));

        // fazer a logica de enviar por email :)
        System.out.println("Reset token: " + token);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        validateTokenExpiration(resetToken);
        updateUserPassword(resetToken.getUser(), newPassword);
        passwordResetTokenRepository.delete(resetToken);
    }

    @Transactional
    public String confirmEmail(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        validateEmailVerificationTokenExpiration(verificationToken);

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        emailVerificationTokenRepository.deleteAllByUser(user);

        return "Email confirmado com sucesso. Agora você já pode fazer login.";
    }

    private User createUser(String name, String email, String rawPassword) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEmailVerified(false);
        return user;
    }

    private void createAndSendVerificationToken(User user) {
        emailVerificationTokenRepository.deleteAllByUser(user);

        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();

        emailVerificationTokenRepository.save(verificationToken);

        String link = emailVerificationBaseUrl + token;
        String subject = "Confirme seu email";
        String body = String.format(
                "Olá, %s!%n%n" +
                        "Obrigado por se cadastrar no Cook-It.%n" +
                        "Para confirmar seu email, acesse o link abaixo:%n%n" +
                        "%s%n%n" +
                        "Esse link expira em 24 horas.%n",
                user.getName(),
                link
        );

        emailService.sendEmail(user.getEmail(), subject, body);
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    private AuthResponse buildAuthResponse(User user, boolean issueToken) {
        AuthResponse response = new AuthResponse();
        response.setUser(userMapper.toDTO(user));
        response.setToken(issueToken ? jwtService.generateToken(user.getId()) : null);
        return response;
    }

    private PasswordResetToken buildResetToken(User user, String token) {
        return PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();
    }

    private void validateTokenExpiration(PasswordResetToken resetToken) {
        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }
    }

    private void validateEmailVerificationTokenExpiration(EmailVerificationToken verificationToken) {
        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Email verification token expired");
        }
    }

    private void updateUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}