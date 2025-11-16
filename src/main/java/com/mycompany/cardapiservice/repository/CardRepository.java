/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.cardapiservice.repository;

import com.mycompany.cardapiservice.entity.Card;
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
}
