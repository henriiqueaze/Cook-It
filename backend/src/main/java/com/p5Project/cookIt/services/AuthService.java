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

        ensurePasswordResetCooldown(user);

        passwordResetTokenRepository.deleteAllByUser(user);

        String code = generateResetCode();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(code);
        resetToken.setUser(user);
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));

        passwordResetTokenRepository.save(resetToken);

        String subject = "Recuperação de senha";
        String body = String.format(
            "<html><body style=\"margin:0;padding:0;background:linear-gradient(180deg,#fff7f3 0%%,#f6f3ee 100%%);font-family:Arial,sans-serif;color:#222;\">" +
                "<div style=\"padding:32px 16px;\">" +
                "<div style=\"max-width:720px;margin:0 auto;background:#fff;border-radius:22px;overflow:hidden;box-shadow:0 20px 60px rgba(0,0,0,0.12);border:1px solid rgba(243,65,0,0.08)\">" +
                "<div style=\"height:14px;background:linear-gradient(90deg,#ff7a45 0%%,#f34100 50%%,#ffb36b 100%%)\"></div>" +
                "<div style=\"padding:36px 34px 34px;text-align:center\">" +
                "<div style=\"display:inline-block;padding:8px 14px;border-radius:999px;background:rgba(243,65,0,0.08);color:#f34100;font-size:13px;font-weight:700;letter-spacing:1px;text-transform:uppercase;margin-bottom:18px\">Recuperação de senha</div>" +
                "<h1 style=\"margin:0 0 16px;font-size:28px;line-height:1.2;color:#1f1f1f\">Olá, %s!</h1>" +
                "<p style=\"margin:0 0 18px;color:#555;font-size:18px;line-height:1.7\">Seu código para redefinir a senha é:</p>" +
                "<div style=\"display:inline-block;font-size:30px;letter-spacing:6px;background:linear-gradient(180deg,#f9f9f9 0%%,#efefef 100%%);padding:18px 24px;border-radius:16px;margin-bottom:18px;font-weight:700;border:1px solid rgba(0,0,0,0.06);box-shadow:0 10px 24px rgba(0,0,0,0.06)\">%s</div>" +
                "<p style=\"margin:0;color:#777;font-size:16px;line-height:1.6\">Esse código expira em 15 minutos.</p>" +
                "</div></div></div></body></html>",
            user.getName(),
                    code
        );

        emailService.sendEmailWithInlineLogo(user.getEmail(), subject, body);
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

    @Transactional
    public void resendConfirmationEmail(String email) {
        User user = findUserByEmail(email);

        if (user.isEmailVerified()) {
            throw new BadRequestException("Email already verified");
        }

        ensureEmailVerificationCooldown(user);
        sendVerificationEmail(user);
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
            "<html><body style=\"margin:0;padding:0;background:linear-gradient(180deg,#fff7f3 0%%,#f6f3ee 100%%);font-family:Arial,sans-serif;color:#222;\">" +
                "<div style=\"padding:32px 16px;\">" +
                "<div style=\"max-width:720px;margin:0 auto;background:#fff;border-radius:22px;overflow:hidden;box-shadow:0 20px 60px rgba(0,0,0,0.12);border:1px solid rgba(243,65,0,0.08)\">" +
                "<div style=\"height:14px;background:linear-gradient(90deg,#ff7a45 0%%,#f34100 50%%,#ffb36b 100%%)\"></div>" +
                "<div style=\"padding:36px 34px 34px;text-align:center\">" +
                "<div style=\"display:inline-block;padding:8px 14px;border-radius:999px;background:rgba(243,65,0,0.08);color:#f34100;font-size:13px;font-weight:700;letter-spacing:1px;text-transform:uppercase;margin-bottom:18px\">Confirmação de email</div>" +
                "<h1 style=\"margin:0 0 16px;font-size:30px;line-height:1.2;color:#1f1f1f\">Olá, %s!</h1>" +
                "<p style=\"margin:0 0 18px;color:#555;font-size:18px;line-height:1.7\">Obrigado por se cadastrar no <strong>Cook-It</strong>.</p>" +
                "<p style=\"margin:0 0 18px\"><a href=\"%s\" style=\"display:inline-block;padding:16px 28px;background:linear-gradient(90deg,#ff7a45 0%%,#f34100 100%%);color:#fff;text-decoration:none;border-radius:14px;font-weight:700;font-size:18px;box-shadow:0 12px 28px rgba(243,65,0,0.24)\">Confirmar email</a></p>" +
                "<p style=\"margin:0 0 10px;color:#777;font-size:16px\">Ou copie e cole este link no seu navegador:</p>" +
                "<p style=\"word-break:break-all;color:#777;margin:0 0 14px;font-size:14px\"><a href=\"%s\" style=\"color:#f34100;text-decoration:none\">%s</a></p>" +
                    "<p style=\"margin:0;color:#777;font-size:14px\">Esse link expira em 24 horas.</p>" +
                "</div></div></div></body></html>",
            user.getName(), link, link, link
        );

        emailService.sendEmailWithInlineLogo(user.getEmail(), subject, body);
    }

    private void ensureEmailVerificationCooldown(User user) {
        emailVerificationTokenRepository.findTopByUserOrderByCreatedAtDesc(user)
                .ifPresent(token -> ensureCooldownExpired(token.getCreatedAt(), 5,
                        "Você já solicitou um novo e-mail de confirmação recentemente. Tente novamente em 5 minutos."));
    }

    private void ensurePasswordResetCooldown(User user) {
        passwordResetTokenRepository.findTopByUserOrderByCreatedAtDesc(user)
                .ifPresent(token -> ensureCooldownExpired(token.getCreatedAt(), 3,
                        "Você já solicitou uma redefinição de senha recentemente. Tente novamente em 3 minutos."));
    }

    private void ensureCooldownExpired(LocalDateTime createdAt, int cooldownMinutes, String message) {
        if (createdAt.plusMinutes(cooldownMinutes).isAfter(LocalDateTime.now())) {
            throw new BadRequestException(message);
        }
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