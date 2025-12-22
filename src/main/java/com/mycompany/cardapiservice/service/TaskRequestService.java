package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.TaskRequestDto;
import com.mycompany.cardapiservice.dto.interfaces.TransferableDtoToEntity;
import com.mycompany.cardapiservice.entity.TaskRequest;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.repository.OperationRepository;
import com.mycompany.cardapiservice.repository.TaskRequestRepository;
import com.mycompany.cardapiservice.repository.TaskStatusRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class TaskRequestService extends ExtendedUniversalWriteEndpointsService<TaskRequest, TaskRequestDto, TaskRequest, Long, JpaRepository<TaskRequest, Long>>{
    private TaskRequestRepository taskRequestRepository;
    private OperationService operationService;
    private TaskStatusService taskStatusService;
    private UserService userService;
    
    public TaskRequestService(
            TaskRequestRepository taskRequestRepository,
            OperationService operationService,
            TaskStatusService taskStatusService,
            UserService userService
    )
    {
        super(taskRequestRepository);
        this.taskRequestRepository = taskRequestRepository;
        this.operationService = operationService;
        this.taskStatusService = taskStatusService;
        this.userService = userService;
    }
    
    public ResponseEntity<?> createTask(TaskRequestDto taskRequestDto, User currentUser)
    {
        TaskRequest newTaskRequest = new TaskRequest();
        //newTaskRequest.setId(taskRequestDto.getId());
        newTaskRequest.setDescription(taskRequestDto.getDescription());
        newTaskRequest.setUser(currentUser);
        newTaskRequest.setOperation(
                operationService.getObjectById(taskRequestDto.getOperationDto().getId())
        );
        newTaskRequest.setTaskStatus(
                taskStatusService.getObjectById(taskRequestDto.getTaskStatusDto().getId())
        );

        taskRequestRepository.save(newTaskRequest);

        return ResponseEntity.ok(new TaskRequestDto(newTaskRequest));
    }

    /**
     * Обновить пользователя (заменить данные обьекта, или его целиком - в зависимости от isSaveByPart).
     * @param idTaskRequestForUpdate Id задачи для обновления.
     * @param refreshedTaskRequest Обьект задачи для обновления.
     * @param isSaveByPart
     * true - обновить конкретное поле/поля в данном обьекте (не целый обьект - для PATCH);
     * false - обновить обьект целиком (для PUT).
     * @return Статус выполнения.
     */
    public ResponseEntity<?> refreshTaskRequest(Long idTaskRequestForUpdate, TaskRequestDto refreshedTaskRequest, boolean isSaveByPart)
    {
        try {
            TaskRequest taskRequest = taskRequestRepository.findById(idTaskRequestForUpdate).get();
        
            //обновляем все поля кроме ключей и возвращаем обьект с данными
            TaskRequest updatedTaskRequestObject = refreshedTaskRequest.toEntityWithFieldsCondition(taskRequest, isSaveByPart);
            
            if(isSaveByPart) {
                if(refreshedTaskRequest.getOperationDto() != null) {
                    updatedTaskRequestObject.setOperation(operationService.getObjectById(refreshedTaskRequest.getOperationDto().getId()));
                }
                
                if(refreshedTaskRequest.getTaskStatusDto() != null) {
                    updatedTaskRequestObject.setTaskStatus(taskStatusService.getObjectById(refreshedTaskRequest.getTaskStatusDto().getId()));
                }
                
                if(refreshedTaskRequest.getUserDto() != null) {
                    updatedTaskRequestObject.setUser(userService.getObjectById(refreshedTaskRequest.getUserDto().getId()));
                }
            } else {
                //сохраняем все ключи в обьект из переданного обьекта клиентом, если у нас сохранение обьекта целиком (isSaveByPart == false - для PUT запросов)
                updatedTaskRequestObject.setOperation(operationService.getObjectById(refreshedTaskRequest.getOperationDto().getId()));
                updatedTaskRequestObject.setTaskStatus(taskStatusService.getObjectById(refreshedTaskRequest.getTaskStatusDto().getId()));
                updatedTaskRequestObject.setUser(userService.getObjectById(refreshedTaskRequest.getUserDto().getId()));
            }
            
            taskRequestRepository.save(updatedTaskRequestObject);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: UserService.refreshUser() проверка полей вызвало исключение: " + t.getMessage());
            
            return ResponseEntity.badRequest()
               .body("Ошибка при обновлении пользователя: " + t.getMessage());
        }
        
        return ResponseEntity.ok("пользователь успешно обновлен!");
    }
    
    /**
     * Создание заявки пользователем для админа.
     * @param taskRequestDto Данные создаваемой заявки.
     * @return Статус выполнения.
     */
    public ResponseEntity<?> setTaskRequestForAdmin(TaskRequestDto taskRequestDto)
    {
        try {
            TaskRequest newTaskRequest = taskRequestDto.toEntity();
            newTaskRequest.setUser(userService.getObjectById(taskRequestDto.getUserDto().getId()));
            newTaskRequest.setOperation(operationService.getObjectById(taskRequestDto.getOperationDto().getId()));
            newTaskRequest.setTaskStatus(taskStatusService.getObjectById(taskRequestDto.getTaskStatusDto().getId()));
            newTaskRequest.setDescription(taskRequestDto.getDescription());
            
            taskRequestRepository.save(newTaskRequest);
            
            return ResponseEntity.ok("Обьект задачи успешно сохранен!");
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: CardService.setCardForUser() - не удалось сохранить обьект новой задачи: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("не удалось сохранить обьект новой задачи: " + t.getMessage());
        }
    }
    
    @Override
    protected TaskRequestDto toDto(TaskRequest obj) {
        return new TaskRequestDto(obj);
    }
}
