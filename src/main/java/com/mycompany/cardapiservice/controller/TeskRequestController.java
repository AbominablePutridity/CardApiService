package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.TaskRequestDto;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.service.TaskRequestService;
import com.mycompany.cardapiservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/api/taskRequest")
@Tag(name = "Апи Заявки пользователей", description = "Операции над заявками пользователей администраторам")
public class TeskRequestController {
    private TaskRequestService taskRequestService; 
    private UserService userService;
    
    public TeskRequestController(
            TaskRequestService taskRequestService,
            UserService userService
    )
    {
        this.taskRequestService = taskRequestService;
        this.userService = userService;
    }
    
    @PostMapping("/user/setTask")
    @Operation(
        summary = "Создать заявку текущим пользователем для администратора (для создания карт и тд)", 
        description = "Возвращает статус выполнения сохранения заявки"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    @ApiResponse(responseCode = "200", description = "Успешное сохранение заявки")
    public ResponseEntity<?> createTaskRequest(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                    examples = @ExampleObject(
                        value = """
                            {
                              "description": "Создание новой карты",
                              "operationDto": {
                                "id": 1
                              },
                              "taskStatusDto": {
                                "id": 1
                              }
                            }
                            """
                    )
                )
            )
            @RequestBody TaskRequestDto taskRequestDto,
            Authentication authentication //берем данные текущего пользователя для логина
    )
    {
        User currentUser = userService.getCurrentUserByLogin(authentication.getName());
        
        return ResponseEntity.ok(
                taskRequestService.createTask(taskRequestDto, currentUser)
        );
    }
    
    
}
