package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.CardTransferDto;
import com.mycompany.cardapiservice.dto.auth.AuthorizationDto;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.service.CardTransferService;
import com.mycompany.cardapiservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    
    @GetMapping("user/getAllTransfersToUserCard")
    @Operation(
        summary = "Проверить по id-карты принадлежность к текущему пользователю, и если она принадлежит текущему пользователю - взять все транзакции этой карты по ее id", 
        description = "Возвращает список транзакций карты пользователя"
    )
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = false
    )
    public List<CardTransferDto> getAllTransfersToUserCard(
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
            
            @Parameter(
                description = "Id карты для взятия ее транзакций"
            )
            @RequestParam(value="idUserCard") Long idUserCard,
            
            Authentication userAuthData
    )
    {
        Pageable pageable = PageRequest.of(page, size);
        
        return cardTransferService.getAllTransfersByCardIdAndUser(userAuthData, idUserCard, pageable);
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
