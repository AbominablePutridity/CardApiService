package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.dto.interfaces.TransferableDtoToEntity;
import com.mycompany.cardapiservice.entity.TaskStatus;

/**
 *
 */
public class TaskStatusDto implements TransferableDtoToEntity<TaskStatus>{
    private Long id;
    private String name;
    
    public TaskStatusDto(){} // для сериализатора
    
    public TaskStatusDto(TaskStatus taskStatus)
    {
        id = taskStatus.getId();
        name = taskStatus.getName();
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
    public TaskStatus toEntity() {
        TaskStatus taskStatus = new TaskStatus();
        
        return setDataInObject(taskStatus);
    }
    
    /**
     * Выношу логику в отдельный полноценный метод для того чтобы не было повторений кода
     * @param taskStatus Обьект сущности, в который сохраняем данные из этого DTO класса
     * @return Обьект сущности с данными
     */
    private TaskStatus setDataInObject(TaskStatus taskStatus) {
        taskStatus.setName(name);
        
        return taskStatus;
    }

    /**
     * Метод для обновления полей сущности из DTO класса
     * @param objectForUpdate Обьект сущности, которую нужно обновить
     * @param isSaveByPart
     * @return 
     */
    @Override
    public TaskStatus toEntityWithFieldsCondition(TaskStatus objectForUpdate, Boolean isSaveByPart) {
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
