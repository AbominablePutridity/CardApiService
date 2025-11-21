package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.CardDto;
import com.mycompany.cardapiservice.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/api/card")
@Tag(name = "Апи Карты", description = "Операции над картой")
public class CardController {
    public CardService cardService;
    
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }
    
    @GetMapping("/user/getAllCards")
    @Operation(
        summary = "Получить все карты текущего пользователя (по логину)", 
        description = "Возвращает список всех карт пользователя"
    ) 
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    public List<CardDto> getUserCards(
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
            Authentication authentication //берем данные текущего пользователя для логина
    )
    { // далее будем брать логин из spring ыусгкшен
        Pageable pageable = PageRequest.of(page, size);
        
        return cardService.prepareUserCards(authentication.getName(), pageable);
    }
}
