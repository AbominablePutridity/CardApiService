package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.CardDto;
import com.mycompany.cardapiservice.dto.UserDto;
import com.mycompany.cardapiservice.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    
    @GetMapping("/admin/getUserCards")
    @Operation(
        summary = "Получить все карты текущего пользователя (по данным пользователя - для админов)", 
        description = "Возвращает список всех карт пользователя"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public List<CardDto> getCardsByUserData(
            @Parameter(
                description = "Номер карты пользователя"
            )
            @RequestParam(value = "cardNumber", required = false) String cardNumber,
            
            @Parameter(
                description = "Поиск по блокировке карты пользователя"
            )
            @RequestParam(value = "cardIsBlocked", required = false) Boolean cardIsBlocked,
            
            @Parameter(
                description = "Логин пользователя карты"
            )
            @RequestParam(value = "userLogin", required = false) String userLogin,
            
            @Parameter(
                description = "Имя пользователя карты"
            )
            @RequestParam(value = "userName", required = false) String userName,
            
            @Parameter(
                description = "Фамилия пользователя карты"
            )
            @RequestParam(value = "userSurname", required = false) String userSurname,
            
            @Parameter(
                description = "Отчество пользователя карты"
            )
            @RequestParam(value = "userPatronymic", required = false) String userPatronymic,
            
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
        
        return cardService.getCardsByUserData(cardNumber, cardIsBlocked, userLogin, userName, userSurname, userPatronymic, pageable);
    }
    
    
    @GetMapping("/admin/getUserCard")
    @Operation(
        summary = "Получить конкретную карту (по id или номеру карты - для админов)", 
        description = "Возвращает конкретную карту пользователя"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public CardDto getCardByIdOrNumber(
            @Parameter(
                description = "id карты"
            )
            @RequestParam(value = "id", required = false) Long id,
            
            @Parameter(
                description = "номер карты"
            )
            @RequestParam(value = "id", required = false) String number
    )
    {
        return cardService.getCardByIdOrNumber(id, number);
    }
    
    @PostMapping("/admin/setUserCard")
    @Operation(
        summary = "Создать карту пользователю (для админов)", 
        description = "Возвращает статус выполнения"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public ResponseEntity<?> setCardForUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                    examples = @ExampleObject(
                        value = """
                            {
                              "number": "string125",
                              "validityPeriod": "2025-11-28",
                              "balance": 0,
                              "userDto": {
                                "id": 1
                              },
                              "statusCardDto": {
                                "id": 1
                              },
                              "currencyDto": {
                                "id": 1
                              },
                              "isBlocked": true
                            }
                            """
                    )
                )
            )
            @RequestBody CardDto cardDto
    )
    {
        return cardService.setCardForUser(cardDto);
    }
    
    @PutMapping("/admin/refreshFullCard")
    @Operation(
        summary = "Обновить (полностью) данные о конкретной карте (для админов)", 
        description = "Возвращает статус выполнения"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public ResponseEntity<?> refreshFullCard(
            @Parameter(
                description = "Id карты для обновления"
            )
            @RequestParam(value = "id", required = true) Long id,
            
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                    examples = @ExampleObject(
                        value = """
                            {
                              "number": "string",
                              "validityPeriod": "2025-11-28",
                              "balance": 0,
                              "userDto": {
                                "id": 1
                              },
                              "statusCardDto": {
                                "id": 1
                              },
                              "currencyDto": {
                                "id": 1
                              }
                            }
                            """
                    )
                )
            )
            @RequestBody CardDto cardDto
    )
    {
        try {
            return cardService.updateUserCard(id, cardDto, false);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: CardController.refreshFullCard() - метод принимает целый обьект в формате json: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("ОШИБКА - json составлен неправильно (принимает полноценный обьект)");
        }
    }
    
    @PatchMapping("/admin/refreshPartUser")
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
    public ResponseEntity<?> refreshPartUser(
            @Parameter(
                description = "Id пользователя для обновления"
            )
            @RequestParam(value = "id", required = true) Long id,
            
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                    examples = @ExampleObject(
                        value = """
                            {
                              "number": "string",
                              "validityPeriod": "2025-11-28",
                              "balance": 0,
                              "userDto": {
                                "id": 1
                              },
                              "statusCardDto": {
                                "id": 1
                              },
                              "currencyDto": {
                                "id": 1
                              }
                            }
                            """
                    )
                )
            )
            @RequestBody CardDto cardDto
    )
    {
        return cardService.updateUserCard(id, cardDto, true);
    }
}
