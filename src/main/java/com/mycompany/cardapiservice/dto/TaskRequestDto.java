package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.dto.interfaces.TransferableDtoToEntity;
import com.mycompany.cardapiservice.entity.TaskRequest;

/**
 *
 */
public class TaskRequestDto implements TransferableDtoToEntity<TaskRequest>{
    private Long id;
    private String description;
    private UserDto userDto;
    private OperationDto operationDto;
    private TaskStatusDto taskStatusDto;
    
    public TaskRequestDto(){} // для сериализатора
    
    public TaskRequestDto(TaskRequest taskRequest)
    {
        id = taskRequest.getId();
        description = taskRequest.getDescription();
        userDto = new UserDto(taskRequest.getUser());
        operationDto = new OperationDto(taskRequest.getOperation());
        taskStatusDto = new TaskStatusDto(taskRequest.getTaskStatus());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public OperationDto getOperationDto() {
        return operationDto;
    }

    public void setOperationDto(OperationDto operationDto) {
        this.operationDto = operationDto;
    }

    public TaskStatusDto getTaskStatusDto() {
        return taskStatusDto;
    }

    public void setTaskStatusDto(TaskStatusDto taskStatusDto) {
        this.taskStatusDto = taskStatusDto;
    }
    
    /**
     * Перевод DTO класса в сущность
     * тут добавляем аттрибуты без ключей - ключи добавляем в сервисах.
     * (для унификации POST запроса, конкретная реализация перевода в сущность)
     * @return Обьект новой сущности (для сохранения в бд)
     */
    @Override
    public TaskRequest toEntity() {
        TaskRequest taskRequest = new TaskRequest();
        
        return setDataInObject(taskRequest);
    }

    /**
     * Выношу логику в отдельный полноценный метод для того чтобы не было повторений кода
     * @param cardObject Обьект сущности, в который сохраняем данные из этого DTO класса
     * @return Обьект сущности с данными
     */
    private TaskRequest setDataInObject(TaskRequest taskRequestObject) {
        taskRequestObject.setDescription(description);
        
        return taskRequestObject;
    }
    
    /**
     * Метод для обновления полей сущности из DTO класса
     * @param objectForUpdate Обьект сущности, которую нужно обновить
     * @param isSaveByPart
     * @return 
     */
    @Override
    public TaskRequest toEntityWithFieldsCondition(TaskRequest objectForUpdate, Boolean isSaveByPart) {
        if (isSaveByPart) {
            if(description != null)
            {
                objectForUpdate.setDescription(description);
            }
        } else {
            objectForUpdate = setDataInObject(objectForUpdate);
        }
        
        return objectForUpdate;
    }
    
}
