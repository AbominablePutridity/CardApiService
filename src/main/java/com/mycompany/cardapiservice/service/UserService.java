package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.auth.AuthorizationDto;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.repository.UserRepository;
import com.mycompany.cardapiservice.security.TockenSecurity;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserService {
    private UserRepository userRepository;
     private PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Проверка на совпадение логина и пароля из БД
     * @param userAuthData Обьект, хранящий логин и пароль для входа.
     * @return true - если данные совпали false - если нет
     */
    public String findUserByAuthData(AuthorizationDto userAuthData)
    {
        //System.out.println("10001 -> " + passwordEncoder.encode("password123").toString());
        
         Optional<User> currentUser = userRepository.findByLogin(userAuthData.getLogin());
        
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            
            //используем passwordEncoder!
            if (passwordEncoder.matches(userAuthData.getPassword(), user.getPassword())) {
                
                System.out.println("Login cur user = " + user.getLogin());
                System.out.println("Role cur user = " + user.getRole());

                // И используем реальные данные пользователя, а не хардкод!
                return TockenSecurity.createToken(user.getLogin(), user.getRole());
            }
        }
        return null;
    }
    
    /**
     * Возвращает обьект из БД текущего пользователя по его логину.
     * @param userLogin Логин текущего пользователя.
     * @return Обьект пользователя.
     */
    public User getCurrentUserByLogin(String userLogin)
    {
        Optional<User> currentUser = userRepository.findByLogin(userLogin);
        
        if(!currentUser.isEmpty()) {
            return currentUser.get();
        } else {
            System.out.println("UserService.getCurrentUserByLogin() -> ТЕКУЩИЙ ПОЛЬЗОВАТЕЛЬ ПУСТОЙ!!!");
            
            return null;
        }
    }
}
