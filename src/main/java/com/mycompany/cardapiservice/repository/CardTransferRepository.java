package com.mycompany.cardapiservice.repository;

import com.mycompany.cardapiservice.entity.CardTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 */
public interface CardTransferRepository extends JpaRepository<CardTransfer, Long>{
    
}
