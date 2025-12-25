package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.CardDto;
import com.mycompany.cardapiservice.dto.PasswordKeeperDto;
import com.mycompany.cardapiservice.dto.UserDto;
import com.mycompany.cardapiservice.dto.auth.AuthorizationDto;
import com.mycompany.cardapiservice.entity.Card;
import com.mycompany.cardapiservice.entity.Currency;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.repository.UserRepository;
import com.mycompany.cardapiservice.security.TockenSecurity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserService extends ExtendedUniversalWriteEndpointsService<User, UserDto, User, Long, JpaRepository<User, Long>>{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    )
    {
        super(userRepository); //передаем репозиторий в абстрактный класс, от которого унаследуемся здесь
        
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
            
            if(user.getIsBlocked())
            {
                return "К сожалению, данный пользователь является заблокированным: пожалуйста, обратитесь к администратору!";
            }
            
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
        
            //обновляем все поля кроме ключей и возвращаем обьект с данными
            User updatedUserObject = refreshedUser.toEntityWithFieldsCondition(user, isSaveByPart);
            
            userRepository.save(updatedUserObject);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: UserService.refreshUser() проверка полей вызвало исключение: " + t.getMessage());
            
            return ResponseEntity.badRequest()
               .body("Ошибка при обновлении пользователя: " + t.getMessage());
        }
        
        return ResponseEntity.ok("пользователь успешно обновлен!");
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
            user = getObjectById(id);
        }
        
        if (login != null && id == null) {
            user = userRepository.findByLogin(login).get();
        }
        
        return new UserDto(user);
    }
    
    /**
     * Зарегестрировать нового пользователя в системе по данным из формы (с паролем).
     * @param newUserData Данные о новом пользователе в системе.
     * @return Статус выполнение.
     */
    public ResponseEntity<?> registerNewUser(PasswordKeeperDto newUserData)
    {
        try {
            if(userRepository.findByLogin(newUserData.getLogin()).isPresent()) {
                return ResponseEntity.badRequest()
                    .body("Ошибка ввода данных: Пользователь с таким логином существует! Попробуйте зарегестрироваться под другим логином.");
            }
            
            User newUser = newUserData.setDataInObject(new User(), passwordEncoder);

            userRepository.save(newUser);
        } catch (Throwable t)
        {
            return ResponseEntity.badRequest()
                    .body("Ошибка сохранения сущности UserService.registerNewUser(): " + t.getMessage());
        }
        
        return ResponseEntity.ok()
                .body("Пользователь успешно сохранен! Дождитесь подерации от админа для успешного входа в свой аккаунт!");
    }

    @Override
    protected UserDto toDto(User obj) {
        return new UserDto(obj); //конкретная реализация
    }
}
