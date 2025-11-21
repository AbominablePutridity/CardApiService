package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.CardTransferDto;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.service.CardTransferService;
import com.mycompany.cardapiservice.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
public class CardTransferController {
    private UserService userService;
    private CardTransferService cardTransferService;
    
    public CardTransferController(UserService userService, CardTransferService cardTransferService)
    {
        this.userService = userService;
        this.cardTransferService = cardTransferService;
    }
    
    @PostMapping("user/transfer")
    @Parameter( // параметр, создающий поле заголовка для токена пользователя JWT (В сервисе JwtFilter берем если основной заголовок является пустым)
            in = ParameterIn.HEADER,
            name = "X-Api-Token", //ключ заголовка
            description = "Введите JWT-токен сюда (Bearer <JWT-tocken>)", //надпись над полем
            required = true
    )
    public ResponseEntity<?> getTransfer(@RequestBody CardTransferDto cardTransferData, Authentication authentication) 
    {
        // используем логин текущего пользователя для поиска пользователя для взятия его обьекта
        User currentUser = userService.getCurrentUserByLogin(authentication.getName());
        
        return cardTransferService.setCardTransfer(cardTransferData, currentUser);
    }
}
