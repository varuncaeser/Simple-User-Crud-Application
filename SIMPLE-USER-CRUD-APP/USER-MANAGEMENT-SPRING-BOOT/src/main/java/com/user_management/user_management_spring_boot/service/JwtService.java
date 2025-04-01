package com.user_management.user_management_spring_boot.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.user_management.user_management_spring_boot.entity.JwtAudit;
import com.user_management.user_management_spring_boot.repo.JwtAuditRepository;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * This class provides services related to JSON Web Tokens (JWT).
 * It includes methods for generating, validating, and extracting information
 * from JWT tokens.
 * @author PUSHKAR D
 * @version 1.0
 */
@Component
public class JwtService {

    @Autowired
    private JwtAuditService jwtAuditService;

    @Autowired
    private JwtAuditRepository jwtAuditRepository;

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    /**
     * Generate a JWT token with the given user name.
     *
     * @param userName The user name for which the token will be generated.
     * @return The generated JWT token.
     */
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        String token = createToken(claims, userName);

        jwtAuditService.saveTokenToDatabase(userName, token);

        return token;
    }

    /**
     * Create a JWT token with the specified claims and subject (user name).
     *
     * @param claims   The claims to be included in the token.
     * @param userName The user name to be set as the subject of the token.
     * @return The generated JWT token.
     */
    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Token valid for 30 minutes
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Get the signing key for JWT token.
     *
     * @return The signing key.
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extract the username from the token.
     *
     * @param token The JWT token from which the username will be extracted.
     * @return The username extracted from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract the expiration date from the token.
     *
     * @param token The JWT token from which the expiration date will be extracted.
     * @return The expiration date extracted from the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract a claim from the token.
     *
     * @param token          The JWT token from which the claim will be extracted.
     * @param claimsResolver A function that resolves the claim from the token.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from the token.
     *
     * @param token The JWT token from which all claims will be extracted.
     * @return The extracted claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Check if the token is expired.
     *
     * @param token       The JWT token to be validated.
     * @param userDetails The user details associated with the token.
     * @return True if the token is valid and not expired, false otherwise.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        Optional<JwtAudit> jwtAudit = jwtAuditRepository.findByToken(token);
        if (jwtAudit.isPresent() && jwtAudit.get().getIsValid()) {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }
        return false;
    }

    /**
     * Check if the token is expired.
     *
     * @param token The JWT token to be validated.
     * @return True if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Validate the token by ensuring it is valid.
     *
     * @param token The JWT token to be validated.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token); // Ensure the token is valid
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Invalidate the given JWT token by marking it as invalid in the database.
     * This method should be called when a user logs out or when a token needs to be revoked.
     *
     * @param token The JWT token to be invalidated.
     *
     * @throws IllegalArgumentException If the token is null or empty.
     */
    public void invalidateToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        jwtAuditService.revokeToken(token);
    }

}
