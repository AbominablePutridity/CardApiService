package com.mycompany.cardapiservice.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

/**
 * Абстрактный класс с общей логикой на стандартные запросы c чтением данных из бд и удалением.
 * @param <OBJ> Класс для типа обьекта сущности.
 * @param <RET> Класс для типа обьекта возвращаемого значения.
 * @param <INP> Класс для типа обьекта передачи в метод.
 * @param <ID> Класс для типа первичного идентификатора (id) в сущностях.
 * @param <R> Класс для типа репозитория сущности.
 */
public abstract class BasedUniversalEndpointsService<OBJ, RET, INP, ID, R extends JpaRepository<OBJ, ID>> {
    protected R repository;
    
    public BasedUniversalEndpointsService(R repository)
    {
        this.repository = repository;
    }
    
    /**
     * Берем все обьекты из бд по пагинации.
     * @param pageable Количество страниц.
     * @return Коллекцию обьектов (в DTO формате).
     */
    public List<RET> prepareObjects(Pageable pageable)
    {
        List<OBJ> page = repository.findAll(pageable).getContent();

        return page.stream().map((obj) -> {
            return toDto(obj);
        }).collect(Collectors.toList());
    }
    
    /**
     * Возвращает обьект из БД по его Id.
     * @param objId Id код для удаления записи.
     * @return Обьект с данными.
     */
    public OBJ getObjectById(ID objId)
    {
        return repository.findById(objId).get();
    }
    
    /**
     * Универсальный метод для удаления записи по id.
     * @param objId Id код для удаления записи.
     * @return Статус выполнения.
     */
    public ResponseEntity<?> deleteObject(ID objId)
    {
        try {
            OBJ obj = repository.findById(objId).get();

            repository.delete(obj);
            
            return ResponseEntity.ok("Запись была успешно удалена!");
        }
        catch (Throwable t) {
            System.err.println("ОШИБКА: UserService.deleteUser() - при удалении пользователя: " + t.getMessage());
            
            return ResponseEntity.badRequest()
                    .body("ОШИБКА: UserService.deleteUser() - при удалении пользователя: " + t.getMessage());
        }
    }
    
    // Абстрактный метод - реализуется в каждом наследнике
    //(отвечает за преобразование классов сущностей в обьекты DTO для вывода)
    protected abstract RET toDto(OBJ obj);
}
