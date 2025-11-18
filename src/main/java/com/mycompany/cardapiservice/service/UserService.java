package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.auth.AuthorizationDto;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.repository.UserRepository;
import com.mycompany.cardapiservice.security.TockenSecurity;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserService {
    private UserRepository userRepository;
    
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    
    /**
     * Проверка на совпадение логина и пароля из БД
     * @param userAuthData Обьект, хранящий логин и пароль для входа.
     * @return true - если данные совпали false - если нет
     */
    public String findUserByAuthData(AuthorizationDto userAuthData)
    {
        Optional<User> currentUser = userRepository.findByLogin(userAuthData.getLogin());
        
        if ((!currentUser.isEmpty()) && (currentUser.get().getPassword().equals(userAuthData.getPassword())))
        {
            // Создаем токен
            String token = TockenSecurity.createToken("ivan", "admin");
            
            return token;
        } else {
            return null;
        }
    }
}
