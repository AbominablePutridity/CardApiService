package com.mycompany.cardapiservice.dto.interfaces;

/**
 * Контракт между всеми DTO классами для реализаций метода перевода
 * классов DTO в классы сущностей
 * @param <E> Сущность (обьект сущности)
 */
public interface TransferableDtoToEntity<E> {
    E toEntity();
    E toEntityWithFieldsCondition(E objectForUpdate, Boolean isSaveByPart);
}
