package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.requests.LoginRequest;
import com.p5Project.cookIt.dtos.requests.RegisterRequest;
import com.p5Project.cookIt.dtos.responses.AuthResponse;
import com.p5Project.cookIt.entities.PasswordResetToken;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.mappers.UserMapper;
import com.p5Project.cookIt.repository.PasswordResetTokenRepository;
import com.p5Project.cookIt.repository.UserRepository;
import com.p5Project.cookIt.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String token = jwtService.generateToken(user.getId());

        AuthResponse response = new AuthResponse();
        response.setUser(userMapper.toDTO(user));
        response.setToken(token);

        return response;
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getId());

        AuthResponse response = new AuthResponse();
        response.setUser(userMapper.toDTO(user));
        response.setToken(token);

        return response;
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder().token(token).user(user).expiresAt(LocalDateTime.now().plusHours(1)).build();

        passwordResetTokenRepository.save(resetToken);

        // fazer a logica de enviar por email :)
        System.out.println("Reset token: " + token);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }
}