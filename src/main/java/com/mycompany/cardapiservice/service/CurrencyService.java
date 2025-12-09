package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.CurrencyDto;
import com.mycompany.cardapiservice.entity.Currency;
import com.mycompany.cardapiservice.repository.CurrencyRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CurrencyService extends UniversalEndpointsService<Currency, CurrencyDto, Currency, Long, JpaRepository<Currency, Long>>{
    private CurrencyRepository currencyRepository;
    
    public CurrencyService(
            JpaRepository<Currency, Long> repository,
            CurrencyRepository currencyRepository
    )
    {
        super(repository);
        
        this.currencyRepository = currencyRepository;
    }
    
    /**
     * Сохранить обьект валюты в БД.
     * @param newCurrency Обьект валюты для сохранения.
     * @return Статус выполнения.
     */
    public ResponseEntity<?> setCurrency(CurrencyDto newCurrency)
    {
        try {
        Currency currency = new Currency();
        currency.setName(newCurrency.getName());
        currency.setSign(newCurrency.getSign());
        
        currencyRepository.save(currency);
        } catch(Throwable t) {
            System.err.println("ОШИБКА: Cyrrency.setCurrency(), при сохранении сущности обьекта в бд - " + t.getMessage());
            
            return ResponseEntity.badRequest().body(t.getMessage());
        }
        
        return ResponseEntity.ok().body("Новая валюта была успешно сохранена!");
    }
    
     /**
     * Обновить валюту (заменить данные обьекта, или его целиком - в зависимости от isSaveByPart).
     * @param idCurrencyForUpdate Id валюты для обновления.
     * @param refreshedCurrency Обьект валюты доля обновления.
     * @param isSaveByPart
     * true - обновить конкретное поле/поля в данном обьекте (не целый обьект - для PATCH);
     * false - обновить обьект целиком (для PUT).
     * @return Статус выполнения.
     */
    public ResponseEntity<?> refreshCurrency(Long idCurrencyForUpdate, CurrencyDto refreshedCurrency, boolean isSaveByPart)
    {
        try {
            Currency currency = currencyRepository.findById(idCurrencyForUpdate).get();
        
            if (isSaveByPart) {
                if(refreshedCurrency.getName()!= null)
                {
                    currency.setName(refreshedCurrency.getName());
                }

                if(refreshedCurrency.getSign()!= null)
                {
                    currency.setSign(refreshedCurrency.getSign());
                }
            } else {
                currency.setName(refreshedCurrency.getName());
                currency.setSign(refreshedCurrency.getSign());
            }
            currencyRepository.save(currency);
        } catch (Throwable t)
        {
            System.err.println("ОШИБКА: CurrencyService.refreshCurrency() проверка полей вызвало исключение: " + t.getMessage());
            
            return ResponseEntity.badRequest()
               .body("Ошибка при обновлении валюты: " + t.getMessage());
        }
        
        return ResponseEntity.ok("валюта успешно обновлена!");
    }

    @Override
    protected CurrencyDto toDto(Currency obj) {
        return new CurrencyDto(obj);  //конкретная реализация
    }
}
