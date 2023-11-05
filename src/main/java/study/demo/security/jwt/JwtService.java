package study.demo.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    // create new access token with secret key and algorithm HS256
    public String generateTokenFromUserName(String userName) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    // create new refresh token as access token with the same jti and longer expiration time
    public String generateRefreshToken(String token) {
        final Claims tokenClaims = extractAllClaims(token);
        HashMap<String, Boolean> rfclaim = new HashMap<>();
        rfclaim.put("isRefreshToken",true);
        return Jwts.builder()
                .setClaims(rfclaim)
                .setId(tokenClaims.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenDurationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // get username from token 
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    
    //get claims from token
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    // create key with secret key for generating token
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    
    // check whether token is valid or not 
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // check expiration time of token 
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // get jti from token
    public String extractJti(String token) {
        return extractAllClaims(token).getId();
    }
    
    // check if the refresh token or not
    public boolean isRefrehToken(String token) {
        return extractAllClaims(token).get("isRefrehToken", Boolean.class);
    }
    
}
