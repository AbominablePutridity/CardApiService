package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Класс для хранения пароля пользователя как DTO
 * (для передачи в роут регистрации)
 */
public class PasswordKeeperDto extends UserDto {
    private String password;

    // Обязательный конструктор без параметров (для серелизатора)
    public PasswordKeeperDto() {
        super();
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Сохранение пользователя при регистрации (для неавторезованных "Гостей" приложения).
     * @param user Обьект пользователя для сохранения.
     * @param passwordEncoder Обьект для шифрования пароля в BCrypt
     * @return Готовый обьект для сохранения.
     */
    public User setDataInObject(User user, PasswordEncoder passwordEncoder) {
        user.setName(super.getName());
        user.setSurname(super.getSurname());
        user.setRole(Role.ROLE_USER.getValue()); // у запи, создаваемой пользователем роль всегда по умолчанию будет USER
        // (пока админ не изменит ее)
        user.setLogin(super.getLogin());
        
        // сохраняем хешированный пароль (в BCrypt)
        user.setPassword(passwordEncoder.encode(password));
        
        user.setIsBlocked(true); //всегда для пользователя, который пытается зарегестрироваться ставим блокировку
        //разблокирует админ после модерации в PUT/PATCH запросах
        
        return user;
    }
}
