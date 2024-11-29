package com.Subhansh.DSO.DailyScheduleOrganiser.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private static final String secretKey = "Ay1aYZrnvODMaGzG8tRQb20NyFPQy8Hgpm3a2QDpFSc=";
    private final CustomUserDetailsService customUserDetailsService;

    // Logger for logging events
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public String generateToken(String username) {
        logger.info("Generating JWT token for user: {}", username);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        String token = createToken(claims, userDetails.getUsername());
        logger.info("JWT token generated successfully for user: {}", username);
        return token;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        logger.debug("Creating JWT token for subject: {}", subject);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        try {
            logger.info("Validating token for user: {}", username);
            String extractedUsername = extractUsername(token);
            boolean isValid = username.equals(extractedUsername) && !isTokenExpired(token);
            logger.info("Token validation result for user {}: {}", username, isValid ? "valid" : "invalid");
            return isValid;
        } catch (Exception e) {
            logger.error("Error validating token for user: {} - {}", username, e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        logger.debug("Checking if token is expired");
        return extractClaims(token).getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token) {
        logger.info("Getting authentication details for token");
        String username = extractUsername(token);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    String extractUsername(String token) {
        logger.debug("Extracting username from token");
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {
        logger.debug("Extracting claims from token");
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
    public List<String> extractRoles(String token) {
        logger.debug("Extracting roles from token");
        Claims claims = extractClaims(token);
        return (List<String>) claims.get("roles");
    }
}
