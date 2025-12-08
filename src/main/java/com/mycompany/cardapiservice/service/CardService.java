package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.CardDto;
import com.mycompany.cardapiservice.dto.UserDto;
import com.mycompany.cardapiservice.entity.Card;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.repository.CardRepository;
import com.mycompany.cardapiservice.repository.UserRepository;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */
@Service
public class CardService extends UniversalEndpointsService<Card, CardDto, Card, Long, JpaRepository<Card, Long>> {
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
            Card newCard = new Card();
            newCard.setNumber(cardDto.getNumber());
            newCard.setUser(userService.getObjectById(cardDto.getUserDto().getId()));
            newCard.setValidityPeriod(cardDto.getValidityPeriod());
            newCard.setStatusCard(statusCardService.getStatusCardById(cardDto.getStatusCardDto().getId()));
            newCard.setBalance(cardDto.getBalance());
            newCard.setCurrency(currencyService.getObjectById(cardDto.getCurrencyDto().getId()));
            newCard.setIsBlocked(cardDto.getIsBlocked());
            
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
        
            if (isSaveByPart) {
                if(refreshedCard.getNumber()!= null)
                {
                    card.setNumber(refreshedCard.getNumber());
                }
                
                if(refreshedCard.getUserDto()!= null)
                {
                    card.setUser(userService.getObjectById(refreshedCard.getUserDto().getId()));
                }
                
                if(refreshedCard.getValidityPeriod()!= null)
                {
                    card.setValidityPeriod(refreshedCard.getValidityPeriod());
                }

                if(refreshedCard.getStatusCardDto() != null)
                {
                    card.setStatusCard(statusCardService.getStatusCardById(refreshedCard.getStatusCardDto().getId()));
                }

                if(refreshedCard.getBalance()!= null)
                {
                    card.setBalance(refreshedCard.getBalance());
                }
                
                if(refreshedCard.getCurrencyDto() != null)
                {
                    card.setCurrency(currencyService.getObjectById(refreshedCard.getCurrencyDto().getId()));
                }
            } else {
                card.setNumber(refreshedCard.getNumber());
                card.setUser(userService.getObjectById(refreshedCard.getUserDto().getId()));
                card.setValidityPeriod(refreshedCard.getValidityPeriod());
                card.setStatusCard(statusCardService.getStatusCardById(refreshedCard.getStatusCardDto().getId()));
                card.setBalance(refreshedCard.getBalance());
                card.setCurrency(currencyService.getObjectById(refreshedCard.getCurrencyDto().getId()));
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

    @Override
    protected CardDto toDto(Card obj) {
        return new CardDto(obj); //конкретная реализация из абстрактного класса.
    }
}
