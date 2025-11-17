package com.mycompany.cardapiservice.repository;

import com.mycompany.cardapiservice.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 */
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    
    @Query("SELECT ts FROM TaskStatus ts WHERE ts.id = :id")
    public TaskStatus getTaskStatusById(@Param("id") Long id);
}
