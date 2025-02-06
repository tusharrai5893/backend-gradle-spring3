package com.reddit.backend.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionController implements AuthenticationEntryPoint {

    @Order(1)
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleException(ExpiredJwtException ex, HttpRequest request) {
        log.error(ex.getMessage());
       return new ResponseEntity<>(ex.getClaims().getExpiration().toString(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({Exception.class, RedditCustomException.class})
    public ResponseEntity<Object> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("Internal Server Error: {}", ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", HttpStatus.OK.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred"); // Generic message for security
        body.put("path", request.getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(response.getStatus());
        response.getWriter().write(response.getStatus() + "".toUpperCase() + authException.getCause());
    }
}
