package com.example.demo.service;


import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws ServletException, IOException {
        // Bỏ qua các API không cần xác thực JWT
        String path = request.getServletPath();
        if (path.startsWith("/api/auth/login") ||
                path.startsWith("/api/auth/register")||
                path.startsWith("/api/auth/logout")||
                path.startsWith("/api/auth/refresh")
                || path.startsWith("/swagger") || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
            String token=authorizationHeader.substring(7);
            try {
                if(jwtUtil.validateToken(token)){
                    String userName= jwtUtil.getUserName(token);
                    List<String> role= jwtUtil.getRole(token);
                    //Đặt username vào security
                    List<GrantedAuthority> authorities = role.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userName,null,authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else {
                    logger.warn("Invalid JWT token");
                }
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
        filterChain.doFilter(request,response);
    }
}
