package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.CardDto;
import com.mycompany.cardapiservice.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/api/card")
@Tag(name = "Card API", description = "Операции над картой")
public class CardController {
    public CardService cardService;
    
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }
    
    @GetMapping("/getAllCards")
    @Operation(summary = "Получить все карты текущего пользователя (по логину)", 
               description = "Возвращает список всех карт пользователя")
    @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    public List<CardDto> getUserCards(
            @Parameter(
                description = "Логин текущего пользователя",
                example = "",
                required = true
            )
            @RequestParam(value = "userLogin") String userLogin,
            
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
    { // далее будем брать логин из spring ыусгкшен
        Pageable pageable = PageRequest.of(page, size);
        
        return cardService.prepareUserCards(userLogin, pageable);
    }
}
