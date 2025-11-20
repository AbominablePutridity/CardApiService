/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.CardDto;
import com.mycompany.cardapiservice.entity.Card;
import com.mycompany.cardapiservice.repository.CardRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Service
public class CardService {
    private final CardRepository cardRepository;
    
    public CardService(CardRepository cardRepository)
    {
        this.cardRepository = cardRepository;
    }
    
    public List<CardDto> prepareUserCards(String userLogin, Pageable pageable)
    {
        List<Card> page = cardRepository.getCardsByUserLogin(userLogin, pageable).getContent();
        
        return page.stream().map((card) -> {
            return new CardDto(card);
        })
        .collect(Collectors.toList());
    }
    
    /**
     * Взять карту по ее номеру.
     * @param number Номер карты которую хотим получить.
     * @return Обьект карты.
     */
    public Card getCardByNumber(String number)
    {
        Optional<Card> card = cardRepository.getCardByNumber(number);
        
        if(!card.isEmpty())
        {
            return card.get();
        } else {
            System.out.println("CardService.getCardByNumber -> КАРТА В БАЗЕ ДАННЫХ ПО НОМЕРУ НЕ БЫЛА НАЙДЕНА!!!");
            
            return null;
        }
    }
}
