package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.CardTransferDto;
import com.mycompany.cardapiservice.entity.Card;
import com.mycompany.cardapiservice.entity.CardTransfer;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.repository.CardRepository;
import com.mycompany.cardapiservice.repository.CardTransferRepository;
import com.mycompany.cardapiservice.repository.UserRepository;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CardTransferService {
    private CardTransferRepository cardTransferRepository;
    private CardService cardService;
//    private UserRepository userRepository;
    
    public CardTransferService(CardTransferRepository cardTransferRepository, CardService cardService, UserRepository userRepository)
    {
        this.cardTransferRepository = cardTransferRepository;
        this.cardService = cardService;
//        this.userRepository = userRepository;
    }
    
    /**
     * 
     * @param cardTransferData
     * @param currentUser
     */
    public ResponseEntity<?> setCardTransfer(CardTransferDto cardTransferData, User currentUser)
    {
        // сначала проверим, что карта отправителя действительно принадлежит отправителю
        // для этого из обьекта возьмем номер карты и проверим какой у нее пользователь
        Card senderCard = cardService.getCardByNumber(cardTransferData.getSenderDto().getNumber());
        
        //если пользователь карты эквивалентен текущему пользователю портала, то проводим транзакцию перевода
        //иначе выводим ограничение доступа с сообщением.
        if(senderCard.getUser().equals(currentUser))
        {
            //TODO: СДЕЛАТЬ ПРОВЕРКУ СЧЕТА НА КОРРЕКТНЫЕ ЗНАЧЕНИЯ СУММЫ ПЕРЕВОДА!!! 
            
            CardTransfer newTransfer = new CardTransfer();
            newTransfer.setAmountOfMoney(cardTransferData.getAmountOfMoney());
            newTransfer.setDescription(cardTransferData.getDescription());
            newTransfer.setSender(senderCard);
            newTransfer.setReceiver(cardService.getCardByNumber(cardTransferData.getReceiverDto().getNumber()));
            newTransfer.setTransferDate(LocalDate.now());
            newTransfer.setIsTransfered(false);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Карта не принадлежит текущему пользователю");
        }
        
        return ResponseEntity.status(HttpStatus.OK)
                .body("Перевод был успешно осуществлен!");
    }
}
