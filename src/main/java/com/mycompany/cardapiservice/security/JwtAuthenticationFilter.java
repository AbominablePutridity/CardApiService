package com.mycompany.cardapiservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Фильтр JWT тоенов для внедрения в конфигурацию приложения для настройки JWT
 * @author User
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        System.out.println("=== JWT FILTER WORKING ===");
        System.out.println("Request: " + request.getRequestURI());
        
        //Берем заголовок с токеном пользователя
        String authHeader = request.getHeader("Authorization");
        System.out.println("Auth Header: " + authHeader);
        
        //Проверяем - если заголовок с токеном не null и начинается с "Barer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            //Урезаем в строке с токеном "Barer " для того чтобы получить только чистый токен
            String token = authHeader.substring(7);
            System.out.println("Token received: " + token.substring(0, 10) + "...");
            
            //проверяем токен на валидность
            if (TockenSecurity.isTokenValid(token)) {
                
                // если токен валиден то берем из него логин и роль и записываем в лист, который передаем в spring Security
                String username = TockenSecurity.validateTokenAndGetUsername(token);
                String role = TockenSecurity.getRoleFromToken(token);
                System.out.println("Token valid! User: " + username + ", Role: " + role);
                
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Authentication set in SecurityContext");
            } else {
                System.out.println("Token invalid!");
            }
        } else {
            System.out.println("No Bearer token found");
        }
        
        filterChain.doFilter(request, response);
    }
}