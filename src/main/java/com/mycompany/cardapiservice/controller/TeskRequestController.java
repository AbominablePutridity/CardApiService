package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.TaskRequestDto;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.repository.UserRepository;
import com.mycompany.cardapiservice.service.TaskRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/api/taskRequest")
@Tag(name = "Апи Заявки пользователей", description = "Операции над заявками пользователей администраторам")
public class TeskRequestController {
    private TaskRequestService taskRequestService; 
    private UserRepository userRepository;
    
    public TeskRequestController(
            TaskRequestService taskRequestService,
            UserRepository userRepository
    )
    {
        this.taskRequestService = taskRequestService;
        this.userRepository = userRepository;
    }
    
    @PostMapping("/user/setTask")
    @Operation(summary = "Создать заявку текущим пользователем для администратора (для создания карт и тд)", 
               description = "Возвращает статус выполнения сохранения заявки")
    @ApiResponse(responseCode = "200", description = "Успешное сохранение заявки")
    public ResponseEntity<?> createTaskRequest(
            @Parameter(
                description = "Логин текущего пользователя",
                example = "",
                required = true
            )
            @RequestParam(value = "userLogin") String userLogin,
            
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
            @RequestBody TaskRequestDto taskRequestDto
    )
    {
        User currentUser = userRepository.findByLogin(userLogin).get();
        
        return ResponseEntity.ok(
                taskRequestService.createTask(taskRequestDto, currentUser)
        );
    }
}
