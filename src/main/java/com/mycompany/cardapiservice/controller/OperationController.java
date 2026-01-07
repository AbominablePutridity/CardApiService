package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.OperationDto;
import com.mycompany.cardapiservice.service.OperationService;
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
@RequestMapping("/api/card")
@Tag(name = "Апи Справочника операций", description = "Операции над операциями")
public class OperationController {
    public OperationService operationService;
    
    public OperationController(OperationService operationService)
    {
        this.operationService = operationService;
    }

    @GetMapping("/admin/getAllOperations")
    @Operation(
        summary = "Взять список всех операций", 
        description = "Возвращает статус выполнения"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = false
    )
    public List<OperationDto> getAllOperations(
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
        
        return operationService.prepareObjects(pageable);
    }
    
    @GetMapping("/admin/getOperationById")
    @Operation(
        summary = "Получить конкретную операцию по id", 
        description = "Возвращает конкретную операцию"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = false
    )
    public OperationDto getOperationById(
            @Parameter(
                description = "Код операции"
            )
            @RequestParam(value = "cardNumber", required = false) Long id
    )
    {
        return new OperationDto(operationService.getObjectById(id));
    }
    
    @PostMapping("/admin/setOperation")
    @Operation(
        summary = "Сохранить конкретную операцию по id", 
        description = "Возвращает конкретную операцию"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = false
    )
    public ResponseEntity<?> setOperation(
            @Parameter(
                description = "Обьект валюты в формате json"
            )
            @RequestBody OperationDto newCurrency
    )
    {
        return operationService.setObject(newCurrency);
    }
    
    @PutMapping("/admin/refreshFulCurrency")
    @Operation(
        summary = "Создать данные о конкретной операции (для админов)", 
        description = "Возвращает статус выполнения"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = false
    )
    public ResponseEntity<?> updateFullOperation(
            @Parameter(
                description = "Id операции для обновления"
            )
            @RequestParam(value = "id", required = true) Long id,
            
            @Parameter(
                description = "Обьект операции в формате json"
            )
            @RequestBody OperationDto operationForUpdate
    )
    {
        try {
            return operationService.refreshOperation(id, operationForUpdate, false);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: OperationController.refreshFullUser() - метод принимает целый обьект в формате json: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("ОШИБКА - json составлен неправильно (принимает полноценный обьект)");
        }
    }
    
    @PatchMapping("/admin/refreshPartOperation")
    @Operation(
        summary = "Обновить (частично) данные о конкретной операции (для админов)", 
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
                description = "Id операции для обновления"
            )
            @RequestParam(value = "id", required = true) Long id,
            
            @Parameter(
                description = "Обьект операции в формате json"
            )
            @RequestBody OperationDto operationDto
    )
    {
        try {
            return operationService.refreshOperation(id, operationDto, false);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: UserController.refreshFullUser() - метод принимает целый обьект в формате json: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("ОШИБКА - json составлен неправильно (принимает полноценный обьект)");
        }
    }
    
    @DeleteMapping("/admin/deleteOperation")
    @Operation(
        summary = "Удалить данные о конкретной операции", 
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
            description = "Id операции для обновления"
        )
        @RequestParam(value = "id", required = true) Long id
    )
    {
        return operationService.deleteObject(id);
    }
}
