package com.mycompany.cardapiservice.service;

import com.mycompany.cardapiservice.dto.interfaces.TransferableDtoToEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

/**
 *Абстрактный класс с общей логикой на стандартные запросы c сохранением данных в бд.
 * @param <OBJ> Класс для типа обьекта сущности.
 * @param <RET> Класс для типа обьекта возвращаемого значения.
 * @param <INP> Класс для типа обьекта передачи в метод.
 * @param <ID> Класс для типа первичного идентификатора (id) в сущностях.
 * @param <R> Класс для типа репозитория сущности.
 */
public abstract class ExtendedUniversalWriteEndpointsService<
    OBJ, 
    RET extends TransferableDtoToEntity<OBJ>,  // ДОБАВЛЕНО ограничение!
    INP, 
    ID, 
    R extends JpaRepository<OBJ, ID>
>
extends BasedUniversalEndpointsService<OBJ, RET, INP, ID, R> {  // передаем все параметры предку
    
    // Конструктор должен вызывать конструктор родителя
    public ExtendedUniversalWriteEndpointsService(R repository) {
        super(repository);
    }
    
    /**
     * Универсальный метод сохранение обьекта в бд.
     * (для POST-запросов).
     * @param newObject Обьект, который хотим сохранить в бд.
     * @return Статус выполнения.
     */
    public ResponseEntity<?> setObject(RET newObject)
    {
        try {
            repository.save(newObject.toEntity());
        } catch(Throwable t) {
            System.err.println("ОШИБКА: UniversalEndpointsService.setObject(), при сохранении сущности обьекта в бд - " + t.getMessage());
            
            return ResponseEntity.badRequest().body(t.getMessage());
        }
        
        return ResponseEntity.ok().body("Новый обьект был успешно сохранен в бд!");
    }
}
