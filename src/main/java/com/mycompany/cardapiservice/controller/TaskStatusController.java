package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.TaskStatusDto;
import com.mycompany.cardapiservice.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@RequestMapping("api/taskStatus")
@Tag(name = "Апи Справочника статуса задач", description = "Операции над статусами задач")
public class TaskStatusController {
    public TaskStatusService taskStatusService;
    
    public TaskStatusController(TaskStatusService taskStatusService)
    {
        this.taskStatusService = taskStatusService;
    }
    
    @GetMapping("/admin/getAllTaskStatus")
    @Operation(
        summary = "Получить список статусов задач по пагинации", 
        description = "Возвращает список валют"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public List<TaskStatusDto> getAllCurrency(
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
        
        return taskStatusService.prepareObjects(pageable);
    }
    
    @GetMapping("/admin/getTaskStatusById")
    @Operation(
        summary = "Получить конкретный статус задачи по id", 
        description = "Возвращает конкретный статус задачи"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public TaskStatusDto getTaskStatusById(
            @Parameter(
                description = "Код статуса задачи"
            )
            @RequestParam(value = "id", required = false) Long id
    )
    {
        return new TaskStatusDto(taskStatusService.getObjectById(id));
    }
    
    @PostMapping("/admin/setTaskStatus")
    @Operation(
        summary = "Сохранить конкретный статус задачи по id", 
        description = "Возвращает конкретный статус задачи"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public ResponseEntity<?> setTaskStatus(
            @Parameter(
                description = "Обьект статуса задачи в формате json"
            )
            @RequestBody TaskStatusDto newTaskStatus
    )
    {
        return taskStatusService.setObject(newTaskStatus);
    }
    
    @PutMapping("/admin/refreshFullTaskStatus")
    public ResponseEntity<?> refreshFullCurrency(
            @Parameter(
                description = "Id статуса задачи для обновления"
            )
            @RequestParam(value = "id", required = true) Long id,
            
            @Parameter(
                description = "Обьект статуса задачи в формате json"
            )
            @RequestBody TaskStatusDto taskStatusForUpdate
    )
    {
        try {
            return taskStatusService.refreshTaskStatus(id, taskStatusForUpdate, false);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: TAskStatusController.refreshFullTaskStatus() - метод принимает целый обьект в формате json: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("ОШИБКА - json составлен неправильно (принимает полноценный обьект)");
        }
    }
    
    @PatchMapping("/admin/refreshPartTaskStatus")
    @Operation(
        summary = "Обновить (частично) данные о статусе задачи (для админов)", 
        description = "Возвращает статус выполнения"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public ResponseEntity<?> refreshPartCurrency(
            @Parameter(
                description = "Id статуса задачи для обновления"
            )
            @RequestParam(value = "id", required = true) Long id,
            
            @Parameter(
                description = "Обьект статуса задачи в формате json"
            )
            @RequestBody TaskStatusDto taskStatusDto
    )
    {
        try {
            return taskStatusService.refreshTaskStatus(id, taskStatusDto, false);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: UserController.refreshFullUser() - метод принимает целый обьект в формате json: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("ОШИБКА - json составлен неправильно (принимает полноценный обьект)");
        }
    }
    
    @DeleteMapping("/admin/deleteTaskStatus")
    @Operation(
        summary = "Удалить данные о конкретноv статусе задачи", 
        description = "Возвращает статус выполнения"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public ResponseEntity<?> deleteCurrency(
        @Parameter(
            description = "Id статуса задачи для обновления"
        )
        @RequestParam(value = "id", required = true) Long id
    )
    {
        return taskStatusService.deleteObject(id);
    }
    
}
