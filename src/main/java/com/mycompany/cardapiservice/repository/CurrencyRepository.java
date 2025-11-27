package com.mycompany.cardapiservice.repository;

import com.mycompany.cardapiservice.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 */
public interface CurrencyRepository extends JpaRepository<Currency, Long>{
    
}
