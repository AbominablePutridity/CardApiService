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
import org.springframework.web.bind.annotation.GetMapping;
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
    
    @PutMapping("/admin/refreshFullUser")
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public ResponseEntity<?> refreshFullUser(
            @RequestParam(value = "id", required = true) Long id,
            @RequestBody UserDto userDto
    )
    {
        return userService.refreshFullUser(id, userDto);
    }
}
