package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.dto.interfaces.TransferableDtoToEntity;
import com.mycompany.cardapiservice.entity.Currency;

/**
 *
 */
public class CurrencyDto implements TransferableDtoToEntity<Currency>{
    private Long id;
    private String sign;
    private String name;
    
    public CurrencyDto() {} // для сериализатора
    
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
    
    /**
     * Перевод DTO класса в сущность
     * (для унификации POST запроса, конкретная реализация перевода в сущность)
     * @return Обьект новой сущности (для сохранения в бд)
     */
    @Override
    public Currency toEntity() {
        Currency currency = new Currency();
        
        return setDataInObject(currency);
    }
    
    /**
     * Выношу логику в отдельный полноценный метод для того чтобы не было повторений кода
     * @param cardObject Обьект сущности, в который сохраняем данные из этого DTO класса
     * @return Обьект сущности с данными
     */
    private Currency setDataInObject(Currency currency) {
        currency.setName(name);
        currency.setSign(sign);
        
        return currency;
    }

    /**
     * Метод для обновления полей сущности из DTO класса
     * @param objectForUpdate Обьект сущности, которую нужно обновить
     * @param isSaveByPart
     * @return 
     */
    @Override
    public Currency toEntityWithFieldsCondition(Currency objectForUpdate, Boolean isSaveByPart) {
        if (isSaveByPart) {
            if(name != null)
            {
                objectForUpdate.setName(name);
            }

            if(sign != null)
            {
                objectForUpdate.setSign(sign);
            }
        } else {
            objectForUpdate = setDataInObject(objectForUpdate);
        }
        
        return objectForUpdate;
    }
}
