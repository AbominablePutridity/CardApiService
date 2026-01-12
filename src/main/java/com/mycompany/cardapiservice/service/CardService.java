package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.CardDto;
import com.mycompany.cardapiservice.dto.UserDto;
import com.mycompany.cardapiservice.entity.Card;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.repository.CardRepository;
import com.mycompany.cardapiservice.repository.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CardService extends ExtendedUniversalWriteEndpointsService<Card, CardDto, Card, Long, JpaRepository<Card, Long>> {
    private final CardRepository cardRepository;
    private UserService userService;
    private StatusCardService statusCardService;
    private CurrencyService currencyService;
    
    public CardService(
            CardRepository cardRepository,
            UserService userService,
            StatusCardService statusCardService,
            CurrencyService currencyService
    )
    {
        super(cardRepository); //передаем репозиторий в абстрактный класс, от которого унаследуемся здесь
        
        this.cardRepository = cardRepository;
        this.userService = userService;
        this.statusCardService = statusCardService;
        this.currencyService = currencyService;
    }
    
    /**
     * Взять список карт по фильтрам.
     * @param userLogin Фильтр по логину пользователя.
     * @param pageable Фильтр по пагинации.
     * @return 
     */
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
    public boolean transferMoney(Card sender, Card receiver, BigDecimal amount)
    {
        try {
            //TODO: СДЕЛАТЬ ПРОВЕРКУ СЧЕТА НА КОРРЕКТНЫЕ ЗНАЧЕНИЯ СУММЫ ПЕРЕВОДА!!! 
            //если введено количество переводимых денег с карты пользователя меньше чем денег на самой карте пользователя,
            //то делаем транзакцию отправки денег, иначе выводим
            if (sender.getBalance().compareTo(amount) > 0 && amount.compareTo(BigDecimal.ZERO) > 0) {
                /* balance > amount И amount > 0
                - Методы compareTo() возвращают:
                    -1 — если первый объект МЕНЬШЕ второго

                    0 — если объекты РАВНЫ

                    1 — если первый объект БОЛЬШЕ второго
                */
                
                
                //если карты отправителя и получателя не заблокированны
                if(!sender.getIsIsBlocked() && !receiver.getIsIsBlocked()) {
                    //если валюты карт отправителя и получчателя совпадают - делаем перевод средств
                    if(sender.getCurrency().getSign().equals(receiver.getCurrency().getSign()))
                    {
                        //Отнимаем деньги у пользователся в связи с переводом
                        sender.setBalance(sender.getBalance().subtract(amount));
                        cardRepository.save(sender);

                        //Прибавляем сумму перевода на катру получателя
                        receiver.setBalance(receiver.getBalance().add(amount));
                        cardRepository.save(receiver);
                    } else {
                        //если валюты отправителя и получателя разные - перед отправкой получателю конвертируем валюту отправителя в валюту получателя
                        BigDecimal priceForTransfer = (amount.multiply(receiver.getCurrency().getRublePrice())).divide(sender.getCurrency().getRublePrice(), 2, RoundingMode.HALF_UP);
                        
                        System.out.println("Сумма " + amount + "" + sender.getCurrency().getSign() + " при переводе в " + receiver.getCurrency().getSign()
                                + " равна " + priceForTransfer + "" + receiver.getCurrency().getSign()     
                        );
                        
                        //переводим конвертированную валюту получателю:
                        //Отнимаем деньги у пользователся в связи с переводом
                        sender.setBalance(sender.getBalance().subtract(amount));
                        cardRepository.save(sender);

                        //Прибавляем сумму перевода на катру получателя
                        receiver.setBalance(receiver.getBalance().add(priceForTransfer));
                        cardRepository.save(receiver);
                    }
                } else {
                    System.err.println("ОШИБКА: Карта отправителя или получателя заблокированна, перевод не выполнен!");
                    
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
    
    /**
     * Взять обьект карты по ее id.
     * @param id Id-код карты.
     * @return Обьект карты.
     */
    public Card getCardById(Long id)
    {
        return cardRepository.findById(id).get();
    }
    
    /**
     * Взять обьект карты по ее id или номеру карты.
     * @param id Id-код карты.
     * @param number Номер карты.
     * @return Обьект карты.
     */
    public CardDto getCardByIdOrNumber(Long id, String number)
    {
        Card card = null;
        
        if (id != null && number == null)
        {
            card = getCardById(id);
        }
        
        if (number != null && id == null) {
            card = cardRepository.getCardByNumber(number).get();
        }
        
        return new CardDto(card);
    }
    
    /**
     * Создание карты для пользователя.
     * @param cardDto Данные создаваемой карты.
     * @return Статус выполнения.
     */
    public ResponseEntity<?> setCardForUser(CardDto cardDto)
    {
        try {
            Card newCard = cardDto.toEntity();
            newCard.setUser(userService.getObjectById(cardDto.getUserDto().getId()));
            newCard.setStatusCard(statusCardService.getObjectById(cardDto.getStatusCardDto().getId()));
            newCard.setCurrency(currencyService.getObjectById(cardDto.getCurrencyDto().getId()));
            
            cardRepository.save(newCard);
            
            return ResponseEntity.ok("Обьект карты успешно сохранен пользователю!");
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: CardService.setCardForUser() - не удалось сохранить обьект новой карты: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("не удалось сохранить обьект новой карты!");
        }
    }
    
     /**
     * Обновление обьекта карты пользователя по его id (для PUT и PATCH запросов).
     * @param idCardForUpdate
     * @param refreshedCard Обьект карты с данными для обновления.
     * @param isSaveByPart
     * true - обновить конкретное поле/поля в данном обьекте (не целый обьект - для PATCH);
     * false - обновить обьект целиком (для PUT).
     * @return Статус выполнения.
     */
    public ResponseEntity<?> updateUserCard(Long idCardForUpdate, CardDto refreshedCard, boolean isSaveByPart)
    {
        try {
            Card card = cardRepository.findById(idCardForUpdate).get();
        
            //обновляем все поля кроме ключей и возвращаем обьект с данными
            Card updatedCardObject = refreshedCard.toEntityWithFieldsCondition(card, isSaveByPart);
            
            if (isSaveByPart) { 
                if(refreshedCard.getUserDto()!= null)
                {
                    updatedCardObject.setUser(userService.getObjectById(refreshedCard.getUserDto().getId()));
                }

                if(refreshedCard.getStatusCardDto() != null)
                {
                    updatedCardObject.setStatusCard(statusCardService.getObjectById(refreshedCard.getStatusCardDto().getId()));
                }
                
                if(refreshedCard.getCurrencyDto() != null)
                {
                    updatedCardObject.setCurrency(currencyService.getObjectById(refreshedCard.getCurrencyDto().getId()));
                }
            } else {
                updatedCardObject.setUser(userService.getObjectById(refreshedCard.getUserDto().getId()));
                updatedCardObject.setStatusCard(statusCardService.getObjectById(refreshedCard.getStatusCardDto().getId()));
                updatedCardObject.setCurrency(currencyService.getObjectById(refreshedCard.getCurrencyDto().getId()));
            }
            cardRepository.save(card);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: CardService.updateUserCard() проверка полей вызвало исключение: " + t.getMessage());
            
            return ResponseEntity.badRequest()
               .body("Ошибка при обновлении пользователя: " + t.getMessage());
        }
        
        return ResponseEntity.ok("пользователь успешно обновлен!");
    }

    /**
     * Возвращает список карт со скрытыми номерами. 
     * @param cards Лист с картами для скрытия номеров.
     * @return Лист карт со скрытыми номерами.
     */
    public List<CardDto> hideCardsNumber(List<CardDto> cards)
    {
        for(CardDto card : cards) {
            try {
                card.setNumber("**** **** **** " + card.getNumber().substring(12));
            } catch (Throwable t) {
                System.err.println("ОШИБКА CardService.hideCardsNumber - номер карты с id = " + card.getId() + " имеет неправильную длинну: " + t.getMessage());
            }
        }
        
        return cards;
    }
    
    @Override
    protected CardDto toDto(Card obj) {
        return new CardDto(obj); //конкретная реализация из абстрактного класса.
    }
    
    /**
     * Взять обьект карты, по ее id и логину пользователя,
     * гарантируем, что выбранная карта принадлежит текущему пользователю
     * (для взятия транзакций карты в CardTransferService).
     * @param userLogin Логин текущего пользователя.
     * @param cardId Id карты пользователя.
     * @return 
     */
    public Card getCardByUserLoginAndCardId(String userLogin, Long cardId)
    {
        Optional<Card> card = cardRepository.getCardByUserLoginAndCardId(userLogin, cardId);
        
        if(card.isPresent())
        {
            return card.get();
        } else {
            return null;
        }
    }
}
