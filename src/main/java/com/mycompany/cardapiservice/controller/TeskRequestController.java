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
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
            required = false
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
    
    @GetMapping("/user/getAllTasksByCurrentUser")
    @Operation(
        summary = "Получить список задач по пагинации", 
        description = "Возвращает список задач"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = false
    )
    public List<TaskRequestDto> getAllTasksByCurrentUser(
            @Parameter(
                description = "Номер страницы (начинается с 0)",
                example = "0"
            )
            @RequestParam(value = "page", defaultValue = "0") int page,
            
            @Parameter(
                description = "Размер страницы",
                example = "10"
            )
            @RequestParam(value = "size", defaultValue = "10") int size,
            
            Authentication currentUser
    )
    {
        Pageable pageable = PageRequest.of(page, size);
        
        return taskRequestService.getTasksForCurrentUser(pageable, userService.getCurrentUserByLogin(currentUser.getName()));
    }
    
    @GetMapping("/admin/getAllTaskRequest")
    @Operation(
        summary = "Получить список задач текущего пользователя по пагинации", 
        description = "Возвращает список задач"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = false
    )
    public List<TaskRequestDto> getAllTaskRequest(
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
        
        return taskRequestService.prepareObjects(pageable);
    }
    
    @GetMapping("/admin/getCurrencyById")
    @Operation(
        summary = "Получить конкретную задачу по id", 
        description = "Возвращает конкретную валюту"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = false
    )
    public TaskRequestDto getCurrencyById(
            @Parameter(
                description = "Код задачи"
            )
            @RequestParam(value = "id", required = false) Long id
    )
    {
        return new TaskRequestDto(taskRequestService.getObjectById(id));
    }
    
    @PostMapping("/admin/setTaskRequest")
    @Operation(
        summary = "Сохранить конкретную задачу по id", 
        description = "Возвращает конкретную задачу"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = false
    )
    public ResponseEntity<?> setTaskRequest(
            @Parameter(
                description = "Обьект валюты в формате json"
            )
            @RequestBody TaskRequestDto newTaskRequest
    )
    {
        return taskRequestService.setObject(newTaskRequest);
    }
    
    @PutMapping("/admin/refreshFullTaskRequest")
    @Operation(
        summary = "Обновить конкретную задачу по id", 
        description = "Возвращает конкретную задачу"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = false
    )
    public ResponseEntity<?> refreshFullTaskRequest(
            @Parameter(
                description = "Id задачи для обновления"
            )
            @RequestParam(value = "id", required = true) Long id,
            
            @Parameter(
                description = "Обьект валюты в формате json"
            )
            @RequestBody TaskRequestDto taskRequestForUpdate
    )
    {
        try {
            return taskRequestService.refreshTaskRequest(id, taskRequestForUpdate, false);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: CurrencyController.refreshFullCurrency() - метод принимает целый обьект в формате json: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("ОШИБКА - json составлен неправильно (принимает полноценный обьект)");
        }
    }
    
    @PatchMapping("/admin/refreshPartTaskRequest")
    @Operation(
        summary = "Обновить (частично) данные о конкретной заявке (для админов)", 
        description = "Возвращает статус выполнения"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = false
    )
    public ResponseEntity<?> refreshPartCurrency(
            @Parameter(
                description = "Id валюты для обновления"
            )
            @RequestParam(value = "id", required = true) Long id,
            
            @Parameter(
                description = "Обьект валюты в формате json"
            )
            @RequestBody TaskRequestDto taskRequestDto
    )
    {
        try {
            return taskRequestService.refreshTaskRequest(id, taskRequestDto, false);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: TaskRequestController.refreshParttaskRequest() - метод принимает целый обьект в формате json: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("ОШИБКА - json составлен неправильно (принимает полноценный обьект): " + t.getMessage());
        }
    }
    
    @DeleteMapping("/admin/deleteTaskRequest")
    @Operation(
        summary = "Удалить данные о конкретной задаче", 
        description = "Возвращает статус выполнения"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = false
    )
    public ResponseEntity<?> deleteCurrency(
        @Parameter(
            description = "Id задачи для удаления"
        )
        @RequestParam(value = "id", required = true) Long id
    )
    {
        return taskRequestService.deleteObject(id);
    }
}
