package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.auth.AuthorizationDto;
import com.mycompany.cardapiservice.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    public UserService userService;
    
    public UserController(UserService userService)
    {
        this.userService = userService;
    }
    
    @PostMapping("/login")
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
