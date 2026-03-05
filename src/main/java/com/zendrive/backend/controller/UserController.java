package com.zendrive.backend.controller;

import com.zendrive.backend.dto.ApiResponse;
import com.zendrive.backend.dto.UserDTO;
import com.zendrive.backend.entity.User;
import com.zendrive.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> getProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(user.getId())));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(
            @RequestBody UserDTO userDTO,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(userService.updateUser(user.getId(), userDTO)));
    }
}
