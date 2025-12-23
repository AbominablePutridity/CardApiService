package com.mycompany.cardapiservice.security;

import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.repository.UserRepository;
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
 * Фильтр JWT токенов для внедрения в конфигурацию приложения Spring Security.
 * Этот фильтр перехватывает каждый HTTP-запрос, извлекает JWT-токен из заголовка Authorization,
 * валидирует его и устанавливает контекст безопасности Spring.
 * @author User
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private UserRepository userRepository;
    
    public JwtAuthenticationFilter(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        System.out.println("=== JWT FILTER WORKING ===");
        System.out.println("Request URI: " + request.getRequestURI()); // Логируем URI запроса
        
        // 1. Берем заголовок Authorization с токеном пользователя
        String authHeader = request.getHeader("Authorization");
        
        //если заголовок с ключом Authorization (для постмана и сторонних систем) пустой, то берем заголовок из API эндпоинта
        if(authHeader == null) {
            authHeader = request.getHeader("X-Api-Token"); //токен из поя эндпоинта
        }
        
        System.out.println("Auth Header: " + (authHeader != null ? authHeader : "Header is null"));
        
        // 2. Проверяем наличие заголовка и его формат (должен начинаться с "Bearer ")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 3. Извлекаем чистый токен, убирая префикс "Bearer " (длина "Bearer " - 7 символов)
            String token = authHeader.substring(7);
            System.out.println("Token received: " + token.substring(0, Math.min(token.length(), 10)) + "...");
            
            // 4. Проверяем токен на валидность с помощью утилитарного класса TockenSecurity
            if (TockenSecurity.isTokenValid(token)) {
                
                // 5. Если токен валиден, извлекаем из него логин (username) и роль
                String username = TockenSecurity.validateTokenAndGetUsername(token);
                String role = TockenSecurity.getRoleFromToken(token);
                
                //Ищем пользователя по логину и проверяем заблокирован ли он
                User userObject = userRepository.findByLogin(username).get();
                if((userObject != null) && (!userObject.getIsBlocked()))
                {
                    System.out.println("Token valid! User: " + username + ", Role: " + role);

                    // 6. Создаем список GrantedAuthority (ролей/прав доступа) для Spring Security.
                    // Важно: Spring Security автоматически добавляет префикс ROLE_ при использовании hasAnyRole(),
                    // поэтому мы должны добавить его здесь вручную.
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                    // 7. Создаем объект аутентификации Spring Security
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                    // 8. Устанавливаем объект аутентификации в текущий контекст безопасности.
                    // Это позволяет остальной части приложения (включая SecurityConfig) знать, кто этот пользователь и какие у него роли.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("Authentication set in SecurityContext");
                } else {
                    System.out.println("This current user is blocked!");
                }
            } else {
                System.out.println("Token invalid!");
            }
        } else {
            System.out.println("No Bearer token found or incorrect format");
        }
        
        // 9. Продолжаем цепочку фильтров. Запрос пойдет дальше к контроллеру или другим фильтрам Spring Security.
        filterChain.doFilter(request, response);
    }
}
