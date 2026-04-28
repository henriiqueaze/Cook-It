package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.requests.ChangePasswordRequest;
import com.p5Project.cookIt.dtos.requests.LoginRequest;
import com.p5Project.cookIt.dtos.requests.RegisterRequest;
import com.p5Project.cookIt.dtos.responses.AuthResponse;
import com.p5Project.cookIt.entities.EmailVerificationToken;
import com.p5Project.cookIt.entities.PasswordResetToken;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.exceptions.EmailAlreadyInUseException;
import com.p5Project.cookIt.exceptions.BadRequestException;
import com.p5Project.cookIt.exceptions.EmailNotVerifiedException;
import com.p5Project.cookIt.exceptions.InvalidCredentialsException;
import com.p5Project.cookIt.exceptions.InvalidTokenException;
import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.exceptions.TokenExpiredException;
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

    @SuppressWarnings("unused")
    @Value("${app.email-verification-base-url}")
    private String emailVerificationBaseUrl;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        ensureEmailAvailable(request.email());

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmailVerified(false);

        userRepository.save(user);
        sendVerificationEmail(user);

        return new AuthResponse(userMapper.toDTO(user), null);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = findUserByEmail(request.email());

        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException("Email not verified");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return new AuthResponse(userMapper.toDTO(user), jwtService.generateToken(user.getId()));
    }

    @Transactional
    public void forgotPassword(String email) {
        User user = findUserByEmail(email);

        passwordResetTokenRepository.deleteAllByUser(user);

        String code = generateResetCode();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(code);
        resetToken.setUser(user);
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));

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
        PasswordResetToken resetToken = requireValidResetToken(token);

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }

    @Transactional
    public void changePassword(String userId, ChangePasswordRequest request) {
        User user = findUserById(userId);

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid current password");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public String confirmEmail(String token) {
        EmailVerificationToken verificationToken = requireValidVerificationToken(token);

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        emailVerificationTokenRepository.deleteAllByUser(user);

        return "Email confirmado com sucesso.";
    }

    private void sendVerificationEmail(User user) {
        emailVerificationTokenRepository.deleteAllByUser(user);

        String token = UUID.randomUUID().toString();

        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiresAt(LocalDateTime.now().plusHours(24));

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
        requireValidResetToken(token);
    }

    private void ensureEmailAvailable(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use");
        }
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private EmailVerificationToken requireValidVerificationToken(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token expired");
        }

        return verificationToken;
    }

    private PasswordResetToken requireValidResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Código inválido"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Código expirado");
        }

        return resetToken;
    }
}