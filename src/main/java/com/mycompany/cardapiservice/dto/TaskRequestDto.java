package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.entity.TaskRequest;

/**
 *
 */
public class TaskRequestDto {
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
}
