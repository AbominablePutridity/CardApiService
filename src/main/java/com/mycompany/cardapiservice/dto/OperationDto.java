package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.dto.interfaces.TransferableDtoToEntity;
import com.mycompany.cardapiservice.entity.Operation;

/**
 *
 */
public class OperationDto implements TransferableDtoToEntity<Operation>{
    private Long id;
    private String name;
    
    public OperationDto(){} // для сериализатора
    
    public OperationDto(Operation operation)
    {
        id = operation.getId();
        name = operation.getName();
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
    public Operation toEntity() {
        Operation operation = new Operation();
        
        return setDataInObject(operation);
    }
    
    /**
     * Выношу логику в отдельный полноценный метод для того чтобы не было повторений кода
     * @param operation Обьект сущности, в который сохраняем данные из этого DTO класса
     * @return Обьект сущности с данными
     */
     private Operation setDataInObject(Operation operation) {
        operation.setName(name);
        
        return operation;
    }

     /**
     * Метод для обновления полей сущности из DTO класса
     * @param objectForUpdate Обьект сущности, которую нужно обновить
     * @param isSaveByPart
     * @return 
     */
    @Override
    public Operation toEntityWithFieldsCondition(Operation objectForUpdate, Boolean isSaveByPart) {
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
