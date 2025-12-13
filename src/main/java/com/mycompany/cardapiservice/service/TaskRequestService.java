package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.TaskRequestDto;
import com.mycompany.cardapiservice.entity.TaskRequest;
import com.mycompany.cardapiservice.entity.User;
import com.mycompany.cardapiservice.repository.OperationRepository;
import com.mycompany.cardapiservice.repository.TaskRequestRepository;
import com.mycompany.cardapiservice.repository.TaskStatusRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class TaskRequestService {
    private TaskRequestRepository taskRequestRepository;
    private OperationService operationService;
    private TaskStatusService taskStatusService;
    
    public TaskRequestService(
            TaskRequestRepository taskRequestRepository,
            OperationService operationService,
            TaskStatusService taskStatusService
    )
    {
        this.taskRequestRepository = taskRequestRepository;
        this.operationService = operationService;
        this.taskStatusService = taskStatusService;
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
}
