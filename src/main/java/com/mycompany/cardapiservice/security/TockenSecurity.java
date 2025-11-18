package com.mycompany.cardapiservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;

/**
 * Основные методы для создания и проверки валидности токена пользователя
 */
public class TockenSecurity {
     // Секретный ключ для подписи токена
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    
    // Время жизни токена (30 минут)
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

    /**
     * Создает JWT токен с логином и ролью пользователя
     */
    public static String createToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(username)              // Логин пользователя
                .claim("role", role)            // Роль пользователя
                .issuedAt(now)                  // Время создания
                .expiration(expiryDate)         // Время истечения
                .signWith(SECRET_KEY)           // Подписываем ключом
                .compact();                     // Преобразуем в строку
    }

    /**
     * Проверяет валидность токена и извлекает логин
     */
    public static String validateTokenAndGetUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)     // Указываем ключ для проверки
                    .build()                    // Собираем парсер
                    .parseSignedClaims(token)   // Парсим и проверяем подпись
                    .getPayload();              // Получаем данные из payload

            // Проверяем не истек ли срок действия токена
            if (claims.getExpiration().before(new Date())) {
                throw new Exception("Токен истек");
            }

            return claims.getSubject();
            
        } catch (Exception e) {
            throw new RuntimeException("Невалидный токен: " + e.getMessage());
        }
    }

    /**
     * Извлекает роль пользователя из токена
     */
    public static String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return claims.get("role", String.class);
    }

    /**
     * Проверяет валиден ли токен
     */
    public static boolean isTokenValid(String token) {
        try {
            validateTokenAndGetUsername(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
