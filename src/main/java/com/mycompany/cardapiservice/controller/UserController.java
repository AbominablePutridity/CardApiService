package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.auth.AuthorizationDto;
import com.mycompany.cardapiservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    
    @PostMapping("/user/login")
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
}
