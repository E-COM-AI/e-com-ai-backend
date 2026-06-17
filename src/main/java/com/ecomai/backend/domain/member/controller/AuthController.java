package com.ecomai.backend.domain.member.controller;

import com.ecomai.backend.domain.member.dto.request.LoginRequest;
import com.ecomai.backend.domain.member.dto.request.SignupRequest;
import com.ecomai.backend.domain.member.dto.response.AuthResponse;
import com.ecomai.backend.domain.member.service.AuthService;
import com.ecomai.backend.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(
            @Valid @RequestBody SignupRequest request
    ) {

        authService.signup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {

        return ResponseEntity
                .ok(ApiResponse.success(authService.login(request)));
    }
}
