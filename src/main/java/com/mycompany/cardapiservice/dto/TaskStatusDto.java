package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.entity.TaskStatus;

/**
 *
 */
public class TaskStatusDto {
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
}
