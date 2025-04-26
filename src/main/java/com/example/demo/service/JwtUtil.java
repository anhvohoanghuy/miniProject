package com.example.demo.service;

import com.example.demo.model.Claim;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "yourverystrongsecretkeywhichisatleast32bytes";
    private static final SecretKey SECRET = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    //Tạo jwt từ userName
    public String generateToken(String useName, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(useName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET).build().parseClaimsJwt(token).getBody();
    }

    public String getUserName(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    public List<String> getRole(String token) {
        Claims claim = getClaimsFromToken(token);
        return claim.get("role", List.class);
    }

    public Date getExpiration(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    public boolean validateToken(String token) {
        try {
            if (getExpiration(token).after(new Date())){
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
