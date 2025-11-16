package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.entity.Currency;

/**
 *
 */
public class CurrencyDto {
    private Long id;
    private String sign;
    private String name;
    
    public CurrencyDto(Currency currency) {
        id = currency.getId();
        sign = currency.getSign();
        name = currency.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
