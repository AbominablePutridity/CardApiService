package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.entity.Operation;

/**
 *
 */
public class OperationDto {
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
}
