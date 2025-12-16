package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.dto.interfaces.TransferableDtoToEntity;
import com.mycompany.cardapiservice.entity.StatusCard;

/**
 *
 */
public class StatusCardDto implements TransferableDtoToEntity<StatusCard>{
    private Long id;
    private String name;
    
    public StatusCardDto(){} // для сериализатора
    
    public StatusCardDto(StatusCard statusCard)
    {
        id = statusCard.getId();
        name = statusCard.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public StatusCard toEntity() {
        StatusCard statusCard = new StatusCard();
        
        return setDataInObject(statusCard);
    }

    /**
     * Выношу логику в отдельный полноценный метод для того чтобы не было повторений кода
     * @param statusCard Обьект сущности, в который сохраняем данные из этого DTO класса
     * @return Обьект сущности с данными
     */
    private StatusCard setDataInObject(StatusCard statusCard) {
        statusCard.setName(name);
        
        return statusCard;
    }
    
    /**
     * Метод для обновления полей сущности из DTO класса
     * @param objectForUpdate Обьект сущности, которую нужно обновить
     * @param isSaveByPart
     * @return 
     */
    @Override
    public StatusCard toEntityWithFieldsCondition(StatusCard objectForUpdate, Boolean isSaveByPart) {
        if (isSaveByPart) {
            if(name != null)
            {
                objectForUpdate.setName(name);
            }
        } else {
            objectForUpdate = setDataInObject(objectForUpdate);
        }
        
        return objectForUpdate;
    }
}
