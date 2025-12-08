package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.CurrencyDto;
import com.mycompany.cardapiservice.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CurrencyService extends UniversalEndpointsService<Currency, CurrencyDto, Currency, Long, JpaRepository<Currency, Long>>{

    public CurrencyService(JpaRepository<Currency, Long> repository) {
        super(repository);
    }

    @Override
    protected CurrencyDto toDto(Currency obj) {
        return new CurrencyDto(obj);  //конкретная реализация
    }
}
