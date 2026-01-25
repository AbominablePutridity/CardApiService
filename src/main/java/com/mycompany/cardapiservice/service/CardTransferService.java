package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.CardTransferDto;
import com.mycompany.cardapiservice.entity.Card;
import com.mycompany.cardapiservice.entity.CardTransfer;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.repository.CardRepository;
import com.mycompany.cardapiservice.repository.CardTransferRepository;
import com.mycompany.cardapiservice.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CardTransferService {
    private CardTransferRepository cardTransferRepository;
    private CardService cardService;
    
    public CardTransferService(CardTransferRepository cardTransferRepository, CardService cardService, UserRepository userRepository)
    {
        this.cardTransferRepository = cardTransferRepository;
        this.cardService = cardService;
    }
    
    /**
     * Метод отвечающий за перевод средств между пользовательскими картами.
     * @param cardTransferData Данные перевода.
     * @param currentUser Обьект текущего пользователя из Security модуля.
     * @return Статус выполения.
     */
    public ResponseEntity<?> setCardTransfer(CardTransferDto cardTransferData, User currentUser)
    {
        // сначала проверим, что карта отправителя действительно принадлежит отправителю
        // для этого из обьекта возьмем номер карты и проверим какой у нее пользователь
        
        //карта отправителя
        Card senderCard = null;
        
        if(cardService.getCardByNumber(cardTransferData.getSenderDto().getNumber()) != null) {
            senderCard = cardService.getCardByNumber(cardTransferData.getSenderDto().getNumber());
        } else {
            senderCard = cardService.getCardById(cardTransferData.getSenderDto().getId());
        }
        
        //если пользователь карты эквивалентен текущему пользователю портала, то проводим транзакцию перевода
        //иначе выводим ограничение доступа с сообщением.
        if(senderCard.getUser().equals(currentUser))
        {         
            //карта получателя
            Card receiverCard = cardService.getCardByNumber(cardTransferData.getReceiverDto().getNumber());
                
            //данные о переводе
            BigDecimal amount = cardTransferData.getAmountOfMoney(); //количество переводимых денег
            String description = cardTransferData.getDescription(); //сообщение получателю от отправителя денег
        
            if(cardService.transferMoney(senderCard, receiverCard, amount)) {
                if(saveNewTranzactionTransferMoney(amount, description, senderCard, receiverCard)) {
                    return ResponseEntity.status(HttpStatus.OK)
                    .body("Перевод был успешно осуществлен!");
                } else {
                    return ResponseEntity.status(HttpStatus.OK)
                    .body("Деньги были отправлены без сохранения транзакции в таблице CardTransfer");
                }
            } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("ОШИБКА: Деньги не были отправлены! Возможно вы ввели некорректную сумму перевода или карта отправителя или получателя - заблокированны");
            } 
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("ОШИБКА: Отправитель был неверно указан!");
        }
    }
    
    /**
     * Сохранение транзакции перевода денежных средств с карты на кату.
     * @param amount Количество денег перевода
     * @param desctiption Сообщение перевода от отправителя
     * @param sender Карта отправителя
     * @param receiver Карта получателя
     * @return транзакция пройдена - true, не пройдена - false.
     */
    public boolean saveNewTranzactionTransferMoney(
            BigDecimal amount,
            String desctiption,
            Card sender,
            Card receiver
    )
    {
        try {
            CardTransfer newTransfer = new CardTransfer();
            newTransfer.setAmountOfMoney(amount);
            newTransfer.setDescription(desctiption);
            newTransfer.setSender(sender);
            newTransfer.setReceiver(receiver);
            newTransfer.setTransferDate(LocalDate.now());
            newTransfer.setIsTransfered(true);
            
            cardTransferRepository.save(newTransfer);
        } catch (Throwable t) {
            System.out.println("ОШИБКА: CardTransferService.saveNewTranzactionTransferMoney() - обьект перевода денег не был сохранен - " + t.getMessage());
            
            return false;
        }
        
        return true;
    }
    
    /**
     * Взять все транзакции карты по ее id у текущего пользователя с пагинацией. 
     * @param userAuthData Данные пользователя.
     * @param idCard Id карты пользователя
     * @param pageable Пагинация.
     * @return Лист транзакций выбранной карты.
     */
    public List<CardTransferDto> getAllTransfersByCardIdAndUser(
            Authentication userAuthData,
            Long idCard,
            Pageable pageable
    ) {
        Card currentCard = cardService.getCardByUserLoginAndCardId(userAuthData.getName(), idCard);
        
        if(currentCard != null)
        {
            Page<CardTransfer> cardTransfers = cardTransferRepository.getTransactionsByCardId(currentCard, pageable);
            
            return cardTransfers.stream().map((cardTransfer) -> {
                return new CardTransferDto(cardTransfer);
            })
            .collect(Collectors.toList());
            
        } else {
            throw new AccessDeniedException("У вас нет доступа к карте с id " + idCard);
        }
    }
}
