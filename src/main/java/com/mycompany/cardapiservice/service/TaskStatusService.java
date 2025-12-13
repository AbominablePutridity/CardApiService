package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.TaskStatusDto;
import com.mycompany.cardapiservice.entity.TaskStatus;
import com.mycompany.cardapiservice.repository.TaskStatusRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class TaskStatusService extends ExtendedUniversalWriteEndpointsService<TaskStatus, TaskStatusDto, TaskStatus, Long, JpaRepository<TaskStatus, Long>>{
    public TaskStatusRepository taskStatusRepository;
    
    public TaskStatusService (TaskStatusRepository taskStatusRepository)
    {
        super(taskStatusRepository);
        
        this.taskStatusRepository = taskStatusRepository;
    }
    
    /**
     * Обновить валюту (заменить данные обьекта, или его целиком - в зависимости от isSaveByPart).
     * @param idTaskStatusForUpdate Id валюты для обновления.
     * @param refreshedTaskStatus Обьект валюты доля обновления.
     * @param isSaveByPart
     * true - обновить конкретное поле/поля в данном обьекте (не целый обьект - для PATCH);
     * false - обновить обьект целиком (для PUT).
     * @return Статус выполнения.
     */
    public ResponseEntity<?> refreshTaskStatus(Long idTaskStatusForUpdate, TaskStatusDto refreshedTaskStatus, boolean isSaveByPart)
    {
        try {
            TaskStatus taskStatus = taskStatusRepository.findById(idTaskStatusForUpdate).get();
        
            //обновляем все поля кроме ключей и возвращаем обьект с данными
            TaskStatus updatedTaskStatusObject = refreshedTaskStatus.toEntityWithFieldsCondition(taskStatus, isSaveByPart);
            
            taskStatusRepository.save(updatedTaskStatusObject);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: OperationService.refreshOperation() проверка полей вызвало исключение: " + t.getMessage());
            
            return ResponseEntity.badRequest()
               .body("Ошибка при обновлении статуса задачи: " + t.getMessage());
        }
        
        return ResponseEntity.ok("операция успешно обновлена!");
    }

    @Override
    protected TaskStatusDto toDto(TaskStatus obj)
    {
        return new TaskStatusDto(obj);
    }
}
