/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.cardapiservice.repository;

import com.mycompany.cardapiservice.entity.Card;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author User
 */
public interface CardRepository extends JpaRepository<Card, Long>{
    
    @Query("SELECT c FROM Card c WHERE c.user.login = :userLogin")
    public Page<Card> getCardsByUserLogin(@Param("userLogin") String userLogin, Pageable pageable);
    
    @Query("SELECT c FROM Card c WHERE c.number = :number")
    public Optional<Card> getCardByNumber(@Param("number") String numberCard);
    
    @Query("SELECT c FROM Card c WHERE (:number IS NULL OR c.number = :number)"
            + " AND (:isBlocked IS NULL OR c.isBlocked = :isBlocked)"
            + " AND (:userLogin IS NULL OR c.user.login = :userLogin)"
            + " AND (:userName IS NULL OR c.user.name = :userName)"
            + " AND (:userSurname IS NULL OR c.user.surname = :userSurname)"
            + " AND (:userPatronymic IS NULL OR c.user.patronymic = :userPatronymic)")
    public Page<Card> getCardByUserData(
            @Param("number") String cardNumber,
            @Param("isBlocked") Boolean cardIsBlocked,
            @Param("userLogin") String userLogin,
            @Param("userName") String userName,
            @Param("userSurname") String userSurname,
            @Param("userPatronymic") String userPatronymic,
            Pageable pageable
    );
    
    @Query("SELECT c FROM Card c WHERE c.user.login = :userLogin AND c.id = :cardId")
    public Optional<Card> getCardByUserLoginAndCardId(@Param("userLogin") String userLogin, @Param("cardId") Long cardId);
}
