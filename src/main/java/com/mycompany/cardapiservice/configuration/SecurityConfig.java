package com.mycompany.cardapiservice.configuration;

import com.mycompany.cardapiservice.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Этот сервис - главное сердце безоапсности
 * Оно проверяет пользователя, его логин и пароль, внедряется в конфигах Security (SecurityConfig) для проверки
 * @author User
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // ДОБАВИТЬ JWT фильтр
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter)
    {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    
    // Кодировщик паролей (обязательно строим этот бин для шифрации в базе данных!)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // Настройка бина с доступам к страницам
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/user/login", "/public/**", "/static/**",
                            "/swagger-ui/**",       // ВСЕ страницы Swagger UI
                            "/v3/api-docs/**",      // OpenAPI спецификация
                            "/swagger-ui.html"     // Главная страница Swagger
                    )
                    .permitAll() // эти страницы доступны всем

                    .requestMatchers("/api/card/user/**").hasAnyRole("USER") // только для пользователей
                    .anyRequest().authenticated() // остальные только для вошедших
                )
                
                // ДОБАВЬ ЭТИ ДВЕ СТРОКИ:
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                
                .csrf(csrf -> csrf.disable()) //отключение csrf токенов для пост запросов (по умолчанию требует, иначе будет 403)
                .build();
    }
}
