package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.dto.interfaces.TransferableDtoToEntity;
import com.mycompany.cardapiservice.entity.Card;
import java.time.LocalDate;

/**
 *
 */
public class CardDto implements TransferableDtoToEntity<Card> {
    private Long id;
    private String number;
    private LocalDate validityPeriod;
    private Long balance;
    private UserDto userDto;
    private StatusCardDto statusCardDto;
    private CurrencyDto currencyDto;
    private boolean isBlocked;
    
    public CardDto(){} // для сериализатора
    
    public CardDto(Card card)
    {
        id = card.getId();
        number = card.getNumber();
        validityPeriod = card.getValidityPeriod();
        balance = card.getBalance();
        userDto = new UserDto(card.getUser());
        statusCardDto = new StatusCardDto(card.getStatusCard());
        currencyDto = new CurrencyDto(card.getCurrency());
        isBlocked = card.getIsIsBlocked();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(LocalDate validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public StatusCardDto getStatusCardDto() {
        return statusCardDto;
    }

    public void setStatusCardDto(StatusCardDto statusCardDto) {
        this.statusCardDto = statusCardDto;
    }

    public CurrencyDto getCurrencyDto() {
        return currencyDto;
    }

    public void setCurrencyDto(CurrencyDto currencyDto) {
        this.currencyDto = currencyDto;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    /**
     * Перевод DTO класса в сущность
     * тут добавляем аттрибуты без ключей - ключи добавляем в сервисах.
     * (для унификации POST запроса, конкретная реализация перевода в сущность)
     * @return Обьект новой сущности (для сохранения в бд)
     */
    @Override
    public Card toEntity() {
        Card card = new Card();
        
        return setDataInObject(card);
    }
    
    /**
     * Выношу логику в отдельный полноценный метод для того чтобы не было повторений кода
     * @param cardObject Обьект сущности, в который сохраняем данные из этого DTO класса
     * @return Обьект сущности с данными
     */
    private Card setDataInObject(Card cardObject) {
        cardObject.setNumber(number);
        cardObject.setValidityPeriod(validityPeriod);
        cardObject.setBalance(balance);
        cardObject.setIsBlocked(isBlocked);
        
        return cardObject;
    }
    
    /**
     * Метод для обновления полей сущности из DTO класса
     * (обновление полей без ключей, ключи обновляются в сервисах)
     * @param cardForUpdate Обьект сущности, которую нужно обновить
     * @param isSaveByPart
     * @return 
     */
    @Override
    public Card toEntityWithFieldsCondition(Card cardForUpdate, Boolean isSaveByPart) {
        if (isSaveByPart) {
            if(number != null)
            {
                cardForUpdate.setNumber(number);
            }
            
            if(validityPeriod!= null)
            {
                cardForUpdate.setValidityPeriod(validityPeriod);
            }
            
            if(balance != null)
            {
                cardForUpdate.setBalance(balance);
            }
            
            cardForUpdate.setIsBlocked(isBlocked);
        } else {           
            cardForUpdate = setDataInObject(cardForUpdate);
        }
        
        return cardForUpdate;
    }
}
