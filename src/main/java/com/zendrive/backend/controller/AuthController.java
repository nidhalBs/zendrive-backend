package com.zendrive.backend.controller;

import com.zendrive.backend.dto.*;
import com.zendrive.backend.entity.User;
import com.zendrive.backend.mapper.UserMapper;
import com.zendrive.backend.security.JwtService;
import com.zendrive.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .user(userMapper.toDTO(user))
                .build();

        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody RegisterRequest request) {
        UserDTO user = userService.registerUser(request);
        return ResponseEntity.ok(ApiResponse.success("Registration successful", user));
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<ApiResponse<String>> confirmEmail(@RequestParam String token) {
        boolean confirmed = userService.confirmEmail(token);
        if (confirmed) {
            return ResponseEntity.ok(ApiResponse.success("Email confirmed successfully", null));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Invalid token"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(userMapper.toDTO(user)));
    }
}
