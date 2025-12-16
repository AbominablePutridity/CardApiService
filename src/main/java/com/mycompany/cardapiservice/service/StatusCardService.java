package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.StatusCardDto;
import com.mycompany.cardapiservice.entity.StatusCard;
import com.mycompany.cardapiservice.repository.StatusCardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class StatusCardService extends ExtendedUniversalWriteEndpointsService<StatusCard, StatusCardDto, StatusCard, Long, JpaRepository<StatusCard, Long>> {
    private StatusCardRepository statusCardRepository;
    
    public StatusCardService(StatusCardRepository statusCardRepository)
    {
        super(statusCardRepository);
        
        this.statusCardRepository = statusCardRepository;
    }
    
    /**
     * Обновить статус карты (заменить данные обьекта, или его целиком - в зависимости от isSaveByPart).
     * @param idTaskStatusForUpdate Id валюты для обновления.
     * @param refreshedStatusCardDto Обьект статуса карты доля обновления.
     * @param isSaveByPart
     * true - обновить конкретное поле/поля в данном обьекте (не целый обьект - для PATCH);
     * false - обновить обьект целиком (для PUT).
     * @return Статус выполнения.
     */
    public ResponseEntity<?> refreshStatusCard(Long idTaskStatusForUpdate, StatusCardDto refreshedStatusCardDto, boolean isSaveByPart)
    {
        try {
            StatusCard statusCard = statusCardRepository.findById(idTaskStatusForUpdate).get();
        
            //обновляем все поля кроме ключей и возвращаем обьект с данными
            StatusCard updatedStatusCardObject = refreshedStatusCardDto.toEntityWithFieldsCondition(statusCard, isSaveByPart);
            
            statusCardRepository.save(updatedStatusCardObject);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: OperationService.refreshOperation() проверка полей вызвало исключение: " + t.getMessage());
            
            return ResponseEntity.badRequest()
               .body("Ошибка при обновлении статуса задачи: " + t.getMessage());
        }
        
        return ResponseEntity.ok("операция успешно обновлена!");
    }

    @Override
    protected StatusCardDto toDto(StatusCard obj) {
        return new StatusCardDto(obj);
    }
}
