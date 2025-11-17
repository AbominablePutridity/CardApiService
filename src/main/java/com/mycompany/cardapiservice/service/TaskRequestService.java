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
    private OperationRepository operationRepository;
    private TaskStatusRepository taskStatusRepository;
    
    public TaskRequestService(
            TaskRequestRepository taskRequestRepository,
            OperationRepository operationRepository,
            TaskStatusRepository taskStatusRepository
    )
    {
        this.taskRequestRepository = taskRequestRepository;
        this.operationRepository = operationRepository;
        this.taskStatusRepository = taskStatusRepository;
    }
    
    public ResponseEntity<?> createTask(TaskRequestDto taskRequestDto, User currentUser)
    {
        TaskRequest newTaskRequest = new TaskRequest();
        //newTaskRequest.setId(taskRequestDto.getId());
        newTaskRequest.setDescription(taskRequestDto.getDescription());
        newTaskRequest.setUser(currentUser);
        newTaskRequest.setOperation(
                operationRepository.getOperationById(taskRequestDto.getOperationDto().getId())
        );
        newTaskRequest.setTaskStatus(
                taskStatusRepository.getTaskStatusById(taskRequestDto.getTaskStatusDto().getId())
        );

        taskRequestRepository.save(newTaskRequest);

        return ResponseEntity.ok(new TaskRequestDto(newTaskRequest));
    }
}
