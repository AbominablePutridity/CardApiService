package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.CardTransferDto;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.service.CardTransferService;
import com.mycompany.cardapiservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("api/cardTransfer")
@Tag(name = "Апи Транзакций переводов пользователей", description = "Операции над переводами пользователей")
public class CardTransferController {
    private UserService userService;
    private CardTransferService cardTransferService;
    
    public CardTransferController(UserService userService, CardTransferService cardTransferService)
    {
        this.userService = userService;
        this.cardTransferService = cardTransferService;
    }
    
    @PostMapping("user/transfer")
    @Operation(
        summary = "Осуществить перевод другому пользователю по номеру карты", 
        description = "Возвращает статус выполнения сохранения перевода и его транзакции"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = false
    )
    public ResponseEntity<?> getTransfer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                    examples = @ExampleObject(
                        value = """
                            {
                              "senderDto": {
                                "number": "4111111111111111",
                                "validityPeriod": "2026-12-31",
                                "balance": 15000.50,
                                "userDto": {
                                  "login": "ivanov",
                                  "surname": "Иванов",
                                  "name": "Иван",
                                  "patronymic": "Иванович"
                                },
                                "statusCardDto": {
                                  "id": 1
                                },
                                "currencyDto": {
                                  "id": 1
                                },
                                "isBlocked": false
                              },
                              "receiverDto": {
                                "number": "4222222222222222",
                                "validityPeriod": "2025-10-31",
                                "balance": 5000.00,
                                "userDto": {
                                  "login": "petrov",
                                  "surname": "Петров",
                                  "name": "Петр",
                                  "patronymic": "Петрович"
                                },
                                "statusCardDto": {
                                  "id": 1
                                },
                                "currencyDto": {
                                  "id": 1
                                },
                                "isBlocked": false
                              },
                              "amountOfMoney": 1000.00,
                              "description": "Перевод за услуги"
                            }
                            """
                    )
                )
            )
            @RequestBody CardTransferDto cardTransferData, Authentication authentication) 
    {
        // используем логин текущего пользователя для поиска пользователя для взятия его обьекта
        User currentUser = userService.getCurrentUserByLogin(authentication.getName());
        
        System.out.println("CUR_USR => " + currentUser.toString());
        
        return cardTransferService.setCardTransfer(cardTransferData, currentUser);
    }
}
