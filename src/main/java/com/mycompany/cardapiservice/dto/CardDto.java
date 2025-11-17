package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.entity.Card;
import java.time.LocalDate;

/**
 *
 */
public class CardDto {
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
}
