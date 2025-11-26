/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.CardDto;
import com.mycompany.cardapiservice.dto.UserDto;
import com.mycompany.cardapiservice.entity.Card;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.repository.CardRepository;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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

                //если карта отправителя не заблокированна
                if(!sender.getIsIsBlocked()) {
                    //Отнимаем деньги у пользователся в связи с переводом
                    sender.setBalance(sender.getBalance() - amount);
                    cardRepository.save(sender);
                } else {
                    System.err.println("ОШИБКА: Карта отправителя заблокированна, перевод не выполнен!");
                    
                    return false;
                }

                //если карта получателя не заблокированна
                if(!receiver.getIsIsBlocked()) {
                    //Прибавляем сумму перевода на катру получателя
                    receiver.setBalance(receiver.getBalance() + amount);
                    cardRepository.save(receiver);
                } else {
                    System.err.println("ОШИБКА: Карта получателя заблокированна, перевод не выполнен!");
                    
                    return false;
                }
            }
        } catch (Throwable t) {
            System.err.println("ОШИБКА: CardService.transferMoney() - обьект перевода денег не был сохранен - " + t.getMessage());
            
            return false;
        }
        
        return true;
    }
    
    /**
     * Взять карты пользователя по его данным.
     * @param cardNumber Фильтр номера карты.
     * @param cardIsBlocked Фильтр с блокировкой карты (искать заблокированную или нет).
     * @param userLogin Фильтр по логину пользователя.
     * @param userName Фильтр по имени пользователя.
     * @param userSurname Фильтр по фамилии пользователя.
     * @param userPatronymic фильтр по отчеству пользователя.
     * @param pageable
     * @return 
     */
    public List<CardDto> getCardsByUserData(
            String cardNumber,
            Boolean cardIsBlocked,
            String userLogin,
            String userName,
            String userSurname,
            String userPatronymic,
            Pageable pageable
    )
    {
        Page<Card> cards = cardRepository.getCardByUserData(cardNumber, cardIsBlocked, userLogin, userName, userSurname, userPatronymic, pageable);
        
        return cards.stream().map((card) -> {
            return new CardDto(card);
        })
        .collect(Collectors.toList());
    }
}
