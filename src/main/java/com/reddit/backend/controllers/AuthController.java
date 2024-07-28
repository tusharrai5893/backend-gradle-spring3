package com.reddit.backend.controllers;

import com.reddit.backend.domain.JwtAuthResDto;
import com.reddit.backend.domain.LoginRequestDto;
import com.reddit.backend.domain.RefreshTokenRequestDto;
import com.reddit.backend.domain.RegisterRequestDto;
import com.reddit.backend.service.AuthService;
import com.reddit.backend.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final RefreshTokenService refreshTokenService;

    @PostMapping(value = "/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterRequestDto registerRequestDto) {
        //authService.signup(registerRequestDto);
        return authService.signup(registerRequestDto)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @GetMapping(value = "/verifyAccount/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.mailVerifyAccount(token);
        return ResponseEntity.ok("<h1>User Activated Successfully !!</h1>");
    }

    @PostMapping(value = "/login")
    public ResponseEntity<JwtAuthResDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping(value = "/refreshToken")
    public ResponseEntity<JwtAuthResDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequestDto));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequestDto.getRefreshToken());
        return ResponseEntity.status(200).body("Sorry to see you have been logged out.");
    }


}
