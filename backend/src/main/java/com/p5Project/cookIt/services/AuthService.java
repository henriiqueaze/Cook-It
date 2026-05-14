package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.requests.ChangePasswordRequest;
import com.p5Project.cookIt.dtos.requests.LoginRequest;
import com.p5Project.cookIt.dtos.requests.RegisterRequest;
import com.p5Project.cookIt.dtos.responses.AuthResponse;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.exceptions.EmailAlreadyInUseException;
import com.p5Project.cookIt.exceptions.InvalidCredentialsException;
import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.mappers.UserMapper;
import com.p5Project.cookIt.repository.UserRepository;
import com.p5Project.cookIt.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        ensureEmailAvailable(request.email());

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        // Emails/confirmation removed: mark verified by default
        user.setEmailVerified(true);

        userRepository.save(user);

        return new AuthResponse(userMapper.toDTO(user), null);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = findUserByEmail(request.email());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return new AuthResponse(userMapper.toDTO(user), jwtService.generateToken(user.getId()));
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
}