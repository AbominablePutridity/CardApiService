package com.mycompany.cardapiservice.repository;

import com.mycompany.cardapiservice.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 */
public interface OperationRepository extends JpaRepository<Operation, Long>{
    
    @Query("SELECT o FROM Operation o WHERE o.id = :id")
    public Operation getOperationById(@Param("id") Long id);
}
