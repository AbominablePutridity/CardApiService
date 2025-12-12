package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.OperationDto;
import com.mycompany.cardapiservice.entity.Operation;
import com.mycompany.cardapiservice.repository.OperationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OperationService extends ExtendedUniversalWriteEndpointsService<Operation, OperationDto, Operation, Long, JpaRepository<Operation, Long>> {
    private OperationRepository operationRepository;
    
    public OperationService(
            JpaRepository<Operation, Long> repository,
            OperationRepository operationRepository
    )
    {
        super(repository);
        
        this.operationRepository = operationRepository;
    }
    
    /**
     * Обновить валюту (заменить данные обьекта, или его целиком - в зависимости от isSaveByPart).
     * @param idOperationForUpdate Id валюты для обновления.
     * @param refreshedOperation Обьект валюты доля обновления.
     * @param isSaveByPart
     * true - обновить конкретное поле/поля в данном обьекте (не целый обьект - для PATCH);
     * false - обновить обьект целиком (для PUT).
     * @return Статус выполнения.
     */
    public ResponseEntity<?> refreshOperation(Long idOperationForUpdate, OperationDto refreshedOperation, boolean isSaveByPart)
    {
        try {
            Operation operation = operationRepository.findById(idOperationForUpdate).get();
        
            //обновляем все поля кроме ключей и возвращаем обьект с данными
            Operation updatedOperationObject = refreshedOperation.toEntityWithFieldsCondition(operation, isSaveByPart);
            
            operationRepository.save(updatedOperationObject);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: OperationService.refreshOperation() проверка полей вызвало исключение: " + t.getMessage());
            
            return ResponseEntity.badRequest()
               .body("Ошибка при обновлении валюты: " + t.getMessage());
        }
        
        return ResponseEntity.ok("операция успешно обновлена!");
    }
    
    @Override
    protected OperationDto toDto(Operation obj) {
        return new OperationDto(obj);
    }
}
