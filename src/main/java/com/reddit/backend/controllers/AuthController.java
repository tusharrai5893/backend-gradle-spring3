package com.reddit.backend.controllers;

import com.reddit.backend.domain.JwtAuthResDto;
import com.reddit.backend.domain.LoginRequestDto;
import com.reddit.backend.domain.RefreshTokenRequestDto;
import com.reddit.backend.domain.RegisterRequestDto;
import com.reddit.backend.models.User;
import com.reddit.backend.service.AuthService;
import com.reddit.backend.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
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

    @GetMapping(value = "/verifyAccount")
    public ResponseEntity<String> verifyAccount(@RequestParam String token) {
        authService.mailVerifyAccount(token);
        return ResponseEntity.ok("User Activated Successfully !!");
    }

    @PostMapping(value = "/login")
    public ResponseEntity<JwtAuthResDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        JwtAuthResDto res = authService.login(loginRequestDto);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("JWT_TOKEN", res.getJwtToken());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(value = "/refreshToken")
    public ResponseEntity<JwtAuthResDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequestDto));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequestDto.getRefreshToken());
        log.info("Logout Successfully");
        return ResponseEntity.status(200).body("Sorry to see you have been logged out.");
    }

    @DeleteMapping("/delete/customer/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable String customerId) {
        User user = authService.deleteUser(customerId);
        return ResponseEntity.status(204).body(user.toString());
    }


}
