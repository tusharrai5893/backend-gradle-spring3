package com.reddit.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JTokenOPRFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    private final JwtProviderService jwtProviderService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        //Parsing JWT from Oncoming Request
        var authorization = request.getHeader(AUTHORIZATION);

        if (null == authorization || !StringUtils.hasText(AUTHORIZATION)) {
            filterChain.doFilter(request, response);
        } else {
            var jwtFromComingRequest = authorization.substring(7);
            // Validating Jwt with public key , getting certificate from it
            boolean validateJwtToken = jwtProviderService.isTokenExpired(jwtFromComingRequest);
            // Extracting Username from UserDetails from its serviceImpl , adding it to Context

            if (StringUtils.hasText(jwtFromComingRequest) && validateJwtToken) {
                String username = jwtProviderService.claims(jwtFromComingRequest).getSubject();

                var userDetails = userDetailsService.loadUserByUsername(username);

                var authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(request, response);
            }
        }//ends Else
    }
}
