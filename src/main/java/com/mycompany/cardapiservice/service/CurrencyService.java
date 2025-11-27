package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.entity.Currency;
import com.mycompany.cardapiservice.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CurrencyService {
    private CurrencyRepository currencyRepository;
    
    public CurrencyService(CurrencyRepository currencyRepository)
    {
        this.currencyRepository = currencyRepository;
    }
    
    /**
     * Взять валюту по ее id.
     * @param id Id валюты.
     * @return Обьект валюты.
     */
    public Currency getCurrencyById(Long id)
    {
        return currencyRepository.findById(id).get();
    }
}
