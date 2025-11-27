package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.entity.StatusCard;
import com.mycompany.cardapiservice.repository.StatusCardRepository;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class StatusCardService {
    private StatusCardRepository statusCardRepository;
    
    public StatusCardService(StatusCardRepository statusCardRepository)
    {
        this.statusCardRepository = statusCardRepository;
    }
    
    /**
     * Взять статус карты по id.
     * @param id Id статуса карты.
     * @return Обьект статуса карты.
     */
    public StatusCard getStatusCardById(Long id)
    {
        return statusCardRepository.findById(id).get();
    }
}
