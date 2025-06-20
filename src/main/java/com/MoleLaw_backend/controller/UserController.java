package com.MoleLaw_backend.controller;

import com.MoleLaw_backend.dto.AuthResponse;
import com.MoleLaw_backend.dto.SignupRequest;
import com.MoleLaw_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.MoleLaw_backend.dto.LoginRequest;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        String token = userService.signup(request);
        return ResponseEntity.ok().body(token); // 토큰 반환
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

}
