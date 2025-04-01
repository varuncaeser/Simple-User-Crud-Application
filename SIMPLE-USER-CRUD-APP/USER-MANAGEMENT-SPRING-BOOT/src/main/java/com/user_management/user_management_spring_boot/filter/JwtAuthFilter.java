package com.user_management.user_management_spring_boot.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.user_management.user_management_spring_boot.service.JwtService;
import com.user_management.user_management_spring_boot.service.UserInfoService;

import java.io.IOException;

/**
 * This class is a custom filter for JWT (JSON Web Tokens) authentication in a Spring Boot application.
 * It extends the {@link OncePerRequestFilter} class and is responsible for validating and authenticating
 * incoming requests based on the presence of a valid JWT token in the request's Authorization header.
 *
 * @author PUSHKAR D
 * @version 1.0
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    /**
     * The {@link JwtService} instance used for token validation and extraction.
     */
    @Autowired
    private JwtService jwtService;

    /**
     * The {@link UserInfoService} instance used to load user details by username.
     */
    @Autowired
    @Lazy
    private UserInfoService userDetailsService;

    /**
     *
     * This method is called for each incoming request. It retrieves the Authorization header, extracts
     * the JWT token, and validates it. If the token is valid and no authentication is set in the context,
     * it sets up a new {@link UsernamePasswordAuthenticationToken} and authenticates the user.
     *
     * @param request The incoming HTTP request.
     * @param response The outgoing HTTP response.
     * @param filterChain The filter chain to continue processing the request.
     * @throws ServletException If an error occurs during the processing of the request.
     * @throws IOException If an error occurs during the processing of the request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Retrieve the Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check if the header starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extract token
            username = jwtService.extractUsername(token); // Extract username from token
        }

        // If the token is valid and no authentication is set in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate token and set authentication
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
