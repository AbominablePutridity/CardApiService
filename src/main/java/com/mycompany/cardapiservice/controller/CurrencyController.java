package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.CurrencyDto;
import com.mycompany.cardapiservice.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    
    @GetMapping("/user/getCurrencyById")
    @Operation(
        summary = "Получить все валюты", 
        description = "Возвращает список всех валют"
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
            @RequestParam(value = "cardNumber", required = false) Long id
    )
    {
        return new CurrencyDto(currencyService.getObjectById(id));
    }
    
    @PatchMapping("/user/deleteCurrency")
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
            description = "Id валюты для обновления"
        )
        @RequestParam(value = "id", required = true) Long id
    )
    {
        return currencyService.deleteObject(id);
    }
}
