package com.mycompany.cardapiservice.configuration;

import com.mycompany.cardapiservice.enums.Role;
import com.mycompany.cardapiservice.security.JwtAuthenticationFilter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

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
                 // Отключаем CORS (для фронта)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOriginPatterns(List.of("*"));
                    config.setAllowedMethods(List.of("*"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/users/guest/getEntryTocken",   // роут для входа пользователя (взятие токена)
                            "/public/**", "/static/**",
                            "/swagger-ui/**",       // ВСЕ страницы Swagger UI
                            "/v3/api-docs/**",      // OpenAPI спецификация
                            "/swagger-ui.html",     // Главная страница Swagger
                            "/api/users/guest/**"      // Роут с регистрацией пользователя
                            
                    )
                    .permitAll() // эти страницы доступны всем

                    .requestMatchers("/api/card/user/**").hasAnyRole(Role.ROLE_USER.getValue()) // только для пользователей
                        
                    .requestMatchers("/api/users/admin/**",
                            "/api/card/admin/**"
                    ).hasAnyRole(Role.ROLE_ADMIN.getValue()) // только для пользователей
                        
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
    
    // Добавим бин с аутентификацией по JWT
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
}
