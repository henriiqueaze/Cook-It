package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.requests.ChangePasswordRequest;
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

import java.security.SecureRandom;
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

    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.email-verification-base-url}")
    private String emailVerificationBaseUrl;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmailVerified(false);

        userRepository.save(user);
        sendVerificationEmail(user);

        AuthResponse response = new AuthResponse();
        response.setUser(userMapper.toDTO(user));
        response.setToken(null);
        return response;
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email not verified");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        AuthResponse response = new AuthResponse();
        response.setUser(userMapper.toDTO(user));
        response.setToken(jwtService.generateToken(user.getId()));
        return response;
    }

    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        passwordResetTokenRepository.deleteAllByUser(user);

        String code = generateResetCode();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(code)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();

        passwordResetTokenRepository.save(resetToken);

        String subject = "Recuperação de senha";
        String body = String.format(
                "Olá, %s!%n%n" +
                        "Seu código para redefinir a senha é:%n%n" +
                        "%s%n%n" +
                        "Esse código expira em 15 minutos.%n",
                user.getName(),
                code
        );

        emailService.sendEmail(user.getEmail(), subject, body);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }

    @Transactional
    public void changePassword(String userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public String confirmEmail(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        emailVerificationTokenRepository.deleteAllByUser(user);

        return "Email confirmado com sucesso.";
    }

    private void sendVerificationEmail(User user) {
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
                        "Esse link expira em 24 horas.",
                user.getName(),
                link
        );

        emailService.sendEmail(user.getEmail(), subject, body);
    }

    private String generateResetCode() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }

    @Transactional(readOnly = true)
    public void validateResetCode(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Código inválido"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Código expirado");
        }
    }
}