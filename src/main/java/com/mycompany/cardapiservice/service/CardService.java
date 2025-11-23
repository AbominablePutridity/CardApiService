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
    
    /**
     * Метод с переводом денег с карты на карту.
     * @param sender Карта отправителя.
     * @param receiver Карта получателя.
     * @param amount Количество переводимых денег.
     * @return переведен успешно - true, не переведен - false.
     */
    public boolean transferMoney(Card sender, Card receiver, Long amount)
    {
        try {
            //TODO: СДЕЛАТЬ ПРОВЕРКУ СЧЕТА НА КОРРЕКТНЫЕ ЗНАЧЕНИЯ СУММЫ ПЕРЕВОДА!!! 
            //если введено количество переводимых денег с карты пользователя меньше чем денег на самой карте пользователя,
            //то делаем транзакцию отправки денег, иначе выводим
            if((sender.getBalance() > amount) && (amount > 0)) {

                //Отнимаем деньги у пользователся в связи с переводом
                sender.setBalance(sender.getBalance() - amount);
                cardRepository.save(sender);

                //Прибавляем сумму перевода на катру получателя
                receiver.setBalance(receiver.getBalance() + amount);
                cardRepository.save(receiver);
            }
        } catch (Throwable t) {
            System.out.println("ОШИБКА: CardService.transferMoney() - обьект перевода денег не был сохранен - " + t.getMessage());
            
            return false;
        }
        
        return true;
    }
}
