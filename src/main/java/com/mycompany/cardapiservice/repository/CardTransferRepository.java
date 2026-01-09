package com.mycompany.cardapiservice.repository;

import com.mycompany.cardapiservice.entity.Card;
import com.mycompany.cardapiservice.entity.CardTransfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 */
public interface CardTransferRepository extends JpaRepository<CardTransfer, Long>{
    
    @Query("SELECT ct FROM CardTransfer ct WHERE ct.sender = :cardObj OR ct.receiver = :cardObj")
    public Page<CardTransfer> getTransactionsByCardId(@Param("cardObj") Card cardObj, Pageable pageable);
}
