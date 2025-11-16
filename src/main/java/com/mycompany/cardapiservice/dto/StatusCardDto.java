package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.entity.StatusCard;

/**
 *
 */
public class StatusCardDto {
    private Long id;
    private String name;
    
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
}
