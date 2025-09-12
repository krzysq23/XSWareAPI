package pl.xsware.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.xsware.domain.model.auth.JwtType;
import pl.xsware.domain.model.auth.JwtValidation;
import pl.xsware.domain.model.user.UserDto;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.accessExpirationMs}")
    private long accessExpirationMs;
    @Value("${jwt.refreshExpirationMs}")
    private long refreshExpirationMs;

    private final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserDto user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessExpirationMs);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("roles", user.getRoles())
                .claim("tokenType", JwtType.ACCESS)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDto user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpirationMs);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("roles", user.getRoles())
                .claim("tokenType", JwtType.REFRESH)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    @SuppressWarnings("unchecked")
    public UserDto getUserFromJwtToken(String token) {
        Claims claim = Jwts.parser()
                .verifyWith(getSigningKey())
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return UserDto.builder()
                .id(claim.get("userId", Long.class))
                .email(claim.getSubject())
                .roles(claim.get("roles", List.class))
                .build();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Long getUserIdFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", Long.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("roles", List.class);
    }

    public JwtValidation validateJwtToken(String token, JwtType expectedType) {
        try {
            if(token == null) {
                return JwtValidation.NONE;
            }
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .clockSkewSeconds(60)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String tokenType = claims.get("tokenType", String.class);
            if (tokenType == null || !tokenType.equalsIgnoreCase(expectedType.name())) {
                return JwtValidation.INVALID;
            }
            return JwtValidation.VALID;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.warn("Token wygasł: {}", e.getClaims().getExpiration());
            return JwtValidation.EXPIRED;
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            log.warn("Nieprawidłowy token: {}", e.getMessage());
            return JwtValidation.INVALID;
        }
    }

}
