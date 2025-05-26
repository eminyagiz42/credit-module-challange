package com.eminyagiz.creditmodule.security;

import com.eminyagiz.creditmodule.model.entity.Role;
import com.eminyagiz.creditmodule.common.exception.AuthorizationTokenException;
import com.eminyagiz.creditmodule.model.entity.LoanAppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTUtil {

    @Value("${app.security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${app.security.jwt.expiration-time-millis}")
    private long jwtExpirationTimeAsMillis;

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Role extractUserType(String jwt) {
        return extractClaim(jwt, claims -> claims.get("USER_TYPE", Role.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Role role, LoanAppUser userDetails) {
        return buildToken(Map.of("USER_TYPE", role.getValue()), userDetails, jwtExpirationTimeAsMillis);
    }

    private String buildToken(Map<String, Object> extraClaims, LoanAppUser loanAppUser, long expiration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(String.valueOf(loanAppUser.getUserName()))
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(expiration, java.time.temporal.ChronoUnit.SECONDS)))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, LoanAppUser loanAppUser) {
        return (extractUsername(token).equals(loanAppUser.getUserName())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(getSignInKey())
                .build();
        try {
            return (Claims) jwtParser.parse(token).getPayload();
        } catch (Exception e) {
            throw new AuthorizationTokenException("Could not verify JWT token integrity!", e);
        }
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }
}
