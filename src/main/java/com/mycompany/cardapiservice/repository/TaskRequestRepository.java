package com.mycompany.cardapiservice.repository;

import com.mycompany.cardapiservice.entity.TaskRequest;
import com.mycompany.cardapiservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 */
public interface TaskRequestRepository extends JpaRepository<TaskRequest, Long>{

    public TaskRequest save(TaskRequest taskRequest);
    
    @Query("SELECT tr FROM TaskRequest tr WHERE tr.user = :user")
    public Page<TaskRequest> getTasksForCurrentUser(@Param("user") User user, Pageable pageable);
}
