package com.reddit.backend.security;

import com.reddit.backend.exceptions.RedditCustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProviderService jwtProviderService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        //Parsing JWT from Oncoming Request
        String jwtFromComingRequest = null;
        String containBearerToken = httpServletRequest.getHeader("Authorization");

        if (null == containBearerToken || !StringUtils.hasText("containBearerToken")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            jwtFromComingRequest = containBearerToken.substring(7);
            // Validating Jwt with public key , getting certificate from it
            boolean validateJwtToken = jwtProviderService.validateJwtToken(jwtFromComingRequest);
            // Extracting Username from UserDetails from its serviceImpl , adding it to Context

            if (StringUtils.hasText(jwtFromComingRequest) && validateJwtToken) {
                String usernameFromJwt = Optional.of(jwtProviderService.getUsernameFromJwt(jwtFromComingRequest))
                        .orElseThrow(() -> new RedditCustomException("Error in getting username from JWT"));
                UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromJwt);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        }//ends Else
    }
}
