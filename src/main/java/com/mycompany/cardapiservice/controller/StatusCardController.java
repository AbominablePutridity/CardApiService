package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.StatusCardDto;
import com.mycompany.cardapiservice.service.StatusCardService;
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
@RequestMapping("api/statusCardController")
@Tag(name = "Апи Справочника статусов карт", description = "Операции над статусом карт")
public class StatusCardController {
    public StatusCardService statusCardService;
    
    public StatusCardController(StatusCardService statusCardService)
    {
        this.statusCardService = statusCardService;
    }
    
    @GetMapping("/admin/getAllStatusCard")
    @Operation(
        summary = "Получить список статусов карт по пагинации", 
        description = "Возвращает список статусов карт"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public List<StatusCardDto> getAllStatusCard(
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
        
        return statusCardService.prepareObjects(pageable);
    }
    
    @GetMapping("/admin/getStatusCardById")
    @Operation(
        summary = "Получить конкретный статус карты по id", 
        description = "Возвращает конкретный статус карты"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public StatusCardDto getStatusCardById(
            @Parameter(
                description = "Код валюты"
            )
            @RequestParam(value = "id", required = false) Long id
    )
    {
        return new StatusCardDto(statusCardService.getObjectById(id));
    }
    
    @PostMapping("/admin/setStatusCard")
    @Operation(
        summary = "Сохранить конкретный статус карты по id", 
        description = "Возвращает конкретный статус карты"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public ResponseEntity<?> setCurrency(
            @Parameter(
                description = "Обьект валюты в формате json"
            )
            @RequestBody StatusCardDto newStatusCardDto
    )
    {
        return statusCardService.setObject(newStatusCardDto);
    }
    
    @PutMapping("/admin/refreshFullCurrency")
    public ResponseEntity<?> refreshFullStatusCard(
            @Parameter(
                description = "Id валюты для обновления"
            )
            @RequestParam(value = "id", required = true) Long id,
            
            @Parameter(
                description = "Обьект валюты в формате json"
            )
            @RequestBody StatusCardDto statusCardForUpdate
    )
    {
        try {
            return statusCardService.refreshStatusCard(id, statusCardForUpdate, false);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: StatusCardController.refreshFullStatusCard() - метод принимает целый обьект в формате json: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("ОШИБКА - json составлен неправильно (принимает полноценный обьект)");
        }
    }
    
    @PatchMapping("/admin/refreshPartStatusCard")
    @Operation(
        summary = "Обновить (частично) данные о конкретном статусе карты (для админов)", 
        description = "Возвращает статус выполнения"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public ResponseEntity<?> refreshPartStatusCard(
            @Parameter(
                description = "Id статуса карты для обновления"
            )
            @RequestParam(value = "id", required = true) Long id,
            
            @Parameter(
                description = "Обьект статуса карты в формате json"
            )
            @RequestBody StatusCardDto StatusCardDto
    )
    {
        try {
            return statusCardService.refreshStatusCard(id, StatusCardDto, false);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: UserController.refreshFullUser() - метод принимает целый обьект в формате json: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("ОШИБКА - json составлен неправильно (принимает полноценный обьект)");
        }
    }
    
    @DeleteMapping("/admin/deleteCurrency")
    @Operation(
        summary = "Удалить данные о конкретном статусе карты", 
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
            description = "Id статуса катры для обновления"
        )
        @RequestParam(value = "id", required = true) Long id
    )
    {
        return statusCardService.deleteObject(id);
    }
}
