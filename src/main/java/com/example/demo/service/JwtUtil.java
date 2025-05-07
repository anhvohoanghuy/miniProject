package com.example.demo.service;

import com.example.demo.model.Claim;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "yourverystrongsecretkeywhichisatleast32bytes";
    private static final SecretKey SECRET = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //Tạo jwt từ userName
    public String generateAccessToken(String useName, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(useName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(SECRET)
                .compact();
    }

    public String generateRefeshToken(String useName) {
        Instant now = Instant.now();
        Instant expiry = now.plus(30, ChronoUnit.DAYS); // 30 ngày
        String tokenId = UUID.randomUUID().toString();
        saveRefreshToken(useName, tokenId, expiry);
        return Jwts.builder()
                .setSubject(useName)
                .claim("type", "refresh")
                .setId(tokenId) // jti
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(SECRET)
                .compact();

    }

    public void saveRefreshToken(String useName, String id, Instant expiry) {
        String key = "refresh_token: " + id;
        Duration ttl = Duration.between(Instant.now(), expiry);
        redisTemplate.opsForValue().set(key, useName, ttl);
    }

    public void deleteRefreshToken(String id) {
        String key = "refresh_token: " + id;
        redisTemplate.delete(key);
    }

    public boolean validateRefreshToken(String id) {
        String key = "refresh_token: " + id;
        return redisTemplate.hasKey(key);
    }

    public String getRefreshTokenId(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getId();
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // 👉 Token hết hạn nhưng vẫn muốn lấy claims
            return e.getClaims();
        } catch (Exception e) {
            // 👉 Các lỗi khác: sai chữ ký, token không hợp lệ
            return null;
        }
    }

    public String getUserName(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    public List<String> getRole(String token) {
        Claims claim = getClaimsFromToken(token);
        return claim.get("roles", List.class);
    }

    public Date getExpiration(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    public boolean validateToken(String token) {
        try {
            if (getExpiration(token).after(new Date())) {
                return true;
            }
        } catch (
                ExpiredJwtException e) {
            System.out.println("Token đã hết hạn!");
        } catch (
                UnsupportedJwtException e) {
            System.out.println("Token không được hỗ trợ!");
        } catch (
                MalformedJwtException e) {
            System.out.println("Token không hợp lệ!");
        } catch (IllegalArgumentException e) {
            System.out.println("Token rỗng hoặc null!");
        }
        return false;
    }
}
