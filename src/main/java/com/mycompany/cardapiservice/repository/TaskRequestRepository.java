package com.mycompany.cardapiservice.repository;

import com.mycompany.cardapiservice.entity.TaskRequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 */
public interface TaskRequestRepository extends JpaRepository<TaskRequest, Long>{

    public TaskRequest save(TaskRequest taskRequest);
}
