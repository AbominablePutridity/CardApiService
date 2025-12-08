package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.UserDto;
import com.mycompany.cardapiservice.dto.auth.AuthorizationDto;
import com.mycompany.cardapiservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Апи Пользователей", description = "Операции над пользователями")
public class UserController {
    public UserService userService;
    
    public UserController(UserService userService)
    {
        this.userService = userService;
    }
    
    @PostMapping("/guest/getEntryTocken")
    @Operation(summary = "Получить токен по логину и паролю", 
               description = "Возвращает статус выполнения сохранения заявки")
    private String getTockenByAuth(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                    examples = @ExampleObject(
                        value = """
                            {
                              "login": "ivanov",
                              "password": "password123"
                            }
                            """
                    )
                )
            )
            @RequestBody AuthorizationDto userAuthData
    )
    {
        return userService.findUserByAuthData(userAuthData);
    }
    
    @GetMapping("/admin/getAllUsers")
    @Operation(
        summary = "Получить всех пользователей (по фильтру - не обязательно)", 
        description = "Возвращает список всех карт пользователя"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    private List<UserDto> getAllUsers(
            @Parameter(
                description = "Логин пользователя"
            )
            @RequestParam(value = "login", required = false) String login,
            
            @Parameter(
                description = "Фамилия пооользователя"
            )
            @RequestParam(value = "surname", required = false) String surname,
            
            @Parameter(
                description = "Имя пользователя"
            )
            @RequestParam(value = "name", required = false) String name,
            
            @Parameter(
                description = "Отчество пользователя"
            )
            @RequestParam(value = "patronymic", required = false) String patronymic,
            
            @Parameter(
                description = "Номер страницы (начинается с 0)",
                example = "0"
            )
            @RequestParam(value = "page", defaultValue = "0") int page,
            
            @Parameter(
                description = "Размер страницы",
                example = "10"
            )
            @RequestParam(value = "size", defaultValue = "10") int size
    )
    {
        Pageable pageable = PageRequest.of(page, size);
        
        return userService.getAllUsersByFilter(login, surname, name, patronymic, pageable);
    }
    
    @GetMapping("/admin/getUserByIdOrLogin")
    @Operation(
        summary = "Получить конкретного пользователя (по логину или его id)", 
        description = "Возвращает конкретного пользователя"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public UserDto getUserByIdOrLogin(
            @Parameter(
                description = "Id пользователя"
            )
            @RequestParam(value = "id", required = false) Long id,
            
            @Parameter(
                description = "Логин пользователя"
            )
            @RequestParam(value = "id", required = false) String login
    )
    {
        return userService.getUserByIdOrLogin(id, login);
    }
    
    @PutMapping("/admin/refreshFullUser")
    @Operation(
        summary = "Обновить (полностью) данные о конкретном пользователе (для админов)", 
        description = "Возвращает статус выполнения"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public ResponseEntity<?> refreshFullUser(
            @Parameter(
                description = "Id пользователя для обновления"
            )
            @RequestParam(value = "id", required = true) Long id,
            
            @Parameter(
                description = "Обьект пользователя в формате json"
            )
            @RequestBody UserDto userDto
    )
    {
        try {
            return userService.refreshUser(id, userDto, false);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: UserController.refreshFullUser() - метод принимает целый обьект в формате json: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("ОШИБКА - json составлен неправильно (принимает полноценный обьект)");
        }
    }
    
    @PatchMapping("/admin/refreshPartUser")
    @Operation(
        summary = "Обновить (частично) данные о конкретном пользователе (для админов)", 
        description = "Возвращает статус выполнения"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public ResponseEntity<?> refreshPartUser(
            @Parameter(
                description = "Id пользователя для обновления"
            )
            @RequestParam(value = "id", required = true) Long id,
            
            @Parameter(
                description = "Обьект пользователя в формате json"
            )
            @RequestBody UserDto userDto
    )
    {
        return userService.refreshUser(id, userDto, true);
    }
    
    @DeleteMapping("/admin/deleteUser")
    @Operation(
        summary = "Удалить конкретного пользователя по его Id (для админов)", 
        description = "Возвращает статус выполнения"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public ResponseEntity<?> deleteUser(
            @Parameter(
                description = "Id пользователя для удаления"
            )
            @RequestParam(value = "id", required = true) Long idUserForDelete
    )
    {
        return userService.deleteObject(idUserForDelete);
    }
}
