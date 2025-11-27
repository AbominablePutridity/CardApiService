package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.UserDto;
import com.mycompany.cardapiservice.dto.auth.AuthorizationDto;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.repository.UserRepository;
import com.mycompany.cardapiservice.security.TockenSecurity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
    
    /**
     * Возвращает список пользователей по фильтрам.
     * @param login Логин пользователя.
     * @param surname Фамилия пользователя.
     * @param name Имя пользователя.
     * @param patronymic Отчество пользователя.
     * @param pageable Пагинация.
     * @return Лист пользователей по фильтрации.
     */
    public List<UserDto> getAllUsersByFilter(String login, String surname, String name, String patronymic, Pageable pageable)
    {
        Page<User> page = userRepository.findAllByFilters(login, surname, name, patronymic, pageable);
        
        return page.stream().map((user) -> {
            return new UserDto(user);
        })
        .collect(Collectors.toList());
    }
    
    /**
     * Обновить пользователя (заменить данные обьекта, или его целиком - в зависимости от isSaveByPart).
     * @param idUserForUpdate Id пользователя для обновления.
     * @param refreshedUser Обьект пользователя доля обновления.
     * @param isSaveByPart 
     * true - обновить конкретное поле/поля в данном обьекте (не целый обьект - для PATCH);
     * false - обновить обьект целиком (для PUT).
     * @return Статус выполнения.
     */
    public ResponseEntity<?> refreshUser(Long idUserForUpdate, UserDto refreshedUser, boolean isSaveByPart)
    {
        try {
            User user = userRepository.findById(idUserForUpdate).get();
        
            if (isSaveByPart) {
                if(refreshedUser.getSurname() != null)
                {
                    user.setSurname(refreshedUser.getSurname());
                }

                if(refreshedUser.getName() != null)
                {
                    user.setName(refreshedUser.getName());
                }

                if(refreshedUser.getPatronymic() != null)
                {
                    user.setPatronymic(refreshedUser.getPatronymic());
                }

                if(refreshedUser.getRole() != null)
                {
                    user.setRole(refreshedUser.getRole());
                }

                if(refreshedUser.getLogin() != null)
                {
                    user.setLogin(refreshedUser.getLogin());
                }
            } else {
                user.setSurname(refreshedUser.getSurname());
                user.setName(refreshedUser.getName());
                user.setPatronymic(refreshedUser.getPatronymic());
                user.setRole(refreshedUser.getRole());
                user.setLogin(refreshedUser.getLogin());
            }
            userRepository.save(user);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: UserService.refreshFullUser() проверка полей вызвало исключение: " + t.getMessage());
            
            return ResponseEntity.badRequest()
               .body("Ошибка при обновлении пользователя: " + t.getMessage());
        }
        
        return ResponseEntity.ok("пользователь успешно обновлен!");
    }
    
    /**
     * Метод для удаления пользователя.
     * @param userId Id код для удаления пользователя.
     * @return Статус выполнения.
     */
    public ResponseEntity<?> deleteUser(Long userId)
    {
        try {
            User user = userRepository.findById(userId).get();

            userRepository.delete(user);
            
            return ResponseEntity.ok("Пользователь был успешно удален!");
        }
        catch (Throwable t) {
            System.err.println("ОШИБКА: UserService.ddeleteUser() - при удалении пользователя: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("ОШИБКА: UserService.ddeleteUser() - при удалении пользователя: " + t.getMessage());
        }
    }
    
    /**
     * Взять пользователя по его id. (для того чтобы брать обьекты по id в post-запросах)
     * @param id Id получаемого пользователя.
     * @return Обьект пользователя
     */
    public User getUserById(Long id)
    {
        return userRepository.findById(id).get();
    }
    
    /**
     * Взять пользователя по логину или его id.
     * @param id Id пользователя.
     * @param login Логин пользователя.
     * @return Обьект пользователя.
     */
    public UserDto getUserByIdOrLogin(Long id, String login)
    {
        User user = null;
        
        if (id != null && login == null) {
            user = getUserById(id);
        }
        
        if (login != null && id == null) {
            user = userRepository.findByLogin(login).get();
        }
        
        return new UserDto(user);
    }
}
