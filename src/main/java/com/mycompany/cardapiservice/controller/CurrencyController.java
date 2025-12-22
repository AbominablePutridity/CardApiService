package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.CurrencyDto;
import com.mycompany.cardapiservice.service.CurrencyService;
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
@RequestMapping("api/currency")
@Tag(name = "Апи Справочника валюты", description = "Операции над валютой")
public class CurrencyController {
    public CurrencyService currencyService;
    
    public CurrencyController(CurrencyService currencyService)
    {
        this.currencyService = currencyService;
    }
    
    @GetMapping("/admin/getAllCurrency")
    @Operation(
        summary = "Получить список валют по пагинации", 
        description = "Возвращает список валют"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public List<CurrencyDto> getAllCurrency(
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
        
        return currencyService.prepareObjects(pageable);
    }
    
    @GetMapping("/admin/getCurrencyById")
    @Operation(
        summary = "Получить конкретную валюту по id", 
        description = "Возвращает конкретную валюту"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public CurrencyDto getCurrencyById(
            @Parameter(
                description = "Код валюты"
            )
            @RequestParam(value = "id", required = false) Long id
    )
    {
        return new CurrencyDto(currencyService.getObjectById(id));
    }
    
    @PostMapping("/admin/setCurrency")
    @Operation(
        summary = "Сохранить конкретную валюту по id", 
        description = "Возвращает конкретную валюту"
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
            @RequestBody CurrencyDto newCurrency
    )
    {
        return currencyService.setObject(newCurrency);
    }
    
    @PutMapping("/admin/refreshFullCurrency")
    public ResponseEntity<?> refreshFullCurrency(
            @Parameter(
                description = "Id валюты для обновления"
            )
            @RequestParam(value = "id", required = true) Long id,
            
            @Parameter(
                description = "Обьект валюты в формате json"
            )
            @RequestBody CurrencyDto сurrencyForUpdate
    )
    {
        try {
            return currencyService.refreshCurrency(id, сurrencyForUpdate, false);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: CurrencyController.refreshFullCurrency() - метод принимает целый обьект в формате json: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("ОШИБКА - json составлен неправильно (принимает полноценный обьект)");
        }
    }
    
    @PatchMapping("/admin/refreshPartCurrency")
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
    public ResponseEntity<?> refreshPartCurrency(
            @Parameter(
                description = "Id валюты для обновления"
            )
            @RequestParam(value = "id", required = true) Long id,
            
            @Parameter(
                description = "Обьект валюты в формате json"
            )
            @RequestBody CurrencyDto currencyDto
    )
    {
        try {
            return currencyService.refreshCurrency(id, currencyDto, false);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: UserController.refreshFullUser() - метод принимает целый обьект в формате json: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("ОШИБКА - json составлен неправильно (принимает полноценный обьект)");
        }
    }
    
    @DeleteMapping("/admin/deleteCurrency")
    @Operation(
        summary = "Удалить данные о конкретной валюте", 
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
            description = "Id валюты для удаления"
        )
        @RequestParam(value = "id", required = true) Long id
    )
    {
        return currencyService.deleteObject(id);
    }
}
