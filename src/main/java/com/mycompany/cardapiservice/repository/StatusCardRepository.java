package com.mycompany.cardapiservice.repository;

import com.mycompany.cardapiservice.entity.StatusCard;
import org.springframework.data.jpa.repository.JpaRepository;

/**
*
*/
public interface StatusCardRepository extends JpaRepository<StatusCard, Long>{
    
}
